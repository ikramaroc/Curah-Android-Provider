package com.curahservice.netset.module.appointment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.AppoinmentData;
import com.curahservice.netset.gsonModel.ProviderPojo;
import com.curahservice.netset.module.history.HistoryDetailFragment;
import com.curahservice.netset.module.home.HomeRecyclerAdapter;
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

public class ApointmentAdapter extends RecyclerView.Adapter<ApointmentAdapter.MyViewHolder> implements ApiResponse {
    private Context context;
    private List<ProviderPojo.ProviderAppointment> providerAppointmentList;
    private AppointmentFragment fragment;
    private String bookigId = "", raterId = "";
    int selectedPosition;

    ApointmentAdapter(Context context, List<ProviderPojo.ProviderAppointment> menuModelList, AppointmentFragment fragment) {
        this.context = context;
        this.providerAppointmentList = menuModelList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointments, parent, false);
        return new MyViewHolder(view);
    }


    //A = accept, P=pending, R=reject,I=inprogress)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.service_name.setText(providerAppointmentList.get(position).getServiceName());
        holder.label_time.setText(
                changeDateFormat(providerAppointmentList.get(position).getDate(), "yyyy-MM-dd", "dd MMM, yyyy") + " "
                        + changeDateFormat(providerAppointmentList.get(position).getStartTime(), "HH:mm:ss", "hh:mm a") + " to "
                        + changeDateFormat(providerAppointmentList.get(position).getCloseTime(), "HH:mm:ss", "hh:mm a"));
        if (providerAppointmentList.get(position).getStatus().equalsIgnoreCase("A")) {
            holder.viewButton.setVisibility(View.VISIBLE);
            holder.task_status.setText("Accepted");
            holder.viewButton.setText("Start Service");
        } else if (providerAppointmentList.get(position).getStatus().equalsIgnoreCase("I")) {
            holder.viewButton.setVisibility(View.VISIBLE);
            holder.task_status.setText("In-progress");
            holder.viewButton.setText("End Service");
        } else if (providerAppointmentList.get(position).getStatus().equalsIgnoreCase("K")) {
            holder.viewButton.setVisibility(View.INVISIBLE);
            if (providerAppointmentList.get(position).getCancel_by().equalsIgnoreCase("C")) {
                holder.task_status.setText("Customer cancelled");
            } else {
                holder.task_status.setText("You cancelled");
            }
        } else if (providerAppointmentList.get(position).getStatus().equals("C")) {
            holder.viewButton.setVisibility(View.VISIBLE);
            holder.task_status.setText("Completed");
            holder.viewButton.setVisibility(View.INVISIBLE);
        }
        if (position != providerAppointmentList.size() - 1) {
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.view.setVisibility(View.GONE);
        }
        holder.appoinmentOutside.setTag(position);
        holder.appoinmentOutside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = (int) v.getTag();
                fragment.openAppointmentDetail(providerAppointmentList.get(selectedPosition).getBookingId() + "", ApointmentAdapter.this);
            }
        });
        holder.viewButton.setTag(position);
        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProviderPojo.ProviderAppointment item = providerAppointmentList.get((int) v.getTag());
                if (item.getStatus().equals("A")) {
                    providerAppointmentList.get((int) v.getTag()).setStatus("I");
                    fragment.startService(item.getBookingId() + "", ApointmentAdapter.this);
                } else if (item.getStatus().equals("I")) {
                    raterId = providerAppointmentList.get((Integer) v.getTag()).getCustomerId() + "";
                    bookigId = providerAppointmentList.get((Integer) v.getTag()).getBookingId() + "";
                    providerAppointmentList.get((int) v.getTag()).setStatus("C");
                    fragment.endService(item.getBookingId() + "", ApointmentAdapter.this);
                }

            }
        });
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
    public int getItemCount() {
        return providerAppointmentList.size();
    }

    @Override
    public void onSuccess(Call call, Object object) {
        if (call == fragment.startServiceApi) {
            //{"status":402,"message":"This service is started in time 13:00:00 to 14:00:00 on date 2018-09-14"}

            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    notifyDataSetChanged();
                } else if (result.getInt(Const.STATUS) == 402) {
                    fragment.showMessage(context, "", result.getString(Const.MESSAGE), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("----------", object.toString());
        } else if (call == fragment.endServiceApi) {
            Log.e("----------", object.toString());
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    notifyDataSetChanged();
                    fragment.rateLayout(raterId + "", bookigId + "");

                } else if (result.getInt(Const.STATUS) == 402) {
                    fragment.showMessage(context, "", result.getString(Const.MESSAGE), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (call == fragment.appointmentDeatil) {
            Log.e("-----------", object.toString());
            Gson gson = new GsonBuilder().serializeNulls().create();
            AppoinmentData appointmentData = gson.fromJson(object.toString(), AppoinmentData.class);
            HistoryDetailFragment detail = new HistoryDetailFragment();
            Bundle bun = new Bundle();
            bun.putString("ctrl_show", "end");
            bun.putSerializable("detail", (Serializable) appointmentData);
            detail.setArguments(bun);
            fragment.replaceFragment(R.id.fl_container_home, detail, "AppointmentStartService", bun);
        }


    }

    @Override
    public void onError(Call call, String errorMessage, ApiResponse apiResponse) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView service_name, task_status, label_time;
        private View view;
        LinearLayout appoinmentOutside;
        private Button viewButton;


        MyViewHolder(View itemView) {
            super(itemView);
            service_name = itemView.findViewById(R.id.service_name);
            view = itemView.findViewById(R.id.view);
            task_status = itemView.findViewById(R.id.task_status);
            viewButton = itemView.findViewById(R.id.viewButton);
            label_time = itemView.findViewById(R.id.label_time);
            appoinmentOutside = itemView.findViewById(R.id.appoinmentOutside);

        }
    }
}
