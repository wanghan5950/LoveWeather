package com.example.wanghanpc.loveweather.weatherGson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

public class Result implements Serializable{

    public Sk sk;

    public Today today;

    @SerializedName("future")
    public Map<String,Future> futureMap;

}
