package com.curahservice.netset.gsonModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReviewModel implements Serializable{

    @SerializedName("my_reviews")
    @Expose
    private List<MyReview> myReviews = null;
    @SerializedName("img_url")
    @Expose
    private String imgUrl;
    @SerializedName("status")
    @Expose
    private Integer status;

    public List<MyReview> getMyReviews() {
        return myReviews;
    }

    public void setMyReviews(List<MyReview> myReviews) {
        this.myReviews = myReviews;
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


    public class MyReview implements Serializable{

        @SerializedName("firstname")
        @Expose
        private String firstname;
        @SerializedName("lastname")
        @Expose
        private String lastname;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("rating")
        @Expose
        private Integer rating;
        @SerializedName("description")
        @Expose
        private String description;

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

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public Integer getRating() {
            return rating;
        }

        public void setRating(Integer rating) {
            this.rating = rating;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

}
