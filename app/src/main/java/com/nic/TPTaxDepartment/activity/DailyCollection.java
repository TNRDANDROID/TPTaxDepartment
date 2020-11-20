package com.nic.TPTaxDepartment.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.android.volley.VolleyError;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.databinding.DailyCollectionBinding;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class DailyCollection extends AppCompatActivity implements View.OnClickListener,Api.ServerResponseListener {

    private DailyCollectionBinding dailyCollectionBinding;
    private Handler handler = new Handler();
    private PrefManager prefManager;
    private static TextView date;
    static boolean callApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyCollectionBinding = DataBindingUtil.setContentView(this, R.layout.daily_collection);
        dailyCollectionBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());

        prefManager = new PrefManager(this);
        date = dailyCollectionBinding.date;

        dailyCollectionBinding.date.setTranslationX(800);
        dailyCollectionBinding.dateLayout.setTranslationX(800);
        dailyCollectionBinding.voteprogresscard.setTranslationX(800);
        dailyCollectionBinding.attendanecard.setTranslationX(800);
        dailyCollectionBinding.cameracard.setTranslationX(800);
        dailyCollectionBinding.votecountcard.setTranslationX(800);
        dailyCollectionBinding.viewPollingStationImage.setTranslationX(800);


        dailyCollectionBinding.date.setAlpha(0);
        dailyCollectionBinding.dateLayout.setAlpha(0);
        dailyCollectionBinding.voteprogresscard.setAlpha(0);
        dailyCollectionBinding.attendanecard.setAlpha(0);
        dailyCollectionBinding.cameracard.setAlpha(0);
        dailyCollectionBinding.votecountcard.setAlpha(0);
        dailyCollectionBinding.viewPollingStationImage.setAlpha(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dailyCollectionBinding.date.animate().translationX(0).alpha(1).setDuration(1400).setStartDelay(400).start();
                dailyCollectionBinding.dateLayout.animate().translationX(0).alpha(1).setDuration(1450).setStartDelay(450).start();
                dailyCollectionBinding.voteprogresscard.animate().translationX(0).alpha(1).setDuration(1500).setStartDelay(600).start();
                dailyCollectionBinding.attendanecard.animate().translationX(0).alpha(1).setDuration(1600).setStartDelay(800).start();
                dailyCollectionBinding.cameracard.animate().translationX(0).alpha(1).setDuration(1700).setStartDelay(1000).start();
                dailyCollectionBinding.votecountcard.animate().translationX(0).alpha(1).setDuration(1800).setStartDelay(1200).start();
                dailyCollectionBinding.viewPollingStationImage.animate().translationX(0).alpha(1).setDuration(1800).setStartDelay(1400).start();
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
        anim.setRepeatCount(Animation.INFINITE);


        dailyCollectionBinding.propertyTax.startAnimation(anim);
        dailyCollectionBinding.waterTax.startAnimation(anim);
        dailyCollectionBinding.proffTax.startAnimation(anim);
        dailyCollectionBinding.nonTax.startAnimation(anim);
        dailyCollectionBinding.tradeLicense.startAnimation(anim);

        Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        date.setText(day + "-" + (month + 1) + "-" + year);

       // getDailyCollection();
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new datePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public static class datePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        static Calendar cldr = Calendar.getInstance();

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
            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            String start_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            cldr.set(Calendar.YEAR, year);
            cldr.set(Calendar.MONTH, (monthOfYear));
            cldr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Log.d("startdate", "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }

    }

    public void getDailyCollection() {
        try {
            new ApiService(this).makeJSONObjectRequest("DailyCollection", Api.Method.POST, UrlGenerator.saveTradersUrl(), dailyCollectionJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject dailyCollectionJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), taxDailyCollectionJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("workList", "" + authKey);
        return dataSet;
    }

    public JSONObject taxDailyCollectionJsonParams() throws JSONException {

        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"TaxDailyCollection");
        data.put(AppConstant.COLLECTION_DATE,"2020-04-09");
        return data;
    }



    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("DailyCollection".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedSchemeKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedSchemeKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    dailyCollectionBinding.propertyTax.setText("");
                    dailyCollectionBinding.waterTax.setText("");
                    dailyCollectionBinding.proffTax.setText("");
                    dailyCollectionBinding.nonTax.setText("");
                    dailyCollectionBinding.tradeLicense.setText("");

                } else if(jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")){
                    Utils.showAlert(this,"NO RECORD FOUND!");
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