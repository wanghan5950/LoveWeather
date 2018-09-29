package com.example.wanghanpc.loveweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.example.wanghanpc.loveweather.cityGson.CityBackResult;
import com.example.wanghanpc.loveweather.cityGson.HotCityBackResult;
import com.example.wanghanpc.loveweather.weatherGson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

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
            Gson gson = new Gson();
            return gson.fromJson(response,Weather.class);
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
     * 请求天气数据并保存
     */
    public static void requestWeather(final String placeName, final Context context){
        requestDone = false;
        String weatherUrl = "http://v.juhe.cn/weather/index?cityname=" + placeName + "&key=f6aaf579c3b72065ce65d74591c1614e";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Toast.makeText(context,"获取天气失败01",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = handleWeatherResponse(responseText);
                if (weather != null && "200".equals(weather.resultCode)){
                    Utility.weather = weather;
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                    editor.putString(placeName,responseText);
                    editor.apply();
                    requestDone = true;
                }else {
                    Toast.makeText(context,"获取天气失败02",Toast.LENGTH_SHORT).show();
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
}
