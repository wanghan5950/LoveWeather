package com.example.wanghanpc.loveweather.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.wanghanpc.loveweather.gson.cityGson.City;
import com.example.wanghanpc.loveweather.model.DatabaseModel;

import java.util.ArrayList;
import java.util.List;

public class BaseService extends Service {

    protected List<City> placeNameList = new ArrayList<>();
    protected DatabaseModel databaseModel;
    protected LocalBroadcastManager broadcastManager;

    public BaseService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseModel = new DatabaseModel(this);
        databaseModel.onCreateDatabase();
        broadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 从数据库中获取城市列表
     */
    protected void getCityListFromDatabase(){
        List<City> cityList = databaseModel.getFromDatabase();
        int size = cityList.size();
        for (int i = 0; i < size; i++){
            if (cityList.get(i) != null && !placeNameList.contains(cityList.get(i)))
                placeNameList.add(cityList.get(i));
        }
    }
}
