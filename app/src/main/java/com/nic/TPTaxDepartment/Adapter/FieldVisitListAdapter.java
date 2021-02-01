package com.nic.TPTaxDepartment.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;

import static com.nic.TPTaxDepartment.Adapter.PendingScreenAdapter.db;

public class FieldVisitListAdapter extends RecyclerView.Adapter<FieldVisitListAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<CommonModel> traders;
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }

            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteRow(position);
                }
            });
            holder.upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PendingScreen)activity).jsonDatasetValues(traders.get(position).getRequest_id(),position);
                }
            });

            holder.image_list_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((PendingScreen)activity).showImageList(traders.get(position).getRequest_id());
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
        TextView name,code,taxType,current_status;
        RelativeLayout delete,upload;
        RelativeLayout image_list_icon;
        SummaryViewHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.nameValue);
            code=(TextView)view.findViewById(R.id.codeValue);
            taxType=(TextView)view.findViewById(R.id.taxTypeValue);
            delete=(RelativeLayout)view.findViewById(R.id.left);
            upload=(RelativeLayout)view.findViewById(R.id.right);
            current_status=(TextView) view.findViewById(R.id.status_filed);
            image_list_icon=(RelativeLayout) view.findViewById(R.id.image_list_layout);
        }
    }

    public void deleteRow(int position){
        Dashboard.db.delete(DBHelper.SAVE_FIELD_VISIT, "request_id" + "=?", new String[]{traders.get(position).getRequest_id()});
        Dashboard.db.delete(DBHelper.CAPTURED_PHOTO, "request_id" + "=?", new String[]{traders.get(position).getRequest_id()});
        traders.remove(position);
        notifyDataSetChanged();
        if(traders.size()>0){
        }
        else {
            ((PendingScreen)activity).noDataLayout();
        }
    }
}
