package com.example.wanghanpc.loveweather.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CityDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "cityId Integer, "
            + "location text, "
            + "parentCity text, "
            + "province text)";

    private static final String CREATE_HOT_CITY = "create table HotCity ("
            + "id integer primary key autoincrement, "
            + "cityId Integer, "
            + "location text, "
            + "parentCity text, "
            + "province text)";

    private Context context;

    public CityDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_HOT_CITY);
        Log.d("CityDatabaseHelper","-----------create succeeded");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists City");
        db.execSQL("drop table if exists HotCity");
        onCreate(db);
    }
}
