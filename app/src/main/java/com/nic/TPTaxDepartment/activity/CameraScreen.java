package com.nic.TPTaxDepartment.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.databinding .CameraScreenBinding;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.Support.MyLocationListener;
import com.nic.TPTaxDepartment.utils.CameraUtils;
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
import java.util.List;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class CameraScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    Double offlatTextValue, offlongTextValue;
    private PrefManager prefManager;
    private CameraScreenBinding cameraScreenBinding;
    private com.nic.TPTaxDepartment.dataBase.dbData dbData = new dbData(this);
    String trader_details_id ="";
    String MOBILE="";
    String screen_status="";
    String LATITUDE = "";
    String LONGITUDE = "";
    String TraderImage = "";
    String PropertyImage = "";
    String selectedTaxTypeId="";
    String assessmentNumber="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraScreenBinding = DataBindingUtil.setContentView(this, R.layout.camera_screen);
        cameraScreenBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        this.getWindow().setStatusBarColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

        intializeUI();
    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        cameraScreenBinding.imageViewPreview.setOnClickListener(this);
        cameraScreenBinding.imageView.setOnClickListener(this);
        cameraScreenBinding.backImg.setOnClickListener(this);
        cameraScreenBinding.homeImg.setOnClickListener(this);
        cameraScreenBinding.btnSave.setOnClickListener(this);


       trader_details_id =  getIntent().getStringExtra(AppConstant.TRADE_CODE);
        MOBILE =  getIntent().getStringExtra(AppConstant.MOBILE);
       screen_status =  getIntent().getStringExtra("screen_status");
       if(screen_status.equals("PropertyExist")){

       }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.btn_save :
                if(screen_status.equals("EditTraderImage")){
                    editTraderImage();
                }
                else if(screen_status.equals("PropertyExistImage")){
                    uploadPropertyExistImage();
                }else {
                    saveImage();
                }

                break;
        }
    }

    public void saveImage() {
        dbData.open();
        long id = 0; String whereClause = "";String[] whereArgs = null;
        String dcode = prefManager.getDistrictCode();
        String bcode = prefManager.getBlockCode();
        String pvcode = prefManager.getPvCode();
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageInByte = baos.toByteArray();
            image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

            byte [] encodeByte = Base64.decode(image_str.trim(),Base64.DEFAULT);
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Bitmap converetdImage = Utils.getResizedBitmap(bitmap1, 500);
            image_str=Utils.bitmapToString(converetdImage);


            ContentValues values = new ContentValues();
            values.put(AppConstant.TRADE_IMAGE,image_str.trim());
            values.put(AppConstant.LATITUDE, offlatTextValue.toString());
            values.put(AppConstant.LONGITUDE, offlongTextValue.toString());
            values.put(AppConstant.TRADE_CODE,trader_details_id);
            values.put(AppConstant.MOBILE,MOBILE);
            values.put("screen_status",screen_status);
            id = Dashboard.db.insert(DBHelper.SAVE_TRADE_IMAGE, null, values);
//
            if(id > 0){
                Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.success),Toast.LENGTH_LONG).show();
               // Toasty.success(this, "Success!", Toast.LENGTH_LONG, true).show();
                onBackPressed();
            }
            Log.d("insIdsaveImageLatLong", String.valueOf(id));

        } catch (Exception e) {
            Utils.showAlert(CameraScreen.this, getApplicationContext().getResources().getString(R.string.atleast_Capture_one_Photo));
            //e.printStackTrace();
        }
    }
    public void editTraderImage() {
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageInByte = baos.toByteArray();
            image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);
            byte [] encodeByte = Base64.decode(image_str.trim(),Base64.DEFAULT);
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Bitmap converetdImage = Utils.getResizedBitmap(bitmap1, 500);
            image_str=Utils.bitmapToString(converetdImage);

            TraderImage=image_str;
            LATITUDE=offlatTextValue.toString();
            LONGITUDE=offlongTextValue.toString();
            if(Utils.isOnline()) {
                uploadTraderImage();
            }
            else {
                Utils.showAlert(this,getApplicationContext().getResources().getString(R.string.no_internet_connection));
            }
        } catch (Exception e) {
            Utils.showAlert(CameraScreen.this, getApplicationContext().getResources().getString(R.string.atleast_Capture_one_Photo));
            //e.printStackTrace();
        }
    }

    public void uploadTraderImage() {
        try {
            new ApiService(this).makeJSONObjectRequest("TraderImage", Api.Method.POST, UrlGenerator.TradersUrl(),
                    traderImageJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject traderImageJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector),
                traderImageParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("EditTraderImage", "" + dataSet);
        return dataSet;
    }

    public JSONObject traderImageParams() throws JSONException{
        JSONObject dataSet = new JSONObject();
        JSONObject dataSet1 = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        dataSet.put(AppConstant.LATITUDE,LATITUDE);
        dataSet.put(AppConstant.LONGITUDE,LONGITUDE);
        dataSet.put(AppConstant.TRADE_IMAGE,TraderImage);
        jsonArray.put(dataSet);
        dataSet1.put(AppConstant.KEY_SERVICE_ID, "UpdateLicenseTraders");
        dataSet1.put("edit_id",trader_details_id);
        dataSet1.put("del_id","0");
        dataSet1.put(AppConstant.ATTACHMENT_FILES,jsonArray);
        Log.d("EditTraderImage", "" + dataSet);
        return dataSet1;
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
            offlongTextValue = MyLocationListener.longitude;
        }
    }

    public void getLatLong() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();
        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(CameraScreen.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(CameraScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(CameraScreen.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraScreen.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CameraUtils.checkPermissions(CameraScreen.this)) {
                        captureImage();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
//                            checkPermissionForCamera();
                } else {
                    captureImage();
                }
            } else {
                Utils.showAlert(CameraScreen.this, getApplicationContext().getResources().getString(R.string.satellite_communication_msg));
            }
        } else {
            Utils.showAlert(CameraScreen.this, getApplicationContext().getResources().getString(R.string.gPS_is_not_turned_on));
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
        builder.setTitle(getApplicationContext().getResources().getString(R.string.permissions_required))
                .setMessage(getApplicationContext().getResources().getString(R.string.camera_needs_few_permissions))
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.goto_SETTINGS), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(CameraScreen.this);
                    }
                })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void previewCapturedImage() {
        try {
            // hide video preview
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            cameraScreenBinding.imageViewPreview.setVisibility(View.GONE);
            cameraScreenBinding.imageView.setVisibility(View.VISIBLE);
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
            cameraScreenBinding.imageView.setImageBitmap(rotatedBitmap);
//            cameraScreenBinding.imageView.showImage((getImageUri(rotatedBitmap)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public Uri getImageUri( Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, getApplicationContext().getResources().getString(R.string.title), null);
        return Uri.parse(path);
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
                        getApplicationContext().getResources().getString(R.string.user_cancelled_image_capture), Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        getApplicationContext().getResources().getString(R.string.sorry_Failed_to_capture_image), Toast.LENGTH_SHORT)
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
                        getApplicationContext().getResources().getString(R.string.user_cancelled_video_recording), Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        getApplicationContext().getResources().getString(R.string.sorry_Failed_to_record_video), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status;
            if ("TraderImage".equals(urlType) && responseObj != null) {

                try {
                    String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                    String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                    Log.d("EditTraderImage", "" + userDataDecrypt);
                    JSONObject jsonObject = new JSONObject(userDataDecrypt);

                    // status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    if (status.equalsIgnoreCase("SUCCESS")) {
                        Utils.showToast(this, jsonObject.getString("MESSAGE"));
                        onBackPressed();
                        Log.d("TraderImage", "" + jsonObject);

                    } else if (status.equalsIgnoreCase("FAILD")) {
                        Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if ("SavePropertyImage".equals(urlType) && responseObj != null) {

                try {
                    String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                    String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                    Log.d("SavePropertyImage", "" + userDataDecrypt);
                    JSONObject jsonObject = new JSONObject(userDataDecrypt);

                    // status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    if (status.equalsIgnoreCase("SUCCESS")) {
                        Utils.showToast(this, jsonObject.getString("MESSAGE"));
                        onBackPressed();
                        Log.d("SavePropertyImage", "" + jsonObject);

                    } else if (status.equalsIgnoreCase("FAILD")) {
                        Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
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

    public void uploadPropertyExistImage(){
        ImageView imageView = (ImageView) findViewById(R.id.image_view);
        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageInByte = baos.toByteArray();
            image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);
            byte [] encodeByte = Base64.decode(image_str.trim(),Base64.DEFAULT);
            Bitmap bitmap1 = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Bitmap converetdImage = Utils.getResizedBitmap(bitmap1, 500);
            image_str=Utils.bitmapToString(converetdImage);

            PropertyImage=image_str;
            LATITUDE=offlatTextValue.toString();
            LONGITUDE=offlongTextValue.toString();
            prefManager.setPropertyImage(PropertyImage);
            prefManager.setPropertyImageLat(LATITUDE);
            prefManager.setPropertyImageLong(LONGITUDE);
            if(PropertyImage != null &&  !PropertyImage.equals("")){
                Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.success),Toast.LENGTH_LONG).show();
                // Toasty.success(this, "Success!", Toast.LENGTH_LONG, true).show();
                onBackPressed();
            }
            if(Utils.isOnline()) {
                propertyCaptureImageSave();
            }
            else {
                Utils.showAlert(this,getApplicationContext().getResources().getString(R.string.no_internet_connection));
            }
        } catch (Exception e) {
            Utils.showAlert(CameraScreen.this, getApplicationContext().getResources().getString(R.string.atleast_Capture_one_Photo));
            //e.printStackTrace();
        }
    }
    public void propertyCaptureImageSave() {
        try {
            new ApiService(this).makeJSONObjectRequest("SavePropertyImage", Api.Method.POST, UrlGenerator.TradersUrl(), propertyEncryptedJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject propertyEncryptedJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), propertyCaptureJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("SavePropertyImage1", "" + authKey);

        return dataSet;
    }

    public JSONObject propertyCaptureJsonParams() throws JSONException {
        assessmentNumber =  getIntent().getStringExtra(AppConstant.ASSESSMENT_NO);
        selectedTaxTypeId =  getIntent().getStringExtra(AppConstant.TAX_TYPE_ID);
        JSONObject data = new JSONObject();
        data.put(AppConstant.KEY_SERVICE_ID,"SavePropertyImage");
        data.put(AppConstant.TAX_TYPE_ID,selectedTaxTypeId);
        data.put(AppConstant.ASSESSMENT_NO,assessmentNumber);
        data.put(AppConstant.LATITUDE,LATITUDE);
        data.put(AppConstant.LONGITUDE,LONGITUDE);
        data.put(AppConstant.TRADE_IMAGE,PropertyImage);
        Log.d("SavePropertyImage2", "" + data);
        return data;
    }

}
