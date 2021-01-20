package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.app.AlertDialog;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.Support.ProgressHUD;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.databinding.DashboardBinding;
import com.nic.TPTaxDepartment.dialog.MyDialog;
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
import java.util.Iterator;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements MyDialog.myOnClickListener,Api.ServerResponseListener {
    private static LinearLayout sync_layout;
    public dbData dbData = new dbData(this);
    private BottomAppBar bar;
    private DashboardBinding dashboardBinding;
    ArrayList<TPtaxModel> pendingList = new ArrayList<>();
    final Handler handler = new Handler();
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private PrefManager prefManager;



    ArrayList<CommonModel> wardList;
    ArrayList<CommonModel> streetList;
    ArrayList<CommonModel> finYearList;
    ArrayList<CommonModel> traderLicenseTypeList;

    ProgressHUD progressHUD;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardBinding = DataBindingUtil.setContentView(this, R.layout.dashboard);
        dashboardBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        prefManager=new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sync_layout =(LinearLayout)  findViewById(R.id.sync_layout);


        dashboardBinding.voteprogresscard.setTranslationX(800);
        dashboardBinding.attendanecard.setTranslationX(800);
        dashboardBinding.cameracard.setTranslationX(800);
        dashboardBinding.votecountcard.setTranslationX(800);
        dashboardBinding.viewPollingStationImage.setTranslationX(800);


        dashboardBinding.voteprogresscard.setAlpha(0);
        dashboardBinding.attendanecard.setAlpha(0);
        dashboardBinding.cameracard.setAlpha(0);
        dashboardBinding.votecountcard.setAlpha(0);
        dashboardBinding.viewPollingStationImage.setAlpha(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dashboardBinding.voteprogresscard.animate().translationX(0).alpha(1).setDuration(1400).setStartDelay(400).start();
                dashboardBinding.attendanecard.animate().translationX(0).alpha(1).setDuration(1500).setStartDelay(600).start();
                dashboardBinding.cameracard.animate().translationX(0).alpha(1).setDuration(1600).setStartDelay(800).start();
                dashboardBinding.votecountcard.animate().translationX(0).alpha(1).setDuration(1700).setStartDelay(1000).start();
                dashboardBinding.viewPollingStationImage.animate().translationX(0).alpha(1).setDuration(1800).setStartDelay(1200).start();
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


        dashboardBinding.tradeTv.startAnimation(anim);
        dashboardBinding.fieldTv.startAnimation(anim);
        dashboardBinding.assessTv.startAnimation(anim);
        dashboardBinding.dailcollTv.startAnimation(anim);




        getWardList();
        getStreetList();
        getFinYearList();
        getLicenceTypeList();
        syncvisiblity();
    }

    public void getDistrictList() {
        try {
            new ApiService(this).makeJSONObjectRequest("DistrictList", Api.Method.POST, UrlGenerator.prodOpenUrl(), districtListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getLocalPanchayatList() {
        try {
            new ApiService(this).makeJSONObjectRequest("LocalList", Api.Method.POST, UrlGenerator.prodOpenUrl(), districtListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getWardList() {
        try {
            new ApiService(this).makeJSONObjectRequest("WardList", Api.Method.POST, UrlGenerator.prodOpenUrl(), wardListJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getStreetList() {
        try {
            new ApiService(this).makeJSONObjectRequest("StreetList", Api.Method.POST, UrlGenerator.prodOpenUrl(), streetListJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFinYearList() {
        try {
            new ApiService(this).makeJSONObjectRequest("FinYearList", Api.Method.POST, UrlGenerator.prodOpenUrl(), FinyearListJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public JSONObject districtListJsonParams() throws JSONException {
        //String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.districtListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "District");
        dataSet.put("state_code",prefManager.getStateCode());
        Log.d("districtList", "");
        return dataSet;
    }

    public void getLicenceTypeList() {
        try {
            new ApiService(this).makeJSONObjectRequest("TraderLicenseTypeList", Api.Method.POST, UrlGenerator.getTradersUrl(), licencelistJsonEncryptParams(), "not cache", this);
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




    public JSONObject wardListJsonParams() throws JSONException {
        //String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.districtListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "OS_Ward");
        dataSet.put("lb_code", 200292);
        dataSet.put("district_code",20);
        dataSet.put("state_code", prefManager.getStateCode());
        Log.d("wardList", ""+dataSet);
        return dataSet;
    }

    public Map<String,String> wardParams(){
        Map<String, String> params = new HashMap<>();
        params.put(AppConstant.KEY_SERVICE_ID,"OS_Ward");
        params.put("lb_code",prefManager.getTpCode1() );
        params.put("district_code", prefManager.getDistrictCode());
        params.put("state_code", prefManager.getStateCode());
        Log.d("WardList",""+params);
        return params;
    }


    public JSONObject streetListJsonParams() throws JSONException {
        //String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.districtListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "OS_Street");
        dataSet.put("lb_code", 200292);
        dataSet.put("district_code", 20);
        dataSet.put("state_code", prefManager.getStateCode());
        Log.d("streetList", "");
        return dataSet;
    }

    public Map<String,String> streetParams(){
        Map<String, String> params = new HashMap<>();
        params.put(AppConstant.KEY_SERVICE_ID,"OS_Street");
        params.put("lb_code", prefManager.getTpCode1());
        params.put("district_code", prefManager.getDistrictCode());
        params.put("state_code", prefManager.getStateCode());
        Log.d("Street",""+params);
        return params;
    }

    public JSONObject FinyearListJsonParams() throws JSONException {
        //String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.districtListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "OS_Finyear");
        Log.d("districtList", ""+dataSet);
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
            sync_layout.setVisibility(View.VISIBLE);
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


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
                try {
                    //progressHUD = ProgressHUD.show(Dashboard.this, "Loading", true, false, null);

                    String response=null;
                    String urlType = serverResponse.getApi();
                    JSONObject  jsonResponse  = serverResponse.getJsonResponse();
                    String status;

                    if ("WardList".equals(urlType)) {
                        //dbData.open();
                        if (jsonResponse.getString("STATUS").equalsIgnoreCase("SUCCESS")) {
                            JSONArray jsonString = new JSONArray();
                            jsonString=jsonResponse.getJSONArray("DATA");
                            wardList = new ArrayList<>();
                            CommonModel warddetails = new CommonModel();
                            warddetails.setWard_code("SW");
                            warddetails.setWardID(0);
                            warddetails.setWard_name("Select Ward");
                            wardList.add(warddetails);
                            if(jsonString.length()>0){
                                db.delete(DBHelper.WARD_TABLE_NAME, null,null);
                            }
                            for (int i = 0; i < jsonString.length(); i++) {
                                JSONObject jsonObject = jsonString.getJSONObject(i);
                                CommonModel wardDetails = new CommonModel();
                                String statecode = jsonObject.getString("statecode");
                                String dcode = jsonObject.getString("dcode");
                                String lbcode = jsonObject.getString("lbcode");
                                String ward_id = jsonObject.getString("ward_id");
                                String ward_code = jsonObject.getString("ward_code");
                                String ward_name_en = jsonObject.getString("ward_name_en");
                                String ward_name_ta = jsonObject.getString("ward_name_ta");
                                wardDetails.setState_code(Integer.parseInt(statecode));
                                wardDetails.setD_code(Integer.parseInt(dcode));
                                wardDetails.setLocal_pan_code((lbcode));
                                wardDetails.setWard_code(ward_code);
                                wardDetails.setWardID(Integer.parseInt(ward_id));
                                wardDetails.setWard_name_english(ward_name_en);
                                wardDetails.setWard_name_tamil(ward_name_ta);
                                dbHelper.insertWARD(wardDetails);
                                // wardList.add(wardDetails);

                            }
                        }
                        //getStreetList();
                    }

                    if ("StreetList".equals(urlType)) {
                        //progressHUD.cancel();
                        //String json = jsonResponse;
                        //dbData.open();
                        if (jsonResponse.getString("STATUS").equalsIgnoreCase("SUCCESS")) {
                            JSONArray jsonString = new JSONArray();
                            jsonString=jsonResponse.getJSONArray("DATA");
                            streetList = new ArrayList<>();
                            CommonModel streetdetails = new CommonModel();
                            streetdetails.setState_code(0);
                            streetdetails.setStreet_id(0);
                            streetdetails.setStreet_name_english("Select Ward");
                            streetList.add(streetdetails);
                            if(jsonString.length()>0){
                                db.delete(DBHelper.STREET_TABLE_NAME, null,null);
                            }
                            for (int i = 0; i < jsonString.length(); i++) {
                                JSONObject jsonObject = jsonString.getJSONObject(i);
                                CommonModel streetDetails = new CommonModel();
                                String statecode = jsonObject.getString("statecode");
                                String dcode = jsonObject.getString("dcode");
                                String lbcode = jsonObject.getString("lbcode");
                                String ward_id = jsonObject.getString("wardid");
                                String ward_code = jsonObject.getString("ward_code");
                                String streetid = jsonObject.getString("streetid");
                                String street_code = jsonObject.getString("street_code");
                                String street_name_en = jsonObject.getString("street_name_en");
                                String street_name_ta = jsonObject.getString("street_name_ta");
                                String building_zone = jsonObject.getString("building_zone");

                                streetDetails.setState_code(Integer.parseInt(statecode));
                                streetDetails.setD_code(Integer.parseInt(dcode));
                                streetDetails.setLocal_pan_code((lbcode));
                                streetDetails.setWard_code(ward_code);
                                streetDetails.setWardID(Integer.parseInt(ward_id));
                                streetDetails.setStreet_id(Integer.parseInt(streetid));
                                streetDetails.setStreet_code(street_code);
                                streetDetails.setStreet_name_english(street_name_en);
                                streetDetails.setStreet_name_tamil(street_name_ta);
                                dbHelper.insertStreet(streetDetails);
                                // streetList.add(streetDetails);
                                //progressHUD.cancel();
                            }
                        }
                        //getFinYearList();
                        //progressHUD.cancel();
                    }
                    if ("FinYearList".equals(urlType)) {
                        //progressHUD.cancel();
                        //String json = jsonResponse;
                        //dbData.open();
                        if (jsonResponse.getString("STATUS").equalsIgnoreCase("SUCCESS")) {
                            JSONArray jsonString = new JSONArray();
                            jsonString=jsonResponse.getJSONArray("DATA");
                            finYearList = new ArrayList<>();
                            CommonModel yearDetails = new CommonModel();
                            yearDetails.setFIN_YEAR_ID("0");
                            yearDetails.setFIN_YEAR("Select Financial Year");
                            finYearList.add(yearDetails);
                            if(jsonString.length()>0){
                                db.delete(DBHelper.FIN_YEAR_LIST, null,null);
                            }
                            for (int i = 0; i < jsonString.length(); i++) {
                                JSONObject jsonObject = jsonString.getJSONObject(i);
                                CommonModel yearDetails1 = new CommonModel();
                                String fin_year_id = jsonObject.getString(AppConstant.FIN_YEAR_ID);
                                String fin_year = jsonObject.getString(AppConstant.FIN_YEAR);
                                String from_fin_year = jsonObject.getString(AppConstant.FROM_FIN_YEAR);
                                String to_fin_year = jsonObject.getString(AppConstant.TO_FIN_YEAR);
                                String from_fin_month = jsonObject.getString(AppConstant.FROM_FIN_MONTH);
                                String to_fin_month = jsonObject.getString(AppConstant.TO_FIN_MONTH);

                                yearDetails1.setFIN_YEAR_ID((fin_year_id));
                                yearDetails1.setFIN_YEAR(fin_year);
                                yearDetails1.setFROM_FIN_YEAR((from_fin_year));
                                yearDetails1.setTO_FIN_YEAR(to_fin_year);
                                yearDetails1.setFROM_FIN_MONTH(from_fin_month);
                                yearDetails1.setTO_FIN_MONTH(to_fin_month);
                                dbHelper.insertFinYear(yearDetails1);
                                // streetList.add(streetDetails);
                                //progressHUD.cancel();

                            }

                        }

                        //progressHUD.cancel();
                    }
                    if ("TraderLicenseTypeList".equals(urlType)) {
                        traderLicenseTypeList=new ArrayList<CommonModel>();
                        String user_data = jsonResponse.getString(AppConstant.ENCODE_DATA);
                        String userDataDecrypt = Utils.decrypt2(prefManager.getUserPassKey(), user_data);
                        Log.d("userdatadecry", "" + userDataDecrypt);
                        JSONObject jsonObject = new JSONObject(userDataDecrypt);
                        status = jsonObject.getString(AppConstant.KEY_STATUS);
                        if (status.equalsIgnoreCase("SUCCESS" )) {
                            JSONArray jsonarray = jsonObject.getJSONArray("DATA");
                            //prefManager.setTraderLicenseTypeList(jsonarray.toString());
                            Log.d("TraderLicenseTypeList", "" + jsonObject);

                        } }

                }
                catch (JSONException e){
                    e.printStackTrace();
                    //progressHUD.cancel();
                }
    }

    @Override
    public void OnError(VolleyError volleyError) {
            //progressHUD.cancel();
    }
}
