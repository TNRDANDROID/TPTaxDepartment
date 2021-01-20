package com.nic.TPTaxDepartment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.Gender;

import java.util.ArrayList;

public class GenderAdapter extends BaseAdapter {

    ArrayList<CommonModel> genderArrayList;
    Context context;
    String type;

    public GenderAdapter(Context context,ArrayList<CommonModel> genderArrayList,String type) {
        this.context=context;
        this.genderArrayList = genderArrayList;
        this.type=type;
    }

    @Override
    public int getCount() {
        return genderArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).
                    inflate(R.layout.gender_list_text_view, viewGroup, false);
        }

        TextView gender_text=view.findViewById(R.id.gender_text);
        if(type.equals("Gender")) {
            gender_text.setText(genderArrayList.get(i).getGender_name_en());
        }

        else if(type.equals("Ward")){
            gender_text.setText(genderArrayList.get(i).getWard_name_tamil());
        }
        else if(type.equals("Street")){
            gender_text.setText(genderArrayList.get(i).getStreet_name_tamil());
        }
        else if(type.equals("TaxType")){
            gender_text.setText(genderArrayList.get(i).getTaxtypedesc_en());
        }

        else if(type.equals("FinancialYear")){
            gender_text.setText(genderArrayList.get(i).getFIN_YEAR());
        }

        return view;
    }
}
