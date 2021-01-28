package com.nic.TPTaxDepartment.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nic.TPTaxDepartment.Adapter.DailyCollectionAdapter;
import com.nic.TPTaxDepartment.Adapter.TraderListAdapter;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.databinding.DailyCollectionBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;


public class DailyCollection extends AppCompatActivity implements View.OnClickListener,Api.ServerResponseListener {

    private DailyCollectionBinding dailyCollectionBinding;
    private Handler handler = new Handler();
    private PrefManager prefManager;
    private static TextView date;
    static boolean callApi;
    ArrayList<TPtaxModel> collectionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyCollectionBinding = DataBindingUtil.setContentView(this, R.layout.daily_collection);
        dailyCollectionBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());

        prefManager = new PrefManager(this);
        date = dailyCollectionBinding.date;

        /*dailyCollectionBinding.date.setTranslationX(800);
        dailyCollectionBinding.dateLayout.setTranslationX(800);
        dailyCollectionBinding.recyLayout.setTranslationX(800);*/
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        dailyCollectionBinding.dailyCollectionRecycler.setLayoutManager(mLayoutManager);
        dailyCollectionBinding.dailyCollectionRecycler.setItemAnimator(new DefaultItemAnimator());
        dailyCollectionBinding.dailyCollectionRecycler.setHasFixedSize(true);
        dailyCollectionBinding.dailyCollectionRecycler.setNestedScrollingEnabled(false);
        dailyCollectionBinding.dailyCollectionRecycler.setFocusable(false);

       /* dailyCollectionBinding.date.setAlpha(0);
        dailyCollectionBinding.dateLayout.setAlpha(0);
        dailyCollectionBinding.recyLayout.setAlpha(0);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dailyCollectionBinding.date.animate().translationX(0).alpha(1).setDuration(1400).setStartDelay(400).start();
                dailyCollectionBinding.dateLayout.animate().translationX(0).alpha(1).setDuration(1450).setStartDelay(450).start();
                dailyCollectionBinding.recyLayout.animate().translationX(0).alpha(1).setDuration(1450).setStartDelay(450).start();
              }
        }, 500);


        Animation anim = new ScaleAnimation(
                0.95f, 1f, // Start and end values for the X axis scaling
                0.95f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        anim.setRepeatMode(Animation.INFINITE);
        anim.setRepeatCount(Animation.INFINITE);*/

        Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
       // updateLabel(day + "-" + (month + 1) + "-" + year);
        date.setText("Select Date");

        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                getDailyCollection();
            }
        });

    }

     private void LoadDailyCollectionList() throws JSONException {
         collectionList = new ArrayList<TPtaxModel>();
/*
        for (int i = 0; i < 6; i++) {
            if(i==0){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("1");
                Detail.setTaxTypeName("Property Tax");
                Detail.setTaxCollection("10050.00");
                collectionList.add(Detail);
            }else if(i==1){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("2");
                Detail.setTaxTypeName("Water Tax");
                Detail.setTaxCollection("");
                collectionList.add(Detail);
            }else if(i==2){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("3");
                Detail.setTaxTypeName("SWM Tax");
                Detail.setTaxCollection("70050.00");
                collectionList.add(Detail);
            }else if(i==3){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("4");
                Detail.setTaxTypeName("Professional Tax");
                Detail.setTaxCollection("780050.00");
                collectionList.add(Detail);
            }else if(i==4){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("5");
                Detail.setTaxTypeName("Non Tax");
                Detail.setTaxCollection("10050.00");
                collectionList.add(Detail);
            }else {
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("6");
                Detail.setTaxTypeName("Trade License Tax");
                Detail.setTaxCollection("36050.00");
                collectionList.add(Detail);
            }
        }
*/
        JSONArray jsonarray=new JSONArray(prefManager.getDailyCollectionList());
        if(jsonarray != null && jsonarray.length() >0) {
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String taxtypeid = Utils.NotNullString(jsonobject.getString("taxtypeid"));
                String taxtypedesc_en =Utils.NotNullString(jsonobject.getString("taxtypedesc_en"));
                String collectionreceived = Utils.NotNullString(jsonobject.getString("collectionreceived"));

                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId(taxtypeid);
                Detail.setTaxTypeName(taxtypedesc_en);
                Detail.setTaxCollection(collectionreceived);
                collectionList.add(Detail);
            }
        }

         Collections.sort(collectionList, (lhs, rhs) -> lhs.getTaxTypeName().compareTo(rhs.getTaxTypeName()));
         if(collectionList != null && collectionList.size() >0) {
             DailyCollectionAdapter adapter = new DailyCollectionAdapter(DailyCollection.this,collectionList);
             adapter.notifyDataSetChanged();
             dailyCollectionBinding.dailyCollectionRecycler.setAdapter(adapter);
             dailyCollectionBinding.dailyCollectionRecycler.setVisibility(View.VISIBLE);
             dailyCollectionBinding.noDataFound.setVisibility(View.GONE);
         }else {
             dailyCollectionBinding.dailyCollectionRecycler.setVisibility(View.GONE);
             dailyCollectionBinding.noDataFound.setVisibility(View.VISIBLE);
         }


    }


    public void showDatePickerDialog() {
        DialogFragment newFragment = new datePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public static class datePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        Calendar cldr = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), this, year,
                    month, day);
            cldr.set(year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Do something with the date chosen by the user
//            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            datePickerFragment d = new datePickerFragment();
            String start_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            cldr.set(Calendar.YEAR, year);
            cldr.set(Calendar.MONTH, (monthOfYear));
            cldr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Log.d("startdate", "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            updateLabel(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

        }


    }
    public static String updateLabel(String olddate){
        final String OLD_FORMAT = "dd-MM-yyyy";
        final String NEW_FORMAT = "yyyy-MM-dd";
        String newDateString;
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = null;
        try {
            d = sdf.parse(olddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);
        date.setText(newDateString);


        return  newDateString;
    }

    public  void getDailyCollection() {
        try {
            new ApiService(this).makeJSONObjectRequest("DailyCollection", Api.Method.POST, UrlGenerator.TradersUrl(), dailyCollectionJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject dailyCollectionJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), taxDailyCollectionJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("DailyCollectionReq", "" + authKey);
        return dataSet;
    }

    public JSONObject taxDailyCollectionJsonParams() throws JSONException {

        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"TaxDailyCollection");
        data.put(AppConstant.COLLECTION_DATE,date.getText().toString());
        return data;
    }



    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("DailyCollection".equals(urlType) && responseObj != null) {
                String key =Utils.NotNullString( responseObj.getString(AppConstant.ENCODE_DATA));
                String responseDecryptedSchemeKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedSchemeKey);
                Log.d("AssessmentStatus", "" + jsonObject);
                String status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.DATA);
                    if(jsonarray != null && jsonarray.length() >0) {
                        prefManager.setDailyCollectionList(jsonarray.toString());
                        LoadDailyCollectionList();
                    }

                } else {
                    dailyCollectionBinding.dailyCollectionRecycler.setVisibility(View.GONE);
                    dailyCollectionBinding.noDataFound.setVisibility(View.VISIBLE);
                }
//                String authKey = responseDecryptedSchemeKey.toString();
//                int maxLogSize = 4000;
//                for(int i = 0; i <= authKey.length() / maxLogSize; i++) {
//                    int start = i * maxLogSize;
//                    int end = (i+1) * maxLogSize;
//                    end = end > authKey.length() ? authKey.length() : end;
//                    Log.v("to_send", authKey.substring(start, end));
//                }
//                Log.d("WorkListResp", "" + responseDecryptedSchemeKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnError(VolleyError volleyError) {

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
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onClick(View v) {

    }
}