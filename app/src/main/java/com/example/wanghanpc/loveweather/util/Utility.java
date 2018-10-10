package com.example.wanghanpc.loveweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.wanghanpc.loveweather.cityGson.CityBackResult;
import com.example.wanghanpc.loveweather.cityGson.HotCityBackResult;
import com.example.wanghanpc.loveweather.weatherGson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 从服务器请求数据及解析数据
 */

public class Utility {

    private static boolean requestDone = false;
    private static Weather weather;

    /**
     * 将返回的JSON数据解析成Weather实体类
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
     * 根据城市名请求天气数据并保存
     */
    public static void requestWeather(final String location, final Context context){
        requestDone = false;
        final String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + location + "&key=e7b4b21007f048a9a4fe2cb236ce5569";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("Utility","--------------------获取天气失败1");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = handleWeatherResponse(responseText);
                if (weather != null && "ok".equals(weather.getStatus())){
                    Utility.weather = weather;
                    String city = weather.getBasic().getLocation();
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                    editor.putString(city,responseText);
                    editor.apply();
                    requestDone = true;
                }else {
                    Log.d("Utility","--------------------获取天气失败2");
                }
            }
        });
    }

    public static Weather getWeather(){
        return weather;
    }

    public static boolean isRequestDoneOrNot(){
        return requestDone;
    }

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
