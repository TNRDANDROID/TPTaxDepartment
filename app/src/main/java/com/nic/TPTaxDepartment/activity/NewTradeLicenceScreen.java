package com.nic.TPTaxDepartment.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.Support.MyCustomTextView;
import com.nic.TPTaxDepartment.constant.AppConstant;

import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.databinding.NewTradeLicenceScreenBinding;

import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.CameraUtils;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewTradeLicenceScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {
    NewTradeLicenceScreenBinding newTradeLicenceScreenBinding; 
    private List<String> GenderList = new ArrayList<>();
    private List<TPtaxModel> District = new ArrayList<>();
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    String[] mApps = {"Male", "Female", "Others"};
    private PrefManager prefManager;
    String pref_district;
    private Handler handler = new Handler();
    private static TextView date;
    private SQLiteDatabase db;
    public static DBHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newTradeLicenceScreenBinding = DataBindingUtil.setContentView(this, R.layout.new_trade_licence_screen);
        newTradeLicenceScreenBinding.setActivity(this);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mApps);
        newTradeLicenceScreenBinding.licenceValidity.setAdapter(adapter);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        newTradeLicenceScreenBinding.scrollView.setNestedScrollingEnabled(true);
        date = newTradeLicenceScreenBinding.date;
        loadGenderList();
       // getLicenceTypeList();
       // getWardList();
       // getStreetList();

        String colored = "*";
        String mobileView= "கைபேசி எண் / Mobile No";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(mobileView);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        newTradeLicenceScreenBinding.mobileHint.setText(builder);

        newTradeLicenceScreenBinding.licenceValidity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {

                }
//                pref_district = District.get(position).getDistrictName();
                prefManager.setDistrictName(pref_district);


//                prefManager.setDistrictCode(District.get(position).getDistictCode());


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void loadGenderList() {
        GenderList.clear();
        GenderList.add("Select Gender");
        GenderList.add("Male");
        GenderList.add("Female");
        GenderList.add("Others");
        ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, GenderList);
        RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newTradeLicenceScreenBinding.gender.setAdapter(RuralUrbanArray);

    }

    @Override
    public void onClick(View v) {

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

    public void getWardList() {
        try {
            new ApiService(this).makeJSONObjectRequest("WardList", Api.Method.POST, UrlGenerator.prodOpenUrl(), wardJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject wardJsonParams() throws JSONException {

        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"Ward");
        data.put(AppConstant.KEY_STATE_CODE,"");
        data.put(AppConstant.KEY_DISTRICT_CODE,prefManager.getDistrictCode());
        data.put(AppConstant.KEY_LB_CODE,"");
        return data;
    }

    public void getStreetList() {
        try {
            new ApiService(this).makeJSONObjectRequest("StreetList", Api.Method.POST, UrlGenerator.prodOpenUrl(), streetJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject streetJsonParams() throws JSONException {

        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"Street");
        data.put(AppConstant.KEY_STATE_CODE,"");
        data.put(AppConstant.KEY_DISTRICT_CODE,prefManager.getDistrictCode());
        data.put(AppConstant.KEY_LB_CODE,"");
        return data;
    }

    public void getLicenceTypeList() {
        try {
            new ApiService(this).makeJSONObjectRequest("TraderLicenseTypeList", Api.Method.POST, UrlGenerator.saveTradersUrl(), licencelistJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject licencelistJsonParams() throws JSONException{


        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "TraderLicenseTypeList");
        return dataSet;
    }

    public void signUP() {
        try {
            new ApiService(this).makeJSONObjectRequest("SaveLicenseTraders", Api.Method.POST, UrlGenerator.saveTradersUrl(), dataTobeSavedJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        } }


    public JSONObject dataTobeSavedJsonParams() throws JSONException {

        byte[] imageInByte = new byte[0];
        String image_str = "";


        JSONObject dataSet = new JSONObject();
       dataSet.put(AppConstant.KEY_SERVICE_ID, "SaveLicenseTraders");
       dataSet.put(AppConstant.MODE, "NEW");
        dataSet.put(AppConstant.TRADE_CODE,"21");
        dataSet.put(AppConstant.DATE,"04-11-2020");
        dataSet.put(AppConstant.LICENCE_TYPE,"1" );
        dataSet.put(AppConstant.TRADE_DESCRIPTION,"erere" );
        dataSet.put(AppConstant.APPLICANT_NAME, "asdsda");
        dataSet.put(AppConstant.GENDER,"M" );
        dataSet.put(AppConstant.AGE, "34");
        dataSet.put(AppConstant.FATHER_HUSBAND_NAME, "sdsd");
        dataSet.put(AppConstant.MOBILE, "2365478964");
        dataSet.put(AppConstant.E_MAIL, "asd@gmail.com");
        dataSet.put(AppConstant.ESTABLISHMENT_NAME, "test");
        dataSet.put(AppConstant.WARD_ID, "23");
        dataSet.put(AppConstant.STREET_ID, "34");
        dataSet.put(AppConstant.DOOR_NO, "87");
        dataSet.put(AppConstant.LICENCE_VALIDITY, "98");
        dataSet.put(AppConstant.LATITUDE, "10.3");
        dataSet.put(AppConstant.LONGITUDE, "20.6");
        dataSet.put(AppConstant.TRADE_IMAGE, "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GjBD6sDHcN79Na+cesHH/wBHwHzp6wcf/R8B86ZRl9H3cXQ+LHQ9j4v2f7dNPso0UJfeGNS9rZ930rcr2vavnHrBx/8AR8B86esHH/0fAfOg+lKyBQoFlAsBlNrcrW4Vi3EGQR7pcgNwu76IPMC3GvnXrBx/9HwHzp6wcf8A0fAfOg+kMsROYoC1wb5De4Fgb25Ej7a1ocBAkxlVOllRVGTRAgIGTTTRjVA9YOP/AKPgPnT1g4/+j4D50Tl9HaOIoUMYKEklTH0SSbkkW560aOI2vGDYAC8fUDcDhwB1r5x6wcf/AEfAfOnrBx/9HwHzpky+jGKE3vGuoIPxfEMbsOHWdTXjYeAoIzChjGoQxdEe4WtXzr1g4/8Ao+A+dPWDj/6PgPnTJlZ/S9gdzbqzdRHKq3XP2j6W4nE5d6IzlvaykcftrT/fMnzU7j50ezpdbZtWYor4vqGwdpQR4SJHmjVgDcFgCOkanFNhFxEk/wC1Lme3R3gCiwtw6/tr5Z++ZPmp3Hzp++ZPmp3Hzor1TpZqqqiufn9n0r0m2jBJhSscqO2ZdFYE8aqsczKHCmwcZW0Goveq/wDvmT5qdx86fvmT5qdx86LFjU6WzR8OJme/gscOMkjXKjWW5NrDiy5TxHKp/vGa98+t0PBeKCy9XVVZ/fMnzU7j50/fMnzU7j50bZ1ukmczHoshx0pFi+mUpwX2S2YjhzrKNrYjpfGe1cm6p1ixtppoOqqt++ZPmp3Hzp++ZPmp3HzobzR+Hos42nODcSa3U8F4quUdXLTt66hNjpXDBm0bLcBVA6N8tgBpa54c6rf75k+ancfOn75k+ancfOhvdJE5iPRYDOxQR36AJYCw4njrU0xsqsjB7GNcq6DQcuGvE8arn75k+ancfOn75k+ancfOjKddpZ4+ywYjEPK2dzdrAcANBwAA0ArHXD/fMnzU7j50/fMnzU7j50TH+hpojEeznUpSjnilKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUH/2Q==");

//        dataSet.put(AppConstant.TRADE_CODE, newTradeLicenceScreenBinding.tradersCode.getText().toString());
//        dataSet.put(AppConstant.DATE, newTradeLicenceScreenBinding.date.getText().toString());
//        dataSet.put(AppConstant.LICENCE_TYPE, newTradeLicenceScreenBinding.licenceType.getSelectedItemPosition());
//        dataSet.put(AppConstant.TRADE_DESCRIPTION, newTradeLicenceScreenBinding.tradeDescription.getText().toString());
//        dataSet.put(AppConstant.APPLICANT_NAME, newTradeLicenceScreenBinding.applicantName.getText().toString());
//        dataSet.put(AppConstant.GENDER, newTradeLicenceScreenBinding.gender.getSelectedItemPosition());
//        dataSet.put(AppConstant.AGE, newTradeLicenceScreenBinding.age.getText().toString());
//        dataSet.put(AppConstant.FATHER_HUSBAND_NAME, newTradeLicenceScreenBinding.fatherHusName.getText().toString());
//        dataSet.put(AppConstant.MOBILE, newTradeLicenceScreenBinding.mobileNo.getText().toString());
//        dataSet.put(AppConstant.E_MAIL, newTradeLicenceScreenBinding.emailId.getText().toString());
//        dataSet.put(AppConstant.ESTABLISHMENT_NAME, newTradeLicenceScreenBinding.establishName.getText().toString());
//        dataSet.put(AppConstant.WARD_ID, newTradeLicenceScreenBinding.wardNo.getSelectedItemPosition());
//        dataSet.put(AppConstant.STREET_ID, newTradeLicenceScreenBinding.streetName.getText().toString());
//        dataSet.put(AppConstant.DOOR_NO, newTradeLicenceScreenBinding.doorNo.getText().toString());
//        dataSet.put(AppConstant.LICENCE_VALIDITY, newTradeLicenceScreenBinding.licenceValidity.getSelectedItemPosition());

        String sql = "SELECT * FROM " + DBHelper.SAVE_TRADE_IMAGE + " WHERE screen_status = 'new' and tradecode ="+newTradeLicenceScreenBinding.tradersCode.getText();

        Cursor cursor = db.rawQuery(sql, null);
        Log.d("cursor_count", String.valueOf(cursor.getCount()));
        String image = null;
        String lat = null;
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    image = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TRADE_IMAGE));
                    lat = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LATITUDE));
                    Log.d("lat", "" + lat);
                    Log.d("image", "" + image);
                } while (cursor.moveToNext());
            }
        }
        Log.d("olat", "" + lat);
        Log.d("oimage", "" + image);
//        dataSet.put(AppConstant.LATITUDE, "");
//        dataSet.put(AppConstant.LONGITUDE, "");
//        dataSet.put(AppConstant.TRADE_IMAGE, "");

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
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status = responseObj.getString(AppConstant.KEY_STATUS);
            String response = responseObj.getString(AppConstant.KEY_RESPONSE);
            if ("SaveLicenseTraders".equals(urlType) && responseObj != null) {
                if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("OK")) {
                    JSONObject jsonObject = responseObj.getJSONObject(AppConstant.JSON_DATA);
//                    String Motivatorid = jsonObject.getString(AppConstant.KEY_REGISTER_MOTIVATOR_ID);
//                    Log.d("motivatorid",""+Motivatorid);
                    Utils.showAlert(this, "நீங்கள் வெற்றிகரமாக பதிவு செய்யப்பட்டுள்ளீர்கள்!");
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                    handler.postDelayed(runnable, 2000);

                } else if (status.equalsIgnoreCase("OK") && response.equalsIgnoreCase("FAIL")) {
                    Utils.showAlert(this, responseObj.getString("MESSAGE"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }


    public void validateUserDetails() {

        if (!newTradeLicenceScreenBinding.tradersCode.getText().toString().isEmpty()) {
            if (!newTradeLicenceScreenBinding.date.getText().toString().isEmpty()) {
                if (!newTradeLicenceScreenBinding.licenceType.getSelectedItem().toString().isEmpty()) {
                    if (!newTradeLicenceScreenBinding.tradeDescription.getText().toString().isEmpty()) {
                        if (!newTradeLicenceScreenBinding.applicantName.getText().toString().isEmpty()) {
                            if (!newTradeLicenceScreenBinding.fatherHusName.getText().toString().isEmpty()) {
                                if (!"Select Gender".equalsIgnoreCase(GenderList.get(newTradeLicenceScreenBinding.gender.getSelectedItemPosition()))) {
                                    if (!newTradeLicenceScreenBinding.age.getText().toString().isEmpty()) {
                                        if (!newTradeLicenceScreenBinding.mobileNo.getText().toString().isEmpty()) {
                                            if (Utils.isValidMobile(newTradeLicenceScreenBinding.mobileNo.getText().toString())) {
                                                if (!newTradeLicenceScreenBinding.emailId.getText().toString().isEmpty()) {
                                                    if (Utils.isEmailValid(newTradeLicenceScreenBinding.emailId.getText().toString())) {
                                                        if (!newTradeLicenceScreenBinding.establishName.getText().toString().isEmpty()) {
                                                            if (!newTradeLicenceScreenBinding.wardNo.getSelectedItem().toString().isEmpty()) {
                                                                if (!newTradeLicenceScreenBinding.streetName.getText().toString().isEmpty()) {
                                                                    if (!newTradeLicenceScreenBinding.doorNo.getText().toString().isEmpty()) {
                                                                        if (!newTradeLicenceScreenBinding.licenceValidity.getSelectedItem().toString().isEmpty()) {
                                                                        }
                                                                    } else {
                                                                        Utils.showAlert(this, "Enter License validity!");
                                                                    }
                                                                } else {
                                                                    Utils.showAlert(this, "Enter Door no!");
                                                                }
                                                            } else {
                                                                Utils.showAlert(this, "Enter Street name!");
                                                            }
                                                        } else {
                                                            Utils.showAlert(this, "Enter Ward no name!");
                                                        }
                                                    } else {
                                                        Utils.showAlert(this, "சரியான மின்னஞ்சல் முகவரியை உள்ளிடவும்!");
                                                    }
                                                } else {
                                                    Utils.showAlert(this, "உங்கள் மின்னஞ்சல் முகவரியை உள்ளிடவும்!");
                                                }
                                            } else {
                                                Utils.showAlert(this, "சரியான கைபேசி எண்ணை உள்ளிடவும்!");
                                            }
                                        } else {
                                            Utils.showAlert(this, "உங்கள் கைபேசி எண்ணை உள்ளிடவும்!");
                                        }
                                    } else {
                                        Utils.showAlert(this, "உங்கள் age தேர்ந்தெடுக்கவும்!");
                                    }
                                } else {
                                    Utils.showAlert(this, "உங்கள் பாலினத்தைத் தேர்ந்தெடுக்கவும்!");
                                }
                            } else {
                                Utils.showAlert(this, "உங்கள் தந்தை / கணவர் பெயரை உள்ளிடவும்!");
                            }
                        } else {
                            Utils.showAlert(this, "உங்கள் பெயரை உள்ளிடவும்!");
                        }
                    } else {
                        Utils.showAlert(this, "Enter Trade Desription!");
                    }
                } else {
                    Utils.showAlert(this, "Select License type!");
                }
            } else {
                Utils.showAlert(this, "தேதியைத் தேர்ந்தெடுக்கவும்!");
            }
        } else {
            Utils.showAlert(this, "Enter Traders code!");
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

    public void openCameraScreen() {
        Intent intent = new Intent(this, CameraScreen.class);
        intent.putExtra(AppConstant.TRADE_CODE,newTradeLicenceScreenBinding.tradersCode.getText().toString());
        intent.putExtra(AppConstant.KEY_SCREEN_STATUS,"new");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void viewImageScreen() {
        Intent intent = new Intent(this, FullImageActivity.class);
        intent.putExtra(AppConstant.TRADE_CODE,newTradeLicenceScreenBinding.tradersCode.getText().toString());
        intent.putExtra(AppConstant.KEY_SCREEN_STATUS,"new");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

}





