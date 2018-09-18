package com.example.wanghanpc.loveweather;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wanghanpc.loveweather.weatherGson.Future;
import com.example.wanghanpc.loveweather.weatherGson.Weather;

import java.util.List;
import java.util.Map;

public class MainPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Weather> weathersList;

    public MainPagerAdapter(Context context, List<Weather> weathersList) {
        this.context = context;
        this.weathersList = weathersList;
    }

    @Override
    public int getCount(){
        return weathersList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        View view = View.inflate(context,R.layout.page,null);
        view.setTag(position);
        //加载布局
        LinearLayout futureLayout = (LinearLayout) view.findViewById(R.id.future_layout);
        TextView degreeText = (TextView) view.findViewById(R.id.degree_text);
        ImageView nowDegreeIcon = (ImageView) view.findViewById(R.id.now_degree_icon);
        TextView tempRange = (TextView) view.findViewById(R.id.temp_range);
        TextView weatherInfo = (TextView) view.findViewById(R.id.weather_info);
        TextView dressingAdvice = (TextView) view.findViewById(R.id.dressing_index_text);
        TextView wind = (TextView) view.findViewById(R.id.wind_text);
        TextView washIndex = (TextView) view.findViewById(R.id.wash_index_text);
        TextView travelIndex = (TextView) view.findViewById(R.id.travel_index_text);
        TextView exerciseIndex = (TextView) view.findViewById(R.id.exercise_index_text);
        TextView humidity = (TextView) view.findViewById(R.id.humidity_text);
        TextView uvIndex = (TextView) view.findViewById(R.id.uv_index_text);
        degreeText.setText(weathersList.get(position).result.sk.temp);
        nowDegreeIcon.setImageResource(MainActivity.getLargeWeatherIcon(weathersList.get(position).result.today.weatherChangeId.startWeather));
        tempRange.setText(weathersList.get(position).result.today.temperature);
        weatherInfo.setText(weathersList.get(position).result.today.weather);
        dressingAdvice.setText(weathersList.get(position).result.today.dressingAdvice);
        wind.setText(weathersList.get(position).result.today.wind);
        washIndex.setText(weathersList.get(position).result.today.washIndex);
        travelIndex.setText(weathersList.get(position).result.today.travelIndex);
        exerciseIndex.setText(weathersList.get(position).result.today.exerciseIndex);
        humidity.setText(weathersList.get(position).result.sk.humidity);
        uvIndex.setText(weathersList.get(position).result.today.uvIndex);
        Map<String,Future> futureMap = weathersList.get(position).result.futureMap;
        futureLayout.removeAllViews();
        for (String key : futureMap.keySet()){
            View futureView = LayoutInflater.from(context).inflate(R.layout.weather_future_item,futureLayout,false);
            TextView futureItemDate = (TextView) futureView.findViewById(R.id.date_text);
            ImageView weatherIcon1 = (ImageView) futureView.findViewById(R.id.weather_icon_1);
            ImageView weatherIcon2 = (ImageView) futureView.findViewById(R.id.weather_icon_2);
            TextView futureItemTempRang = (TextView) futureView.findViewById(R.id.future_item_temp_range);
            futureItemDate.setText(futureMap.get(key).week);
            weatherIcon1.setImageResource(MainActivity.getWeatherIcon(futureMap.get(key).futureWeatherId.startWeather));
            weatherIcon2.setImageResource(MainActivity.getWeatherIcon(futureMap.get(key).futureWeatherId.lastWeather));
            futureItemTempRang.setText(futureMap.get(key).temperature);
            futureLayout.addView(futureView);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view == object;
    }

    @Override
    public int getItemPosition(Object object){
        View view = (View)object;
        int currentPage = ((MainActivity)context).getPagePosition();
        if (currentPage == (Integer)view.getTag()){
            return POSITION_NONE;
        }else {
            return POSITION_UNCHANGED;
        }
    }
}
