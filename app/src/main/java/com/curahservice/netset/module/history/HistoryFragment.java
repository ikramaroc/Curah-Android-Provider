package com.curahservice.netset.module.history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curahservice.netset.MyFirebaseMessagingService;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.AppoinmentData;
import com.curahservice.netset.gsonModel.ProviderPojo;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;

public class HistoryFragment extends BaseFragment implements MyFirebaseMessagingService.ratingInterface {

    private RecyclerView recycle_item;
    private ArrayList<HistoryModel> histor;
    Button view_receipt;
    LinearLayout rate;
    View view;
    TextView noHistory;
    public com.curahservice.netset.gsonModel.HistoryModel history;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.recycle_view, container, false);
            recycle_item = view.findViewById(R.id.recycle_item);
            noHistory = view.findViewById(R.id.noHistory);
            getHistory(HistoryFragment.this);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setToolBar();
        MyFirebaseMessagingService.registerRating(HistoryFragment.this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyFirebaseMessagingService.unregisterRating();
    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == historyApi) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            history = gson.fromJson(object.toString(), com.curahservice.netset.gsonModel.HistoryModel.class);
            if (history.getStatus() == 200) {
                if (history.getHistory().size() > 0) {
                    noHistory.setVisibility(View.GONE);
                    recycle_item.setVisibility(View.VISIBLE);
                    Collections.sort(history.getHistory(),COMPARE_BY_LOWEST);
                    setData(history.getHistory());

                } else {
                    noHistory.setVisibility(View.VISIBLE);
                    recycle_item.setVisibility(View.GONE);
                    noHistory.setText("No History Found");
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

    public static Comparator<com.curahservice.netset.gsonModel.HistoryModel.History> COMPARE_BY_LOWEST = new Comparator<com.curahservice.netset.gsonModel.HistoryModel.History>() {
        @Override
        public int compare(com.curahservice.netset.gsonModel.HistoryModel.History t1, com.curahservice.netset.gsonModel.HistoryModel.History t2) {
            return (t2.getDate()+ t2.getStartTime()).compareTo((t1.getDate()+ t1.getStartTime()));
        }
    };



    void setData(final List<com.curahservice.netset.gsonModel.HistoryModel.History>historyList) {
        HistoryAdapter adapter = new HistoryAdapter(getActivity(), historyList, this);
        LinearLayoutManager grid = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycle_item.setLayoutManager(grid);
        recycle_item.setAdapter(adapter);
        recycle_item.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click


                        openAppointmentDetail(historyList.get(position).getBookingId() + "", HistoryFragment.this);
                        //  replaceFragment(R.id.fl_container_home, new HistoryDetailFragment(), "History", null);

                        //  Toast.makeText(getContext(),"Current Position----"+position,Toast.LENGTH_LONG).show();
                    }
                })
        );

    }


    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes("History", R.mipmap.ic_menu, true);
    }

    @Override
    public void rating() {
        getHistory(HistoryFragment.this);
    }
}
