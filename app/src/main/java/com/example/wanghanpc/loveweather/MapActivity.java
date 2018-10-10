package com.example.wanghanpc.loveweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.wanghanpc.loveweather.OtherEntityClass.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.util.Utility;
import com.example.wanghanpc.loveweather.weatherGson.Weather;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends BaseActivity {

    private LocationClient locationClient;
    private MapView mapView;
    private Toolbar toolbar;
    private BaiduMap baiduMap;
    private UiSettings uiSettings;
    private double myLatitude;
    private double myLongitude;
    private String latAndLong;
    private TextView mapBarLocation;
    private TextView mapBarProvince;
    private TextView mapBarCity;
    private TextView mapBarDate;
    private TextView mapBarTime;
    private TextView mapBarTemp;
    private TextView mapBarForecast;
    private ImageView mapBarIcon;
    private RelativeLayout mapBarLayout;
    private RelativeLayout mapProgressBarLayout;
    private Weather weather;
    private List<String> placeNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.map_mapView);
        mapView.getMap().setMaxAndMinZoomLevel(17,6);
        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        uiSettings = baiduMap.getUiSettings();
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setOverlookingGesturesEnabled(false);
        uiSettings.setEnlargeCenterWithDoubleClickEnable(false);

        mapBarLocation = (TextView) findViewById(R.id.map_bar_location);
        mapBarProvince = (TextView) findViewById(R.id.map_bar_province);
        mapBarCity = (TextView) findViewById(R.id.map_bar_city);
        mapBarDate = (TextView) findViewById(R.id.map_bar_date);
        mapBarTime = (TextView) findViewById(R.id.map_bar_time);
        mapBarForecast = (TextView) findViewById(R.id.map_bar_forecast);
        mapBarTemp = (TextView) findViewById(R.id.map_bar_temp);
        mapBarIcon = (ImageView) findViewById(R.id.map_bar_icon);
        mapBarLayout = (RelativeLayout) findViewById(R.id.map_bar_layout);
        mapProgressBarLayout = (RelativeLayout) findViewById(R.id.map_progressBar_layout);
        mapBarLayout.setVisibility(View.GONE);
        mapProgressBarLayout.setVisibility(View.GONE);

        getPlaceNameListFromShared();
        initToolbar();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()){
            uiSettings.setAllGesturesEnabled(true);
            requestLocation();
        }else {
            Toast.makeText(MapActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
            uiSettings.setAllGesturesEnabled(false);
        }
        setLongClickListener();
        mapBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = weather.getBasic().getLocation();
                if (placeNameList.contains(place)){
                    Toast.makeText(MapActivity.this,"已注册",Toast.LENGTH_SHORT).show();
                }else {
                    placeNameList.add(place);
                    setPlaceNameListToShared();
                    Toast.makeText(MapActivity.this,"添加完成",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 初始化toolbar及其点击事件
     */
    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.map_toolbar);
        toolbar.inflateMenu(R.menu.toolbar_map);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.locate:
                        Log.d("MapActivity","--------------------------使用已定的位置");
                        signLocationOnMap(myLatitude,myLongitude);
                        latAndLong = String.valueOf(myLatitude) + "," + String.valueOf(myLongitude);
                        Utility.requestWeather(latAndLong,MapActivity.this);
                        showTheSignLocationWeather();
                    default:
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_map,menu);
        return true;
    }

    public class MyLocationListener extends BDAbstractLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
                Log.d("MapActivity","--------------------------定位成功");
                locateToMyLocation(bdLocation);
                latAndLong = String.valueOf(myLatitude) + "," + String.valueOf(myLongitude);
                Log.d("MapActivity","==============我的经纬度是" + latAndLong);
                Utility.requestWeather(latAndLong,MapActivity.this);
                showTheSignLocationWeather();
            }
        }
    }

    /**
     * 设置长按监听
     */
    private void setLongClickListener(){
        baiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mapBarLayout.setVisibility(View.GONE);
                signLocationOnMap(latLng.latitude,latLng.longitude);
                latAndLong = String.valueOf(latLng.latitude) + "," + String.valueOf(latLng.longitude);
                Log.d("MapActivity","==============经纬度是" + latAndLong);
                Utility.requestWeather(latAndLong,MapActivity.this);
                showTheSignLocationWeather();
            }
        });
    }

    /**
     * 显示标记点的天气
     */
    private void showTheSignLocationWeather(){
        mapProgressBarLayout.setVisibility(View.VISIBLE);
        while (true){
            if (Utility.isRequestDoneOrNot()){
                weather = Utility.getWeather();
                break;
            }
        }
        mapProgressBarLayout.setVisibility(View.GONE);
        mapBarLayout.setVisibility(View.VISIBLE);
        String location = weather.getBasic().getLocation();
        String province = weather.getBasic().getAdminArea() + "省";
        String city = ","+weather.getBasic().getParentCity() + "市";
        String date = weather.getUpdate().getLoc().substring(0,10);
        String time = weather.getUpdate().getLoc().substring(11) + "更新";
        String week = Utility.getWeek(date)+",";
        String temp = weather.getNow().getTmp();
        String forecast = weather.getNow().getCondTxt();
        int iconId = ReadyIconAndBackground.getLargeWeatherIcon(weather.getNow().getCondCode());
        mapBarLocation.setText(location);
        if (weather.getBasic().getAdminArea().equals(weather.getBasic().getParentCity())
                && weather.getBasic().getParentCity().equals(weather.getBasic().getLocation())){
            mapBarCity.setText(city);
        }else {
            mapBarProvince.setText(province);
            mapBarCity.setText(city);
        }
        mapBarIcon.setImageResource(iconId);
        mapBarDate.setText(week);
        mapBarTime.setText(time);
        mapBarTemp.setText(temp);
        mapBarForecast.setText(forecast);
    }

    /**
     * 百度定位，获取地址
     */
    private void requestLocation(){
        getDetailedLocation();
        locationClient.start();
    }

    /**
     * 百度定位，允许详细地址
     */
    private void getDetailedLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        locationClient.setLocOption(option);
    }

    /**
     * 定位到自己的位置
     */
    private void locateToMyLocation(BDLocation bdLocation){
        LatLng latLng = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(6);
        baiduMap.animateMapStatus(update);
        Log.d("MapActivity","-------------------------找到位置");
        signLocationOnMap(bdLocation.getLatitude(),bdLocation.getLongitude());
        myLatitude = bdLocation.getLatitude();
        myLongitude = bdLocation.getLongitude();
    }

    /**
     * 标记位置
     */
    private void signLocationOnMap(double latitude, double longitude){
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(latitude);
        builder.longitude(longitude);
        MyLocationData locationData = builder.build();
        baiduMap.setMyLocationData(locationData);
        Log.d("MapActivity","----------------------------标记成功");
    }

    /**
     * 将城市名列表保存到SharedPreference
     */
    private void setPlaceNameListToShared(){
        SharedPreferences.Editor editor = getSharedPreferences("placeNameList",MODE_PRIVATE).edit();
        editor.putInt("listSize",placeNameList.size());
        for (int i = 0; i < placeNameList.size(); i++){
            editor.putString("item_"+i,placeNameList.get(i));
        }
        editor.apply();
    }

    /**
     * 从SharedPreference中获取城市名列表
     */
    private void getPlaceNameListFromShared(){
        SharedPreferences preferences = getSharedPreferences("placeNameList",MODE_PRIVATE);
        int index = preferences.getInt("listSize",0);
        for (int i = 0; i < index; i++){
            String place = preferences.getString("item_"+i,null);
            if (!placeNameList.contains(place) && place != null) {
                placeNameList.add(place);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
}
