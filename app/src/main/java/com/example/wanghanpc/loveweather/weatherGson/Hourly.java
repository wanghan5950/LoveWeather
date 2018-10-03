package com.example.wanghanpc.loveweather.weatherGson;

import com.google.gson.annotations.SerializedName;

public class Hourly {

    /**
     * cloud : 3
     * cond_code : 100
     * cond_txt : 晴
     * dew : 3
     * hum : 41
     * pop : 0
     * pres : 1016
     * time : 2018-10-01 16:00
     * tmp : 23
     * wind_deg : 347
     * wind_dir : 西北风
     * wind_sc : 4-5
     * wind_spd : 29
     */

    private String cloud;

    @SerializedName("cond_code")
    private String condCode;

    @SerializedName("cond_txt")
    private String condTxt;

    @SerializedName("dew")
    private String dewTemp;

    @SerializedName("hum")
    private String rHumidity;

    private String pop;

    @SerializedName("pres")
    private String atmosphere;

    private String time;

    private String tmp;

    @SerializedName("wind_deg")
    private String windDegree;

    @SerializedName("wind_dir")
    private String windDirection;

    @SerializedName("wind_sc")
    private String windSize;

    @SerializedName("wind_spd")
    private String windSpeed;

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getCondCode() {
        return condCode;
    }

    public void setCondCode(String condCode) {
        this.condCode = condCode;
    }

    public String getCondTxt() {
        return condTxt;
    }

    public void setCondTxt(String condTxt) {
        this.condTxt = condTxt;
    }

    public String getDewTemp() {
        return dewTemp;
    }

    public void setDewTemp(String dewTemp) {
        this.dewTemp = dewTemp;
    }

    public String getrHumidity() {
        return rHumidity;
    }

    public void setrHumidity(String rHumidity) {
        this.rHumidity = rHumidity;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(String atmosphere) {
        this.atmosphere = atmosphere;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(String windDegree) {
        this.windDegree = windDegree;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSize() {
        return windSize;
    }

    public void setWindSize(String windSize) {
        this.windSize = windSize;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}
