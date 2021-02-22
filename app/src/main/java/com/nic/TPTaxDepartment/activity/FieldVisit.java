package com.nic.TPTaxDepartment.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nic.TPTaxDepartment.Adapter.FieldVisitRquestListAdapter;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.Support.MyCustomTextView;
import com.nic.TPTaxDepartment.Support.MyLocationListener;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.databinding.FieldVisitBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class FieldVisit extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener  {

    private FieldVisitBinding fieldVisitBinding;
    private ArrayList<String> Current_Status = new ArrayList<>();
    private boolean imageboolean = Boolean.parseBoolean(null);
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
    private PrefManager prefManager;
    FieldVisitRquestListAdapter fieldVisitRquestListAdapter;
    String request_id,data_ref_id;
    Dialog dialog;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fieldVisitBinding = DataBindingUtil.setContentView(this, R.layout.field_visit);
        fieldVisitBinding.setActivity(this);
        context=this;
        prefManager = new PrefManager(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        //loadCurrentStatus();
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getTaxTypeFieldVisitList();
        getFieldVisitStatusList();


        Utils.setLanguage(fieldVisitBinding.remarksText,"en","USA");

        fieldVisitBinding.serviceFiledType.setEnabled(false);
        fieldVisitBinding.taxType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String TaxTypeName = parent.getSelectedItem().toString();
                ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

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
                    getServiceListFieldVisitTypes(selectedTaxTypeId);
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

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
                    getServiceRequestList();
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

    public void openCameraScreen() {
        Intent intent = new Intent(this, CameraScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void viewImage() {
        if(fieldVisitBinding.requestIdTextField.getText()!= null && !fieldVisitBinding.requestIdTextField.getText().equals("")){
            if(imageboolean==true) {
                if (getSaveTradeImageTable() > 0) {
                    Intent intent = new Intent(this, FullImageActivity.class);
                    intent.putExtra("request_id", fieldVisitBinding.requestIdTextField.getText().toString());
                    intent.putExtra("key", "FieldVisit");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } else {
                    Utils.showAlert(FieldVisit.this, "No image Saved in Local");
                }
            }
            else {
                Utils.showAlert(FieldVisit.this, "No image Saved in Local");
            }
        }else {
            Utils.showAlert(FieldVisit.this, "Select TaxType To Get Request Id");
        }

    }

    public int getSaveTradeImageTable(){

        Cursor cursor = null;
        String sql = "SELECT * FROM " + DBHelper.CAPTURED_PHOTO + " WHERE request_id ="+fieldVisitBinding.requestIdTextField.getText().toString();
        cursor=Dashboard.db.rawQuery(sql,null,null);
        if (cursor.getCount() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public void image() {
        if(!fieldVisitBinding.requestIdTextField.getText().toString().isEmpty() && !selectedTaxTypeName.equals("Select TaxType")) {
            imageWithDescription(fieldVisitBinding.takePhotoTv, "mobile");
        }
        else {
            Utils.showAlert(FieldVisit.this,"Select Tax Type first!");
        }
    }
    public void imageWithDescription(TextView action_tv, final String type) {

        dataset = new JSONObject();

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
                            imageboolean = false;
                            Utils.showAlert(FieldVisit.this, "Atleast Capture one Photo");
                            break;
                            //e.printStackTrace();
                        }

                        String description = myEditTextView.getText().toString();

                        if (MyLocationListener.latitude > 0) {
                            offlatTextValue = MyLocationListener.latitude;
                            offlanTextValue = MyLocationListener.longitude;
                        }


                        ContentValues imageValue = new ContentValues();

                        imageValue.put("request_id", request_id);
                        imageValue.put(AppConstant.LATITUDE, offlatTextValue);
                        imageValue.put(AppConstant.LONGITUDE, offlanTextValue);
                        imageValue.put(AppConstant.FIELD_IMAGE, image_str.trim());
                        imageValue.put(AppConstant.DESCRIPTION, myEditTextView.getText().toString());
                        imageValue.put("pending_flag", 1);


                           if(iscaptureImgExist(request_id)) {
                               long rowUpdated1 = LoginScreen.db.update(DBHelper.CAPTURED_PHOTO, imageValue, "request_id  = ? ", new String[]{request_id});
                               if (rowUpdated1 != -1) {
                                   imageboolean = true;
                                    Toast.makeText(FieldVisit.this, "Image Updated!", Toast.LENGTH_SHORT).show();
//                                   Utils.showAlert(FieldVisit.this, " Capture-Photo updated");

                               }
                           }
                           else {
                                long rowInserted = LoginScreen.db.insert(DBHelper.CAPTURED_PHOTO,null,imageValue);
                                //Toast.makeText(FieldVisit.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                if (rowInserted != -1) {
                                     Toast.makeText(FieldVisit.this, "Image added", Toast.LENGTH_SHORT).show();
//                                    Utils.showAlert(FieldVisit.this, " Capture-Photo added in Local");

                                }
                                else {
                                    Toast.makeText(FieldVisit.this, "Something wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        if (Utils.isOnline())  {
                            try {
                                //imageArray.put(i);
                                imageArray.put("lat",offlatTextValue);
                                imageArray.put("long",offlanTextValue);
                                imageArray.put("photo",image_str.trim());
                                //imageArray.put(description);
                                imageJson.put(imageArray);
                                imageboolean = true;
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
                    Utils.showAlert(FieldVisit.this, "First Capture Image then add another Image!");
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
        if(!fieldVisitBinding.taxType.getSelectedItem().equals(null)){
            if(!fieldVisitBinding.assessmentId.getText().equals(null)){
                if(fieldVisitBinding.currentStatus.getSelectedItem()!=null&&!fieldVisitBinding.currentStatus.getSelectedItem().equals("Select Status")){
                    if(!fieldVisitBinding.remarksText.getText().toString().isEmpty()){
                        if(imageboolean == true) {
                            submit();
                        }
                        else {
                            Utils.showAlert(FieldVisit.this,"Take Photo");
                        }
                    }
                    else {
                        Utils.showAlert(FieldVisit.this,"Enter Remarks");
                    }

                }
                else {
                    Utils.showAlert(FieldVisit.this," Please Select Current Status");
                }
            }
            else {
                Utils.showAlert(FieldVisit.this,"Enter Assessment Id");
            }
        }
        else {
            Utils.showAlert(FieldVisit.this,"Enter Tax Type Id");
        }
    }

    public  void  submit() {
        String tax_type_id = selectedTaxTypeId;
        //String assessment_id = fieldVisitBinding.assessmentId.getText().toString();
        String applicant_name = fieldVisitBinding.applicantName.getText().toString();
        //String build_type = fieldVisitBinding.buildType.getText().toString();
        String current_status = selectedFieldVisitStatusId;
        String remarks = fieldVisitBinding.remarksText.getText().toString();

        if(Utils.isOnline()) {
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

            if (isrequestIDexixt(request_id)){
                long rowUpdated1 = Dashboard.db.update(DBHelper.SAVE_FIELD_VISIT, fieldValue, "request_id  = ? ", new String[]{request_id});
            if (rowUpdated1 != -1) {
//                 Toast.makeText(FieldVisit.this, "Field-Visit updated", Toast.LENGTH_SHORT).show();
//                Utils.showAlert(FieldVisit.this, " Field-Visit updated");
                Utils.showToast(this,"Field-Visit updated");

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
                    Utils.showToast(this,"Field-Visit added");
                    onBackPressed();
                    //Dashboard.syncvisiblity();
                    //finish();
                    //dashboard();
                } else {
                    Toast.makeText(FieldVisit.this, "Something wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }

        else {

            try {
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



        String authKey = dataset.toString();
        int maxLogSize = 2000;
        for(int i = 0; i <= authKey.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > authKey.length() ? authKey.length() : end;
            Log.v("to_send+_plain", authKey.substring(start, end));
        }

      //  String authKey1 = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());

//        for(int i = 0; i <= authKey1.length() / maxLogSize; i++) {
//            int start = i * maxLogSize;
//            int end = (i+1) * maxLogSize;
//            end = end > authKey.length() ? authKey1.length() : end;
//            Log.v("to_send_encryt", authKey1.substring(start, end));
//        }
        //sync_data();

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
                Utils.showAlert(FieldVisit.this, "Satellite communication not available to get GPS Co-ordination Please Capture Photo in Open Area..");
            }
        } else {
            Utils.showAlert(FieldVisit.this, "GPS is not turned on...");
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
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(FieldVisit.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
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
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
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
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
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
    @Override
    public void onBackPressed() {
        if(fieldVisitBinding.fieldVisitLists.getVisibility() == View.VISIBLE){
            fieldVisitBinding.fieldVisitLists.setVisibility(View.GONE);
            fieldVisitBinding.detailsView.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
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
        return data;
    }
    public JSONObject ServiceRequestListJsonParams() throws JSONException {
        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"ServiceRequestList");
        data.put("taxtypeid",selectedTaxTypeId);
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
        Collections.sort(taxType, (lhs, rhs) -> lhs.getTaxtypedesc_en().compareTo(rhs.getTaxtypedesc_en()));

        if(taxType != null && taxType.size() >0) {

            spinnerMapTaxType = new HashMap<String, String>();
            spinnerMapTaxType.put(null, null);
            final String[] items = new String[taxType.size() + 1];
            items[0] = "Select TaxType";
            for (int i = 0; i < taxType.size(); i++) {
                spinnerMapTaxType.put( taxType.get(i).taxtypeid, taxType.get(i).taxtypedesc_en);
                String Class = taxType.get(i).taxtypedesc_en;
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fieldVisitBinding.taxType.setAdapter(RuralUrbanArray);
                    fieldVisitBinding.taxType.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
                    selectedTaxTypeId="0";
                    selectedTaxTypeName="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
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
            items[0] = "Select Status";
            for (int i = 0; i < fieldVisitStatus.size(); i++) {
                spinnerMapFieldVisitType.put(fieldVisitStatus.get(i).getFIELD_VISIT_STATUS_ID(), fieldVisitStatus.get(i).getFIELD_VISIT_STATUS());
                String Class = fieldVisitStatus.get(i).getFIELD_VISIT_STATUS();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fieldVisitBinding.currentStatus.setAdapter(RuralUrbanArray);
                    fieldVisitBinding.currentStatus.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
                    selectedFieldVisitStatusId="0";
                    selectedFieldVisitStatusName="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }
    public void getServiceListFieldVisitTypes(String id) {
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
            items[0] = "Select ServiceListFieldVisit";
            for (int i = 0; i < selectedService.size(); i++) {
                spinnerMapServiceFieldVisitTypes.put(selectedService.get(i).getService_list_field_visit_service_id(), selectedService.get(i).getService_list_field_visit_types_desc());
                String Class = selectedService.get(i).getService_list_field_visit_types_desc();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    fieldVisitBinding.serviceFiledType.setAdapter(RuralUrbanArray);
                    fieldVisitBinding.serviceFiledType.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
                    selectedServiceFieldVisitTypesId="0";
                    selectedServiceFieldVisitTypesName="";
                    fieldVisitBinding.serviceFiledType.setSelection(1);
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
            String request_id;
            String data_ref_id;
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

            if ("ServiceRequestList".equals(urlType)) {
                try{
                    searchRequestList=new ArrayList<CommonModel>();
                    String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                    String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                    Log.d("userdatadecry", "" + userDataDecrypt);
                    JSONObject jsonObject = new JSONObject(userDataDecrypt);
                    status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    if (status.equalsIgnoreCase("SUCCESS") ) {
                        JSONArray jsonarray = jsonObject.getJSONArray(AppConstant.DATA);
                        if(jsonarray != null && jsonarray.length() >0) {
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                CommonModel commonModel = new CommonModel();
                                request_id = Utils.NotNullString(jsonobject.getString("request_id"));
                                data_ref_id = Utils.NotNullString(jsonobject.getString("data_ref_id"));
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

                                commonModel.setRequest_id(request_id);
                                commonModel.setData_ref_id(data_ref_id);
                                commonModel.setWard_code(ward_code);
                                commonModel.setWard_name_en(ward_name_en);
                                commonModel.setWard_name_ta(ward_name_ta);
                                commonModel.setStreet_code(street_code);
                                commonModel.setStreet_name_en(street_name_en);
                                commonModel.setStreet_name_ta(street_name_ta);
                                commonModel.setOwnername(ownername);


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
                        }

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
                        Utils.showAlert(FieldVisit.this, jsonObject.getString("MESSAGE"));
                        onBackPressed();
                    }
                    else if(status.equalsIgnoreCase("FAILD")){
                        Utils.showAlert(FieldVisit.this, jsonObject.getString("MESSAGE"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void OnError(VolleyError volleyError) {
        Utils.showAlert(FieldVisit.this, "Try after some time!");
    }

    public void fieldRequestListClickedItemProcess(int pos){
        request_id=searchRequestList.get(pos).getRequest_id();
        data_ref_id=searchRequestList.get(pos).getData_ref_id();
        fieldVisitBinding.fieldVisitLists.setVisibility(View.GONE);
        fieldVisitBinding.detailsView.setVisibility(View.VISIBLE);
        fieldVisitBinding.applicantName.setText(searchRequestList.get(pos).getOwnername());
        fieldVisitBinding.requestIdTextField.setText(searchRequestList.get(pos).getRequest_id());

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
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("saving", "" + authKey);
        return dataSet;
    }

    public boolean iscaptureImgExist(String request_id) {
        Cursor cursor = null;
        String query = " SELECT  request_id  FROM " + DBHelper.CAPTURED_PHOTO+ " WHERE  request_id  =?";

        cursor = Dashboard.db.rawQuery(query,  new String[]{request_id});

        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        return false;
    }

    public boolean isrequestIDexixt(String request_id) {
        Cursor cursor = null;
        String query = " SELECT  request_id  FROM " + DBHelper.SAVE_FIELD_VISIT + " WHERE  request_id  =?";
        cursor = Dashboard.db.rawQuery(query,  new String[]{request_id});

        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        return false;
    }
}
