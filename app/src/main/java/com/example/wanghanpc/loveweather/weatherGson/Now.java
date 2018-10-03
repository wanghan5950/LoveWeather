package com.example.wanghanpc.loveweather.weatherGson;

import com.google.gson.annotations.SerializedName;

public class Now {

    /**
     * cloud : 0
     * cond_code : 100
     * cond_txt : 晴
     * fl : 23
     * hum : 22
     * pcpn : 0.0
     * pres : 1015
     * tmp : 25
     * vis : 35
     * wind_deg : 339
     * wind_dir : 西北风
     * wind_sc : 2
     * wind_spd : 8
     */

    private String cloud;

    @SerializedName("cond_code")
    private String condCode;

    @SerializedName("cond_txt")
    private String condTxt;

    @SerializedName("fl")
    private String feelTemp;

    @SerializedName("hum")
    private String rHumidity;

    @SerializedName("pcpn")
    private String precipitation;

    @SerializedName("pres")
    private String atmosphere;

    private String tmp;

    @SerializedName("vis")
    private String visibility;

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

    public String getFeelTemp() {
        return feelTemp;
    }

    public void setFeelTemp(String feelTemp) {
        this.feelTemp = feelTemp;
    }

    public String getrHumidity() {
        return rHumidity;
    }

    public void setrHumidity(String rHumidity) {
        this.rHumidity = rHumidity;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    public String getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(String atmosphere) {
        this.atmosphere = atmosphere;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
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
