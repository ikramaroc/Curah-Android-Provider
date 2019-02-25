package com.curahservice.netset.module.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.NotificationData;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private Context context;
    private List<NotificationData.Notification> notificationList;
    NotificationFragment fragment;


    public NotificationAdapter(Context context, List<NotificationData.Notification> menuModelList, NotificationFragment fragment) {
        this.context = context;
        this.notificationList = menuModelList;
        this.fragment=fragment;

    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        holder.notificationMessage.setText(Html.fromHtml(notificationList.get(position).getMessage()));

        holder.notification_time.setText(fragment.converStringToDate(notificationList.get(position).getDate()));

        if (position==(notificationList.size()-1)) {
            holder.view.setVisibility(View.GONE);
        } else {
            holder.view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView notificationMessage,notification_time;
        View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            notificationMessage = itemView.findViewById(R.id.notificationMessage);
            view = itemView.findViewById(R.id.view);
            notification_time=itemView.findViewById(R.id.notification_time);
        }
    }
}