package com.example.wanghanpc.loveweather.gson.cityGson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotCityBackResult {

    private String status;

    @SerializedName("basic")
    private List<City> hotCityList;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<City> getHotCityList() {
        return hotCityList;
    }

    public void setHotCityList(List<City> hotCityList) {
        this.hotCityList = hotCityList;
    }
}
