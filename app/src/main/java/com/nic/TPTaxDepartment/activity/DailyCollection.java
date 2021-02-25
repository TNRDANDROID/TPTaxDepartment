package com.nic.TPTaxDepartment.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.VolleyError;
import com.nic.TPTaxDepartment.Adapter.DailyCollectionAdapter;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
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
import java.util.HashMap;
import java.util.Map;


public class DailyCollection extends AppCompatActivity implements View.OnClickListener,Api.ServerResponseListener, Spinner.OnItemSelectedListener {

    private DailyCollectionBinding dailyCollectionBinding;
    private PrefManager prefManager;
    private static TextView date;
    ArrayList<TPtaxModel> collectionList;
    ArrayList<CommonModel> finYear;
    HashMap<String,String> spinnerMapFinYear;
    ArrayAdapter<String> finYearArray;
    String selectedFinId;
    String selectedFinName="";
    boolean flag;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyCollectionBinding = DataBindingUtil.setContentView(this, R.layout.daily_collection);
        dailyCollectionBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());

        prefManager = new PrefManager(this);
        date = dailyCollectionBinding.date;
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        dailyCollectionBinding.dailyCollectionRecycler.setLayoutManager(mLayoutManager);
        dailyCollectionBinding.dailyCollectionRecycler.setItemAnimator(new DefaultItemAnimator());
        dailyCollectionBinding.dailyCollectionRecycler.setHasFixedSize(true);
        dailyCollectionBinding.dailyCollectionRecycler.setNestedScrollingEnabled(false);
        dailyCollectionBinding.dailyCollectionRecycler.setFocusable(false);
        dailyCollectionBinding.finYear.setOnItemSelectedListener(this);

        Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        date.setText("Select Date");

        dailyCollectionBinding.dateLayout.setVisibility(View.GONE);
        dailyCollectionBinding.finYearLayout.setVisibility(View.GONE);
        dailyCollectionBinding.yearVice.setChecked(false);
        dailyCollectionBinding.daily.setChecked(false);
        dailyCollectionBinding.daily.setOnClickListener(this::onClick);
        dailyCollectionBinding.yearVice.setOnClickListener(this::onClick);

        try {
            LoadDailyCollectionList();
            LoadFinYearSpinner();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

     private void LoadDailyCollectionList() throws JSONException {
         collectionList = new ArrayList<TPtaxModel>();
        for (int i = 0; i < 6; i++) {
            if(i==0){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("1");
                Detail.setTaxTypeName("Property Tax");
                Detail.setTaxCollection("10050.00");
                collectionList.add(Detail);
            }
            else if(i==1){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("2");
                Detail.setTaxTypeName("Water Tax");
                Detail.setTaxCollection("");
                collectionList.add(Detail);
            }
            else if(i==2){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("3");
                Detail.setTaxTypeName("SWM Tax");
                Detail.setTaxCollection("70050.00");
                collectionList.add(Detail);
            }
            else if(i==3){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("4");
                Detail.setTaxTypeName("Professional Tax");
                Detail.setTaxCollection("780050.00");
                collectionList.add(Detail);
            }
            else if(i==4){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("5");
                Detail.setTaxTypeName("Non Tax");
                Detail.setTaxCollection("10050.00");
                collectionList.add(Detail);
            }
            /*else if(i==5){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("4");
                Detail.setTaxTypeName("Professional Tax");
                Detail.setTaxCollection("780050.00");
                collectionList.add(Detail);
            }
            else if(i==6){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("5");
                Detail.setTaxTypeName("Non Tax");
                Detail.setTaxCollection("10050.00");
                collectionList.add(Detail);
            }
*/
            else {
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTaxTypeId("6");
                Detail.setTaxTypeName("Trade License Tax");
                Detail.setTaxCollection("36050.00");
                collectionList.add(Detail);
            }
        }
       /* JSONArray jsonarray=new JSONArray(prefManager.getDailyCollectionList());
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
        }*/

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
      /*  DialogFragment newFragment = new datePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
*/        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
                /*if(Utils.isOnline()){
                getDailyCollection();
            }
            else {
                Utils.showAlert(getActivity(),"No Network Connection");
            }*/
            }

        };

        new DatePickerDialog(DailyCollection.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateLabel() {
        String myFormat = "dd-mm-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        dailyCollectionBinding.date.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(adapterView == dailyCollectionBinding.finYear){
            String finYear = adapterView.getSelectedItem().toString();
            selectedFinName=finYear;
            ((TextView) adapterView.getChildAt(0)).setTextColor(this.getResources().getColor(R.color.grey2));

            for(Map.Entry<String, String> entry: spinnerMapFinYear.entrySet()) {
                if(entry.getValue() == selectedFinName) {
                    selectedFinId=entry.getKey();
                    break;
                }
            }
/*
            if(selectedFinId!=null&&!selectedFinName.equals("Select Financial Year")){
                if(Utils.isOnline()) {
                    getYearCollection();
                }
                else {
                    Utils.showAlert(DailyCollection.this,"No Network Connection");
                }
            }
*/
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


      public static class datePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
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
            /*if(Utils.isOnline()){
                getDailyCollection();
            }
            else {
                Utils.showAlert(getActivity(),"No Network Connection");
            }*/

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
    public  void getYearCollection() {
        try {
            new ApiService(this).makeJSONObjectRequest("YearCollection", Api.Method.POST, UrlGenerator.TradersUrl(), dailyCollectionJsonParams(), "not cache", this);
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
        if(flag) {
            data.put(AppConstant.KEY_SERVICE_ID, "TaxYearCollection");
            data.put(AppConstant.COLLECTION_YEAR, selectedFinId);
            Log.d("YearCollectionRequest", "" + data);
        }
        else {
            data.put(AppConstant.KEY_SERVICE_ID, "TaxDailyCollection");
            data.put(AppConstant.COLLECTION_DATE, date.getText().toString());
            Log.d("DailyCollectionRequest", "" + data);
        }
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
            }

            if ("YearCollection".equals(urlType) && responseObj != null) {
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
        switch (v.getId()){
            case R.id.year_vice:
                dailyCollectionBinding.yearVice.setChecked(true);
                dailyCollectionBinding.daily.setChecked(false);
                dailyCollectionBinding.dateLayout.setVisibility(View.GONE);
                dailyCollectionBinding.finYearLayout.setVisibility(View.VISIBLE);
                flag=true;
                break;

            case R.id.daily:
                dailyCollectionBinding.yearVice.setChecked(false);
                dailyCollectionBinding.daily.setChecked(true);
                dailyCollectionBinding.dateLayout.setVisibility(View.VISIBLE);
                dailyCollectionBinding.finYearLayout.setVisibility(View.GONE);
                flag=false;
                break;
        }
    }

    public void LoadFinYearSpinner() {
        finYear = new ArrayList<CommonModel>();
        String select_query= "SELECT *FROM " + DBHelper.LICENCE_VALIDITY_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){

            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setFIN_YEAR_ID(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.FIN_YEAR_ID))));
                    commonModel.setFIN_YEAR(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FIN_YEAR))));
                    commonModel.setFROM_FIN_YEAR(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.FROM_FIN_YEAR))));
                    commonModel.setFROM_FIN_MON(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.FROM_FIN_MON))));
                    commonModel.setTO_FIN_YEAR(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TO_FIN_YEAR))));
                    commonModel.setTO_FIN_MON(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TO_FIN_MON))));
                    finYear.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        Collections.sort(finYear, (lhs, rhs) -> lhs.getFIN_YEAR().compareTo(rhs.getFIN_YEAR()));

        if(finYear != null && finYear.size() >0) {

            spinnerMapFinYear = new HashMap<String, String>();
            spinnerMapFinYear.put(null, null);
            final String[] items = new String[finYear.size() + 1];
            items[0] = "Select Financial Year";
            for (int i = 0; i < finYear.size(); i++) {
                spinnerMapFinYear.put(finYear.get(i).getFIN_YEAR_ID(), finYear.get(i).getFIN_YEAR());
                String Class = finYear.get(i).getFIN_YEAR();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    finYearArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    finYearArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dailyCollectionBinding.finYear.setAdapter(finYearArray);
                    dailyCollectionBinding.finYear.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
                    selectedFinId="0";
                    selectedFinName="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }

}