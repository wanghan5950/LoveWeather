package com.example.wanghanpc.loveweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanghanpc.loveweather.OtherEntityClass.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.util.Utility;
import com.example.wanghanpc.loveweather.weatherGson.Weather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> implements PlaceItemTouchCallback.ItemTouchAdapter {

    private static final int PLACES_MODE_CHECK = 0;
    private int editMode = PLACES_MODE_CHECK;

    private List<Weather> weatherList;
    private List<String> placeNameList;
    private OnItemClickListener placeOnItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView placeItemLocation;
        TextView placeItemProvince;
        TextView placeItemCity;
        TextView placeItemWeek;
        TextView placeItemTime;
        TextView placeItemTemp;
        TextView placeItemForecast;
        ImageView placeItemIcon;
        ImageView placeCheckBox;

        private ViewHolder(View view){
            super(view);
            placeItemLocation = (TextView) view.findViewById(R.id.place_item_location);
            placeItemProvince = (TextView) view.findViewById(R.id.place_item_province);
            placeItemCity = (TextView) view.findViewById(R.id.place_item_city);
            placeItemWeek = (TextView) view.findViewById(R.id.place_item_week);
            placeItemTime = (TextView) view.findViewById(R.id.place_item_time);
            placeItemTemp = (TextView) view.findViewById(R.id.place_item_temp);
            placeItemForecast = (TextView) view.findViewById(R.id.place_item_forecast);
            placeItemIcon = (ImageView) view.findViewById(R.id.place_item_icon);
            placeCheckBox = (ImageView) view.findViewById(R.id.place_item_check);
        }
    }

    public PlacesAdapter(List<Weather> weatherList, List<String> placeNameList){
        this.weatherList = weatherList;
        this.placeNameList = placeNameList;
    }

    public List<Weather> getWeatherList(){
        return weatherList;
    }

    public List<String> getPlaceNameList(){
        return placeNameList;
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition){
            for (int i = fromPosition; i < toPosition; i++){
                Collections.swap(weatherList, i, i + 1);
                Collections.swap(placeNameList,i, i + 1);
            }
        }else if (fromPosition > toPosition){
            for (int i = fromPosition; i > toPosition; i--){
                Collections.swap(weatherList, i, i - 1);
                Collections.swap(placeNameList,i, i - 1);
            }
        }else {
            return;
        }
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onSwiped(int position) {
    }

    @Override
    public void saveWeatherList() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        final Weather weather = weatherList.get(holder.getAdapterPosition());

        String location = weather.getBasic().getLocation();
        String province = weather.getBasic().getAdminArea();
        String city = ","+weather.getBasic().getParentCity();
        String date = weather.getUpdate().getLoc().substring(0,10);
        String time = weather.getUpdate().getLoc().substring(11) + "更新";
        String week = Utility.getWeek(date)+",";
        String temp = weather.getNow().getTmp();
        String weatherInfo = weather.getNow().getCondTxt();
        int iconId = ReadyIconAndBackground.getLargeWeatherIcon(weather.getNow().getCondCode());
        holder.placeItemLocation.setText(location);
        if (!weather.getBasic().getAdminArea().equals(weather.getBasic().getParentCity())){
            holder.placeItemProvince.setText(province);
            holder.placeItemCity.setText(city);
        }else {
            holder.placeItemProvince.setText(province);
        }
        holder.placeItemWeek.setText(week);
        holder.placeItemTime.setText(time);
        holder.placeItemTemp.setText(temp);
        holder.placeItemForecast.setText(weatherInfo);
        holder.placeItemIcon.setImageResource(iconId);

        if (editMode == PLACES_MODE_CHECK){
            holder.placeCheckBox.setVisibility(View.GONE);
        }else {
            holder.placeCheckBox.setVisibility(View.VISIBLE);
            if (weather.getSelected()){
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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                placeOnItemClickListener.onItemLongClickListener(holder.getAdapterPosition(),weatherList);
                return false;
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.placeOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClickListener(int position, List<Weather> weatherList);
        void onItemLongClickListener(int position, List<Weather> weatherList);
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
