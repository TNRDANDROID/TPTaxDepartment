package com.nic.TPTaxDepartment.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.activity.ExistingTradeList;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import java.util.ArrayList;

public class FieldVisitListAdapter extends RecyclerView.Adapter<FieldVisitListAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<TPtaxModel> traders;
    LayoutInflater mInflater;

    public FieldVisitListAdapter( Activity activity, ArrayList<TPtaxModel> traders) {
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
            holder.name.setText(traders.get(position).getTraderName());
            holder.code.setText(traders.get(position).getAssessmentId());
            holder.taxType.setText(traders.get(position).getTaxTypeName());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
        TextView name,code,taxType;
        RelativeLayout delete,upload;

        SummaryViewHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.nameValue);
            code=(TextView)view.findViewById(R.id.codeValue);
            taxType=(TextView)view.findViewById(R.id.taxTypeValue);
            delete=(RelativeLayout)view.findViewById(R.id.left);
            upload=(RelativeLayout)view.findViewById(R.id.right);
        }
    }
}
