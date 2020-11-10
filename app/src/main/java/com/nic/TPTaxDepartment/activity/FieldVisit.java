package com.nic.TPTaxDepartment.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.Support.MyCustomTextView;
import com.nic.TPTaxDepartment.Support.MyLocationListener;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.databinding.AssessmentStatusBinding;
import com.nic.TPTaxDepartment.databinding.FieldVisitBinding;
import com.nic.TPTaxDepartment.utils.FontCache;
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
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class FieldVisit extends AppCompatActivity {

    private FieldVisitBinding fieldVisitBinding;
    private List<String> Current_Status = new ArrayList<>();
    private boolean imageboolean;
    String offlatTextValue, offlanTextValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fieldVisitBinding = DataBindingUtil.setContentView(this, R.layout.field_visit);
        fieldVisitBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        loadCurrentStatus();

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

    public void imageWithDescription(final MyCustomTextView action_tv, final String type, final ScrollView scrollView) {
//        imageboolean = true;
//        dataset = new JSONObject();
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//        work_id = getIntent().getStringExtra(AppConstant.WORK_ID);
//        String stage_of_work_on_inspection = stageListValues.get(sp_stage.getSelectedItemPosition()).getWorkStageCode();
//        String stage_of_work_on_inspection_name = stageListValues.get(sp_stage.getSelectedItemPosition()).getWorkStageName();
//        String date_of_inspection = sdf.format(new Date());
//        String inspected_by = "inspected_by";
//        int observation = observationList.get(sp_observation.getSelectedItemPosition()).getObservationID();
//        String inspection_remark = remarkTv.getText().toString();
//        String created_date = date_of_inspection;
//        String created_ipaddress = "123214124";
//        String created_username = "test";
//
//        if (!Utils.isOnline()) {
//            ContentValues inspectionValue = new ContentValues();
//            inspectionValue.put(AppConstant.WORK_ID, work_id);
//            inspectionValue.put(AppConstant.STAGE_OF_WORK_ON_INSPECTION, stage_of_work_on_inspection);
//            inspectionValue.put(AppConstant.STAGE_OF_WORK_ON_INSPECTION_NAME, stage_of_work_on_inspection_name);
//            inspectionValue.put(AppConstant.DATE_OF_INSPECTION, date_of_inspection);
//            //  inspectionValue.put(AppConstant.INSPECTED_BY,inspected_by);
//            inspectionValue.put(AppConstant.OBSERVATION, observation);
//            inspectionValue.put(AppConstant.INSPECTION_REMARK, inspection_remark);
//            inspectionValue.put(AppConstant.CREATED_DATE, created_date);
//            inspectionValue.put(AppConstant.CREATED_IMEI_NO, prefManager.getIMEI());
//            inspectionValue.put(AppConstant.CREATED_USER_NAME, prefManager.getUserName());
//            inspectionValue.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCode());
//            if(prefManager.getLevels().equalsIgnoreCase("D")) {
//                inspectionValue.put("level", "D");
//            }else if(prefManager.getLevels().equalsIgnoreCase("S")) {
//                inspectionValue.put("level", "S");
//            }
//            inspectionValue.put("delete_flag", 0);
//
//            LoginScreen.db.insert(DBHelper.INSPECTION_PENDING, null, inspectionValue);
//        } else {
//            try {
//                dataset.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_HIGH_VALUE_PROJECT_INSPECTION_SAVE);
//                dataset.put(AppConstant.WORK_ID, work_id);
//                dataset.put(AppConstant.DATE_OF_INSPECTION, date_of_inspection);
//                // dataset.put(AppConstant.INSPECTED_BY, inspected_by);
//                dataset.put(AppConstant.CREATED_DATE, created_date);
//                dataset.put(AppConstant.CREATED_IMEI_NO, prefManager.getIMEI());
//                dataset.put(AppConstant.CREATED_USER_NAME, prefManager.getUserName());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

 //       }


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
        done.setGravity(Gravity.CENTER);
        done.setVisibility(View.VISIBLE);
        done.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.HEAVY));

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
                            offlatTextValue = "" + MyLocationListener.latitude;
                            offlanTextValue = "" + MyLocationListener.longitude;
                        }

                        // Toast.makeText(getApplicationContext(),str,Toast.LENGTH_LONG).show();

                        ContentValues imageValue = new ContentValues();

//                        imageValue.put(AppConstant.INSPECTION_ID, inspectionID);
//                        imageValue.put(AppConstant.WORK_ID, work_id);
//                        imageValue.put(AppConstant.LATITUDE, offlatTextValue);
//                        imageValue.put(AppConstant.LONGITUDE, offlanTextValue);
//                        imageValue.put(AppConstant.IMAGE, image_str.trim());
//                        imageValue.put(AppConstant.DESCRIPTION, description);
//                        imageValue.put("pending_flag", 1);
//                        if(prefManager.getLevels().equalsIgnoreCase("D")) {
//                            imageValue.put("level", "D");
//                        }else if(prefManager.getLevels().equalsIgnoreCase("S")) {
//                            imageValue.put("level", "S");
//                        }


                        if (!Utils.isOnline()) {
                        //    long rowInserted = LoginScreen.db.insert(DBHelper.CAPTURED_PHOTO, null, imageValue);

//                            if (rowInserted != -1) {
//                                Toast.makeText(AddInspectionReportScreen.this, "New Inspection added", Toast.LENGTH_SHORT).show();
//                                Dashboard.getPendingCount();
//                               finish();
//                            } else {
//                                Toast.makeText(AddInspectionReportScreen.this, "Something wrong", Toast.LENGTH_SHORT).show();
//                            }
                        } else {
                            imageArray.put(i);
                         //   imageArray.put(work_id);
                            imageArray.put(offlatTextValue);
                            imageArray.put(offlanTextValue);
                            imageArray.put(image_str.trim());
                            imageArray.put(description);
                            imageJson.put(imageArray);
                        }

                      //  long localImageInserted = LoginScreen.db.insert(DBHelper.LOCAL_IMAGE, null, imageValue);
                    }
                    try {
                        dataset.put("image_details", imageJson);

                        Log.d("post_dataset_inspection", dataset.toString());
                        //   String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataset.toString());
//                        String authKey = dataset.toString();
//                        int maxLogSize = 1000;
//                        for(int i = 0; i <= authKey.length() / maxLogSize; i++) {
//                            int start = i * maxLogSize;
//                            int end = (i+1) * maxLogSize;
//                            end = end > authKey.length() ? authKey.length() : end;
//                            Log.v("to_send", authKey.substring(start, end));
//                     }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                dialog.dismiss();
                focusOnView(scrollView, action_tv);


            }
        });
        final String values = action_tv.getText().toString().replace("NA", "");
        Button btnAddMobile = (Button) dialog.findViewById(R.id.btn_add);
        btnAddMobile.setTypeface(FontCache.getInstance(this).getFont(FontCache.Font.MEDIUM));
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
                        updateView(this, mobileNumberLayout,String.valueOf(i), "localImage");
                        //i++;
//                    } while (imageList.moveToNext());
//                }
//                try {
//                    db.delete(DBHelper.LOCAL_IMAGE, null, null);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }


//            if (values.contains(",")) {
//                String[] mobileOrEmail = values.split(",");
//                for (int i = 0; i < mobileOrEmail.length; i++) {
//                    if (viewArrayList.size() < 5) {
//                        updateView(this, mobileNumberLayout, mobileOrEmail[i], type);
//                    }
//                }
//            }
            else {
                if (viewArrayList.size() < 5) {
                    updateView(this, mobileNumberLayout, values, type);
                }
            }
        } else {
            updateView(this, mobileNumberLayout, values, type);
        }
    }

    private final void focusOnView(final ScrollView your_scrollview, final MyCustomTextView your_EditBox) {
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
        if ("localImage".equalsIgnoreCase(type)) {
            int i = Integer.parseInt(values);
            if (!values.isEmpty()) {
                myEditTextView.setText(imagelistvalues.get(i).getDescription());

                image_view_preview.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(imagelistvalues.get(i).getImage());

            }
        }
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
        if (ContextCompat.checkSelfPermission(AddInspectionReportScreen.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(AddInspectionReportScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddInspectionReportScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(AddInspectionReportScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddInspectionReportScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddInspectionReportScreen.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CameraUtils.checkPermissions(AddInspectionReportScreen.this)) {
                        captureImage();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
//                            checkPermissionForCamera();
                } else {
                    captureImage();
                }
            } else {
                Utils.showAlert(AddInspectionReportScreen.this, "Satellite communication not available to get GPS Co-ordination Please Capture Photo in Open Area..");
            }
        } else {
            Utils.showAlert(AddInspectionReportScreen.this, "GPS is not turned on...");
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
                        CameraUtils.openSettings(AddInspectionReportScreen.this);
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


}
