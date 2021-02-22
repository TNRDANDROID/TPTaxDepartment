package com.nic.TPTaxDepartment.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.android.volley.VolleyError;
import com.nic.TPTaxDepartment.Adapter.AssessmentAdapter;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.databinding.AssessmentStatusNewBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
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

public class  AssessmentStatus extends AppCompatActivity implements View.OnClickListener,Api.ServerResponseListener {

    private AssessmentStatusNewBinding assessmentStatusBinding;
    private PrefManager prefManager;
    ArrayList<CommonModel> taxType ;
    ArrayList<CommonModel> professionalTax ;
    ArrayList<CommonModel> propertyTax ;
    ArrayList<CommonModel> tradeLicense ;
    ArrayList<CommonModel> waterTax ;
    ArrayList<CommonModel> nonTax ;
    HashMap<String,String> spinnerMapTaxType;
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

        assessmentStatusBinding.recycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        assessmentStatusBinding.recycler.setLayoutManager(layoutManager);

        Utils.setLanguage(assessmentStatusBinding.assessmentId,"en","USA");

        assessmentStatusBinding.detailsLayout.setVisibility(View.GONE);
        assessmentStatusBinding.submitLayout.setVisibility(View.VISIBLE);
        assessmentStatusBinding.submit.setVisibility(View.VISIBLE);

        getTaxTypeList();

        assessmentStatusBinding.taxType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String TaxTypeName = parent.getSelectedItem().toString();
                String TaxTypeId ="";
                // iterate each entry of hashmap
                for(Map.Entry<String, String> entry: spinnerMapTaxType.entrySet()) {
                    // if give value is equal to value from entry
                    // print the corresponding key
                    if(entry.getValue() == TaxTypeName) {
                        TaxTypeId=entry.getKey();
                        break;
                    }
                }
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

                getAssessmentStatus();
            }else { Utils.showAlert(this, "Enter Assessment ID"); }

        }else { Utils.showAlert(this, "Select Tax Type"); }
    }
    public void closeDetails() {
                assessmentStatusBinding.detailsLayout.setVisibility(View.GONE);
                assessmentStatusBinding.submitLayout.setVisibility(View.VISIBLE);
                assessmentStatusBinding.submit.setVisibility(View.VISIBLE);
    }

    public  void getTaxTypeList() {
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

            spinnerMapTaxType = new HashMap<String, String>();
            spinnerMapTaxType.put(null, null);
            final String[] items = new String[taxType.size() + 1];
            items[0] = "Select TaxType";
            for (int i = 0; i < taxType.size(); i++) {
                spinnerMapTaxType.put(taxType.get(i).taxtypeid, taxType.get(i).taxtypedesc_en);
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
                    selectedTaxTypeId="0";
                    selectedTaxTypeName="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }

    public void getAssessmentStatus() {
        try {
            new ApiService(this).makeJSONObjectRequest("AssessmentStatus", Api.Method.POST, UrlGenerator.TradersUrl(), assessmentStatusJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject assessmentStatusJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), checkAssessmentStatusJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("AssessmentStatusReq", "" + authKey);

        return dataSet;
    }

    public JSONObject checkAssessmentStatusJsonParams() throws JSONException {

        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"CheckAssessmentStatus");
        data.put(AppConstant.TAX_TYPE_ID,selectedTaxTypeId);
        data.put(AppConstant.ASSESSMENT_NO,assessmentStatusBinding.assessmentId.getText().toString());
        Log.d("AssessmentStatusRequest", "" + data);
        return data;
    }


    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("AssessmentStatus".equals(urlType) && responseObj != null) {
                String key =Utils.NotNullString( responseObj.getString(AppConstant.ENCODE_DATA));
                String responseDecryptedSchemeKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                Log.d("AssessmentStatus1", "" + responseDecryptedSchemeKey);
                JSONObject jsonObject = new JSONObject(responseDecryptedSchemeKey);
                Log.d("AssessmentStatus", "" + jsonObject);
                String status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.DATA);
                    if(jsonarray != null && jsonarray.length() >0) {
                        if (selectedTaxTypeName.equals("Professional Tax") || selectedTaxTypeId.equals("4")){
                            professionalTax=new ArrayList<CommonModel>();
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String lb_assessmentno = Utils.NotNullString(jsonobject.getString("lb_assessmentno"));
                                String assessmentnameeng = Utils.NotNullString(jsonobject.getString("assessmentnameeng"));
                                String organizationtype = Utils.NotNullString(jsonobject.getString("organizationtype"));
                                  /*  String assessment_no = Utils.NotNullString(jsonobject.getString("assessment_no"));
                                String owner_name = Utils.NotNullString(jsonobject.getString("owner_name"));
                                String father_name = Utils.NotNullString(jsonobject.getString("father_name"));
                                String permanent_address = Utils.NotNullString(jsonobject.getString("permanent_address"));
                                String area_in_sq_feet = Utils.NotNullString(jsonobject.getString("area_in_sq_feet"));*/
                                CommonModel commonModel=new CommonModel();
                                commonModel.setLb_assessmentno(lb_assessmentno);
                                commonModel.setAssessmentnameeng(assessmentnameeng);
                                commonModel.setOrganizationtype(organizationtype);
                                professionalTax.add(commonModel);

                            }
                        Collections.sort(professionalTax, (lhs, rhs) -> lhs.getAssessmentnameeng().compareTo(rhs.getAssessmentnameeng()));
                        if (professionalTax != null && professionalTax.size() > 0) {
                            AssessmentAdapter  assessmentAdapter = new AssessmentAdapter(AssessmentStatus.this, professionalTax,selectedTaxTypeName,selectedTaxTypeId);
                            assessmentAdapter.notifyDataSetChanged();
                            assessmentStatusBinding.recycler.setAdapter(assessmentAdapter);
                            assessmentStatusBinding.header.setText(selectedTaxTypeName);
                            assessmentStatusBinding.detailsLayout.setVisibility(View.VISIBLE);
                            assessmentStatusBinding.submitLayout.setVisibility(View.GONE);
                            assessmentStatusBinding.submit.setVisibility(View.GONE);
                        }
                    }
                        if (selectedTaxTypeName.equals("Property Tax") || selectedTaxTypeId.equals("1")){
                            propertyTax=new ArrayList<CommonModel>();
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String assessment_no = Utils.NotNullString(jsonobject.getString("assessment_no"));
                                String owner_name = Utils.NotNullString(jsonobject.getString("owner_name"));
                                String father_name = Utils.NotNullString(jsonobject.getString("father_name"));
                                String permanent_address = Utils.NotNullString(jsonobject.getString("permanent_address"));
                                String area_in_sq_feet = Utils.NotNullString(jsonobject.getString("area_in_sq_feet"));
                                CommonModel commonModel=new CommonModel();
                                commonModel.setAssessment_no(assessment_no);
                                commonModel.setOwner_name(owner_name);
                                commonModel.setFather_name(father_name);
                                commonModel.setPermanent_address(permanent_address);
                                commonModel.setArea_in_sq_feet(area_in_sq_feet);
                                propertyTax.add(commonModel);

                            }
                        Collections.sort(propertyTax, (lhs, rhs) -> lhs.getOwner_name().compareTo(rhs.getOwner_name()));
                        if (propertyTax != null && propertyTax.size() > 0) {
                            AssessmentAdapter  assessmentAdapter = new AssessmentAdapter(AssessmentStatus.this, propertyTax,selectedTaxTypeName,selectedTaxTypeId);
                            assessmentAdapter.notifyDataSetChanged();
                            assessmentStatusBinding.recycler.setAdapter(assessmentAdapter);
                            assessmentStatusBinding.header.setText(selectedTaxTypeName);
                            assessmentStatusBinding.detailsLayout.setVisibility(View.VISIBLE);
                            assessmentStatusBinding.submitLayout.setVisibility(View.GONE);
                            assessmentStatusBinding.submit.setVisibility(View.GONE);
                        }
                    }
                        if (selectedTaxTypeName.equals("Trade License") || selectedTaxTypeId.equals("6")){
                            tradeLicense=new ArrayList<CommonModel>();
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String lb_traderscode = Utils.NotNullString(jsonobject.getString("lb_traderscode"));
                                String apfathername_ta = Utils.NotNullString(jsonobject.getString("apfathername_ta"));
                                String apfathername_en = Utils.NotNullString(jsonobject.getString("apfathername_en"));
                                String doorno = Utils.NotNullString(jsonobject.getString("doorno"));
                                String traders_rate = Utils.NotNullString(jsonobject.getString("traders_rate"));
                                String from_fin_year = Utils.NotNullString(jsonobject.getString("from_fin_year"));
                                String to_fin_year = Utils.NotNullString(jsonobject.getString("to_fin_year"));
                                String trade_description_en = Utils.NotNullString(jsonobject.getString("trade_description_en"));
                                CommonModel commonModel=new CommonModel();
                                commonModel.setLb_traderscode(lb_traderscode);
                                commonModel.setApfathername_ta(apfathername_ta);
                                commonModel.setApfathername_en(apfathername_en);
                                commonModel.setDoorno(doorno);
                                commonModel.setTraders_rate(traders_rate);
                                commonModel.setFrom_fin_year(from_fin_year);
                                commonModel.setTo_fin_year(to_fin_year);
                                commonModel.setTrade_description_en(trade_description_en);
                                tradeLicense.add(commonModel);

                            }
                        Collections.sort(tradeLicense, (lhs, rhs) -> lhs.getApfathername_en().compareTo(rhs.getApfathername_en()));
                        if (tradeLicense != null && tradeLicense.size() > 0) {
                            AssessmentAdapter  assessmentAdapter = new AssessmentAdapter(AssessmentStatus.this, tradeLicense,selectedTaxTypeName,selectedTaxTypeId);
                            assessmentAdapter.notifyDataSetChanged();
                            assessmentStatusBinding.recycler.setAdapter(assessmentAdapter);
                            assessmentStatusBinding.header.setText(selectedTaxTypeName);
                            assessmentStatusBinding.detailsLayout.setVisibility(View.VISIBLE);
                            assessmentStatusBinding.submitLayout.setVisibility(View.GONE);
                            assessmentStatusBinding.submit.setVisibility(View.GONE);
                        }
                    }
                        if (selectedTaxTypeName.equals("Water Charges") || selectedTaxTypeId.equals("2")){
                            waterTax=new ArrayList<CommonModel>();
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String lb_connectionno = Utils.NotNullString(jsonobject.getString("lb_connectionno"));
                                String connectionname = Utils.NotNullString(jsonobject.getString("connectionname"));
                                String water_charges = Utils.NotNullString(jsonobject.getString("water_charges"));
                                 CommonModel commonModel=new CommonModel();
                                commonModel.setLb_connectionno(lb_connectionno);
                                commonModel.setConnectionname(connectionname);
                                commonModel.setWater_charges(water_charges);
                                waterTax.add(commonModel);

                            }
                        Collections.sort(waterTax, (lhs, rhs) -> lhs.getConnectionname().compareTo(rhs.getConnectionname()));
                        if (waterTax != null && waterTax.size() > 0) {
                            AssessmentAdapter  assessmentAdapter = new AssessmentAdapter(AssessmentStatus.this, waterTax,selectedTaxTypeName,selectedTaxTypeId);
                            assessmentAdapter.notifyDataSetChanged();
                            assessmentStatusBinding.recycler.setAdapter(assessmentAdapter);
                            assessmentStatusBinding.header.setText(selectedTaxTypeName);
                            assessmentStatusBinding.detailsLayout.setVisibility(View.VISIBLE);
                            assessmentStatusBinding.submitLayout.setVisibility(View.GONE);
                            assessmentStatusBinding.submit.setVisibility(View.GONE);
                        }
                    }
                        if (selectedTaxTypeName.equals("Non Tax") || selectedTaxTypeId.equals("5")){
                            nonTax=new ArrayList<CommonModel>();
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                String lb_leaseassessmentno = Utils.NotNullString(jsonobject.getString("lb_leaseassessmentno"));
                                String leasee_name_en = Utils.NotNullString(jsonobject.getString("leasee_name_en"));
                                String leasee_name_ta = Utils.NotNullString(jsonobject.getString("leasee_name_ta"));
                                String lease_type_code = Utils.NotNullString(jsonobject.getString("lease_type_code"));
                                String lease_type_description_en = Utils.NotNullString(jsonobject.getString("lease_type_description_en"));
                                String lease_payment_due_type = Utils.NotNullString(jsonobject.getString("lease_payment_due_type"));
                                String annuallease_amount = Utils.NotNullString(jsonobject.getString("annuallease_amount"));

                                CommonModel commonModel=new CommonModel();
                                commonModel.setLb_leaseassessmentno(lb_leaseassessmentno);
                                commonModel.setLeasee_name_en(leasee_name_en);
                                commonModel.setLeasee_name_ta(leasee_name_ta);
                                commonModel.setLease_type_code(lease_type_code);
                                commonModel.setLease_type_description_en(lease_type_description_en);
                                commonModel.setLease_payment_due_type(lease_payment_due_type);
                                commonModel.setAnnuallease_amount(annuallease_amount);
                                nonTax.add(commonModel);

                            }
                        Collections.sort(nonTax, (lhs, rhs) -> lhs.getLeasee_name_en().compareTo(rhs.getLeasee_name_en()));
                        if (nonTax != null && nonTax.size() > 0) {
                            AssessmentAdapter  assessmentAdapter = new AssessmentAdapter(AssessmentStatus.this, nonTax,selectedTaxTypeName,selectedTaxTypeId);
                            assessmentAdapter.notifyDataSetChanged();
                            assessmentStatusBinding.recycler.setAdapter(assessmentAdapter);
                            assessmentStatusBinding.header.setText(selectedTaxTypeName);
                            assessmentStatusBinding.detailsLayout.setVisibility(View.VISIBLE);
                            assessmentStatusBinding.submitLayout.setVisibility(View.GONE);
                            assessmentStatusBinding.submit.setVisibility(View.GONE);
                        }
                    }
                    }

                } else {
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

        if(assessmentStatusBinding.detailsLayout.getVisibility() == View.VISIBLE){
            assessmentStatusBinding.detailsLayout.setVisibility(View.GONE);
            assessmentStatusBinding.submitLayout.setVisibility(View.VISIBLE);
            assessmentStatusBinding.submit.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }


}
