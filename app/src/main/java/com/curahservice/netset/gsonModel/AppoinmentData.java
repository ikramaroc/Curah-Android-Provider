package com.curahservice.netset.gsonModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AppoinmentData implements Serializable{



    @SerializedName("appointmentDetails")
    @Expose
    private AppointmentDetail appointmentDetails;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    @SerializedName("rating")
    @Expose
    private Rating rating;

    public AppointmentDetail getAppointmentDetails() {
        return appointmentDetails;
    }

    public void setAppointmentDetails(AppointmentDetail appointmentDetails) {
        this.appointmentDetails = appointmentDetails;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public class Rating implements Serializable  {

        @SerializedName("provider_rating")
        @Expose
        private Integer providerRating;
        @SerializedName("provider_message")
        @Expose
        private String providerMessage;
        @SerializedName("customer_rating")
        @Expose
        private Integer customerRating;
        @SerializedName("customer_message")
        @Expose
        private String customerMessage;

        public Integer getProviderRating() {
            return providerRating;
        }

        public void setProviderRating(Integer providerRating) {
            this.providerRating = providerRating;
        }

        public String getProviderMessage() {
            return providerMessage;
        }

        public void setProviderMessage(String providerMessage) {
            this.providerMessage = providerMessage;
        }

        public Integer getCustomerRating() {
            return customerRating;
        }

        public void setCustomerRating(Integer customerRating) {
            this.customerRating = customerRating;
        }

        public String getCustomerMessage() {
            return customerMessage;
        }

        public void setCustomerMessage(String customerMessage) {
            this.customerMessage = customerMessage;
        }

    }

}
