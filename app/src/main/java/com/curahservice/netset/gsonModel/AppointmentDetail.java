package com.curahservice.netset.gsonModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AppointmentDetail implements Serializable {

    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
    @SerializedName("price")
    @Expose
    private String price;

    public String getGet_tax_price() {
        return get_tax_price;
    }

    public void setGet_tax_price(String get_tax_price) {
        this.get_tax_price = get_tax_price;
    }

    @SerializedName("getPrice")
    @Expose
    private String get_tax_price;

    @SerializedName("status")
    @Expose
    private String status;



    public String getCancel_by() {
        return cancel_by;
    }

    public void setCancel_by(String cancel_by) {
        this.cancel_by = cancel_by;
    }

    @SerializedName("cancel_by")
    @Expose
    private String cancel_by;

    public String getCancel_description() {
        return cancel_description;
    }

    public void setCancel_description(String cancel_description) {
        this.cancel_description = cancel_description;
    }

    @SerializedName("cancel_description")
    @Expose
    private String cancel_description;

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("close_time")
    @Expose
    private String closeTime;

    @SerializedName("user_details")
    @Expose
    private UserDetails userDetails;

    @SerializedName("service_name")
    @Expose
    public List<ServiceName> serviceName = null;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @SerializedName("description")
    @Expose
    private String description;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public List<ServiceName> getServiceName() {
        return serviceName;
    }

    public void setServiceName(List<ServiceName> serviceName) {
        this.serviceName = serviceName;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }


    public class ServiceName implements Serializable {

        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("name")
        @Expose
        public String name;

    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public class UserDetails implements Serializable {


        public int getConnection_id() {
            return connection_id;
        }

        public void setConnection_id(int connection_id) {
            this.connection_id = connection_id;
        }

        @SerializedName("connection_id")
        @Expose
        private int connection_id;

        @SerializedName("id")
        @Expose
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("distance")
        @Expose
        private Integer distance;
        @SerializedName("rating")
        @Expose
        private String rating;

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }


    }
}