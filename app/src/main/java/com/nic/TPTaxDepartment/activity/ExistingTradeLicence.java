package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
import com.nic.TPTaxDepartment.databinding.ExistingTradeLicenceBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.Gender;
import com.nic.TPTaxDepartment.model.TPtaxModel;
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

public class ExistingTradeLicence extends AppCompatActivity implements Api.ServerResponseListener{
    private ExistingTradeLicenceBinding existingTradeLicenceBinding;
    private PrefManager prefManager;
    ArrayList<CommonModel> wards ;
    ArrayList<CommonModel> streets;
    ArrayList<CommonModel> selectedStreets;

    HashMap<Integer,String> spinnerMapStreets;
    HashMap<Integer,String> spinnerMapWard;
    String selectedWardId;
    String selectedWardName="";
    String selectedStreetId;
    String selectedStreetName="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existingTradeLicenceBinding = DataBindingUtil.setContentView(this, R.layout.existing_trade_licence);
        existingTradeLicenceBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        prefManager = new PrefManager(this);

        LoadWardSpinner();

       // getTradersList();

        existingTradeLicenceBinding.wardNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String ward = parent.getSelectedItem().toString();
                String wardId = spinnerMapWard.get(parent.getSelectedItemPosition());
                selectedWardName=ward;
                selectedWardId=wardId;
                System.out.println("selectedWardId >> "+selectedWardId);
                if(selectedWardId != null){
                    LoadStreetSpinner(selectedWardId);
                }else {
                    existingTradeLicenceBinding.streetsName.setAdapter(null);                  }


            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        existingTradeLicenceBinding.streetsName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String street = parent.getSelectedItem().toString();
                String streetCode = spinnerMapStreets.get(parent.getSelectedItemPosition());
                selectedStreetId=streetCode;
                selectedStreetName=street;
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });


    }
    public void getTradersList() {
        try {
            new ApiService(this).makeJSONObjectRequest("TradeLicenseTradersList", Api.Method.POST, UrlGenerator.saveTradersUrl(), traderlistJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject traderlistJsonParams() throws JSONException{


        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "TradeLicenseTradersList");
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
        Collections.sort(streets, (lhs, rhs) -> lhs.getStreet_name_ta().compareTo(rhs.getStreet_name_ta()));

        selectedStreets = new ArrayList<CommonModel>();
        for (int i = 0; i < streets.size(); i++) {
            if(streets.get(i).ward_id.equals(selectedWardId)){
                selectedStreets.add(streets.get(i));
            }else { }
        }

        if(selectedStreets != null && selectedStreets.size() >0) {

            spinnerMapStreets = new HashMap<Integer, String>();
            spinnerMapStreets.put(0, null);
            final String[] items = new String[selectedStreets.size() + 1];
            items[0] = "Select Street";
            for (int i = 0; i < selectedStreets.size(); i++) {
                spinnerMapStreets.put(i + 1, selectedStreets.get(i).streetid);
                String Class = selectedStreets.get(i).street_name_ta;
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeLicenceBinding.streetsName.setAdapter(RuralUrbanArray);
                    existingTradeLicenceBinding.streetsName.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
                    selectedStreetId=selectedStreets.get(1).streetid;
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
        Collections.sort(wards, (lhs, rhs) -> lhs.getWard_name_ta().compareTo(rhs.getWard_name_ta()));

        if(wards != null && wards.size() >0) {

            spinnerMapWard = new HashMap<Integer, String>();
            spinnerMapWard.put(0, null);
            final String[] items = new String[wards.size() + 1];
            items[0] = "Select Ward";
            for (int i = 0; i < wards.size(); i++) {
                spinnerMapWard.put(i + 1, wards.get(i).ward_id);
                String Class = wards.get(i).ward_name_ta;
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeLicenceBinding.wardNo.setAdapter(RuralUrbanArray);
                    existingTradeLicenceBinding.wardNo.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
                    selectedWardId=wards.get(1).ward_id;
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

    public void existTradeLicenceSubmit() {
        Intent intent = new Intent( this, ExistingTradeList.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void validateDetails() {

        if (!existingTradeLicenceBinding.tradersCode.getText().toString().isEmpty() || !existingTradeLicenceBinding.mobileNo.getText().toString().isEmpty() || (!selectedWardName.isEmpty()&& !selectedWardName.equals("Select Ward")) || ( !selectedStreetName.isEmpty()&& !selectedStreetName.equals("Select Street"))) {

                if(!selectedWardName.isEmpty() && !selectedWardName.equals("Select Ward")){
                    if(!selectedStreetName.isEmpty()&& !selectedStreetName.equals("Select Street")){
                        existTradeLicenceSubmit();
                    }else {
                        Utils.showAlert(this, "Please Select Street!");
                    }
                }else {
                    existTradeLicenceSubmit();
                }

        }else { Utils.showAlert(this, "Select Any One!"); }
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
            String responseObj = serverResponse.getResponse();
            String urlType = serverResponse.getApi();
            /*String status = responseObj.getString(AppConstant.KEY_STATUS);
            String response = responseObj.getString(AppConstant.KEY_RESPONSE);
            if ("SaveLicenseTraders".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    JSONObject jsonObject = responseObj.getJSONObject(AppConstant.JSON_DATA);
//                    String Motivatorid = jsonObject.getString(AppConstant.KEY_REGISTER_MOTIVATOR_ID);
//                    Log.d("motivatorid",""+Motivatorid);
                    Utils.showAlert(this, "நீங்கள் வெற்றிகரமாக பதிவு செய்யப்பட்டுள்ளீர்கள்!");
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                    handler.postDelayed(runnable, 2000);

                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("FAIL")) {
                    Utils.showAlert(this, responseObj.getString("MESSAGE"));
                }
            }*/
            if ("TradeLicenseTradersList".equals(urlType) && responseObj != null) {
                String jsonStr = responseObj;
                JSONArray jsonarray = new JSONArray(jsonStr);
                if(jsonarray != null && jsonarray.length() >0) {
                    prefManager.setTradersList(jsonStr);
                }
                Log.d("TradeLicenseTradersList", "" + responseObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void OnError(VolleyError volleyError) {

    }

}
