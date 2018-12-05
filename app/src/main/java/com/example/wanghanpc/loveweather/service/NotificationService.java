package com.example.wanghanpc.loveweather.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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

import com.example.wanghanpc.loveweather.activities.MainActivity;
import com.example.wanghanpc.loveweather.tools.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.tools.ParseData;
import com.example.wanghanpc.loveweather.gson.weatherGson.Weather;

/**
 * 通知服务，接受自动更新服务的广播后推送通知
 */

public class NotificationService extends BaseService {

    private Weather weatherForNotification;
    private LocalBroadcastReceiver broadcastReceiver;

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
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.loveWeather.broadcast.weather.new");
        broadcastReceiver = new LocalBroadcastReceiver();
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
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
        String weatherString = preferences.getString(placeNameList.get(0).getCityId(),null);
        if (weatherString != null){
            weatherForNotification = ParseData.handleWeatherResponse(weatherString);
        }
    }

    class LocalBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            getCityListFromDatabase();
            getWeatherForNotification();
            sendNotification();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }
}
