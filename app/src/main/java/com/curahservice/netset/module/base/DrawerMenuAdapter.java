package com.curahservice.netset.module.base;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.util.MenuModel;

import java.util.List;

public class DrawerMenuAdapter extends RecyclerView.Adapter<DrawerMenuAdapter.MyViewHolder> {

    private Activity context;
    private List<MenuModel> menuModelList;
    private DrawerActivity activity;

    DrawerMenuAdapter(Activity context, List<MenuModel> menuModelList) {
        this.context = context;
        this.menuModelList = menuModelList;
        this.activity = (DrawerActivity) context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drawer_menu,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MenuModel menuModel=menuModelList.get(position);

        holder.menuNameTv.setText(menuModel.getName());
        holder.menuImage.setImageResource(menuModel.getImage());

    }

    @Override
    public int getItemCount() {
        return menuModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView menuNameTv;
        private ImageView menuImage;

        MyViewHolder(View itemView) {
            super(itemView);

            menuNameTv=itemView.findViewById(R.id.txt_menu_item_name);
            menuImage=itemView.findViewById(R.id.img_menu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.menuItemClick(getAdapterPosition());
                }
            });
        }
    }
}
