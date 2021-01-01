package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.MediaRouteButton;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.databinding.DashboardBinding;
import com.nic.TPTaxDepartment.dialog.MyDialog;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import java.util.ArrayList;

public class Dashboard extends AppCompatActivity implements MyDialog.myOnClickListener {
    private static LinearLayout sync_layout;
    public com.nic.TPTaxDepartment.dataBase.dbData dbData = new dbData(this);
    private BottomAppBar bar;
    private DashboardBinding dashboardBinding;
    ArrayList<TPtaxModel> pendingList = new ArrayList<>();
    final Handler handler = new Handler();
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardBinding = DataBindingUtil.setContentView(this, R.layout.dashboard);
        dashboardBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
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

        syncvisiblity();
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


}
