package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.databinding.TradeLicenceScreenBinding;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

public class TradeLicenceScreen extends AppCompatActivity {
    TradeLicenceScreenBinding tradeLicenceScreenBinding;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tradeLicenceScreenBinding = DataBindingUtil.setContentView(this, R.layout.trade_licence_screen);
        tradeLicenceScreenBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());

        tradeLicenceScreenBinding.newcard.setTranslationX(800);
        tradeLicenceScreenBinding.existCard.setTranslationX(800);


        tradeLicenceScreenBinding.newcard.setAlpha(0);
        tradeLicenceScreenBinding.existCard.setAlpha(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tradeLicenceScreenBinding.newcard.animate().translationX(0).alpha(1).setDuration(1400).setStartDelay(400).start();
                tradeLicenceScreenBinding.existCard.animate().translationX(0).alpha(1).setDuration(1500).setStartDelay(600).start();

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


        tradeLicenceScreenBinding.newTv.startAnimation(anim);

        tradeLicenceScreenBinding.existTv.startAnimation(anim);


    }

    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void newTradeSilenceScreen(){
        Intent intent = new Intent( this, NewTradeLicenceScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void existTradeSilenceScreen(){
        Intent intent = new Intent( this, ExistingTradeLicence.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
