package com.example.wanghanpc.loveweather.OtherEntityClass;

import com.example.wanghanpc.loveweather.R;

import java.util.HashMap;
import java.util.Map;

public class ReadyIconAndBackground {

    public static Map<String,Integer> weatherIconList = new HashMap<>();
    public static Map<String,Integer> largeWeatherIconList = new HashMap<>();
    public static Map<String,Integer> backgroundList = new HashMap<>();

    /**
     * 将图标资源添加到Map表中
     */
    public static void initWeatherIconAndBackground(){
        weatherIconList.put("00", R.drawable.icon_sunny);
        weatherIconList.put("01",R.drawable.icon_cloudy);
        weatherIconList.put("02",R.drawable.icon_cloudy_sky);
        weatherIconList.put("03",R.drawable.icon_rain_s_sunny);
        weatherIconList.put("04",R.drawable.icon_thundershower);
        weatherIconList.put("05",R.drawable.icon_thundershower);
        weatherIconList.put("06",R.drawable.icon_snow_rain);
        weatherIconList.put("07",R.drawable.icon_rain_s);
        weatherIconList.put("08",R.drawable.icon_rain_m);
        weatherIconList.put("09",R.drawable.icon_rain_l);
        weatherIconList.put("10",R.drawable.icon_rainstorm);
        weatherIconList.put("11",R.drawable.icon_rainstorm);
        weatherIconList.put("12",R.drawable.icon_rainstorm);
        weatherIconList.put("13",R.drawable.icon_snow_s_sunny);
        weatherIconList.put("14",R.drawable.icon_snow_s);
        weatherIconList.put("15",R.drawable.icon_snow_m);
        weatherIconList.put("16",R.drawable.icon_snow_l);
        weatherIconList.put("17",R.drawable.icon_snow_l);
        weatherIconList.put("18",R.drawable.icon_hoze);
        weatherIconList.put("19",R.drawable.icon_snow_rain);
        weatherIconList.put("20",R.drawable.icon_sand);
        weatherIconList.put("21",R.drawable.icon_rain_s);
        weatherIconList.put("22",R.drawable.icon_rain_m);
        weatherIconList.put("23",R.drawable.icon_rain_l);
        weatherIconList.put("24",R.drawable.icon_rainstorm);
        weatherIconList.put("25",R.drawable.icon_rainstorm);
        weatherIconList.put("26",R.drawable.icon_snow_s);
        weatherIconList.put("27",R.drawable.icon_snow_m);
        weatherIconList.put("28",R.drawable.icon_snow_l);
        weatherIconList.put("29",R.drawable.icon_frost);
        weatherIconList.put("30",R.drawable.icon_sand);
        weatherIconList.put("31",R.drawable.icon_sand);
        weatherIconList.put("53",R.drawable.icon_hoze);

        largeWeatherIconList.put("00", R.drawable.icon_sunny_l);
        largeWeatherIconList.put("01",R.drawable.icon_cloudy_l);
        largeWeatherIconList.put("02",R.drawable.icon_cloudy_sky_l);
        largeWeatherIconList.put("03",R.drawable.icon_rain_s_sunny_l);
        largeWeatherIconList.put("04",R.drawable.icon_thundershower_l);
        largeWeatherIconList.put("05",R.drawable.icon_thundershower_l);
        largeWeatherIconList.put("06",R.drawable.icon_snow_rain_l);
        largeWeatherIconList.put("07",R.drawable.icon_rain_s_l);
        largeWeatherIconList.put("08",R.drawable.icon_rain_m_l);
        largeWeatherIconList.put("09",R.drawable.icon_rain_l_l);
        largeWeatherIconList.put("10",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("11",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("12",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("13",R.drawable.icon_snow_s_sunny_l);
        largeWeatherIconList.put("14",R.drawable.icon_snow_s_l);
        largeWeatherIconList.put("15",R.drawable.icon_snow_m_l);
        largeWeatherIconList.put("16",R.drawable.icon_snow_l_l);
        largeWeatherIconList.put("17",R.drawable.icon_snow_l_l);
        largeWeatherIconList.put("18",R.drawable.icon_hoze_l);
        largeWeatherIconList.put("19",R.drawable.icon_snow_rain_l);
        largeWeatherIconList.put("20",R.drawable.icon_sand_l);
        largeWeatherIconList.put("21",R.drawable.icon_rain_s_l);
        largeWeatherIconList.put("22",R.drawable.icon_rain_m_l);
        largeWeatherIconList.put("23",R.drawable.icon_rain_l_l);
        largeWeatherIconList.put("24",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("25",R.drawable.icon_rainstorm_l);
        largeWeatherIconList.put("26",R.drawable.icon_snow_s_l);
        largeWeatherIconList.put("27",R.drawable.icon_snow_m_l);
        largeWeatherIconList.put("28",R.drawable.icon_snow_l_l);
        largeWeatherIconList.put("29",R.drawable.icon_frost_l);
        largeWeatherIconList.put("30",R.drawable.icon_sand_l);
        largeWeatherIconList.put("31",R.drawable.icon_sand_l);
        largeWeatherIconList.put("53",R.drawable.icon_hoze_l);

        backgroundList.put("00",R.drawable.sunny_background);
        backgroundList.put("01",R.drawable.sun_cloud_background);
        backgroundList.put("02",R.drawable.cloudy_background);
        backgroundList.put("03",R.drawable.rain_background);
        backgroundList.put("04",R.drawable.rain_background);
        backgroundList.put("05",R.drawable.rain_snow_background);
        backgroundList.put("06",R.drawable.rain_snow_background);
        backgroundList.put("07",R.drawable.rain_background);
        backgroundList.put("08",R.drawable.rain_background);
        backgroundList.put("09",R.drawable.rain_background);
        backgroundList.put("10",R.drawable.rainstorm_background);
        backgroundList.put("11",R.drawable.rainstorm_background);
        backgroundList.put("12",R.drawable.rainstorm_background);
        backgroundList.put("13",R.drawable.snow_background);
        backgroundList.put("14",R.drawable.snow_background);
        backgroundList.put("15",R.drawable.snow_background);
        backgroundList.put("16",R.drawable.snow_background);
        backgroundList.put("17",R.drawable.snow_background);
        backgroundList.put("18",R.drawable.fog_background);
        backgroundList.put("19",R.drawable.rain_background);
        backgroundList.put("20",R.drawable.storm_background);
        backgroundList.put("21",R.drawable.rain_background);
        backgroundList.put("22",R.drawable.rain_background);
        backgroundList.put("23",R.drawable.rain_background);
        backgroundList.put("24",R.drawable.rainstorm_background);
        backgroundList.put("25",R.drawable.rainstorm_background);
        backgroundList.put("26",R.drawable.snow_background);
        backgroundList.put("27",R.drawable.snow_background);
        backgroundList.put("28",R.drawable.snow_background);
        backgroundList.put("29",R.drawable.storm_background);
        backgroundList.put("30",R.drawable.storm_background);
        backgroundList.put("31",R.drawable.storm_background);
        backgroundList.put("53",R.drawable.haze_background);
    }
}
