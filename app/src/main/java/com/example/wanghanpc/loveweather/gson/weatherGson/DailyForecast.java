package com.example.wanghanpc.loveweather.gson.weatherGson;

import com.google.gson.annotations.SerializedName;

public class DailyForecast {

    /**
     * cond_code_d : 100
     * cond_code_n : 100
     * cond_txt_d : 晴
     * cond_txt_n : 晴
     * date : 2018-10-01
     * hum : 41
     * mr : 22:11
     * ms : 12:10
     * pcpn : 0.0
     * pop : 7
     * pres : 1018
     * sr : 06:11
     * ss : 17:55
     * tmp_max : 25
     * tmp_min : 12
     * uv_index : 9
     * vis : 20
     * wind_deg : 339
     * wind_dir : 西北风
     * wind_sc : 4-5
     * wind_spd : 27
     */

    @SerializedName("cond_code_d")
    private String condCodeDay;

    @SerializedName("cond_code_n")
    private String condCodeNight;

    @SerializedName("cond_txt_d")
    private String condTxtDay;

    @SerializedName("cond_txt_n")
    private String condTxtNight;

    private String date;

    @SerializedName("hum")
    private String rHumidity;

    @SerializedName("mr")
    private String moonUp;

    @SerializedName("ms")
    private String moonDown;

    @SerializedName("pcpn")
    private String precipitation;

    private String pop;

    @SerializedName("pres")
    private String atmosphere;

    @SerializedName("sr")
    private String sunUp;

    @SerializedName("ss")
    private String sunDown;

    @SerializedName("tmp_max")
    private String tempMax;

    @SerializedName("tmp_min")
    private String tempMin;

    @SerializedName("uv_index")
    private String uvIndex;

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

    public String getCondCodeDay() {
        return condCodeDay;
    }

    public void setCondCodeDay(String condCodeDay) {
        this.condCodeDay = condCodeDay;
    }

    public String getCondCodeNight() {
        return condCodeNight;
    }

    public void setCondCodeNight(String condCodeNight) {
        this.condCodeNight = condCodeNight;
    }

    public String getCondTxtDay() {
        return condTxtDay;
    }

    public void setCondTxtDay(String condTxtDay) {
        this.condTxtDay = condTxtDay;
    }

    public String getCondTxtNight() {
        return condTxtNight;
    }

    public void setCondTxtNight(String condTxtNight) {
        this.condTxtNight = condTxtNight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getrHumidity() {
        return rHumidity;
    }

    public void setrHumidity(String rHumidity) {
        this.rHumidity = rHumidity;
    }

    public String getMoonUp() {
        return moonUp;
    }

    public void setMoonUp(String moonUp) {
        this.moonUp = moonUp;
    }

    public String getMoonDown() {
        return moonDown;
    }

    public void setMoonDown(String moonDown) {
        this.moonDown = moonDown;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
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

    public String getSunUp() {
        return sunUp;
    }

    public void setSunUp(String sunUp) {
        this.sunUp = sunUp;
    }

    public String getSunDown() {
        return sunDown;
    }

    public void setSunDown(String sunDown) {
        this.sunDown = sunDown;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(String uvIndex) {
        this.uvIndex = uvIndex;
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
