package com.curahservice.netset.gsonModel;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProviderPojo {
    @SerializedName("provider_appointment")
    @Expose
    private List<ProviderAppointment> providerAppointment = null;


    @NonNull
    @SerializedName("inprocessService")
    @Expose
    private InprocessService inprocessService;

    @SerializedName("status")
    @Expose
    private Integer status;

    public InprocessService getInprocessService() {
        return inprocessService;
    }

    public void setInprocessService(InprocessService inprocessService) {
        this.inprocessService = inprocessService;
    }

    public List<ProviderAppointment> getProviderAppointment() {
        return providerAppointment;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    public class InprocessService {

        @NonNull
        @SerializedName("bookingId")
        @Expose
        private Integer bookingId;
        @NonNull
        @SerializedName("provider_id")
        @Expose
        private Integer providerId;
        @NonNull
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @NonNull
        @SerializedName("close_time")
        @Expose
        private String closeTime;
        @NonNull
        @SerializedName("date")
        @Expose
        private String date;
        @NonNull
        @SerializedName("status")
        @Expose
        private String status;
        @NonNull
        @SerializedName("price")
        @Expose
        private String price;
        @NonNull
        @SerializedName("customer_id")
        @Expose
        private Integer customerId;
        @NonNull
        @SerializedName("service_name")
        @Expose
        private String serviceName;

        public Integer getBookingId() {
            return bookingId;
        }

        public void setBookingId(Integer bookingId) {
            this.bookingId = bookingId;
        }

        public Integer getProviderId() {
            return providerId;
        }

        public void setProviderId(Integer providerId) {
            this.providerId = providerId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(String closeTime) {
            this.closeTime = closeTime;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

    }

    public class ProviderAppointment {

        @SerializedName("provider_id")
        @Expose
        private Integer providerId;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("close_time")
        @Expose
        private String closeTime;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("customer_id")
        @Expose
        private Integer customerId;


        @SerializedName("bookingId")
        @Expose
        private Integer bookingId;


        @SerializedName("service_name")
        @Expose
        private String serviceName;

        public String getCancel_by() {
            return cancel_by;
        }

        public void setCancel_by(String cancel_by) {
            this.cancel_by = cancel_by;
        }

        @SerializedName("cancel_by")
        @Expose
        private String cancel_by;


        public Integer getProviderId() {
            return providerId;
        }

        public Integer getBookingId() {
            return bookingId;
        }

        public void setBookingId(Integer bookingId) {
            this.bookingId = bookingId;
        }

        public void setProviderId(Integer providerId) {
            this.providerId = providerId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(String closeTime) {
            this.closeTime = closeTime;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

    }
}
