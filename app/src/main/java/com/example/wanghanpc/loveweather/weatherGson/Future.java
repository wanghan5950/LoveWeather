package com.example.wanghanpc.loveweather.weatherGson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Future implements Serializable{

    public String temperature;

    public String weather;

    @SerializedName("weather_id")
    public futureWeatherId futureWeatherId;

    public String wind;

    public String week;

    public String date;

    public class futureWeatherId implements Serializable{

        @SerializedName("fa")
        public String startWeather;

        @SerializedName("fb")
        public String lastWeather;
    }
}
