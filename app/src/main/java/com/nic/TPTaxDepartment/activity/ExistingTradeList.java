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
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
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
        tradersList = new ArrayList<TPtaxModel>();
        tradersList = (ArrayList<TPtaxModel>)getIntent().getSerializableExtra("tradersList");

        try {
            LoadTradersList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void LoadTradersList() throws JSONException {
        /*tradersList = new ArrayList<TPtaxModel>();
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
*/
        if(tradersList != null && tradersList.size() >0) {
            existingTradeList.tradeRecycler.setVisibility(View.VISIBLE);
            existingTradeList.noDataFoundLayout.setVisibility(View.GONE);
            TraderListAdapter adapter = new TraderListAdapter(ExistingTradeList.this,tradersList);
            adapter.notifyDataSetChanged();
            existingTradeList.tradeRecycler.setAdapter(adapter);
        }else {
            existingTradeList.tradeRecycler.setVisibility(View.GONE);
            existingTradeList.noDataFoundLayout.setVisibility(View.VISIBLE);
        }
    }
    public void showTraderDetails( int position , ArrayList<TPtaxModel> tradersList) {
        ArrayList<TPtaxModel> selectedTradersImageList = new ArrayList<TPtaxModel>();
        ArrayList<TPtaxModel> ImageList = new ArrayList<TPtaxModel>();
        try {
            JSONArray jsonarray = new JSONArray(prefManager.getTraderImageList());
            System.out.println("json"+jsonarray.toString());
            if(jsonarray != null && jsonarray.length() >0) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String img = Utils.NotNullString(jsonobject.getString("img"));
                    byte[] ByteArray = img.getBytes();
                    TPtaxModel Detail = new TPtaxModel();
                    Detail.setImageByte(ByteArray);
                    selectedTradersImageList.add(Detail);
                }
            }
            ImageList.add(selectedTradersImageList.get(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent( this, ExistTradeViewClass.class);
        intent.putExtra("position", position);
        intent.putExtra("tradersList", tradersList);
        intent.putExtra("tradersImageList", ImageList);
        intent.putExtra("tradersImagePosition", 0);
        intent.putExtra("flag",true);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

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
