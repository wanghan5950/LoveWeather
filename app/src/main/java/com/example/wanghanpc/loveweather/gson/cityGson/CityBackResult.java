package com.example.wanghanpc.loveweather.gson.cityGson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityBackResult {

    private String status;

    @SerializedName("basic")
    private List<City> cityList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }
}
