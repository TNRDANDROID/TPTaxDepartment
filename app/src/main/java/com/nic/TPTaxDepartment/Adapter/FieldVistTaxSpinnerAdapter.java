package com.nic.TPTaxDepartment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.activity.FieldVisit;

import java.util.HashMap;
import java.util.Map;

public class FieldVistTaxSpinnerAdapter extends ArrayAdapter<String> {
    private String[] objects;
    HashMap<String, String> spinnerMapTax;
    String selectedTaxTypeId;
    String selectedTaxTypeName="";
    Context mcontext;

    public FieldVistTaxSpinnerAdapter(Context context, int textViewResourceId, String[] objects, HashMap<String, String> spinnerMapTaxType) {
        super(context, textViewResourceId, objects);
        this.objects=objects;
        this.spinnerMapTax=spinnerMapTaxType;
        this.mcontext=context;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_spinner_dropdown_item, parent, false);
        final TextView label=(TextView)row.findViewById(R.id.text);
        label.setText(objects[position]);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Map.Entry<String, String> entry: spinnerMapTax.entrySet()) {
                    // if give value is equal to value from entry
                    // print the corresponding key
                    if(entry.getValue() == objects[position]) {
                        selectedTaxTypeId=entry.getKey();
                        break;
                    }
                }
                selectedTaxTypeName = objects[position];
                ((FieldVisit)mcontext).taxSpinnerClickedMethod(position,selectedTaxTypeId,selectedTaxTypeName);
            }
        });


        return row;    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(final int position, View convertView, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_spinner_dropdown_item, parent, false);
        final TextView label=(TextView)row.findViewById(R.id.text);
        label.setText(objects[position]);
        return row;
    }
}
/*
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.activity.FieldVisit;
import com.nic.TPTaxDepartment.databinding.FieldVisitBinding;
import com.nic.TPTaxDepartment.model.CommonModel;

import java.lang.reflect.Method;
import java.util.List;

public class FieldVistTaxSpinnerAdapter extends BaseAdapter  {
    private List<CommonModel> vPtaxModels;
    private Context mContext;
    private String type;


    public FieldVistTaxSpinnerAdapter(Context mContext, List<CommonModel> searchRequestId) {
        this.vPtaxModels = searchRequestId;
        this.mContext = mContext;
        this.type = type;
    }


    public int getCount() {
        return vPtaxModels.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.spinner_drop_down_item, parent, false);
//        TextView tv_type = (TextView) view.findViewById(R.id.tv_spinner_item);
        View view = inflater.inflate(R.layout.spinner_value, parent, false);
        TextView tv_type = (TextView) view.findViewById(R.id.spinner_list_value);
        tv_type.setText(vPtaxModels.get(position).getTaxtypedesc_en());

        tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FieldVisit)mContext).taxSpinnerClickedMethod(position,vPtaxModels.get(position).getTaxtypeid(),vPtaxModels.get(position).getTaxtypedesc_en());

            }
        });


        return view;
    }
    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
*/
