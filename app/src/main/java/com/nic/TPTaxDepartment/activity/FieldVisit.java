package com.nic.TPTaxDepartment.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.TPTaxDepartment.Adapter.CommonAdapter;
import com.nic.TPTaxDepartment.Adapter.FieldVisitHistoryAdapter;
import com.nic.TPTaxDepartment.Adapter.FieldVisitRquestListAdapter;
import com.nic.TPTaxDepartment.Adapter.FieldVistTaxSpinnerAdapter;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.Interface.DateInterface;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.Support.MyCustomTextView;
import com.nic.TPTaxDepartment.Support.MyLocationListener;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.databinding.FieldVisitBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.SpinnerAdapter;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.CameraUtils;
import com.nic.TPTaxDepartment.utils.FontCache;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.view.View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS;
import static com.nic.TPTaxDepartment.activity.Dashboard.db;

public class FieldVisit extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener,AdapterView.OnItemClickListener, StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener, AdapterView.OnItemSelectedListener , DateInterface {

    private FieldVisitBinding fieldVisitBinding;
    private ArrayList<String> Current_Status = new ArrayList<>();
    static JSONObject dataset;
    Double offlatTextValue, offlanTextValue;
    private List<View> viewArrayList = new ArrayList<>();
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ImageView imageView, image_view_preview;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;
    private static String imageStoragePath;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private ScrollView scrollView;
    ArrayList<CommonModel> taxType ;
    HashMap<String,String> spinnerMapTaxType;
    String selectedTaxTypeId;
    String selectedTaxTypeName="";
    String selectedTaxTypedInRecyler;

    //FieldVisitStatus;
    ArrayList<CommonModel> fieldVisitStatus ;
    HashMap<String,String> spinnerMapFieldVisitType;
    String selectedFieldVisitStatusId;
    String selectedFieldVisitStatusName="";

    //ServiceListFieldTypes
    //FieldVisitStatus;
    ArrayList<CommonModel> serviceFieldVisitTypes ;
    HashMap<String,String> spinnerMapServiceFieldVisitTypes;
    String selectedServiceFieldVisitTypesId;
    String selectedServiceFieldVisitTypesName="";

    //SearchRequestIDLIST
    ArrayList<CommonModel> searchRequestList;
    ArrayList<CommonModel> fieldVisits;
    private PrefManager prefManager;
    FieldVisitRquestListAdapter fieldVisitRquestListAdapter;
    String request_id="",data_ref_id="";
    Dialog dialog;
    Context context;
    ArrayAdapter<String> serviceFieldVisitAdapter;
    ArrayAdapter<String> taxTypeAdapter;
    ArrayAdapter<String> fieldCurrentStatusArray;
    ArrayList<TPtaxModel> historyList;
    String FieldVisitImageString="";
    boolean flag=false;
    boolean backPressflag=false;
    int selectedPosition;
    final Calendar myCalendar = Calendar.getInstance();
    private androidx.appcompat.app.AlertDialog alert;
    Spinner tax,serviceType;
    TextView date;
    String fromDate,toDate,TaxTypeIdHistory,serviceListFieldTaxTypeIdHistory;
    String[] taxItems;
    public com.nic.TPTaxDepartment.dataBase.dbData dbData = new dbData(this);
    boolean spinner_text_color=false;
    boolean click_history_icon=false;
    int  tax_position,previous_status_position;
    String previous_remarks_text,previous_applicant_name,previous_request_id;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fieldVisitBinding = DataBindingUtil.setContentView(this, R.layout.field_visit);
        fieldVisitBinding.setActivity(this);
        context=this;
        prefManager = new PrefManager(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        this.getWindow().setStatusBarColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        dataset = new JSONObject();
        //loadCurrentStatus();
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fieldVisitBinding.historyLayout.setVisibility(View.GONE);
        fieldVisitBinding.activityMain.setVisibility(View.VISIBLE);
        fieldVisitBinding.stickyList.setOnItemClickListener(this);
        fieldVisitBinding.stickyList.setOnHeaderClickListener(this);
        fieldVisitBinding.stickyList.setOnStickyHeaderChangedListener(this);
        fieldVisitBinding.stickyList.setOnStickyHeaderOffsetChangedListener(this);
        fieldVisitBinding.stickyList.addHeaderView(this.getLayoutInflater().inflate(R.layout.list_header, null));
//        fieldVisitBinding.stickyList.setEmptyView(getParent().findViewById(android.R.id.empty));
        fieldVisitBinding.stickyList.setAreHeadersSticky(true);
        fieldVisitBinding.stickyList.setDividerHeight(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fieldVisitBinding.stickyList.setImportantForAutofill(IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
        getTaxTypeFieldVisitList();
        getFieldVisitStatusList();
//        getFieldVisitImage();
        //getFieldVisitHistory();

        Utils.setLanguage(fieldVisitBinding.remarksText,"en","USA");

        fieldVisitBinding.serviceFiledType.setEnabled(false);
       /* fieldVisitBinding.taxType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String TaxTypeName = parent.getSelectedItem().toString();
                if(spinner_text_color) {
                    ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.full_transparent));
                }
                else {
                    ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));
                    spinner_text_color=false;
                }

//                String TaxTypeId = spinnerMapTaxType.get(parent.getSelectedItemPosition());
                String TaxTypeId = "";
                // iterate each entry of hashmap
                for(Map.Entry<String, String> entry: spinnerMapTaxType.entrySet()) {
                    // if give value is equal to value from entry
                    // print the corresponding key
                    if(entry.getValue() == TaxTypeName) {
                        TaxTypeId=entry.getKey();
                        break;
                    }
                }
                selectedTaxTypeId = TaxTypeId;
                selectedTaxTypeName = TaxTypeName;
                if(selectedTaxTypeId!=null&&position>0) {
                    getServiceListFieldVisitTypes(1,selectedTaxTypeId);
                }
                else {
                    if(spinner_text_color) {

                    }
                    else {
                        fieldVisitBinding.serviceFiledType.setAdapter(null);
                        fieldVisitBinding.requestIdTextField.setText("");
                        fieldVisitBinding.applicantName.setText("");
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });*/

        fieldVisitBinding.serviceFiledType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String serviceListFieldDesc = parent.getSelectedItem().toString();
                String serviceListFieldTaxTypeId = "";
                ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));
                for(Map.Entry<String, String> entry: spinnerMapServiceFieldVisitTypes.entrySet()) {
                    // if give value is equal to value from entry
                    // print the corresponding key
                    if(entry.getValue() == serviceListFieldDesc) {
                        serviceListFieldTaxTypeId=entry.getKey();
                        break;
                    }
                }


                selectedServiceFieldVisitTypesId=serviceListFieldTaxTypeId;
                selectedServiceFieldVisitTypesName=serviceListFieldDesc;
                if(selectedServiceFieldVisitTypesId!=null&&position>0){
                    if (!flag) {
                        if(Utils.isOnline()) {
                            getServiceRequestList();
                        }
                        else {
                            Utils.showAlert(FieldVisit.this,getApplicationContext().getResources().getString(R.string.no_internet_connection));
                        }
                    }else {
                        flag=false;
                    }

                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        fieldVisitBinding.currentStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String Name = parent.getSelectedItem().toString();
                String Id = "";
                ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

                for(Map.Entry<String, String> entry: spinnerMapFieldVisitType.entrySet()) {
                    // if give value is equal to value from entry
                    // print the corresponding key
                    if(entry.getValue() == Name) {
                        Id=entry.getKey();
                        break;
                    }
                }
                selectedFieldVisitStatusId=Id;
                selectedFieldVisitStatusName=Name;

            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        fieldVisitBinding.fieldVisitLists.setLayoutManager(layoutManager);
        fieldVisitBinding.fieldVisitLists.setItemAnimator(new DefaultItemAnimator());
        fieldVisitBinding.fieldVisitLists.setHasFixedSize(true);
        fieldVisitBinding.fieldVisitLists.setNestedScrollingEnabled(false);
        fieldVisitBinding.fieldVisitLists.setFocusable(false);

        fieldVisitBinding.fieldVisitLists.setVisibility(View.GONE);
        flag=getIntent().getBooleanExtra("flag",false);
        if(flag){
            selectedPosition=getIntent().getIntExtra("position",0);
            fieldVisits = (ArrayList<CommonModel>)getIntent().getSerializableExtra("fieldsList");
            LoadPendingFieldDetails();

        }else {
            fieldVisitBinding.taxType.setEnabled(true);
        }

    }

    private void LoadPendingFieldDetails() {
        fieldVisitBinding.taxType.setEnabled(false);
        fieldVisitBinding.btnRegister.setText(context.getResources().getString(R.string.update));
        fieldVisitBinding.history.setVisibility(View.GONE);
        try {
            int taxTypePosition = taxTypeAdapter.getPosition(spinnerMapTaxType.get(fieldVisits.get(selectedPosition).getTaxtypeid()));
            if(taxTypePosition >= 0){
                fieldVisitBinding.taxType.setSelection(taxTypePosition);
                taxSpinnerClickedMethod(1,fieldVisits.get(selectedPosition).getTaxtypeid(),spinnerMapTaxType.get(fieldVisits.get(selectedPosition).getTaxtypeid()));
            }else {
                fieldVisitBinding.taxType.setAdapter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int currentStatusPosition = fieldCurrentStatusArray.getPosition(spinnerMapFieldVisitType.get(fieldVisits.get(selectedPosition).getFIELD_VISIT_STATUS_ID()));
            if(currentStatusPosition >= 0){
                fieldVisitBinding.currentStatus.setSelection(currentStatusPosition);
            }else {
                fieldVisitBinding.currentStatus.setAdapter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fieldVisitBinding.requestIdTextField.setText(fieldVisits.get(selectedPosition).getRequest_id());
        request_id=fieldVisits.get(selectedPosition).getRequest_id();
        data_ref_id=fieldVisits.get(selectedPosition).getData_ref_id();
        fieldVisitBinding.applicantName.setText(fieldVisits.get(selectedPosition).getOwnername());
        fieldVisitBinding.remarksText.setText(fieldVisits.get(selectedPosition).getRemark());

    }

    public void taxSpinnerClickedMethod(int pos,String tax_type_id,String tax_Type_name){
        hideSpinnerDropDown(fieldVisitBinding.taxType);

        selectedTaxTypedInRecyler = tax_type_id;
        selectedTaxTypeName = tax_Type_name;
        if(selectedTaxTypedInRecyler!=null&&pos>0) {

            getServiceListFieldVisitTypes(1,selectedTaxTypedInRecyler);
        }
        else {
                fieldVisitBinding.taxType.setSelection(0);
                fieldVisitBinding.serviceFiledType.setAdapter(null);
                fieldVisitBinding.requestIdTextField.setText("");
                fieldVisitBinding.applicantName.setText("");
                fieldVisitBinding.remarksText.setText("");
                fieldVisitBinding.currentStatus.setSelection(0);

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
    }
    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
    }
    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
//        Toast.makeText(getContext(), countryNames[position], Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    public void loadCurrentStatus() {
        Current_Status.clear();
        Current_Status.add("Select Current Status");
        Current_Status.add("Statisfied");
        Current_Status.add("Unstatisfied");
        Current_Status.add("Need Improvement");
        ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Current_Status);
        RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldVisitBinding.currentStatus.setAdapter(RuralUrbanArray);
        fieldVisitBinding.currentStatus.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);

    }
    public void getFieldVisitImage(String service_id,String taxtypeid,String data_ref_id) {
        if(Utils.isOnline()) {
            try {
                new ApiService(this).makeJSONObjectRequest("FieldVisitedImage", Api.Method.POST, UrlGenerator.TradersUrl(),
                        FieldVisitImageJsonParams(service_id, taxtypeid, data_ref_id), "not cache", this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Utils.showAlert(FieldVisit.this,getApplicationContext().getResources().getString(R.string.no_internet_connection));
        }

    }
    public void getFieldVisitHistory() {
        if(Utils.isOnline()) {
            try {
                new ApiService(this).makeJSONObjectRequest("TradersFieldVisitHistoryService", Api.Method.POST, UrlGenerator.TradersUrl(),
                        fieldVisitJsonParams(), "not cache", this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Utils.showAlert(FieldVisit.this,getApplicationContext().getResources().getString(R.string.no_internet_connection));
        }

    }
    public JSONObject fieldVisitJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), fieldJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("fieldVisitJsonParams", "" + dataSet);
        return dataSet;
    }

    public JSONObject fieldJsonParams() throws JSONException {

        JSONObject data = new JSONObject();
            data.put(AppConstant.KEY_SERVICE_ID, "FieldVisitServiceHistory");
            data.put("from_date", updateLabel(fromDate));
            data.put("to_date", updateLabel(toDate));
            data.put(AppConstant.TAX_TYPE_ID, TaxTypeIdHistory);
            data.put("serviceid", serviceListFieldTaxTypeIdHistory);
            Log.d("fieldVisitReqParams", "" + data);
        return data;
    }


    public JSONObject FieldVisitImageJsonParams(String service_id,String taxtypeid,String data_ref_id) throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector),
                FieldVisitImageParams(service_id,taxtypeid,data_ref_id).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("FieldVisitedImage", "" + dataSet);
        return dataSet;
    }

    public JSONObject FieldVisitImageParams(String service_id,String taxtypeid,String data_ref_id) throws JSONException{
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "FieldVisitImage");
        dataSet.put("serviceid", service_id);
        dataSet.put("taxtypeid", taxtypeid);
        dataSet.put("data_ref_id", data_ref_id);
        Log.d("FieldVisitedJsonImage", "" + dataSet);
        return dataSet;
    }
    public void openCameraScreen() {
        Intent intent = new Intent(this, CameraScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void viewImageScreen(String fieldVisitImg) {
        if (fieldVisitImg != null ) {
            byte [] encodeByte = Base64.decode(fieldVisitImg,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Bitmap converetdImage = Utils.getResizedBitmap(bitmap, 500);
            fieldVisitImg=Utils.bitmapToString(converetdImage);
            Intent intent = new Intent(this, FullImageActivity.class);
            intent.putExtra(AppConstant.TRADE_CODE, "");
            intent.putExtra(AppConstant.MOBILE, "");
            intent.putExtra(AppConstant.KEY_SCREEN_STATUS, "");
            intent.putExtra(AppConstant.TRADE_IMAGE, fieldVisitImg);
            intent.putExtra("key", "FieldVisitedImage");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            Utils.showAlert(FieldVisit.this, context.getResources().getString(R.string.no_image_Found));
        }

    }


    public void viewImage() {
        if(fieldVisitBinding.requestIdTextField.getText()!= null && !fieldVisitBinding.requestIdTextField.getText().equals("")){

                if (getSaveTradeImageTable() > 0) {
                    Intent intent = new Intent(this, FullImageActivity.class);
                    intent.putExtra("request_id", fieldVisitBinding.requestIdTextField.getText().toString());
                    intent.putExtra("data_ref_id", data_ref_id);
                    intent.putExtra("key", "FieldVisit");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
                    Utils.showAlert(FieldVisit.this, context.getResources().getString(R.string.no_image_Saved_in_Local));
                }

        }else {
            Utils.showAlert(FieldVisit.this, context.getResources().getString(R.string.select_TaxType_To_Get_Request_Id));
        }

    }

    public int getSaveTradeImageTable(){

        Cursor cursor = null;
        String sql = "SELECT * FROM " + DBHelper.CAPTURED_PHOTO
                + " WHERE request_id ="+fieldVisitBinding.requestIdTextField.getText().toString()
                + " AND data_ref_id ="+data_ref_id;
        cursor=Dashboard.db.rawQuery(sql,null,null);
        if (cursor.getCount() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public void image() {
        if(!fieldVisitBinding.requestIdTextField.getText().toString().isEmpty()){

            imageWithDescription(fieldVisitBinding.takePhotoTv, "mobile");
        }
        else {
            Utils.showAlert(FieldVisit.this,context.getResources().getString(R.string.select_Tax_Type_first));
        }
    }
    public void imageWithDescription(TextView action_tv, final String type) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String date = sdf.format(new Date());
        String tax_type_id = fieldVisitBinding.taxType.getSelectedItem().toString();
        String assessment_id = fieldVisitBinding.assessmentId.getText().toString();
        String applicant_name = fieldVisitBinding.applicantName.getText().toString();
        String build_type = fieldVisitBinding.buildType.getText().toString();
        String current_status = fieldVisitBinding.currentStatus.getSelectedItem().toString();
        String remarks = fieldVisitBinding.remarks.getText().toString();

        dialog = new Dialog(this, R.style.AppTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_photo);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        final LinearLayout mobileNumberLayout = (LinearLayout) dialog.findViewById(R.id.mobile_number_layout);
        MyCustomTextView cancel = (MyCustomTextView) dialog.findViewById(R.id.tv_save_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button done = (Button) dialog.findViewById(R.id.btn_save_inspection);
        ImageView back = (ImageView) dialog.findViewById(R.id.back_arrow);
        ImageView home = (ImageView) dialog.findViewById(R.id.home);

        done.setGravity(Gravity.CENTER);
        done.setVisibility(View.VISIBLE);
        done.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.HEAVY));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboard();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                JSONArray imageJson = new JSONArray();
                int childCount = mobileNumberLayout.getChildCount();
                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        JSONObject imageArray = new JSONObject();

                        View vv = mobileNumberLayout.getChildAt(i);
                        EditText myEditTextView = (EditText) vv.findViewById(R.id.description);
                        Utils.setLanguage(myEditTextView,"en","USA");
                        ImageView imageView = (ImageView) vv.findViewById(R.id.image_view);
                        byte[] imageInByte = new byte[0];
                        String image_str = "";
                        try {
                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                            imageInByte = baos.toByteArray();
                            image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);
                            // String string = new String(imageInByte);
                            //Log.d("imageInByte_string",string);
                            Log.d("image_str", image_str);
                        } catch (Exception e) {
                            Utils.showAlert(FieldVisit.this, context.getResources().getString(R.string.atleast_Capture_one_Photo));
                            break;
                            //e.printStackTrace();
                        }

                        String description = myEditTextView.getText().toString();

                        if (MyLocationListener.latitude > 0) {
                            offlatTextValue = MyLocationListener.latitude;
                            offlanTextValue = MyLocationListener.longitude;
                        }


                        ContentValues imageValue = new ContentValues();

                        byte [] encodeByte = Base64.decode(image_str.trim(),Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        Bitmap converetdImage = Utils.getResizedBitmap(bitmap, 500);
                        image_str=Utils.bitmapToString(converetdImage);

                        imageValue.put("request_id", request_id);
                        imageValue.put("data_ref_id", data_ref_id);
                        imageValue.put(AppConstant.LATITUDE, offlatTextValue);
                        imageValue.put(AppConstant.LONGITUDE, offlanTextValue);
                        imageValue.put(AppConstant.FIELD_IMAGE, image_str);
                        imageValue.put(AppConstant.DESCRIPTION, myEditTextView.getText().toString());
                        imageValue.put("pending_flag", 1);


                           if(iscaptureImgExist(request_id,data_ref_id)) {
                               long rowUpdated1 = LoginScreen.db.update(DBHelper.CAPTURED_PHOTO, imageValue, "request_id  = ? "+"and data_ref_id  = ? ", new String[]{request_id,data_ref_id});
                               if (rowUpdated1 != -1) {
                                    Toast.makeText(FieldVisit.this, context.getResources().getString(R.string.image_Updated), Toast.LENGTH_SHORT).show();
//                                   Utils.showAlert(FieldVisit.this, " Capture-Photo updated");

                               }
                           }
                           else {
                               try{
                                   long rowInserted = LoginScreen.db.insert(DBHelper.CAPTURED_PHOTO,null,imageValue);
                                   //Toast.makeText(FieldVisit.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                   if (rowInserted != -1) {
                                       Toast.makeText(FieldVisit.this, context.getResources().getString(R.string.image_added), Toast.LENGTH_SHORT).show();
//                                    Utils.showAlert(FieldVisit.this, " Capture-Photo added in Local");

                                   }
                                   else {
                                       Toast.makeText(FieldVisit.this, context.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                                   }
                               }catch (Exception e){
                                   e.printStackTrace();
                               }

                            }
                           /*
                        if (Utils.isOnline())  {
                            try {
                                //imageArray.put(i);
                                imageArray.put("lat",offlatTextValue);
                                imageArray.put("long",offlanTextValue);
                                imageArray.put("photo",image_str.trim());
                                //imageArray.put(description);
                                imageJson.put(imageArray);

                                try {
                                    dataset.put("image_details", imageJson);
                                    Log.d("post_dataset_inspection", dataset.toString());
                                    String authKey = dataset.toString();
                                    int maxLogSize = 1000;
                                    for(int j = 0; j <= authKey.length() / maxLogSize; j++) {
                                        int start = j * maxLogSize;
                                        int end = (j+1) * maxLogSize;
                                        end = end > authKey.length() ? authKey.length() : end;
                                        Log.v("to_send", authKey.substring(start, end));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            catch (Exception e){

                            }

                        }
*/


                    }
                }
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                dialog.dismiss();
      //         focusOnView(scrollView, action_tv);


            }
        });
        final String values = action_tv.getText().toString().replace("NA", "");
        Button btnAddMobile = (Button) dialog.findViewById(R.id.btn_add);
        btnAddMobile.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.MEDIUM));


        if(fieldVisitBinding.currentStatus.getSelectedItem().equals("Need improvement")){
            btnAddMobile.setVisibility(View.GONE);
        }else {
            btnAddMobile.setVisibility(View.GONE);
        }
        viewArrayList.clear();
        btnAddMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.getDrawable() != null && viewArrayList.size()>0) {
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    updateView(FieldVisit.this, mobileNumberLayout, "", type);
                } else {
                    Utils.showAlert(FieldVisit.this, context.getResources().getString(R.string.first_Capture_Image_then_add_another_Image));
                }
            }
        });
        if (!values.isEmpty()) {
//            String level = "";
//            if(prefManager.getLevels().equalsIgnoreCase("D")) {
//                level = "D";
//            }else if(prefManager.getLevels().equalsIgnoreCase("S")) {
//                level = "S";
//            }
//         //   Cursor imageList = getRawEvents("SELECT * FROM " + DBHelper.LOCAL_IMAGE +" WHERE level='"+level+"' and work_id="+work_id, null);
//
//            if (imageList.getCount() > 0) {
//                imagelistvalues.clear();
//                int i =0;
//
//                if (imageList.moveToFirst()) {
//                    do {
//                        String work_id = imageList.getString(imageList.getColumnIndexOrThrow(AppConstant.WORK_ID));
//                        String latitude = imageList.getString(imageList.getColumnIndexOrThrow(AppConstant.LATITUDE));
//                        String longitude = imageList.getString(imageList.getColumnIndexOrThrow(AppConstant.LONGITUDE));
//                        String description = imageList.getString(imageList.getColumnIndexOrThrow(AppConstant.DESCRIPTION));
//
//                        byte[] photo = imageList.getBlob(imageList.getColumnIndexOrThrow(AppConstant.IMAGE));
//                        byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//
//                        BlockListValue imageValue = new BlockListValue();
//
//                        imageValue.setWorkID(work_id);
//                        imageValue.setLatitude(latitude);
//                        imageValue.setLongitude(longitude);
//                        imageValue.setDescription(description);
//                        imageValue.setImage(decodedByte);
//
//                        imagelistvalues.add(imageValue);
//                        updateView(this, mobileNumberLayout,values, "localImage");
            //i++;
//                    } while (imageList.moveToNext());
//                }
//                try {
//                    db.delete(DBHelper.LOCAL_IMAGE, null, null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
        }


            if (values.contains(",")) {
                String[] mobileOrEmail = values.split(",");
                for (int i = 0; i < mobileOrEmail.length; i++) {
                    if (viewArrayList.size() < 5) {
                        updateView(this, mobileNumberLayout, mobileOrEmail[i], type);
                    }
                }
            }
            else {
                if (viewArrayList.size() < 5) {
                    updateView(this, mobileNumberLayout, values, type);
                }
            }
        // }
//        else {
//            updateView(this, mobileNumberLayout, values, type);
//        }
    }

    public void validation() {
        if(selectedTaxTypedInRecyler!=null&&!selectedTaxTypedInRecyler.toString().equals("")){
            if(!fieldVisitBinding.assessmentId.getText().equals(null)){
                if(fieldVisitBinding.currentStatus.getSelectedItem()!=null&&!fieldVisitBinding.currentStatus.getSelectedItem().equals(context.getResources().getString(R.string.select_Status))){
                    if(!fieldVisitBinding.remarksText.getText().toString().isEmpty()){
                        if (getSaveTradeImageTable() > 0) {
//                            submit();
                            showConfirmationAlert(this,context.getResources().getString(R.string.would_you_like_to_change_image));
                        }
                        else {
                            Utils.showAlert(FieldVisit.this,context.getResources().getString(R.string.take_Photo));
                        }
                    }
                    else {
                        Utils.showAlert(FieldVisit.this,context.getResources().getString(R.string.enter_Remarks));
                    }

                }
                else {
                    Utils.showAlert(FieldVisit.this,context.getResources().getString(R.string.select_Status));
                }
            }
            else {
                Utils.showAlert(FieldVisit.this,context.getResources().getString(R.string.enter_Assessment_ID));
            }
        }
        else {
            Utils.showAlert(FieldVisit.this,context.getResources().getString(R.string.enter_Tax_Type_Id));
        }
    }
    public void showConfirmationAlert(Activity activity, String msg) {

        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
            dialogButton.setText(context.getResources().getString(R.string.yes));
            btn_cancel.setText(context.getResources().getString(R.string.save_submit));
            btn_cancel.setVisibility(View.VISIBLE);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image();
                    dialog.dismiss();

                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    submit();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void  submit() {
        //String tax_type_id = selectedTaxTypeId;
        String tax_type_id = selectedTaxTypedInRecyler;
        //String assessment_id = fieldVisitBinding.assessmentId.getText().toString();
        String applicant_name = fieldVisitBinding.applicantName.getText().toString();
        //String build_type = fieldVisitBinding.buildType.getText().toString();
        String current_status = selectedFieldVisitStatusId;
        String remarks = fieldVisitBinding.remarksText.getText().toString();

        if(!Utils.isOnline()) {
            ContentValues fieldValue = new ContentValues();
            fieldValue.put("taxtypeid", tax_type_id);
            fieldValue.put("tax_type_name", selectedTaxTypeName);
            fieldValue.put("serviceid", selectedServiceFieldVisitTypesId);
            fieldValue.put("request_id", request_id);
            fieldValue.put("data_ref_id", data_ref_id);
            fieldValue.put("field_visit_status", selectedFieldVisitStatusId);
            fieldValue.put("field_visit_status_name", selectedFieldVisitStatusName);
            fieldValue.put("remark", remarks);
            fieldValue.put("owner_name", fieldVisitBinding.applicantName.getText().toString());
            //fieldValue.put("photo", remarks);
            //fieldValue.put("lat", remarks);
            //fieldValue.put("long", remarks);


            //  long rowUpdated = LoginScreen.db.update(DBHelper.SAVE_FIELD_VISIT, fieldValue, "taxtypeid//  = ? AND delete_flag = ?", new String[]{tax_type_id,"0" });
            //long rowUpdated = db.insert(DBHelper.SAVE_FIELD_VISIT, null, fieldValue);

            if (isrequestIDexixt(request_id,data_ref_id)){
                long rowUpdated1 = Dashboard.db.update(DBHelper.SAVE_FIELD_VISIT, fieldValue, "request_id  = ? "+" and data_ref_id  = ? ", new String[]{request_id,data_ref_id});
            if (rowUpdated1 != -1) {
//                 Toast.makeText(FieldVisit.this, "Field-Visit updated", Toast.LENGTH_SHORT).show();
//                Utils.showAlert(FieldVisit.this, " Field-Visit updated");
                Utils.showToast(this,context.getResources().getString(R.string.field_Visit_updated));
                backPressflag=true;
                onBackPressed();
                //Dashboard.syncvisiblity();
                //finish();
                //dashboard();
            }
        }
            else {
                long rowInserted = Dashboard.db.insert(DBHelper.SAVE_FIELD_VISIT,null,fieldValue);
                //Toast.makeText(FieldVisit.this, "Something wrong", Toast.LENGTH_SHORT).show();
                if (rowInserted != -1) {
//                     Toast.makeText(FieldVisit.this, "Field-Visit added", Toast.LENGTH_SHORT).show();
//                    Utils.showAlert(FieldVisit.this, " Field-Visit added");
                    Utils.showToast(this,context.getResources().getString(R.string.field_Visit_added));
                    backPressflag=true;
                    onBackPressed();
                    //Dashboard.syncvisiblity();
                    //finish();
                    //dashboard();
                } else {
                    Toast.makeText(FieldVisit.this, context.getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                }
            }
        }

        else {
            ArrayList<TPtaxModel> commonModels = new ArrayList<>();
            JSONArray jsonArray = new JSONArray();
            dbData.open();

//        commonModels.addAll(dbData.selectPendingImage(request_id));
            commonModels = new ArrayList<>(dbData.selectFieldVisitImage(request_id,data_ref_id));

            for (int i = 0; i < commonModels.size(); i++) {
                JSONObject imageArray = new JSONObject();
                try {
                    if (request_id.equals(commonModels.get(i).getRequest_id()) && data_ref_id.equals(commonModels.get(i).getData_ref_id())) {
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
                dataset.put(AppConstant.KEY_SERVICE_ID,"FieldVisitStatusUpdate");
                dataset.put("taxtypeid", tax_type_id);
                dataset.put("serviceid", selectedServiceFieldVisitTypesId);
                dataset.put("request_id", request_id);
                dataset.put("data_ref_id", data_ref_id);
                dataset.put("field_visit_status", selectedFieldVisitStatusId);
                dataset.put("remark", remarks);

                sync_data();
            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }



     /*   String authKey = dataset.toString();
        int maxLogSize = 2000;
        for(int i = 0; i <= authKey.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > authKey.length() ? authKey.length() : end;
            Log.v("to_send+_plain", authKey.substring(start, end));
        }*/

      //  String authKey1 = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());

//        for(int i = 0; i <= authKey1.length() / maxLogSize; i++) {
//            int start = i * maxLogSize;
//            int end = (i+1) * maxLogSize;
//            end = end > authKey.length() ? authKey1.length() : end;
//            Log.v("to_send_encryt", authKey1.substring(start, end));
//        }
        //sync_data();

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

    private final void focusOnView(final ScrollView your_scrollview, TextView your_EditBox) {
        your_scrollview.post(new Runnable() {
            @Override
            public void run() {
                your_scrollview.fullScroll(View.FOCUS_DOWN);
                //your_scrollview.scrollTo(0, your_EditBox.getY());
            }
        });
    }
    //Method for update single view based on email or mobile type
    public View updateView(final Activity activity, final LinearLayout emailOrMobileLayout, final String values, final String type) {
        final View hiddenInfo = activity.getLayoutInflater().inflate(R.layout.image_with_description, emailOrMobileLayout, false);
        final ImageView imageView_close = (ImageView) hiddenInfo.findViewById(R.id.imageView_close);
        imageView = (ImageView) hiddenInfo.findViewById(R.id.image_view);
        image_view_preview = (ImageView) hiddenInfo.findViewById(R.id.image_view_preview);
        final EditText myEditTextView = (EditText) hiddenInfo.findViewById(R.id.description);


        Typeface typeFace = Typeface.createFromAsset(activity.getAssets(), "fonts/Avenir-Roman.ttf");

        myEditTextView.setSelection(0);
        if ("Mobile".equalsIgnoreCase(type)) {

            if (!values.isEmpty()) {

                if (values.length() > 0 && values.contains("-")) {
                    String[] mobile = values.split("-");
                    if (mobile.length == 2) {
                        myEditTextView.setText(values.split("-")[1]);
                        int countryCode = Integer.parseInt(values.split("-")[0]);

                    }
                } /*else {
                    myEditTextView.setText(values);
                }*/
            }
        }
//        if ("localImage".equalsIgnoreCase(type)) {
//            int i = Integer.parseInt(values);
//            if (!values.isEmpty()) {
//                myEditTextView.setText(imagelistvalues.get(i).getDescription());
//
//                image_view_preview.setVisibility(View.GONE);
//                imageView.setVisibility(View.VISIBLE);
//                imageView.setImageBitmap(imagelistvalues.get(i).getImage());
//
//            }
//        }
        imageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    imageView.setVisibility(View.VISIBLE);
                    if (viewArrayList.size() != 1) {
                        ((LinearLayout) hiddenInfo.getParent()).removeView(hiddenInfo);
                        viewArrayList.remove(hiddenInfo);
                    }

                } catch (IndexOutOfBoundsException a) {
                    a.printStackTrace();
                }
            }
        });
        image_view_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLong();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLong();
            }
        });
        emailOrMobileLayout.addView(hiddenInfo);

        View vv = emailOrMobileLayout.getChildAt(viewArrayList.size());
        EditText myEditTextView1 = (EditText) vv.findViewById(R.id.description);
        //myEditTextView1.setSelection(myEditTextView1.length());
        myEditTextView1.requestFocus();
        viewArrayList.add(hiddenInfo);
        return hiddenInfo;
    }
    private void getLatLong() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(FieldVisit.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(FieldVisit.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FieldVisit.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(FieldVisit.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FieldVisit.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FieldVisit.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CameraUtils.checkPermissions(FieldVisit.this)) {
                        captureImage();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
//                            checkPermissionForCamera();
                } else {
                    captureImage();
                }
            } else {
                Utils.showAlert(FieldVisit.this, context.getResources().getString(R.string.satellite_communication_msg));
            }
        } else {
            Utils.showAlert(FieldVisit.this, context.getResources().getString(R.string.gPS_is_not_turned_on));
        }
    }
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(this, file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        if (MyLocationListener.latitude > 0) {
            offlatTextValue = MyLocationListener.latitude;
            offlanTextValue = MyLocationListener.longitude;
        }
    }


    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            } else {
//                                captureVideo();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(context.getResources().getString(R.string.permissions_required))
                .setMessage(context.getResources().getString(R.string.camera_needs_few_permissions))
                .setPositiveButton(context.getResources().getString(R.string.goto_SETTINGS), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(FieldVisit.this);
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    public void previewCapturedImage() {
        try {
            // hide video preview


            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            image_view_preview.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(imageStoragePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap rotatedBitmap = null;
            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            imageView.setImageBitmap(rotatedBitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        context.getResources().getString(R.string.user_cancelled_image_capture), Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        context.getResources().getString(R.string.sorry_Failed_to_capture_image), Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // video successfully recorded
                // preview the recorded video
//                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        context.getResources().getString(R.string.user_cancelled_video_recording), Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        context.getResources().getString(R.string.sorry_Failed_to_record_video), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
    public void getHistory(){

            try {
                //We need to get the instance of the LayoutInflater, use the context of this activity
                final LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Inflate the view from a predefined XML layout
                final View view = inflater.inflate(R.layout.pop_up_get_field_visit_history, null);
                TextView header;
                ImageView close;
                RelativeLayout date_layout;
                Button submit;

                date_layout = (RelativeLayout) view.findViewById(R.id.date_layout);
                header = (TextView) view.findViewById(R.id.header);
                submit = (Button) view.findViewById(R.id.btnBuy);
                date = (TextView) view.findViewById(R.id.date);
                close = (ImageView) view.findViewById(R.id.close);
                tax = (Spinner) view.findViewById(R.id.tax_type_history);
                serviceType = (Spinner) view.findViewById(R.id.service_filed_type_history);
//                tax.setAdapter(taxTypeAdapter);
                tax.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, taxItems));
                serviceType.setEnabled(false);
                header.setText(context.getResources().getString(R.string.field_Visit));
                close.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_close));

                date_layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(tax.getSelectedItem().toString()!=null && !tax.getSelectedItem().toString().equals("")
                                && !tax.getSelectedItem().toString().equals(context.getResources().getString(R.string.select_TaxType)) &&
                                serviceType.getSelectedItem().toString()!=null && !serviceType.getSelectedItem().toString().equals("")
                                && !tax.getSelectedItem().toString().equals(context.getResources().getString(R.string.select_ServiceListFieldVisit))){
                            if(!date.getText().toString().equals("") && !date.getText().toString().equals(context.getResources().getString(R.string.select_from_and_to_Date))){
                                getFieldVisitHistory();
                            }else {
                                Utils.showAlert(FieldVisit.this,context.getResources().getString(R.string.select_from_and_to_Date));
                            }
                        }else {
                            Utils.showAlert(FieldVisit.this,context.getResources().getString(R.string.select_TaxType));

                        }

                    }
                });
                tax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String TaxTypeName = parent.getSelectedItem().toString();
                        ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));
                        for(Map.Entry<String, String> entry: spinnerMapTaxType.entrySet()) {
                            if(entry.getValue() == TaxTypeName) {
                                TaxTypeIdHistory=entry.getKey();
                                break;
                            }
                        }
                        if(TaxTypeIdHistory!=null&&position>0) {
                            getServiceListFieldVisitTypes(2,TaxTypeIdHistory);
                        }else {
                            serviceType.setAdapter(null);
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
                });

                serviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        String serviceListFieldDesc = parent.getSelectedItem().toString();
                        ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));
                        for(Map.Entry<String, String> entry: spinnerMapServiceFieldVisitTypes.entrySet()) {
                            if(entry.getValue() == serviceListFieldDesc) {
                                serviceListFieldTaxTypeIdHistory=entry.getKey();
                                break;
                            }
                        }

                    }
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });


                androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
                dialogBuilder.setView(view);
                alert = dialogBuilder.create();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(alert.getWindow().getAttributes());
                lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                lp.windowAnimations = R.style.DialogAnimation;
                alert.getWindow().setAttributes(lp);
                alert.show();
                alert.setCanceledOnTouchOutside(false);
                alert.setCancelable(true);
                alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }




    public void loadHistory() {
        String door_no= "" , plotarea="", buildage="", buildusage="", buildstructure="", taxlocation="" ;
        historyList = new ArrayList<TPtaxModel>();
        JSONArray jsonarray= null;
        try {
            jsonarray = new JSONArray(prefManager.getFieldVisitHistoryList());
            if (jsonarray != null && jsonarray.length() > 0) {
                fieldVisitBinding.activityMain.setVisibility(View.GONE);
                fieldVisitBinding.historyLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String taxtypeid = Utils.NotNullString(jsonobject.getString("taxtypeid"));
                    if (taxtypeid.equals("1")){
                        door_no = Utils.NotNullString(jsonobject.getString("door_no"));
                         plotarea = Utils.NotNullString(jsonobject.getString("plotarea"));
                         buildage = Utils.NotNullString(jsonobject.getString("buildage"));
                         buildusage = Utils.NotNullString(jsonobject.getString("buildusage"));
                         buildstructure = Utils.NotNullString(jsonobject.getString("buildstructure"));
                         taxlocation = Utils.NotNullString(jsonobject.getString("taxlocation"));
                    }
                    String Request_id = Utils.NotNullString(jsonobject.getString("request_id"));
                    String serviceid = Utils.NotNullString(jsonobject.getString("serviceid"));
                    String Data_ref_id = Utils.NotNullString(jsonobject.getString("data_ref_id"));
                    String TraderName = Utils.NotNullString(jsonobject.getString("ownername"));
                    String FieldVisitDate = Utils.NotNullString(jsonobject.getString("field_visit_upd_date"));
                    String TaxTypeName = Utils.NotNullString(jsonobject.getString("taxtypedesc_en"));
                    String Status = Utils.NotNullString(jsonobject.getString("field_visit_status"));
                    String Wardname = Utils.NotNullString(jsonobject.getString("ward_name_en"));
                    String Streetname = Utils.NotNullString(jsonobject.getString("street_name_en"));
                    String field_visit_image_status = Utils.NotNullString(jsonobject.getString("field_visit_photo_available"));
                    String Remark = Utils.NotNullString(jsonobject.getString("field_visit_remark"));


                    FieldVisitDate=changeDateFormat(FieldVisitDate);

                    TPtaxModel Detail = new TPtaxModel();
                    Detail.setRequest_id(Request_id);
                    Detail.setData_ref_id(Data_ref_id);
                    Detail.setTraderName(TraderName);
                    Detail.setFieldVisitDate(FieldVisitDate);
                    Detail.setTaxTypeName(TaxTypeName);
                    Detail.setStatus(Status);
                    Detail.setWardname(Wardname);
                    Detail.setStreetname(Streetname);
                    Detail.setRemark(Remark);
                    Detail.setField_visit_image_status(field_visit_image_status);
                    Detail.setServiceid(serviceid);
                    Detail.setTaxTypeId(taxtypeid);
                    if (taxtypeid.equals("1")){
                        Detail.setDoorno(door_no);
                        Detail.setPlotarea(plotarea);
                        Detail.setBuildage(buildage);
                        Detail.setBuildusage(buildusage);
                        Detail.setBuildstructure(buildstructure);
                        Detail.setTaxlocation(taxlocation);
                    }

                    historyList.add(Detail);
                }
            }else {
                Utils.showAlert(FieldVisit.this,context.getResources().getString(R.string.no_RECORD_FOUND));
                fieldVisitBinding.activityMain.setVisibility(View.VISIBLE);
                fieldVisitBinding.historyLayout.setVisibility(View.GONE);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

      /*  for (int i = 0; i < 3; i++) {
            fieldVisitBinding.activityMain.setVisibility(View.GONE);
            fieldVisitBinding.historyLayout.setVisibility(View.VISIBLE);
            if(i==0){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setRequest_id("2");
                Detail.setTraderName("Ram");
                Detail.setFieldVisitDate("05-02-2021");
                Detail.setTaxTypeName("Property Tax");
                Detail.setStatus("satisfied");
                Detail.setWardname("Adayar");
                Detail.setStreetname("North Street");
                Detail.setRemark("Testing");
                Detail.setFieldVisitImage("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GjBD6sDHcN79Na+cesHH/wBHwHzp6wcf/R8B86ZRl9H3cXQ+LHQ9j4v2f7dNPso0UJfeGNS9rZ930rcr2vavnHrBx/8AR8B86esHH/0fAfOg+lKyBQoFlAsBlNrcrW4Vi3EGQR7pcgNwu76IPMC3GvnXrBx/9HwHzp6wcf8A0fAfOg+kMsROYoC1wb5De4Fgb25Ej7a1ocBAkxlVOllRVGTRAgIGTTTRjVA9YOP/AKPgPnT1g4/+j4D50Tl9HaOIoUMYKEklTH0SSbkkW560aOI2vGDYAC8fUDcDhwB1r5x6wcf/AEfAfOnrBx/9HwHzpky+jGKE3vGuoIPxfEMbsOHWdTXjYeAoIzChjGoQxdEe4WtXzr1g4/8Ao+A+dPWDj/6PgPnTJlZ/S9gdzbqzdRHKq3XP2j6W4nE5d6IzlvaykcftrT/fMnzU7j50ezpdbZtWYor4vqGwdpQR4SJHmjVgDcFgCOkanFNhFxEk/wC1Lme3R3gCiwtw6/tr5Z++ZPmp3Hzp++ZPmp3Hzor1TpZqqqiufn9n0r0m2jBJhSscqO2ZdFYE8aqsczKHCmwcZW0Goveq/wDvmT5qdx86fvmT5qdx86LFjU6WzR8OJme/gscOMkjXKjWW5NrDiy5TxHKp/vGa98+t0PBeKCy9XVVZ/fMnzU7j50/fMnzU7j50bZ1ukmczHoshx0pFi+mUpwX2S2YjhzrKNrYjpfGe1cm6p1ixtppoOqqt++ZPmp3Hzp++ZPmp3HzobzR+Hos42nODcSa3U8F4quUdXLTt66hNjpXDBm0bLcBVA6N8tgBpa54c6rf75k+ancfOn75k+ancfOhvdJE5iPRYDOxQR36AJYCw4njrU0xsqsjB7GNcq6DQcuGvE8arn75k+ancfOn75k+ancfOjKddpZ4+ywYjEPK2dzdrAcANBwAA0ArHXD/fMnzU7j50/fMnzU7j50TH+hpojEeznUpSjnilKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUH/2Q==");
                historyList.add(Detail);
            }
            else if(i==1){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setRequest_id("1");
                Detail.setTraderName("Kumar");
                Detail.setFieldVisitDate("05-02-2021");
                Detail.setTaxTypeName("Water Tax");
                Detail.setStatus("UnSatisfied");
                Detail.setWardname("Adayar");
                Detail.setStreetname("West Street");
                Detail.setRemark("Testing");
                Detail.setFieldVisitImage("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GjBD6sDHcN79Na+cesHH/wBHwHzp6wcf/R8B86ZRl9H3cXQ+LHQ9j4v2f7dNPso0UJfeGNS9rZ930rcr2vavnHrBx/8AR8B86esHH/0fAfOg+lKyBQoFlAsBlNrcrW4Vi3EGQR7pcgNwu76IPMC3GvnXrBx/9HwHzp6wcf8A0fAfOg+kMsROYoC1wb5De4Fgb25Ej7a1ocBAkxlVOllRVGTRAgIGTTTRjVA9YOP/AKPgPnT1g4/+j4D50Tl9HaOIoUMYKEklTH0SSbkkW560aOI2vGDYAC8fUDcDhwB1r5x6wcf/AEfAfOnrBx/9HwHzpky+jGKE3vGuoIPxfEMbsOHWdTXjYeAoIzChjGoQxdEe4WtXzr1g4/8Ao+A+dPWDj/6PgPnTJlZ/S9gdzbqzdRHKq3XP2j6W4nE5d6IzlvaykcftrT/fMnzU7j50ezpdbZtWYor4vqGwdpQR4SJHmjVgDcFgCOkanFNhFxEk/wC1Lme3R3gCiwtw6/tr5Z++ZPmp3Hzp++ZPmp3Hzor1TpZqqqiufn9n0r0m2jBJhSscqO2ZdFYE8aqsczKHCmwcZW0Goveq/wDvmT5qdx86fvmT5qdx86LFjU6WzR8OJme/gscOMkjXKjWW5NrDiy5TxHKp/vGa98+t0PBeKCy9XVVZ/fMnzU7j50/fMnzU7j50bZ1ukmczHoshx0pFi+mUpwX2S2YjhzrKNrYjpfGe1cm6p1ixtppoOqqt++ZPmp3Hzp++ZPmp3HzobzR+Hos42nODcSa3U8F4quUdXLTt66hNjpXDBm0bLcBVA6N8tgBpa54c6rf75k+ancfOn75k+ancfOhvdJE5iPRYDOxQR36AJYCw4njrU0xsqsjB7GNcq6DQcuGvE8arn75k+ancfOn75k+ancfOjKddpZ4+ywYjEPK2dzdrAcANBwAA0ArHXD/fMnzU7j50/fMnzU7j50TH+hpojEeznUpSjnilKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUH/2Q==");
                historyList.add(Detail);
            }
            else {
                TPtaxModel Detail = new TPtaxModel();
                Detail.setRequest_id("3");
                Detail.setTraderName("Mathi");
                Detail.setFieldVisitDate("02-01-2021");
                Detail.setTaxTypeName("Property Tax");
                Detail.setStatus("Need Improvement");
                Detail.setWardname("Adayar");
                Detail.setStreetname("South Street");
                Detail.setRemark("Testing");
                Detail.setFieldVisitImage("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GjBD6sDHcN79Na+cesHH/wBHwHzp6wcf/R8B86ZRl9H3cXQ+LHQ9j4v2f7dNPso0UJfeGNS9rZ930rcr2vavnHrBx/8AR8B86esHH/0fAfOg+lKyBQoFlAsBlNrcrW4Vi3EGQR7pcgNwu76IPMC3GvnXrBx/9HwHzp6wcf8A0fAfOg+kMsROYoC1wb5De4Fgb25Ej7a1ocBAkxlVOllRVGTRAgIGTTTRjVA9YOP/AKPgPnT1g4/+j4D50Tl9HaOIoUMYKEklTH0SSbkkW560aOI2vGDYAC8fUDcDhwB1r5x6wcf/AEfAfOnrBx/9HwHzpky+jGKE3vGuoIPxfEMbsOHWdTXjYeAoIzChjGoQxdEe4WtXzr1g4/8Ao+A+dPWDj/6PgPnTJlZ/S9gdzbqzdRHKq3XP2j6W4nE5d6IzlvaykcftrT/fMnzU7j50ezpdbZtWYor4vqGwdpQR4SJHmjVgDcFgCOkanFNhFxEk/wC1Lme3R3gCiwtw6/tr5Z++ZPmp3Hzp++ZPmp3Hzor1TpZqqqiufn9n0r0m2jBJhSscqO2ZdFYE8aqsczKHCmwcZW0Goveq/wDvmT5qdx86fvmT5qdx86LFjU6WzR8OJme/gscOMkjXKjWW5NrDiy5TxHKp/vGa98+t0PBeKCy9XVVZ/fMnzU7j50/fMnzU7j50bZ1ukmczHoshx0pFi+mUpwX2S2YjhzrKNrYjpfGe1cm6p1ixtppoOqqt++ZPmp3Hzp++ZPmp3HzobzR+Hos42nODcSa3U8F4quUdXLTt66hNjpXDBm0bLcBVA6N8tgBpa54c6rf75k+ancfOn75k+ancfOhvdJE5iPRYDOxQR36AJYCw4njrU0xsqsjB7GNcq6DQcuGvE8arn75k+ancfOn75k+ancfOjKddpZ4+ywYjEPK2dzdrAcANBwAA0ArHXD/fMnzU7j50/fMnzU7j50TH+hpojEeznUpSjnilKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUH/2Q==");
                historyList.add(Detail);
            }
        }*/

       /* Collections.sort(historyList, (lhs, rhs) -> lhs.getRequest_id().compareTo(rhs.getRequest_id()));*/
        Collections.sort(historyList, (lhs, rhs) -> Integer.valueOf((lhs.getRequest_id())).compareTo(Integer.valueOf((rhs.getRequest_id()))));
        SortAndReverseList(historyList);
        if(historyList != null && historyList.size() > 0){
            fieldVisitBinding.noDataFoundLayout.setVisibility(View.GONE);
            fieldVisitBinding.swipeRefresh.setVisibility(View.VISIBLE);

            FieldVisitHistoryAdapter fieldVisitHistoryAdapter = new FieldVisitHistoryAdapter(context,historyList,this);
            fieldVisitBinding.stickyList.setAdapter(fieldVisitHistoryAdapter);
            fieldVisitHistoryAdapter.notifyDataSetChanged();
        } else {
            fieldVisitBinding.noDataFoundLayout.setVisibility(View.VISIBLE);
            fieldVisitBinding.swipeRefresh.setVisibility(View.GONE);
        }

    }
    public static String changeDateFormat(String olddate){
        final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
        final String NEW_FORMAT = "dd-MM-yyyy";
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
        //date.setText(newDateString);
        return  newDateString;
    }

    public void SortAndReverseList( ArrayList<TPtaxModel> historyList) {
        Collections.sort(historyList, new Comparator<TPtaxModel>() {
            @Override
            public int compare(TPtaxModel o1, TPtaxModel o2) {
                String date1=o1.fieldVisitDate;
                String date2=o2.fieldVisitDate;
                int compareResult = 0;
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                try {
                    Date arg0Date = format.parse(date1);
                    Date arg1Date = format.parse(date2);
                    compareResult = arg0Date.compareTo(arg1Date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    compareResult = date2.compareTo(date1);
                }
                return compareResult;
            }

        });
        // Collections.reverse(studentActivityDetails);
    }

    @Override
    public void onBackPressed() {
         if(fieldVisitBinding.historyLayout.getVisibility() == View.VISIBLE ){

            fieldVisitBinding.historyLayout.setVisibility(View.GONE);
            fieldVisitBinding.activityMain.setVisibility(View.VISIBLE);
        }else  if(/*request_id.equals("") &&*/fieldVisitBinding.fieldVisitLists.getVisibility() == View.VISIBLE){
//            fieldVisitBinding.taxType.setAdapter(taxTypeAdapter);
             /*fieldVisitBinding.taxType.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, taxItems));
             fieldVisitBinding.serviceFiledType.setAdapter(serviceFieldVisitAdapter);*/
             fieldVisitBinding.taxType.setSelection(0);
             fieldVisitBinding.serviceFiledType.setAdapter(null);
             fieldVisitBinding.requestIdTextField.setText("");
             fieldVisitBinding.selectedTaxName.setText("");
             fieldVisitBinding.applicantName.setText("");
             selectedTaxTypedInRecyler="";
             spinner_text_color=false;
            fieldVisitBinding.fieldVisitLists.setVisibility(View.GONE);
            fieldVisitBinding.detailsView.setVisibility(View.VISIBLE);
        }
        /*else if(fieldVisitBinding.fieldVisitLists.getVisibility() == View.VISIBLE  &&!request_id.equals("")){

            fieldVisitBinding.fieldVisitLists.setVisibility(View.GONE);
            fieldVisitBinding.detailsView.setVisibility(View.VISIBLE);
        }*/
        else {
            if(backPressflag){
                setResult(RESULT_OK);
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
                super.onBackPressed();
            }else {
                setResult(RESULT_CANCELED);
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
                super.onBackPressed();
            }

        }
    }


    @Override
    public void onClick(View v) {

    }

    public void getServiceRequestList() {
        try {
            new ApiService(this).makeJSONObjectRequest("ServiceRequestList", Api.Method.POST, UrlGenerator.TradersUrl(), encryptJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public JSONObject encryptJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), ServiceRequestListJsonParams().toString());
        JSONObject data = new JSONObject();
        data.put("user_name",prefManager.getUserName());
        data.put(AppConstant.DATA_CONTENT,authKey);
        Log.d("Req params", "" + data);
        return data;
    }
    public JSONObject ServiceRequestListJsonParams() throws JSONException {
        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"ServiceRequestList");
        data.put("taxtypeid",selectedTaxTypedInRecyler);
        data.put("serviceid",selectedServiceFieldVisitTypesId);

        Log.d("params", "" + data);
        return data;
    }



    public void getTaxTypeFieldVisitList() {
        taxType = new ArrayList<CommonModel>();
        String select_query= "SELECT *FROM " + DBHelper.TAX_TYPE_FIELD_VISIT_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){

            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setTaxtypeid(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TAX_TYPE_ID))));
                    commonModel.setTaxtypedesc_en(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TAX_TYPE_DESC_EN)));
                    commonModel.setTaxtypedesc_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TAX_TYPE_DESC_TA)));
                    commonModel.setTaxcollection_methodlogy(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TAX_COLLECTION_METHODLOGY)));
                    commonModel.setInstallmenttypeid(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.INSTALLMENT_TYPE_ID))));
                    taxType.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        Collections.sort(taxType, (lhs, rhs) -> lhs.getTaxtypeid().compareTo(rhs.getTaxtypeid()));

        if(taxType != null && taxType.size() >0) {
            spinnerMapTaxType = new HashMap<String, String>();
            spinnerMapTaxType.put(null, null);
            taxItems = new String[taxType.size() + 1];
            taxItems[0] = context.getResources().getString(R.string.select_TaxType);
            for (int i = 0; i < taxType.size(); i++) {
                spinnerMapTaxType.put( taxType.get(i).getTaxtypeid(), taxType.get(i).getTaxtypedesc_en());
                String Class = taxType.get(i).getTaxtypedesc_en();
                taxItems[i + 1] = Class;
            }
            System.out.println("items" + taxItems.toString());
            taxTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, taxItems);
          /*  try {
                if (taxItems != null && taxItems.length > 0) {
                    taxTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, taxItems);
                    taxTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fieldVisitBinding.taxType.setAdapter(taxTypeAdapter);
                    fieldVisitBinding.taxType.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
                    fieldVisitBinding.taxType.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, taxItems));

                    selectedTaxTypeId="0";
                    selectedTaxTypeName="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }*/
            fieldVisitBinding.taxType.setAdapter(new FieldVistTaxSpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, taxItems,spinnerMapTaxType));
        }


    }
    public void getFieldVisitStatusList() {
        fieldVisitStatus = new ArrayList<CommonModel>();
        String select_query= "SELECT *FROM " + DBHelper.FIELD_VISIT_STATUS;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){

            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setFIELD_VISIT_STATUS(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FIELD_VISIT_STATUS))));
                    commonModel.setFIELD_VISIT_STATUS_ID(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FIELD_VISIT_STATUS_ID)));

                    fieldVisitStatus.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        //Collections.sort(fieldVisitStatus, (lhs, rhs) -> lhs.getTaxtypedesc_en().compareTo(rhs.getTaxtypedesc_en()));

        if(fieldVisitStatus != null && fieldVisitStatus.size() >0) {

            spinnerMapFieldVisitType = new HashMap<String, String>();
            spinnerMapFieldVisitType.put(null, null);
            final String[] items = new String[fieldVisitStatus.size() + 1];
            items[0] = context.getResources().getString(R.string.select_Status);
            for (int i = 0; i < fieldVisitStatus.size(); i++) {
                spinnerMapFieldVisitType.put(fieldVisitStatus.get(i).getFIELD_VISIT_STATUS_ID(), fieldVisitStatus.get(i).getFIELD_VISIT_STATUS());
                String Class = fieldVisitStatus.get(i).getFIELD_VISIT_STATUS();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    fieldCurrentStatusArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                   /*
                    fieldCurrentStatusArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fieldVisitBinding.currentStatus.setAdapter(fieldCurrentStatusArray);
                    fieldVisitBinding.currentStatus.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    fieldVisitBinding.currentStatus.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedFieldVisitStatusId="0";
                    selectedFieldVisitStatusName="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }
    public void getServiceListFieldVisitTypes(int check,String id) {
        serviceFieldVisitTypes = new ArrayList<CommonModel>();
        String select_query= "SELECT * FROM " + DBHelper.SERVICE_LIST_FIELD_VISIT_TYPES;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){

            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setService_list_field_visit_taxtype_id((cursor.getString(cursor.getColumnIndexOrThrow("service_list_field_visit_taxtype_id"))));
                    commonModel.setService_list_field_visit_service_id(cursor.getString(cursor.getColumnIndexOrThrow("service_list_field_visit_service_id")));
                    commonModel.setService_list_field_visit_types_desc(cursor.getString(cursor.getColumnIndexOrThrow("service_list_field_visit_types_desc")));


                    serviceFieldVisitTypes.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        //Collections.sort(fieldVisitStatus, (lhs, rhs) -> lhs.getTaxtypedesc_en().compareTo(rhs.getTaxtypedesc_en()));
       ArrayList<CommonModel> selectedService = new ArrayList<CommonModel>();
        for (int i = 0; i < serviceFieldVisitTypes.size(); i++) {
            if(serviceFieldVisitTypes.get(i).getService_list_field_visit_taxtype_id().equals(id)){
                selectedService.add(serviceFieldVisitTypes.get(i));
            }else { }
        }
        if(selectedService != null && selectedService.size() >0) {

            spinnerMapServiceFieldVisitTypes = new HashMap<String, String>();
            spinnerMapServiceFieldVisitTypes.put(null, null);
            final String[] items = new String[selectedService.size() + 1];
            items[0] = context.getResources().getString(R.string.select_ServiceListFieldVisit);
            for (int i = 0; i < selectedService.size(); i++) {
                spinnerMapServiceFieldVisitTypes.put(selectedService.get(i).getService_list_field_visit_service_id(), selectedService.get(i).getService_list_field_visit_types_desc());
                String Class = selectedService.get(i).getService_list_field_visit_types_desc();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    if (check == 1) {
                        serviceFieldVisitAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                        /*
                        serviceFieldVisitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        fieldVisitBinding.serviceFiledType.setAdapter(serviceFieldVisitAdapter);
                        fieldVisitBinding.serviceFiledType.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                        fieldVisitBinding.serviceFiledType.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                        selectedServiceFieldVisitTypesId="0";
                        selectedServiceFieldVisitTypesName="";
                        fieldVisitBinding.serviceFiledType.setSelection(1);
                    }else if(check == 2){
                        serviceFieldVisitAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                       /*
                        serviceFieldVisitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        serviceType.setAdapter(serviceFieldVisitAdapter);
*/
                        serviceType.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));
                        serviceType.setSelection(1);
                    }

                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status;
            String request_Id;
            String data_ref_Id;
            String ward_code;
            String ward_name_en;
            String ward_name_ta;
            String street_code;
            String street_name_en;
            String street_name_ta;
            String ownername;
            String door_no;
            String plotarea;
            String buildage;
            String buildusage;
            String buildstructure;
            String taxlocation;
            String tax_type_name;
            String tax_type_id_;

            if ("ServiceRequestList".equals(urlType)) {
                try{
                    searchRequestList=new ArrayList<CommonModel>();
                    String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                    String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                    Log.d("ServiceRequestList", "" + userDataDecrypt);
                    JSONObject jsonObject = new JSONObject(userDataDecrypt);
                    status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    if (status.equalsIgnoreCase("SUCCESS") ) {
                        JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.DATA);
                        if(jsonarray != null && jsonarray.length() >0) {
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                CommonModel commonModel = new CommonModel();
                                tax_type_name=Utils.NotNullString(jsonobject.getString("tax_type_name"));
                                tax_type_id_=Utils.NotNullString(jsonobject.getString("tax_type_id"));
                                request_Id = Utils.NotNullString(jsonobject.getString("request_id"));
                                data_ref_Id = Utils.NotNullString(jsonobject.getString("data_ref_id"));
                                ward_code = Utils.NotNullString(jsonobject.getString("ward_code"));
                                ward_name_en = Utils.NotNullString(jsonobject.getString("ward_name_en"));
                                ward_name_ta = Utils.NotNullString(jsonobject.getString("ward_name_ta"));
                                street_code = Utils.NotNullString(jsonobject.getString("street_code"));
                                street_name_en = Utils.NotNullString(jsonobject.getString("street_name_en"));
                                street_name_ta = Utils.NotNullString(jsonobject.getString("street_name_ta"));
                                ownername = Utils.NotNullString(jsonobject.getString("ownername"));

                                if (selectedTaxTypeName.equals("Property Tax")){
                                door_no = Utils.NotNullString(jsonobject.getString("door_no"));
                                plotarea = Utils.NotNullString(jsonobject.getString("plotarea"));
                                buildage = Utils.NotNullString(jsonobject.getString("buildage"));
                                buildstructure = Utils.NotNullString(jsonobject.getString("buildstructure"));
                                buildusage = Utils.NotNullString(jsonobject.getString("buildusage"));
                                taxlocation = Utils.NotNullString(jsonobject.getString("taxlocation"));
                                    commonModel.setDoor_no(door_no);
                                    commonModel.setPlotarea(plotarea);
                                    commonModel.setBuildage(buildage);
                                    commonModel.setBuildstructure(buildstructure);
                                    commonModel.setBuildusage(buildusage);
                                    commonModel.setTaxlocation(taxlocation);
                            }

                                commonModel.setRequest_id(request_Id);
                                commonModel.setData_ref_id(data_ref_Id);
                                commonModel.setWard_code(ward_code);
                                commonModel.setWard_name_en(ward_name_en);
                                commonModel.setWard_name_ta(ward_name_ta);
                                commonModel.setStreet_code(street_code);
                                commonModel.setStreet_name_en(street_name_en);
                                commonModel.setStreet_name_ta(street_name_ta);
                                commonModel.setOwnername(ownername);
                                commonModel.setTaxtypeid(tax_type_id_);
                                commonModel.setTaxtypedesc_en(tax_type_name);


                                searchRequestList.add(commonModel);

                            }

                            if(searchRequestList.size()>0){
                                if(selectedTaxTypeName.equals("Property Tax")) {
                                    fieldVisitRquestListAdapter = new FieldVisitRquestListAdapter(FieldVisit.this, searchRequestList,"Property");
                                    fieldVisitBinding.fieldVisitLists.setAdapter(fieldVisitRquestListAdapter);
                                    fieldVisitBinding.fieldVisitLists.setVisibility(View.VISIBLE);
                                    fieldVisitBinding.detailsView.setVisibility(View.GONE);
                                }
                                else {
                                    fieldVisitRquestListAdapter = new FieldVisitRquestListAdapter(FieldVisit.this, searchRequestList,"Water Charges");
                                    fieldVisitBinding.fieldVisitLists.setAdapter(fieldVisitRquestListAdapter);
                                    fieldVisitBinding.fieldVisitLists.setVisibility(View.VISIBLE);
                                    fieldVisitBinding.detailsView.setVisibility(View.GONE);
                                }
                            }
                        }else {
                            Utils.showAlert(FieldVisit.this,context.getResources().getString(R.string.no_RECORD_FOUND));
                            fieldVisitBinding.requestIdTextField.setText("");
                            fieldVisitBinding.applicantName.setText("");
                        }

                    }else if(status.equalsIgnoreCase("FAILD")){
                        Utils.showAlert(FieldVisit.this, jsonObject.getString("MESSAGE"));
                        fieldVisitBinding.requestIdTextField.setText("");
                        fieldVisitBinding.applicantName.setText("");
                    }
                }catch (Exception e){

                }

            }
            if("save_data".equals(urlType)){
                try {
                    String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                    String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                    Log.d("userdatadecry", "" + userDataDecrypt);
                    JSONObject jsonObject = new JSONObject(userDataDecrypt);
                    status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    if (status.equalsIgnoreCase("SUCCESS") ){
                        db.delete(DBHelper.CAPTURED_PHOTO, "request_id" + "=?"+ " and  data_ref_id" + "=?", new String[]{request_id,data_ref_id});
                        Utils.showToast(FieldVisit.this, jsonObject.getString("MESSAGE"));
                        onBackPressed();
                    }
                    else if(status.equalsIgnoreCase("FAILD")){
                        Utils.showAlert(FieldVisit.this, jsonObject.getString("MESSAGE"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
            if ("FieldVisitedImage".equals(urlType) && responseObj != null) {
                String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                Log.d("FieldImageDatadecry", "" + userDataDecrypt);
                JSONObject jsonObject = new JSONObject(userDataDecrypt);

                status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    JSONObject jsonObject1=jsonObject.getJSONObject(AppConstant.DATA);
                    FieldVisitImageString = jsonObject1.getString(AppConstant.FIELD_VISIT_IMAGE);
                    Log.d("FieldVisitImage", "" + jsonObject);
                    viewImageScreen(FieldVisitImageString);

                }else if(status.equalsIgnoreCase("FAILD")){
                    Utils.showAlert(FieldVisit.this, jsonObject.getString("MESSAGE"));
                } }
            if ("TradersFieldVisitHistoryService".equals(urlType) && responseObj != null) {
                String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                Log.d("FieldVisitHistorydecry", "" + userDataDecrypt);
                JSONObject jsonObject = new JSONObject(userDataDecrypt);

                status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                if (status.equalsIgnoreCase("SUCCESS") ) {
                    JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.DATA);
                    prefManager.setFieldVisitHistoryList(jsonarray.toString());
                    Log.d("FieldVisitHistory", "" + jsonObject);
                    alert.dismiss();
                    loadHistory();
                } else if(status.equalsIgnoreCase("FAILD")){
                    Utils.showAlert(FieldVisit.this, jsonObject.getString("MESSAGE"));
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void OnError(VolleyError volleyError) {
//        Utils.showAlert(FieldVisit.this, context.getResources().getString(R.string.try_after_some_time));
    }

    public void fieldRequestListClickedItemProcess(int pos){
        request_id=searchRequestList.get(pos).getRequest_id();
        data_ref_id=searchRequestList.get(pos).getData_ref_id();
        fieldVisitBinding.fieldVisitLists.setVisibility(View.GONE);
        fieldVisitBinding.detailsView.setVisibility(View.VISIBLE);
        fieldVisitBinding.applicantName.setText(searchRequestList.get(pos).getOwnername());
        fieldVisitBinding.requestIdTextField.setText(searchRequestList.get(pos).getRequest_id());
        //fieldVisitBinding.selectedTaxName.setText(searchRequestList.get(pos).getTaxtypedesc_en());
        selectedTaxTypedInRecyler=searchRequestList.get(pos).getTaxtypeid();
        if(selectedTaxTypedInRecyler.equals("1")){
            fieldVisitBinding.taxType.setSelection(1);
        }
        else if(selectedTaxTypedInRecyler.equals("2")){
            fieldVisitBinding.taxType.setSelection(2);
        }
        else{
            fieldVisitBinding.taxType.setSelection(0);
        }
        //spinner_text_color=true;


    }

    public void sync_data() {
        try {
            new ApiService(this).makeJSONObjectRequest("save_data", Api.Method.POST, UrlGenerator.TradersUrl(), dataTobeSavedJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject dataTobeSavedJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());
        Log.d("jason saving", "" + dataset.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("saving", "" + dataSet);

        return dataSet;
    }

    public boolean iscaptureImgExist(String request_id,String data_ref_id) {
        Cursor cursor = null;
        String query = " SELECT  request_id  FROM " + DBHelper.CAPTURED_PHOTO+ " WHERE  request_id  =?"+ " AND  data_ref_id  =?";

        cursor = Dashboard.db.rawQuery(query,  new String[]{request_id,data_ref_id});

        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public boolean isrequestIDexixt(String request_id, String data_ref_id) {
        Cursor cursor = null;
        String query = " SELECT  request_id  FROM " + DBHelper.SAVE_FIELD_VISIT + " WHERE  request_id  =?"+ " AND  data_ref_id  =?";
        cursor = Dashboard.db.rawQuery(query,  new String[]{request_id,data_ref_id});

        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public void showDatePickerDialog(){
        Utils.showDatePickerDialog(context);

    }

    /*
    public void showDatePickerDialog() {
        SmoothDateRangePickerFragment smoothDateRangePickerFragment = SmoothDateRangePickerFragment.newInstance(
                new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                    @Override
                    public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                               int yearStart, int monthStart,
                                               int dayStart, int yearEnd,
                                               int monthEnd, int dayEnd) {
                        fromDate=dayStart+"-"+(monthStart+1)+"-"+yearStart;
                         toDate=dayEnd+"-"+(monthEnd+1)+"-"+yearEnd;
                        String fromToDate=updateLabel(fromDate)+" to "+updateLabel(toDate);
//                        String fromToDate=fromDate+" to "+toDate;
                                date.setText(fromToDate);
                                System.out.println("date>>>"+fromDate);
                        // grab the date range, do what you want
                    }
                });
        smoothDateRangePickerFragment.setMaxDate(Calendar.getInstance());
        smoothDateRangePickerFragment.setThemeDark(false);
        smoothDateRangePickerFragment.show(getFragmentManager(), "smoothDateRangePicker");

    }
*/
    private String updateLabel(String dateStr) {
        String myFormat="";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = format.parse(dateStr);
            System.out.println(date1);
            String dateTime = format.format(date1);
            System.out.println("Current Date Time : " + dateTime);
             myFormat = dateTime; //In which you need put here
        } catch (ParseException e) {
            e.printStackTrace();
        }
     return myFormat;
    }

    @Override
    public void getDate(String dateText) {
        String[] separated = dateText.split(":");
        fromDate = separated[0]; // this will contain "Fruit"
        toDate = separated[1];
        date.setText(fromDate+" to "+toDate);
    }

    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
