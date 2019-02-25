package com.curahservice.netset.gsonModel;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceUpdate {

    @SerializedName("status")
    @Expose
    private Integer status;

    @NonNull
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("services")
    @Expose
    private List<createAccount.Service> services = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<createAccount.Service> getServices() {
        return services;
    }

    public void setServices(List<createAccount.Service> services) {
        this.services = services;
    }


}