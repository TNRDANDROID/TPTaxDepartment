package com.nic.VPTax.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment;
import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.nic.VPTax.R;
import com.nic.VPTax.databinding.ActivityLoginBinding;
import com.nic.VPTax.databinding.CatBottomappbarFragmentBinding;

public class test extends AppCompatActivity {
    private BottomAppBar bar;
    private ActivityLoginBinding  activityLoginBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        activityLoginBinding.setActivity(this);

    }


}
