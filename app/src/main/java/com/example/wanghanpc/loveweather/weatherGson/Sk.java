package com.example.wanghanpc.loveweather.weatherGson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sk implements Serializable{

    public String temp;

    @SerializedName("wind_direction")
    public String direction;

    @SerializedName("wind_strength")
    public String strength;

    public String humidity;

    public String time;

}
