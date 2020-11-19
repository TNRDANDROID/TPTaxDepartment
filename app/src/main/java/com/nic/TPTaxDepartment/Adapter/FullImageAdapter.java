package com.nic.TPTaxDepartment.Adapter;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.dataBase.dbData;
import com.nic.TPTaxDepartment.databinding.GalleryThumbnailBinding;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;

import java.util.List;

public class FullImageAdapter extends RecyclerView.Adapter<FullImageAdapter.MyViewHolder> {

    private Context context;
    private PrefManager prefManager;
    private List<TPtaxModel> imagePreviewlistvalues;
    private final dbData dbData;
    private LayoutInflater layoutInflater;

    public FullImageAdapter(Context context, List<TPtaxModel> imagePreviewlistvalues, dbData dbData) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.dbData = dbData;
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
