package com.curahservice.netset.gsonModel;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryModel {


    @SerializedName("history")
    @Expose
    private List<History> history = null;


    @NonNull
    @SerializedName("img_url")
    @Expose
    private String imgUrl="";
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    public class History {

        @SerializedName("booking_id")
        @Expose
        private Integer bookingId;
        @SerializedName("appointment_id")
        @Expose
        private Integer appointmentId;
        @SerializedName("service_name")
        @Expose
        private String serviceName;
        @SerializedName("customer_id")
        @Expose
        private Integer customerId;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("work_description")
        @Expose
        private String workDescription;
        @SerializedName("cancel_type")
        @Expose
        private String cancelType;
        @SerializedName("cancel_description")
        @Expose
        private String cancelDescription;
        @SerializedName("start_time")
        @Expose
        private String startTime;
        @SerializedName("close_time")
        @Expose
        private String closeTime;
        @SerializedName("date")
        @Expose
        private String date;






        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }



        @SerializedName("rating")
        @Expose
        private String rate;



        public Integer getBookingId() {
            return bookingId;
        }

        public void setBookingId(Integer bookingId) {
            this.bookingId = bookingId;
        }

        public Integer getAppointmentId() {
            return appointmentId;
        }

        public void setAppointmentId(Integer appointmentId) {
            this.appointmentId = appointmentId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public Integer getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Integer customerId) {
            this.customerId = customerId;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getWorkDescription() {
            return workDescription;
        }

        public void setWorkDescription(String workDescription) {
            this.workDescription = workDescription;
        }

        public String getCancelType() {
            return cancelType;
        }

        public void setCancelType(String cancelType) {
            this.cancelType = cancelType;
        }

        public String getCancelDescription() {
            return cancelDescription;
        }

        public void setCancelDescription(String cancelDescription) {
            this.cancelDescription = cancelDescription;
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


    }
}
