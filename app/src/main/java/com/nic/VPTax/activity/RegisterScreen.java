package com.nic.VPTax.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.VolleyError;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.VPTax.Adapter.CommonAdapter;
import com.nic.VPTax.Api.Api;
import com.nic.VPTax.Api.ApiService;
import com.nic.VPTax.Api.ServerResponse;
import com.nic.VPTax.R;
import com.nic.VPTax.constant.AppConstant;
import com.nic.VPTax.databinding.RegisterScreenBinding;
import com.nic.VPTax.model.VPtaxModel;
import com.nic.VPTax.session.PrefManager;
import com.nic.VPTax.utils.CameraUtils;
import com.nic.VPTax.utils.UrlGenerator;
import com.nic.VPTax.utils.Utils;
import com.nic.VPTax.windowpreferences.WindowPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {
    RegisterScreenBinding registerScreenBinding;
    private List<String> GenderList = new ArrayList<>();
    private List<VPtaxModel> District = new ArrayList<>();
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerScreenBinding = DataBindingUtil.setContentView(this, R.layout.register_screen);
        registerScreenBinding.setActivity(this);
        prefManager = new PrefManager(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mApps);
        registerScreenBinding.district.setAdapter(adapter);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        registerScreenBinding.scrollView.setNestedScrollingEnabled(true);
        loadGenderList();

        String colored = "*";
        String mobileView= "கைபேசி எண் / Mobile No";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(mobileView);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        registerScreenBinding.mobileHint.setText(builder);

        registerScreenBinding.district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        registerScreenBinding.gender.setAdapter(RuralUrbanArray);

    }

    @Override
    public void onClick(View v) {

    }

//    public void signUP() {
//        try {
//            new ApiService(this).makeJSONObjectRequest("Register", Api.Method.POST, UrlGenerator.getMotivatorCategory(), dataTobeSavedJsonParams(), "not cache", this);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    public JSONObject dataTobeSavedJsonParams() throws JSONException {

        byte[] imageInByte = new byte[0];
        String image_str = "";
        try {
            Bitmap bitmap = ((BitmapDrawable) registerScreenBinding.profileImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            imageInByte = baos.toByteArray();
            image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject dataSet = new JSONObject();
//        dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_REGISTER_SIGNUP);
        dataSet.put(AppConstant.NAME, registerScreenBinding.username.getText().toString());
        dataSet.put(AppConstant.FATHER_HUSBAND_NAME, registerScreenBinding.fatherName.getText().toString());
        dataSet.put(AppConstant.GENDER, registerScreenBinding.gender.getSelectedItemPosition());
        dataSet.put(AppConstant.MOBILE, registerScreenBinding.mobileNo.getText().toString());
        dataSet.put(AppConstant.E_MAIL, registerScreenBinding.email.getText().toString());
        dataSet.put(AppConstant.ADDRESS, registerScreenBinding.address.getText().toString());

        dataSet.put(AppConstant.DISTRICT_CODE, prefManager.getDistrictCode());

        Log.d("RegisterDataSet", "" + dataSet);
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
            if ("Register".equals(urlType) && responseObj != null) {
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
        if (registerScreenBinding.profileImage.getDrawable() != null) {
            if (!registerScreenBinding.username.getText().toString().isEmpty()) {
                if (!registerScreenBinding.fatherName.getText().toString().isEmpty()) {
                    if (!"Select Gender".equalsIgnoreCase(GenderList.get(registerScreenBinding.gender.getSelectedItemPosition()))) {
                        if (!registerScreenBinding.mobileNo.getText().toString().isEmpty()) {
                            if (Utils.isValidMobile(registerScreenBinding.mobileNo.getText().toString())) {
                                if (!registerScreenBinding.email.getText().toString().isEmpty()) {
                                    if (Utils.isEmailValid(registerScreenBinding.email.getText().toString())) {
                                        if (!registerScreenBinding.address.getText().toString().isEmpty()) {
//                                            if (!"Select District".equalsIgnoreCase(District.get(registerScreenBinding.district.getSelectedItemPosition()).getDistrictName())) {
                                                Utils.showAlert(this, " "+GenderList.get(registerScreenBinding.gender.getSelectedItemPosition()));
//                                            } else {
//                                                Utils.showAlert(this, "உங்கள் மாவட்டத்தைத் தேர்ந்தெடுக்கவும்!");
//                                            }
                                        } else {
                                            Utils.showAlert(this, "உங்கள் முகவரியை உள்ளிடவும்!");
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
                        Utils.showAlert(this, "உங்கள் பாலினத்தைத் தேர்ந்தெடுக்கவும்!");
                    }

                } else {
                    Utils.showAlert(this, "உங்கள் தந்தை / கணவர் பெயரை உள்ளிடவும்!");
                }
            } else {
                Utils.showAlert(this, "உங்கள் பெயரை உள்ளிடவும்!");
            }
        } else {
            Utils.showAlert(this, "முதலில் சுயவிவரப் படத்தைப் பிடிக்கவும்!!");
        }
    }

    public void getCameraPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CameraUtils.checkPermissions(RegisterScreen.this)) {
                captureImage();
            } else {
                requestCameraPermission(MEDIA_TYPE_IMAGE);
            }
        } else {
            captureImage();
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
                        CameraUtils.openSettings(RegisterScreen.this);
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
            registerScreenBinding.profileImagePreview.setVisibility(View.GONE);
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
            registerScreenBinding.profileImage.setImageBitmap(rotatedBitmap);
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
//        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                // Refreshing the gallery
//                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
//
//                // video successfully recorded
//                // preview the recorded video
////                previewVideo();
//            } else if (resultCode == RESULT_CANCELED) {
//                // user cancelled recording
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled video recording", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                // failed to record video
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
//                        .show();
//            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

}





