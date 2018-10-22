package com.example.wanghanpc.loveweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.wanghanpc.loveweather.tools.HttpUtil;
import com.example.wanghanpc.loveweather.tools.Utility;
import com.example.wanghanpc.loveweather.weatherGson.Weather;

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

    protected List<String> placeNameList = new ArrayList<>();
    protected List<Weather> weatherList = new ArrayList<>();
    protected LocationClient locationClient;
    protected Toolbar toolbar;
    protected IntentFilter intentFilter;
    protected LocalReceiver localReceiver;
    protected LocalBroadcastManager localBroadcastManager;
    protected Weather weatherResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        locationClient = new LocationClient(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.wanghanpc.loveWeather.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);
    }

    /**
     * 保存城市名列表
     */
    protected void savePlaceNameList (List<String> placeNameList){
        SharedPreferences.Editor editor = getSharedPreferences("placeNameList",MODE_PRIVATE).edit();
        editor.putInt("listSize",placeNameList.size());
        for (int i = 0; i < placeNameList.size(); i++){
            editor.putString("item_"+i,placeNameList.get(i));
        }
        editor.apply();
    }

    /**
     * 获取城市名列表
     */
    protected void getPlaceNameList(){
        SharedPreferences preferences = getSharedPreferences("placeNameList",MODE_PRIVATE);
        int index = preferences.getInt("listSize",0);
        for (int i = 0; i < index; i++){
            String place = preferences.getString("item_"+i,null);
            if (!placeNameList.contains(place) && place != null) {
                placeNameList.add(place);
            }
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
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
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
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.getStatus())){
                            String city = weather.getBasic().getLocation();
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(BaseActivity.this).edit();
                            editor.putString(city,responseText);
                            editor.apply();
                            weatherResult = weather;
                            Intent intent = new Intent("com.example.wanghanpc.loveWeather.LOCAL_BROADCAST");
                            localBroadcastManager.sendBroadcast(intent);
                            Log.d("BaseActivity","------------------------查询成功，发出广播");
                        }else {
                            Toast.makeText(BaseActivity.this,"查询天气失败02",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    protected class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BaseActivity","------------------------接收到广播");
            updateWeatherUI();
        }
    }

    /**
     * 进行更新UI的操作
     */
    protected void updateWeatherUI(){

    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }
}
