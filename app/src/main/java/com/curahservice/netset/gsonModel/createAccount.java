package com.curahservice.netset.gsonModel;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.List;

public class createAccount {

    @SerializedName("userInfo")
    @Expose
    private UserInfo userInfo;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("services")
    @Expose


    private List<Service> services = null;


    @SerializedName("portfolio")
    @Expose
    private List<PortFolioGson.Porfolio> portfolio = null;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

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

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }


    public static class Service {

        public Service(String name, Integer serviceId, Integer price, String type) {
            this.name = name;
            this.serviceId = serviceId;
            this.price = price;
            this.type = type;
        }

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("service_id")
        @Expose
        private Integer serviceId;
        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("type")
        @Expose
        private String type;


        public boolean isIs_approved() {
            return is_approved;
        }

        public void setIs_approved(boolean is_approved) {
            this.is_approved = is_approved;
        }

        @SerializedName("is_approved")
        @Expose
        private boolean is_approved;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getServiceId() {
            return serviceId;
        }

        public void setServiceId(Integer serviceId) {
            this.serviceId = serviceId;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


    }


    public class UserInfo {

        @NonNull
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("dob")
        @Expose
        private Object dob;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("city")
        @Expose
        private String city;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        @SerializedName("state")
        @Expose
        private String state;


        @SerializedName("rating")
        @Expose
        private String rating;


        @NonNull
        @SerializedName("notification_type")
        @Expose
        private String notification_type;


        public String getNotification_type() {
            return notification_type;
        }

        public void setNotification_type(String notification_type) {
            this.notification_type = notification_type;
        }

        @NonNull
        @SerializedName("reviewCount")
        @Expose
        private Integer reviewCount = 0;
        @SerializedName("imgUrl")
        @Expose
        private String imgUrl;
        @SerializedName("facebook_link")
        @Expose
        private String facebookLink;
        @SerializedName("instagram_link")
        @Expose
        private String instagramLink;
        @SerializedName("yelp_link")
        @Expose
        private String yelpLink;
        @SerializedName("appointment_from")
        @Expose
        private String appointmentFrom;
        @SerializedName("appointment_to")
        @Expose
        private String appointmentTo;

        public String getBreaktime_from() {
            return breaktime_from;
        }

        public void setBreaktime_from(String breaktime_from) {
            this.breaktime_from = breaktime_from;
        }

        public String getBreaktime_to() {
            return breaktime_to;
        }

        public void setBreaktime_to(String breaktime_to) {
            this.breaktime_to = breaktime_to;
        }

        @SerializedName("breaktime_from")
        @Expose
        private String breaktime_from;

        @SerializedName("breaktime_to")
        @Expose
        private String breaktime_to;

        @SerializedName("experience")
        @Expose
        private String experience;

        public String getLicense_number() {
            return license_number;
        }

        public void setLicense_number(String license_number) {
            this.license_number = license_number;
        }

        @SerializedName("license_number")
        @Expose
        private String license_number;


        @SerializedName("license")
        @Expose
        private String license;


        @SerializedName("identification_card")
        @Expose
        private String identification_card;


        @NonNull
        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(@NonNull String latitude) {
            this.latitude = latitude;
        }

        @NonNull
        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(@NonNull String longitude) {
            this.longitude = longitude;
        }

        @NonNull
        @SerializedName("latitude")
        @Expose
        private String latitude = "";

        @NonNull
        @SerializedName("longitude")
        @Expose
        private String longitude = "";


        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getDob() {
            return dob;
        }

        public void setDob(Object dob) {
            this.dob = dob;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public Integer getReviewCount() {
            return reviewCount;
        }

        public void setReviewCount(Integer reviewCount) {
            this.reviewCount = reviewCount;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getFacebookLink() {
            return facebookLink;
        }

        public void setFacebookLink(String facebookLink) {
            this.facebookLink = facebookLink;
        }

        public String getInstagramLink() {
            return instagramLink;
        }

        public void setInstagramLink(String instagramLink) {
            this.instagramLink = instagramLink;
        }

        public String getYelpLink() {
            return yelpLink;
        }

        public void setYelpLink(String yelpLink) {
            this.yelpLink = yelpLink;
        }

        public String getAppointmentFrom() {
            return appointmentFrom;
        }

        public void setAppointmentFrom(String appointmentFrom) {
            this.appointmentFrom = appointmentFrom;
        }

        public String getAppointmentTo() {
            return appointmentTo;
        }

        public void setAppointmentTo(String appointmentTo) {
            this.appointmentTo = appointmentTo;
        }

        public String getIdentification_card() {
            return identification_card;
        }

        public void setIdentification_card(String identification_card) {
            this.identification_card = identification_card;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }
    }
}
