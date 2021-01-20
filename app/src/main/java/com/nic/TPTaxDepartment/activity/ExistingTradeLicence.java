package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.nic.TPTaxDepartment.Adapter.GenderAdapter;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.databinding.ExistingTradeLicenceBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import java.util.ArrayList;

import static com.nic.TPTaxDepartment.activity.Dashboard.db;

public class ExistingTradeLicence extends AppCompatActivity {
    private ExistingTradeLicenceBinding existingTradeLicenceBinding;

    ArrayList<CommonModel> wardList;
    ArrayList<CommonModel> streetList;
    int wardid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existingTradeLicenceBinding = DataBindingUtil.setContentView(this, R.layout.existing_trade_licence);
        existingTradeLicenceBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());

        displayWard();
        existingTradeLicenceBinding.wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    wardid=wardList.get(i).getWardID();
                    displayStreet();
                    existingTradeLicenceBinding.streetSpinner.setVisibility(View.VISIBLE);
                }
                else {
                    existingTradeLicenceBinding.streetSpinner.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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




    //display ward list
    public void displayWard() {
        wardList = new ArrayList<>();
        CommonModel commonModel=new CommonModel();
        commonModel.setWardID(0);
        commonModel.setWard_name_tamil("Select Ward");
        commonModel.setWard_code("SW");
        wardList.add(commonModel);
        wardList.addAll(Dashboard.dbHelper.getAllWard());
        GenderAdapter adapter = new GenderAdapter(ExistingTradeLicence.this,wardList,"Ward");
        existingTradeLicenceBinding.wardSpinner.setAdapter(adapter);
    }

    //display street list
    public void displayStreet() {
        streetList=new ArrayList<>();
        String select_query= "SELECT *FROM " + DBHelper.STREET_TABLE_NAME + " WHERE wardid="+wardid;
        Cursor cursor = db.rawQuery(select_query, null);
        if(cursor.getCount()>0){

            if(cursor.moveToFirst()){
                do{
                  CommonModel commonModel=new CommonModel();
                  commonModel.setState_code(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.STATE_CODE)));
                  commonModel.setD_code(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                  commonModel.setLocal_pan_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LP_CODE)));
                  commonModel.setWard_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_CODE)));
                  commonModel.setWardID(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.WARD_ID)));
                  commonModel.setStreet_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_CODE)));
                  commonModel.setStreet_id(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.STREET_ID)));
                    commonModel.setStreet_name_english(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_NAME_EN)));
                    commonModel.setStreet_name_tamil(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_NAME_TA)));
                    streetList.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        GenderAdapter adapter = new GenderAdapter(ExistingTradeLicence.this,streetList,"Street");
        existingTradeLicenceBinding.streetSpinner.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
