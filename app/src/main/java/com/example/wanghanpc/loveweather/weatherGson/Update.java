package com.example.wanghanpc.loveweather.weatherGson;

import com.google.gson.annotations.SerializedName;

public class Update {

    /**
     * loc : 2018-10-01 13:47
     * utc : 2018-10-01 05:47
     */

    @SerializedName("loc")
    private String localTime;

    @SerializedName("utc")
    private String utcTime;

    public String getLoc() {
        return localTime;
    }

    public void setLoc(String localTime) {
        this.localTime = localTime;
    }

    public String getUtc() {
        return utcTime;
    }

    public void setUtc(String utcTime) {
        this.utcTime = utcTime;
    }
}
