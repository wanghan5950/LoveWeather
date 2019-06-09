package com.example.wanghanpc.loveweather.tools;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getName();
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        UMConfigure.init(context,"5c1b5393b465f55116000031","Umeng",UMConfigure.DEVICE_TYPE_PHONE,"2aed77e2cf7fcf25372cfde3f9a8e872");
        PushAgent pushAgent = PushAgent.getInstance(this);
        pushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG,"注册成功--------------" + s);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.d(TAG,"注册失败--------------" + "s : " + s + ",s1 : " + s1);
            }
        });
    }

    public static Context getContext(){
        return context;
    }
}
