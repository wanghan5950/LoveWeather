package com.example.wanghanpc.loveweather.cityGson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HotCityBackResult {

    public String status;

    @SerializedName("basic")
    public List<City> hotCityList;
}
