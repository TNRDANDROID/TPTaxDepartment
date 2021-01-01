package com.nic.TPTaxDepartment.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nic.TPTaxDepartment.activity.FullImageActivity;
import com.nic.TPTaxDepartment.activity.PendingScreen;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.model.TPtaxModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PendingScreenAdapter extends RecyclerView.Adapter<PendingScreenAdapter.MyViewHolder> {

    private com.nic.TPTaxDepartment.dataBase.dbData dbData;
    private Context context;
    private List<TPtaxModel> pendingListValues;
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    JSONObject datasetActivity = new JSONObject();

    public PendingScreenAdapter(Context context, List<TPtaxModel> pendingListValues, dbData dbData) {
        try {
            dbHelper = new DBHelper(context);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.context = context;
        this.pendingListValues = pendingListValues;
        this.dbData = dbData;
        prefManager = new PrefManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_screen_adapter, parent, false);
        return new PendingScreenAdapter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tax_type_id, assessment_id,current_status;
        private RelativeLayout upload,view_image_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tax_type_id = (TextView) itemView.findViewById(R.id.tax_type_id);
            assessment_id = (TextView) itemView.findViewById(R.id.assessment_id);
            current_status = (TextView) itemView.findViewById(R.id.current_status);
            upload = (RelativeLayout) itemView.findViewById(R.id.upload);
            view_image_layout = (RelativeLayout) itemView.findViewById(R.id.view_image_layout);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {

            }
        }
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tax_type_id.setText(String.valueOf(pendingListValues.get(position).getTaxTypeId()));
        holder.assessment_id.setText(String.valueOf(pendingListValues.get(position).getAssessmentId()));
        holder.current_status.setText(String.valueOf(pendingListValues.get(position).getCurrentStatus()));

//        final Integer scheduleId = pendingListValues.get(position).getScheduleId();
//        final Integer activityId = pendingListValues.get(position).getActivityId();
//        final Integer scheduleMasterId = pendingListValues.get(position).getScheduleMasterId();
        holder.view_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewImageScreen(pendingListValues.get(position).getTaxTypeId());
            }
        });

        holder.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline()) {
                    TPtaxModel roadListValue = new TPtaxModel();

//                    roadListValue.setScheduleId(scheduleId);
//                    roadListValue.setActivityId(activityId);

//                    ArrayList<ODFMonitoringListValue> activityImage1 = dbData.selectImageActivity(dcode, bcode, pvcode, String.valueOf(scheduleId), String.valueOf(activityId), "End");
//
//                    if (activityImage1.size() > 0) {
//                        prefManager.setDeletedkeyList(roadListValue);
//                        new toUploadActivityTask().execute(roadListValue);
//                    }
//                    else {
//                        Utils.showAlert((Activity) context, "Activity incomplete!");
//                    }
                } else {
                    Activity activity = (Activity) context;
                    Utils.showAlert(activity, "Turn On Mobile Data To Synchronize!");
                }
            }
        });

    }

    public void viewImageScreen(String taxtypeid) {
        Intent intent = new Intent(context, FullImageActivity.class);
        intent.putExtra(AppConstant.TAX_TYPE_ID,taxtypeid);
        intent.putExtra(AppConstant.KEY_SCREEN_STATUS,"pending");
        context.startActivity(intent);
    }

//    public class toUploadActivityTask extends AsyncTask<ODFMonitoringListValue, Void,
//            JSONObject> {
//        @Override
//        protected JSONObject doInBackground(ODFMonitoringListValue... values) {
//            try {
//                dbData.open();
//                ArrayList<ODFMonitoringListValue> saveActivityLists = dbData.getSavedActivity("upload",values[0]);
//                JSONArray saveAcivityArray = new JSONArray();
//                if (saveActivityLists.size() > 0) {
//                    for (int i = 0; i < saveActivityLists.size(); i++) {
//                        JSONObject activityJson = new JSONObject();
//                        activityJson.put(AppConstant.KEY_MOTIVATOR_ID, saveActivityLists.get(i).getMotivatorId());
//                        activityJson.put(AppConstant.KEY_SCHEDULE_ID, saveActivityLists.get(i).getScheduleId());
//                        activityJson.put(AppConstant.KEY_ACTIVITY_ID, saveActivityLists.get(i).getActivityId());
//                        activityJson.put(AppConstant.KEY_SCHEDULE_MASTER_ID, saveActivityLists.get(i).getScheduleMasterId());
//                        activityJson.put(AppConstant.DISTRICT_CODE, String.valueOf(saveActivityLists.get(i).getDistictCode()));
//                        activityJson.put(AppConstant.BLOCK_CODE, saveActivityLists.get(i).getBlockCode());
//                        activityJson.put(AppConstant.PV_CODE, saveActivityLists.get(i).getPvCode());
//                        activityJson.put(AppConstant.KEY_LATITUDE, saveActivityLists.get(i).getLatitude());
//                        activityJson.put(AppConstant.KEY_LONGITUDE, saveActivityLists.get(i).getLongitude());
//                        activityJson.put(AppConstant.KEY_TYPE, saveActivityLists.get(i).getType());
//                        activityJson.put(AppConstant.KEY_DATE_TIME, saveActivityLists.get(i).getDateTime());
//                        activityJson.put(AppConstant.KEY_IMAGE_REMARK, saveActivityLists.get(i).getImageRemark());
//                        activityJson.put(AppConstant.KEY_SERIAL_NUMBER, saveActivityLists.get(i).getSerialNo());
//
//                        Bitmap bitmap = saveActivityLists.get(i).getImage();
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
//                        byte[] imageInByte = baos.toByteArray();
//                        String image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);
//
//                        activityJson.put(AppConstant.KEY_IMAGE,image_str);
//
//                        saveAcivityArray.put(activityJson);
//                    }
//                }
//
//                datasetActivity = new JSONObject();
//                try {
//                    datasetActivity.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_ACTIVITY_IMAGE_SAVE);
//                    datasetActivity.put(AppConstant.KEY_TRACK_DATA, saveAcivityArray);
//
//                    String authKey = datasetActivity.toString();
//                    int maxLogSize = 2000;
//                    for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
//                        int start = i * maxLogSize;
//                        int end = (i + 1) * maxLogSize;
//                        end = end > authKey.length() ? authKey.length() : end;
//                        Log.v("to_send_plain", authKey.substring(start, end));
//                    }
//
//                    String authKey1 = Utils.encrypt(prefManager.getUserPassKey(), context.getResources().getString(R.string.init_vector), datasetActivity.toString());
//
//                    for(int i = 0; i <= authKey1.length() / maxLogSize; i++) {
//                        int start = i * maxLogSize;
//                        int end = (i+1) * maxLogSize;
//                        end = end > authKey.length() ? authKey1.length() : end;
//                        Log.v("to_send_encryt", authKey1.substring(start, end));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return datasetActivity;
//        }
//
//        protected void onPostExecute(JSONObject dataset) {
//            super.onPostExecute(dataset);
//            ((PendingScreen)context).syncData(dataset);
//        }
//    }
    @Override
    public int getItemCount() {
        return pendingListValues.size();
    }

}

