package com.nic.TPTaxDepartment.activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.library.baseAdapters.BuildConfig;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.databinding.SplashScreenBinding;
import com.nic.TPTaxDepartment.helper.AppVersionHelper;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;


public class SplashScreen extends AppCompatActivity implements
        AppVersionHelper.myAppVersionInterface {
    private TextView textView;
    private Button button;
    private static int SPLASH_TIME_OUT = 2000;
    private PrefManager prefManager;
    public SplashScreenBinding splashScreenBinding;
    Animation smalltobig, fleft, fhelper;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashScreenBinding = DataBindingUtil.setContentView(this, R.layout.splash_screen);
        splashScreenBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        this.getWindow().setStatusBarColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

        prefManager = new PrefManager(this);
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase("production")) {
            if (Utils.isOnline()) {
                checkAppVersion();
            } else {
//                showSignInScreen();

            }
        } else {checkAppVersion();
//            showSignInScreen();
        }

        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        fleft = AnimationUtils.loadAnimation(this, R.anim.fleft);
        fhelper = AnimationUtils.loadAnimation(this, R.anim.fhelper);
       /* splashScreenBinding.ivSplash.startAnimation(smalltobig);

        splashScreenBinding.ivLogo.setTranslationX(400);
        splashScreenBinding.ivSubtitle.setTranslationX(400);
        splashScreenBinding.ivBtn.setTranslationX(400);

        splashScreenBinding.ivLogo.setAlpha(0);
        splashScreenBinding.ivSubtitle.setAlpha(0);
        splashScreenBinding.ivBtn.setAlpha(0);

        splashScreenBinding.ivLogo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        splashScreenBinding.ivSubtitle.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        splashScreenBinding.ivBtn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();*/

       /* if((prefManager.getUserName()!=null) &&(!prefManager.getUserName().equals(""))
                &&(prefManager.getUserPassword()!=null)&&(!prefManager.getUserPassword().equals(""))){
            Intent i = new Intent(SplashScreen.this, LoginScreen.class);
            startActivity(i);
            finish();
            overridePendingTransition(R.anim.fleft, R.anim.fhelper);
        }*/


       /*
            splashScreenBinding.ivBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //screenLockPermission();
                showSignInScreen();
            }
        });
*/
        showSignInScreen();

    }


    private void showSignInScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LoginScreen.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.fleft, R.anim.fhelper);
            }
        }, 2000);

    }

    private void checkAppVersion() {
        new AppVersionHelper(this, SplashScreen.this).callAppVersionCheckApi();
    }

    @Override
    public void onAppVersionCallback(String value) {
        if (value.length() > 0 && "Update".equalsIgnoreCase(value)) {
            startActivity(new Intent(this, AppUpdateDialog.class));
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            //screenLockPermission();
            showSignInScreen();
        }

    }


   /* public void screenLockPermission(){
        KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        if(km.isKeyguardSecure()) {

            Intent i = km.createConfirmDeviceCredentialIntent("Authentication required", "password");
            startActivityForResult(i, 2);
        }
        else
            //Toast.makeText(this, "No any security setup done by user(pattern or password or pin or fingerprint", Toast.LENGTH_SHORT).show();
        showSignInScreen();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==2)
        {
            //Toast.makeText(this, "Success: Verified user's identity", Toast.LENGTH_SHORT).show();
            showSignInScreen();
        }
        else
        {
            //Toast.makeText(this, "Failure: Unable to verify user's identity", Toast.LENGTH_SHORT).show();
        }
    }
*/
}
