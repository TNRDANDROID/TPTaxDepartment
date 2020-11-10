package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.databinding.ExistingTradeLicenceBinding;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

public class ExistingTradeLicence extends AppCompatActivity {
    private ExistingTradeLicenceBinding existingTradeLicenceBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existingTradeLicenceBinding = DataBindingUtil.setContentView(this, R.layout.existing_trade_licence);
        existingTradeLicenceBinding.setActivity(this);
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

    public void existTradeLicenceSubmit() {
        Intent intent = new Intent( this, ExistingTradeSubmit.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void validateDetails() {

        if (!existingTradeLicenceBinding.tradersCode.getText().toString().isEmpty() || !existingTradeLicenceBinding.mobileNo.getText().toString().isEmpty() || !existingTradeLicenceBinding.streetId.getText().toString().isEmpty() || !existingTradeLicenceBinding.wardId.getText().toString().isEmpty()) {
            existTradeLicenceSubmit();
        } else {
            Utils.showAlert(this, "Enter Any One!");
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
