package com.nic.VPTax.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.nic.VPTax.Api.Api;
import com.nic.VPTax.Api.ServerResponse;
import com.nic.VPTax.R;
import com.nic.VPTax.Support.ProgressHUD;
import com.nic.VPTax.constant.AppConstant;
import com.nic.VPTax.dataBase.DBHelper;
import com.nic.VPTax.dataBase.dbData;

import com.nic.VPTax.dialog.MyDialog;
import com.nic.VPTax.model.VPtaxModel;
import com.nic.VPTax.session.PrefManager;
import com.nic.VPTax.utils.Utils;
import com.nic.VPTax.windowpreferences.WindowPreferencesManager;


import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity  {


    private List<String> RuralUrbanList = new ArrayList<>();

    private List<VPtaxModel> District = new ArrayList<>();
    private List<VPtaxModel> Block = new ArrayList<>();
    public com.nic.VPTax.dataBase.dbData dbData = new dbData(this);
    private SQLiteDatabase db;
    public static DBHelper dbHelper;
    Animation smalltobig, stb2;
    final Handler handler = new Handler();
    private PrefManager prefManager;
    String pref_Block, pref_district, pref_Village;
    boolean isPanchayatUnion, isMunicipality, isTownPanchayat, isCorporation;
    private ProgressHUD progressHUD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        dashboardBinding = DataBindingUtil.setContentView(this, R.layout.dashboard);
//        dashboardBinding.setActivity(this);
//        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
//        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
//
//        try {
//            dbHelper = new DBHelper(this);
//            db = dbHelper.getWritableDatabase();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
//        stb2 = AnimationUtils.loadAnimation(this, R.anim.stb2);
//        dashboardBinding.homeImg.setVisibility(View.GONE);
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dashboardBinding.homeImg.setVisibility(View.VISIBLE);
//                dashboardBinding.homeImg.startAnimation(smalltobig);
//            }
//        }, 500);
//
//        prefManager = new PrefManager(this);
//
//        dashboardBinding.districtUserLayout.setTranslationX(800);
//        dashboardBinding.blockUserLayout.setTranslationX(800);
//        dashboardBinding.voteprogresscard.setTranslationX(800);
//        dashboardBinding.attendanecard.setTranslationX(800);
//        dashboardBinding.cameracard.setTranslationX(800);
//        dashboardBinding.votecountcard.setTranslationX(800);
//        dashboardBinding.viewPollingStationImage.setTranslationX(800);
//
//
//        dashboardBinding.districtUserLayout.setAlpha(0);
//        dashboardBinding.blockUserLayout.setAlpha(0);
//        dashboardBinding.voteprogresscard.setAlpha(0);
//        dashboardBinding.attendanecard.setAlpha(0);
//        dashboardBinding.cameracard.setAlpha(0);
//        dashboardBinding.votecountcard.setAlpha(0);
//        dashboardBinding.viewPollingStationImage.setAlpha(0);
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dashboardBinding.districtUserLayout.animate().translationX(0).alpha(1).setDuration(1200).setStartDelay(400).start();
//                dashboardBinding.blockUserLayout.animate().translationX(0).alpha(1).setDuration(1300).setStartDelay(600).start();
//                dashboardBinding.voteprogresscard.animate().translationX(0).alpha(1).setDuration(1400).setStartDelay(800).start();
//                dashboardBinding.attendanecard.animate().translationX(0).alpha(1).setDuration(1500).setStartDelay(1000).start();
//                dashboardBinding.cameracard.animate().translationX(0).alpha(1).setDuration(1600).setStartDelay(1200).start();
//                dashboardBinding.votecountcard.animate().translationX(0).alpha(1).setDuration(1700).setStartDelay(1400).start();
//                dashboardBinding.viewPollingStationImage.animate().translationX(0).alpha(1).setDuration(1800).setStartDelay(1600).start();
//            }
//        }, 1000);
//
//        Animation anim = new ScaleAnimation(
//                0.95f, 1f, // Start and end values for the X axis scaling
//                0.95f, 1f, // Start and end values for the Y axis scaling
//                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
//                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
//        anim.setFillAfter(true); // Needed to keep the result of the animation
//        anim.setDuration(1000);
//        anim.setRepeatMode(Animation.INFINITE);
//        anim.setRepeatCount(Animation.INFINITE);
//
//
//        dashboardBinding.imageVotingProgress.startAnimation(anim);
//        dashboardBinding.imageAttendance.startAnimation(anim);
//        dashboardBinding.imageCamera.startAnimation(anim);
//        dashboardBinding.imageVotingCount.startAnimation(anim);
//        dashboardBinding.imageVotingCount.startAnimation(anim);
//        dashboardBinding.pollingStationImage.startAnimation(anim);
//        syncButtonVisibility();
    }

    //    public void checkAnimation() {
//
//
//        ((Animatable) dashboardBinding.imageView.getDrawable()).start();
//
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }






//    public void votingProgress(){
//        Intent intent = new Intent(this,VotingProgress.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.fleft, R.anim.fhelper);
//    }
//
//    public void attendance(){
//        Intent intent = new Intent(this,AttendanceScreen.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.fleft, R.anim.fhelper);
//    }
//
//    public void cameraScreen(){
//        Intent intent = new Intent(this,CameraScreen.class);
//        intent.putExtra(AppConstant.KEY_PURPOSE, "Insert");
//        startActivity(intent);
//        overridePendingTransition(R.anim.fleft, R.anim.fhelper);
//    }
//
//    public void viewPollingStationImage(){
//        Intent intent = new Intent(this,FullImageActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.fleft, R.anim.fhelper);
//    }





//    public void syncButtonVisibility() {
//        dbData.open();
//        ArrayList<VPtaxModel> workImageCount = dbData.getAllpollingStationImages();
//
//        if (workImageCount.size() > 0) {
//            dashboardBinding.viewPollingStationImage.setVisibility(View.VISIBLE);
//        }else {
//            dashboardBinding.viewPollingStationImage.setVisibility(View.GONE);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        syncButtonVisibility();
    }
}
