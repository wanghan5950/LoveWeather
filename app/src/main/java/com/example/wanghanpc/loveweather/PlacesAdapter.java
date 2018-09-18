package com.example.wanghanpc.loveweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanghanpc.loveweather.weatherGson.Weather;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder>{

    private static final int PLACES_MODE_CHECK = 0;
    private int editMode = PLACES_MODE_CHECK;

    private List<Weather> weatherList;
    private OnItemClickListener placeOnItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView placeItemName;
        TextView placeItemWeek;
        TextView placeItemTime;
        TextView placeItemTemp;
        TextView placeItemForecast;
        ImageView placeItemIcon;
        ImageView placeCheckBox;

        public ViewHolder(View view){
            super(view);
            placeItemName = (TextView) view.findViewById(R.id.place_item_name);
            placeItemWeek = (TextView) view.findViewById(R.id.place_item_week);
            placeItemTime = (TextView) view.findViewById(R.id.place_item_time);
            placeItemTemp = (TextView) view.findViewById(R.id.place_item_temp);
            placeItemForecast = (TextView) view.findViewById(R.id.place_item_forecast);
            placeItemIcon = (ImageView) view.findViewById(R.id.place_item_icon);
            placeCheckBox = (ImageView) view.findViewById(R.id.place_item_check);
        }
    }

    public PlacesAdapter(List<Weather> weatherList){
        this.weatherList = weatherList;
    }

    public List<Weather> getWeatherList(){
        if (weatherList == null){
            weatherList = new ArrayList<>();
        }
        return weatherList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        final Weather weather = weatherList.get(holder.getAdapterPosition());

        String city = weather.result.today.city;
        String week = weather.result.today.week+",";
        String time = weather.result.sk.time+"更新";
        String temp = weather.result.sk.temp;
        String forecast = weather.result.today.weather;
        int iconId = MainActivity.getLargeWeatherIcon(weather.result.today.weatherChangeId.startWeather);
        holder.placeItemName.setText(city);
        holder.placeItemWeek.setText(week);
        holder.placeItemTime.setText(time);
        holder.placeItemTemp.setText(temp);
        holder.placeItemForecast.setText(forecast);
        holder.placeItemIcon.setImageResource(iconId);

        if (editMode == PLACES_MODE_CHECK){
            holder.placeCheckBox.setVisibility(View.GONE);
        }else {
            holder.placeCheckBox.setVisibility(View.VISIBLE);
            if (weather.isSelected){
                holder.placeCheckBox.setImageResource(R.mipmap.selected_blue);
            }else {
                holder.placeCheckBox.setImageResource(R.mipmap.unselected);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOnItemClickListener.onItemClickListener(holder.getAdapterPosition(),weatherList);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.placeOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClickListener(int position, List<Weather> weatherList);
    }

    @Override
    public int getItemCount(){
        return weatherList.size();
    }

    public void setEditMode(int editMode){
        this.editMode = editMode;
        notifyDataSetChanged();
    }
}
