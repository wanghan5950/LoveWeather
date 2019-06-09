package com.example.wanghanpc.loveweather.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * 其它工具
 */

public class Tools {

    /**
     * 判断时间是白天还是晚上
     */
    public static boolean timeIsDay (String timeAndDate){
        String time = timeAndDate.substring(11,13);
        int timeInt = Integer.parseInt(time);
        if (timeInt >= 6 && timeInt <= 18){
            return true;
        }
        return false;
    }
    /**
     * 将日期转为星期
     * @param time
     * @return
     */
    public static String getWeek(String time){
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int wek=c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += "星期日";
        }
        if (wek == 2) {
            Week += "星期一";
        }
        if (wek == 3) {
            Week += "星期二";
        }
        if (wek == 4) {
            Week += "星期三";
        }
        if (wek == 5) {
            Week += "星期四";
        }
        if (wek == 6) {
            Week += "星期五";
        }
        if (wek == 7) {
            Week += "星期六";
        }
        return Week;
    }
}
