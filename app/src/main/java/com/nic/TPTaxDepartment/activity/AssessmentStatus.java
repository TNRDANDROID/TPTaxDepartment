package com.nic.TPTaxDepartment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.nic.TPTaxDepartment.Adapter.GenderAdapter;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.databinding.AssessmentStatusBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  AssessmentStatus extends AppCompatActivity implements Api.ServerResponseListener {

    private AssessmentStatusBinding assessmentStatusBinding;
    private PrefManager prefManager;
    private List<TPtaxModel> Block = new ArrayList<>();
    ArrayList<CommonModel> taxTypeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assessmentStatusBinding = DataBindingUtil.setContentView(this, R.layout.assessment_status);
        assessmentStatusBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        prefManager = new PrefManager(this);
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
        getTaxTypes();

    }
    public void getAssessmentStatus() {
        try {
            new ApiService(this).makeJSONObjectRequest("AssessmentStatus", Api.Method.POST, UrlGenerator.saveTradersUrl(), assessmentStatusJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getTaxTypes() {
        try {
            new ApiService(this).makeJSONObjectRequest("TaxTypes", Api.Method.POST, UrlGenerator.prodOpenUrl(), gettaxTypeListJsonParams(), "not cache", this);
        } catch (Exception e) {
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

    public Map<String,String> gettaxTypeListParams(){
        Map<String, String> params = new HashMap<>();
        params.put(AppConstant.KEY_SERVICE_ID, "TaxTypesFieldVisit");
        Log.d("params", "" + params);
        return params;
    }

    public JSONObject gettaxTypeListJsonParams(){
        JSONObject data = new JSONObject();
        try {
            data.put(AppConstant.KEY_SERVICE_ID,"OS_TaxTypes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }



    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
           /* String responseObj = serverResponse.getResponse();
            String urlType = serverResponse.getApi();*/

          /*  if ("AssessmentStatus".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedSchemeKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedSchemeKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    assessmentStatusBinding.applicantNameTv.setText("");
                    assessmentStatusBinding.detailsTv.setText("");

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
            }*/
          if("TaxTypes".equals(urlType)&&(responseObj!=null)){
              if (responseObj.getString("STATUS").equalsIgnoreCase("SUCCESS")) {
                  JSONArray jsonString =responseObj.getJSONArray("DATA");
                  taxTypeArrayList = new ArrayList<>();
                  CommonModel commonModel = new CommonModel();
                  commonModel.setInstallmenttypeid(0);
                  commonModel.setTaxtypedesc_ta("Select TaxType");
                  commonModel.setTaxtypedesc_en("Select TaxType");
                  commonModel.setTaxtypeid("ST");
                  taxTypeArrayList.add(commonModel);
                  for (int i = 0; i < jsonString.length(); i++) {
                      JSONObject jsonObject = jsonString.getJSONObject(i);
                      CommonModel taxType = new CommonModel();
                      taxType.setTaxtypeid(jsonObject.getString("taxtypeid"));
                      taxType.setTaxtypedesc_en(jsonObject.getString("taxtypedesc_en"));
                      taxType.setTaxtypedesc_ta(jsonObject.getString("taxtypedesc_ta"));
                      taxType.setInstallmenttypeid(Integer.parseInt(jsonObject.getString("installmenttypeid")));
                      taxType.setTaxcollection_methodlogy(jsonObject.getString("taxcollection_methodlogy"));

                      taxTypeArrayList.add(taxType);
                      displayTaxType(taxTypeArrayList);
                  }
              }
          }
        } catch (JSONException e) {
            e.printStackTrace();
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

    //display ward list
    public void displayTaxType(ArrayList<CommonModel> taxTypeArrayList) {

        GenderAdapter adapter = new GenderAdapter(AssessmentStatus.this,taxTypeArrayList,"TaxType");
        assessmentStatusBinding.taxType.setAdapter(adapter);
    }

}
