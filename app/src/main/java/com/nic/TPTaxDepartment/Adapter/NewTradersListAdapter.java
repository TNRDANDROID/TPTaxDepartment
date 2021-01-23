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
import com.nic.TPTaxDepartment.activity.ExistingTradeList;
import com.nic.TPTaxDepartment.activity.NewTradeLicenceScreen;
import com.nic.TPTaxDepartment.model.TPtaxModel;

import java.io.Serializable;
import java.util.ArrayList;

public class NewTradersListAdapter extends RecyclerView.Adapter<NewTradersListAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<TPtaxModel> traders;
    LayoutInflater mInflater;

    public NewTradersListAdapter( Activity activity, ArrayList<TPtaxModel> traders) {
        this.activity=activity;
        this.traders=traders;
        mInflater = LayoutInflater.from(activity);
    }
    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.new_trader_recycler, viewGroup, false);
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
            holder.name.setText(traders.get(position).traderName);
            holder.code.setText(traders.get(position).traderCode);
            holder.mobileValue.setText(traders.get(position).mobileno);
            holder.date.setText(traders.get(position).trade_date);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( activity, NewTradeLicenceScreen.class);
                    intent.putExtra("flag",true);
                    intent.putExtra("position",position);
                    intent.putExtra("tradersList", (Serializable) traders);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in, R.anim.slide_out);


                }

            });

        } catch (Exception exp){
            exp.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {

        return traders.size();
    }
    class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView name,code,mobileValue,date;
        RelativeLayout delete,upload;

        SummaryViewHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.nameValue);
            code=(TextView)view.findViewById(R.id.codeValue);
            mobileValue=(TextView)view.findViewById(R.id.mobileValue);
            date=(TextView)view.findViewById(R.id.date);
            delete=(RelativeLayout)view.findViewById(R.id.left);
            upload=(RelativeLayout)view.findViewById(R.id.right);
        }
    }
}
