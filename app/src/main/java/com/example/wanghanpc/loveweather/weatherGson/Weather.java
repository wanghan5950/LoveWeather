package com.example.wanghanpc.loveweather.weatherGson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Weather implements Serializable{

    @SerializedName("resultcode")
    public String resultCode;

    public String reason;

    public Result result;

    public Integer error_code;

    public Boolean isSelected = false;

    @Override
    public boolean equals(Object obj) {
//        return super.equals(obj);
        if (obj == null) {
            return false;
        }
        Weather weather = (Weather) obj;
        return weather.result.today.city != null && result.today.city.equals(weather.result.today.city);
    }
}
