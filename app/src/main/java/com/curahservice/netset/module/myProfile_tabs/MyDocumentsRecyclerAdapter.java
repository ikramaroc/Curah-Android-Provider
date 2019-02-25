package com.curahservice.netset.module.myProfile_tabs;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.R;

import java.util.List;

public class MyDocumentsRecyclerAdapter extends RecyclerView.Adapter<MyDocumentsRecyclerAdapter.MyViewHolder> {
    private Activity context;

    private android.support.v4.app.Fragment fragment;
    List<String> item, image;
    boolean isVerified;

    MyDocumentsRecyclerAdapter(Activity context, List<String> item, List<String> image, MyDocumentsTab4 fragment,boolean isVerified) {
        this.context = context;
        this.item = item;
        this.image = image;
        this.fragment = fragment;
        this.isVerified=isVerified;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_my_documents, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name_TV.setText(item.get(position));
        Glide.with(context)
                .load(image.get(position))
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(new RequestOptions()
                        .dontAnimate())
                .into(holder.cover_IV);
        if (isVerified){
            holder.cover_check_IV.setVisibility(View.VISIBLE);
        }else {
            holder.cover_check_IV.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return image.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView cover_IV,cover_check_IV;
        private TextView name_TV;
        MyViewHolder(View itemView) {
            super(itemView);
            cover_IV = itemView.findViewById(R.id.cover_IV);
            cover_check_IV=itemView.findViewById(R.id.cover_check_IV);
            name_TV = itemView.findViewById(R.id.name_TV);

        }
    }
}
