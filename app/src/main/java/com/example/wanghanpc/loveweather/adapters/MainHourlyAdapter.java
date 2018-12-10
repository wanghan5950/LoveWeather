package com.example.wanghanpc.loveweather.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanghanpc.loveweather.myDefined.TempCurveView;
import com.example.wanghanpc.loveweather.tools.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.gson.weatherGson.Hourly;

import java.util.ArrayList;
import java.util.List;

public class MainHourlyAdapter extends RecyclerView.Adapter<MainHourlyAdapter.ViewHolder> {

    private List<Hourly> hourlyList;
    private int minTemp;
    private int maxTemp;
    private List<Integer> tempList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView hourlyTime;
        private ImageView hourlyIcon;
        private TextView hourlyHumText;
        private TempCurveView tempCurveView;
        private ImageView hourlyWindIcon;
        private TextView hourlyWindText;

        public ViewHolder(View itemView) {
            super(itemView);
            hourlyTime = (TextView) itemView.findViewById(R.id.hour_item_time);
            hourlyIcon = (ImageView) itemView.findViewById(R.id.hour_item_icon);
            hourlyHumText = (TextView) itemView.findViewById(R.id.hour_item_hum_text);
            tempCurveView = (TempCurveView) itemView.findViewById(R.id.temp_curve);
            hourlyWindIcon = (ImageView) itemView.findViewById(R.id.hour_item_wind_icon);
            hourlyWindText = (TextView) itemView.findViewById(R.id.hour_item_wind_text);
        }
    }

    public MainHourlyAdapter(List<Hourly> hourlyList) {
        this.hourlyList = hourlyList;
        for (Hourly hourly : hourlyList){
            tempList.add(Integer.parseInt(hourly.getTmp()));
        }
        int[] tempArray = new int[tempList.size()];
        for (int i = 0; i < tempList.size() ; i++){
            tempArray[i] = tempList.get(i);
        }
        int[] sortTempArray = sortTempArray(tempArray);
        this.minTemp = sortTempArray[0];
        this.maxTemp = sortTempArray[sortTempArray.length - 1];
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_hour_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hourly hourly = hourlyList.get(position);

        String temp = hourly.getTmp();
        String time = hourly.getTime().substring(11);
        String hum = hourly.getrHumidity()+"%";
        holder.hourlyTime.setText(time);
        String weatherCode = hourly.getCondCode();
        if (ReadyIconAndBackground.weatherNightIconList.containsKey(weatherCode)){
            if (judgeTimeIsDay(hourly.getTime())){
                holder.hourlyIcon.setImageResource(ReadyIconAndBackground.getWeatherIcon(weatherCode));
            }else {
                holder.hourlyIcon.setImageResource(ReadyIconAndBackground.getWeatherNightIcon(weatherCode));
            }
        }else {
            holder.hourlyIcon.setImageResource(ReadyIconAndBackground.getWeatherIcon(weatherCode));
        }
        holder.hourlyHumText.setText(hum);
        holder.hourlyWindIcon.setImageResource(ReadyIconAndBackground.getWindDirectionIcon(hourly.getWindDirection()));
        holder.hourlyWindText.setText(hourly.getWindDirection());

        float[] tempDataArray = new float[3];
        tempDataArray[1] = Float.parseFloat(temp);
        if (position <= 0){
            tempDataArray[0] = 0;
        }else {
            Hourly lastHourly = hourlyList.get(position - 1);
            tempDataArray[0] = (Float.parseFloat(lastHourly.getTmp()) + tempDataArray[1]) / 2;
        }
        if (position >= hourlyList.size() - 1){
            tempDataArray[2] = 0;
        }else {
            Hourly nextHourly = hourlyList.get(position + 1);
            tempDataArray[2] = (Float.parseFloat(nextHourly.getTmp()) + tempDataArray[1]) / 2;
        }
        holder.tempCurveView.setTempDataArray(tempDataArray);
        holder.tempCurveView.setMaxMinTemp(minTemp,maxTemp);
        holder.tempCurveView.setPosition(position,hourlyList.size());
    }

    @Override
    public int getItemCount() {
        return hourlyList.size();
    }

    /**
     * 判断时间是白天还是晚上
     */
    private boolean judgeTimeIsDay (String timeAndDate){
        String time = timeAndDate.substring(11,13);
        int timeInt = Integer.parseInt(time);
        if (timeInt >= 6 && timeInt <= 19){
            return true;
        }
        return false;
    }

    private int[] sortTempArray(int[] tempArray){
        int i,j;
        int flag;
        int length = tempArray.length;
        for (i = length - 1; i > 0; i--){
            flag = 0;
            for (j = 0; j < i; j++){
                if (tempArray[j] > tempArray[j + 1]){
                    int temp = tempArray[j];
                    tempArray[j] = tempArray[j + 1];
                    tempArray[j + 1] = temp;
                    flag = 1;
                }
            }
            if (flag == 0){
                break;
            }
        }
        return tempArray;
    }
}
