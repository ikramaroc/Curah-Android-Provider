package com.curahservice.netset.module.chat;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.curahservice.netset.MyFirebaseMessagingService;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.ConversationModel;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.ImageUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.curahservice.netset.MyFirebaseMessagingService.connectionId;
import static com.curahservice.netset.MyFirebaseMessagingService.registerChatNotification;
import static com.curahservice.netset.MyFirebaseMessagingService.unregisterNotification;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ChatFragment extends BaseFragment implements MyFirebaseMessagingService.notificationListner, BaseFragment.PermCallback {

    RecyclerView chat_rv;
    File imageFile;
    ImageView msg_send;
    ChatAdapter adapter;
    EditText message;
    Call<JsonObject> sendMessage, getMessages;
    String barberId;
    ImageView uploadImage;
    public ConversationModel conversation;
    List<ConversationModel.Conversation> messageList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_ll, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadImage = view.findViewById(R.id.uploadImage);
        chat_rv = view.findViewById(R.id.chat_rv);
        msg_send = view.findViewById(R.id.msg_send);
        message = view.findViewById(R.id.message);
        connectionId = getArguments().getString("connectionId");
        barberId = getArguments().getString("id");
        if (connectionId == null) {
            connectionId = "";
        }
        getConversation();
        msg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().trim().length() > 0) {
                    sendMessgae(message.getText().toString().trim(), null);
                }
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
            }
        });
        setToolBar();

    }

    @Override
    public void onResume() {
        super.onResume();
        registerChatNotification(ChatFragment.this);
    }

    private void getConversation() {
        getMessages = apiInterfaceAuth.getConversation(store.getInt(Const.USER_ID) + "", barberId, connectionId);
        apiHitAndHandle.makeApiCall(getMessages, ChatFragment.this);
    }


    void setData() {
        if (adapter == null) {
            adapter = new ChatAdapter(getActivity(), messageList, ChatFragment.this);
            LinearLayoutManager grid = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            grid.setStackFromEnd(true);
            chat_rv.setLayoutManager(grid);
            chat_rv.setAdapter(adapter);
            if (adapter != null && adapter.getItemCount() > 0) {
               // chat_rv.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        } else {
            adapter.notifyDataSetChanged();
            if (adapter != null && adapter.getItemCount() > 0) {
                //chat_rv.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        }


    }


    private void checkCameraPermission() {
        String[] arrayPermission = new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (checkPermissions(arrayPermission, 99, ChatFragment.this)) {
            new ImageUtils.ImageSelect.Builder(getActivity(), new ImageUtils.ImageSelectCallback() {
                @Override
                public void onImageSelected(String imagePath, int resultCode) {
                    imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        if (message.getText().toString().trim().length() > 0) {
                            sendMessgae(message.getText().toString().trim(), imageFile);
                        } else {
                            sendMessgae("", imageFile);
                        }

                    }
                }
            }, 200).crop().start();
        }
    }

    private void sendMessgae(String message, File image) {
        if (image != null) {
            sendMessage = apiInterfaceAuth.sendMessage(returnRequestBody(store.getInt(Const.USER_ID) + ""), returnRequestBody(barberId), returnRequestBody(message), returnRequestFile(image, "image"));
        } else {
            sendMessage = apiInterfaceAuth.sendMessage(returnRequestBody(store.getInt(Const.USER_ID) + ""), returnRequestBody(barberId), returnRequestBody(message));
        }
        apiHitAndHandle.makeApiCall(sendMessage, ChatFragment.this);

    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.pink));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes("Chat", R.mipmap.ic_back, false);
    }


    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == sendMessage) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    message.setText("");
                    message.requestFocus();
                    connectionId = result.getInt("connection_id") + "";
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    ConversationModel.Conversation conversationIs = gson.fromJson(result.getJSONObject("last_msg").toString(), ConversationModel.Conversation.class);
                    messageList.add(conversationIs);
                    if (adapter != null && adapter.getItemCount() > 0) {
                        adapter.notifyDataSetChanged();
                        //chat_rv.smoothScrollToPosition(adapter.getItemCount() - 1);
                    } else {
                        setData();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (call == getMessages) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            conversation = gson.fromJson(object.toString(), ConversationModel.class);
            if (conversation.getStatus() == 200) {
                if (conversation.getConversations().size() > 0) {
                    messageList.clear();
                    messageList.addAll(conversation.getConversations());
                    setData();
                }

            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        connectionId = "";
        unregisterNotification();
    }

    @Override
    public void message(String message, String recieverId, String connection_Id) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                getConversation();
            }
        });
    }

    @Override
    public void newConnection() {

    }

    @Override
    public void newAppointment(String bookingid) {

    }


    @Override
    public void permGranted(int resultCode) {
        new ImageUtils.ImageSelect.Builder(getActivity(), new ImageUtils.ImageSelectCallback() {
            @Override
            public void onImageSelected(String imagePath, int resultCode) {
                imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    if (message.getText().toString().trim().length() > 0) {
                        sendMessgae(message.getText().toString().trim(), imageFile);
                    } else {
                        sendMessgae("", imageFile);
                    }

                }
            }
        }, 200).crop().start();
    }

    @Override
    public void permDenied(int resultCode) {

    }
}
