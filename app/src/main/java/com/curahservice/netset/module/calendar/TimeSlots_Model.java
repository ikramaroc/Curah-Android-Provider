package com.curahservice.netset.module;

public class TimeSlots_Model {

    private String id,time;

    public TimeSlots_Model(String id, String date) {
        this.id = id;
        this.time = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
