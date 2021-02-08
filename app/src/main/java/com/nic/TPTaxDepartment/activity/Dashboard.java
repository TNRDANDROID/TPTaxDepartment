package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.MediaRouteButton;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.databinding.DashboardBinding;
import com.nic.TPTaxDepartment.databinding.NewDashboardBinding;
import com.nic.TPTaxDepartment.dialog.MyDialog;
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
import java.util.Map;

public class Dashboard extends AppCompatActivity implements MyDialog.myOnClickListener, Api.ServerResponseListener {
    private static LinearLayout sync_layout;
    public com.nic.TPTaxDepartment.dataBase.dbData dbData = new dbData(this);
    private BottomAppBar bar;
    private DashboardBinding dashboardBinding;
    private NewDashboardBinding newDashboardBinding;
    ArrayList<TPtaxModel> pendingList = new ArrayList<>();
    final Handler handler = new Handler();
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private PrefManager prefManager;
    ArrayList<CommonModel> traderLicenseTypeList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newDashboardBinding = DataBindingUtil.setContentView(this, R.layout.new_dashboard);
        newDashboardBinding.setActivity(this);
        prefManager = new PrefManager(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sync_layout =(LinearLayout)  findViewById(R.id.sync_layout);


        newDashboardBinding.voteprogresscard.setTranslationX(800);
        newDashboardBinding.attendanecard.setTranslationX(800);
        newDashboardBinding.cameracard.setTranslationX(800);
        newDashboardBinding.votecountcard.setTranslationX(800);
        newDashboardBinding.viewPollingStationImage.setTranslationX(800);
        newDashboardBinding.pendingScreen.setTranslationX(800);


        newDashboardBinding.voteprogresscard.setAlpha(0);
        newDashboardBinding.attendanecard.setAlpha(0);
        newDashboardBinding.cameracard.setAlpha(0);
        newDashboardBinding.votecountcard.setAlpha(0);
        newDashboardBinding.viewPollingStationImage.setAlpha(0);
        newDashboardBinding.pendingScreen.setAlpha(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newDashboardBinding.voteprogresscard.animate().translationX(0).alpha(1).setDuration(1400).setStartDelay(400).start();
                newDashboardBinding.attendanecard.animate().translationX(0).alpha(1).setDuration(1500).setStartDelay(600).start();
                newDashboardBinding.cameracard.animate().translationX(0).alpha(1).setDuration(1600).setStartDelay(800).start();
                newDashboardBinding.votecountcard.animate().translationX(0).alpha(1).setDuration(1700).setStartDelay(1000).start();
                newDashboardBinding.viewPollingStationImage.animate().translationX(0).alpha(1).setDuration(1800).setStartDelay(1200).start();
                newDashboardBinding.pendingScreen.animate().translationX(0).alpha(1).setDuration(1800).setStartDelay(1200).start();
            }
        }, 800);


        Animation anim = new ScaleAnimation(
                0.95f, 1f, // Start and end values for the X axis scaling
                0.95f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        anim.setRepeatMode(Animation.INFINITE);
        anim.setRepeatCount(Animation.INFINITE);

        newDashboardBinding.tradeTv.startAnimation(anim);
        newDashboardBinding.fieldTv.startAnimation(anim);
        newDashboardBinding.assessTv.startAnimation(anim);
        newDashboardBinding.dailcollTv.startAnimation(anim);
        newDashboardBinding.pendingTv.startAnimation(anim);

        syncvisiblity();
        if(Utils.isOnline()) {
            getTaxTypeList();
            getTaxTypeListFieldVisit();
            getWardList();
            getStreetList();
            getLicenceValidityList();
            getLicenceTypeList();
            getGenderList();
            getTradeList();
            getFieldVisitStatus();
            getServiceFieldVisitTypes();
        }
       /* if(getTaxTypeCount()<= 0){
            getTaxTypeList();
        }
        if(getWardCount()<= 0){
            getWardList();
        }
       if(getStreetCount()<= 0){
           getStreetList();
        }*/



    }
    public void getGenderList() {
        try {
            new ApiService(this).makeJSONObjectRequest("Gender", Api.Method.POST, UrlGenerator.prodOpenUrl(), genderParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTradeList() {
        try {
            new ApiService(this).makeJSONObjectRequest("TradeLicenseTradeList", Api.Method.POST, UrlGenerator.TradersUrl(), encryptTradeJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JSONObject genderParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "OS_Gender");
        return dataSet;
    }

    public JSONObject encryptTradeJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), tradeJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("TradeLicenseTradeList", "" + authKey);
        return dataSet;
    }
    public JSONObject tradeJsonParams() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "TradeLicenseTradeList");
        return dataSet;
    }

    public void tradeSilenceScreen(){
        Intent intent = new Intent( this, TradeLicenceScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void fieldVisitScreen(){
        Intent intent = new Intent( this, FieldVisit.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void assessmentStatusScreen(){
        Intent intent = new Intent( this, AssessmentStatus.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void dailyCollectionScreen(){
        Intent intent = new Intent( this, DailyCollection.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void pendingScreen(){
        Intent intent = new Intent( this, PendingScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public static void syncvisiblity() {

        Cursor pendingList = db.rawQuery("select * from "+DBHelper.SAVE_FIELD_VISIT, null);
        int count = pendingList.getCount();
        Log.d("pending_count",String.valueOf(count));
        if (count > 0) {
            sync_layout.setVisibility(View.GONE);
           // count_tv.setText(String.valueOf(count));
        }else {
            sync_layout.setVisibility(View.GONE);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


    public void closeApplication() {
        new MyDialog(this).exitDialog(this, "Are you sure you want to Logout?", "Logout");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                new MyDialog(this).exitDialog(this, "Are you sure you want to exit ?", "Exit");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onButtonClick(AlertDialog alertDialog, String type) {
        alertDialog.dismiss();
        if ("Exit".equalsIgnoreCase(type)) {
            onBackPressed();
        } else {

            Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", false);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }

    public void getFieldVisitStatus() {
        try {
            new ApiService(this).makeJSONObjectRequest("FieldVisitStatus", Api.Method.POST, UrlGenerator.prodOpenUrl(), fieldVisitStatusJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getServiceFieldVisitTypes() {
        try {
            new ApiService(this).makeJSONObjectRequest("ServiceFieldVisitTypes", Api.Method.POST, UrlGenerator.prodOpenUrl(), serviceListFieldVisitTypesJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getWardList() {
        try {
            new ApiService(this).makeJSONObjectRequest("WardList", Api.Method.POST, UrlGenerator.prodOpenUrl(), wardJsonParam(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getTaxTypeList() {
        try {
            new ApiService(this).makeJSONObjectRequest("TaxTypeList", Api.Method.POST, UrlGenerator.prodOpenUrl(), taxTpeJsonParam(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getTaxTypeListFieldVisit() {
        try {
            new ApiService(this).makeJSONObjectRequest("TaxTypeListFieldVisit", Api.Method.POST, UrlGenerator.prodOpenUrl(), taxTypeFieldVisitJsonParam(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JSONObject taxTpeJsonParam() throws JSONException {
        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"OS_TaxTypes_Assessment_Status");
        Log.d("params", "" + data);
        return data;
    }

    public JSONObject fieldVisitStatusJsonParams() throws JSONException {
        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"OS_FieldVisitStatus");
        Log.d("params", "" + data);
        return data;
    }
    public JSONObject serviceListFieldVisitTypesJsonParams() throws JSONException {
        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"OS_ServiceListFieldVisit");
        Log.d("params", "" + data);
        return data;
    }
    public JSONObject taxTypeFieldVisitJsonParam() throws JSONException {
        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"OS_TaxTypesFieldVisit");
        Log.d("params", "" + data);
        return data;
    }
    public JSONObject wardJsonParam() throws JSONException {
        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"OS_Ward");
        data.put(AppConstant.KEY_STATE_CODE,prefManager.getStateCode());
        data.put(AppConstant.KEY_DISTRICT_CODE,prefManager.getDistrictCode());
        data.put(AppConstant.KEY_LB_CODE,prefManager.getTpCode());
        Log.d("params", "" + data);
        return data;
    }
    public void getStreetList() {
        try {
            new ApiService(this).makeJSONObjectRequest("StreetList", Api.Method.POST, UrlGenerator.prodOpenUrl(), streetJsonParam(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getLicenceValidityList() {
        try {
            new ApiService(this).makeJSONObjectRequest("LicenceValidityList", Api.Method.POST, UrlGenerator.prodOpenUrl(), licenceValidityListJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JSONObject streetJsonParam() throws JSONException {

        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"OS_Street");
        data.put(AppConstant.KEY_STATE_CODE,prefManager.getStateCode());
        data.put(AppConstant.KEY_DISTRICT_CODE,prefManager.getDistrictCode());
        data.put(AppConstant.KEY_LB_CODE,prefManager.getTpCode());
        Log.d("params", "" + data);
        return data;
    }
    public void getLicenceTypeList() {
        try {
            new ApiService(this).makeJSONObjectRequest("TraderLicenseTypeList", Api.Method.POST, UrlGenerator.TradersUrl(), licencelistJsonEncryptParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject licencelistJsonEncryptParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), licencelistJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("TraderLicenseTypeList", "" + authKey);
        return dataSet;
    }

    public JSONObject licencelistJsonParams() throws JSONException{


        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "TraderLicenseTypeList");
        return dataSet;
    }
    public JSONObject licenceValidityListJsonParams() throws JSONException{


        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "OS_Finyear");
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status;
            if ("WardList".equals(urlType) && responseObj != null) {
                status = responseObj.getString(AppConstant.KEY_STATUS);
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    db.execSQL("delete from "+ DBHelper.WARD_LIST);
                    JSONArray jsonarray = new JSONArray();
                    jsonarray=responseObj.getJSONArray(AppConstant.DATA);
                    if(jsonarray != null && jsonarray.length() >0) {
                        System.out.println("DAta: "+ Toast.makeText(Dashboard.this,"Deleted",Toast.LENGTH_SHORT));
                        prefManager.setWardList(jsonarray.toString());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String statecode = Utils.NotNullString(jsonobject.getString("statecode"));
                            String dcode = Utils.NotNullString(jsonobject.getString("dcode"));
                            String lbcode = Utils.NotNullString(jsonobject.getString("lbcode"));
                            String ward_id = Utils.NotNullString(jsonobject.getString("ward_id"));
                            String ward_code = Utils.NotNullString(jsonobject.getString("ward_code"));
                            String ward_name_en = Utils.NotNullString(jsonobject.getString("ward_name_en"));
                            String ward_name_ta = Utils.NotNullString(jsonobject.getString("ward_name_ta"));
                            ContentValues fieldValue = new ContentValues();
                            fieldValue.put(AppConstant.KEY_STATE_CODE, statecode);
                            fieldValue.put(AppConstant.DISTRICT_CODE, dcode);
                            fieldValue.put(AppConstant.LB_CODE, lbcode);
                            fieldValue.put(AppConstant.WARD_ID, ward_id);
                            fieldValue.put(AppConstant.WARD_CODE, ward_code);
                            fieldValue.put(AppConstant.WARD_NAME_EN, ward_name_en);
                            fieldValue.put(AppConstant.WARD_NAME_TA, ward_name_ta);
                            db.insert(DBHelper.WARD_LIST, null, fieldValue);
                        }
                    }
                }

                Log.d("wards", "" + responseObj);
            }
            if ("StreetList".equals(urlType) && responseObj != null) {
                status = Utils.NotNullString(responseObj.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS")) {
                    db.execSQL("delete from "+ DBHelper.STREET_LIST);
                    JSONArray jsonarray = new JSONArray();
                    jsonarray = responseObj.getJSONArray(AppConstant.DATA);
                    if(jsonarray != null && jsonarray.length() >0) {
                        prefManager.setStreetList(jsonarray.toString());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String statecode = Utils.NotNullString(jsonobject.getString("statecode"));
                            String dcode = Utils.NotNullString(jsonobject.getString("dcode"));
                            String lbcode = Utils.NotNullString(jsonobject.getString("lbcode"));
                            String ward_id = Utils.NotNullString(jsonobject.getString("wardid"));
                            String ward_code = Utils.NotNullString(jsonobject.getString("ward_code"));
                            String streetid = Utils.NotNullString(jsonobject.getString("streetid"));
                            String street_code = Utils.NotNullString(jsonobject.getString("street_code"));
                            String street_name_en = Utils.NotNullString(jsonobject.getString("street_name_en"));
                            String street_name_ta = Utils.NotNullString(jsonobject.getString("street_name_ta"));
                            String building_zone = Utils.NotNullString(jsonobject.getString("building_zone"));
                            ContentValues fieldValue = new ContentValues();
                            fieldValue.put(AppConstant.KEY_STATE_CODE, statecode);
                            fieldValue.put(AppConstant.DISTRICT_CODE, dcode);
                            fieldValue.put(AppConstant.LB_CODE, lbcode);
                            fieldValue.put(AppConstant.WARD_ID, ward_id);
                            fieldValue.put(AppConstant.WARD_CODE, ward_code);
                            fieldValue.put(AppConstant.STREET_ID, streetid);
                            fieldValue.put(AppConstant.STREET_CODE, street_code);
                            fieldValue.put(AppConstant.STREET_NAME_EN, street_name_en);
                            fieldValue.put(AppConstant.STREET_NAME_TA, street_name_ta);
                            fieldValue.put(AppConstant.BUILDING_ZONE, building_zone);

                            db.insert(DBHelper.STREET_LIST, null, fieldValue);

                        }
                    }

                }
                Log.d("streets", "" + responseObj);
            }
            if ("TaxTypeList".equals(urlType) && responseObj != null) {
                status = responseObj.getString(AppConstant.KEY_STATUS);
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    db.execSQL("delete from "+ DBHelper.TAX_TYPE_LIST);
                    JSONArray jsonarray = new JSONArray();
                    jsonarray = responseObj.getJSONArray(AppConstant.DATA);
                    if(jsonarray != null && jsonarray.length() >0) {

                        prefManager.setTaxTypeList(jsonarray.toString());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int taxtypeid = jsonobject.getInt("taxtypeid");
                            String taxtypedesc_en = Utils.NotNullString(jsonobject.getString("taxtypedesc_en"));
                            String taxtypedesc_ta = Utils.NotNullString(jsonobject.getString("taxtypedesc_ta"));
                            String taxcollection_methodlogy = Utils.NotNullString(jsonobject.getString("taxcollection_methodlogy"));
                            int installmenttypeid = jsonobject.getInt("installmenttypeid");

                            ContentValues fieldValue = new ContentValues();
                            fieldValue.put(AppConstant.TAX_TYPE_ID, taxtypeid);
                            fieldValue.put(AppConstant.TAX_TYPE_DESC_EN, taxtypedesc_en);
                            fieldValue.put(AppConstant.TAX_TYPE_DESC_TA, taxtypedesc_ta);
                            fieldValue.put(AppConstant.TAX_COLLECTION_METHODLOGY, taxcollection_methodlogy);
                            fieldValue.put(AppConstant.INSTALLMENT_TYPE_ID, installmenttypeid);

                            db.insert(DBHelper.TAX_TYPE_LIST, null, fieldValue);

                        }
                    }

                }
                Log.d("TaxTypeList", "" + responseObj);
            }
            if ("TaxTypeListFieldVisit".equals(urlType) && responseObj != null) {
                status = Utils.NotNullString(responseObj.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    db.execSQL("delete from "+ DBHelper.TAX_TYPE_FIELD_VISIT_LIST);
                    JSONArray jsonarray = new JSONArray();
                    jsonarray = responseObj.getJSONArray(AppConstant.DATA);
                    if(jsonarray != null && jsonarray.length() >0) {

                        prefManager.setTaxTypeList(jsonarray.toString());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int taxtypeid = jsonobject.getInt("taxtypeid");
                            String taxtypedesc_en = Utils.NotNullString(jsonobject.getString("taxtypedesc_en"));
                            String taxtypedesc_ta = Utils.NotNullString(jsonobject.getString("taxtypedesc_ta"));
                            String taxcollection_methodlogy = Utils.NotNullString(jsonobject.getString("taxcollection_methodlogy"));
                            int installmenttypeid = jsonobject.getInt("installmenttypeid");

                            ContentValues fieldValue = new ContentValues();
                            fieldValue.put(AppConstant.TAX_TYPE_ID, taxtypeid);
                            fieldValue.put(AppConstant.TAX_TYPE_DESC_EN, taxtypedesc_en);
                            fieldValue.put(AppConstant.TAX_TYPE_DESC_TA, taxtypedesc_ta);
                            fieldValue.put(AppConstant.TAX_COLLECTION_METHODLOGY, taxcollection_methodlogy);
                            fieldValue.put(AppConstant.INSTALLMENT_TYPE_ID, installmenttypeid);

                            db.insert(DBHelper.TAX_TYPE_FIELD_VISIT_LIST, null, fieldValue);

                        }
                    }

                }
                Log.d("TaxTypeFieldVisitList", "" + responseObj);
            }
            if ("LicenceValidityList".equals(urlType) && responseObj != null) {
                status = responseObj.getString(AppConstant.KEY_STATUS);
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    db.execSQL("delete from "+ DBHelper.LICENCE_VALIDITY_LIST);
                    JSONArray jsonarray = new JSONArray();
                    jsonarray = responseObj.getJSONArray(AppConstant.DATA);
                    if(jsonarray != null && jsonarray.length() >0) {

                        prefManager.setTaxTypeList(jsonarray.toString());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            int fin_yearid = (jsonobject.getInt("fin_yearid"));
                            String fin_year = Utils.NotNullString(jsonobject.getString("fin_year"));
                            int from_fin_year = (jsonobject.getInt("from_fin_year"));
                            int from_fin_mon = jsonobject.getInt("from_fin_mon");
                            int to_fin_year = jsonobject.getInt("to_fin_year");
                            int to_fin_mon = jsonobject.getInt("to_fin_mon");

                            ContentValues fieldValue = new ContentValues();
                            fieldValue.put(AppConstant.FIN_YEAR_ID, fin_yearid);
                            fieldValue.put(AppConstant.FIN_YEAR, fin_year);
                            fieldValue.put(AppConstant.FROM_FIN_YEAR, from_fin_year);
                            fieldValue.put(AppConstant.FROM_FIN_MON, from_fin_mon);
                            fieldValue.put(AppConstant.TO_FIN_YEAR, to_fin_year);
                            fieldValue.put(AppConstant.TO_FIN_MON, to_fin_mon);

                            db.insert(DBHelper.LICENCE_VALIDITY_LIST, null, fieldValue);

                        }
                    }

                }
                Log.d("LicenceValidityList", "" + responseObj);
            }
            if ("TraderLicenseTypeList".equals(urlType)) {
                traderLicenseTypeList=new ArrayList<CommonModel>();
                String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                        Log.d("userdatadecry", "" + userDataDecrypt);
                        JSONObject jsonObject = new JSONObject(userDataDecrypt);
                         status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                         if (status.equalsIgnoreCase("SUCCESS") ) {
                             JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.DATA);
                             prefManager.setTraderLicenseTypeList(jsonarray.toString());
                             Log.d("TraderLicenseTypeList", "" + jsonObject);

                         }
            }
            if("TradeLicenseTradeList".equals(urlType)){
                String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                Log.d("userdatadecry", "" + userDataDecrypt);
                JSONObject jsonObject = new JSONObject(userDataDecrypt);
                status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    db.execSQL("delete from "+ DBHelper.TRADE_CODE_LIST);
                    JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.DATA);
                    Log.d("TraderLicenseTradeList", "" + jsonObject);
                    if(jsonarray != null && jsonarray.length() >0) {

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);

                            ContentValues fieldValue = new ContentValues();
                            fieldValue.put(AppConstant.TRADE_DETAILS_ID, Utils.NotNullString(jsonobject.getString(AppConstant.TRADE_DETAILS_ID)));
                            fieldValue.put(AppConstant.LB_TRADE_CODE, Utils.NotNullString(jsonobject.getString(AppConstant.LB_TRADE_CODE)));
                            fieldValue.put(AppConstant.DESCRIPTION_EN, Utils.NotNullString(jsonobject.getString(AppConstant.DESCRIPTION_EN)));
                            fieldValue.put(AppConstant.DESCRIPTION_TA, Utils.NotNullString(jsonobject.getString(AppConstant.DESCRIPTION_TA)));
                            fieldValue.put(AppConstant.DATE_FIELD, Utils.NotNullString(jsonobject.getString(AppConstant.DATE_FIELD)));
                            fieldValue.put(AppConstant.FINYEAR, Utils.NotNullString(jsonobject.getString(AppConstant.FINYEAR)));
                            fieldValue.put(AppConstant.TRADE_RATE, Utils.NotNullString(jsonobject.getString(AppConstant.TRADE_RATE)));
                            fieldValue.put(AppConstant.LICENSE_TYPE_ID, Utils.NotNullString(jsonobject.getString(AppConstant.LICENSE_TYPE_ID)));
                            fieldValue.put(AppConstant.LB_CODE, Utils.NotNullString(jsonobject.getString(AppConstant.LB_CODE)));
                            fieldValue.put(AppConstant.STATECODE, Utils.NotNullString(jsonobject.getString(AppConstant.STATECODE)));
                            fieldValue.put(AppConstant.DISTRICT_CODE, Utils.NotNullString(jsonobject.getString(AppConstant.DISTRICT_CODE)));

                            db.insert(DBHelper.TRADE_CODE_LIST, null, fieldValue);
                        }
                    }
                }
            }
            if ("Gender".equals(urlType) && responseObj != null) {
                 status = Utils.NotNullString(responseObj.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    db.execSQL("delete from "+ DBHelper.GENDER_LIST);
                    JSONArray jsonarray = responseObj.getJSONArray(AppConstant.DATA);
                    if(jsonarray != null && jsonarray.length() >0) {

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String gender_code = Utils.NotNullString(jsonobject.getString("gender_code"));
                            String gender_name_en = Utils.NotNullString(jsonobject.getString("gender_name_en"));
                            String gender_name_ta = Utils.NotNullString(jsonobject.getString("gender_name_ta"));
                            ContentValues fieldValue = new ContentValues();
                            fieldValue.put(AppConstant.GENDER_CODE, gender_code);
                            fieldValue.put(AppConstant.GENDER_EN, gender_name_en);
                            fieldValue.put(AppConstant.GENDER_TA, gender_name_ta);

                            db.insert(DBHelper.GENDER_LIST, null, fieldValue);
                        }
                    }
                }
                Log.d("Gender", "" + responseObj);
            }
            if ("FieldVisitStatus".equals(urlType) && responseObj != null) {
                status = Utils.NotNullString(responseObj.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    db.execSQL("delete from "+ DBHelper.FIELD_VISIT_STATUS);
                    JSONArray jsonarray = responseObj.getJSONArray(AppConstant.DATA);
                    if(jsonarray != null && jsonarray.length() >0) {

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String field_visit_status_id = Utils.NotNullString(jsonobject.getString("field_visit_status_id"));
                            String field_visit_status = Utils.NotNullString(jsonobject.getString("field_visit_status"));

                            ContentValues fieldValue = new ContentValues();
                            fieldValue.put(AppConstant.FIELD_VISIT_STATUS_ID, field_visit_status_id);
                            fieldValue.put(AppConstant.FIELD_VISIT_STATUS, field_visit_status);


                            db.insert(DBHelper.FIELD_VISIT_STATUS, null, fieldValue);
                        }
                    }
                }
                Log.d("Gender", "" + responseObj);
            }
            if ("ServiceFieldVisitTypes".equals(urlType) && responseObj != null) {
                status = Utils.NotNullString(responseObj.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    db.execSQL("delete from "+ DBHelper.SERVICE_LIST_FIELD_VISIT_TYPES);
                    JSONArray jsonarray = responseObj.getJSONArray(AppConstant.DATA);
                    if(jsonarray != null && jsonarray.length() >0) {

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String service_list_field_visit_taxtype_id = Utils.NotNullString(jsonobject.getString("taxtypeid"));
                            String service_list_field_visit_service_id = Utils.NotNullString(jsonobject.getString("serviceid"));
                            String service_list_field_visit_types_desc=Utils.NotNullString(jsonobject.getString("servicedesc"));

                            ContentValues fieldValue = new ContentValues();
                            fieldValue.put("service_list_field_visit_taxtype_id", service_list_field_visit_taxtype_id);
                            fieldValue.put("service_list_field_visit_service_id", service_list_field_visit_service_id);
                            fieldValue.put("service_list_field_visit_types_desc", service_list_field_visit_types_desc);


                            db.insert(DBHelper.SERVICE_LIST_FIELD_VISIT_TYPES, null, fieldValue);
                        }
                    }
                }
                Log.d("ServiceFieldVisitTypes", "" + responseObj);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void OnError(VolleyError volleyError) {

    }
    public int getWardCount() {
        String countQuery = "SELECT  * FROM " + DBHelper.WARD_LIST;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int getStreetCount() {
        String countQuery = "SELECT  * FROM " + DBHelper.STREET_LIST;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int getTaxTypeCount() {
        String countQuery = "SELECT  * FROM " + DBHelper.TAX_TYPE_LIST;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void logout() {
        int count=0;
        int count1=0;
        dbData.open();
        ArrayList<TPtaxModel> activityCount = dbData.getPendingActivity();
        String select_query= "SELECT * FROM " + DBHelper.SAVE_NEW_TRADER_DETAILS;
        String select_query1= "SELECT * FROM " + DBHelper.SAVE_FIELD_VISIT;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        Cursor cursor1 = Dashboard.db.rawQuery(select_query1, null);
        if(cursor.getCount()>0) {
            count=cursor.getCount();
        }
        if(cursor1.getCount()>0){
            count1=cursor1.getCount();
        }
            if (!Utils.isOnline()) {
            Utils.showAlert(this, "Logging out while offline may leads to loss of data!");
        } else {
            if (count1>0 ||count>0) {
                Utils.showAlert(this,"Sync all the data before logout!");

            }else{
                closeApplication();
            }
        }
    }

}
