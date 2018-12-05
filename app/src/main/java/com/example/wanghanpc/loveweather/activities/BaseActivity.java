package com.example.wanghanpc.loveweather.activities;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.wanghanpc.loveweather.gson.cityGson.City;
import com.example.wanghanpc.loveweather.gson.cityGson.CityBackResult;
import com.example.wanghanpc.loveweather.model.DatabaseModel;
import com.example.wanghanpc.loveweather.tools.SendOkHttp;
import com.example.wanghanpc.loveweather.tools.ParseData;
import com.example.wanghanpc.loveweather.gson.weatherGson.Weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 父活动
 * 初始化百度定位，本地广播
 * 定义每个活动都会用到的方法
 */

public class BaseActivity extends AppCompatActivity {

    protected List<City> placeNameList = new ArrayList<>();
    protected List<Weather> weatherList = new ArrayList<>();
    protected LocationClient locationClient;
    protected Toolbar toolbar;
    protected Weather weatherResult;
    protected DatabaseModel databaseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        databaseModel = new DatabaseModel(this);
        databaseModel.onCreateDatabase();

        locationClient = new LocationClient(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 保存城市列表到数据库
     */
    protected void saveCityListToDatabase(List<City> cityList){
        databaseModel.saveToDatabase(cityList);
    }

    /**
     * 从数据库中获取城市列表
     */
    protected void getCityListFromDatabase(){
        List<City> cityList = databaseModel.getFromDatabase();
        int size = cityList.size();
        for (int i = 0; i < size; i++){
            if (!placeNameList.contains(cityList.get(i)))
                placeNameList.add(cityList.get(i));
        }
    }

    /**
     * 百度定位，获取地址
     */
    protected void requestLocation(){
        getDetailedLocation();
        locationClient.start();
    }

    /**
     * 百度定位，允许详细地址
     */
    private void getDetailedLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
    }

    /**
     * 根据经纬度或城市名查询天气
     * @param location
     */
    protected void requestWeather(String location){
        String weatherUrl = "https://free-api.heweather.com/s6/weather?location=" + location + "&key=e7b4b21007f048a9a4fe2cb236ce5569";
        SendOkHttp.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BaseActivity.this,"查询天气失败01",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = ParseData.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.getStatus())){
                            String cityId = weather.getBasic().getCityId();
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this).edit();
                            editor.putString(cityId,responseText);
                            editor.apply();
                            weatherResult = weather;
                            Log.d("BaseActivity","-------------------------获取天气成功");
                            updateWeatherUI();
                        }else {
                            Toast.makeText(BaseActivity.this,"查询天气失败02",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 请求城市搜索数据
     */
    protected void requestCityList(final String searchName){
        String cityUrl = "https://search.heweather.com/find?&location=" + searchName + "&key=e7b4b21007f048a9a4fe2cb236ce5569";
        SendOkHttp.sendOkHttpRequest(cityUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BaseActivity.this,"获取城市列表失败1",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final CityBackResult cityBackResult = ParseData.handleCityResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cityBackResult != null && "ok".equals(cityBackResult.getStatus())){
                            updateCity(cityBackResult);
                        }else {
                            Toast.makeText(BaseActivity.this,"获取城市列表失败2",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取城市成功后调用
     * @param result
     */
    protected void updateCity(CityBackResult result){

    }

    /**
     * 获取天气成功后调用
     */
    protected void updateWeatherUI(){

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
