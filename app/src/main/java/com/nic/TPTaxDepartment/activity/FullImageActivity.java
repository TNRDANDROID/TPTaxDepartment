package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nic.TPTaxDepartment.Fragment.SlideshowDialogFragment;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.Adapter.FullImageAdapter;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.databinding.FullImageRecyclerBinding;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FullImageActivity extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {
    private FullImageRecyclerBinding fullImageRecyclerBinding;
    public String tradecode="",status="",mobileNo="",request_id="",key="",tradeImage="";
    private FullImageAdapter fullImageAdapter;
    private PrefManager prefManager;
    private static  ArrayList<TPtaxModel> activityImage = new ArrayList<>();
    private dbData dbData = new dbData(this);
    Bitmap tradeBitmapImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullImageRecyclerBinding = DataBindingUtil.setContentView(this, R.layout.full_image_recycler);
        fullImageRecyclerBinding.setActivity(this);
        prefManager = new PrefManager(this);

        key = getIntent().getStringExtra("key");

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setLayoutManager(mLayoutManager);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setItemAnimator(new DefaultItemAnimator());
        fullImageRecyclerBinding.imagePreviewRecyclerview.setHasFixedSize(true);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setNestedScrollingEnabled(false);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setFocusable(false);
        fullImageRecyclerBinding.imagePreviewRecyclerview.setAdapter(fullImageAdapter);


        if(key.equals("FieldVisit")){
            request_id = getIntent().getStringExtra("request_id");
            new fetchFieldVisitImagetask().execute();
        }else  if(key.equals("ExistTradeViewClass")){
            tradeImage = getIntent().getStringExtra(AppConstant.TRADE_IMAGE);
            new fetchTradeImagetask().execute();
        }else  if(key.equals("FieldVisitedImage")){
            tradeImage = getIntent().getStringExtra(AppConstant.TRADE_IMAGE);
            new fetchFieldVisitedImagetask().execute();
        }else  if(key.equals("ExistingTradeSubmit")){
            tradecode = getIntent().getStringExtra(AppConstant.TRADE_CODE);
            mobileNo = getIntent().getStringExtra(AppConstant.MOBILE);
            status = getIntent().getStringExtra(AppConstant.KEY_SCREEN_STATUS);
            new fetchImagetask().execute();
        }else  if(key.equals("NewTradeLicence")){
            tradecode = getIntent().getStringExtra(AppConstant.TRADE_CODE);
            mobileNo = getIntent().getStringExtra(AppConstant.MOBILE);
            status = getIntent().getStringExtra(AppConstant.KEY_SCREEN_STATUS);
            new fetchImagetask().execute();
        }else {
        }

//        if(OnOffType.equalsIgnoreCase("Online")) {
//            if (Utils.isOnline()) {
//                getOnlineImage();
//            }
//        }
//        else {
//            new fetchImagetask().execute();
//        }

    }
    public class fetchImagetask extends AsyncTask<Void, Void,
            ArrayList<TPtaxModel>> {
        @Override
        protected ArrayList<TPtaxModel> doInBackground(Void... params) {

            final String dcode = prefManager.getDistrictCode();
            final String bcode = prefManager.getBlockCode();
            final String pvcode = prefManager.getPvCode();
            String type_of_work = "", cd_work_no = "", work_type_flag_le = "";

            dbData.open();
            activityImage = new ArrayList<>();
            if(status.equals("pending")) {
                //activityImage = dbData.selectPendingImage(mobileNo);
            }else {
                activityImage = dbData.selectImage(mobileNo,status);
            }


            Log.d("IMAGE_COUNT", String.valueOf(activityImage.size()));
            return activityImage;
        }

        @Override
        protected void onPostExecute(final ArrayList<TPtaxModel> imageList) {
            super.onPostExecute(imageList);
            setAdapter();
        }
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public class fetchTradeImagetask extends AsyncTask<Void, Void,
            ArrayList<TPtaxModel>> {
        @Override
        protected ArrayList<TPtaxModel> doInBackground(Void... params) {
            dbData.open();
            activityImage = new ArrayList<>();
           if(tradeImage != null){
               TPtaxModel card = new TPtaxModel();
               card.setImage(StringToBitMap(tradeImage));
               activityImage.add(card);
           }

            Log.d("IMAGE_COUNT", String.valueOf(activityImage.size()));
            return activityImage;
        }

        @Override
        protected void onPostExecute(final ArrayList<TPtaxModel> imageList) {
            super.onPostExecute(imageList);
            setAdapter();
        }
    }
    public class fetchFieldVisitedImagetask extends AsyncTask<Void, Void,
            ArrayList<TPtaxModel>> {
        @Override
        protected ArrayList<TPtaxModel> doInBackground(Void... params) {
            dbData.open();
            activityImage = new ArrayList<>();
           if(tradeImage != null){
               TPtaxModel card = new TPtaxModel();
               card.setImage(StringToBitMap(tradeImage));
               activityImage.add(card);
           }

            Log.d("IMAGE_COUNT", String.valueOf(activityImage.size()));
            return activityImage;
        }

        @Override
        protected void onPostExecute(final ArrayList<TPtaxModel> imageList) {
            super.onPostExecute(imageList);
            setAdapter();
        }
    }
    public class fetchFieldVisitImagetask extends AsyncTask<Void, Void,
            ArrayList<TPtaxModel>> {
        @Override
        protected ArrayList<TPtaxModel> doInBackground(Void... params) {

            dbData.open();
            activityImage = new ArrayList<>();
            activityImage = dbData.selectFieldVisitImage(request_id);
            Log.d("IMAGE_COUNT", String.valueOf(activityImage.size()));
            return activityImage;
        }

        @Override
        protected void onPostExecute(final ArrayList<TPtaxModel> imageList) {
            super.onPostExecute(imageList);
            setAdapter();
        }
    }

    public void homePage() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
//        Intent intent = new Intent(this, HomePage.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("Home", "Home");
//        startActivity(intent);
//        super.onBackPressed();
//        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!key.equals("FieldVisit")){
            setResult(Activity.RESULT_CANCELED);
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
        else {
            setResult(Activity.RESULT_OK,new Intent().putExtra("Data","FieldVisit"));
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }    }

    public void onBackPress() {
        super.onBackPressed();
        if (!key.equals("FieldVisit")){
            setResult(Activity.RESULT_CANCELED);
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
        else {
            setResult(Activity.RESULT_OK,new Intent().putExtra("Data","FieldVisit"));
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }

    }

    @Override
    public void onClick(View view) {

    }

    public void getOnlineImage() {
//        try {
//            new ApiService(this).makeJSONObjectRequest("OnlineImage", Api.Method.POST, UrlGenerator.getWorkListUrl(), ImagesJsonParams(), "not cache", this);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public JSONObject ImagesJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), ImagesListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("utils_ImageEncrydataSet", "" + authKey);
        return dataSet;
    }

    public JSONObject ImagesListJsonParams() throws JSONException {
       JSONObject dataSet = new JSONObject();
//        if(getIntent().getStringExtra(AppConstant.TYPE_OF_WORK).equalsIgnoreCase(AppConstant.MAIN_WORK)){
//            dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ONLINE_IMAGE_MAIN_WORK_SERVICE_ID);
//        }else {
//            dataSet.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ONLINE_IMAGE_ADDITIONAL_WORK_SERVICE_ID);
//            dataSet.put(AppConstant.CD_PORT_WORK_ID_ONLINE_IMAGE, getIntent().getStringExtra(AppConstant.CD_WORK_NO));
//        }
//        dataSet.put(AppConstant.DISTRICT_CODE, getIntent().getStringExtra(AppConstant.DISTRICT_CODE));
//        dataSet.put(AppConstant.BLOCK_CODE, getIntent().getStringExtra(AppConstant.BLOCK_CODE));
//        dataSet.put(AppConstant.PV_CODE, getIntent().getStringExtra(AppConstant.PV_CODE));
//        dataSet.put(AppConstant.WORK_ID, getIntent().getStringExtra(AppConstant.WORK_ID));
//        dataSet.put(AppConstant.WORK_TYPE_FLAG_LE,  getIntent().getStringExtra(AppConstant.WORK_TYPE_FLAG_LE));
//        Log.d("utils_imageDataset", "" + dataSet);
       return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("OnlineImage".equals(urlType) && responseObj != null) {
                String key =  Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if ( Utils.NotNullString(jsonObject.getString("STATUS")).equalsIgnoreCase("OK") &&  Utils.NotNullString(jsonObject.getString("RESPONSE")).equalsIgnoreCase("OK")) {
                    generateImageArrayList(jsonObject.getJSONArray(AppConstant.JSON_DATA));
                    Log.d("Length", "" + jsonObject.getJSONArray(AppConstant.JSON_DATA).length());
                }
                Log.d("resp_OnlineImage", "" + responseDecryptedBlockKey);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void generateImageArrayList(JSONArray jsonArray){
        if(jsonArray.length() > 0){
            activityImage = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++ ) {
//                try {
//                    TPtaxModel imageOnline = new TPtaxModel();
////                    imageOnline.setImageRemark(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE_REMARK));
//                    imageOnline.setWorkStageName(jsonArray.getJSONObject(i).getString(AppConstant.WORK_SATGE_NAME));
//                    if (!(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE).equalsIgnoreCase("null") || jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE).equalsIgnoreCase(""))) {
//                        byte[] decodedString = Base64.decode(jsonArray.getJSONObject(i).getString(AppConstant.KEY_IMAGE), Base64.DEFAULT);
//                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                        imageOnline.setImage(decodedByte);
//                        activityImage.add(imageOnline);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }

            setAdapter();
        }
    }
    public void slideShow( Bundle bundle){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
        newFragment.setArguments(bundle);
        newFragment.show(ft, "slideshow");
    }
    public void setAdapter(){
        fullImageAdapter = new FullImageAdapter(FullImageActivity.this,
                activityImage, dbData,key);
/*
        fullImageRecyclerBinding.imagePreviewRecyclerview.addOnItemTouchListener(new FullImageAdapter.RecyclerTouchListener(getApplicationContext(), fullImageRecyclerBinding.imagePreviewRecyclerview, new FullImageAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", activityImage);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
*/
        fullImageRecyclerBinding.imagePreviewRecyclerview.setAdapter(fullImageAdapter);
    }
    @Override
    public void OnError(VolleyError volleyError) {
        Utils.showAlert(this, getApplicationContext().getResources().getString(R.string.try_after_some_time));

    }
}
