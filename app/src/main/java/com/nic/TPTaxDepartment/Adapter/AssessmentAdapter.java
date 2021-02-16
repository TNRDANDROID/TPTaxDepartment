package com.nic.TPTaxDepartment.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.nic.TPTaxDepartment.Interface.AdapterInterface;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.model.CommonModel;

import java.util.ArrayList;


public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.SummaryViewHolder>{
    private Activity activity;
    private ArrayList<CommonModel> commonModelArrayList;
    LayoutInflater mInflater;
    String selectedTaxTypeName;
    String selectedTaxTypeId;

    AdapterInterface adapterInterface;

    public AssessmentAdapter( Activity activity, ArrayList<CommonModel> commonModelArrayList,String selectedTaxTypeName,String selectedTaxTypeId) {
        this.activity=activity;
        this.commonModelArrayList=commonModelArrayList;
        this.selectedTaxTypeName=selectedTaxTypeName;
        this.selectedTaxTypeId=selectedTaxTypeId;
        mInflater = LayoutInflater.from(activity);
    }
    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.assessment_recycler, viewGroup, false);
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
            if(selectedTaxTypeName.equals("Professional Tax") || selectedTaxTypeId.equals("4")) {
                holder.professionalTax.setVisibility(View.VISIBLE);
                holder.propertyTax.setVisibility(View.GONE);
                holder.tradeLicenceTax.setVisibility(View.GONE);
                holder.waterTax.setVisibility(View.GONE);
                holder.nonTax.setVisibility(View.GONE);
                holder.assessment_no.setText(commonModelArrayList.get(position).lb_assessmentno);
                holder.assessment_name.setText(commonModelArrayList.get(position).assessmentnameeng);
                holder.organization_type.setText(commonModelArrayList.get(position).organizationtype);
            }else if(selectedTaxTypeName.equals("Property Tax") || selectedTaxTypeId.equals("1")) {
                holder.professionalTax.setVisibility(View.GONE);
                holder.propertyTax.setVisibility(View.VISIBLE);
                holder.tradeLicenceTax.setVisibility(View.GONE);
                holder.waterTax.setVisibility(View.GONE);
                holder.nonTax.setVisibility(View.GONE);
                holder.property_assessment_no.setText(commonModelArrayList.get(position).assessment_no);
                holder.owner_name.setText(commonModelArrayList.get(position).owner_name);
                holder.father_name.setText(commonModelArrayList.get(position).father_name);
                holder.permanent_address.setText(commonModelArrayList.get(position).permanent_address);
                holder.area_in_sq_feet.setText(commonModelArrayList.get(position).area_in_sq_feet);
            }else if(selectedTaxTypeName.equals("Trade License") || selectedTaxTypeId.equals("6")) {
                holder.professionalTax.setVisibility(View.GONE);
                holder.propertyTax.setVisibility(View.GONE);
                holder.tradeLicenceTax.setVisibility(View.VISIBLE);
                holder.waterTax.setVisibility(View.GONE);
                holder.nonTax.setVisibility(View.GONE);
                holder.lb_traderscode.setText(commonModelArrayList.get(position).lb_traderscode);
                holder.apfathername_en.setText(commonModelArrayList.get(position).apfathername_en);
                holder.apfathername_ta.setText(commonModelArrayList.get(position).apfathername_ta);
                holder.doorno.setText(commonModelArrayList.get(position).doorno);
                holder.traders_rate.setText(commonModelArrayList.get(position).traders_rate);
                holder.traders_rate.setText(commonModelArrayList.get(position).traders_rate);
                holder.trade_description_en.setText(commonModelArrayList.get(position).trade_description_en);
                holder.fin_year.setText(commonModelArrayList.get(position).from_fin_year);
            }else if(selectedTaxTypeName.equals("Water Charges") || selectedTaxTypeId.equals("2")) {
                holder.professionalTax.setVisibility(View.GONE);
                holder.propertyTax.setVisibility(View.GONE);
                holder.tradeLicenceTax.setVisibility(View.GONE);
                holder.waterTax.setVisibility(View.VISIBLE);
                holder.nonTax.setVisibility(View.GONE);
                holder.lb_connectionno.setText(commonModelArrayList.get(position).lb_connectionno);
                holder.connectionname.setText(commonModelArrayList.get(position).connectionname);
                holder.water_charges.setText("\u20b9"+" "+commonModelArrayList.get(position).water_charges);
            }else if(selectedTaxTypeName.equals("Non Tax") || selectedTaxTypeId.equals("5")) {
                holder.professionalTax.setVisibility(View.GONE);
                holder.propertyTax.setVisibility(View.GONE);
                holder.tradeLicenceTax.setVisibility(View.GONE);
                holder.waterTax.setVisibility(View.GONE);
                holder.nonTax.setVisibility(View.VISIBLE);
                holder.lb_leaseassessmentno.setText(commonModelArrayList.get(position).lb_leaseassessmentno);
                holder.leasee_name_en.setText(commonModelArrayList.get(position).leasee_name_en);
                holder.leasee_name_ta.setText(commonModelArrayList.get(position).leasee_name_ta);
                holder.lease_type_code.setText(commonModelArrayList.get(position).lease_type_code);
                holder.lease_type_description_en.setText(commonModelArrayList.get(position).lease_type_description_en);
                holder.lease_payment_due_type.setText(commonModelArrayList.get(position).lease_payment_due_type);
                holder.annuallease_amount.setText("\u20b9"+" "+commonModelArrayList.get(position).annuallease_amount);
            }else {
                holder.professionalTax.setVisibility(View.GONE);
                holder.propertyTax.setVisibility(View.GONE);
                holder.tradeLicenceTax.setVisibility(View.GONE);
                holder.waterTax.setVisibility(View.GONE);
                holder.nonTax.setVisibility(View.GONE);
            }

        } catch (Exception exp){
            exp.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {

        return commonModelArrayList.size();
    }
    class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView assessment_no,assessment_name,organization_type;
        LinearLayout professionalTax;
        TextView property_assessment_no,owner_name,father_name,permanent_address,area_in_sq_feet;
        LinearLayout propertyTax;
        TextView lb_traderscode,apfathername_en,apfathername_ta,doorno,traders_rate,fin_year,trade_description_en;
        LinearLayout tradeLicenceTax;
        TextView lb_connectionno,connectionname,water_charges;
        LinearLayout waterTax;
        TextView lb_leaseassessmentno,leasee_name_en,leasee_name_ta,lease_type_code,lease_type_description_en,
                lease_payment_due_type,annuallease_amount;
        LinearLayout nonTax;

        SummaryViewHolder(View view) {
            super(view);
            professionalTax=(LinearLayout)view.findViewById(R.id.professionalTax);
            assessment_no=(TextView)view.findViewById(R.id.assessment_no);
            assessment_name=(TextView)view.findViewById(R.id.assessment_name);
            organization_type=(TextView)view.findViewById(R.id.organization_type);

            propertyTax=(LinearLayout)view.findViewById(R.id.propertyTax);
            property_assessment_no=(TextView)view.findViewById(R.id.property_assessment_no);
            owner_name=(TextView)view.findViewById(R.id.owner_name);
            father_name=(TextView)view.findViewById(R.id.father_name);
            permanent_address=(TextView)view.findViewById(R.id.permanent_address);
            area_in_sq_feet=(TextView)view.findViewById(R.id.area_in_sq_feet);

            tradeLicenceTax=(LinearLayout)view.findViewById(R.id.tradeLicenceTax);
            lb_traderscode=(TextView)view.findViewById(R.id.lb_traderscode);
            apfathername_en=(TextView)view.findViewById(R.id.apfathername_en);
            apfathername_ta=(TextView)view.findViewById(R.id.apfathername_ta);
            doorno=(TextView)view.findViewById(R.id.doorno);
            traders_rate=(TextView)view.findViewById(R.id.traders_rate);
            fin_year=(TextView)view.findViewById(R.id.fin_year);
            trade_description_en=(TextView)view.findViewById(R.id.trade_description_en);

            waterTax=(LinearLayout)view.findViewById(R.id.waterTax);
            lb_connectionno=(TextView)view.findViewById(R.id.lb_connectionno);
            connectionname=(TextView)view.findViewById(R.id.connectionname);
            water_charges=(TextView)view.findViewById(R.id.water_charges);

            nonTax=(LinearLayout)view.findViewById(R.id.non_tax);
            lb_leaseassessmentno=(TextView)view.findViewById(R.id.lb_leaseassessmentno);
            leasee_name_en=(TextView)view.findViewById(R.id.leasee_name_en);
            leasee_name_ta=(TextView)view.findViewById(R.id.leasee_name_ta);
            lease_type_code=(TextView)view.findViewById(R.id.lease_type_code);
            lease_type_description_en=(TextView)view.findViewById(R.id.lease_type_description_en);
            lease_payment_due_type=(TextView)view.findViewById(R.id.lease_payment_due_type);
            annuallease_amount=(TextView)view.findViewById(R.id.annuallease_amount);

        }
    }



}
