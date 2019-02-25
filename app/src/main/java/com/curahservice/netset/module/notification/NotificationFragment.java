package com.curahservice.netset.module.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.AppoinmentData;
import com.curahservice.netset.gsonModel.NotificationData;
import com.curahservice.netset.module.appointment.ApointmentAdapter;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.chat.ChatFragment;
import com.curahservice.netset.module.history.HistoryDetailFragment;
import com.curahservice.netset.retrofitManager.ApiResponse;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;

public class NotificationFragment extends BaseFragment {

    RecyclerView recycle_item;
    Call<JsonObject> notificationApi;
    TextView nodata;
    NotificationData notification;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycle_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycle_item = view.findViewById(R.id.recycle_item);
        nodata = view.findViewById(R.id.noHistory);
        setToolBar();
        addData();
        NotificationManager nMgr = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancelAll();
    }


    void addData() {
        getNotificationApi(NotificationFragment.this);
    }

    public void getNotificationApi(ApiResponse response) {
        notificationApi = apiInterfaceAuth.getNotification(String.valueOf(store.getInt("userId")));
        apiHitAndHandle.makeApiCall(notificationApi, response);

    }


    void setData() {
        NotificationAdapter adapter = new NotificationAdapter(getActivity(), notification.getNotifications(), NotificationFragment.this);
        LinearLayoutManager grid = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycle_item.setLayoutManager(grid);
        recycle_item.setAdapter(adapter);
        recycle_item.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        if (notification.getNotifications().get(position).getLabel().equals("Cancel-Notification")) {
                            openAppointmentDetail(notification.getNotifications().get(position).getNotificationId() + "", NotificationFragment.this);
                        } else if (notification.getNotifications().get(position).getLabel().equals("First-Message-Notification")) {
                            ChatFragment fragmentToOpen = new ChatFragment();
                            Bundle data = new Bundle();
                            data.putString("id", notification.getNotifications().get(position).getSenderId() + "");
                            data.putString("connectionId", notification.getNotifications().get(position).getNotificationId() + "");
                            fragmentToOpen.setArguments(data);
                            replaceFragment(R.id.fl_container_home, fragmentToOpen, "", data);
                        }

                    }
                })
        );
    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.pink));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes("Notifications", R.mipmap.ic_menu, true);
    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == notificationApi) {
            Gson gson = new Gson();
            notification = gson.fromJson(object.toString(), NotificationData.class);
            if (notification.getStatus() == 200) {
                if (notification.getNotifications().size() > 0) {
                    nodata.setVisibility(View.GONE);
                    recycle_item.setVisibility(View.VISIBLE);
                    setData();
                } else {
                    nodata.setVisibility(View.VISIBLE);
                    nodata.setText("No notifications found");
                    recycle_item.setVisibility(View.GONE);
                }
            }
        } else if (call == appointmentDeatil) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            AppoinmentData appointmentData = gson.fromJson(object.toString(), AppoinmentData.class);
            HistoryDetailFragment detail = new HistoryDetailFragment();
            Bundle bun = new Bundle();
            bun.putString("ctrl_show", "end");
            bun.putSerializable("detail", (Serializable) appointmentData);
            detail.setArguments(bun);
            replaceFragment(R.id.fl_container_home, detail, "AppointmentStartService", bun);

        }
    }
}
