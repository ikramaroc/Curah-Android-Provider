package com.curahservice.netset.module.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.curahservice.netset.R;
import com.curahservice.netset.util.DayEntityModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class CalenderCellAdapter extends BaseAdapter {
    private Context context;
    private int resource;
    private ArrayList<DayEntityModel> daysList = new ArrayList<DayEntityModel>();;
    private int daysInMonth;
    private TextView gridcell,counterTv;
    private RelativeLayout parentLay;
    private View bookedDateView;
    private boolean isClicked = false;
    private int clickedItem = -1;
    private int month, year;
    private String date;
    private String mTime;
    private String mCurrentDate;
    private ImageView im_isevent;
    private String selectedDate;
    private Calendar _calendar;
    private InterfaceContainer.CalendarClickListener calenderClickInterface;
    private Fragment calenderFragment;
    private ArrayList<String> MyDateList = new ArrayList<>();
    private ArrayList<String> SelectDateList = new ArrayList<>();
    private ArrayList<String> byDefaultSelectDateList = new ArrayList<>();

    public CalenderCellAdapter(Context context, int month, int year, InterfaceContainer.CalendarClickListener calenderClickInterface, Fragment calenderFragment, String Selecteddate, ArrayList<String> selectedlist) {
        super();
        this.context = context;
        this.calenderFragment=calenderFragment;
        this.calenderClickInterface=calenderClickInterface;
        printMonth(month, year);
        month = _calendar.get(Calendar.MONTH) + 1;
        year = _calendar.get(Calendar.YEAR);
        Calendar c = Calendar.getInstance();
        this.selectedDate = Selecteddate;
        this.SelectDateList = selectedlist;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        mTime = sdf.format(c.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        mCurrentDate = dateFormat.format(date);
        _calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());

    }

    private int getNumberOfDaysOfMonth(int year, int month) {
        return totalMonthDays(year, month);
    }

    @Override
    public int getCount() {
        return daysList.size();
    }

    private DayEntityModel getDayEntity(int year, int month,
                                        int day,
                                        boolean isChecked,
                                        boolean isCurrentDay,
                                        boolean isCurrentMonthDay) {
        DayEntityModel entity = new DayEntityModel();
        String mm = "";
        String dd = "";
        if (month < 10) {
            mm = "0" + month;
        } else {
            mm = "" + month;
        }
        if (day < 10) {
            dd = "0" + day;
        } else {
            dd = "" + day;
        }
        entity.date = year + "-" + mm + "-" + dd;
        entity.isChecked = isChecked;
        entity.isCurrentMonthDay = isCurrentMonthDay;
        entity.isCurrentDay = isCurrentDay;
        entity.year = String.valueOf(year);
        entity.month = String.valueOf(month);
        entity.day = String.valueOf(day);
        return entity;
    }

    private void printMonth(int mm, int yy) {
        date = yy + "-" + mm + "-" + "01";
        int trailingSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;
        int currentMonth2 = mm - 1;
        int currentMonth = mm;
        daysInMonth = getNumberOfDaysOfMonth(yy, currentMonth2);
        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth2, 1);
        if (currentMonth2 == 11) {
            prevMonth = currentMonth2 - 1;
            daysInPrevMonth = getNumberOfDaysOfMonth(yy, prevMonth);
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
        } else if (currentMonth2 == 0) {
            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(yy, prevMonth);
            nextMonth = 1;
        } else {
            prevMonth = currentMonth2 - 1;
            nextMonth = currentMonth2 + 1;
            nextYear = yy;
            prevYear = yy;
            daysInPrevMonth = getNumberOfDaysOfMonth(yy, prevMonth);
        }
        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;
        // Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {
            DayEntityModel entity = getDayEntity(prevYear, prevMonth,
                    ((daysInPrevMonth - trailingSpaces + 1) + i), false,
                    false, false);
            daysList.add(entity);
        }
        // Current Month Days
        for (int i = 1; i <= daysInMonth; i++) {
            DayEntityModel entity = getDayEntity(yy, currentMonth, i, false,
                    false, true);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date1 = null;
            Date date2 = null;
            try {
                date1 = sdf.parse(getCurrentDate("yyyy-MM-dd"));
                date2 = sdf.parse(entity.date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (getCurrentDate("yyyy-MM-dd").equalsIgnoreCase(entity.date)) {
                entity.isCurrentDay = true;
            }


            _calendar = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
            month = _calendar.get(Calendar.MONTH) + 1;
            if(mm == month){
                if(date2.before(date1)){
                    entity.isPastDay = true;
                }
            }else{
                entity.isPastDay = false;
            }
            daysList.add(entity);
        }
        // Leading Month days
        for (int i = 0; i < daysList.size() % 7; i++) {
            DayEntityModel entity = getDayEntity(nextYear, nextMonth, i + 1,
                    false, false, false);
            daysList.add(entity);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position,
                        View convertView,
                        ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_date_cell, null);
        }
        // Get a reference to the Day grid cell
        gridcell = (TextView) row.findViewById(R.id.calendar_tv);
        parentLay=row.findViewById(R.id.parent_rl);
        // Set the Day GridCell
        gridcell.setText(daysList.get(position).day);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkDateIs(mCurrentDate, daysList.get(position).date)) {
                    if (!daysList.get(position).isCurrentMonthDay) {
                        // CustomLogs.e("date not selected", "date not selected");
                    } else {
                        clickedItem = position;
                        doCellAction(position);
                        notifyDataSetChanged();
                    }
                }
            }

            private boolean checkDateIs(String mCurrentDate, String date) {
                boolean isDate = false;
                try {
                    // Create 2 dates starts
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = sdf.parse(sdf.format(new Date()));
                    Date date2 = sdf.parse(date);

                    System.out.println("Date1" + sdf.format(date1));
                    System.out.println("Date2" + sdf.format(date2));
                    System.out.println();
                    // Create 2 dates ends
                    // Date object is having 3 methods namely after,before and equals for comparing
                    // after() will return true if and only if date1 is after date 2
                    if (date1.after(date2)) {
                        System.out.println("Date1 is after Date2");
                        isDate = false;
                    } else
                        // before() will return true if and only if date1 is before date2
                        if (date1.before(date2)) {
                            System.out.println("Date1 is before Date2");
                            isDate = true;
                        } else
                            //equals() returns true if both the dates are equal
                            if (date1.equals(date2)) {
                                System.out.println("Date1 is equal Date2");
                                isDate = true;
                            }
                    System.out.println();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                return isDate;
            }
        });

       /* if ((daysList.get(position).isCurrentDay && !isClicked)) {
            gridcell.setTextColor(context.getResources().getColor(android.R.color.white));
            parentLay.setBackgroundResource(R.drawable.circle_selected);
        }

        else*/ if (!daysList.get(position).isCurrentMonthDay) {
            parentLay.setBackgroundResource(0);
            gridcell.setTextColor(context.getResources().getColor(R.color.grey));
        }

        else if(daysList.get(position).isPastDay) {
            parentLay.setBackgroundResource(0);
            gridcell.setTextColor(context.getResources().getColor(R.color.grey));
        }

       /* else if (daysList.get(position).isCurrentMonthDay && isClicked) {
            gridcell.setTextColor(context.getResources().getColor(R.color.white));
            parentLay.setBackgroundResource(R.drawable.circle_selected);
        }
*/
        else if(daysList.get(position).date.equals(selectedDate)){
            gridcell.setTextColor(context.getResources().getColor(android.R.color.white));
            parentLay.setBackgroundResource(R.drawable.circle_selected);
        }

        else if(SelectDateList.contains(daysList.get(position).date.trim())){
            gridcell.setTextColor(context.getResources().getColor(android.R.color.black));
            parentLay.setBackgroundResource(0);
        }

        else {
            gridcell.setTextColor(context.getResources().getColor(android.R.color.black));
            parentLay.setBackgroundResource(0);
        }

        return row;
    }

    private void setEvent(ImageView view) {
        for (int i = 0; i < daysList.size(); i++) {

            if (MyDateList.contains(daysList.get(i).date)) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    private boolean compareDates(Date calDate, Date taskDate) {
        try {
            if (calDate.before(taskDate)) {
                return false;  // If start date is before end date.
            } else if (calDate.equals(taskDate)) {
                return true;  // If two dates are equal.
            } else {
                return false; // If start date is after the end date.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Date getTaskDate(String dateString) {
        Date date = null;
        if (dateString != null && !dateString.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//
            try {
                date = format.parse(dateString);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
                try {
                    date = format.parse("2016-10-30");
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return date;
    }

    private Date getCalendarDate(String dateString) {
        Date date = null;
        if (dateString != null && !dateString.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");//
            try {
                date = format.parse(dateString);
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
                try {
                    date = format.parse("2016-10-30");
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return date;
    }

    @SuppressWarnings("unused")
    private void doCellAction(int position) {
        try {
            isClicked = true;
            mCurrentDate = daysList.get(position).date;
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date mDate = null;
            try {
                mDate = mDateFormat.parse(mCurrentDate);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Hit API and get response
            //Call Interfaces
            //  clickInterface.getTimeSlot(PreferencesManager.getInstance().getValue(Constants.PreferencesKey.USER_ID), mCurrentDate);
            // mPresenter.getTimeSlotByUserId(PreferencesManager.getInstance().getValue(Constants.PreferencesKey.USER_ID), mCurrentDate);
            calenderClickInterface.onCalDateClickListener(position, mCurrentDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getItem(int position) {
        return daysList.get(position);
    }


    String getCurrentDate(String pattern) {
        String date = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            date = sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    int totalMonthDays(int year, int month) {
        int monthMaxDays = 30;
        try {
            Calendar c = new GregorianCalendar(year, month, 1);
            monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthMaxDays;
    }

    public interface OnDateClickInterface {
        void getTimeSlot(String userId, String selectedDate);
    }
}
