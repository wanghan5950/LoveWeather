package com.example.wanghanpc.loveweather.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wanghanpc.loveweather.cityGson.City;

import java.util.ArrayList;
import java.util.List;

public class DatabaseModel {

    private CityDatabaseHelper databaseHelper;
    private Context context;

    public DatabaseModel(Context context) {
        this.context = context;
    }

    /**
     * 创建数据库
     */
    public void onCreateDatabase(){
        databaseHelper = new CityDatabaseHelper(context,"CityList.db",null,1);
        databaseHelper.getWritableDatabase();
    }

    /**
     * 保存cityList到数据库
     */
    public void saveToDatabase(List<City> cityList){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.delete("City",null,null);
        ContentValues values = new ContentValues();
        for (City city : cityList){
            values.put("cityId",city.getCityId());
            values.put("location",city.getLocationName());
            values.put("parentCity",city.getParentCity());
            values.put("province",city.getAdminArea());
            database.insert("City",null,values);
            values.clear();
        }
    }

    /**
     * 保存hotCityList到数据库
     */
    public void saveHotToDatabase(List<City> hotCityList){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        database.delete("HotCity",null,null);
        ContentValues values = new ContentValues();
        for (City city : hotCityList){
            values.put("cityId",city.getCityId());
            values.put("location",city.getLocationName());
            values.put("parentCity",city.getParentCity());
            values.put("province",city.getAdminArea());
            database.insert("HotCity",null,values);
            values.clear();
        }
    }

    /**
     * 从数据库中获取cityList
     */
    public List<City> getFromDatabase(){
        List<City> cityList = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.query("City",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String cityId = cursor.getString(cursor.getColumnIndex("cityId"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String parentCity = cursor.getString(cursor.getColumnIndex("parentCity"));
                String province = cursor.getString(cursor.getColumnIndex("province"));
                City city = new City();
                city.setCityId(cityId);
                city.setLocationName(location);
                city.setParentCity(parentCity);
                city.setAdminArea(province);
                cityList.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return cityList;
    }

    /**
     * 从数据库中获取hotCityList
     */
    public List<City> getHotFromDatabase(){
        List<City> hotCityList = new ArrayList<>();
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        Cursor cursor = database.query("HotCity",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                String cityId = cursor.getString(cursor.getColumnIndex("cityId"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String parentCity = cursor.getString(cursor.getColumnIndex("parentCity"));
                String province = cursor.getString(cursor.getColumnIndex("province"));
                City city = new City();
                city.setCityId(cityId);
                city.setLocationName(location);
                city.setParentCity(parentCity);
                city.setAdminArea(province);
                hotCityList.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return hotCityList;
    }
}
