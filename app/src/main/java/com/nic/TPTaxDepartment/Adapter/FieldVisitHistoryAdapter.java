package com.nic.TPTaxDepartment.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.model.TPtaxModel;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class FieldVisitHistoryAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private Context context;
    ArrayList<TPtaxModel> historyList;
    private LayoutInflater inflater;
    Activity activity;
    private Animation animShow, animHide;

    public FieldVisitHistoryAdapter(Context context,ArrayList<TPtaxModel> historyList,Activity activity) {
        this.context = context;
        this.historyList = historyList;
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }
    @Override
    public int getCount() {
        return historyList.size();
    }
    @Override
    public Object getItem(int position) {
        return historyList.get(position).request_id;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final FieldVisitHistoryAdapter.ViewHolder holder;
        final String postId = historyList.get(position).request_id;
        if (convertView == null) {
            holder = new FieldVisitHistoryAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.field_visit_history_recycler, parent, false);
            holder.nameValue = (TextView) convertView.findViewById(R.id.nameValue);
            holder.requestId = (TextView) convertView.findViewById(R.id.requestId);
            holder.taxtypeValue = (TextView) convertView.findViewById(R.id.taxtypeValue);
            holder.status_filed = (TextView) convertView.findViewById(R.id.status_filed);
            holder.ward_name_value = (TextView) convertView.findViewById(R.id.ward_name_value);
            holder.remarks_value = (TextView) convertView.findViewById(R.id.remarks_value);
            holder.street_name_value = (TextView) convertView.findViewById(R.id.street_name_value);
            holder.image_list = (ImageView) convertView.findViewById(R.id.image_list);
            holder.extend_view_icon = (ImageView) convertView.findViewById(R.id.extend_view_icon);
            holder.hide_details_icon = (ImageView) convertView.findViewById(R.id.hide_details_icon);
            holder.details_view_layout = (RelativeLayout) convertView.findViewById(R.id.details_view_layout);
            holder.content2 = (RelativeLayout) convertView.findViewById(R.id.content2);
            convertView.setTag(holder);
        } else {
            holder = (FieldVisitHistoryAdapter.ViewHolder) convertView.getTag();
        }
        try {
            holder.extend_view_icon.setVisibility(View.VISIBLE);
            holder.details_view_layout.setVisibility(View.GONE);
            animShow = AnimationUtils.loadAnimation( context, R.anim.dialog_slide_down);
            animHide = AnimationUtils.loadAnimation( context, R.anim.dialog_slide_up);
            holder.nameValue.setText(historyList.get(position).getTraderName());
            holder.requestId.setText(historyList.get(position).getRequest_id());
            holder.taxtypeValue.setText(historyList.get(position).getTaxTypeName());
            holder.status_filed.setText(historyList.get(position).getStatus());
            holder.ward_name_value.setText(historyList.get(position).getWardname());
            holder.remarks_value.setText(historyList.get(position).getRemark());
            holder.street_name_value.setText(historyList.get(position).getStreetname());

            holder.image_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.content2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.extend_view_icon.setVisibility(View.GONE);
                    holder.details_view_layout.setVisibility(View.VISIBLE);

/*
                    if(historyList!=null && historyList.size()>0){
                        for (int i = 0; i < historyList.size(); i++) {
                            if(i==position){
                                holder.extend_view_icon.setVisibility(View.GONE);
                                holder.details_view_layout.setVisibility(View.VISIBLE);
                            }else {
                                holder.extend_view_icon.setVisibility(View.VISIBLE);
                                holder.details_view_layout.setVisibility(View.GONE);
                            }
                        }
                    }
*/

                }
            });
            holder.details_view_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.extend_view_icon.setVisibility(View.VISIBLE);
                    holder.details_view_layout.setVisibility(View.GONE);
/*
                    if(historyList!=null && historyList.size()>0){
                        for (int i = 0; i < historyList.size(); i++) {
                            if(i==position){
                                holder.extend_view_icon.setVisibility(View.VISIBLE);
                                holder.details_view_layout.setVisibility(View.GONE);
                            }else {
                                holder.extend_view_icon.setVisibility(View.GONE);
                                holder.details_view_layout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
*/

                }
            });
        } catch(Exception exp){
            exp.printStackTrace();
        }
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        FieldVisitHistoryAdapter.HeaderViewHolder holder;
        if (convertView == null) {
            holder = new FieldVisitHistoryAdapter.HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (FieldVisitHistoryAdapter.HeaderViewHolder) convertView.getTag();
        }
        holder.text.setText(historyList.get(position).fieldVisitDate);
        return convertView;
    }
    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        String date = historyList.get(position).fieldVisitDate.replace("-","");
        return Integer.parseInt(date);
    }
    private class HeaderViewHolder {
        TextView text;
    }
    class ViewHolder {
        TextView requestId, taxtypeValue, status_filed,ward_name_value,street_name_value,remarks_value,nameValue;
        ImageView image_list,extend_view_icon,hide_details_icon;
        RelativeLayout details_view_layout,content2;
    }
}