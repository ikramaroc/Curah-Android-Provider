package com.curahservice.netset.module.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.module.TimeSlots_Model;

import java.util.ArrayList;

public class TimeSlots_Adapter extends RecyclerView.Adapter<TimeSlots_Adapter.MyViewHolder> {

    private ArrayList<TimeSlots_Model> listdata;
    private Context context;
    private RefreshCallBack refreshlistner;
    private View.OnClickListener click_accept, itemClickListener;

    public TimeSlots_Adapter(Context context, ArrayList<TimeSlots_Model> arrayList) {
        this.listdata = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_timeslote, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtTime.setText(listdata.get(position).getTime());
    }

    public void setAdapterCallback(RefreshCallBack adapterCallback) {
        this.refreshlistner = adapterCallback;
    }

    public interface RefreshCallBack {
        void refreshlist();
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTime;

        MyViewHolder(View v) {
            super(v);
            txtTime = v.findViewById(R.id.time_slots_TV);
        }
    }
}
