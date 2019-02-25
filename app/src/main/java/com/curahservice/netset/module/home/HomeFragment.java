package com.curahservice.netset.module.home;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curahservice.netset.MyFirebaseMessagingService;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.ProviderPojo;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.history.HistoryDetailFragment;
import com.curahservice.netset.module.history.HistoryFragment;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment implements MyFirebaseMessagingService.notificationListner {

    private RecyclerView recyclerView;
    Call<JsonObject> appointmentApi;
    LinearLayout nodata, appointment_list;
    TextView title, startEndTime, end_service_TV;
    ProviderPojo appointmentGsonModel;
    String type, bookingId;
    View view;
    List<ProviderPojo.ProviderAppointment> newAppoinmentList = new ArrayList<>();
    HomeRecyclerAdapter mAdapter;
    int i;

    @SuppressLint("ValidFragment")
    public HomeFragment(String type, String id) {
        this.type = type;
        this.bookingId = id;


    }

    public HomeFragment() {
        this.type = null;
        this.bookingId = null;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if (view == null) {
            view = inflater.inflate(R.layout.home, container, false);
            recyclerView = view.findViewById(R.id.services_list);
            appointment_list = view.findViewById(R.id.appointment_list);
            nodata = view.findViewById(R.id.nodata);
            title = view.findViewById(R.id.title);
            startEndTime = view.findViewById(R.id.startEndTime);
            end_service_TV = view.findViewById(R.id.end_service_TV);
            getNewRequestAppointment(true);
            Log.e("---Print --", i + "-----i is");
            } else {
            getNewRequestAppointment(false);
        }


        return view;
    }





    @Override
    public void onResume() {
        super.onResume();
        setToolBar();
        MyFirebaseMessagingService.registerChatNotification(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyFirebaseMessagingService.unregisterNotification();
    }

    public void getNewRequestAppointment(boolean loader) {
        appointmentApi = apiInterfaceAuth.newAppointmentsApi(String.valueOf(store.getInt("userId")));
        apiHitAndHandle.makeApiCall(appointmentApi, loader, this);
    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == appointmentApi) {

            Gson gson = new GsonBuilder().serializeNulls().create();
            appointmentGsonModel = gson.fromJson(object.toString(), ProviderPojo.class);
            if (appointmentGsonModel.getStatus() == 200) {
                nodata.setVisibility(View.GONE);
                appointment_list.setVisibility(View.VISIBLE);

                newAppoinmentList.clear();
                if (appointmentGsonModel.getProviderAppointment().size() > 0) {
                    newAppoinmentList.addAll(appointmentGsonModel.getProviderAppointment());
                    setAdapter();
                } else {
                    nodata.setVisibility(View.VISIBLE);
                    appointment_list.setVisibility(View.GONE);
                }
                if (appointmentGsonModel.getInprocessService().getServiceName() != null) {
                    startEndTime.setVisibility(View.VISIBLE);
                    end_service_TV.setVisibility(View.VISIBLE);
                    end_service_TV.setText("End Service");
                    startEndTime.setText(
                            changeDateFormat(appointmentGsonModel.getInprocessService().getStartTime(), "HH:mm:ss", "hh:mm a") + " - "
                                    + changeDateFormat(appointmentGsonModel.getInprocessService().getCloseTime(), "HH:mm:ss", "hh:mm a"));
                    title.setText(appointmentGsonModel.getInprocessService().getServiceName());

                    end_service_TV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            endService(appointmentGsonModel.getInprocessService().getBookingId() + "", HomeFragment.this);
                        }
                    });


                } else {
                    startEndTime.setVisibility(View.GONE);
                    end_service_TV.setVisibility(View.GONE);
                    title.setText("No service in progress");
                }

                if (bookingId != null) {
                    Fragment fragmentToOpen = new HistoryDetailFragment();
                    Bundle data = new Bundle();
                    data.putString("type", type);
                    data.putString("bookid", bookingId);
                    fragmentToOpen.setArguments(data);
                    bookingId = null;
                    type = null;
                    replaceFragment(R.id.fl_container_home, fragmentToOpen, "", data);
                }


            }
        } else if (call == endServiceApi) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    startEndTime.setVisibility(View.GONE);
                    end_service_TV.setVisibility(View.GONE);
                    title.setText("No service in progress");
                    rateLayout(appointmentGsonModel.getInprocessService().getCustomerId() + "", appointmentGsonModel.getInprocessService().getBookingId() + "");

                } else if (result.getInt(Const.STATUS) == 402) {
                    showMessage(getContext(), "", result.getString(Const.MESSAGE), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new HomeRecyclerAdapter(getActivity(), newAppoinmentList, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes(getString(R.string.home), R.mipmap.ic_menu, true);
    }

    @Override
    public void message(String message, String recieverId, String connectionId) {

    }

    @Override
    public void newConnection() {

    }

    @Override
    public void newAppointment(String bookingid) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                getNewRequestAppointment(true);
            }
        });


    }


}
