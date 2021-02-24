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
            holder.name.setText(traders.get(position).getTraderName());
            holder.code.setText(traders.get(position).getTraderCode());

//            holder.payment.setText(traders.get(position).paymentStatus);
            holder.mobileValue.setText(traders.get(position).getMobileno());
            holder.paymentDateValue.setText(traders.get(position).getPaymentdate());
            holder.traderType.setText(traders.get(position).getTraders_license_type_name());
            holder.emailValue.setText(traders.get(position).getEmail());


           /* if(traders.get(position).paymentStatus .equals("Paid")){
                holder.payment.setBackground(activity.getResources().getDrawable(R.drawable.round_green_bg));
            }else if(traders.get(position).paymentStatus .equals("UnPaid")){
                holder.payment.setBackground(activity.getResources().getDrawable(R.drawable.round_red_bg));
            }
            else {
                holder.payment.setBackground(activity.getResources().getDrawable(R.drawable.round_corner_button));
                holder.payment.setText("-");
            }
*/
/*
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof ExistingTradeList) {
                        ((ExistingTradeList)activity).showTraderDetails(position,traders);
                    }

                }

            });
*/
            holder.viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof ExistingTradeList) {
                        ((ExistingTradeList)activity).showTraderDetails(position,traders);
                    }

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
        TextView name,code,payment,mobileValue,paymentDateValue,traderType,emailValue,viewDetails;

        SummaryViewHolder(View view) {
            super(view);
            name=(TextView)view.findViewById(R.id.nameValue);
            code=(TextView)view.findViewById(R.id.codeValue);
            payment=(TextView)view.findViewById(R.id.paymentStatus);
            mobileValue=(TextView)view.findViewById(R.id.mobileValue);
            paymentDateValue=(TextView)view.findViewById(R.id.paymentDateValue);
            traderType=(TextView)view.findViewById(R.id.traderType);
            emailValue=(TextView)view.findViewById(R.id.emailValue);
            viewDetails=(TextView)view.findViewById(R.id.viewDetails);
        }
    }
}
