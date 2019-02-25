package com.curahservice.netset.util;

public class TimeUtils {
    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;

    private TimeUtils() {
    }

    /**
     * converts time (in milliseconds) to human-readable format
     * "<w> days, <x> hours, <y> minutes and (z) seconds"
     */
    public static String millisToLongDHMS(long duration) {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_DAY;
            if (temp > 0) {
                if (temp==1){
                    return "day ago";
                }else {
                    return temp+" days ago";
                }
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                if (temp<=24){
                    return temp+" hours ago";
                }
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                if (temp < 60) {
                    return temp + " " + "minutes ago";
                }
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                /*res.append(temp).append(" second").append(temp > 1 ? "s" : "");*/
                if (temp <= 60) {
                    return temp+" seconds ago";
                }
            }
            return res.toString();
        } else {
            return "just now";
        }
    }
}




