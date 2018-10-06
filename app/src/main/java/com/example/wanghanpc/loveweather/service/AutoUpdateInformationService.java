package com.example.wanghanpc.loveweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;

import com.example.wanghanpc.loveweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class AutoUpdateInformationService extends Service {

    private List<String> placeNameList = new ArrayList<>();
    private LocalBroadcastManager localBroadcastManager;

    public AutoUpdateInformationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = 8 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + hour;
        Intent intent1 = new Intent(this,AutoUpdateInformationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,intent1,0);
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent);
        autoUpdateWeather();
        sendBroadcast();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 发出广播
     */
    private void sendBroadcast(){
        Intent intent = new Intent("com.loveWeather.broadcast.weather.new");
        localBroadcastManager.sendBroadcast(intent);
    }

    /**
     * 更新天气信息
     */
    private void autoUpdateWeather(){
        getPlaceNameListFromShared();
        for (String place : placeNameList){
            Utility.requestWeather(place,this);
        }
    }

    /**
     * 获取城市名列表
     */
    private void getPlaceNameListFromShared(){
        SharedPreferences preferences = getSharedPreferences("placeNameList",MODE_PRIVATE);
        int index = preferences.getInt("listSize",0);
        for (int i = 0; i < index; i++){
            String place = preferences.getString("item_"+i,null);
            if (!placeNameList.contains(place) && place != null) {
                placeNameList.add(place);
            }
        }
    }
}
