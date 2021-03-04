package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.Call;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.nic.TPTaxDepartment.Interface.AdapterInterface;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.Support.ProgressHUD;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class PendingScreen extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener, AdapterInterface {

    private RecyclerView newTraderRecycler, fieldVisitRecycler;
    public com.nic.TPTaxDepartment.dataBase.dbData dbData = new dbData(this);
    ArrayList<TPtaxModel> newTraderList = new ArrayList<>();
    ArrayList<TPtaxModel> fieldVisitList = new ArrayList<>();
    ArrayList<CommonModel> fieldVisitPendingList;
    private ImageView back_img, home_img;
    private TextView newTrader, fieldVisit;
    private RelativeLayout left, right;
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    Context mContext;
    Activity activity;
    private Handler handler = new Handler();

    RelativeLayout no_data_fond_layout, satisfied_rl, un_satisfied_rl, need_improvement_rl;
    int recyclerClickedPosition;

    NewTradersListAdapter newTradersListAdapter;
    JSONObject dataset;
    FieldVisitListAdapter adapter;
    int pos;
    private ArrayList<CommonModel> filterListSatisfied;
    private ArrayList<CommonModel> filterListUnSatisfied;
    private ArrayList<CommonModel> filterListNeedImprovement;

    View v_1, v_2, v_3;
    String screen_enable = "";
    Paint p = new Paint();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_screen);
        mContext = this;
        activity = this;
        dataset = new JSONObject();
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

        no_data_fond_layout = findViewById(R.id.no_data_found_layout);
        newTraderRecycler = (RecyclerView) findViewById(R.id.new_trader_recycler);
        fieldVisitRecycler = (RecyclerView) findViewById(R.id.field_visit_recycler);
        back_img = (ImageView) findViewById(R.id.refresh);
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

        satisfied_rl = findViewById(R.id.satisfied_rl);
        satisfied_rl.setOnClickListener(this::onClick);

        un_satisfied_rl = findViewById(R.id.un_satisfied_rl);
        un_satisfied_rl.setOnClickListener(this::onClick);

        need_improvement_rl = findViewById(R.id.need_improvemnt_rl);
        need_improvement_rl.setOnClickListener(this::onClick);
        loadFieldVisitListPending();
        v_1 = findViewById(R.id.v_1);
        v_2 = findViewById(R.id.v_2);
        v_3 = findViewById(R.id.v_3);


        v_1.setVisibility(View.VISIBLE);
        v_2.setVisibility(View.GONE);
        v_3.setVisibility(View.GONE);

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

//        loadNewTraderList();
//        loadFieldVisitList();

        left.setBackground(activity.getResources().getDrawable(R.drawable.left_selected_bg));
        right.setBackground(activity.getResources().getDrawable(R.drawable.right_bg));
        newTrader.setTextColor(activity.getResources().getColor(R.color.white));
        fieldVisit.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        fieldVisitRecycler.setVisibility(View.VISIBLE);
        newTraderRecycler.setVisibility(View.GONE);
        setAdapterField("Satisfied");
        //swipLayout();
        initSwipe();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
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
                //loadFieldVisitList();
                loadFieldVisitListPending();
                break;

            case R.id.satisfied_rl:
                v_1.setVisibility(View.VISIBLE);
                v_2.setVisibility(View.GONE);
                v_3.setVisibility(View.GONE);
                setAdapterField("Satisfied");
                break;
            case R.id.un_satisfied_rl:
                v_1.setVisibility(View.GONE);
                v_2.setVisibility(View.VISIBLE);
                v_3.setVisibility(View.GONE);
                setAdapterField("Unsatisfied");
                break;
            case R.id.need_improvemnt_rl:
                v_1.setVisibility(View.GONE);
                v_2.setVisibility(View.GONE);
                v_3.setVisibility(View.VISIBLE);
                setAdapterField("Need improvement");
                break;
        }
    }


    private void loadFieldVisitList() {
        fieldVisitList = new ArrayList<TPtaxModel>();
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName("raj");
                Detail.setAssessmentId("12");
                Detail.setTaxTypeName("PropertyTax");
                fieldVisitList.add(Detail);
            } else if (i == 4) {
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName("kumar");
                Detail.setAssessmentId("34");
                Detail.setTaxTypeName("WaterTax");
                fieldVisitList.add(Detail);
            } else {
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName("raj");
                Detail.setAssessmentId("12");
                Detail.setTaxTypeName("PropertyTax");
                fieldVisitList.add(Detail);
            }
        }

        Collections.sort(fieldVisitList, (lhs, rhs) -> lhs.getTraderName().compareTo(rhs.getTraderName()));
        if (fieldVisitList != null && fieldVisitList.size() > 0) {
            no_data_fond_layout.setVisibility(View.GONE);
            FieldVisitListAdapter adapter = new FieldVisitListAdapter(PendingScreen.this, fieldVisitPendingList);
            adapter.notifyDataSetChanged();
            fieldVisitRecycler.setAdapter(adapter);
            fieldVisitRecycler.setVisibility(View.VISIBLE);
        } else {
            no_data_fond_layout.setVisibility(View.VISIBLE);
            fieldVisitRecycler.setVisibility(View.GONE);
            //Utils.showAlert(this, "No Data Found!");
        }
    }

    private void loadFieldVisitListPending() {
        fieldVisitPendingList = new ArrayList<>();
        String select_query = "SELECT *FROM " + DBHelper.SAVE_FIELD_VISIT;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    CommonModel Detail = new CommonModel();
                    Detail.setRequest_id(cursor.getString(cursor.getColumnIndexOrThrow("request_id")));
                    Detail.setService_list_field_visit_service_id(cursor.getString(cursor.getColumnIndexOrThrow("serviceid")));
                    Detail.setOwnername(cursor.getString(cursor.getColumnIndexOrThrow("owner_name")));
                    Detail.setTaxtypeid(cursor.getString(cursor.getColumnIndexOrThrow("taxtypeid")));
                    Detail.setData_ref_id(cursor.getString(cursor.getColumnIndexOrThrow("data_ref_id")));
                    Detail.setFIELD_VISIT_STATUS_ID(cursor.getString(cursor.getColumnIndexOrThrow("field_visit_status")));
                    Detail.setRemark(cursor.getString(cursor.getColumnIndexOrThrow("remark")));
                    Detail.setTaxtypedesc_en(cursor.getString(cursor.getColumnIndexOrThrow("tax_type_name")));
                    Detail.setFIELD_VISIT_STATUS(cursor.getString(cursor.getColumnIndexOrThrow("field_visit_status_name")));

                    fieldVisitPendingList.add(Detail);
                } while (cursor.moveToNext());
            }
        }
        Collections.sort(fieldVisitPendingList, (lhs, rhs) -> lhs.getOwnername().compareTo(rhs.getOwnername()));
        filterList();
      /*  if(fieldVisitPendingList != null && fieldVisitPendingList.size() >0) {
            no_data_fond_layout.setVisibility(View.GONE);
            adapter = new FieldVisitListAdapter(PendingScreen.this,fieldVisitPendingList);
            adapter.notifyDataSetChanged();
            fieldVisitRecycler.setAdapter(adapter);
            fieldVisitRecycler.setVisibility(View.VISIBLE);
        }else {
            no_data_fond_layout.setVisibility(View.VISIBLE);
            fieldVisitRecycler.setVisibility(View.GONE);
            //Utils.showAlert(this, "No Data Found!");
        }

*/
    }

    private void loadNewTraderList() {
        newTraderList = new ArrayList<TPtaxModel>();
       /* for (int i = 0; i < 5; i++) {
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
        }*/
        String select_query = "SELECT *FROM " + DBHelper.SAVE_NEW_TRADER_DETAILS;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TPtaxModel Detail = new TPtaxModel();
                    Detail.setTraderName(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.APPLICANT_NAME_EN)));
                    Detail.setTraderCode(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TRADE_CODE)));
                    Detail.setTradecodeId(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TRADE_CODE_ID)));
                    Detail.setTradedesce(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TRADE_DESCRIPTION)));
                    Detail.setWardId(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_ID)));
                    Detail.setWardname(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_NAME_EN)));
                    Detail.setStreetId(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_ID)));
                    Detail.setStreetname(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_NAME_EN)));
                    Detail.setDoorno(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DOOR_NO)));
                    Detail.setApfathername_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FATHER_HUSBAND_NAME_TA)));
                    Detail.setEstablishment_name_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.ESTABLISHMENT_NAME_TA)));
                    Detail.setLicenceValidity(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LICENCE_VALIDITY)));
                    Detail.setLicence_validity(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LICENCE_VALIDITY_ID)));
                    Detail.setMobileno(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.MOBILE)));
                    Detail.setTrade_date(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DATE)));
                    Detail.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.E_MAIL)));
                    Detail.setLicencetypeid(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LICENCE_TYPE_ID)));
                    Detail.setTraders_license_type_name(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LICENCE_TYPE)));
                    Detail.setApgender(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.GENDER)));
                    Detail.setApgenderId(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.GENDER_CODE)));
                    Detail.setApage(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.AGE)));
                    Detail.setApfathername_en(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FATHER_HUSBAND_NAME_EN)));
                    Detail.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LATITUDE)));
                    Detail.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LONGITUDE)));
                    Detail.setPaymentStatus(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PAYMENT_STATUS)));
                    Detail.setTradeImage(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TRADE_IMAGE)));
                    Detail.setApname_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.APPLICANT_NAME_TA)));
                    Detail.setEstablishment_name_en(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.ESTABLISHMENT_NAME_EN)));
                    Detail.setDescription_en(cursor.getString(cursor.getColumnIndexOrThrow("description_en")));
                    Detail.setDescription_ta(cursor.getString(cursor.getColumnIndexOrThrow("description_ta")));
                    newTraderList.add(Detail);
                } while (cursor.moveToNext());
            }
        }
        Collections.sort(newTraderList, (lhs, rhs) -> lhs.getTraderName().compareTo(rhs.getTraderName()));
        if (newTraderList != null && newTraderList.size() > 0) {
            no_data_fond_layout.setVisibility(View.GONE);
            newTraderRecycler.setVisibility(View.VISIBLE);
            newTradersListAdapter = new NewTradersListAdapter(PendingScreen.this, newTraderList);
            newTradersListAdapter.notifyDataSetChanged();
            newTraderRecycler.setAdapter(newTradersListAdapter);
        } else {
            no_data_fond_layout.setVisibility(View.VISIBLE);
            newTraderRecycler.setVisibility(View.GONE);
            //Utils.showAlert(this, "No Data Found!");
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
            String status1;
            if ("saveActivityImage".equals(urlType) && responseObj != null) {
                String key = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (Utils.NotNullString(jsonObject.getString("STATUS")).equalsIgnoreCase("OK") && Utils.NotNullString(jsonObject.getString("RESPONSE")).equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, getApplicationContext().getResources().getString(R.string.synchronized_to_server));
//                    ODFMonitoringListValue value = prefManager.getLocalSaveDeletedKeyList();
//                    pendingScreenAdapter.notifyDataSetChanged();
//                    long id = db.delete(DBHelper.SAVE_ACTIVITY,"dcode = ? and bcode = ? and pvcode = ? and schedule_id = ? and activity_id = ?",new String[] {value.getDistictCode(),value.getBlockCode(),value.getPvCode(), String.valueOf(value.getScheduleId()), String.valueOf(value.getActivityId())});
//                    new fetchpendingtask().execute();
//                    HomePage.getInstance().getMotivatorSchedule();
                }
                Log.d("savedImage", "" + responseDecryptedBlockKey);
            }
            if ("SaveLicenseTraders".equals(urlType) && responseObj != null) {
                String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                JSONObject jsonObject = new JSONObject(userDataDecrypt);
                String status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                Log.d("Response", "" + userDataDecrypt);
                //String status =  Utils.NotNullString(responseObj.getString(AppConstant.KEY_STATUS));
                //String response =  Utils.NotNullString(responseObj.getString(AppConstant.KEY_RESPONSE));
                if (status.equalsIgnoreCase("SUCCESS")) {
                    //JSONObject jsonObject = responseObj.getJSONObject(AppConstant.JSON_DATA);
                    //JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.DATA);
//                    String Motivatorid =  Utils.NotNullString(jsonObject.getString(AppConstant.KEY_REGISTER_MOTIVATOR_ID));
//                    Log.d("motivatorid",""+Motivatorid);
                    Utils.showAlert(this, Utils.NotNullString(jsonObject.getString("MESSAGE")));
                    Utils.showAlert(this, Utils.NotNullString(jsonObject.getString("MESSAGE_TA")));
                    Dashboard.db.delete(DBHelper.SAVE_NEW_TRADER_DETAILS, AppConstant.MOBILE + "=?", new String[]{newTraderList.get(recyclerClickedPosition).getMobileno()});
                    Dashboard.db.delete(DBHelper.SAVE_TRADE_IMAGE, AppConstant.MOBILE + "=?", new String[]{newTraderList.get(recyclerClickedPosition).getMobileno()});
                    loadNewTraderList();
                    /*Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            //finish();
                        }
                    };
                    handler.postDelayed(runnable, 2000);*/

                } else if (status.equalsIgnoreCase("FAILD")) {
                    Utils.showAlert(this, Utils.NotNullString(jsonObject.getString("MESSAGE")));
                }
            }
            if ("save_data".equals(urlType)) {
                try {
                    String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                    String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                    Log.d("userdatadecry", "" + userDataDecrypt);
                    JSONObject jsonObject = new JSONObject(userDataDecrypt);
                    status1 = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    if (status1.equalsIgnoreCase("SUCCESS")) {
                        Utils.showAlert(PendingScreen.this, jsonObject.getString("MESSAGE"));
                        adapter.deleteRow(pos);
                    } else if (status1.equalsIgnoreCase("FAILD")) {
                        Utils.showAlert(PendingScreen.this, jsonObject.getString("MESSAGE"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showAlert(PendingScreen.this, getApplicationContext().getResources().getString(R.string.pending_not_saved));
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {
        Utils.showAlert(this, getApplicationContext().getResources().getString(R.string.try_after_some_time));

    }


    /* @Override
     protected void onResume() {
         super.onResume();
         loadNewTraderList();
     }*/
    public void SaveLicenseTraders(int pos) {
        recyclerClickedPosition = pos;
        try {
            new ApiService(this).makeJSONObjectRequest("SaveLicenseTraders", Api.Method.POST, UrlGenerator.TradersUrl(), dataSavedEncryptJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject dataSavedEncryptJsonParams() throws JSONException {
        JSONArray imageArray = new JSONArray();
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataTobeSavedJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        JSONObject dataSet1 = new JSONObject();

        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        //dataSet.put(AppConstant.LATITUDE, lat);
        //dataSet.put(AppConstant.LONGITUDE, lan);
        dataSet1.put(AppConstant.TRADE_IMAGE, newTraderList.get(recyclerClickedPosition).getTradeImage());
        imageArray.put(dataSet1);
        dataSet.put(AppConstant.ATTACHMENT_FILES, imageArray);
        Log.d("TraderLicenseTypeList", "" + authKey);
        Log.d("DataSetS__:", "" + dataSet);
        return dataSet;
    }

    public JSONObject dataTobeSavedJsonParams() throws JSONException {

        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "SaveLicenseTraders");
        dataSet.put(AppConstant.MODE, "NEW");
        dataSet.put(AppConstant.TRADE_CODE, newTraderList.get(recyclerClickedPosition).getTradecodeId());
        dataSet.put(AppConstant.DATE, newTraderList.get(recyclerClickedPosition).getTrade_date());
        dataSet.put(AppConstant.LICENCE_TYPE_ID, newTraderList.get(recyclerClickedPosition).getLicencetypeid());
        dataSet.put(AppConstant.TRADE_DESCRIPTION, newTraderList.get(recyclerClickedPosition).getTradedesce());
        dataSet.put(AppConstant.APPLICANT_NAME_EN, newTraderList.get(recyclerClickedPosition).getTraderName());
        dataSet.put(AppConstant.APPLICANT_NAME_TA, newTraderList.get(recyclerClickedPosition).getApname_ta());
        dataSet.put(AppConstant.GENDER, newTraderList.get(recyclerClickedPosition).getApgenderId());
        dataSet.put(AppConstant.AGE, newTraderList.get(recyclerClickedPosition).getApage());
        dataSet.put(AppConstant.FATHER_HUSBAND_NAME_EN, newTraderList.get(recyclerClickedPosition).getApfathername_en());
        dataSet.put(AppConstant.FATHER_HUSBAND_NAME_TA, newTraderList.get(recyclerClickedPosition).getApfathername_ta());
        dataSet.put(AppConstant.MOBILE, newTraderList.get(recyclerClickedPosition).getMobileno());
        dataSet.put(AppConstant.E_MAIL, newTraderList.get(recyclerClickedPosition).getEmail());
        dataSet.put(AppConstant.ESTABLISHMENT_NAME_EN, newTraderList.get(recyclerClickedPosition).getEstablishment_name_en());
        dataSet.put(AppConstant.ESTABLISHMENT_NAME_TA, newTraderList.get(recyclerClickedPosition).getEstablishment_name_ta());
//        dataSet.put(AppConstant.WARD_ID, selectedWardId);
        dataSet.put(AppConstant.STREET_ID, newTraderList.get(recyclerClickedPosition).getStreetId());
        dataSet.put(AppConstant.DOOR_NO, newTraderList.get(recyclerClickedPosition).getDoorno());
        dataSet.put(AppConstant.LICENCE_VALIDITY, newTraderList.get(recyclerClickedPosition).getLicence_validity());
        dataSet.put("wardid", newTraderList.get(recyclerClickedPosition).getWardId());
        dataSet.put("description_en", newTraderList.get(recyclerClickedPosition).getDescription_en());
        dataSet.put("description_ta", newTraderList.get(recyclerClickedPosition).getDescription_ta());
        dataSet.put(AppConstant.LATITUDE, newTraderList.get(recyclerClickedPosition).getLatitude());
        dataSet.put(AppConstant.LONGITUDE, newTraderList.get(recyclerClickedPosition).getLongitude());
        //dataSet.put(AppConstant.TRADE_IMAGE, image);

        Log.d("DataSet", "" + dataSet);
        String authKey = dataSet.toString();
        int maxLogSize = 2000;
        for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > authKey.length() ? authKey.length() : end;
            Log.v("to_send+_plain", authKey.substring(start, end));
        }
        return dataSet;
    }

    @Override
    public void newTraderecyclerPosition(int pos) {
        recyclerClickedPosition = pos;
        //SaveLicenseTraders();
    }

    @Override
    public void fieldVisitRecyclerposition(int pos) {

    }

    /* @Override
     protected void onRestart() {
         super.onRestart();
         newTradersListAdapter.notifyDataSetChanged();
     }
 */
    public void noDataLayout() {
        no_data_fond_layout.setVisibility(View.VISIBLE);
        fieldVisitRecycler.setVisibility(View.GONE);
        newTraderRecycler.setVisibility(View.GONE);
    }

    public void sync_data() {
        try {
            new ApiService(this).makeJSONObjectRequest("save_data", Api.Method.POST, UrlGenerator.TradersUrl(), pendingFieldJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject pendingFieldJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("saving", "" + authKey);
        Log.d("dataSet", "" + dataSet);
        return dataSet;
    }

    public void jsonDatasetValues(String request_id, int pos1) {
        pos = pos1;
        no_data_fond_layout.setVisibility(View.GONE);
        newTraderRecycler.setVisibility(View.GONE);
        ArrayList<TPtaxModel> commonModels = new ArrayList<>();
        JSONArray jsonArray = new JSONArray();
        dbData.open();

//        commonModels.addAll(dbData.selectPendingImage(request_id));
        commonModels = new ArrayList<>(dbData.selectFieldVisitImage(request_id));

        for (int i = 0; i < commonModels.size(); i++) {
            JSONObject imageArray = new JSONObject();
            try {
                if (request_id.equals(commonModels.get(i).getRequest_id())) {
                    //imageArray.put(i);
                    imageArray.put("lat", commonModels.get(i).getLatitude());
                    imageArray.put("long", commonModels.get(i).getLongitude());
                    imageArray.put("photo", bitmapToString(commonModels.get(i).getImage()));
                    //imageArray.put(description);
                    jsonArray.put(imageArray);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("jsonArray" + jsonArray.toString());
        try {
            dataset.put("image_details", jsonArray);
            dataset.put(AppConstant.KEY_SERVICE_ID, "FieldVisitStatusUpdate");
            dataset.put("taxtypeid", fieldVisitPendingList.get(pos1).getTaxtypeid());
            dataset.put("serviceid", fieldVisitPendingList.get(pos1).getService_list_field_visit_service_id());
            dataset.put("request_id", request_id);
            dataset.put("data_ref_id", fieldVisitPendingList.get(pos1).getData_ref_id());
            dataset.put("field_visit_status", fieldVisitPendingList.get(pos1).getFIELD_VISIT_STATUS_ID());
            dataset.put("remark", fieldVisitPendingList.get(pos1).getRemark());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (Utils.isOnline()) {
            sync_data();
        } else {
            Utils.showAlert(PendingScreen.this, getApplicationContext().getResources().getString(R.string.no_internet_connection));
        }

    }

    public String bitmapToString(Bitmap bitmap1) {
        byte[] imageInByte = new byte[0];
        String image_str = "";
        Bitmap bitmap = bitmap1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        imageInByte = baos.toByteArray();
        image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);
        return image_str;
    }

    public void showImageList(String requestid) {
        Intent intent = new Intent(PendingScreen.this, FullImageActivity.class);
        intent.putExtra("request_id", requestid);
        intent.putExtra("key", "FieldVisit");
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void gotoNewTradeLicenseScreen(int pos) {
        Intent intent = new Intent(activity, NewTradeLicenceScreen.class);
        intent.putExtra("flag", true);
        intent.putExtra("position", pos);
        intent.putExtra("tradersList", (Serializable) newTraderList);
        startActivityForResult(intent, 2);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


    }

    public void restart(int value) {
        if (value == 1) {
            loadNewTraderList();
            left.setBackground(activity.getResources().getDrawable(R.drawable.left_selected_bg));
            right.setBackground(activity.getResources().getDrawable(R.drawable.right_bg));
            newTrader.setTextColor(activity.getResources().getColor(R.color.white));
            fieldVisit.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            fieldVisitRecycler.setVisibility(View.GONE);
            newTraderRecycler.setVisibility(View.VISIBLE);
        } else {
            loadFieldVisitListPending();
            left.setBackground(activity.getResources().getDrawable(R.drawable.left_bg));
            right.setBackground(activity.getResources().getDrawable(R.drawable.right_selected_bg));
            newTrader.setTextColor(activity.getResources().getColor(R.color.colorPrimary));
            fieldVisit.setTextColor(activity.getResources().getColor(R.color.white));
            newTraderRecycler.setVisibility(View.GONE);
            fieldVisitRecycler.setVisibility(View.VISIBLE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            restart(2);
        } else if (requestCode == 2) {
            restart(1);
        }
    }

    public void loadFieldList(ArrayList<CommonModel> fields) {
        if (v_1.getVisibility() == View.VISIBLE) {
            screen_enable = "Satisfied";
        } else if (v_2.getVisibility() == View.VISIBLE) {
            screen_enable = "Unsatisfied";
        } else if (v_3.getVisibility() == View.VISIBLE) {
            screen_enable = "Need improvement";
        }
        Intent intent = new Intent(PendingScreen.this, FieldVisit.class);
        intent.putExtra("flag", true);
        intent.putExtra("position", 0);
        intent.putExtra("fieldsList", fields);
        startActivityForResult(intent, 1);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void filterList() {
        filterListSatisfied = new ArrayList<>();
        filterListUnSatisfied = new ArrayList<>();
        filterListNeedImprovement = new ArrayList<>();
        for (int i = 0; i < fieldVisitPendingList.size(); i++) {
            if ("Satisfied".equals(fieldVisitPendingList.get(i).getFIELD_VISIT_STATUS())) {
                filterListSatisfied.add(fieldVisitPendingList.get(i));
            } else if ("Unsatisfied".equals(fieldVisitPendingList.get(i).getFIELD_VISIT_STATUS())) {
                filterListUnSatisfied.add(fieldVisitPendingList.get(i));
            } else {
                filterListNeedImprovement.add(fieldVisitPendingList.get(i));
            }
        }
        if (screen_enable.equals("Satisfied")) {
            setAdapterField("Satisfied");
        } else if (screen_enable.equals("Unsatisfied")) {
            setAdapterField("Unsatisfied");
        } else if (screen_enable.equals("Need improvement")) {
            setAdapterField("Need improvement");
        }
    }

    public void setAdapterField(String condition) {
        if (condition.equals("Satisfied")) {
            if (filterListSatisfied.size() > 0) {
                no_data_fond_layout.setVisibility(View.GONE);
                adapter = new FieldVisitListAdapter(PendingScreen.this, filterListSatisfied);
                adapter.notifyDataSetChanged();
                fieldVisitRecycler.setAdapter(adapter);
                fieldVisitRecycler.setVisibility(View.VISIBLE);
            } else {
                no_data_fond_layout.setVisibility(View.VISIBLE);
                fieldVisitRecycler.setVisibility(View.GONE);
                //Utils.showAlert(this, "No Data Found!");
            }
        } else if (condition.equals("Unsatisfied")) {
            if (filterListUnSatisfied.size() > 0) {
                no_data_fond_layout.setVisibility(View.GONE);
                adapter = new FieldVisitListAdapter(PendingScreen.this, filterListUnSatisfied);
                adapter.notifyDataSetChanged();
                fieldVisitRecycler.setAdapter(adapter);
                fieldVisitRecycler.setVisibility(View.VISIBLE);
            } else {
                no_data_fond_layout.setVisibility(View.VISIBLE);
                fieldVisitRecycler.setVisibility(View.GONE);
                //Utils.showAlert(this, "No Data Found!");
                //Utils.showAlert(this, "No Data Found!");
            }
        } else {
            if (filterListNeedImprovement.size() > 0) {
                no_data_fond_layout.setVisibility(View.GONE);
                adapter = new FieldVisitListAdapter(PendingScreen.this, filterListNeedImprovement);
                adapter.notifyDataSetChanged();
                fieldVisitRecycler.setAdapter(adapter);
                fieldVisitRecycler.setVisibility(View.VISIBLE);
            } else {
                no_data_fond_layout.setVisibility(View.VISIBLE);
                fieldVisitRecycler.setVisibility(View.GONE);
                //Utils.showAlert(this, "No Data Found!");                }
            }
        }


    }

    public void swipLayout() {

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        final int position = viewHolder.getAdapterPosition();
                        adapter.deleteRow(position);
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    // Get RecyclerView item from the ViewHolder
                    View itemView = viewHolder.itemView;

                    Paint p = new Paint();
                    if (dX > 0) {
                        /* Set your color for positive displacement */

                        // Draw Rect with varying right side, equal to displacement dX
                        c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                                (float) itemView.getBottom(), p);

                    } else {
                        /* Set your color for negative displacement */
                        RectF rightButton = new RectF(itemView.getRight() , itemView.getTop(), itemView.getRight(), itemView.getBottom());

                        // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
                        c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                (float) itemView.getRight(), (float) itemView.getBottom(), p);
                        drawText("DELETE",c,rightButton,p);
                    }

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(fieldVisitRecycler);
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    adapter.deleteRow(position);
                }
                /*else {
                    removeView();
                    edit_position = position;
                    alertDialog.setTitle("Edit Country");
                    et_country.setText(countries.get(position));
                    alertDialog.show();
                }*/
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon=null;


                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.new_icon_delete);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.new_icon_delete);
                        //icon = ((BitmapDrawable) getResources().getDrawable(R.drawable.ic_trash)).getBitmap();
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(fieldVisitRecycler);
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 30;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
        c.drawText(text, button.centerX() - (textWidth / 2), button.centerY() + (textSize / 2), p);
    }

    public Bitmap vectortoBimap(Drawable drawable){
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }
    }

}
