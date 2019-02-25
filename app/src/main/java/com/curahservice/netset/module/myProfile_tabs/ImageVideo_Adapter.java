package com.curahservice.netset.module.myProfile_tabs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.curahservice.netset.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class ImageVideo_Adapter extends RecyclerView.Adapter<ImageVideo_Adapter.MyViewHolder>{

    private ArrayList<Image_Model> list;
    private Context context;
    private RefreshCallBack refreshlistner;
    private View.OnClickListener click_accept,itemClickListener;

    public ImageVideo_Adapter(Context context, ArrayList<Image_Model> Data, View.OnClickListener click) {
        this.context =context;
        this.click_accept = click;
        this.list = Data;
    }

    ImageVideo_Adapter(Context context, ArrayList<Image_Model> Data) {
        this.context = context;
        this.list = Data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_picturesvideos, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(list.get(position).getThumb() != 0) {
            holder.cardImg.setBackgroundResource(list.get(position).getThumb());
            holder.thumbImg.setVisibility(View.VISIBLE);
        } else {
            holder.cardImg.setBackgroundResource(list.get(position).getImage());
            holder.thumbImg.setVisibility(View.GONE);
        }

        holder.cardImg.setOnClickListener(itemClickListener);
        holder.cardImg.setTag(position);

    }
    public void setAdapterCallback(RefreshCallBack adapterCallback){
        this.refreshlistner = adapterCallback;
    }

    public interface RefreshCallBack{
        void refreshlist();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircularImageView cardImg;
        ImageView thumbImg;
        LinearLayout lay_main;

        MyViewHolder(View v) {
            super(v);
            cardImg = v.findViewById(R.id.cardImage);
            lay_main = v.findViewById(R.id.main);
            thumbImg = v.findViewById(R.id.img_thumb);

        }
    }
}
