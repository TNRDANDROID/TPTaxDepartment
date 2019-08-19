package com.nic.VPTax.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.nic.VPTax.Api.Api;
import com.nic.VPTax.Api.ApiService;
import com.nic.VPTax.Api.ServerResponse;
import com.nic.VPTax.R;
import com.nic.VPTax.constant.AppConstant;
import com.nic.VPTax.dataBase.DBHelper;
import com.nic.VPTax.dataBase.dbData;
import com.nic.VPTax.databinding.HomeScreenBinding;
import com.nic.VPTax.dialog.MyDialog;
import com.nic.VPTax.model.VPtaxModel;
import com.nic.VPTax.session.PrefManager;
import com.nic.VPTax.utils.UrlGenerator;
import com.nic.VPTax.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener, MyDialog.myOnClickListener {
    private HomeScreenBinding homeScreenBinding;
    private PrefManager prefManager;
    public dbData dbData = new dbData(this);
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private String isHome;
    Handler myHandler = new Handler();
    String lastInsertedID;
    JSONObject dataset = new JSONObject();
    JSONObject datasetTrack = new JSONObject();


    String pref_Village;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        homeScreenBinding = DataBindingUtil.setContentView(this, R.layout.home_screen);
        homeScreenBinding.setActivity(this);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            isHome = bundle.getString("Home");
        }
        if (Utils.isOnline() && !isHome.equalsIgnoreCase("Home")) {
         //   getTankPondList();
        }
        syncButtonVisibility();
    }

    public void toUpload() {
        if(Utils.isOnline()) {
        }
        else {
            Utils.showAlert(this,"Please Turn on Your Mobile Data to Upload");
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public void logout() {
//        dbData.open();
//        ArrayList<MITank> TankImageCount = dbData.getSavedData(prefManager.getDistrictCode(),prefManager.getBlockCode());
//        ArrayList<MITank> trackCount = dbData.getSavedTrack();
//
//
//        if (!Utils.isOnline()) {
//            Utils.showAlert(this, "Logging out while offline may leads to loss of data!");
//        } else {
//            if (!(TankImageCount.size() > 0 || trackCount.size() > 0)) {
//                closeApplication();
//            } else {
//                Utils.showAlert(this, "Sync all the data before logout!");
//            }
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncButtonVisibility();
    }


//    public void getTankPondList() {
//        try {
//            new ApiService(this).makeJSONObjectRequest("TankPondList", Api.Method.POST, UrlGenerator.getTankPondListUrl(), tankPondListJsonParams(), "not cache", this);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    public void syncData() {
        try {
            new ApiService(this).makeJSONObjectRequest("saveTankData", Api.Method.POST, UrlGenerator.getTankPondListUrl(), saveTankListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject saveTankListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("savebridgesList", "" + authKey);
        return dataSet;
    }

    public void syncDataTrack() {
        try {
            new ApiService(this).makeJSONObjectRequest("saveTrackDataList", Api.Method.POST, UrlGenerator.getTankPondListUrl(), saveTrackDataListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject saveTrackDataListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), datasetTrack.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("saveTrackDataList", "" + authKey);
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("TankPondList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD") && jsonObject.getString("MESSAGE").equalsIgnoreCase("NO_RECORD")) {
//                    Utils.showAlert(this, "No Record Found !");
                }
                Log.d("TankPondList", "" + responseDecryptedBlockKey);

            }

            if ("TankPondStructure".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD") && jsonObject.getString("MESSAGE").equalsIgnoreCase("NO_RECORD")) {
//                    Utils.showAlert(this, "No Record Found !");
                }
                Log.d("TankPondStructure", "" + responseDecryptedBlockKey);
            }

            if ("TankCondition".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {


                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD") && jsonObject.getString("MESSAGE").equalsIgnoreCase("NO_RECORD")) {
//                    Utils.showAlert(this, "No Record Found !");
                }
                Log.d("TankCondition", "" + responseDecryptedBlockKey);
            }
            if ("saveTankData".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    dbData.open();
                    dbData.deleteMITankData();
                    dbData.deleteMITankImages();
                    dbData.deleteStructures();
                    dataset = new JSONObject();
                    //getTankPondList();
                    Utils.showAlert(this,"Saved");
                    syncButtonVisibility();
                }
                Log.d("saved_TankData", "" + responseDecryptedBlockKey);
            }
            if ("saveTrackDataList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    dbData.open();
                    dbData.update_Track();
                    datasetTrack = new JSONObject();
                    Utils.showAlert(this, "Tracked Data Saved");
                    syncButtonVisibility();
                }
                Log.d("saved_TrackDataList", "" + responseDecryptedBlockKey);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnError(VolleyError volleyError) {
        volleyError.printStackTrace();
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


    public void syncButtonVisibility() {
//        dbData.open();
//        ArrayList<MITank> TankImageCount = dbData.getSavedData(prefManager.getDistrictCode(),prefManager.getBlockCode());
//        ArrayList<MITank> trackCount = dbData.getSavedTrack();
//
//        if (TankImageCount.size() > 0 || trackCount.size() > 0) {
//            homeScreenBinding.synData.setVisibility(View.VISIBLE);
//        }else {
//            homeScreenBinding.synData.setVisibility(View.GONE);
//        }
    }


    public void checkFields() {

    }

    public void tanksPondsTitleScreen() {

    }
}
