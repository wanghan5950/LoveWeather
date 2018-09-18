package com.example.wanghanpc.loveweather.weatherGson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Today implements Serializable{

    public String city;

    @SerializedName("date_y")
    public String date;

    public String week;

    public String temperature;

    public String weather;

    @SerializedName("weather_id")
    public weatherChangeId weatherChangeId;

    public String wind;

    @SerializedName("dressing_index")
    public String dressingIndex;

    @SerializedName("dressing_advice")
    public String dressingAdvice;

    @SerializedName("uv_index")
    public String uvIndex;

    @SerializedName("comfort_index")
    public String comfortIndex;

    @SerializedName("wash_index")
    public String washIndex;

    @SerializedName("travel_index")
    public String travelIndex;

    @SerializedName("exercise_index")
    public String exerciseIndex;

    @SerializedName("drying_index")
    public String dryingIndex;

    public class weatherChangeId implements Serializable{

        @SerializedName("fa")
        public String startWeather;

        @SerializedName("fb")
        public String lastWeather;
    }
}
