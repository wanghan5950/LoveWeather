package com.example.wanghanpc.loveweather.cityGson;

import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("cid")
    public String cityId;

    @SerializedName("location")
    public String locationName;

    @SerializedName("parent_city")
    public String parentCity;

    @SerializedName("admin_area")
    public String adminArea;

    public String cnty;

    public String lat;

    public String lon;

    @SerializedName("tz")
    public String timeArea;

    @SerializedName("type")
    public String cityType;
}

