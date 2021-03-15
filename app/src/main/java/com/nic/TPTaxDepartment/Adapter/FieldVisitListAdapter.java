package com.nic.TPTaxDepartment.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.activity.Dashboard;
import com.nic.TPTaxDepartment.activity.ExistingTradeList;
import com.nic.TPTaxDepartment.activity.FieldVisit;
import com.nic.TPTaxDepartment.activity.FullImageActivity;
import com.nic.TPTaxDepartment.activity.PendingScreen;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.TPtaxModel;

import java.io.Serializable;
import java.util.ArrayList;

import static com.nic.TPTaxDepartment.activity.Dashboard.db;


public class FieldVisitListAdapter extends RecyclerView.Adapter<FieldVisitListAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<CommonModel> traders;
    private ArrayList<CommonModel> selectedTraders;
    LayoutInflater mInflater;

    public FieldVisitListAdapter( Activity activity, ArrayList<CommonModel> traders) {
        this.activity=activity;
        this.traders=traders;
        mInflater = LayoutInflater.from(activity);
    }
    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.field_visit_recycler, viewGroup, false);
        SummaryViewHolder mainHolder = new SummaryViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return mainHolder;
    }
    @Override
    public void onBindViewHolder(final SummaryViewHolder holder,final int position) {

        try {
            holder.name.setText(traders.get(position).getOwnername());
            holder.code.setText(traders.get(position).getRequest_id());
            holder.taxType.setText(traders.get(position).getTaxtypedesc_en());
            holder.current_status.setText(traders.get(position).getFIELD_VISIT_STATUS());
            holder.image_list_icon.setImageBitmap(getImage(traders.get(position).getRequest_id(),traders.get(position).getData_ref_id()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }

            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showConfirmationAlert(position,activity,activity.getResources().getString(R.string.are_you_sure_you_want_to_delete));
                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedTraders=new ArrayList<>();
                    selectedTraders.add(traders.get(position));
                    ((PendingScreen)activity).loadFieldList(selectedTraders);
                }
            });
            holder.upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PendingScreen)activity).jsonDatasetValues(traders.get(position).getRequest_id(),traders.get(position).getData_ref_id(),position);
                }
            });

            holder.image_list_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PendingScreen)activity).showImageList(traders.get(position).getRequest_id(),traders.get(position).getData_ref_id());
                }
            });

        } catch (Exception exp){
            exp.printStackTrace();
        }
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {

        return traders.size();
    }
    class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView name,code,taxType,current_status,upload;
        RelativeLayout edit_layout,image_list_icon_rl;
        ImageView image_list_icon,delete,edit;
        SummaryViewHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.nameValue);
            code=(TextView)view.findViewById(R.id.codeValue);
            taxType=(TextView)view.findViewById(R.id.taxTypeValue);
            delete=(ImageView)view.findViewById(R.id.delete);
            edit=(ImageView)view.findViewById(R.id.edit);
            //edit_layout=(RelativeLayout)view.findViewById(R.id.edit_layout);
            upload=(TextView) view.findViewById(R.id.upload);
            current_status=(TextView) view.findViewById(R.id.status_filed);
            image_list_icon=(ImageView) view.findViewById(R.id.image_list);

        }
    }

    public void deleteRow(int position){
        db.delete(DBHelper.SAVE_FIELD_VISIT, "request_id" + "=?", new String[]{traders.get(position).getRequest_id()});
        db.delete(DBHelper.CAPTURED_PHOTO, "request_id" + "=?"+ " and  data_ref_id" + "=?", new String[]{traders.get(position).getData_ref_id()});
        traders.remove(position);
        notifyDataSetChanged();
        if(traders.size()>0){
        }
        else {
            ((PendingScreen)activity).noDataLayout();
        }
    }


    public Bitmap getImage(String request_id,String data_ref_id) {
        Bitmap decodedByte = null;
        db.isOpen();
        ArrayList<TPtaxModel> cards = new ArrayList<>();
        Cursor cursor = null;
        String sql = "SELECT * FROM " + DBHelper.CAPTURED_PHOTO + " WHERE request_id ="+request_id+ " AND data_ref_id ="+data_ref_id ;

        try {
            cursor=db.rawQuery(sql,null,null);
            int count = cursor.getCount();

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.FIELD_IMAGE));
                        byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    }while(cursor.moveToNext());
                }
            }

        } catch (Exception e){
            e.printStackTrace();
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }

        return decodedByte;
    }



 public void showConfirmationAlert(int pos,Activity activity, String msg) {

        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
            Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
            btn_cancel.setVisibility(View.VISIBLE);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRow(pos);
                    dialog.dismiss();

                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
