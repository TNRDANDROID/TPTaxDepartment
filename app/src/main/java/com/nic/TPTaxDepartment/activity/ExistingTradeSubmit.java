package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.databinding.ExistTradeSubmitBinding;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

public class ExistingTradeSubmit extends AppCompatActivity {
    private ExistTradeSubmitBinding existTradeSubmitBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existTradeSubmitBinding = DataBindingUtil.setContentView(this, R.layout.exist_trade_submit);
        existTradeSubmitBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
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
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void openCameraScreen() {
        Intent intent = new Intent(this, CameraScreen.class);
        intent.putExtra(AppConstant.TRADE_CODE,"");
        intent.putExtra(AppConstant.KEY_SCREEN_STATUS,"exist");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void viewImageScreen() {
        Intent intent = new Intent(this, FullImageActivity.class);
        intent.putExtra(AppConstant.TRADE_CODE,"");
        intent.putExtra(AppConstant.KEY_SCREEN_STATUS,"exist");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
