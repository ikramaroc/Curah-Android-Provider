package com.curahservice.netset.module.myProfile_tabs;

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
import android.widget.Toast;

import com.curahservice.netset.R;
import com.curahservice.netset.model.TimeSlotModel;
import com.curahservice.netset.module.TimeSlots_Model;
import com.curahservice.netset.module.appointment.ApointmentAdapter;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.CalenderCellAdapter;
import com.curahservice.netset.module.base.InterfaceContainer;
import com.curahservice.netset.util.Const;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import retrofit2.Call;

public class ScheduleTab3 extends BaseFragment implements InterfaceContainer.CalendarClickListener, View.OnClickListener {

    private GridView calenderDaysGrid;
    private TextView monthNameCalenderTv, yearNumberCalenderTv;

    private String mTime;
    private String mCurrentDate;
    private Calendar _calendar;
    private int month, year;

    private String SelectedDate;
    TextView submit_TV;

    private ArrayList<String> selectedList = new ArrayList<>();
    private CalenderCellAdapter calenderCellAdapter;
    private TimeSlotAdapter timeSlotAdapter;
    private View inflate;
    private ImageView next, prev;
    private RecyclerView time_slots_rv;
    List<TimeSlotModel> timeSlotModelList;
    Call<JsonObject> appoinmentAdded, seeAppoinmemtApi,deleteAppoitnment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }


    private void initViews(View view) {
        calenderDaysGrid = view.findViewById(R.id.weekday_gridview);
        monthNameCalenderTv = view.findViewById(R.id.txt_month_name);
        yearNumberCalenderTv = view.findViewById(R.id.txt_year_no);
        time_slots_rv = view.findViewById(R.id.time_slots_rv);
        submit_TV = view.findViewById(R.id.submit_TV);
        submit_TV.setOnClickListener(this);
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

        setCalendarDates();
    }

    public void setCalendarDates() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        mTime = sdf.format(c.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        mCurrentDate = dateFormat.format(date);
        SelectedDate = mCurrentDate;
        System.out.println(dateFormat.format(date));
        _calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        setGridCellAdapterToDate(month, year);
        seeAppointment((SelectedDate));
    }

    public void setGridCellAdapterToDate(int month, int year) {
        calenderCellAdapter = new CalenderCellAdapter(getActivity(), month, year, this, this, SelectedDate, selectedList);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        monthNameCalenderTv.setText(DateFormat.format("MMMM", _calendar.getTime()));
        yearNumberCalenderTv.setText(DateFormat.format("yyyy", _calendar.getTime()));
        calenderDaysGrid.setAdapter(calenderCellAdapter);
    }


    public void setSlotAdapter(String mCurrentDate) {

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(getActivity());
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        timeSlotAdapter = new TimeSlotAdapter(getActivity(), timeSlotModelList, ScheduleTab3.this, mCurrentDate);
        time_slots_rv.setLayoutManager(MyLayoutManager);
        time_slots_rv.setAdapter(timeSlotAdapter);
    }


    private void seeAppointment(String date) {
        seeAppoinmemtApi = apiInterfaceAuth.seeAppointment(String.valueOf(store.getInt("userId")), String.valueOf(store.getInt("userId")), date);
        apiHitAndHandle.makeApiCall(seeAppoinmemtApi, this);
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
        seeAppointment(SelectedDate);
    }

    private void AddAppoinment(JSONObject service) {
        appoinmentAdded = apiInterfaceAuth.addTimeForAppointment(returnRequestBodyJson(service.toString()));
        apiHitAndHandle.makeApiCall(appoinmentAdded, this);
    }
    public void deleteAppoinment(String appoinmentId) {
        deleteAppoitnment = apiInterfaceAuth.deleteAppoitnment(String.valueOf(store.getInt("userId")),appoinmentId);
        apiHitAndHandle.makeApiCall(deleteAppoitnment, this);
    }

    @Override
    public void onSuccess(Call call, Object object) {
        super.onSuccess(call, object);
        Log.e("---------------", object.toString());
        if (call == seeAppoinmemtApi) {
            try {
                JSONObject result = new JSONObject(object.toString());
                JSONArray timeslots = result.getJSONArray("data");
               slotDistribution();
                timeSlotModelList = new ArrayList<>();
                if (timeslots.length() > 0) {
                    for (int i = 0; i < timeslots.length(); i++) {
                        JSONObject singleTime = timeslots.getJSONObject(i);
                        String from = changeDateFormat(singleTime.getString("date") + " " + singleTime.getString("start_time"), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd hh:mm a");
                        String id = singleTime.getString("appointmentTime_id");
                        String to = changeDateFormat(singleTime.getString("date") + " " + singleTime.getString("close_time"), "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd hh:mm a");
                        timeSlotModelList.add(new TimeSlotModel(from, to,id,singleTime.getString("status")));
                    }
                } else {
                    timeSlotModelList.add(new TimeSlotModel("", "","",""));
                }
                setSlotAdapter(SelectedDate);
            } catch (Exception e) {

            }

        } else if (call == appoinmentAdded) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    seeAppointment(SelectedDate);
                    Toast.makeText(getActivity(), "Services successfully added.", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {

            }

        }else if (call==deleteAppoitnment){

            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS)==200){
                    seeAppointment(SelectedDate);
                }else {
                    Toast.makeText(getActivity(),result.getString(Const.MESSAGE),Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private int getHoursValue(int hours){
        return hours - 12;
    }





    private void slotDistribution(){
        String timeValue = "2015-10-28T18:37:04.899+05:30";
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        try {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(sdf.parse(timeValue));

            if (startCalendar.get(Calendar.MINUTE) < 30) {
                startCalendar.set(Calendar.MINUTE, 30);
            } else {
                startCalendar.add(Calendar.MINUTE, 30); // overstep hour and clear minutes
                startCalendar.clear(Calendar.MINUTE);
            }

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(startCalendar.getTime());

            // if you want dates for whole next day, uncomment next line
            //endCalendar.add(Calendar.DAY_OF_YEAR, 1);

            Log.e("startCalendar-->",startCalendar.get(Calendar.HOUR_OF_DAY)+"");
            endCalendar.add(Calendar.HOUR_OF_DAY, 24 - startCalendar.get(Calendar.HOUR_OF_DAY));

            endCalendar.clear(Calendar.MINUTE);
            endCalendar.clear(Calendar.SECOND);
            endCalendar.clear(Calendar.MILLISECOND);

            SimpleDateFormat slotTime = new SimpleDateFormat("hh:mma");
            SimpleDateFormat slotDate = new SimpleDateFormat(", dd/MM/yy");
            while (endCalendar.after(startCalendar)) {
                String slotStartTime = slotTime.format(startCalendar.getTime());
                String slotStartDate = slotDate.format(startCalendar.getTime());

                startCalendar.add(Calendar.MINUTE, 60);
                String slotEndTime = slotTime.format(startCalendar.getTime());
                Log.d("DATE", slotStartTime + " - " + slotEndTime + slotStartDate);
            }

        } catch (ParseException e) {
            // date in wrong format
        }
    }



    private void displayTimeSlots(){
        String timeValue = "2015-10-28T18:37:04.899+05:30";
        StringTokenizer stringTokenizer = new StringTokenizer(timeValue,"T");
        String dateValue = stringTokenizer.nextElement().toString();
        String endDateValue = "2015-10-30";
        String restString= stringTokenizer.nextElement().toString();
        StringTokenizer secondTokeniser = new StringTokenizer(restString,":");
        String hours = secondTokeniser.nextElement().toString();
        String minutes = secondTokeniser.nextElement().toString();
        hours = String.valueOf(Integer.parseInt(hours) + 2);
        if (Integer.parseInt(minutes) > 30){
            minutes = "00";
        }else{
            minutes = "30";
        }

        String amOrPm;
        if(Integer.parseInt(hours) < 12){
            amOrPm = "AM";
        }else{
            amOrPm = "PM";
            hours = String.valueOf(getHoursValue(Integer.parseInt(hours)));
        }
        String time1 = hours + ":" + minutes + " " + amOrPm;
        String time2 = "12" + ":" + "00" + " AM ";
        String format = "yyyy-MM-dd hh:mm a";

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        try {
            Date dateObj1 = sdf.parse(dateValue + " " + time1);
            Date dateObj2 = sdf.parse(endDateValue + " " + time2);
            Log.d("TAG", "Date Start: " + dateObj1);
            Log.d("TAG", "Date End: " + dateObj2);
            long dif = dateObj1.getTime();
            while (dif < dateObj2.getTime()) {
                Date slot1 = new Date(dif);
                dif += 3600000;
                Date slot2 = new Date(dif);
                dif += 3600000;
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a, dd/MM/yy");
                Log.d("TAG", "Hour slot = " + sdf1.format(slot1) + " - " + sdf2.format(slot2));
            }
        }catch (ParseException ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_TV:

                try {
                    JSONArray timeSlot = new JSONArray();
                    if (timeSlotModelList!=null && timeSlotModelList.size()==1 && timeSlotModelList.get(0).getFrom().length()==0){
                        Toast.makeText(getActivity(),"Please select start and end time slot",Toast.LENGTH_LONG).show();
                    }else {
                        for (int i = 0; i < timeSlotModelList.size(); i++) {
                            TimeSlotModel model = timeSlotModelList.get(i);
                            if (model.getFrom().length() > 0 && model.getTo().length() > 0) {
                                JSONObject timeslot = new JSONObject();
                                timeslot.put("start_time", changeDateFormat(model.getFrom(), "yyyy-MM-dd hh:mm a", "HH:mm:ss"));
                                timeslot.put("close_time", changeDateFormat(model.getTo(), "yyyy-MM-dd hh:mm a", "HH:mm:ss"));
                                timeSlot.put(timeslot);

                            }
                        }
                        JSONObject appoinment = new JSONObject();
                        appoinment.put("provider_id", String.valueOf(store.getInt(Const.USER_ID)));
                        appoinment.put("date", SelectedDate);
                        appoinment.put("times", timeSlot);
                        Log.e("----Appoinment--", appoinment.toString());

                        if (timeSlot.length() > 0) {
                            AddAppoinment(appoinment);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }
    }
}
