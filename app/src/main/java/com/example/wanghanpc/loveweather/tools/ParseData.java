package com.example.wanghanpc.loveweather.tools;

import com.example.wanghanpc.loveweather.gson.cityGson.CityBackResult;
import com.example.wanghanpc.loveweather.gson.cityGson.HotCityBackResult;
import com.example.wanghanpc.loveweather.gson.weatherGson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 解析返回的数据数据
 */

public class ParseData {

    /**
     * 解析返回的天气数据
     */
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析返回的城市数据
     */
    public static CityBackResult handleCityResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String cityContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(cityContent,CityBackResult.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析返回的热门城市数据
     */
    public static HotCityBackResult handleHotCityResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather6");
            String hotCityContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(hotCityContent,HotCityBackResult.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
