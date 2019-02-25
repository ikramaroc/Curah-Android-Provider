package com.curahservice.netset.module.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.module.TimeSlots_Model;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.CalenderCellAdapter;
import com.curahservice.netset.module.base.InterfaceContainer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CalenderFragment extends BaseFragment implements InterfaceContainer.CalendarClickListener {

    private GridView calenderDaysGrid;
    private TextView monthNameCalenderTv, yearNumberCalenderTv;
    private RecyclerView activeJobsRv;
    private RelativeLayout gridLay;
    private String mTime;
    private String mCurrentDate;
    private Calendar _calendar;
    private int month, year;
    private int temp_month = 0, temp_year = 0;
    private String SelectedDate;
    private TimeSlots_Adapter adapter;
    private ArrayList<String> selectedList = new ArrayList<>();
    private CalenderCellAdapter calenderCellAdapter;
    private View inflate;
    private ImageView next, prev;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = getLayoutInflater().inflate(R.layout.fragment_calender, null);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initViews(view);
    }

    private void initViews(View view) {
        calenderDaysGrid = view.findViewById(R.id.weekday_gridview);
        monthNameCalenderTv = view.findViewById(R.id.txt_month_name);
        yearNumberCalenderTv = view.findViewById(R.id.txt_year_no);
        gridLay = view.findViewById(R.id.lay_grid);
        activeJobsRv = view.findViewById(R.id.time_slots_rv);

        selectedList.add("2018-07-08");
        selectedList.add("2018-07-15");
        selectedList.add("2018-07-22");
        selectedList.add("2018-07-23");
        selectedList.add("2018-07-29");
        setCalendarDates();
        setTimeAdapter();
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

    }

    private void setTimeAdapter() {
        ArrayList<TimeSlots_Model> list = new ArrayList<TimeSlots_Model>();
        TimeSlots_Model ModelData1 = new TimeSlots_Model("1", "09:00 AM - 10:00 AM");
        TimeSlots_Model ModelData2 = new TimeSlots_Model("2", "10:00 AM - 11:00 AM");
        TimeSlots_Model ModelData3 = new TimeSlots_Model("3", "11:00 AM - 12:00 PM");
        TimeSlots_Model ModelData4 = new TimeSlots_Model("4", "12:00 PM - 01:00 PM");
        TimeSlots_Model ModelData5 = new TimeSlots_Model("5", "01:00 PM - 02:00 PM");
        TimeSlots_Model ModelData6 = new TimeSlots_Model("6", "02:00 PM - 03:00 PM");
        TimeSlots_Model ModelData7 = new TimeSlots_Model("7", "03:00 PM - 04:00 PM");

        list.add(ModelData1);
        list.add(ModelData2);
        list.add(ModelData3);
        list.add(ModelData4);
        list.add(ModelData5);
        list.add(ModelData6);
        list.add(ModelData7);

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        adapter = new TimeSlots_Adapter(getActivity(), list);
        activeJobsRv.setLayoutManager(MyLayoutManager);
        activeJobsRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setCalendarDates() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        mTime = sdf.format(c.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        mCurrentDate = dateFormat.format(date);
        System.out.println(dateFormat.format(date));
        _calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        temp_month = month;
        temp_year = year;
        setGridCellAdapterToDate(month, year);

    }

    public void setGridCellAdapterToDate(int month, int year) {
        calenderCellAdapter = new CalenderCellAdapter(getActivity(), month, year, this, this, SelectedDate, selectedList);
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
        SelectedDate = date;
        setGridCellAdapterToDate(month, year);
    }
}
