package com.curahservice.netset.gsonModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuggestedServices {
    @SerializedName("services")
    @Expose
    private List<createAccount.Service> services = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<createAccount.Service> getServices() {
        return services;
    }

    public void setServices(List<createAccount.Service> services) {
        this.services = services;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}