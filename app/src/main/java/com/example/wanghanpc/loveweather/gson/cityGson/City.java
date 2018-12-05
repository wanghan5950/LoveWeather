package com.example.wanghanpc.loveweather.gson.cityGson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class City implements Serializable{

    private static final long serialVersionUID = 5845098686465379495L;

    @SerializedName("cid")
    private String cityId;

    @SerializedName("location")
    private String locationName;

    @SerializedName("parent_city")
    private String parentCity;

    @SerializedName("admin_area")
    private String adminArea;

    @SerializedName("cnty")
    private String country;

    private String lat;

    private String lon;

    @SerializedName("tz")
    private String timeArea;

    @SerializedName("type")
    private String cityType;

    public String getCityId() {
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

    @Override
    public int hashCode() {
        return cityId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        City city = (City)obj;
        return cityId.equals(city.cityId);
    }
}

