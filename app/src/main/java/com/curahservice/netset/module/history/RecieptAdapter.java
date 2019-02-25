package com.curahservice.netset.module.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.AppointmentDetail;
import com.curahservice.netset.gsonModel.HistoryModel;

import java.util.List;

import butterknife.ButterKnife;

public class RecieptAdapter extends RecyclerView.Adapter<RecieptAdapter.MyViewHolder> {
    private Context context;
    private List<AppointmentDetail.ServiceName> serviceList;
    private ReceiptFragment fragment;

    RecieptAdapter(Context context, List<AppointmentDetail.ServiceName> menuModelList, ReceiptFragment historyFragment) {
        this.context = context;
        this.serviceList = menuModelList;
        fragment = historyFragment;
    }

    @NonNull
    @Override
    public RecieptAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reciept, parent, false);
        return new RecieptAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecieptAdapter.MyViewHolder holder, int position) {

        holder.service_price.setText(serviceList.get(position).price);
        holder.servicename.setText(serviceList.get(position).name);
        if (position != serviceList.size() - 1) {
            holder.viewBorder.setVisibility(View.VISIBLE);
        } else {
            holder.viewBorder.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView servicename;
        private TextView service_price;

        private View viewBorder;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


            servicename = view.findViewById(R.id.servicename);
            service_price = view.findViewById(R.id.service_price);

            viewBorder = (View) view.findViewById(R.id.viewBorder);

        }


    }


}

