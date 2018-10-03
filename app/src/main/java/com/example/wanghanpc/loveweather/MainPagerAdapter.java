package com.example.wanghanpc.loveweather;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wanghanpc.loveweather.OtherEntityClass.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.util.Utility;
import com.example.wanghanpc.loveweather.weatherGson.DailyForecast;
import com.example.wanghanpc.loveweather.weatherGson.Lifestyle;
import com.example.wanghanpc.loveweather.weatherGson.Weather;

import java.util.List;

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
        LinearLayout dailyForecastLayout = (LinearLayout) view.findViewById(R.id.dailyForecast_layout);
        LinearLayout suggestionLayout = (LinearLayout) view.findViewById(R.id.suggestion_layout);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.hour_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        MainHourlyAdapter hourlyAdapter = new MainHourlyAdapter(weathersList.get(position).getHourlyList());
        recyclerView.setAdapter(hourlyAdapter);

        TextView degreeText = (TextView) view.findViewById(R.id.degree_text);
        ImageView nowDegreeIcon = (ImageView) view.findViewById(R.id.now_degree_icon);
        TextView feelTemp = (TextView) view.findViewById(R.id.feel_temp);
        TextView weatherInfo = (TextView) view.findViewById(R.id.weather_info);
        degreeText.setText(weathersList.get(position).getNow().getTmp());
        nowDegreeIcon.setImageResource(ReadyIconAndBackground.getLargeWeatherIcon(weathersList.get(position).getNow().getCondCode()));
        feelTemp.setText(weathersList.get(position).getNow().getFeelTemp());
        weatherInfo.setText(weathersList.get(position).getNow().getCondTxt());

        List<DailyForecast> dailyForecastList = weathersList.get(position).getDailyForecastList();
        dailyForecastLayout.removeAllViews();
        for (DailyForecast dailyForecast : dailyForecastList){
            View dailyView = LayoutInflater.from(context).inflate(R.layout.weather_future_item,dailyForecastLayout,false);
            TextView dailyItemDate = (TextView) dailyView.findViewById(R.id.date_text);
            ImageView weatherIcon1 = (ImageView) dailyView.findViewById(R.id.weather_icon_1);
            ImageView weatherIcon2 = (ImageView) dailyView.findViewById(R.id.weather_icon_2);
            TextView dailyTemp = (TextView) dailyView.findViewById(R.id.daily_item_temp);
            dailyItemDate.setText(Utility.getWeek(dailyForecast.getDate()));
            weatherIcon1.setImageResource(ReadyIconAndBackground.getWeatherIcon(dailyForecast.getCondCodeDay()));
            weatherIcon2.setImageResource(ReadyIconAndBackground.getWeatherIcon(dailyForecast.getCondCodeNight()));
            String temp = dailyForecast.getTempMin()+" ~ "+dailyForecast.getTempMax();
            dailyTemp.setText(temp);
            dailyForecastLayout.addView(dailyView);
        }
        List<Lifestyle> lifestyleList = weathersList.get(position).getLifestyleList();
        suggestionLayout.removeAllViews();
        for (Lifestyle lifestyle : lifestyleList){
            View lifeStyleView = LayoutInflater.from(context).inflate(R.layout.suggestion_item,suggestionLayout,false);
            ImageView lifeStyleIcon = (ImageView) lifeStyleView.findViewById(R.id.suggestion_item_icon);
            TextView lifeStyleName = (TextView) lifeStyleView.findViewById(R.id.suggestion_item_name);
            TextView lifeStyleText = (TextView) lifeStyleView.findViewById(R.id.suggestion_item_text);
            lifeStyleIcon.setImageResource(ReadyIconAndBackground.getLifeStyleIcon(lifestyle.getType()));
            lifeStyleName.setText(ReadyIconAndBackground.getLisfeStyleText(lifestyle.getType()));
            lifeStyleText.setText(lifestyle.getBrf());
            suggestionLayout.addView(lifeStyleView);
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
