package com.example.wanghanpc.loveweather.adapters;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wanghanpc.loveweather.tools.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.gson.cityGson.City;
import com.example.wanghanpc.loveweather.tools.PxAndDp;
import com.example.wanghanpc.loveweather.tools.Tools;
import com.example.wanghanpc.loveweather.gson.weatherGson.Weather;

import java.util.Collections;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> implements PlaceItemTouchCallback.ItemTouchAdapterInterface {

    private static final int PLACE_MODE_CHECK = 0;
    private static final int PLACE_MODE_EDIT = 1;
    private static final int PLACE_MODE_EDIT_CLICK = 2;
    private static final int PLACE_MODE_BACK_TO_CHECK = 3;
    private int editMode = PLACE_MODE_CHECK;

    private List<Weather> weatherList;
    private List<City> placeNameList;
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
        RelativeLayout placeItemLayout;

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
            placeItemLayout = (RelativeLayout) view.findViewById(R.id.place_item_layout);
        }

        private void checkBoxAnimator(){
            ValueAnimator valueAnimator = ValueAnimator.ofInt(PxAndDp.dip2px(-40), PxAndDp.dip2px(15));
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentValue = (Integer) animation.getAnimatedValue();
                    placeCheckBox.setTranslationX(currentValue);
                    placeCheckBox.requestLayout();
                }
            });
            valueAnimator.start();
        }
        private void checkBoxAnimatorBack(){
            ValueAnimator valueAnimator = ValueAnimator.ofInt(PxAndDp.dip2px(15), PxAndDp.dip2px(-40));
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentValue = (Integer) animation.getAnimatedValue();
                    placeCheckBox.setTranslationX(currentValue);
                    placeCheckBox.requestLayout();
                }
            });
            valueAnimator.start();
        }
        private void placeItemAnimator(){
            ValueAnimator valueAnimator = ValueAnimator.ofInt(PxAndDp.dip2px(0),PxAndDp.dip2px(45));
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentValue = (Integer) animation.getAnimatedValue();
                    placeItemLayout.setTranslationX(currentValue);
                    placeItemLayout.requestLayout();
                }
            });
            valueAnimator.start();
        }
        private void placeItemAnimatorBack(){
            ValueAnimator valueAnimator = ValueAnimator.ofInt(PxAndDp.dip2px(45),PxAndDp.dip2px(0));
            valueAnimator.setDuration(300);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int currentValue = (Integer) animation.getAnimatedValue();
                    placeItemLayout.setTranslationX(currentValue);
                    placeItemLayout.requestLayout();
                }
            });
            valueAnimator.start();
        }
    }

    public PlacesAdapter(List<Weather> weatherList, List<City> placeNameList){
        this.weatherList = weatherList;
        this.placeNameList = placeNameList;
    }

    public List<Weather> getWeatherList(){
        return weatherList;
    }

    public List<City> getPlaceNameList(){
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
        notifyItemRemoved(position);
        weatherList.remove(position);
        placeNameList.remove(position);
    }

    @Override
    public void onClearView(int position) {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position){
        final Weather weather = weatherList.get(holder.getAdapterPosition());

        String location = weather.getBasic().getLocation();
        String province = weather.getBasic().getAdminArea() + "省,";
        String city = weather.getBasic().getParentCity() + "市";
        String date = weather.getUpdate().getLoc().substring(0,10);
        String time = weather.getUpdate().getLoc().substring(11) + "更新";
        String week = Tools.getWeek(date)+",";
        String temp = weather.getNow().getTmp();
        String weatherInfo = weather.getNow().getCondTxt();
        int iconId = ReadyIconAndBackground.getLargeWeatherIcon(weather.getNow().getCondCode());
        holder.placeItemLocation.setText(location);
        if (weather.getBasic().getAdminArea().equals(weather.getBasic().getParentCity())
                && weather.getBasic().getParentCity().equals(weather.getBasic().getLocation())){
            holder.placeItemProvince.setText("");
            holder.placeItemCity.setText(city);
        }else {
            holder.placeItemProvince.setText(province);
            holder.placeItemCity.setText(city);
        }
        holder.placeItemWeek.setText(week);
        holder.placeItemTime.setText(time);
        holder.placeItemTemp.setText(temp);
        holder.placeItemForecast.setText(weatherInfo);
        holder.placeItemIcon.setImageResource(iconId);

        if (editMode == PLACE_MODE_EDIT){
            holder.placeItemAnimator();
            holder.checkBoxAnimator();
        }else if (editMode == PLACE_MODE_BACK_TO_CHECK){
            holder.placeItemAnimatorBack();
            holder.checkBoxAnimatorBack();
        }else if (editMode == PLACE_MODE_EDIT_CLICK){
            holder.placeItemLayout.setTranslationX(PxAndDp.dip2px(45));
            holder.placeCheckBox.setTranslationX(PxAndDp.dip2px(15));
        }else {
            holder.placeItemLayout.setTranslationX(PxAndDp.dip2px(0));
            holder.placeCheckBox.setTranslationX(PxAndDp.dip2px(-40));
        }
        if (editMode == PLACE_MODE_CHECK){
            holder.placeCheckBox.setVisibility(View.INVISIBLE);
        }else {
            holder.placeCheckBox.setVisibility(View.VISIBLE);
            if (weather.getSelected()){
                holder.placeCheckBox.setImageResource(R.mipmap.selected_blue);
            }else {
                holder.placeCheckBox.setImageResource(R.mipmap.unselected);
            }
        }
        holder.placeItemLayout.setBackgroundResource(getBackground(position));

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

    /**
     * 获取背景资源
     * @return
     */
    private int getBackground(int position){
        Weather weather = weatherList.get(position);
        return ReadyIconAndBackground.getItemBackground(weather.getNow().getCondCode());
    }
}
