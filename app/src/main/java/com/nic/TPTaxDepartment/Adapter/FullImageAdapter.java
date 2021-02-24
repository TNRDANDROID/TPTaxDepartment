package com.nic.TPTaxDepartment.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nic.TPTaxDepartment.Fragment.SlideshowDialogFragment;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.activity.Dashboard;
import com.nic.TPTaxDepartment.activity.FieldVisit;
import com.nic.TPTaxDepartment.activity.FullImageActivity;
import com.nic.TPTaxDepartment.activity.LoginScreen;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.databinding.GalleryThumbnailBinding;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;

import java.util.ArrayList;
import java.util.List;

import static com.nic.TPTaxDepartment.activity.FieldVisit.db;

public class FullImageAdapter extends RecyclerView.Adapter<FullImageAdapter.MyViewHolder> {

    private Context context;
    private PrefManager prefManager;
    private ArrayList<TPtaxModel> imagePreviewlistvalues = new ArrayList<TPtaxModel>();
    private final dbData dbData;
    private LayoutInflater layoutInflater;
    String key="";

    public FullImageAdapter( Context context, ArrayList<TPtaxModel> imagePreviewlistvalues, dbData dbData, String key) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
        this.key = key;
        this.imagePreviewlistvalues = imagePreviewlistvalues;
    }

    @Override
    public FullImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        GalleryThumbnailBinding galleryThumbnailBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.gallery_thumbnail, viewGroup, false);
        return new FullImageAdapter.MyViewHolder(galleryThumbnailBinding);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private GalleryThumbnailBinding galleryThumbnailBinding;

        public MyViewHolder(GalleryThumbnailBinding Binding) {
            super(Binding.getRoot());
            galleryThumbnailBinding = Binding;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Glide.with(context).load(imagePreviewlistvalues.get(position).getImage())
                .thumbnail(0.5f)
                .into(holder.galleryThumbnailBinding.thumbnail);

        if(key.equals("FieldVisit")){
            holder.galleryThumbnailBinding.closeIcon.setVisibility(View.GONE);
        }else if(key.equals("NewTradeLicence")){
            holder.galleryThumbnailBinding.closeIcon.setVisibility(View.VISIBLE);
        }else if(key.equals("ExistTradeViewClass")){
            holder.galleryThumbnailBinding.closeIcon.setVisibility(View.GONE);
        }else if(key.equals("FieldVisitedImage")){
            holder.galleryThumbnailBinding.closeIcon.setVisibility(View.GONE);
        }

        holder.galleryThumbnailBinding.closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(key.equals("FieldVisit")){
                    Dashboard.db.delete(DBHelper.CAPTURED_PHOTO, "id" + "=?", new String[]{imagePreviewlistvalues.get(position).getField_visit_img_id()});
                    imagePreviewlistvalues.remove(position);
                    notifyDataSetChanged();
                }else if(key.equals("NewTradeLicence")){
                    Dashboard.db.delete(DBHelper.SAVE_TRADE_IMAGE, AppConstant.MOBILE + "=?", new String[]{imagePreviewlistvalues.get(position).getMobileno()});
                    imagePreviewlistvalues.remove(position);
                    notifyDataSetChanged();
                }

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", imagePreviewlistvalues);
                bundle.putInt("position", position);
                ((FullImageActivity)context).slideShow(bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imagePreviewlistvalues.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FullImageAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FullImageAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
