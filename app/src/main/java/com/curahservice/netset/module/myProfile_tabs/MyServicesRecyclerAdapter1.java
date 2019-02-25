package com.curahservice.netset.module.myProfile_tabs;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curahservice.netset.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyServicesRecyclerAdapter1 extends RecyclerView.Adapter<MyServicesRecyclerAdapter1.MyViewHolder> {

    public Activity context;
    public List<String> dataList;

    public MyServicesRecyclerAdapter1(Activity context, List<String> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_TV)
        TextView name_TV;
        @BindView(R.id.price_TV)
        TextView price_TV;
        @BindView(R.id.delete_IV)
        ImageView delete_IV;
        @BindView(R.id.edit_IV)
        ImageView edit_IV;
        @BindView(R.id.LL)
        LinearLayout LL;
        @BindView(R.id.topView)
        View topView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    @Override
    public MyServicesRecyclerAdapter1.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_add_services, parent, false);

        return new MyServicesRecyclerAdapter1.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyServicesRecyclerAdapter1.MyViewHolder holder, int position) {

        if (position%2==0) {
            holder.LL.setBackground(context.getResources().getDrawable(R.drawable.text_background));
            holder.name_TV.setText(dataList.get(position));
        } else {
            holder.LL.setBackground(context.getResources().getDrawable(R.drawable.text_background2));
            holder.name_TV.setText(dataList.get(position));
        }
        if (position!=0){
            holder.topView.setVisibility(View.GONE);
        }else {
            holder.topView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}