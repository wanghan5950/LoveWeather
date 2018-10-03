package com.example.wanghanpc.loveweather.weatherGson;

import com.google.gson.annotations.SerializedName;

public class Basic {

    /**
     * cid : CN101010100
     * location : 北京
     * parent_city : 北京
     * admin_area : 北京
     * cnty : 中国
     * lat : 39.90498734
     * lon : 116.4052887
     * tz : +8.00
     */

    @SerializedName("cid")
    private String cityId;

    private String location;

    @SerializedName("parent_city")
    private String parentCity;

    @SerializedName("admin_area")
    private String adminArea;

    @SerializedName("cnty")
    private String country;

    private String lat;
    private String lon;
    private String tz;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getParentCity() {
        return parentCity;
    }

    public void setParentCity(String parentCity) {
        this.parentCity = parentCity;
    }

    public String getAdminArea() {
        return adminArea;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }
}
