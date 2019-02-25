package com.curahservice.netset.model;

import java.io.File;
import java.io.Serializable;

public class userInformation implements Serializable
{
    String phone,fname,lname,fblink,instalink,yelplink;
    File UserProfile;

    public userInformation(String phone, String fname, String lname, String fblink, String instalink, String yelplink,File userProfile) {
        this.phone = phone;
        this.fname = fname;
        this.lname = lname;
        this.fblink = fblink;
        this.instalink = instalink;
        this.yelplink = yelplink;
        this.UserProfile=userProfile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFblink() {
        return fblink;
    }

    public void setFblink(String fblink) {
        this.fblink = fblink;
    }

    public String getInstalink() {
        return instalink;
    }

    public void setInstalink(String instalink) {
        this.instalink = instalink;
    }

    public String getYelplink() {
        return yelplink;
    }

    public void setYelplink(String yelplink) {
        this.yelplink = yelplink;
    }


    public File getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(File userProfile) {
        UserProfile = userProfile;
    }
}
