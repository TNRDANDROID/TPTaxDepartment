package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.TPTaxDepartment.Adapter.TraderListAdapter;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.databinding.ExistingTraderListBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ExistingTradeList extends AppCompatActivity {
    private ExistingTraderListBinding existingTradeList;
    private PrefManager prefManager;
    ArrayList<TPtaxModel> tradersList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existingTradeList = DataBindingUtil.setContentView(this, R.layout.existing_trader_list);
        existingTradeList.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        prefManager = new PrefManager(this);
        existingTradeList.tradeRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        existingTradeList.tradeRecycler.setLayoutManager(layoutManager);

        try {
            LoadTradersList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void LoadTradersList() throws JSONException {
        tradersList = new ArrayList<TPtaxModel>();
        for (int i = 0; i < 5; i++) {
            if(i==2){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName("raj");
                Detail.setTraderCode("12");
                Detail.setTraders_typ("Industrial");
                Detail.setTradedesct("Traders Description");
                Detail.setDoorno("1/A");
                Detail.setApfathername_ta("Muthu");
                Detail.setEstablishment_name_ta("Establish");
                Detail.setLicenceValidity("2027");
                Detail.setTraders_license_type_name("TradersType");
                Detail.setTraderPayment("UnPaid");
                Detail.setMobileno("12233445");
                Detail.setPaymentdate("20/05/2020");
                tradersList.add(Detail);
            }else if(i==4){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName("raj");
                Detail.setTraderCode("12");
                Detail.setTraders_typ("Industrial");
                Detail.setTradedesct("Traders Description");
                Detail.setDoorno("1/A");
                Detail.setApfathername_ta("Muthu");
                Detail.setEstablishment_name_ta("Establish");
                Detail.setLicenceValidity("2027");
                Detail.setTraders_license_type_name("TradersType");
                Detail.setTraderPayment("UnPaid");
                Detail.setMobileno("12233445");
                Detail.setPaymentdate("20/05/2020");
                tradersList.add(Detail);
            }else {
            TPtaxModel Detail = new TPtaxModel();
            Detail.setTraderName("raj");
            Detail.setTraderCode("12");
            Detail.setTraders_typ("Industrial");
            Detail.setTradedesct("Traders Description");
            Detail.setDoorno("1/A");
            Detail.setApfathername_ta("Muthu");
            Detail.setEstablishment_name_ta("Establish");
            Detail.setLicenceValidity("2027");
            Detail.setTraders_license_type_name("TradersType");
            Detail.setTraderPayment("Paid");
            Detail.setMobileno("12233445");
            Detail.setPaymentdate("20/05/2020");
            tradersList.add(Detail);}
        }

       /* JSONArray jsonarray=new JSONArray(prefManager.getTradersList());
        if(jsonarray != null && jsonarray.length() >0) {
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String lb_traderscode = jsonobject.getString("lb_traderscode");
                String traders_typ = jsonobject.getString("traders_typ");
                String tradedesct = jsonobject.getString("tradedesct");
                String doorno = jsonobject.getString("doorno");
                String apname_ta = jsonobject.getString("apname_ta");
                String apfathername_ta = jsonobject.getString("apfathername_ta");
                String establishment_name_ta = jsonobject.getString("establishment_name_ta");
                String licence_validity = jsonobject.getString("licence_validity");
                String traders_license_type_name = jsonobject.getString("traders_license_type_name");
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName(apname_ta);
                Detail.setTraderCode(lb_traderscode);
                Detail.setTraders_typ(traders_typ);
                Detail.setTradedesct(tradedesct);
                Detail.setDoorno(doorno);
                Detail.setApfathername_ta(apfathername_ta);
                Detail.setEstablishment_name_ta(establishment_name_ta);
                Detail.setLicenceValidity(licence_validity);
                Detail.setEstablishment_name_ta(establishment_name_ta);
                Detail.setTraders_license_type_name(traders_license_type_name);
                tradersList.add(Detail);
            }
        }*/


        Collections.sort(tradersList, (lhs, rhs) -> lhs.getTraderName().compareTo(rhs.getTraderName()));
        if(tradersList != null && tradersList.size() >0) {
            TraderListAdapter adapter = new TraderListAdapter(ExistingTradeList.this,tradersList);
            adapter.notifyDataSetChanged();
            existingTradeList.tradeRecycler.setAdapter(adapter);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
}
