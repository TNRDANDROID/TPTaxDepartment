package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExistingTradeLicence extends AppCompatActivity implements Api.ServerResponseListener{
    private ExistingTradeLicenceBinding existingTradeLicenceBinding;
    private PrefManager prefManager;
    ArrayList<CommonModel> wards ;
    ArrayList<CommonModel> streets;
    ArrayList<CommonModel> selectedStreets;

    HashMap<String,String> spinnerMapStreets;
    HashMap<String,String> spinnerMapWard;
    String selectedWardId="";
    String selectedWardName="";
    String selectedStreetId="";
    String selectedStreetName="";
    ArrayList<TPtaxModel> tradersList;
    ArrayList<TPtaxModel> tradersImageList;
    String selectedTradeCode="";
    String selectedTrdeCodeDetailsID="";
    HashMap<Integer,String> spinnerTradeCode;
    ArrayList<CommonModel> loadTradeCodeList;
    ArrayAdapter<String> tradeCodeSpArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existingTradeLicenceBinding = DataBindingUtil.setContentView(this, R.layout.existing_trade_licence);
        existingTradeLicenceBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        prefManager = new PrefManager(this);
        getTradersList();
        LoadWardSpinner();
        LoadTradeCodeListSpinner();


        existingTradeLicenceBinding.wardNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                if(selectedWardId != null  && !selectedWardId.isEmpty() && !selectedWardName.equals("Select Ward")){

                    LoadStreetSpinner(selectedWardId);
                    existingTradeLicenceBinding.tradeCodeSpinner.setEnabled(false);
                    existingTradeLicenceBinding.mobileNo.setEnabled(false);
                }else {
                    existingTradeLicenceBinding.streetsName.setAdapter(null);
                    selectedStreetId="0";
                    selectedStreetName="";
                    existingTradeLicenceBinding.tradeCodeSpinner.setEnabled(true);
                    existingTradeLicenceBinding.mobileNo.setEnabled(true);
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        existingTradeLicenceBinding.streetsName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        existingTradeLicenceBinding.mobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(existingTradeLicenceBinding.mobileNo.length() > 0){
                    existingTradeLicenceBinding.tradeCodeSpinner.setEnabled(false);
                    existingTradeLicenceBinding.wardNo.setEnabled(false);
                }else {
                    existingTradeLicenceBinding.tradeCodeSpinner.setEnabled(true);
                    existingTradeLicenceBinding.wardNo.setEnabled(true);
                }

            }
        });
        existingTradeLicenceBinding.tradeCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
              /*  String tradeCode = parent.getSelectedItem().toString();
//                String tradeID = spinnerTradeCode.get(parent.getSelectedItemPosition());
                String tradeID ="";
                System.out.println("tradeCode>> "+ tradeCode);
                // iterate each entry of hashmap
                for(Map.Entry<String, String> entry: spinnerTradeCode.entrySet()) {
                    // if give value is equal to value from entry
                    // print the corresponding key
                    if(entry.getValue() == tradeCode) {
                        tradeID=entry.getKey();
                        break;
                    }
                }
              */
                String tradeCode = parent.getSelectedItem().toString();
                String tradeID = spinnerTradeCode.get(parent.getSelectedItemPosition());
                selectedTrdeCodeDetailsID=tradeID;
                ((TextView) parent.getChildAt(0)).setTextColor(ExistingTradeLicence.this.getResources().getColor(R.color.grey2));

                System.out.println("tradeCode>> "+ tradeCode);
                System.out.println("TradeId>> "+ selectedTrdeCodeDetailsID);
                selectedTradeCode=tradeCode;

                selectedTrdeCodeDetailsID=tradeID;
                System.out.println("TradeId>> "+ selectedTrdeCodeDetailsID);
                selectedTradeCode=tradeCode;
                if(selectedTrdeCodeDetailsID != null && !selectedTrdeCodeDetailsID.isEmpty()){
                    existingTradeLicenceBinding.mobileNo.setEnabled(false);
                    existingTradeLicenceBinding.wardNo.setEnabled(false);
                }else {
                    selectedTrdeCodeDetailsID="0";
                    selectedTradeCode="";
                    existingTradeLicenceBinding.mobileNo.setEnabled(true);
                    existingTradeLicenceBinding.wardNo.setEnabled(true);
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

    }

    private void LoadTradeCodeListSpinner(){
        loadTradeCodeList = new ArrayList<>();
        String select_query= "SELECT *FROM " + DBHelper.TRADE_CODE_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setTRADE_DETAILS_ID(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TRADE_DETAILS_ID))));
                    commonModel.setTRADE_RATE(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TRADE_RATE))));
                    commonModel.setLB_TRADE_CODE(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.LB_TRADE_CODE))));
                    commonModel.setSTATECODE(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STATECODE)));
                    commonModel.setDcode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE))));
                    commonModel.setLICENSE_TYPE_ID(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.LICENSE_TYPE_ID))));
                    commonModel.setFINYEAR(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FINYEAR)));
                    commonModel.setDESCRIPTION_EN(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DESCRIPTION_EN)));
                    commonModel.setDESCRIPTION_TA(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DESCRIPTION_TA)));
                    commonModel.setLbcode(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LB_CODE)));
                    commonModel.setDATE_FIELD(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DATE_FIELD)));

                    loadTradeCodeList.add(commonModel);
                }while (cursor.moveToNext());
            }
        }


        if(loadTradeCodeList != null && loadTradeCodeList.size() >0) {

            spinnerTradeCode = new HashMap<Integer, String>();
            spinnerTradeCode.put(0, null);
            final String[] items = new String[loadTradeCodeList.size() + 1];
            items[0] = "Select TradeCode";
            for (int i = 0; i < loadTradeCodeList.size(); i++) {
                spinnerTradeCode.put(i+1, loadTradeCodeList.get(i).getTRADE_DETAILS_ID());
                String Class = loadTradeCodeList.get(i).getLB_TRADE_CODE()+" - " +loadTradeCodeList.get(i).getDESCRIPTION_EN();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    tradeCodeSpArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    tradeCodeSpArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeLicenceBinding.tradeCodeSpinner.setAdapter(tradeCodeSpArray);
                    existingTradeLicenceBinding.tradeCodeSpinner.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
                    selectedTrdeCodeDetailsID = "0";
                    selectedTradeCode = "";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }

    public void getTradersList() {
        try {
            new ApiService(this).makeJSONObjectRequest("TradeLicenseTradersList", Api.Method.POST, UrlGenerator.TradersUrl(), traderlistJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject traderlistJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), tradersJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("TradeLicenseTradersList", "" + dataSet);
        return dataSet;
    }

    public JSONObject tradersJsonParams() throws JSONException{
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

            spinnerMapStreets = new HashMap<String, String>();
            spinnerMapStreets.put(null, null);
            final String[] items = new String[selectedStreets.size() + 1];
            items[0] = "Select Street";
            for (int i = 0; i < selectedStreets.size(); i++) {
                spinnerMapStreets.put(selectedStreets.get(i).streetid, selectedStreets.get(i).street_name_ta);
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
        Collections.sort(wards, (lhs, rhs) -> lhs.getWard_name_ta().compareTo(rhs.getWard_name_ta()));

        if(wards != null && wards.size() >0) {

            spinnerMapWard = new HashMap<String, String>();
            spinnerMapWard.put(null, null);
            final String[] items = new String[wards.size() + 1];
            items[0] = "Select Ward";
            for (int i = 0; i < wards.size(); i++) {
                spinnerMapWard.put(wards.get(i).ward_id, wards.get(i).ward_name_ta);
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

    public void existTradeLicenceSubmit(String wardId,String streetId,String tradeDetails_id,String mobileNo) {

        tradersList = new ArrayList<TPtaxModel>();
        tradersImageList = new ArrayList<TPtaxModel>();

        JSONArray jsonarray= null;
        try {
            jsonarray = new JSONArray(prefManager.getTradersList());
            if(jsonarray != null && jsonarray.length() >0) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);

                    String tradersdetails_id= Utils.NotNullString(jsonobject.getString("tradersdetails_id"));
                    String lb_sno= Utils.NotNullString(jsonobject.getString("lb_sno"));
//                    String lb_tradecode= Utils.NotNullString(jsonobject.getString("lb_tradecode"));
                    String lb_traderscode= Utils.NotNullString(jsonobject.getString("lb_traderscode"));
                    String tradedetails_id= Utils.NotNullString(jsonobject.getString("tradedetails_id"));
//                    String traderate= Utils.NotNullString(jsonobject.getString("traderate"));
                    String traders_rate= Utils.NotNullString(jsonobject.getString("traders_rate"));
                    String traders_type= Utils.NotNullString(jsonobject.getString("traders_type"));
                    String tradersperiod= Utils.NotNullString(jsonobject.getString("tradersperiod"));
                    String traderstypee= Utils.NotNullString(jsonobject.getString("traderstypee"));

                    String apname_ta= Utils.NotNullString(jsonobject.getString("apname_ta"));
                    String apname_en= Utils.NotNullString(jsonobject.getString("apname_en"));
                    String apfathername_ta= Utils.NotNullString(jsonobject.getString("apfathername_ta"));
                    String apfathername_en= Utils.NotNullString(jsonobject.getString("apfathername_en"));
                    String apgender= Utils.NotNullString(jsonobject.getString("apgender"));
                    String apage= Utils.NotNullString(jsonobject.getString("apage"));
                    String date= Utils.NotNullString(jsonobject.getString("date"));
                    String dcode= Utils.NotNullString(jsonobject.getString("dcode"));
                    String doorno= Utils.NotNullString(jsonobject.getString("doorno"));
                    String email= Utils.NotNullString(jsonobject.getString("email"));
                    String description_ta= Utils.NotNullString(jsonobject.getString("establishment_name_ta"));
                    String description_en= Utils.NotNullString(jsonobject.getString("establishment_name_en"));
                    String statecode= Utils.NotNullString(jsonobject.getString("statecode"));
                    String wardid= (Utils.NotNullString(jsonobject.getString("wardid")));
                    String streetid= (Utils.NotNullString(jsonobject.getString("streetid")));
                    String mobileno= Utils.NotNullString(jsonobject.getString("mobileno"));
                    String licencetypeid= Utils.NotNullString(jsonobject.getString("licencetypeid"));
                    String licence_validity= Utils.NotNullString(jsonobject.getString("licence_validity"));
                    String traders_license_type_name= Utils.NotNullString(jsonobject.getString("traders_license_type_name"));

                    String ownerStatus=Utils.NotNullString(jsonobject.getString("owner_y_n"));
                    String motorStatus= Utils.NotNullString(jsonobject.getString("motor_y_n"));
                    String generatorStatus= Utils.NotNullString(jsonobject.getString("generator_y_n"));
                    String professtionlStatus= Utils.NotNullString(jsonobject.getString("professional_tax_paid"));
                    String propertyStatus= Utils.NotNullString(jsonobject.getString("property_tax_paid"));
                    String motor_type_id= Utils.NotNullString(jsonobject.getString("motor_type_id"));
                    String amount_range_id= Utils.NotNullString(jsonobject.getString("amount_range_id"));
                    String generator_range_id= Utils.NotNullString(jsonobject.getString("generator_range_id"));
                    String propertyTaxAssessmentNumber=Utils.NotNullString(jsonobject.getString("property_tax_assessment_no"));
                    String remark=Utils.NotNullString(jsonobject.getString("remark"));
                    String annual_sale_production_amount=Utils.NotNullString(jsonobject.getString("annual_sale_production_amount"));
                    String annual_sale_production_range=Utils.NotNullString(jsonobject.getString("annual_sale_production_range"));
                    String generator_range=Utils.NotNullString(jsonobject.getString("generator_range"));
                    String generator_range_amount=Utils.NotNullString(jsonobject.getString("generator_range_amount"));
                    String motor_range=Utils.NotNullString(jsonobject.getString("motor_range"));
                    String motor_range_amount=Utils.NotNullString(jsonobject.getString("motor_range_amount"));
                    String street_name_en=Utils.NotNullString(jsonobject.getString("street_name_en"));
                    String street_name_ta=Utils.NotNullString(jsonobject.getString("street_name_ta"));
                    String ward_name_en=Utils.NotNullString(jsonobject.getString("ward_name_en"));
                    String ward_name_ta=Utils.NotNullString(jsonobject.getString("ward_name_ta"));

//                     String tradedesct= Utils.NotNullString(jsonobject.getString("tradedesct"));
//                     String tradedesce= Utils.NotNullString(jsonobject.getString("tradedesce"));
//                     String licencefor= Utils.NotNullString(jsonobject.getString("licencefor"));
//                     String fin_year= Utils.NotNullString(jsonobject.getString("from_fin_year"));
//                     String demandtype= Utils.NotNullString(jsonobject.getString("demandtype"));
//                     String onlineapplicationno= Utils.NotNullString(jsonobject.getString("onlineapplicationno"));
//                     String paymentstatus=Utils.NotNullString(jsonobject.getString("paymentstatus"));
//                     String licenceno=Utils.NotNullString( jsonobject.getString("licenceno"));
//                     String paymentdate= Utils.NotNullString(jsonobject.getString("paymentdate"));

                     /*String ownerStatus="N" ;
                     String motorStatus= "Y";
                     String generatorStatus= "N";
                     String professtionlStatus= "Y";
                     String propertyStatus="Y" ;
                     String motor_type_id="5" ;
                     String amount_range_id="6" ;
                     String generator_range_id="4" ;
                     String propertyTaxAssessmentNumber="123" ;*/
                     String document=AppConstant.PDF/*Utils.NotNullString(jsonobject.getString("document_url"))*/;
                     String tradeImage= Utils.NotNullString(AppConstant.SAMPLE_IMAGE);
                     Bitmap bitmap=StringToBitMap(tradeImage);
                     byte[] ByteArray = tradeImage.getBytes();


                         if(!tradeDetails_id.equals("0") && !tradeDetails_id.isEmpty() && tradeDetails_id != null){
                             if(tradeDetails_id.equals(tradedetails_id)){
                                 TPtaxModel Detail = new TPtaxModel();
                                 Detail.setTraderName(apname_en);
//                                 Detail.setTraderCode(lb_tradecode);
                                 Detail.setTraders_typ(traders_type);
                                 Detail.setTraderCode(lb_traderscode);
                                 Detail.setDoorno(doorno);
                                 Detail.setApfathername_ta(apfathername_ta);
                                 Detail.setLicenceValidity(licence_validity);
                                 Detail.setTraders_license_type_name(traders_license_type_name);
                                 Detail.setMobileno(mobileno);
                                 Detail.setTradersdetails_id(tradersdetails_id);
                                 Detail.setLb_sno(lb_sno);
                                 Detail.setTradedetails_id(tradedetails_id);
                                 Detail.setDescription_en(description_en);
                                 Detail.setDescription_ta(description_ta);
//                                 Detail.setTraderate(traderate);
                                 Detail.setTraders_rate(traders_rate);
                                 Detail.setTrade_date(date);
                                 Detail.setTradersperiod(tradersperiod);
                                 Detail.setTraderstypee(traderstypee);
                                 Detail.setEmail(email);
                                 Detail.setLicencetypeid(licencetypeid);
                                 Detail.setApgenderId(apgender);
                                 Detail.setApage(apage);
                                 Detail.setApfathername_en(apfathername_en);
                                 Detail.setStatecode(statecode);
                                 Detail.setDcode(dcode);
                                 Detail.setApname_ta(apname_ta);
                                 Detail.setWardId(String.valueOf(wardid));
                                 Detail.setStreetId(String.valueOf(streetid));
                                 Detail.setOwnerStatus(ownerStatus);
                                 Detail.setMotorStatus(motorStatus);
                                 Detail.setGeneratorStatus(generatorStatus);
                                 Detail.setPropertyStatus(propertyStatus);
                                 Detail.setProfesstionlStatus(professtionlStatus);
                                 Detail.setMotor_type_id(motor_type_id);
                                 Detail.setAmount_range_id(amount_range_id);
                                 Detail.setGenerator_range_id(generator_range_id);
                                 Detail.setPropertyTaxAssessmentNumber(propertyTaxAssessmentNumber);
                                 Detail.setDocument(document);
                                 Detail.setRemark(remark);



                                 Detail.setAnnual_sale_production_amount(annual_sale_production_amount);
                                 Detail.setAnnual_sale_production_range(annual_sale_production_range);
                                 Detail.setGenerator_range(generator_range);
                                 Detail.setGenerator_range_amount(generator_range_amount);
                                 Detail.setMotor_range(motor_range);
                                 Detail.setMotor_range_amount(motor_range_amount);
                                 Detail.setStreet_name_ta(street_name_ta);
                                 Detail.setStreet_name_en(street_name_en);
                                 Detail.setWard_name_en(ward_name_en);
                                 Detail.setWard_name_ta(ward_name_ta);




                                 tradersList.add(Detail);

                                 TPtaxModel img = new TPtaxModel();
                                 img.setImageByte(ByteArray);
                                 tradersImageList.add(img);

                             }
                     }else if(!mobileNo.equals("") && !mobileNo.isEmpty() && mobileNo != null){
                             if(mobileNo.equals(mobileno)){
                                 TPtaxModel Detail = new TPtaxModel();
                                 Detail.setTraderName(apname_en);
//                                 Detail.setTraderCode(lb_tradecode);
                                 Detail.setTraders_typ(traders_type);
                                 Detail.setTraderCode(lb_traderscode);
                                 Detail.setDoorno(doorno);
                                 Detail.setApfathername_ta(apfathername_ta);
                                 Detail.setLicenceValidity(licence_validity);
                                 Detail.setTraders_license_type_name(traders_license_type_name);
                                 Detail.setMobileno(mobileno);
                                 Detail.setTradersdetails_id(tradersdetails_id);
                                 Detail.setLb_sno(lb_sno);
                                 Detail.setTradedetails_id(tradedetails_id);
                                 Detail.setDescription_en(description_en);
                                 Detail.setDescription_ta(description_ta);
//                                 Detail.setTraderate(traderate);
                                 Detail.setTraders_rate(traders_rate);
                                 Detail.setTrade_date(date);
                                 Detail.setTradersperiod(tradersperiod);
                                 Detail.setTraderstypee(traderstypee);
                                 Detail.setEmail(email);
                                 Detail.setLicencetypeid(licencetypeid);
                                 Detail.setApgenderId(apgender);
                                 Detail.setApage(apage);
                                 Detail.setApfathername_en(apfathername_en);
                                 Detail.setStatecode(statecode);
                                 Detail.setDcode(dcode);
                                 Detail.setApname_ta(apname_ta);
                                 Detail.setWardId(String.valueOf(wardid));
                                 Detail.setStreetId(String.valueOf(streetid));
                                 Detail.setOwnerStatus(ownerStatus);
                                 Detail.setMotorStatus(motorStatus);
                                 Detail.setGeneratorStatus(generatorStatus);
                                 Detail.setPropertyStatus(propertyStatus);
                                 Detail.setProfesstionlStatus(professtionlStatus);
                                 Detail.setMotor_type_id(motor_type_id);
                                 Detail.setAmount_range_id(amount_range_id);
                                 Detail.setGenerator_range_id(generator_range_id);
                                 Detail.setPropertyTaxAssessmentNumber(propertyTaxAssessmentNumber);
                                 Detail.setDocument(document);
                                 Detail.setRemark(remark);


                                 Detail.setAnnual_sale_production_amount(annual_sale_production_amount);
                                 Detail.setAnnual_sale_production_range(annual_sale_production_range);
                                 Detail.setGenerator_range(generator_range);
                                 Detail.setGenerator_range_amount(generator_range_amount);
                                 Detail.setMotor_range(motor_range);
                                 Detail.setMotor_range_amount(motor_range_amount);
                                 Detail.setStreet_name_ta(street_name_ta);
                                 Detail.setStreet_name_en(street_name_en);
                                 Detail.setWard_name_en(ward_name_en);
                                 Detail.setWard_name_ta(ward_name_ta);

                                 tradersList.add(Detail);

                                 TPtaxModel img = new TPtaxModel();
                                 img.setImageByte(ByteArray);
                                 tradersImageList.add(img);
                             }
                     }else if(wardId != null && !wardId.equals("0") && !wardId.isEmpty() ){
                        if(streetId != null &&!streetId.equals("0")&& !streetId.isEmpty() ){
                            if(wardId.equals(wardid) && streetId.equals(streetid)){
                                TPtaxModel Detail = new TPtaxModel();
                                Detail.setTraderName(apname_en);
//                                Detail.setTraderCode(lb_tradecode);
                                Detail.setTraders_typ(traders_type);
                                Detail.setTraderCode(lb_traderscode);
                                Detail.setDoorno(doorno);
                                Detail.setApfathername_ta(apfathername_ta);
                                Detail.setLicenceValidity(licence_validity);
                                Detail.setTraders_license_type_name(traders_license_type_name);
                                Detail.setMobileno(mobileno);
                                Detail.setTradersdetails_id(tradersdetails_id);
                                Detail.setLb_sno(lb_sno);
                                Detail.setTradedetails_id(tradedetails_id);
                                Detail.setDescription_en(description_en);
                                Detail.setDescription_ta(description_ta);
//                                Detail.setTraderate(traderate);
                                Detail.setTraders_rate(traders_rate);
                                Detail.setTrade_date(date);
                                Detail.setTradersperiod(tradersperiod);
                                Detail.setTraderstypee(traderstypee);
                                Detail.setEmail(email);
                                Detail.setLicencetypeid(licencetypeid);
                                Detail.setApgenderId(apgender);
                                Detail.setApage(apage);
                                Detail.setApfathername_en(apfathername_en);
                                Detail.setStatecode(statecode);
                                Detail.setDcode(dcode);
                                Detail.setApname_ta(apname_ta);
                                Detail.setWardId(String.valueOf(wardid));
                                Detail.setStreetId(String.valueOf(streetid));
                                Detail.setOwnerStatus(ownerStatus);
                                Detail.setMotorStatus(motorStatus);
                                Detail.setGeneratorStatus(generatorStatus);
                                Detail.setPropertyStatus(propertyStatus);
                                Detail.setProfesstionlStatus(professtionlStatus);
                                Detail.setMotor_type_id(motor_type_id);
                                Detail.setAmount_range_id(amount_range_id);
                                Detail.setGenerator_range_id(generator_range_id);
                                Detail.setPropertyTaxAssessmentNumber(propertyTaxAssessmentNumber);
                                Detail.setDocument(document);
                                Detail.setRemark(remark);

                                Detail.setAnnual_sale_production_amount(annual_sale_production_amount);
                                Detail.setAnnual_sale_production_range(annual_sale_production_range);
                                Detail.setGenerator_range(generator_range);
                                Detail.setGenerator_range_amount(generator_range_amount);
                                Detail.setMotor_range(motor_range);
                                Detail.setMotor_range_amount(motor_range_amount);
                                Detail.setStreet_name_ta(street_name_ta);
                                Detail.setStreet_name_en(street_name_en);
                                Detail.setWard_name_en(ward_name_en);
                                Detail.setWard_name_ta(ward_name_ta);


                                tradersList.add(Detail);

                                TPtaxModel img = new TPtaxModel();
                                img.setImageByte(ByteArray);
                                tradersImageList.add(img);
                            }
                        }
                    }
                }
                Collections.sort(tradersList, (lhs, rhs) -> lhs.getTraderName().compareTo(rhs.getTraderName()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("size >>"+ tradersList.size());
        System.out.println("size2 >>"+ tradersImageList.size());

        if(tradersList != null  && tradersList.size() == 1) {
            Intent intent = new Intent( this, ExistTradeViewClass.class);
            intent.putExtra("position", 0);
            intent.putExtra("tradersList", tradersList);
            intent.putExtra("tradersImageList", tradersImageList);
            intent.putExtra("tradersImagePosition", 0);
            intent.putExtra("flag",true);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }else if(tradersList != null && tradersList.size() >1 ) {
            JSONArray jArray = new JSONArray();
            for (int i = 0; i < tradersImageList.size(); i++) {
                JSONObject jGroup = new JSONObject();// /sub Object
                try {
                    String value = new String(tradersImageList.get(i).getImageByte());
                    jGroup.put("img", value);
                    jArray.put(jGroup);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            prefManager.setTraderImageList(jArray.toString());
            Intent intent = new Intent( this, ExistingTradeList.class);
            intent.putExtra("tradersList", tradersList);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }else {
            Utils.showAlert(this, "No Data Found!");
        }

    }

        public void clearDetails() {
        existingTradeLicenceBinding.streetsName.setAdapter(null);
        selectedStreetId="0";
        selectedStreetName="";
        existingTradeLicenceBinding.wardNo.setSelection(0);
        selectedWardId="0";
        selectedWardName="";
        existingTradeLicenceBinding.mobileNo.setText("");
        existingTradeLicenceBinding.tradeCodeSpinner.setSelection(0);
        selectedTrdeCodeDetailsID = "0";
        selectedTradeCode = "";
    }
    public void validateDetails() {

        if ((!selectedTradeCode.isEmpty()&& !selectedTradeCode.equals("Select TradeCode")) || !existingTradeLicenceBinding.mobileNo.getText().toString().isEmpty() || (!selectedWardName.isEmpty()&& !selectedWardName.equals("Select Ward")) || ( !selectedStreetName.isEmpty()&& !selectedStreetName.equals("Select Street"))) {

                if(!selectedWardName.isEmpty() && !selectedWardName.equals("Select Ward")){
                    if(!selectedStreetName.isEmpty()&& !selectedStreetName.equals("Select Street")){
                        existTradeLicenceSubmit(selectedWardId,selectedStreetId,selectedTrdeCodeDetailsID,existingTradeLicenceBinding.mobileNo.getText().toString());
                    }else {
                        Utils.showAlert(this, "Please Select Street!");
                    }
                }else if(!selectedTradeCode.isEmpty() && !selectedTradeCode.equals("Select TradeCode")){
                    existTradeLicenceSubmit("0","0",selectedTrdeCodeDetailsID,"");

                }else  {
                    existTradeLicenceSubmit("0","0","0",existingTradeLicenceBinding.mobileNo.getText().toString());
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
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status ;
            if ("TradeLicenseTradersList".equals(urlType) && responseObj != null) {
                String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                Log.d("Tradersdatadecry", "" + userDataDecrypt);
                JSONObject jsonObject = new JSONObject(userDataDecrypt);

                status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.DATA);
                    prefManager.setTradersList(jsonarray.toString());
                    Log.d("TradeLicenseTradersList", "" + jsonObject);

                } }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void OnError(VolleyError volleyError) {

    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
