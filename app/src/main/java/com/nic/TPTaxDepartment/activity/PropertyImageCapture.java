package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.android.volley.VolleyError;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.databinding.PropertyImageCaptureBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.SpinnerAdapter;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PropertyImageCapture extends AppCompatActivity implements Api.ServerResponseListener{
    private PropertyImageCaptureBinding propertyImageCaptureBinding;
    private PrefManager prefManager;
    ArrayList<CommonModel> wards ;
    ArrayList<CommonModel> streets;
    ArrayList<CommonModel> assessment;
    ArrayList<CommonModel> selectedStreets;

    HashMap<String,String> spinnerMapStreets;
    HashMap<String,String> spinnerMapWard;
    HashMap<String,String> spinnerMapAssessment;
    String selectedWardId="";
    String selectedWardName="";
    String selectedStreetId="";
    String selectedStreetName="";
    String selectedAssessmentId="";
    String selectedAssessmentNo="";
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        propertyImageCaptureBinding = DataBindingUtil.setContentView(this, R.layout.property_image_capture);
        propertyImageCaptureBinding.setActivity(this);
        context=this;
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        this.getWindow().setStatusBarColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

        prefManager = new PrefManager(this);
        prefManager.setPropertyImage("");
        prefManager.setPropertyImageLat("");
        prefManager.setPropertyImageLong("");
        LoadWardSpinner();

        propertyImageCaptureBinding.wardNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String ward = parent.getSelectedItem().toString();
                String wardId = spinnerMapWard.get(parent.getSelectedItemPosition());
                // iterate each entry of hashmap
                for(Map.Entry<String, String> entry: spinnerMapWard.entrySet()) {
                    // if give value is equal to value from entry
                    // print the corresponding key
                    if(entry.getValue() == ward) {
                        wardId=entry.getKey();
                        break;
                    }
                }
                selectedWardName=ward;
                selectedWardId=wardId;
                System.out.println("selectedWardId >> "+selectedWardId);
                System.out.println("selectedWardName >> "+selectedWardName);
                if(selectedWardId != null  && !selectedWardId.isEmpty() &&
                        !selectedWardName.equals(context.getResources().getString(R.string.select_Ward))){

                    LoadStreetSpinner(selectedWardId);
                }else {
                    propertyImageCaptureBinding.streetsName.setAdapter(null);
                    selectedStreetId="0";
                    selectedStreetName="";

                    propertyImageCaptureBinding.assessmentId.setAdapter(null);
                    selectedAssessmentId="0";
                    selectedAssessmentNo="";

                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        propertyImageCaptureBinding.assessmentId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String assessment_name = parent.getSelectedItem().toString();
                String assessment_id = spinnerMapAssessment.get(parent.getSelectedItemPosition());
                // iterate each entry of hashmap
                for(Map.Entry<String, String> entry: spinnerMapAssessment.entrySet()) {
                    // if give value is equal to value from entry
                    // print the corresponding key
                    if(entry.getValue() == assessment_name) {
                        assessment_id=entry.getKey();
                        break;
                    }
                }
                selectedAssessmentNo=assessment_name;
                selectedAssessmentId=assessment_id;
                System.out.println("selectedAssessmentId >> "+selectedAssessmentId);
                System.out.println("selectedAssessmentNo >> "+selectedAssessmentNo);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        propertyImageCaptureBinding.streetsName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String street = parent.getSelectedItem().toString();
                String streetCode = "";
                // iterate each entry of hashmap
                for(Map.Entry<String, String> entry: spinnerMapStreets.entrySet()) {
                    // if give value is equal to value from entry
                    // print the corresponding key
                    if(entry.getValue() == street) {
                        streetCode=entry.getKey();
                        break;
                    }
                }
                selectedStreetId=streetCode;
                selectedStreetName=street;
                if(selectedStreetId != null  && !selectedStreetId.isEmpty() &&
                        !selectedStreetName.equals(context.getResources().getString(R.string.select_Street))){

                    getAssessmentList();
                }else {
                    propertyImageCaptureBinding.assessmentId.setAdapter(null);
                    selectedAssessmentId="0";
                    selectedAssessmentNo="";

                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    private void LoadAssessmentSpinner(JSONArray jsonarray) {
        assessment = new ArrayList<CommonModel>();
        try {
            if(jsonarray != null && jsonarray.length() >0) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    CommonModel Detail = new CommonModel();
                    Detail.setAssessment_no(jsonobject.getString("assessment_id"));
                    Detail.setAssessmentnameeng(jsonobject.getString("assessment_name"));
                    assessment.add(Detail);
                }
            }
            if(assessment != null && assessment.size() >0) {

                spinnerMapAssessment = new HashMap<String, String>();
                spinnerMapAssessment.put(null, null);
                final String[] items = new String[assessment.size() + 1];
                items[0] = context.getResources().getString(R.string.select_assessment);
                for (int i = 0; i < assessment.size(); i++) {
                    spinnerMapAssessment.put(assessment.get(i).getAssessment_no(), assessment.get(i).getAssessmentnameeng());
                    String Class = assessment.get(i).getAssessmentnameeng();
                    items[i + 1] = Class;
                }
                System.out.println("items" + items.toString());

                try {
                    if (items != null && items.length > 0) {
                        propertyImageCaptureBinding.assessmentId.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                        selectedAssessmentId="0";
                        selectedAssessmentNo="";
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getAssessmentList() {
        try {
            new ApiService(this).makeJSONObjectRequest("AssessmentList", Api.Method.POST, UrlGenerator.TradersUrl(), AssessmentListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject AssessmentListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), AssessmentListParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("AssessmentList", "" + dataSet);
        return dataSet;
    }

    public JSONObject AssessmentListParams() throws JSONException{
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "AssessmentList");
        dataSet.put(AppConstant.WARD_ID, selectedWardId);
        dataSet.put(AppConstant.STREET_ID, selectedStreetId);
        Log.d("AssessmentList", "" + dataSet);
        return dataSet;
    }
    private void LoadStreetSpinner(String selectedWardId)  {
        streets = new ArrayList<CommonModel>();
        String select_query= "SELECT *FROM " + DBHelper.STREET_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setStatecode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.KEY_STATE_CODE))));
                    commonModel.setDcode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE))));
                    commonModel.setLbcode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.LB_CODE))));
                    commonModel.setWard_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_CODE)));
                    commonModel.setWard_id(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.WARD_ID))));
                    commonModel.setStreetid(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.STREET_ID))));
                    commonModel.setStreet_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_CODE)));
                    commonModel.setStreet_name_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_NAME_TA)));
                    commonModel.setStreet_name_en(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_NAME_EN)));
                    commonModel.setBuilding_zone(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.BUILDING_ZONE)));
                    streets.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        Collections.sort(streets, (lhs, rhs) -> lhs.getStreet_name_ta().toLowerCase().compareTo(rhs.getStreet_name_ta().toLowerCase()));

        selectedStreets = new ArrayList<CommonModel>();
        for (int i = 0; i < streets.size(); i++) {
            if(streets.get(i).getWard_id().equals(selectedWardId)){
                selectedStreets.add(streets.get(i));
            }else { }
        }

        if(selectedStreets != null && selectedStreets.size() >0) {

            spinnerMapStreets = new HashMap<String, String>();
            spinnerMapStreets.put(null, null);
            final String[] items = new String[selectedStreets.size() + 1];
            items[0] = context.getResources().getString(R.string.select_Street);
            for (int i = 0; i < selectedStreets.size(); i++) {
                spinnerMapStreets.put(selectedStreets.get(i).getStreetid(), selectedStreets.get(i).getStreet_name_ta());
                String Class = selectedStreets.get(i).getStreet_name_ta();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    /*ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    propertyImageCaptureBinding.streetsName.setAdapter(RuralUrbanArray);
                    propertyImageCaptureBinding.streetsName.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
                    */
                    propertyImageCaptureBinding.streetsName.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedStreetId="0";
                    selectedStreetName="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }

    }

    public void LoadWardSpinner() {
        wards = new ArrayList<CommonModel>();
        String select_query= "SELECT *FROM " + DBHelper.WARD_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){

            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setStatecode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.KEY_STATE_CODE))));
                    commonModel.setDcode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE))));
                    commonModel.setLbcode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.LB_CODE))));
                    commonModel.setWard_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_CODE)));
                    commonModel.setWard_id(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.WARD_ID))));
                    commonModel.setWard_name_en(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_NAME_EN)));
                    commonModel.setWard_name_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_NAME_TA)));
                    wards.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        Collections.sort(wards, (lhs, rhs) -> lhs.getWard_code().compareTo(rhs.getWard_code()));

        if(wards != null && wards.size() >0) {

            spinnerMapWard = new HashMap<String, String>();
            spinnerMapWard.put(null, null);
            final String[] items = new String[wards.size() + 1];
            items[0] =context.getResources().getString(R.string.select_Ward);
            for (int i = 0; i < wards.size(); i++) {
                spinnerMapWard.put(wards.get(i).getWard_id(), wards.get(i).getWard_code());
                String Class = wards.get(i).getWard_code();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                   /* ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    propertyImageCaptureBinding.wardNo.setAdapter(RuralUrbanArray);
                    propertyImageCaptureBinding.wardNo.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    propertyImageCaptureBinding.wardNo.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedWardId="0";
                    selectedWardName="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }

    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


    public void validateDetails() {

            if(!selectedWardName.isEmpty() && !selectedWardName.equals(context.getResources().getString(R.string.select_Ward))){
                if(!selectedStreetName.isEmpty()&& !selectedStreetName.equals(context.getResources().getString(R.string.select_Street))){
                    if(!selectedAssessmentNo.isEmpty()&& !selectedAssessmentNo.equals(context.getResources().getString(R.string.select_assessment))){
                        if(prefManager.getPropertyImage() != null && !prefManager.getPropertyImage().equals("")) {
                            propertyCaptureImageSave();
                        }else {
                            Utils.showAlert(this, context.getResources().getString(R.string.capture_One_Image));
                        }
                    }else {
                        Utils.showAlert(this, context.getResources().getString(R.string.select_assessment));
                    }
                }else {
                    Utils.showAlert(this, context.getResources().getString(R.string.please_Select_Street));
                }
            }else {
                Utils.showAlert(this, context.getResources().getString(R.string.select_Ward));

            }
    }

    public void takePhoto(){
        Intent intent = new Intent(this, CameraScreen.class);
        intent.putExtra(AppConstant.WARD_ID, selectedWardId);
        intent.putExtra(AppConstant.STREET_ID, selectedStreetId);
        intent.putExtra(AppConstant.ASSESSMENT_NO, selectedAssessmentNo);
        intent.putExtra(AppConstant.KEY_SCREEN_STATUS, "PropertyExistImage");
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void viewPhoto(){
        if(prefManager.getPropertyImage() != null && !prefManager.getPropertyImage().equals("")){
        Intent intent = new Intent(this, FullImageActivity.class);
        intent.putExtra("PropertyImage", prefManager.getPropertyImage());
        intent.putExtra("key", "PropertyImage");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }else {
            Utils.showAlert(this, context.getResources().getString(R.string.capture_One_Image));
        }
    }

    public void propertyCaptureImageSave() {
        try {
            new ApiService(this).makeJSONObjectRequest("SavePropertyImage", Api.Method.POST, UrlGenerator.TradersUrl(), propertyEncryptedJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject propertyEncryptedJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), propertyCaptureJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("SavePropertyImage1", "" + authKey);

        return dataSet;
    }

    public JSONObject propertyCaptureJsonParams() throws JSONException {
        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"SavePropertyImage");
        data.put(AppConstant.WARD_ID,selectedWardId);
        data.put(AppConstant.STREET_ID,selectedStreetId);
        data.put(AppConstant.ASSESSMENT_NO,selectedAssessmentNo);
        data.put(AppConstant.LATITUDE,prefManager.getPropertyImageLat());
        data.put(AppConstant.LONGITUDE,prefManager.getPropertyImageLong());
        data.put(AppConstant.TRADE_IMAGE,prefManager.getPropertyImage());
        Log.d("SavePropertyImage2", "" + data);
        return data;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status ;
            if ("AssessmentList".equals(urlType) /*&& responseObj != null*/) {
                    String s="{\"STATUS\":\"OK\",\"RESPONSE\":\"OK\",\"JSON_DATA\":[{\"assessment_id\":\"11\",\"assessment_name\":\"1\"},{\"assessment_id\":\"22\",\"assessment_name\":\"2\"}]}";
                JSONObject jsonObject=new JSONObject(s);

               /* String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                Log.d("AssessmentList", "" + userDataDecrypt);
                JSONObject jsonObject = new JSONObject(userDataDecrypt);*/

                status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("OK") ) {
                    JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.JSON_DATA);
                    LoadAssessmentSpinner(jsonarray);
                    Log.d("AssessmentList", "" + jsonObject);

                } }
            if ("SavePropertyImage".equals(urlType) && responseObj != null) {

                try {
                    String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                    String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                    Log.d("SavePropertyImage", "" + userDataDecrypt);
                    JSONObject jsonObject = new JSONObject(userDataDecrypt);

                    // status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    if (status.equalsIgnoreCase("SUCCESS")) {
                        Utils.showToast(this, jsonObject.getString("MESSAGE"));
                        onBackPressed();
                        Log.d("SavePropertyImage", "" + jsonObject);

                    } else if (status.equalsIgnoreCase("FAILD")) {
                        Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void OnError(VolleyError volleyError) {
//        Utils.showAlert(this, context.getResources().getString(R.string.try_after_some_time));

    }

}
