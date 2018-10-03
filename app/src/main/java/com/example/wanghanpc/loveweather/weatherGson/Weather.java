package com.example.wanghanpc.loveweather.weatherGson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Weather implements Serializable{

    private String status;

    private Basic basic;

    private Now now;

    private Update update;

    @SerializedName("daily_forecast")
    private List<DailyForecast> dailyForecastList;

    @SerializedName("hourly")
    private List<Hourly> hourlyList;

    @SerializedName("lifestyle")
    private List<Lifestyle> lifestyleList;

    private Boolean isSelected = false;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public List<DailyForecast> getDailyForecastList() {
        return dailyForecastList;
    }

    public void setDailyForecastList(List<DailyForecast> dailyForecastList) {
        this.dailyForecastList = dailyForecastList;
    }

    public List<Hourly> getHourlyList() {
        return hourlyList;
    }

    public void setHourlyList(List<Hourly> hourlyList) {
        this.hourlyList = hourlyList;
    }

    public List<Lifestyle> getLifestyleList() {
        return lifestyleList;
    }

    public void setLifestyleList(List<Lifestyle> lifestyleList) {
        this.lifestyleList = lifestyleList;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        Weather weather = (Weather) obj;
        return weather.basic.getLocation() != null && basic.getLocation().equals(weather.basic.getLocation());
    }
}
