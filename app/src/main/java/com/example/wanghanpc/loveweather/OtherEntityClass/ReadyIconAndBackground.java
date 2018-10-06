package com.example.wanghanpc.loveweather.OtherEntityClass;

import com.example.wanghanpc.loveweather.R;

import java.util.HashMap;
import java.util.Map;

public class ReadyIconAndBackground {

    private static Map<String,Integer> weatherIconList = new HashMap<>();
    public static Map<String,Integer> weatherNightIconList = new HashMap<>();
    private static Map<String,Integer> largeWeatherIconList = new HashMap<>();
    private static Map<String,Integer> largeNightIconList = new HashMap<>();
    private static Map<String,Integer> backgroundList = new HashMap<>();
    private static Map<String,Integer> suggestionIconList = new HashMap<>();
    private static Map<String,Integer> windDirectionIconList = new HashMap<>();
    private static Map<String,String> lifeStyleTextList = new HashMap<>();

    /**
     * 获取小图标资源
     * @param weatherId
     * @return
     */
    public static int getWeatherIcon(String weatherId){
        if (weatherId != null && weatherIconList.containsKey(weatherId)){
            return weatherIconList.get(weatherId);
        }
        return R.drawable.unknown;
    }

    public static int getWeatherNightIcon(String weatherId){
        if (weatherId != null && weatherNightIconList.containsKey(weatherId)){
            return weatherNightIconList.get(weatherId);
        }
        return R.drawable.unknown;
    }

    /**
     * 获取大图标资源
     * @param weatherId
     * @return
     */
    public static int getLargeWeatherIcon(String weatherId){
        if (weatherId != null && largeWeatherIconList.containsKey(weatherId)){
            return largeWeatherIconList.get(weatherId);
        }
        return R.drawable.unknown_l;
    }

    public static int getLargeNightIcon(String weatherId){
        if (weatherId != null && largeNightIconList.containsKey(weatherId)){
            return largeNightIconList.get(weatherId);
        }
        return R.drawable.unknown_l;
    }

    /**
     * 获取背景颜色
     * @param weatherId
     * @return
     */
    public static int getWeatherBackground(String weatherId){
        if (weatherId != null && backgroundList.containsKey(weatherId)){
            return backgroundList.get(weatherId);
        }
        return R.drawable.sunny_background;
    }

    /**
     * 获取生活指数图标资源
     * @param lifeStyleType
     * @return
     */
    public static int getLifeStyleIcon(String lifeStyleType){
        if (lifeStyleType != null && suggestionIconList.containsKey(lifeStyleType)){
            return suggestionIconList.get(lifeStyleType);
        }
        return R.drawable.air_quality_index;
    }

    /**
     * 获取风向图标资源
     */
    public static int getWindDirectionIcon(String windText){
        if (windText != null && windDirectionIconList.containsKey(windText)){
            return windDirectionIconList.get(windText);
        }
        return R.mipmap.wind_breeze;
    }

    /**
     * 获取生活指数描述
     */
    public static String getLifeStyleText(String lifeStyleType){
        if (!lifeStyleType.equals("") && lifeStyleTextList.containsKey(lifeStyleType)){
            return lifeStyleTextList.get(lifeStyleType);
        }
        return "";
    }

    /**
     * 将图标资源添加到Map表中
     */
    public static void initWeatherIconAndBackground(){
        weatherIconList.put("100", R.drawable.icon_sunny);
        weatherIconList.put("101",R.drawable.icon_cloudy);
        weatherIconList.put("102",R.drawable.icon_cloudy);
        weatherIconList.put("103",R.drawable.icon_cloudy);
        weatherIconList.put("104",R.drawable.icon_cloudy_sky);
        weatherIconList.put("300",R.drawable.icon_rain_s_sunny);
        weatherIconList.put("301",R.drawable.icon_rain_s_sunny);
        weatherIconList.put("302",R.drawable.icon_thundershower);
        weatherIconList.put("303",R.drawable.icon_thundershower);
        weatherIconList.put("304",R.drawable.icon_snow_rain);
        weatherIconList.put("305",R.drawable.icon_rain_s);
        weatherIconList.put("306",R.drawable.icon_rain_m);
        weatherIconList.put("307",R.drawable.icon_rain_l);
        weatherIconList.put("308",R.drawable.icon_rainstorm);
        weatherIconList.put("309",R.drawable.icon_rain_s);
        weatherIconList.put("310",R.drawable.icon_rainstorm);
        weatherIconList.put("311",R.drawable.icon_rainstorm);
        weatherIconList.put("312",R.drawable.icon_rainstorm);
        weatherIconList.put("313",R.drawable.icon_snow_rain);
        weatherIconList.put("314",R.drawable.icon_rain_m);
        weatherIconList.put("315",R.drawable.icon_rain_l);
        weatherIconList.put("316",R.drawable.icon_rainstorm);
        weatherIconList.put("317",R.drawable.icon_rainstorm);
        weatherIconList.put("318",R.drawable.icon_rainstorm);
        weatherIconList.put("407",R.drawable.icon_snow_s_sunny);
        weatherIconList.put("400",R.drawable.icon_snow_s);
        weatherIconList.put("401",R.drawable.icon_snow_m);
        weatherIconList.put("402",R.drawable.icon_snow_l);
        weatherIconList.put("403",R.drawable.icon_snow_l);
        weatherIconList.put("404",R.drawable.icon_snow_rain);
        weatherIconList.put("405",R.drawable.icon_snow_rain);
        weatherIconList.put("406",R.drawable.icon_snow_rain);
        weatherIconList.put("408",R.drawable.icon_snow_m);
        weatherIconList.put("409",R.drawable.icon_snow_l);
        weatherIconList.put("410",R.drawable.icon_snow_l);
        weatherIconList.put("411",R.drawable.icon_snow_m);
        weatherIconList.put("504",R.drawable.icon_frost);
        weatherIconList.put("503",R.drawable.icon_sand);
        weatherIconList.put("507",R.drawable.icon_sand);
        weatherIconList.put("508",R.drawable.icon_hoze);
        weatherIconList.put("509",R.drawable.icon_hoze);
        weatherIconList.put("511",R.drawable.icon_hoze);
        weatherIconList.put("512",R.drawable.icon_hoze);
        weatherIconList.put("513",R.drawable.icon_hoze);

        weatherNightIconList.put("100",R.drawable.icon_sunny_night);
        weatherNightIconList.put("101",R.drawable.icon_cloudy_night);
        weatherNightIconList.put("102",R.drawable.icon_cloudy_night);
        weatherNightIconList.put("103",R.drawable.icon_cloudy_night);

        largeNightIconList.put("100",R.drawable.icon_sunny_night_l);
        largeNightIconList.put("101",R.drawable.icon_cloudy_night_l);
        largeNightIconList.put("102",R.drawable.icon_cloudy_night_l);
        largeNightIconList.put("103",R.drawable.icon_cloudy_night_l);

        largeWeatherIconList.put("100", R.drawable.icon_sunny_l);
        largeWeatherIconList.put("101",R.drawable.icon_cloudy_l);
        largeWeatherIconList.put("102",R.drawable.icon_cloudy_l);
        largeWeatherIconList.put("103",R.drawable.icon_cloudy_l);
        largeWeatherIconList.put("104",R.drawable.icon_cloudy_sky_l);
        largeWeatherIconList.put("300",R.drawable.icon_rain_s_sunny_l);
        largeWeatherIconList.put("301",R.drawable.icon_rain_s_sunny_l);
        largeWeatherIconList.put("302",R.drawable.icon_thundershower_l);
        largeWeatherIconList.put("303",R.drawable.icon_thundershower_l);
        largeWeatherIconList.put("304",R.drawable.icon_snow_rain_l);
        largeWeatherIconList.put("305",R.drawable.icon_rain_s_l);
        largeWeatherIconList.put("306",R.drawable.icon_rain_m_l);
        largeWeatherIconList.put("307",R.drawable.icon_rain_l_l);
        largeWeatherIconList.put("308",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("309",R.drawable.icon_rain_s_l);
        largeWeatherIconList.put("310",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("311",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("312",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("313",R.drawable.icon_snow_rain_l);
        largeWeatherIconList.put("314",R.drawable.icon_rain_m_l);
        largeWeatherIconList.put("315",R.drawable.icon_rain_l_l);
        largeWeatherIconList.put("316",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("317",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("318",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("407",R.drawable.icon_snow_s_sunny_l);
        largeWeatherIconList.put("400",R.drawable.icon_snow_s_l);
        largeWeatherIconList.put("401",R.drawable.icon_snow_m_l);
        largeWeatherIconList.put("402",R.drawable.icon_snow_l_l);
        largeWeatherIconList.put("403",R.drawable.icon_snow_l_l);
        largeWeatherIconList.put("404",R.drawable.icon_snow_rain_l);
        largeWeatherIconList.put("405",R.drawable.icon_snow_rain_l);
        largeWeatherIconList.put("406",R.drawable.icon_snow_rain_l);
        largeWeatherIconList.put("408",R.drawable.icon_snow_m_l);
        largeWeatherIconList.put("409",R.drawable.icon_snow_l_l);
        largeWeatherIconList.put("410",R.drawable.icon_snow_l_l);
        largeWeatherIconList.put("411",R.drawable.icon_snow_m_l);
        largeWeatherIconList.put("504",R.drawable.icon_frost_l);
        largeWeatherIconList.put("503",R.drawable.icon_sand_l);
        largeWeatherIconList.put("507",R.drawable.icon_sand_l);
        largeWeatherIconList.put("508",R.drawable.icon_hoze_l);
        largeWeatherIconList.put("509",R.drawable.icon_hoze_l);
        largeWeatherIconList.put("511",R.drawable.icon_hoze_l);
        largeWeatherIconList.put("512",R.drawable.icon_hoze_l);
        largeWeatherIconList.put("513",R.drawable.icon_hoze_l);

        backgroundList.put("100",R.drawable.sunny_background);
        backgroundList.put("101",R.drawable.sun_cloud_background);
        backgroundList.put("102",R.drawable.sun_cloud_background);
        backgroundList.put("103",R.drawable.sun_cloud_background);
        backgroundList.put("104",R.drawable.cloudy_background);
        backgroundList.put("300",R.drawable.thundershower_background);
        backgroundList.put("301",R.drawable.thundershower_background);
        backgroundList.put("302",R.drawable.thundershower_background);
        backgroundList.put("303",R.drawable.thundershower_background);
        backgroundList.put("304",R.drawable.thundershower_background);
        backgroundList.put("305",R.drawable.rain_background);
        backgroundList.put("306",R.drawable.rain_background);
        backgroundList.put("307",R.drawable.rain_background);
        backgroundList.put("308",R.drawable.rain_background);
        backgroundList.put("309",R.drawable.rain_background);
        backgroundList.put("310",R.drawable.rainstorm_background);
        backgroundList.put("311",R.drawable.rainstorm_background);
        backgroundList.put("312",R.drawable.rainstorm_background);
        backgroundList.put("313",R.drawable.rain_snow_background);
        backgroundList.put("314",R.drawable.rain_background);
        backgroundList.put("315",R.drawable.rain_background);
        backgroundList.put("316",R.drawable.rain_background);
        backgroundList.put("317",R.drawable.rainstorm_background);
        backgroundList.put("318",R.drawable.rainstorm_background);
        backgroundList.put("399",R.drawable.rain_background);
        backgroundList.put("400",R.drawable.snow_background);
        backgroundList.put("401",R.drawable.snow_background);
        backgroundList.put("402",R.drawable.snow_background);
        backgroundList.put("403",R.drawable.snow_background);
        backgroundList.put("404",R.drawable.rain_snow_background);
        backgroundList.put("405",R.drawable.rain_snow_background);
        backgroundList.put("406",R.drawable.rain_snow_background);
        backgroundList.put("407",R.drawable.snow_background);
        backgroundList.put("408",R.drawable.snow_background);
        backgroundList.put("409",R.drawable.snow_background);
        backgroundList.put("410",R.drawable.snow_background);
        backgroundList.put("499",R.drawable.snow_background);
        backgroundList.put("500",R.drawable.fog_background);
        backgroundList.put("501",R.drawable.fog_background);
        backgroundList.put("509",R.drawable.fog_background);
        backgroundList.put("502",R.drawable.haze_background);
        backgroundList.put("503",R.drawable.storm_background);
        backgroundList.put("504",R.drawable.storm_background);
        backgroundList.put("507",R.drawable.storm_background);
        backgroundList.put("508",R.drawable.storm_background);

        suggestionIconList.put("comf",R.drawable.comfort_index);
        suggestionIconList.put("drsg",R.drawable.dressing_index);
        suggestionIconList.put("flu",R.drawable.cold_index);
        suggestionIconList.put("sport",R.drawable.exercise_index);
        suggestionIconList.put("trav",R.drawable.travel_index);
        suggestionIconList.put("uv",R.drawable.uv_index);
        suggestionIconList.put("cw",R.drawable.wash_index);
        suggestionIconList.put("air",R.drawable.air_quality_index);

        windDirectionIconList.put("无持续风向",R.mipmap.wind_breeze);
        windDirectionIconList.put("北风",R.mipmap.wind_north);
        windDirectionIconList.put("西风",R.mipmap.wind_west);
        windDirectionIconList.put("南风",R.mipmap.wind_south);
        windDirectionIconList.put("东风",R.mipmap.wind_east);
        windDirectionIconList.put("西北风",R.mipmap.wind_northwest);
        windDirectionIconList.put("西南风",R.mipmap.wind_southwest);
        windDirectionIconList.put("东南风",R.mipmap.wind_southeast);
        windDirectionIconList.put("东北风",R.mipmap.wind_northeast);

        lifeStyleTextList.put("comf","舒适度");
        lifeStyleTextList.put("drsg","穿衣");
        lifeStyleTextList.put("flu","感冒");
        lifeStyleTextList.put("sport","运动");
        lifeStyleTextList.put("trav","旅游");
        lifeStyleTextList.put("uv","紫外线");
        lifeStyleTextList.put("cw","洗车");
        lifeStyleTextList.put("air","空气污染");
    }
}
