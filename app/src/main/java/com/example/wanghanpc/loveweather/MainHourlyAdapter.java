package com.example.wanghanpc.loveweather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanghanpc.loveweather.OtherEntityClass.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.weatherGson.Hourly;

import java.util.List;

public class MainHourlyAdapter extends RecyclerView.Adapter<MainHourlyAdapter.ViewHolder> {

    private List<Hourly> hourlyList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView hourlyTime;
        private ImageView hourlyIcon;
        private TextView hourlyHumText;
        private TextView hourlyTemp;
        private ImageView hourlyWindIcon;
        private TextView hourlyWindText;

        public ViewHolder(View itemView) {
            super(itemView);
            hourlyTime = (TextView) itemView.findViewById(R.id.hour_item_time);
            hourlyIcon = (ImageView) itemView.findViewById(R.id.hour_item_icon);
            hourlyHumText = (TextView) itemView.findViewById(R.id.hour_item_hum_text);
            hourlyTemp = (TextView) itemView.findViewById(R.id.hour_item_temp);
            hourlyWindIcon = (ImageView) itemView.findViewById(R.id.hour_item_wind_icon);
            hourlyWindText = (TextView) itemView.findViewById(R.id.hour_item_wind_text);
        }
    }

    public MainHourlyAdapter(List<Hourly> hourlyList) {
        this.hourlyList = hourlyList;
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
        holder.hourlyTemp.setText(hourly.getTmp());
        holder.hourlyWindIcon.setImageResource(ReadyIconAndBackground.getWindDirectionIcon(hourly.getWindDirection()));
        holder.hourlyWindText.setText(hourly.getWindDirection());
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
}
