package com.example.wanghanpc.loveweather.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.example.wanghanpc.loveweather.MainActivity;
import com.example.wanghanpc.loveweather.OtherEntityClass.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.tools.Utility;
import com.example.wanghanpc.loveweather.weatherGson.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知服务，接受自动更新服务的广播后推送通知
 */

public class NotificationService extends Service {

    private List<String> placeNameList = new ArrayList<>();
    private Weather weatherForNotification;
    private LocalBroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;

    public NotificationService() {
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
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.loveWeather.broadcast.weather.new");
        broadcastReceiver = new LocalBroadcastReceiver();
        localBroadcastManager.registerReceiver(broadcastReceiver,intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 发出通知
     */
    private void sendNotification(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(NotificationService.this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this,0,intent,0);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String title = weatherForNotification.getBasic().getLocation();
                String content = weatherForNotification.getLifestyleList().get(1).getTxt();
                Bitmap icon = BitmapFactory.decodeResource(getResources(),ReadyIconAndBackground.getWeatherIcon(weatherForNotification.getNow().getCondCode()));
                Notification notification = new NotificationCompat.Builder(NotificationService.this,"weatherNotification")
                        .setContentTitle(title)
                        .setContentText(content)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.love_weather_icon)
                        .setLargeIcon(icon)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();
                manager.notify(1,notification);
            }
        }).start();
    }

    /**
     * 获取列表中0位置的城市的天气
     */
    private void getWeatherForNotification(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString(placeNameList.get(0),null);
        if (weatherString != null){
            weatherForNotification = Utility.handleWeatherResponse(weatherString);
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

    class LocalBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            getPlaceNameListFromShared();
            getWeatherForNotification();
            sendNotification();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
