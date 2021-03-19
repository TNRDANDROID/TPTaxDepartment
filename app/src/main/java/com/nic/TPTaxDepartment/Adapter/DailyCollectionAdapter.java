package com.nic.TPTaxDepartment.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.model.TPtaxModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DailyCollectionAdapter extends RecyclerView.Adapter<DailyCollectionAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<TPtaxModel> taxCollection;
    LayoutInflater mInflater;
    String type;

    public DailyCollectionAdapter( Activity activity, ArrayList<TPtaxModel> taxCollection,String type) {
        this.activity=activity;
        this.taxCollection=taxCollection;
        mInflater = LayoutInflater.from(activity);
        this.type=type;
    }
    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if(type.equals("Individual")) {
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
        else {
            LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

            ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                    R.layout.tax_collection_tp, viewGroup, false);
            SummaryViewHolder mainHolder = new SummaryViewHolder(mainGroup) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
            return mainHolder;
        }

    }
    @Override
    public void onBindViewHolder(final SummaryViewHolder holder,final int position) {

        try {
            holder.taxName.setText(taxCollection.get(position).getTaxTypeName());

            if(!taxCollection.get(position).getTaxCollection() .equals("") && taxCollection.get(position).getTaxCollection() != null){
                //holder.taxAmount.setText("\u20b9"+" "+taxCollection.get(position).getTaxCollection());
                holder.taxAmount.setText(indianCurrency(Double.parseDouble(taxCollection.get(position).getTaxCollection())));
            }
            else {
                holder.taxAmount.setText("\u20b9"+" "+"00.00");
            }
            if(!taxCollection.get(position).getDemand() .equals("") && taxCollection.get(position).getDemand() != null){
               // holder.demandAmount.setText("\u20b9"+" "+taxCollection.get(position).getDemand());
                holder.demandAmount.setText(indianCurrency(Double.parseDouble(taxCollection.get(position).getDemand())));
            }
            else {
                holder.demandAmount.setText("\u20b9"+" "+"00.00");
            }
            if(!taxCollection.get(position).getBalance() .equals("") && taxCollection.get(position).getBalance() != null){
                //holder.balanceAmount.setText("\u20b9"+" "+taxCollection.get(position).getBalance());
                holder.balanceAmount.setText(indianCurrency(Double.parseDouble(taxCollection.get(position).getBalance())));
            }else {
                holder.balanceAmount.setText("\u20b9"+" "+"00.00");
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
        TextView demand,demandAmount;
        TextView balance,balanceAmount;

        SummaryViewHolder(View view) {
            super(view);
            taxName=(TextView)view.findViewById(R.id.taxName);
            taxAmount=(TextView)view.findViewById(R.id.taxAmount);
            demand=(TextView)view.findViewById(R.id.demand_text);
            demandAmount=(TextView)view.findViewById(R.id.demand_amount);
            balance=(TextView)view.findViewById(R.id.balance_text);
            balanceAmount=(TextView)view.findViewById(R.id.balance_amount);

        }
    }

    public String indianCurrency(double money){
        String format = NumberFormat.getCurrencyInstance(new Locale("en", "in")).format(money);
        System.out.println(format);
        return format;
    }

}
