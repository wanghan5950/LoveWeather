package com.example.wanghanpc.loveweather.util;

import com.example.wanghanpc.loveweather.cityGson.CityBackResult;
import com.example.wanghanpc.loveweather.cityGson.HotCityBackResult;
import com.example.wanghanpc.loveweather.weatherGson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 解析返回的数据数据
 */

public class Utility {

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
     *解析返回的城市数据
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

    /**
     * 将日期转为星期
     * @param time
     * @return
     */
    public static String getWeek(String time){
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int wek=c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += "星期日";
        }
        if (wek == 2) {
            Week += "星期一";
        }
        if (wek == 3) {
            Week += "星期二";
        }
        if (wek == 4) {
            Week += "星期三";
        }
        if (wek == 5) {
            Week += "星期四";
        }
        if (wek == 6) {
            Week += "星期五";
        }
        if (wek == 7) {
            Week += "星期六";
        }
        return Week;
    }
}
