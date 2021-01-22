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

import java.io.Serializable;
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
    ArrayList<TPtaxModel> tradersList;

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
        Log.d("TradeLicenseTradersList", "" + authKey);
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

    public void existTradeLicenceSubmit(String wardId,String streetId,String traderCode,String mobileNo) {

        tradersList = new ArrayList<TPtaxModel>();
        JSONArray jsonarray= null;
        try {
            jsonarray = new JSONArray(prefManager.getTradersList());
            if(jsonarray != null && jsonarray.length() >0) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                     String tradersdetails_id= jsonobject.getString("tradersdetails_id");
                     String lb_sno= jsonobject.getString("lb_sno");
                     String tradedetails_id= jsonobject.getString("tradedetails_id");
                     String lb_tradecode= jsonobject.getString("lb_tradecode");
                     String description_en= jsonobject.getString("description_en");
                     String description_ta= jsonobject.getString("description_ta");
                     String traderate= jsonobject.getString("traderate");
                     String lb_traderscode= jsonobject.getString("lb_traderscode");
                     String traders_rate= jsonobject.getString("traders_rate");
                     String traders_type= jsonobject.getString("traders_type");
                     String date= jsonobject.getString("date");
                     String tradersperiod= jsonobject.getString("tradersperiod");
                     String tradedesct= jsonobject.getString("tradedesct");
                     String tradedesce= jsonobject.getString("tradedesce");
                     String wardid= jsonobject.getString("wardid");
                     String streetid= jsonobject.getString("streetid");
                     String doorno= jsonobject.getString("doorno");
                     String licencefor= jsonobject.getString("licencefor");
                     String fin_year= jsonobject.getString("fin_year");
                     String traderstypee= jsonobject.getString("traderstypee");
                     String demandtype= jsonobject.getString("demandtype");
                     String onlineapplicationno= jsonobject.getString("onlineapplicationno");
                     String mobileno= jsonobject.getString("mobileno");
                     String email= jsonobject.getString("email");
                     String paymentstatus= jsonobject.getString("paymentstatus");
                     String licencetypeid= jsonobject.getString("licencetypeid");
                     String traders_license_type_name= jsonobject.getString("traders_license_type_name");
                     String apgender= jsonobject.getString("apgender");
                     String apage= jsonobject.getString("apage");
                     String apfathername_ta= jsonobject.getString("apfathername_ta");
                     String apfathername_en= jsonobject.getString("apfathername_en");
                     String licenceno= jsonobject.getString("licenceno");
                     String paymentdate= jsonobject.getString("paymentdate");
                     String statecode= jsonobject.getString("statecode");
                     String dcode= jsonobject.getString("dcode");
                     String lbcode= jsonobject.getString("lbcode");
                     String apname_ta= jsonobject.getString("apname_ta");
                     String apname_en= jsonobject.getString("apname_en");
                     String establishment_name_ta= jsonobject.getString("establishment_name_ta");
                     String establishment_name_en= jsonobject.getString("establishment_name_en");
                     String licence_validity= jsonobject.getString("licence_validity");
                     if(!wardId.equals("") && !wardId.isEmpty()){
                         if(!streetId.equals("") && !streetId.isEmpty()){
                             if(wardId.equals(wardid) && streetId.equals(streetid)){
                                 TPtaxModel Detail = new TPtaxModel();
                                 Detail.setTraderName(apname_en);
                                 Detail.setTraderCode(lb_traderscode);
                                 Detail.setTraders_typ(traders_type);
                                 Detail.setTradedesct(tradedesct);
                                 Detail.setDoorno(doorno);
                                 Detail.setApfathername_ta(apfathername_ta);
                                 Detail.setEstablishment_name_ta(establishment_name_ta);
                                 Detail.setLicenceValidity(licence_validity);
                                 Detail.setTraders_license_type_name(traders_license_type_name);
                                 Detail.setTraderPayment(paymentstatus);
                                 Detail.setMobileno(mobileno);
                                 Detail.setPaymentdate(paymentdate);
                                 Detail.setTradersdetails_id(tradersdetails_id);
                                 Detail.setLb_sno(lb_sno);
                                 Detail.setTradedetails_id(tradedetails_id);
                                 Detail.setDescription_en(description_en);
                                 Detail.setDescription_ta(description_ta);
                                 Detail.setTraderate(traderate);
                                 Detail.setTraders_rate(traders_rate);
                                 Detail.setTrade_date(date);
                                 Detail.setTradersperiod(tradersperiod);
                                 Detail.setTradedesce(tradedesce);
                                 Detail.setLicencefor(licencefor);
                                 Detail.setFin_year(fin_year);
                                 Detail.setTraderstypee(traderstypee);
                                 Detail.setDemandtype(demandtype);
                                 Detail.setOnlineapplicationno(onlineapplicationno);
                                 Detail.setEmail(email);
                                 Detail.setLicencetypeid(licencetypeid);
                                 Detail.setApgender(apgender);
                                 Detail.setApage(apage);
                                 Detail.setApfathername_en(apfathername_en);
                                 Detail.setLicenceno(licenceno);
                                 Detail.setStatecode(statecode);
                                 Detail.setDcode(dcode);
                                 Detail.setLbcode(lbcode);
                                 Detail.setApname_ta(apname_ta);
                                 Detail.setEstablishment_name_en(establishment_name_en);

                                 tradersList.add(Detail);
                             }
                         }
                     }else if(!traderCode.equals("") && !traderCode.isEmpty()){
                             if(traderCode.equals(lb_tradecode)){
                                 TPtaxModel Detail = new TPtaxModel();
                                 Detail.setTraderName(apname_en);
                                 Detail.setTraderCode(lb_traderscode);
                                 Detail.setTraders_typ(traders_type);
                                 Detail.setTradedesct(tradedesct);
                                 Detail.setDoorno(doorno);
                                 Detail.setApfathername_ta(apfathername_ta);
                                 Detail.setEstablishment_name_ta(establishment_name_ta);
                                 Detail.setLicenceValidity(licence_validity);
                                 Detail.setTraders_license_type_name(traders_license_type_name);
                                 Detail.setTraderPayment(paymentstatus);
                                 Detail.setMobileno(mobileno);
                                 Detail.setPaymentdate(paymentdate);
                                 Detail.setTradersdetails_id(tradersdetails_id);
                                 Detail.setLb_sno(lb_sno);
                                 Detail.setTradedetails_id(tradedetails_id);
                                 Detail.setDescription_en(description_en);
                                 Detail.setDescription_ta(description_ta);
                                 Detail.setTraderate(traderate);
                                 Detail.setTraders_rate(traders_rate);
                                 Detail.setTrade_date(date);
                                 Detail.setTradersperiod(tradersperiod);
                                 Detail.setTradedesce(tradedesce);
                                 Detail.setLicencefor(licencefor);
                                 Detail.setFin_year(fin_year);
                                 Detail.setTraderstypee(traderstypee);
                                 Detail.setDemandtype(demandtype);
                                 Detail.setOnlineapplicationno(onlineapplicationno);
                                 Detail.setEmail(email);
                                 Detail.setLicencetypeid(licencetypeid);
                                 Detail.setApgender(apgender);
                                 Detail.setApage(apage);
                                 Detail.setApfathername_en(apfathername_en);
                                 Detail.setLicenceno(licenceno);
                                 Detail.setStatecode(statecode);
                                 Detail.setDcode(dcode);
                                 Detail.setLbcode(lbcode);
                                 Detail.setApname_ta(apname_ta);
                                 Detail.setEstablishment_name_en(establishment_name_en);

                                 tradersList.add(Detail);
                             }
                     }else if(!mobileNo.equals("") && !mobileNo.isEmpty()){
                             if(mobileNo.equals(mobileno)){
                                 TPtaxModel Detail = new TPtaxModel();
                                 Detail.setTraderName(apname_en);
                                 Detail.setTraderCode(lb_traderscode);
                                 Detail.setTraders_typ(traders_type);
                                 Detail.setTradedesct(tradedesct);
                                 Detail.setDoorno(doorno);
                                 Detail.setApfathername_ta(apfathername_ta);
                                 Detail.setEstablishment_name_ta(establishment_name_ta);
                                 Detail.setLicenceValidity(licence_validity);
                                 Detail.setTraders_license_type_name(traders_license_type_name);
                                 Detail.setTraderPayment(paymentstatus);
                                 Detail.setMobileno(mobileno);
                                 Detail.setPaymentdate(paymentdate);
                                 Detail.setTradersdetails_id(tradersdetails_id);
                                 Detail.setLb_sno(lb_sno);
                                 Detail.setTradedetails_id(tradedetails_id);
                                 Detail.setDescription_en(description_en);
                                 Detail.setDescription_ta(description_ta);
                                 Detail.setTraderate(traderate);
                                 Detail.setTraders_rate(traders_rate);
                                 Detail.setTrade_date(date);
                                 Detail.setTradersperiod(tradersperiod);
                                 Detail.setTradedesce(tradedesce);
                                 Detail.setLicencefor(licencefor);
                                 Detail.setFin_year(fin_year);
                                 Detail.setTraderstypee(traderstypee);
                                 Detail.setDemandtype(demandtype);
                                 Detail.setOnlineapplicationno(onlineapplicationno);
                                 Detail.setEmail(email);
                                 Detail.setLicencetypeid(licencetypeid);
                                 Detail.setApgender(apgender);
                                 Detail.setApage(apage);
                                 Detail.setApfathername_en(apfathername_en);
                                 Detail.setLicenceno(licenceno);
                                 Detail.setStatecode(statecode);
                                 Detail.setDcode(dcode);
                                 Detail.setLbcode(lbcode);
                                 Detail.setApname_ta(apname_ta);
                                 Detail.setEstablishment_name_en(establishment_name_en);

                                 tradersList.add(Detail);
                             }
                     }
                }
                Collections.sort(tradersList, (lhs, rhs) -> lhs.getTraderName().compareTo(rhs.getTraderName()));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(tradersList != null  && tradersList.size() == 1) {
            Intent intent = new Intent( this, ExistingTradeSubmit.class);
            intent.putExtra("tradersList", (Serializable)tradersList);
            intent.putExtra("position", 0);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }else if(tradersList != null && tradersList.size() >1 ) {
            Intent intent = new Intent( this, ExistingTradeList.class);
            startActivity(intent);
            intent.putExtra("tradersList", (Serializable)tradersList);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }else {
            Utils.showAlert(this, "No Data Found!");
        }

       /* Intent intent = new Intent( this, ExistingTradeList.class);
        startActivity(intent);
        intent.putExtra("tradersList", (Serializable)tradersList);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);*/
    }

    public void validateDetails() {

        if (!existingTradeLicenceBinding.tradersCode.getText().toString().isEmpty() || !existingTradeLicenceBinding.mobileNo.getText().toString().isEmpty() || (!selectedWardName.isEmpty()&& !selectedWardName.equals("Select Ward")) || ( !selectedStreetName.isEmpty()&& !selectedStreetName.equals("Select Street"))) {

                if(!selectedWardName.isEmpty() && !selectedWardName.equals("Select Ward")){
                    if(!selectedStreetName.isEmpty()&& !selectedStreetName.equals("Select Street")){
                        existTradeLicenceSubmit(selectedWardId,selectedStreetId,existingTradeLicenceBinding.tradersCode.getText().toString(),existingTradeLicenceBinding.mobileNo.getText().toString());
                    }else {
                        Utils.showAlert(this, "Please Select Street!");
                    }
                }else {
                    existTradeLicenceSubmit(selectedWardId,selectedStreetId,existingTradeLicenceBinding.tradersCode.getText().toString(),existingTradeLicenceBinding.mobileNo.getText().toString());
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
                String user_data = responseObj.getString(AppConstant.ENCODE_DATA);
                String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                Log.d("userdatadecry", "" + userDataDecrypt);
                JSONObject jsonObject = new JSONObject(userDataDecrypt);

                status = jsonObject.getString(AppConstant.KEY_STATUS);
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

}
