package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nic.TPTaxDepartment.Adapter.FieldVisitListAdapter;
import com.nic.TPTaxDepartment.Adapter.NewTradersListAdapter;
import com.nic.TPTaxDepartment.Adapter.PendingScreenAdapter;
import com.nic.TPTaxDepartment.Adapter.TraderListAdapter;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.Support.ProgressHUD;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class PendingScreen extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener {

    private RecyclerView newTraderRecycler,fieldVisitRecycler;
    public com.nic.TPTaxDepartment.dataBase.dbData dbData = new dbData(this);
    ArrayList<TPtaxModel> newTraderList = new ArrayList<>();
    ArrayList<TPtaxModel> fieldVisitList = new ArrayList<>();
    private ImageView back_img, home_img;
    private TextView newTrader, fieldVisit;
    private RelativeLayout left, right;
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    Context mContext;
    Activity activity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_screen);
        mContext = this;
        activity = this;
        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        newTraderRecycler = (RecyclerView) findViewById(R.id.new_trader_recycler);
        fieldVisitRecycler = (RecyclerView) findViewById(R.id.field_visit_recycler);
        back_img = (ImageView) findViewById(R.id.back_img);
        home_img = (ImageView) findViewById(R.id.home_img);
        newTrader = (TextView) findViewById(R.id.new_trader);
        fieldVisit = (TextView) findViewById(R.id.field_visit);
        left = (RelativeLayout) findViewById(R.id.left);
        right = (RelativeLayout) findViewById(R.id.right);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        newTraderRecycler.setLayoutManager(mLayoutManager);
        newTraderRecycler.setItemAnimator(new DefaultItemAnimator());
        newTraderRecycler.setHasFixedSize(true);
        newTraderRecycler.setNestedScrollingEnabled(false);
        newTraderRecycler.setFocusable(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        fieldVisitRecycler.setLayoutManager(layoutManager);
        fieldVisitRecycler.setItemAnimator(new DefaultItemAnimator());
        fieldVisitRecycler.setHasFixedSize(true);
        fieldVisitRecycler.setNestedScrollingEnabled(false);
        fieldVisitRecycler.setFocusable(false);


        back_img.setOnClickListener(this);
        home_img.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        loadNewTraderList();
        loadFieldVisitList();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPress();
                break;
            case R.id.home_img:
                dashboard();
                break;
            case R.id.left:
                left.setBackground(activity.getResources().getDrawable(R.drawable.left_selected_bg));
                right.setBackground(activity.getResources().getDrawable(R.drawable.right_bg));
                newTrader.setTextColor(activity.getResources().getColor(R.color.white));
                fieldVisit.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                newTraderRecycler.setVisibility(View.VISIBLE);
                fieldVisitRecycler.setVisibility(View.GONE);
                loadNewTraderList();
                break;
            case R.id.right:
                left.setBackground(activity.getResources().getDrawable(R.drawable.left_bg));
                right.setBackground(activity.getResources().getDrawable(R.drawable.right_selected_bg));
                newTrader.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
                fieldVisit.setTextColor(activity.getResources().getColor(R.color.white));
                newTraderRecycler.setVisibility(View.GONE);
                fieldVisitRecycler.setVisibility(View.VISIBLE);
                loadFieldVisitList();
                break;
        }
    }


    private void loadFieldVisitList() {
        fieldVisitList = new ArrayList<TPtaxModel>();
        for (int i = 0; i < 5; i++) {
            if(i==2){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName("raj");
                Detail.setAssessmentId("12");
                Detail.setTaxTypeName("PropertyTax");
                fieldVisitList.add(Detail);
            }else if(i==4){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName("kumar");
                Detail.setAssessmentId("34");
                Detail.setTaxTypeName("WaterTax");
                fieldVisitList.add(Detail);
            }else {
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName("raj");
                Detail.setAssessmentId("12");
                Detail.setTaxTypeName("PropertyTax");
                fieldVisitList.add(Detail);
            }
        }

        Collections.sort(fieldVisitList, (lhs, rhs) -> lhs.getTraderName().compareTo(rhs.getTraderName()));
        if(fieldVisitList != null && fieldVisitList.size() >0) {
            FieldVisitListAdapter adapter = new FieldVisitListAdapter(PendingScreen.this,fieldVisitList);
            adapter.notifyDataSetChanged();
            fieldVisitRecycler.setAdapter(adapter);
        }else {
            Utils.showAlert(this, "No Data Found!");
        }
    }
    private void loadNewTraderList() {
        newTraderList = new ArrayList<TPtaxModel>();
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
                newTraderList.add(Detail);
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
                newTraderList.add(Detail);
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
                newTraderList.add(Detail);}
        }

        Collections.sort(newTraderList, (lhs, rhs) -> lhs.getTraderName().compareTo(rhs.getTraderName()));
        if(newTraderList != null && newTraderList.size() >0) {
            NewTradersListAdapter adapter = new NewTradersListAdapter(PendingScreen.this,newTraderList);
            adapter.notifyDataSetChanged();
            newTraderRecycler.setAdapter(adapter);
        }else {
            Utils.showAlert(this, "No Data Found!");
        }
    }



    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Home", "Home");
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





    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            Runtime.getRuntime().gc();
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("saveActivityImage".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, "Your Activity is Synchronized to the server!");
//                    ODFMonitoringListValue value = prefManager.getLocalSaveDeletedKeyList();
//                    pendingScreenAdapter.notifyDataSetChanged();
//                    long id = db.delete(DBHelper.SAVE_ACTIVITY,"dcode = ? and bcode = ? and pvcode = ? and schedule_id = ? and activity_id = ?",new String[] {value.getDistictCode(),value.getBlockCode(),value.getPvCode(), String.valueOf(value.getScheduleId()), String.valueOf(value.getActivityId())});
//                    new fetchpendingtask().execute();
//                    HomePage.getInstance().getMotivatorSchedule();
                }
                Log.d("savedImage", "" + responseDecryptedBlockKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }


    @Override
    protected void onResume() {
        super.onResume();

    }
}
