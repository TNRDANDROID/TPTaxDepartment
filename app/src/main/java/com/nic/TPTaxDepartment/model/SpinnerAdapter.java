package com.nic.TPTaxDepartment.model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nic.TPTaxDepartment.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
        private String[] objects;

        public SpinnerAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
            this.objects=objects;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

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