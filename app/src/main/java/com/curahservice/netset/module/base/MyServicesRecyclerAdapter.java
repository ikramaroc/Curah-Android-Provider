package com.curahservice.netset.module.base;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.AppointmentDetail;
import com.curahservice.netset.util.Const;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyServicesRecyclerAdapter extends RecyclerView.Adapter<MyServicesRecyclerAdapter.MyViewHolder> {

    public Activity context;
    public List<AppointmentDetail.ServiceName> dataList;
    private  boolean edit=false;

    public MyServicesRecyclerAdapter(Activity context, List<AppointmentDetail.ServiceName> dataList, boolean edit) {
        this.context = context;
        this.dataList = dataList;
        this.edit=edit;
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

        @BindView(R.id.seprator)
                View seprator;




        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    @Override
    public MyServicesRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_add_services, parent, false);

        return new MyServicesRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyServicesRecyclerAdapter.MyViewHolder holder, int position) {
        holder.name_TV.setText(dataList.get(position).name);
        holder.price_TV.setText(Const.CURRENCY+dataList.get(position).price);
        if (position%2==0) {
            holder.LL.setBackground(context.getResources().getDrawable(R.drawable.text_background));

        } else {
            holder.LL.setBackground(context.getResources().getDrawable(R.drawable.text_background2));
        }
        if (position!=0){
            holder.topView.setVisibility(View.GONE);
        }else {
            holder.topView.setVisibility(View.VISIBLE);
        }
        if (edit){
            holder.delete_IV.setVisibility(View.VISIBLE);
            holder.edit_IV.setVisibility(View.VISIBLE);
        }else {

            holder.delete_IV.setVisibility(View.INVISIBLE);
            holder.edit_IV.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}