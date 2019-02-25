package com.curahservice.netset.module.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.curahservice.netset.MyFirebaseMessagingService;
import com.curahservice.netset.R;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.chat.ChatFragment;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class MessageFragment extends BaseFragment implements MyFirebaseMessagingService.notificationListner {

    RecyclerView recycle_item;
    TextView noHistory;
    SwipeMessageAdapter adapter;
    public MessageConnection messageConnection;
    Call<JsonObject> getMessage, deleteConnection;
    boolean isback = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycle_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycle_item = view.findViewById(R.id.recycle_item);
        noHistory = view.findViewById(R.id.noHistory);
        setToolBar();
        getMessage();

    }

    @Override
    public void onResume() {
        super.onResume();
        MyFirebaseMessagingService.connectionId = "";
        MyFirebaseMessagingService.registerChatNotification(this);
    }

    public void getMessage() {
        getMessage = apiInterfaceAuth.getMessage(store.getInt("userId") + "");
        apiHitAndHandle.makeApiCall(getMessage, MessageFragment.this);
    }


    public void deleteMessage(Integer connectionId) {
        deleteConnection = apiInterfaceAuth.deleteConnections(store.getInt("userId") + "", connectionId.toString());
        apiHitAndHandle.makeApiCall(deleteConnection, MessageFragment.this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyFirebaseMessagingService.unregisterNotification();
    }

    void setData(final List<MessageConnection.Connection> messageList) {
        adapter = new SwipeMessageAdapter(getActivity(), messageList, MessageFragment.this);
        LinearLayoutManager grid = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycle_item.setLayoutManager(grid);
        recycle_item.setAdapter(adapter);


    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == getMessage) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            messageConnection = gson.fromJson(object.toString(), MessageConnection.class);
            if (messageConnection.getStatus() == 200) {
                if (messageConnection.getConnections().size() > 0) {
                    setData(messageConnection.getConnections());
                    noHistory.setVisibility(View.GONE);
                    recycle_item.setVisibility(View.VISIBLE);

                    if (getArguments() != null && getArguments().getBoolean("fromNotification", false) && !isback) {

                        isback = true;
                        ChatFragment fragmentToOpen = new ChatFragment();

                        Bundle data = new Bundle();
                        data.putString("id", getArguments().getString("senderId"));
                        data.putString("connectionId", getArguments().getString("connection_id"));
                        fragmentToOpen.setArguments(data);
                        replaceFragment(R.id.fl_container_home, fragmentToOpen, "", data);


                    }

                } else {
                    noHistory.setText("No messages found");
                    noHistory.setVisibility(View.VISIBLE);
                    recycle_item.setVisibility(View.GONE);

                }
            }
        } else if (call == deleteConnection) {
            getMessage();
        }


    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.pink));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes("Messages", R.mipmap.ic_menu, true);
    }


    @Override
    public void message(String message, String recieverId, String connectionId) {

    }

    @Override
    public void newConnection() {
        getMessage();
    }

    @Override
    public void newAppointment(String bookingid) {

    }


}
