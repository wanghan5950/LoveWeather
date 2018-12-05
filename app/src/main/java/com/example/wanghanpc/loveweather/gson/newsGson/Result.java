package com.example.wanghanpc.loveweather.gson.newsGson;

import java.util.List;

public class Result {

    private String stat;

    private List<DataItem> data;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }
}
