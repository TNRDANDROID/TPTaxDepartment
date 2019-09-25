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
import com.jakewharton.rxbinding.widget.RxTextView;
import com.nic.VPTax.Api.Api;
import com.nic.VPTax.Api.ApiService;
import com.nic.VPTax.Api.ServerResponse;
import com.nic.VPTax.R;
import com.nic.VPTax.constant.AppConstant;
import com.nic.VPTax.dataBase.DBHelper;
import com.nic.VPTax.dataBase.dbData;
import com.nic.VPTax.databinding.HomeScreenBinding;
import com.nic.VPTax.dialog.MyDialog;
import com.nic.VPTax.helper.APIHelper;
import com.nic.VPTax.model.TranslatedText;
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
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.functions.Action1;

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
//        Bundle bundle = this.getIntent().getExtras();
//        if (bundle != null) {
//            isHome = bundle.getString("Home");
//        }
//        if (Utils.isOnline() && !isHome.equalsIgnoreCase("Home")) {
//          getTankPondList();
//        }
        homeScreenBinding.translateTitle.setTranslationX(800);
        homeScreenBinding.beforeTranslateLayout.setTranslationX(800);
        homeScreenBinding.afterTranslate.setTranslationX(800);
        homeScreenBinding.afterTranslateLayout.setTranslationX(800);

        homeScreenBinding.translateTitle.setAlpha(0);
        homeScreenBinding.beforeTranslateLayout.setAlpha(0);
        homeScreenBinding.afterTranslate.setAlpha(0);
        homeScreenBinding.afterTranslateLayout.setAlpha(0);

        homeScreenBinding.translateTitle.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
        homeScreenBinding.beforeTranslateLayout.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(600).start();
        homeScreenBinding.afterTranslate.animate().translationX(0).alpha(1).setDuration(1200).setStartDelay(800).start();
        homeScreenBinding.afterTranslateLayout.animate().translationX(0).alpha(1).setDuration(1400).setStartDelay(1000).start();
        textChangedListener();
    }

    public void textChangedListener() {
        // Translate the text after 500 milliseconds when user ends to typing
        RxTextView.textChanges(homeScreenBinding.textToTranslate).
                filter(charSequence -> charSequence.length() > 0).
                debounce(100, TimeUnit.MILLISECONDS).
                subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        translate(charSequence.toString().trim());
                    }
                });
        RxTextView.textChanges(homeScreenBinding.textToTranslate).
                filter(charSequence -> charSequence.length() == 0).
                subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                emptyTranslatedText();
                            }
                        });
                    }
                });
    }

    private void translate(String text){
        Log.d("text",text);

        Retrofit query = new Retrofit.Builder().baseUrl("https://translate.yandex.net/").
                addConverterFactory(GsonConverterFactory.create()).build();
        APIHelper apiHelper = query.create(APIHelper.class);
        Call<TranslatedText> call = apiHelper.getTranslation(getResources().getString(R.string.APIKey), text,
                "en" + "-" + "ta");

        call.enqueue(new Callback<TranslatedText>() {
            @Override
            public void onResponse(Call<TranslatedText> call, Response<TranslatedText> response) {
                if(response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            homeScreenBinding.translatedText.setText(response.body().getText().get(0));
                            Log.d("response",response.body().getText().get(0));
                            emptyTranslatedText();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TranslatedText> call, Throwable t) {}
        });
    }

    public void emptyTranslatedText(){
        if(homeScreenBinding.textToTranslate.getText().toString().equals("")){
            homeScreenBinding.close.setVisibility(View.GONE);
        }else{
            homeScreenBinding.close.setVisibility(View.VISIBLE);
        }
        String text = String.valueOf(homeScreenBinding.textToTranslate.getText());

        if(text.equals("")){
            homeScreenBinding.translatedText.setText("");
        }

    }

    public void empty(){
        homeScreenBinding.textToTranslate.setText("");
        homeScreenBinding.translatedText.setText("");
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
        RxTextView.textChanges(homeScreenBinding.textToTranslate).
                filter(charSequence -> charSequence.length() == 0).
                subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                emptyTranslatedText();
                            }
                        });
                    }
                });
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
