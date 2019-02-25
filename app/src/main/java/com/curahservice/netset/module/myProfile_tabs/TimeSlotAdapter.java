package com.curahservice.netset.module.myProfile_tabs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.curahservice.netset.R;

import com.curahservice.netset.model.TimeSlotModel;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder> {
    private Context context;
    private List<TimeSlotModel> slotModels;
    private String selectedDate;
    private int hr;
    private int min;
    private ScheduleTab3 fragment;
    String currentTime;

    TimeSlotAdapter(Context context, List<TimeSlotModel> menuModelList, ScheduleTab3 fragment, String selectedDate) {
        this.context = context;
        this.slotModels = menuModelList;
        this.fragment = fragment;
        this.selectedDate = selectedDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        currentTime = sdf.format(new Date());
    }

    @NonNull
    @Override
    public TimeSlotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_appointmentslots, parent, false);
        return new TimeSlotAdapter.MyViewHolder(view);
    }


    private boolean idLastItem(int pos) {
        try {

            if (pos == slotModels.size() - 1 && compareDates(convertTo24Hour(slotModels.get(slotModels.size() - 1).getFrom()), convertTo24Hour(slotModels.get(slotModels.size() - 1).getTo()))) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isFilledLastItem(int pos) {
        try {
            if (slotModels.get(slotModels.size() - 1).getTo().length() > 0 && slotModels.get(slotModels.size() - 1).getFrom().length() > 0) {
                if (idLastItem(pos)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    String maxTime = "";

    private String getMaxTime(Collection<TimeSlotModel> c, String currentTime) {
        for (TimeSlotModel o : c) {
            if (maxTime.length() == 0) {
                maxTime = o.getTo();
            } else {
                if (o.getTo().length() > 0) {
                    if (checktimings(convertTo24Hour(maxTime), convertTo24Hour(o.getTo()))) {
                        if (o.getTo().equals(currentTime)) {
                            maxTime = o.getTo();
                        }
                    } else if (maxTime.length() > 0 && currentTime.length() > 0) {
                        if (checktimings(convertTo24Hour(maxTime), convertTo24Hour(currentTime))) {
                            maxTime = currentTime;
                        }
                    }

                }


            }

        }

        return maxTime;


    }

    String minTime = "";

    private String getMinTime(Collection<TimeSlotModel> c) {


        for (TimeSlotModel o : c) {
            if (minTime.length() == 0) {
                minTime = o.getFrom();
            } else {
                if (o.getFrom().length() > 0) {
                    if (!checktimings(convertTo24Hour(minTime), convertTo24Hour(o.getFrom()))) {
                        minTime = o.getFrom();
                    }
                }
            }
        }
        Log.e("min time------->", minTime);
        return minTime;


    }


    private boolean checktimings(String time, String endtime) {

        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date1 = sdf.parse(time);
            Date date2 = sdf.parse(endtime);
            Log.e("----current", time);
            Log.e("----end", endtime);

            if (date1.before(date2)) {
                return true;
            } else {

                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean containsName(Collection<TimeSlotModel> c, String currentTime, boolean to) {

        getMaxTime(c, currentTime);

        if (maxTime.length() > 0 && minTime.length() > 0) {
            try {
                TimeSlotModel model = (TimeSlotModel) new ArrayList(c).get(c.size() - 1);
                if (model.getFrom().equalsIgnoreCase(minTime)) {
                    if (!checkTimeGreater(currentTime, maxTime)) {
                        for (TimeSlotModel o : c) {
                            try {
                                if (to) {
                                    if (convertTo24Hour(currentTime).equals(convertTo24Hour(o.getTo()))) {
                                        return true;
                                    }
                                    if (convertTo24Hour(currentTime).equals(convertTo24Hour(o.getFrom()))) {
                                        return false;
                                    } else if (convertTo24Hour(currentTime).equals(convertTo24Hour(o.getTo()))) {
                                        return true;
                                    } else {
                                        if (o != null && isTimeBetweenTwoTime(convertTo24Hour(o.getFrom()), convertTo24Hour(o.getTo()), convertTo24Hour(currentTime))) {
                                            return true;
                                        }
                                    }

                                } else {

                                    if (convertTo24Hour(currentTime).equals(convertTo24Hour(o.getFrom()))) {
                                        return true;
                                    } else {
                                        if (o != null && isTimeBetweenTwoTime(convertTo24Hour(o.getFrom()), convertTo24Hour(o.getTo()), convertTo24Hour(currentTime))) {
                                            return true;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                return false;
                            }

                        }
                    } else {
                        return true;
                    }
                } else if (!checkTimeGreater(model.getFrom(), maxTime)) {
                    if (model.getFrom().equals(maxTime)) {
                        return false;
                    } else if (to && checkTimeGreater(currentTime, maxTime)) {
                        return true;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        for (TimeSlotModel o : c) {
            try {
                if (to) {
                    if (convertTo24Hour(currentTime).equals(convertTo24Hour(o.getTo()))) {
                        return true;
                    }
                    if (convertTo24Hour(currentTime).equals(convertTo24Hour(o.getFrom()))) {
                        return false;
                    } else if (convertTo24Hour(currentTime).equals(convertTo24Hour(o.getTo()))) {
                        return true;
                    } else {
                        if (o != null && isTimeBetweenTwoTime(convertTo24Hour(o.getFrom()), convertTo24Hour(o.getTo()), convertTo24Hour(currentTime))) {
                            return true;
                        }
                    }

                } else {

                    if (convertTo24Hour(currentTime).equals(convertTo24Hour(o.getFrom()))) {
                        return true;
                    } else {
                        if (o != null && isTimeBetweenTwoTime(convertTo24Hour(o.getFrom()), convertTo24Hour(o.getTo()), convertTo24Hour(currentTime))) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                return false;
            }

        }
        return false;
    }


    private boolean checkTimeGreater(String date, String maxDateTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        try {
            Date currentDate = format.parse(date);
            Date MaxDate = format.parse(maxDateTime);
            System.out.println(date);
            if (currentDate.after(MaxDate)) {
                System.out.println("Current time is greater than max time");
                return true;
            } else {
                System.out.println("Current time is less than max time");
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean isTimeBetweenTwoTime(String initialTime, String finalTime,
                                               String currentTime) throws ParseException {
       /* Log.e("intial time",initialTime+"");
        Log.e("finalTime time",finalTime+"");
        Log.e("currentTime time",currentTime+"");
*/

        boolean valid = false;
        //Start Time
        //all times are from java.util.Date
        Date inTime = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse(initialTime);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(inTime);

        //Current Time
        Date checkTime = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse(currentTime);
        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTime(checkTime);

        //End Time
        Date finTime = new SimpleDateFormat("yyy-MM-dd HH:mm:ss").parse(finalTime);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(finTime);

        if (finalTime.compareTo(initialTime) < 0) {
            calendar2.add(Calendar.DATE, 1);
            calendar3.add(Calendar.DATE, 1);
        }

        java.util.Date actualTime = calendar3.getTime();


        if ((actualTime.after(calendar1.getTime()) ||
                actualTime.compareTo(calendar1.getTime()) == 0) &&
                actualTime.before(calendar2.getTime())) {
            valid = true;
            return valid;
        } else {

            return false;
            // throw new IllegalArgumentException("Not a valid time, expecting HH:MM:SS format");
        }

    }


    //A = accept, P=pending, R=reject,I=inprogress)
    @Override
    public void onBindViewHolder(@NonNull final TimeSlotAdapter.MyViewHolder holder, final int position) {
        holder.to_date.setText(changeDateFormat(slotModels.get(position).getTo(), "yyyy-MM-dd hh:mm a", "hh:mm a"));
        holder.from_date.setText(changeDateFormat(slotModels.get(position).getFrom(), "yyyy-MM-dd hh:mm a", "hh:mm a"));
        if (position == 0) {
            if (slotModels.get(position).getTo().equals("")) {
                holder.to_date.setHint("To");
            }
            if (slotModels.get(position).getFrom().equals("")) {
                holder.from_date.setHint("Start From");
                holder.to_date.setError(null);
                holder.from_date.setError(null);
            }
        } else {
            holder.from_date.setHint("From");
            holder.to_date.setHint("To");
            holder.to_date.setError(null);
        }
        if (slotModels.size() == 1 && slotModels.get(position).getStatus().equals("B") ) {
            holder.delete_layout.setVisibility(View.GONE);
        } else {
            if (slotModels.get(position).getStatus().equalsIgnoreCase("U")) {
                holder.delete_layout.setVisibility(View.VISIBLE);
            }else {
                holder.delete_layout.setVisibility(View.GONE);
            }
        }
        if (isFilledLastItem(position)) {
            holder.addMore_TV.setVisibility(View.VISIBLE);
        } else {
            holder.addMore_TV.setVisibility(View.GONE);
        }
        holder.to_date.setTag(position);
        holder.to_date.setKeyListener(null);
        holder.from_date.setKeyListener(null);
        holder.from_date.setTag(position);
        holder.to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (slotModels.get((Integer) v.getTag()).getFrom().length() == 0 || slotModels.get((Integer) v.getTag()).getTo().length() == 0) {
                    createdTimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                            hr = hourOfDay;
                            min = minutes;
                            String totime = updateTime(hr, min, (TextView) v);
                            boolean isValidTime = true;
                            if (totime.length() > 0 && slotModels.get(position).getFrom().length() > 0) {
                                try {
                                    isValidTime = checktimings(convertTo24Hour(slotModels.get(position).getFrom()), convertTo24Hour(totime));
                                } catch (Exception e) {
                                }
                            }

                            if (totime.length() > 0 && isValidTime) {
                                if (!containsName(slotModels, totime, true)) {
                                    slotModels.set((int) v.getTag(), new TimeSlotModel(slotModels.get(position).getFrom(), totime, slotModels.get(position).getId(), slotModels.get(position).getStatus()));
                                    getMinTime(slotModels);
                                    getMaxTime(slotModels, totime);
                                    ((TextView) v).setError(null);
                                    //Check if Timming repeat
                                    if (slotModels.size() > 1) {
                                        for (int i = 0; i < slotModels.size() - 1; i++) {
                                            try {
                                                boolean isTimeInBeween = isTimeBetweenTwoTime(convertTo24Hour(slotModels.get(slotModels.size() - 1).getFrom()), convertTo24Hour(slotModels.get(slotModels.size() - 1).getTo()), convertTo24Hour(slotModels.get(i).getFrom()));
                                                if (isTimeInBeween) {
                                                    ((TextView) v).setHint("To");
                                                    ((TextView) v).setText("");
                                                    slotModels.set((int) v.getTag(), new TimeSlotModel(slotModels.get(position).getFrom(), "", slotModels.get(position).getId(), slotModels.get(position).getStatus()));
                                                    getMaxTime(slotModels, "");
                                                    ((TextView) v).setError("Already added slot");
                                                    Toast.makeText(context, "This time slot already added", Toast.LENGTH_LONG).show();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    notifyDataSetChanged();
                                } else {
                                    ((TextView) v).setHint("To");
                                    ((TextView) v).setText("");
                                    slotModels.set((int) v.getTag(), new TimeSlotModel(slotModels.get(position).getFrom(), "", slotModels.get(position).getId(), slotModels.get(position).getStatus()));
                                    getMaxTime(slotModels, "");
                                    ((TextView) v).setError("Already added slot");
                                    Toast.makeText(context, "This time slot already added", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                slotModels.set((int) v.getTag(), new TimeSlotModel(slotModels.get(position).getFrom(), "", slotModels.get(position).getId(), slotModels.get(position).getStatus()));
                                getMaxTime(slotModels, "");
                                ((TextView) v).setHint("To");
                                ((TextView) v).setError("Enter valid time");
                                Toast.makeText(context, "Please add valid time", Toast.LENGTH_LONG).show();
                            }


                        }
                    }).show();
                }
            }
        });

        holder.from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (slotModels.get((Integer) v.getTag()).getFrom().length() == 0 || slotModels.get((Integer) v.getTag()).getTo().length() == 0) {
                    createdTimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minutes) {
                            hr = hourOfDay;
                            min = minutes;
                            boolean isValidTime = true;
                            updateTime(hr, min, (TextView) v);
                            String fromtime = updateTime(hr, min, (TextView) v);
                            if (fromtime.length() > 0 && slotModels.get(position).getTo().length() > 0) {
                                try {
                                    isValidTime = compareDates(convertTo24Hour(fromtime), convertTo24Hour(slotModels.get(position).getTo()));
                                } catch (Exception e) {
                                }
                            }
                            if (fromtime.length() > 0 && isValidTime) {
                                if (!containsName(slotModels, fromtime, false)) {
                                    slotModels.set((int) v.getTag(), new TimeSlotModel(fromtime, "", "", "U"));
                                    notifyDataSetChanged();
                                    holder.from_date.setError(null);
                                    getMinTime(slotModels);

                                } else {
                                    holder.from_date.setHint("From");
                                    holder.to_date.setHint("To");

                                    slotModels.set((int) v.getTag(), new TimeSlotModel("", "", "", "U"));
                                    getMinTime(slotModels);
                                    holder.from_date.setError("Already added slot");
                                    holder.from_date.setText("");
                                    Toast.makeText(context, "This time slot already added", Toast.LENGTH_LONG).show();

                                }
                            } else {
                                holder.from_date.setHint("From");
                                holder.to_date.setHint("To");
                                slotModels.set((int) v.getTag(), new TimeSlotModel("", "", "", "U"));
                                getMinTime(slotModels);
                                holder.from_date.setError("Enter valid time");
                                holder.from_date.requestFocus();

                            }

                        }
                    }).show();
                }
            }
        });

        holder.addMore_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slotModels.add(new TimeSlotModel("", "", "", "U"));
                notifyDataSetChanged();

            }
        });
        holder.delete_layout.setTag(position);
        holder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (slotModels.get((int) v.getTag()).getId().length() > 0) {
                    fragment.deleteAppoinment(slotModels.get((int) v.getTag()).getId());
                } else {
                    slotModels.remove((int) v.getTag());
                    getMaxTime(slotModels, "");
                    getMinTime(slotModels);
                    notifyDataSetChanged();
                }


            }
        });
    }

    public static String convertTo24Hour(String Time) {
        DateFormat f1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a"); //11:00 pm
        Date d = null;
        try {
            d = f1.parse(Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String x = f2.format(d); // "23:00"

        return x;
    }

    protected Dialog createdTimePickerDialog(Context context, TimePickerDialog.OnTimeSetListener timePickerListener) {

        return new TimePickerDialog(context, R.style.DialogTheme, timePickerListener, hr, 00, false);
    }

    public static boolean compareDates(String psDate1, String psDate2) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm");
        Date date1 = dateFormat.parse(psDate1);
        Date date2 = dateFormat.parse(psDate2);
        if (date2.after(date1)) {
            return true;
        } else {
            return false;
        }
    }

    private String updateTime(int hours, int mins, TextView editText) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);


        String aTime = "";
        String time = "";
        aTime = new StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet).toString();
        time = selectedDate + " " + aTime;
        boolean checkFromTimeGreaterThanCurrent = checktimings(changeDateFormat(currentTime, "yyyy-MM-dd hh:mm a", "yyyy-MM-dd HH:mm:ss"), changeDateFormat(time, "yyyy-MM-dd hh:mm a", "yyyy-MM-dd HH:mm:ss"));
        if (checkFromTimeGreaterThanCurrent) {
            editText.setText(aTime);
        } else {
            time = "";
            editText.setText("");
            Toast.makeText(context, "You can not add past timing to schedule a booking, Please add future timings.", Toast.LENGTH_LONG).show();
            // Toast.makeText(context,"Can not add time slot for this time",Toast.LENGTH_LONG).show();
        }
        return time;

    }


    public String changeDateFormat(String dateString, String sourceDateFormat, String targetDateFormat) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        SimpleDateFormat inputDateFromat = new SimpleDateFormat(sourceDateFormat, Locale.getDefault());
        Date date = new Date();
        try {
            date = inputDateFromat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat outputDateFormat = new SimpleDateFormat(targetDateFormat, Locale.getDefault());
        return outputDateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return slotModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView from_date, to_date, addMore_TV;
        ImageView delete_layout;


        MyViewHolder(View itemView) {
            super(itemView);
            from_date = itemView.findViewById(R.id.from_date);
            to_date = itemView.findViewById(R.id.to_date);
            addMore_TV = itemView.findViewById(R.id.addMore_TV);
            delete_layout = itemView.findViewById(R.id.delete_layout);
        }
    }
}
