package com.example.wanghanpc.loveweather.cityGson;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("cid")
    private String cityId;

    @SerializedName("location")
    private String locationName;

    @SerializedName("parent_city")
    private String parentCity;

    @SerializedName("admin_area")
    private String adminArea;

    private String cnty;

    private String lat;

    private String lon;

    @SerializedName("tz")
    private String timeArea;

    @SerializedName("type")
    private String cityType;

    private String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
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

    public String getTimeArea() {
        return timeArea;
    }

    public void setTimeArea(String timeArea) {
        this.timeArea = timeArea;
    }

    public String getCityType() {
        return cityType;
    }

    public void setCityType(String cityType) {
        this.cityType = cityType;
    }
}

