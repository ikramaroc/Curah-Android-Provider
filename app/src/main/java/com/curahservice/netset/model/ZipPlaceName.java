package com.curahservice.netset.model;

/**
 * Created by netset on 3/1/17.
 */

public class ZipPlaceName {
    String ZipCode, city,state,adress, countryCode;

    public ZipPlaceName(String ZipCode, String City, String state, String adress, String countryCode)
    {
        this.city=City;
        this.ZipCode=ZipCode;
        this.state=state;
        this.adress=adress;
        this.countryCode=countryCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    //
}
