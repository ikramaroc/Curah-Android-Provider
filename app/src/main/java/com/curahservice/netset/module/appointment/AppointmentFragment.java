package com.curahservice.netset.module.appointment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.curahservice.netset.MyFirebaseMessagingService;
import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.ProviderPojo;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.CalenderCellAdapter;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.base.InterfaceContainer;
import com.curahservice.netset.module.history.HistoryDetailFragment;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;

public class AppointmentFragment extends BaseFragment implements InterfaceContainer.CalendarClickListener,MyFirebaseMessagingService.notificationListner {

    private GridView calenderDaysGrid;
    private TextView monthNameCalenderTv, yearNumberCalenderTv;
    private RecyclerView activeJobsRv;
    private RelativeLayout gridLay;
    private String mTime;
    private String selectedDate;

    private Calendar _calendar;
    private int month, year;
    private int temp_month = 0, temp_year = 0;

    private ApointmentAdapter adapter;
    private ArrayList<String> selectedList = new ArrayList<>();
    private CalenderCellAdapter calenderCellAdapter;
    private View inflate;
    private ImageView next, prev;
    Call<JsonObject> providerAppointmentApi;
    TextView noAppointment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = getLayoutInflater().inflate(R.layout.fragment_calender, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initViews(view);
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

    private void getProviderAppointmentApi(String selectedDate) {
        providerAppointmentApi = apiInterfaceAuth.seeProviderAppointment(String.valueOf(store.getInt("userId")), selectedDate);
        apiHitAndHandle.makeApiCall(providerAppointmentApi, this);

    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        if (call == providerAppointmentApi) {
            Gson gson = new GsonBuilder().serializeNulls().create();
            ProviderPojo appointmentGsonModel = gson.fromJson(object.toString(), ProviderPojo.class);
            if (appointmentGsonModel.getStatus() == 200) {
                noAppointment.setVisibility(View.GONE);
                activeJobsRv.setVisibility(View.VISIBLE);
                setTimeAdapter(appointmentGsonModel.getProviderAppointment());
            } else if (appointmentGsonModel.getStatus() == 404) {
                activeJobsRv.setVisibility(View.GONE);
                noAppointment.setVisibility(View.VISIBLE);
                noAppointment.setText("No result found for a day.");
            }


//{"status":404,"msg":"no request found"}

        }
    }


    private void initViews(View view) {
        calenderDaysGrid = view.findViewById(R.id.weekday_gridview);
        monthNameCalenderTv = view.findViewById(R.id.txt_month_name);
        yearNumberCalenderTv = view.findViewById(R.id.txt_year_no);
        gridLay = view.findViewById(R.id.lay_grid);
        activeJobsRv = view.findViewById(R.id.time_slots_rv);
        noAppointment = view.findViewById(R.id.noAppointment);
        next = view.findViewById(R.id.next);
        prev = view.findViewById(R.id.prev);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonthCalender();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonthCalender();
            }
        });



        selectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        setCalendarDates();
        getProviderAppointmentApi(selectedDate);

    }

    private void setTimeAdapter(List<ProviderPojo.ProviderAppointment> list) {

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new ApointmentAdapter(getActivity(), list, this);
        activeJobsRv.setLayoutManager(MyLayoutManager);
        activeJobsRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        activeJobsRv.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        //  Toast.makeText(getContext(),"Current Position----"+position,Toast.LENGTH_LONG).show();
                    }
                })
        );
    }

    private void setToolBar() {
        StatusBarUtil.setColorNoTranslucent(getActivity(), getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarVisibilty(View.VISIBLE);
        ((DrawerActivity) getActivity()).setToolbarBackground(getResources().getColor(R.color.colorPrimary));
        ((DrawerActivity) getActivity()).setToolbarAttributes(getString(R.string.myAppoitment), R.mipmap.ic_menu, true);
    }

    public void setCalendarDates() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        mTime = sdf.format(c.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        _calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        temp_month = month;
        temp_year = year;
        setGridCellAdapterToDate(month, year);
    }

    public void setGridCellAdapterToDate(int month, int year) {
        calenderCellAdapter = new CalenderCellAdapter(getActivity(), month, year, this, this, selectedDate, selectedList);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        monthNameCalenderTv.setText(DateFormat.format("MMMM", _calendar.getTime()));
        yearNumberCalenderTv.setText(DateFormat.format("yyyy", _calendar.getTime()));
        calenderDaysGrid.setAdapter(calenderCellAdapter);
    }

    private void previousMonthCalender() {
        if (month <= 1) {
            month = 12;
            year--;
        } else {
            month--;
        }
        setGridCellAdapterToDate(month, year);
    }

    private void nextMonthCalender() {
        if (month > 11) {
            month = 1;
            year++;
        } else {
            month++;
        }
        setGridCellAdapterToDate(month, year);
    }

    @Override
    public void onCalDateClickListener(int position, String date) {
        selectedDate=date;
        setGridCellAdapterToDate(month, year);
        getProviderAppointmentApi(selectedDate);
    }

    @Override
    public void message(String message, String recieverId, String connectionId) {

    }

    @Override
    public void newConnection() {

    }

    @Override
    public void newAppointment(String bookingid) {
        getProviderAppointmentApi(selectedDate);
    }


}
