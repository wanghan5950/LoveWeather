package com.example.wanghanpc.loveweather.tools;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * HTTP请求
 */

public class SendOkHttp {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
