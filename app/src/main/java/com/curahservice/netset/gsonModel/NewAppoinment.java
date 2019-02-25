package com.curahservice.netset.gsonModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewAppoinment {
    @SerializedName("provider_appointment")
    @Expose
    private List<ProviderPojo.ProviderAppointment> providerAppointment = null;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<ProviderPojo.ProviderAppointment> getProviderAppointment() {
        return providerAppointment;
    }

    public void setProviderAppointment(List<ProviderPojo.ProviderAppointment> providerAppointment) {
        this.providerAppointment = providerAppointment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
