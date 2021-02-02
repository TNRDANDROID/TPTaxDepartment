package com.nic.TPTaxDepartment.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.Support.ProgressHUD;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.databinding.LoginScreenBinding;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by AchanthiSundar on 28-12-2018.
 */

public class LoginScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    private String randString;

    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    JSONObject jsonObject;

    private PrefManager prefManager;
    private ProgressHUD progressHUD;
    private int setPType;

    public LoginScreenBinding loginScreenBinding;
    public com.nic.TPTaxDepartment.dataBase.dbData dbData = new dbData(this);
    Animation stb2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.login_screen);
        loginScreenBinding.setActivity(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loginScreenBinding.progressBar.setVisibility(View.VISIBLE);
        loginScreenBinding.scrollView.setVisibility(View.GONE);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        loginScreenBinding.scrollView.setVerticalScrollBarEnabled(false);
        loginScreenBinding.scrollView.isSmoothScrollingEnabled();
        intializeUI();




    }

    public void intializeUI() {
        prefManager = new PrefManager(this);


        if((prefManager.getUserName()!=null) &&(!prefManager.getUserName().equals(""))
                &&(prefManager.getUserPassword()!=null)&&(!prefManager.getUserPassword().equals(""))){
            loginScreenBinding.username.setText(prefManager.getUserName());
            loginScreenBinding.password.setText(prefManager.getUserPassword());
            checkLoginScreen();
        }
        else {
            loginScreenBinding.progressBar.setVisibility(View.GONE);
            loginScreenBinding.scrollView.setVisibility(View.VISIBLE);
        }
        stb2 = AnimationUtils.loadAnimation(this, R.anim.stb2);
        loginScreenBinding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        loginScreenBinding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    checkLoginScreen();
                }
                return false;
            }
        });

        loginScreenBinding.password.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.ttf"));
        loginScreenBinding.rd.setTranslationY(400);
        loginScreenBinding.and.setTranslationY(400);
        loginScreenBinding.dpt.setTranslationY(400);
        loginScreenBinding.tvVersionNumber.setTranslationY(400);
        loginScreenBinding.nicName.setTranslationY(400);
        loginScreenBinding.btnBuy.setTranslationY(400);

        loginScreenBinding.ivItemOne.setTranslationX(800);
        loginScreenBinding.ivItemTwo.setTranslationX(800);


        loginScreenBinding.btnBuy.setAlpha(0);

        loginScreenBinding.rd.setAlpha(0);
        loginScreenBinding.and.setAlpha(0);
        loginScreenBinding.dpt.setAlpha(0);
        loginScreenBinding.tvVersionNumber.setAlpha(0);
        loginScreenBinding.nicName.setAlpha(0);

        loginScreenBinding.ivItemOne.setAlpha(0);
        loginScreenBinding.ivItemTwo.setAlpha(0);


        loginScreenBinding.btnBuy.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();

        loginScreenBinding.rd.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginScreenBinding.and.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginScreenBinding.dpt.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
        loginScreenBinding.tvVersionNumber.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        loginScreenBinding.nicName.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(100).start();

        loginScreenBinding.ivItemOne.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        loginScreenBinding.ivItemTwo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();

        loginScreenBinding.ivIlls.startAnimation(stb2);
        randString = Utils.randomChar();


        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            loginScreenBinding.tvVersionNumber.setText("Version" + " " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        /*loginScreenBinding.username.setText("tpbc1@gmail.com");
        loginScreenBinding.password.setText("test123#$");*/
       /* loginScreenBinding.username.setText("tpbc1.200297@gmail.com");
        loginScreenBinding.password.setText("tpbc1.200297");*/
       /* loginScreenBinding.username.setText("tpbc1.200279@gmail.com");
        loginScreenBinding.password.setText("tpbc1.200279");*/
        loginScreenBinding.username.setText("tpeo200279@gmail.com");
        loginScreenBinding.password.setText("tpeo200279");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    public boolean validate() {
        boolean valid = true;
        String username = loginScreenBinding.username.getText().toString().trim();
        prefManager.setUserName(username);
        String password = loginScreenBinding.password.getText().toString().trim();


        if (username.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the username");
        } else if (password.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the password");
        }
        return valid;
    }

    public void checkLoginScreen() {
        final String username = loginScreenBinding.username.getText().toString().trim();
        final String password = loginScreenBinding.password.getText().toString().trim();
        prefManager.setUserPassword(password);

        if (Utils.isOnline()) {
            if (!validate())
                return;
            else if (prefManager.getUserName().length() > 0 && password.length() > 0) {
                new ApiService(this).makeRequest("LoginScreen", Api.Method.POST, UrlGenerator.getLoginUrl(), loginParams(), "not cache", this);
            } else {
                Utils.showAlert(this, "Please enter your username and password!");
            }
        } else {
            //Utils.showAlert(this, getResources().getString(R.string.no_internet));
            AlertDialog.Builder ab = new AlertDialog.Builder(
                    LoginScreen.this);
            ab.setMessage("Internet Connection is not avaliable..Please Turn ON Network Connection OR Continue With Off-line Mode..");
            ab.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(I);
                        }
                    });
            ab.setNegativeButton("Continue With Off-Line",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            offline_mode(username, password);
                        }
                    });
            ab.show();
        }
    }


    public Map<String, String> loginParams() {
        Map<String, String> params = new HashMap<>();
        params.put(AppConstant.KEY_SERVICE_ID, "login");


        String random = Utils.randomChar();

        params.put(AppConstant.USER_LOGIN_KEY, random);
        Log.d("randchar", "" + random);

        params.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        Log.d("user", "" + loginScreenBinding.username.getText().toString().trim());

       /* String encryptUserPass = Utils.md5(loginScreenBinding.password.getText().toString().trim());
        prefManager.setEncryptPass(encryptUserPass);
        Log.d("md5", "" + encryptUserPass);*/



        String encryptUserPass = Utils.getSHA512(loginScreenBinding.password.getText().toString().trim());
        String encryptUserPassword = Utils.md5(encryptUserPass);
        prefManager.setEncryptPass(encryptUserPassword);
        Log.d("SHA512", "" + encryptUserPass);


        String userPass = encryptUserPass.concat(random);
        Log.d("userpass", "" + userPass);
        String sha256 = Utils.getSHA(userPass);
        Log.d("sha", "" + sha256);

        params.put(AppConstant.KEY_USER_PASSWORD, sha256);
        params.put(AppConstant.KEY_APP_CODE,"TP");


        Log.d("user", "" + loginScreenBinding.username.getText().toString().trim());

        Log.d("params", "" + params);
        return params;
    }

    //The method for opening the registration page and another processes or checks for registering


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status;
            String response;

            if ("LoginScreen".equals(urlType)) {
                status =  Utils.NotNullString(loginResponse.getString(AppConstant.KEY_STATUS));
                response =  Utils.NotNullString(loginResponse.getString(AppConstant.KEY_RESPONSE));
                if (status.equalsIgnoreCase("OK")) {
                    if (response.equals("LOGIN_SUCCESS")) {
                        String key =  Utils.NotNullString(loginResponse.getString(AppConstant.KEY_USER));
                        String user_data =  Utils.NotNullString(loginResponse.getString(AppConstant.USER_DATA));
                        String decryptedKey = Utils.decrypt(prefManager.getEncryptPass(), key);
                        String userDataDecrypt = Utils.decrypt(decryptedKey, user_data);
                        Log.d("userdatadecry", "" + userDataDecrypt);
                        jsonObject = new JSONObject(userDataDecrypt);
                        prefManager.setStateCode( Utils.NotNullString(jsonObject.getString(AppConstant.STATE_CODE)));
                        prefManager.setLbType( Utils.NotNullString(jsonObject.getString(AppConstant.LB_TYPE)));
                       /* prefManager.setDistrictCode( Utils.NotNullString(jsonObject.getString(AppConstant.DISTRICT_CODE)));
                        prefManager.setTpCode( Utils.NotNullString(jsonObject.getString(AppConstant.TP_CODE)));*/
                        prefManager.setDistrictCode("20");
                        prefManager.setTpCode("200292");
                        prefManager.setLbodyNameEn( Utils.NotNullString(jsonObject.getString(AppConstant.LBODY_NAME_EN)));
                        prefManager.setRoleCode( Utils.NotNullString(jsonObject.getString(AppConstant.ROLE_CODE)));
                        prefManager.setRoleName( Utils.NotNullString(jsonObject.getString(AppConstant.ROLE_NAME)));
                        prefManager.setUserFname( Utils.NotNullString(jsonObject.getString(AppConstant.USER_FNAME)));
                        prefManager.setUserLname( Utils.NotNullString(jsonObject.getString(AppConstant.USER_LNAME)));

                        Log.d("userdata", "" + prefManager.getStateCode() + prefManager.getDistrictCode() +
                                prefManager.getLbType() + prefManager.getTpCode() + prefManager.getLbodyNameEn() +
                                prefManager.getRoleCode()+prefManager.getRoleName()+prefManager.getUserFname()+prefManager.getUserLname());
                        prefManager.setUserPassKey(decryptedKey);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showHomeScreen();
                            }
                        }, 1000);

                    } else {
                        if (response.equals("LOGIN_FAILED")) {
                            loginScreenBinding.progressBar.setVisibility(View.GONE);
                            loginScreenBinding.scrollView.setVisibility(View.VISIBLE);
                            Utils.showAlert(this, "Invalid UserName Or Password");
                        }
                    }
                }
            }

        } catch (JSONException e) {
            loginScreenBinding.progressBar.setVisibility(View.GONE);
            loginScreenBinding.scrollView.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
    }




    @Override
    public void OnError(VolleyError volleyError) {
        loginScreenBinding.progressBar.setVisibility(View.GONE);
        loginScreenBinding.scrollView.setVisibility(View.VISIBLE);
        Utils.showAlert(this, "Login Again");
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        showHomeScreen();
//    }

    public void showHomeScreen() {
        Intent intent = new Intent(LoginScreen.this, Dashboard.class);
        intent.putExtra("AssessmentStatus", "Login");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void offline_mode(String name, String pass) {
        String userName = prefManager.getUserName();
        String password = prefManager.getUserPassword();
        if (name.equals(userName) && pass.equals(password)) {
            showHomeScreen();
        } else {
            Utils.showAlert(this, "No data available for offline. Please Turn On Your Network");
        }
    }
}
