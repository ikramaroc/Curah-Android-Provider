package com.curahservice.netset.module.history;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.curahservice.netset.MyFirebaseMessagingService;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.AppoinmentData;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.chat.ChatFragment;
import com.curahservice.netset.retrofitManager.ApiResponse;
import com.curahservice.netset.util.Const;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONObject;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class HistoryDetailFragment extends BaseFragment implements ApiResponse, MyFirebaseMessagingService.notificationListner,MyFirebaseMessagingService.ratingInterface {

    @BindView(R.id.history_RecyclerView)
    RecyclerView history_RecyclerView;

    @BindView(R.id.feedBackLayout)
    LinearLayout feedBackLayout;

    @BindView(R.id.buttonOne)
    Button buttonOne;


    @BindView(R.id.buttonTwo)
    Button buttonTwo;


    @BindView(R.id.service_cost)
    TextView service_cost;


    @BindView(R.id.date)
    TextView date;


    @BindView(R.id.time)
    TextView time;


    @BindView(R.id.description)
    TextView description;

    @BindView(R.id.providerName)
    TextView providerName;

    @BindView(R.id.distance)
    TextView distance;

    @BindView(R.id.rating_RB)
    RatingBar rating_RB;

    @BindView(R.id.userImage)
    ImageView userImage;

    @BindView(R.id.provider_message)
    ImageView provider_message;

    @BindView(R.id.provider_call)
    ImageView provider_call;


    @BindView(R.id.yourFeedbackToCustomer_TV)
    TextView yourFeedbackToCustomer_TV;

    @BindView(R.id.customerFeedBackToYou_TV)
    TextView customerFeedBackToYou_TV;

    @BindView(R.id.customer_comment_TV)
    TextView customer_comment_TV;

    @BindView(R.id.your_comment_TV)
    TextView your_comment_TV;

    @BindView(R.id.customer_RB)
    RatingBar customer_RB;

    @BindView(R.id.your_rating_RB)
    RatingBar your_rating_RB;


    @BindView(R.id.RejectMessage)
    TextView RejectMessage;

    private RadioButton busyWithothertask_RB;
    private RadioButton otherReason_RB;


    @BindView(R.id.CancelReasonLL)
    LinearLayout CancelReasonLL;


    LinearLayout reasonLay;
    @BindView(R.id.reason_message)
    TextView reason_message;

    @BindView(R.id.messageCancel)
    TextView messageCancel;

    private Button cancelTV;
    private TextView submitTV, price;
    private EditText reasonEDT;
    RadioGroup radioGroup;
    Dialog dialogBox;


    String tag;
    private boolean accpted = false;
    private boolean started = false;

    @BindView(R.id.accptRejectViewBtn)
    LinearLayout accptRejectViewBtn;
    AppoinmentData detail;
    private String bookigId;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       if (view==null){
           view=inflater.inflate(R.layout.history_detail, container, false);
           setHasOptionsMenu(true);
           ButterKnife.bind(this, view);
           setToolBar();
           if (getArguments() != null && getArguments().getString("type") != null) {
               if (getArguments().getString("type").equals("Request-Notification") || getArguments().getString("type").equals("Cancel-Notification")) {

                 MyFirebaseMessagingService.bookingId_Is=getArguments().getString("bookid");
                   openAppointmentDetail(getArguments().getString("bookid"), HistoryDetailFragment.this);
               }
           } else {
               detail = (AppoinmentData) getArguments().getSerializable("detail");
               setDetatilData();

           }
       }


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        MyFirebaseMessagingService.registerChatNotification(HistoryDetailFragment.this);
        MyFirebaseMessagingService.registerRating(HistoryDetailFragment.this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyFirebaseMessagingService.unregisterNotification();
        MyFirebaseMessagingService.bookingId_Is="-1";
        MyFirebaseMessagingService.unregisterRating();
    }

    private void setDetatilData() {
        RejectMessage.setVisibility(View.GONE);
        bookigId = detail.getAppointmentDetails().getBookingId().toString();
        MyFirebaseMessagingService.bookingId_Is=bookigId;
        setAdapter_myServices(history_RecyclerView, detail.getAppointmentDetails().getServiceName(), false);
        service_cost.setText(Const.CURRENCY + detail.getAppointmentDetails().getPrice());
        date.setText(changeDateFormat(detail.getAppointmentDetails().getDate(), "yyyy-MM-dd", "dd MMM yyyy"));
        time.setText(
                changeDateFormat(detail.getAppointmentDetails().getStartTime(), "HH:mm:ss", "hh:mm a") + " - "
                        + changeDateFormat(detail.getAppointmentDetails().getCloseTime(), "HH:mm:ss", "hh:mm a"));
        String description = detail.getAppointmentDetails().getDescription();
        this.description.setText(description);
        if (detail.getAppointmentDetails().getUserDetails().getRating() != null) {
            rating_RB.setRating(Float.valueOf(detail.getAppointmentDetails().getUserDetails().getRating()));
        } else {
            rating_RB.setRating(0);
        }
        distance.setText(detail.getAppointmentDetails().getUserDetails().getDistance() + " miles away");
        Glide.with(getActivity())
                .load(detail.getImgUrl() + detail.getAppointmentDetails().getUserDetails().getProfileImage())
                .apply(new RequestOptions()
                        .dontAnimate().
                                error(R.mipmap.user_img))
                .into(userImage);
        provider_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(detail.getAppointmentDetails().getUserDetails().getPhone());
            }
        });
        provider_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatFragment fragmentToOpen = new ChatFragment();
                Bundle data = new Bundle();
                data.putString("id", detail.getAppointmentDetails().getUserDetails().getId() + "");
                data.putString("connectionId", detail.getAppointmentDetails().getUserDetails().getConnection_id() + "");
                fragmentToOpen.setArguments(data);
                replaceFragment(R.id.fl_container_home, fragmentToOpen, "", data);
            }
        });
        providerName.setText(detail.getAppointmentDetails().getUserDetails().getFirstname());
        if (detail.getAppointmentDetails().getStatus().equals("P")) {
            accptRejectViewBtn.setVisibility(View.VISIBLE);
            buttonTwo.setText("Accept");
            buttonOne.setText("Reject");
            buttonOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accpted = false;
                    acceptRejectApi(detail.getAppointmentDetails().getBookingId().toString(), "R", HistoryDetailFragment.this);
                }
            });
            buttonTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accpted = true;
                    acceptRejectApi(detail.getAppointmentDetails().getBookingId().toString(), "A", HistoryDetailFragment.this);

                }
            });
        } else if (detail.getAppointmentDetails().getStatus().equals("A")) {
            accptRejectViewBtn.setVisibility(View.VISIBLE);
            buttonTwo.setText("Start Service");
            buttonOne.setText("Cancel Service");
            buttonOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    layoutCancelAppointment();

                }
            });
            buttonTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    started = true;
                    startService(detail.getAppointmentDetails().getBookingId().toString(), HistoryDetailFragment.this);

                }
            });
        } else if (detail.getAppointmentDetails().getStatus().equals("R")) {
            accptRejectViewBtn.setVisibility(View.INVISIBLE);
            RejectMessage.setVisibility(View.VISIBLE);
            RejectMessage.setText("You have rejected the Service");

        } else if (detail.getAppointmentDetails().getStatus().equals("I")) {
            accptRejectViewBtn.setVisibility(View.VISIBLE);
            buttonOne.setText("End Service");
            buttonOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detail.getAppointmentDetails().setStatus("C");
                    endService(detail.getAppointmentDetails().getBookingId() + "", HistoryDetailFragment.this);
                }
            });
            buttonTwo.setVisibility(View.GONE);

        } else if (detail.getAppointmentDetails().getStatus().equals("K")) {
            accptRejectViewBtn.setVisibility(View.GONE);
            feedBackLayout.setVisibility(View.GONE);
            CancelReasonLL.setVisibility(View.VISIBLE);
            provider_message.setVisibility(View.GONE);
            provider_call.setVisibility(View.GONE);
            reason_message.setText(detail.getAppointmentDetails().getCancel_description());
            if (detail.getAppointmentDetails().getCancel_by().equals("C")) {
                messageCancel.setText(getString(R.string.customerCancel));
            } else {
                messageCancel.setText(getString(R.string.youCancel));
            }
        } else if (detail.getAppointmentDetails().getStatus().equals("C")) {
            accptRejectViewBtn.setVisibility(View.VISIBLE);
            buttonOne.setVisibility(View.VISIBLE);
            feedBackLayout.setVisibility(View.VISIBLE);
            if (Const.USER_TYPE.equals("S")) {
                if (detail.getRating().getCustomerMessage() != null) {
                    your_comment_TV.setText(detail.getRating().getCustomerMessage());
                    if (detail.getRating().getProviderRating() != null) {
                        your_rating_RB.setRating(Float.valueOf(detail.getRating().getCustomerRating()));
                    } else {
                        your_rating_RB.setRating(0);
                    }
                }
                if (detail.getRating().getProviderMessage() != null) {
                    customer_comment_TV.setText(detail.getRating().getProviderMessage());
                    if (detail.getRating().getProviderRating() != null) {
                        customer_RB.setRating(Float.valueOf(detail.getRating().getProviderRating()));
                    } else {
                        customer_RB.setRating(0);
                    }
                }
                if (detail.getRating().getCustomerMessage() != null && detail.getRating().getCustomerMessage().length() == 0) {
                    rateLayout(detail.getAppointmentDetails().getUserDetails().getId() + "", detail.getAppointmentDetails().getBookingId() + "");

                }
            }
            buttonOne.setText(getString(R.string.view_receipt));
            buttonOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReceiptFragment reciept = new ReceiptFragment();
                    Bundle data = new Bundle();
                    data.putSerializable("service", detail.getAppointmentDetails());
                    reciept.setArguments(data);
                    replaceFragment(R.id.fl_container_home, reciept, "ReceiptFragment", data);
                }
            });
            buttonTwo.setVisibility(View.GONE);
        }


    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == acceptRejectApi) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    if (accpted) {
                        detail.getAppointmentDetails().setStatus("A");
                        setDetatilData();
                    } else {
                        detail.getAppointmentDetails().setStatus("R");
                        setDetatilData();
                    }

                }else {
                    showMessage(getActivity(), "", result.getString(Const.MESSAGE), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (call == cancelServiceApi) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    detail.getAppointmentDetails().setStatus("K");
                    detail.getAppointmentDetails().setCancel_by("S");
                    accptRejectViewBtn.setVisibility(View.INVISIBLE);
                    feedBackLayout.setVisibility(View.GONE);
                    CancelReasonLL.setVisibility(View.VISIBLE);
                    provider_message.setVisibility(View.GONE);
                    provider_call.setVisibility(View.GONE);
                    messageCancel.setText(getString(R.string.youCancel));
                    reason_message.setText(reasonMessage);
                    //setDetatilData();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (call == startServiceApi) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    detail.getAppointmentDetails().setStatus("I");
                    setDetatilData();
                } else {
                    showMessage(getActivity(), "", result.getString(Const.MESSAGE), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (call == endServiceApi) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    buttonOne.setVisibility(View.INVISIBLE);
                    setDetatilData();
                    rateLayout(detail.getAppointmentDetails().getUserDetails().getId() + "", detail.getAppointmentDetails().getBookingId() + "");

                } else {
                    showMessage(getActivity(), "", result.getString(Const.MESSAGE), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (call == rateUser) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {

                    Toast.makeText(getActivity(), result.getString(Const.MESSAGE), Toast.LENGTH_LONG).show();
                    openAppointmentDetail(detail.getAppointmentDetails().getBookingId() + "", HistoryDetailFragment.this);
                } else {
                    showMessage(getActivity(), "", result.getString(Const.MESSAGE), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (call == appointmentDeatil) {
            try {
                hideSoftKeyboard(getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }

            Gson gson = new GsonBuilder().serializeNulls().create();
            detail = gson.fromJson(object.toString(), AppoinmentData.class);
            setDetatilData();

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes(getString(R.string.details), R.mipmap.ic_back, false);
    }

    @OnClick(R.id.buttonOne)
    void viewReceipt() {
        if (buttonOne.getText().toString().equals(getString(R.string.view_receipt))) {
            ReceiptFragment reciept = new ReceiptFragment();
            Bundle data = new Bundle();
            data.putSerializable("service", detail.getAppointmentDetails());
            reciept.setArguments(data);
            replaceFragment(R.id.fl_container_home, reciept, "ReceiptFragment", data);
        } else if (buttonOne.getText().toString().equals(getString(R.string.cancel))) {
            accptRejectViewBtn.setVisibility(View.INVISIBLE);
        } else if (buttonOne.getText().toString().equals(getString(R.string.cancel_service))) {
            // layoutCancelAppointment();
        } else if (buttonOne.getText().toString().equals(getString(R.string.end_service))) {
            rateLayout(detail.getAppointmentDetails().getUserDetails().getId() + "", detail.getAppointmentDetails().getBookingId() + "");
        }
    }


    int index;
    private String reasonMessage = "";

    private void layoutCancelAppointment() {
        dialogBox = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
        dialogBox.setContentView(R.layout.alert_cancel_appointment);
        dialogBox.setCancelable(true);
        busyWithothertask_RB = dialogBox.findViewById(R.id.busyWithothertask_RB);
        otherReason_RB = dialogBox.findViewById(R.id.otherReason_RB);
        reasonLay = dialogBox.findViewById(R.id.reason_lay);
        cancelTV = dialogBox.findViewById(R.id.cancel_Btn);
        submitTV = dialogBox.findViewById(R.id.submit_Btn);
        reasonEDT = dialogBox.findViewById(R.id.edt_reason);
        radioGroup = dialogBox.findViewById(R.id.radio_group);
        index = 0;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                View radioButton = radioGroup.findViewById(checkedId);
                index = radioGroup.indexOfChild(radioButton);
                // Add logic here
                switch (index) {
                    case 0: // first button
                        reasonLay.setVisibility(View.GONE);
                        break;
                    case 1: // secondbutton

                        reasonLay.setVisibility(View.VISIBLE);
                        break;
                }


            }
        });


        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBox.dismiss();
            }
        });
        submitTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index == 0) {
                    reason_message.setText("Service Provider have not accept the request.");
                    reasonMessage = reason_message.getText().toString().trim();
                    cancelService(detail.getAppointmentDetails().getBookingId() + "", reasonMessage, HistoryDetailFragment.this);
                    dialogBox.dismiss();
                } else {
                    if (reasonEDT.getText().toString().trim().length() == 0) {
                        reasonEDT.setError("Enter message...");
                        reasonEDT.requestFocus();
                    } else {
                        reasonMessage = reasonEDT.getText().toString().trim();
                        started = false;
                        cancelService(detail.getAppointmentDetails().getBookingId() + "", reasonMessage, HistoryDetailFragment.this);
                        dialogBox.dismiss();
                    }
                }

            }
        });

        dialogBox.show();
    }


    @Override
    public void message(String message, String recieverId, String connectionId) {

    }

    @Override
    public void newConnection() {

    }

    @Override
    public void newAppointment(String bookingid) {

        if (bookingid.equals(bookigId)) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    NotificationManager nMgr = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                    nMgr.cancelAll();
                    openAppointmentDetail(bookigId, HistoryDetailFragment.this);
                }
            });
        }

    }

    @Override
    public void rating() {
        openAppointmentDetail(bookigId, HistoryDetailFragment.this);

    }
}