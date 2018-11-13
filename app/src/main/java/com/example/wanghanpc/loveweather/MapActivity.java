package com.example.wanghanpc.loveweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.wanghanpc.loveweather.OtherEntityClass.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.cityGson.City;
import com.example.wanghanpc.loveweather.cityGson.CityBackResult;
import com.example.wanghanpc.loveweather.tools.Tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapActivity extends BaseActivity {

    private MapView mapView;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private BaiduMap baiduMap;
    private double myLatitude;
    private double myLongitude;
    private String longAndLat;
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
    private EditText editText;
    private String cityText;
    private static CityBackResult cityBackResult;
    private List<String> cityNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.map_mapView);
        mapView.getMap().setMaxAndMinZoomLevel(17,6);
        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        UiSettings uiSettings = baiduMap.getUiSettings();
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setOverlookingGesturesEnabled(false);
        uiSettings.setEnlargeCenterWithDoubleClickEnable(false);

        listView = (ListView) findViewById(R.id.map_listView);
        adapter = new ArrayAdapter<>(MapActivity.this,android.R.layout.simple_list_item_1,cityNameList);
        listView.setAdapter(adapter);
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
        editText = (EditText) findViewById(R.id.map_edit_text);
        mapBarLayout.setVisibility(View.GONE);
        mapProgressBarLayout.setVisibility(View.GONE);

        getCityListFromDatabase();
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
        prepareEditTextListener();
        prepareListViewListener();
        mapBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (weatherResult.getBasic().getCountry().equals("中国")) {
                    City city = new City();
                    city.setCityId(weatherResult.getBasic().getCityId());
                    city.setLocationName(weatherResult.getBasic().getLocation());
                    city.setParentCity(weatherResult.getBasic().getParentCity());
                    city.setAdminArea(weatherResult.getBasic().getAdminArea());
                    if (placeNameList.contains(city)) {
                        Toast.makeText(MapActivity.this, "已注册", Toast.LENGTH_SHORT).show();
                    } else {
                        placeNameList.add(city);
                        Toast.makeText(MapActivity.this, "添加完成", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MapActivity.this,"无法添加国外城市",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
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
                        mapBarLayout.setVisibility(View.INVISIBLE);
                        Log.d("MapActivity","--------------------------使用已定的位置");
                        signLocationOnMap(myLatitude,myLongitude);
                        longAndLat = String.valueOf(myLongitude) + "," + String.valueOf(myLatitude);
                        mapProgressBarLayout.setVisibility(View.VISIBLE);
                        requestWeather(longAndLat);
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
                longAndLat = String.valueOf(myLongitude) + "," + String.valueOf(myLatitude);
                Log.d("MapActivity","==============我的经纬度是" + longAndLat);
                mapProgressBarLayout.setVisibility(View.VISIBLE);
                requestWeather(longAndLat);
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
                mapBarLayout.setVisibility(View.INVISIBLE);
                signLocationOnMap(latLng.latitude,latLng.longitude);
                longAndLat = String.valueOf(latLng.longitude) + "," + String.valueOf(latLng.latitude);
                Log.d("MapActivity","==============经纬度是" + longAndLat);
                mapProgressBarLayout.setVisibility(View.VISIBLE);
                requestWeather(longAndLat);
            }
        });
    }

    /**
     * EditText内容监听事件
     */
    private void prepareEditTextListener(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cityText = editText.getText().toString();
                if (cityText != null && !cityText.equals("")) {
                    String cityTextHead = cityText.substring(0,1);
                    Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");//判断输入的内容是否为汉字
                    Matcher matcher = pattern.matcher(cityTextHead);
                    if (matcher.matches()){
                        requestCityList(cityText);
                    }
                    mapView.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    mapBarLayout.setVisibility(View.GONE);
                }else {
                    listView.setVisibility(View.GONE);
                    mapBarLayout.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     *ListView点击事件
     */
    private void prepareListViewListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Double lat = Double.parseDouble(cityBackResult.getCityList().get(position).getLat());
                Double lon = Double.parseDouble(cityBackResult.getCityList().get(position).getLon());
                longAndLat = cityBackResult.getCityList().get(position).getLon() + "," + cityBackResult.getCityList().get(position).getLat();
                listView.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);
                hideInput();
                editText.setText("");
                signLocationOnMap(lat,lon);
                mapProgressBarLayout.setVisibility(View.VISIBLE);
                requestWeather(longAndLat);
            }
        });
    }

    @Override
    protected void updateWeatherUI() {
        mapProgressBarLayout.setVisibility(View.INVISIBLE);
        showWeatherInfo();
    }

    /**
     * 在mapBar上显示天气信息
     */
    private void showWeatherInfo(){
        mapProgressBarLayout.setVisibility(View.GONE);
        mapBarLayout.setVisibility(View.VISIBLE);
        if (weatherResult.getBasic().getCountry().equals("中国")) {
            String location = weatherResult.getBasic().getLocation();
            String province = weatherResult.getBasic().getAdminArea() + "省";
            String city = "," + weatherResult.getBasic().getParentCity() + "市";
            String date = weatherResult.getUpdate().getLoc().substring(0,10);
            String time = weatherResult.getUpdate().getLoc().substring(11) + "更新";
            String week = Tools.getWeek(date) + ",";
            String temp = weatherResult.getNow().getTmp();
            String forecast = weatherResult.getNow().getCondTxt();
            int iconId = ReadyIconAndBackground.getLargeWeatherIcon(weatherResult.getNow().getCondCode());
            mapBarLocation.setText(location);
            if (weatherResult.getBasic().getAdminArea().equals(weatherResult.getBasic().getParentCity())
                    && weatherResult.getBasic().getParentCity().equals(weatherResult.getBasic().getLocation())) {
                mapBarCity.setText(city);
            } else {
                mapBarProvince.setText(province);
                mapBarCity.setText(city);
            }
            mapBarIcon.setImageResource(iconId);
            mapBarDate.setText(week);
            mapBarTime.setText(time);
            mapBarTemp.setText(temp);
            mapBarForecast.setText(forecast);
        }else {
            String location = weatherResult.getBasic().getLocation();
            String country = weatherResult.getBasic().getCountry();
            String date = weatherResult.getUpdate().getLoc().substring(0,10);
            String time = weatherResult.getUpdate().getLoc().substring(11) + "更新";
            String week = Tools.getWeek(date) + ",";
            String temp = weatherResult.getNow().getTmp();
            String forecast = weatherResult.getNow().getCondTxt();
            int iconId = ReadyIconAndBackground.getLargeWeatherIcon(weatherResult.getNow().getCondCode());
            mapBarLocation.setText(location);
            mapBarProvince.setText(country);
            mapBarCity.setText("");
            mapBarIcon.setImageResource(iconId);
            mapBarDate.setText(week);
            mapBarTime.setText(time);
            mapBarTemp.setText(temp);
            mapBarForecast.setText(forecast);
        }
    }

    @Override
    protected void updateCity(CityBackResult result) {
        MapActivity.cityBackResult = result;
        showCityList();
    }

    /**
     * 加载并显示城市列表
     */
    private void showCityList(){
        cityNameList.clear();
        if (cityBackResult.getCityList() != null && cityBackResult.getCityList().size() != 0) {
            List<City> list = cityBackResult.getCityList();
            int size = list.size();
            for (int i = 0; i < size; i++){
                cityNameList.add(list.get(i).getLocationName());
            }
        }
        adapter.notifyDataSetChanged();
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
    private void signLocationOnMap(double longitude, double latitude){
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(latitude);
        builder.longitude(longitude);
        MyLocationData locationData = builder.build();
        baiduMap.setMyLocationData(locationData);
        Log.d("MapActivity","----------------------------标记成功");
    }

    /**
     * 隐藏键盘
     */
    private void hideInput(){
        InputMethodManager methodManager = (InputMethodManager) MapActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        saveCityListToDatabase(placeNameList);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
}
