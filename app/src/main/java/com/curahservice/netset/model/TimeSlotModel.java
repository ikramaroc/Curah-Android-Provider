package com.curahservice.netset.model;

public class TimeSlotModel {
    String from;
    String to;
    String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id="";

    public TimeSlotModel(String from, String to,String id,String status) {
        this.from = from;
        this.to = to;
        this.id=id;
        this.status=status;
    }
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
