package com.nic.TPTaxDepartment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.activity.ExistingTradeList;
import com.nic.TPTaxDepartment.activity.FieldVisit;
import com.nic.TPTaxDepartment.model.CommonModel;

import java.util.ArrayList;

import javax.sql.CommonDataSource;

public class FieldVisitRquestListAdapter extends RecyclerView.Adapter<FieldVisitRquestListAdapter.SummaryViewHolder> {

    ArrayList<CommonModel> commonModelArrayList;
    Context context;
    String type;

    public FieldVisitRquestListAdapter(Context context,ArrayList<CommonModel> commonModelArrayList, String type) {
        this.commonModelArrayList = commonModelArrayList;
        this.context = context;
        this.type=type;
    }

    @NonNull
    @Override
    public FieldVisitRquestListAdapter.SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.field_vist_request_list_adapter_view, viewGroup, false);
        FieldVisitRquestListAdapter.SummaryViewHolder mainHolder = new FieldVisitRquestListAdapter.SummaryViewHolder(mainGroup) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
        return mainHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull FieldVisitRquestListAdapter.SummaryViewHolder holder, int position) {
        try {
            holder.owner_name.setText(commonModelArrayList.get(position).getOwnername());
            if (type.equals("Property")){
            holder.tax_type.setText(context.getResources().getString(R.string.property_tax));
            }
            else {
                holder.tax_type.setText(context.getResources().getString(R.string.water_charges));
            }

            holder.ward_name.setText(commonModelArrayList.get(position).getWard_name_ta());
            holder.street_name.setText(commonModelArrayList.get(position).getStreet_name_ta());
            holder.request_id.setText(commonModelArrayList.get(position).getRequest_id());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FieldVisit)context).fieldRequestListClickedItemProcess(position);
                }

            });

        } catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return commonModelArrayList.size();
    }

    class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView owner_name,tax_type,ward_name,street_name,request_id;


        SummaryViewHolder(View view) {
            super(view);
            owner_name=view.findViewById(R.id.owner_name);
            tax_type=view.findViewById(R.id.tax_type_text);
            ward_name=view.findViewById(R.id.ward_name_text);
            street_name=view.findViewById(R.id.street_name_text);
            request_id=view.findViewById(R.id.request_id_text);
        }
    }

}
