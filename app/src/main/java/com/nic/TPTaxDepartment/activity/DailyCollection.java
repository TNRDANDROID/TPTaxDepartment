package com.nic.TPTaxDepartment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.databinding.DailyCollectionBinding;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;


public class DailyCollection extends AppCompatActivity {

    private DailyCollectionBinding dailyCollectionBinding;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyCollectionBinding = DataBindingUtil.setContentView(this, R.layout.daily_collection);
        dailyCollectionBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());

        dailyCollectionBinding.voteprogresscard.setTranslationX(800);
        dailyCollectionBinding.attendanecard.setTranslationX(800);
        dailyCollectionBinding.cameracard.setTranslationX(800);
        dailyCollectionBinding.votecountcard.setTranslationX(800);
        dailyCollectionBinding.viewPollingStationImage.setTranslationX(800);


        dailyCollectionBinding.voteprogresscard.setAlpha(0);
        dailyCollectionBinding.attendanecard.setAlpha(0);
        dailyCollectionBinding.cameracard.setAlpha(0);
        dailyCollectionBinding.votecountcard.setAlpha(0);
        dailyCollectionBinding.viewPollingStationImage.setAlpha(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dailyCollectionBinding.voteprogresscard.animate().translationX(0).alpha(1).setDuration(1400).setStartDelay(400).start();
                dailyCollectionBinding.attendanecard.animate().translationX(0).alpha(1).setDuration(1500).setStartDelay(600).start();
                dailyCollectionBinding.cameracard.animate().translationX(0).alpha(1).setDuration(1600).setStartDelay(800).start();
                dailyCollectionBinding.votecountcard.animate().translationX(0).alpha(1).setDuration(1700).setStartDelay(1000).start();
                dailyCollectionBinding.viewPollingStationImage.animate().translationX(0).alpha(1).setDuration(1800).setStartDelay(1200).start();
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


        dailyCollectionBinding.tradeTv.startAnimation(anim);
        dailyCollectionBinding.fieldTv.startAnimation(anim);
        dailyCollectionBinding.assessTv.startAnimation(anim);
        dailyCollectionBinding.dailcollTv.startAnimation(anim);
        dailyCollectionBinding.tradeCount.startAnimation(anim);


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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}