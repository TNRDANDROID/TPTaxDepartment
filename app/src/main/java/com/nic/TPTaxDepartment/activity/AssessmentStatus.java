package com.nic.TPTaxDepartment.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.databinding.AssessmentStatusNewBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class  AssessmentStatus extends AppCompatActivity implements View.OnClickListener,Api.ServerResponseListener {

    private AssessmentStatusNewBinding assessmentStatusBinding;
    private PrefManager prefManager;
    private List<TPtaxModel> Block = new ArrayList<>();

    ArrayList<CommonModel> taxType ;
    HashMap<Integer,String> spinnerMapTaxType;
    String selectedTaxTypeId;
    String selectedTaxTypeName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assessmentStatusBinding = DataBindingUtil.setContentView(this, R.layout.assessment_status_new);
        assessmentStatusBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        prefManager = new PrefManager(this);

        assessmentStatusBinding.detailsLayout.setVisibility(View.GONE);
        assessmentStatusBinding.submitLayout.setVisibility(View.VISIBLE);
        assessmentStatusBinding.submit.setVisibility(View.VISIBLE);

        assessmentStatusBinding.taxType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    sp_block.setClickable(false);
//                    sp_block.setVisibility(View.GONE);
//                } else {
//                    sp_block.setClickable(true);
//                    sp_block.setVisibility(View.VISIBLE);
//                }
//                pref_district = District.get(position).getDistrictName();
//                prefManager.setDistrictName(pref_district);
//
//                blockFilterSpinner(District.get(position).getDistictCode());
//                prefManager.setDistrictCode(District.get(position).getDistictCode());
//

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        assessmentStatusBinding.assessmentId.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event != null &&
                                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            if (event == null || !event.isShiftPressed()) {
                                // the user is done typing.
                                if(Utils.isOnline()){
                                    getAssessmentStatus();
                                }
                                else{
                                    Utils.showAlert(AssessmentStatus.this,getResources().getString(R.string.no_internet));
                                }
                                return true; // consume.
                            }
                        }
                        return false; // pass on to other listeners.
                    }
                }
        );
        getTaxTypeList();
        assessmentStatusBinding.taxType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String TaxTypeName = parent.getSelectedItem().toString();
                String TaxTypeId = spinnerMapTaxType.get(parent.getSelectedItemPosition());
                selectedTaxTypeId=TaxTypeId;
                selectedTaxTypeName=TaxTypeName;
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    public void showDetails() {
        if (!selectedTaxTypeName.isEmpty() && !selectedTaxTypeName.equals("Select TaxType")  ) {
            if(!assessmentStatusBinding.assessmentId.getText().toString().isEmpty()){
                assessmentStatusBinding.detailsLayout.setVisibility(View.VISIBLE);
                assessmentStatusBinding.submitLayout.setVisibility(View.GONE);
                assessmentStatusBinding.submit.setVisibility(View.GONE);

            }else { Utils.showAlert(this, "Enter Assessment ID"); }

        }else { Utils.showAlert(this, "Select Tax Type"); }
    }
    public void closeDetails() {
                assessmentStatusBinding.detailsLayout.setVisibility(View.GONE);
                assessmentStatusBinding.submitLayout.setVisibility(View.VISIBLE);
                assessmentStatusBinding.submit.setVisibility(View.VISIBLE);
    }

    void getTaxTypeList() {
        taxType = new ArrayList<CommonModel>();
        String select_query= "SELECT *FROM " + DBHelper.TAX_TYPE_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){

            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setTaxtypeid(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TAX_TYPE_ID))));
                    commonModel.setTaxtypedesc_en(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TAX_TYPE_DESC_EN)));
                    commonModel.setTaxtypedesc_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TAX_TYPE_DESC_TA)));
                    commonModel.setTaxcollection_methodlogy(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TAX_COLLECTION_METHODLOGY)));
                    commonModel.setInstallmenttypeid(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.INSTALLMENT_TYPE_ID))));
                    taxType.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        Collections.sort(taxType, (lhs, rhs) -> lhs.getTaxtypedesc_en().compareTo(rhs.getTaxtypedesc_en()));

        if(taxType != null && taxType.size() >0) {

            spinnerMapTaxType = new HashMap<Integer, String>();
            spinnerMapTaxType.put(0, null);
            final String[] items = new String[taxType.size() + 1];
            items[0] = "Select TaxType";
            for (int i = 0; i < taxType.size(); i++) {
                spinnerMapTaxType.put(i + 1, taxType.get(i).taxtypeid);
                String Class = taxType.get(i).taxtypedesc_en;
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    assessmentStatusBinding.taxType.setAdapter(RuralUrbanArray);
                    assessmentStatusBinding.taxType.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
                    selectedTaxTypeId=taxType.get(1).taxtypeid;
                    selectedTaxTypeName="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }

    public void getAssessmentStatus() {
        try {
            new ApiService(this).makeJSONObjectRequest("AssessmentStatus", Api.Method.POST, UrlGenerator.saveTradersUrl(), assessmentStatusJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject assessmentStatusJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), checkAssessmentStatusJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("workList", "" + authKey);
        return dataSet;
    }

    public JSONObject checkAssessmentStatusJsonParams() throws JSONException {

        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"CheckAssessmentStatus");
//        data.put(AppConstant.TAX_TYPE_ID,assessmentStatusBinding.taxType.getSelectedItemId());
//        data.put(AppConstant.ASSESSMENT_NO,assessmentStatusBinding.assessmentId.getText());
        data.put(AppConstant.TAX_TYPE_ID,"1");
        data.put(AppConstant.ASSESSMENT_NO,"28691");
        return data;
    }



    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("AssessmentStatus".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedSchemeKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedSchemeKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    assessmentStatusBinding.applicantNameTv.setText("");
                    assessmentStatusBinding.fatherNameTv.setText("");

                } else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")){
                    Utils.showAlert(this,"NO RECORD FOUND!");
                }
//                String authKey = responseDecryptedSchemeKey.toString();
//                int maxLogSize = 4000;
//                for(int i = 0; i <= authKey.length() / maxLogSize; i++) {
//                    int start = i * maxLogSize;
//                    int end = (i+1) * maxLogSize;
//                    end = end > authKey.length() ? authKey.length() : end;
//                    Log.v("to_send", authKey.substring(start, end));
//                }
//                Log.d("WorkListResp", "" + responseDecryptedSchemeKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }
    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


}
