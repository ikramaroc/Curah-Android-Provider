package com.curahservice.netset.module.history;

public class HistoryModel {
    private String date, service;

    HistoryModel(String date, String service) {
        this.date = date;
        this.service = service;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

}
