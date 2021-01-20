package com.nic.TPTaxDepartment.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.activity.ExistingTradeList;
import com.nic.TPTaxDepartment.activity.ExistingTradeSubmit;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import java.util.ArrayList;

public class TraderListAdapter extends RecyclerView.Adapter<TraderListAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<TPtaxModel> traders;
    LayoutInflater mInflater;

    public TraderListAdapter( Activity activity, ArrayList<TPtaxModel> traders) {
        this.activity=activity;
        this.traders=traders;
        mInflater = LayoutInflater.from(activity);
    }
    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.trade_recycler, viewGroup, false);
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
            holder.payment.setText(traders.get(position).traderPayment);
            holder.mobileValue.setText(traders.get(position).mobileno);
            holder.paymentDateValue.setText(traders.get(position).paymentdate);


            if(traders.get(position).traderPayment .equals("Paid")){
                holder.payment.setBackground(activity.getResources().getDrawable(R.drawable.round_green_bg));
            }else if(traders.get(position).traderPayment .equals("UnPaid")){
                holder.payment.setBackground(activity.getResources().getDrawable(R.drawable.round_red_bg));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String traderCode=traders.get(position).traderCode;
                    Intent intent = new Intent( activity, ExistingTradeSubmit.class);
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
        TextView name,code,payment,mobileValue,paymentDateValue;

        SummaryViewHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.nameValue);
            code=(TextView)view.findViewById(R.id.codeValue);
            payment=(TextView)view.findViewById(R.id.paymentStatus);
            mobileValue=(TextView)view.findViewById(R.id.mobileValue);
            paymentDateValue=(TextView)view.findViewById(R.id.paymentDateValue);
        }
    }
}
