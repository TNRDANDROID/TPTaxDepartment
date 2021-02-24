package com.nic.TPTaxDepartment.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.model.TPtaxModel;

import java.util.ArrayList;
public class DailyCollectionAdapter extends RecyclerView.Adapter<DailyCollectionAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<TPtaxModel> taxCollection;
    LayoutInflater mInflater;

    public DailyCollectionAdapter( Activity activity, ArrayList<TPtaxModel> taxCollection) {
        this.activity=activity;
        this.taxCollection=taxCollection;
        mInflater = LayoutInflater.from(activity);
    }
    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.daily_collection_recycler, viewGroup, false);
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
            holder.taxName.setText(taxCollection.get(position).getTaxTypeName());

            if(!taxCollection.get(position).getTaxCollection() .equals("") && taxCollection.get(position).getTaxCollection() != null){
                holder.taxAmount.setText("\u20b9"+" "+taxCollection.get(position).getTaxCollection());
            }else {
                holder.taxAmount.setText("\u20b9"+" "+"00.00");
            }

        } catch (Exception exp){
            exp.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {

        return taxCollection.size();
    }
    class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView taxName,taxAmount;

        SummaryViewHolder(View view) {
            super(view);
            taxName=(TextView)view.findViewById(R.id.taxName);
            taxAmount=(TextView)view.findViewById(R.id.taxAmount);

        }
    }
}
