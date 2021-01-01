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
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.Support.MyCustomTextView;
import com.nic.TPTaxDepartment.Support.MyLocationListener;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.databinding.FieldVisitBinding;
import com.nic.TPTaxDepartment.utils.CameraUtils;
import com.nic.TPTaxDepartment.utils.FontCache;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class FieldVisit extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fieldVisitBinding = DataBindingUtil.setContentView(this, R.layout.field_visit);
        fieldVisitBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        loadCurrentStatus();
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    }

    public void openCameraScreen() {
        Intent intent = new Intent(this, CameraScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void image() {
        imageWithDescription(fieldVisitBinding.takePhotoTv, "mobile");
    }

    public void imageWithDescription(TextView action_tv, final String type) {
        imageboolean = true;
        dataset = new JSONObject();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String date = sdf.format(new Date());
        String tax_type_id = fieldVisitBinding.taxType.getText().toString();
        String assessment_id = fieldVisitBinding.assessmentId.getText().toString();
        String applicant_name = fieldVisitBinding.applicantName.getText().toString();
        String build_type = fieldVisitBinding.buildType.getText().toString();
        String current_status = fieldVisitBinding.currentStatus.getSelectedItem().toString();
        String remarks = fieldVisitBinding.remarks.getText().toString();

//            ContentValues fieldValue = new ContentValues();
//            fieldValue.put(AppConstant.TAX_TYPE_ID, tax_type_id);
//            fieldValue.put(AppConstant.ASSESSMENT_ID, assessment_id);
////            fieldValue.put(AppConstant.APPLICANT_NAME, applicant_name);
////            fieldValue.put(AppConstant.BUILD_TYPE, build_type);
//            fieldValue.put(AppConstant.CURRENT_STATUS, current_status);
//          //  fieldValue.put(AppConstant.REMARKS, remarks);
//
//            fieldValue.put("delete_flag", 0);
//
//            LoginScreen.db.insert(DBHelper.SAVE_TRADE_IMAGE, null, fieldValue);

        final Dialog dialog = new Dialog(this,
                R.style.AppTheme);
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
                onBackPressed();
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


//                Cursor inpection_Cursor = getRawEvents("SELECT MAX(inspection_id) FROM " + DBHelper.INSPECTION_PENDING, null);
//                Log.d("cursor_count", String.valueOf(inpection_Cursor.getCount()));
//                if (inpection_Cursor.getCount() > 0) {
//                    if (inpection_Cursor.moveToFirst()) {
//                        do {
//                            inspectionID = inpection_Cursor.getInt(0);
//                            Log.d("inspectionID", "" + inspectionID);
//                        } while (inpection_Cursor.moveToNext());
//                    }
//                }
                int childCount = mobileNumberLayout.getChildCount();
                if (childCount > 0) {
                    for (int i = 0; i < childCount; i++) {
                        JSONArray imageArray = new JSONArray();

                        View vv = mobileNumberLayout.getChildAt(i);
                        EditText myEditTextView = (EditText) vv.findViewById(R.id.description);

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

                        // Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();

                        ContentValues imageValue = new ContentValues();

                        imageValue.put(AppConstant.TAX_TYPE_ID, fieldVisitBinding.taxType.getText().toString());
                        imageValue.put(AppConstant.LATITUDE, offlatTextValue);
                        imageValue.put(AppConstant.LONGITUDE, offlanTextValue);
                        imageValue.put(AppConstant.FIELD_IMAGE, image_str.trim());
                        imageValue.put(AppConstant.DESCRIPTION, myEditTextView.getText().toString());
                        imageValue.put("pending_flag", 1);
//                        if(prefManager.getLevels().equalsIgnoreCase("D")) {
//                            imageValue.put("level", "D");
//                        }else if(prefManager.getLevels().equalsIgnoreCase("S")) {
//                            imageValue.put("level", "S");
//                        }


                    //    if (!Utils.isOnline()) {
                            long rowInserted = db.insert(DBHelper.CAPTURED_PHOTO, null, imageValue);

                            if (rowInserted != -1) {
                                Utils.showAlert(FieldVisit.this, "Image Saved");
                                dialog.dismiss();
                            }
                            //else {
//                                Toast.makeText(FieldVisit.this, "Something wrong", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            imageArray.put(i);
//                         //   imageArray.put(work_id);
//                            imageArray.put(offlatTextValue);
//                            imageArray.put(offlanTextValue);
//                            imageArray.put(image_str.trim());
//                            imageArray.put(description);
//                            imageJson.put(imageArray);
//                        }

                        //  long localImageInserted = LoginScreen.db.insert(DBHelper.LOCAL_IMAGE, null, imageValue);
                    }
//                    try {
//                        //dataset.put("image_details", imageJson);
//
//                        Log.d("post_dataset_inspection", dataset.toString());
//                        //   String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());
////                        String authKey = dataset.toString();
////                        int maxLogSize = 1000;
////                        for(int i = 0; i <= authKey.length() / maxLogSize; i++) {
////                            int start = i * maxLogSize;
////                            int end = (i+1) * maxLogSize;
////                            end = end > authKey.length() ? authKey.length() : end;
////                            Log.v("to_send", authKey.substring(start, end));
////                     }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                dialog.dismiss();
      //         focusOnView(scrollView, action_tv);


            }
        });
        final String values = action_tv.getText().toString().replace("NA", "");
        Button btnAddMobile = (Button) dialog.findViewById(R.id.btn_add);
        btnAddMobile.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.MEDIUM));


        if(fieldVisitBinding.currentStatus.getSelectedItem().equals("Need Improvement")){
            btnAddMobile.setVisibility(View.VISIBLE);
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
        if(!fieldVisitBinding.taxType.getText().equals(null)){
            if(!fieldVisitBinding.assessmentId.getText().equals(null)){
                if(!fieldVisitBinding.currentStatus.getSelectedItem().equals("Select Current Status")){
                    if(imageboolean == true) {
                        submit();
                    }
                    else {
                        Utils.showAlert(FieldVisit.this,"Take Photo");
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
//        try {
//            db.delete(DBHelper.LOCAL_IMAGE, null, null);
//        } catch (Exception e)
//            e.printStackTrace();
//        }

        String tax_type_id = fieldVisitBinding.taxType.getText().toString();
        String assessment_id = fieldVisitBinding.assessmentId.getText().toString();
        String applicant_name = fieldVisitBinding.applicantName.getText().toString();
        String build_type = fieldVisitBinding.buildType.getText().toString();
        String current_status = fieldVisitBinding.currentStatus.getSelectedItem().toString();
        String remarks = fieldVisitBinding.remarks.getText().toString();

        ContentValues fieldValue = new ContentValues();
        fieldValue.put(AppConstant.TAX_TYPE_ID, tax_type_id);
        fieldValue.put(AppConstant.ASSESSMENT_ID, assessment_id);
//        fieldValue.put(AppConstant.APPLICANT_NAME, applicant_name);
//        fieldValue.put(AppConstant.BUILD_TYPE, build_type);
        fieldValue.put(AppConstant.CURRENT_STATUS, current_status);
      //  fieldValue.put(AppConstant.REMARKS, remarks);

          //  long rowUpdated = LoginScreen.db.update(DBHelper.SAVE_FIELD_VISIT, fieldValue, "taxtypeid//  = ? AND delete_flag = ?", new String[]{tax_type_id,"0" });
            long rowUpdated = db.insert(DBHelper.SAVE_FIELD_VISIT, null, fieldValue);

            if (rowUpdated != -1) {
               // Toast.makeText(FieldVisit.this, "New Inspection added", Toast.LENGTH_SHORT).show();
                Utils.showAlert(FieldVisit.this,"Saved");
                Dashboard.syncvisiblity();
                //finish();
                dashboard();
            } else {
                Toast.makeText(FieldVisit.this, "Something wrong", Toast.LENGTH_SHORT).show();
            }

            // db.rawQuery("UPDATE "+DBHelper.INSPECTION_PENDING+" SET (stage_of_work_on_inspection, stage_of_work_on_inspection_name, observation,inspection_remark) = ('"+stage_of_work_on_inspection+"', '"+stage_of_work_on_inspection_name+"', '"+observation+"', '"+inspection_remark+"')  WHERE delete_flag=0 and inspection_id = "+inspectionID+" and work_id ="+work_id, null);

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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


    @Override
    public void onClick(View v) {

    }
}
