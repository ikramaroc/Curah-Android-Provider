package com.curahservice.netset.module.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.AppoinmentData;
import com.curahservice.netset.gsonModel.AppointmentDetail;
import com.curahservice.netset.gsonModel.ProviderPojo;
import com.curahservice.netset.module.history.HistoryDetailFragment;
import com.curahservice.netset.retrofitManager.ApiResponse;
import com.curahservice.netset.util.Const;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.MyViewHolder> implements ApiResponse {

    private Activity context;
    private List<ProviderPojo.ProviderAppointment> providerAppointmentList;
    private HomeFragment fragment;
    int selectedPosition=-1;


    HomeRecyclerAdapter(Activity context, List<ProviderPojo.ProviderAppointment> dataList, HomeFragment fragment) {
        this.context = context;
        this.providerAppointmentList = dataList;
        this.fragment = fragment;
    }


    @Override
    public void onSuccess(Call call, Object object) {
        if (call==fragment.acceptRejectApi){
            try{
                JSONObject result=new JSONObject(object.toString());
                if (result.getInt(Const.STATUS)==200){
                    Toast.makeText(context,result.getString(Const.MESSAGE),Toast.LENGTH_LONG).show();
                    providerAppointmentList.remove(selectedPosition);
                    notifyDataSetChanged();
                    fragment.getNewRequestAppointment(true);
                }else {
                    fragment.showMessage(context, "", result.getString(Const.MESSAGE), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if (call==fragment.appointmentDeatil){
            Gson gson = new GsonBuilder().serializeNulls().create();
            AppoinmentData appointmentData = gson.fromJson(object.toString(), AppoinmentData.class);
            HistoryDetailFragment detail=new HistoryDetailFragment();
            Bundle bun=new Bundle();
            bun.putSerializable("detail", (Serializable) appointmentData);
            detail.setArguments(bun);
            fragment.replaceFragment(R.id.fl_container_home, detail, "HomeStart", bun);
        }

    }

    @Override
    public void onError(Call call, String errorMessage, ApiResponse apiResponse) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView service_name_TV;
        private TextView date_time_TV;
        private RelativeLayout outsideAppoinment;
        private TextView accept_TV, reject_TV;

        MyViewHolder(View view) {
            super(view);
            service_name_TV = view.findViewById(R.id.service_name_TV);
            date_time_TV = view.findViewById(R.id.date_time_TV);
            accept_TV = view.findViewById(R.id.accept_TV);
            reject_TV = view.findViewById(R.id.reject_TV);
            outsideAppoinment=view.findViewById(R.id.outsideAppoinment);



        }
    }

    public String changeDateFormat(String dateString, String sourceDateFormat, String targetDateFormat) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        SimpleDateFormat inputDateFromat = new SimpleDateFormat(sourceDateFormat, Locale.getDefault());
        Date date = new Date();
        try {
            date = inputDateFromat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(targetDateFormat, Locale.getDefault());
        return outputDateFormat.format(date);
    }


    @Override
    public HomeRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_home, parent, false);

        return new HomeRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeRecyclerAdapter.MyViewHolder holder, int position) {

        holder.outsideAppoinment.setTag(position);
        holder.date_time_TV.setText(
                changeDateFormat(providerAppointmentList.get(position).getDate(), "yyyy-MM-dd", "dd MMM, yyyy") + " "
                        + changeDateFormat(providerAppointmentList.get(position).getStartTime(), "HH:mm:ss", "hh:mm a") + " to "
                        + changeDateFormat(providerAppointmentList.get(position).getCloseTime(), "HH:mm:ss", "hh:mm a"));
        holder.service_name_TV.setText(providerAppointmentList.get(position).getServiceName());
        holder.reject_TV.setTag(position);
        holder.accept_TV.setTag(position);
        holder.reject_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=(int) v.getTag();
                fragment.acceptRejectApi(providerAppointmentList.get(selectedPosition).getBookingId()+"", "R",HomeRecyclerAdapter.this);
            }
        });
        holder.accept_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=(int) v.getTag();
                fragment.acceptRejectApi(providerAppointmentList.get(selectedPosition).getBookingId()+"", "A",HomeRecyclerAdapter.this);

            }
        });
        holder.outsideAppoinment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition=(int) v.getTag();
                fragment.openAppointmentDetail(providerAppointmentList.get(selectedPosition).getBookingId()+"",HomeRecyclerAdapter.this);
            }
        });
    }

    @Override
    public int getItemCount() {
        return providerAppointmentList.size();
    }
}