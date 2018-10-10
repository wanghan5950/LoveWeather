package com.example.wanghanpc.loveweather;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.wanghanpc.loveweather.OtherEntityClass.MySwipeRefreshLayout;
import com.example.wanghanpc.loveweather.OtherEntityClass.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.weatherGson.Weather;
import com.example.wanghanpc.loveweather.util.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private LocationClient locationClient;
    private int pagePosition = 0;
    private static String location;
    private List<String> placeNameList = new ArrayList<>();
    private List<Weather> weatherList = new ArrayList<>();
    private TextView toolbarTitle;
    private TextView toolbarWeek;
    private TextView toolbarTime;
    private ImageView lastPage;
    private ImageView nextPage;
    private MySwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout backgroundImage;
    private ViewPager viewPager;
    private MainPagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReadyIconAndBackground.initWeatherIconAndBackground();
        setNotificationChannel();
        //初始化控件
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarWeek = (TextView) findViewById(R.id.toolbar_week);
        toolbarTime = (TextView) findViewById(R.id.toolbar_time);
        lastPage = (ImageView) findViewById(R.id.last_page_button);
        nextPage = (ImageView) findViewById(R.id.next_page_button);
        swipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        backgroundImage = (CoordinatorLayout) findViewById(R.id.main_layout);
        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        progressBar = (ProgressBar) findViewById(R.id.main_progress_bar);

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());

        progressBar.setVisibility(View.VISIBLE);
        judgePermission();
        initToolbar();
        setSwipeRefreshLayoutListener();
        lastPage.setOnClickListener(this);
        nextPage.setOnClickListener(this);
    }

    /**
     * 根据PlaceActivity和SearchActivity返回的数据，更新信息或跳转到指定页
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK){
                    pagePosition = data.getIntExtra("position",0);
                    placeNameList = (List<String>)data.getSerializableExtra("placeNameList");
                }
            case 2:
                if (resultCode == RESULT_OK){
                    pagePosition = data.getIntExtra("position",0);
                    getPlaceNameListFromShared();
                }
        }
        judgeWeatherInformation();
        pagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(pagePosition);
        initToolbarInformation(pagePosition);
    }

    /**
     * 设置下拉刷新监听
     */
    private void setSwipeRefreshLayoutListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable()){
                    updateCurrentPositionWeather(pagePosition);
                }else {
                    Toast.makeText(MainActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    /**
     * 设置viewPager适配器
     */
    private void setUpPagerAdapter(){
        pagerAdapter = new MainPagerAdapter(MainActivity.this,weatherList);
        viewPager.setAdapter(pagerAdapter);
        initToolbarInformation(pagePosition);
        //viewPager滚动监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (weatherList.size() > 1){
                    if (positionOffsetPixels == 0){
                        setPageButtonState(pagePosition);
                    }else {
                        lastPage.setVisibility(View.INVISIBLE);
                        nextPage.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void onPageSelected(int position) {
                pagePosition = position;
                initToolbarInformation(pagePosition);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 根据当前的页面位置，刷新当前页的天气信息
     * @return
     */
    private void updateCurrentPositionWeather(final int position){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
        Utility.requestWeather(placeNameList.get(position),MainActivity.this);
        Weather weather;
        while (true){
            if (Utility.isRequestDoneOrNot()){
                weather = Utility.getWeather();
                break;
            }
        }
        weatherList.remove(position);
        weatherList.add(position,weather);
        pagerAdapter.notifyDataSetChanged();
        initToolbarInformation(position);
    }

    /**
     * 页面按钮的显示和隐藏
     */
    private void setPageButtonState(int pagePosition){
        if (pagePosition == 0){
            lastPage.setVisibility(View.INVISIBLE);
            nextPage.setVisibility(View.VISIBLE);
        }else if (pagePosition == weatherList.size() - 1){
            lastPage.setVisibility(View.VISIBLE);
            nextPage.setVisibility(View.INVISIBLE);
        }else {
            lastPage.setVisibility(View.VISIBLE);
            nextPage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 判断PlaceNameList中的内容
     */
    private void judgeListInformation(){
//        MainActivity.location = "深圳";
        getPlaceNameListFromShared();
        if (placeNameList.size() == 0) {
            if (location != null){
                placeNameList.add(0,location);
                judgeWeatherInformation();
                setUpPagerAdapter();
            }else {
                return;
            }
        }else {
            if (location != null){
                if (!placeNameList.contains(location)){
                    placeNameList.add(0,location);
                }
            }
            judgeWeatherInformation();
            setUpPagerAdapter();
        }
        setPlaceNameListToShared();
        progressBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 保存城市名列表
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
     * 获取城市名列表
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

    /**
     * 判断是否有天气缓存，有就直接解析并添加到列表中，没有就去查询
     */
    private void judgeWeatherInformation(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        weatherList.clear();
        for (String placeName : placeNameList) {
            String weatherString = preferences.getString(placeName,null);
            if (weatherString != null){
                Weather weather = Utility.handleWeatherResponse(weatherString);
                if (!weatherList.contains(weather)){
                    weatherList.add(weather);
                }
            }else {
                Utility.requestWeather(placeName,MainActivity.this);
                while (true) {
                    if (Utility.getWeather() != null && !weatherList.contains(Utility.getWeather())) {
                        weatherList.add(Utility.getWeather());
                        break;
                    }
                }
            }
        }
    }

    /**
     * 分享文字内容
     */
    private void shareWeatherText(String title, String subject, String content){
        if (content == null || "".equals(content)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if (subject != null && !"".equals(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        intent.putExtra(Intent.EXTRA_TEXT, content);
        // 设置弹出框标题
        if (title != null && !"".equals(title)) { // 自定义标题
            startActivity(Intent.createChooser(intent, title));
        } else { // 系统默认标题
            startActivity(intent);
        }
    }

    /**
     * 根据viewPager所在的页面更新toolbar上的内容和背景
     * @param position
     */
    private void initToolbarInformation(int position){
        String title = weatherList.get(position).getBasic().getLocation();
        String updateTime = weatherList.get(position).getUpdate().getLoc();
        String date = updateTime.substring(0,10);
        String time = updateTime.substring(11)+"更新";
        String week = Utility.getWeek(date)+",";
        String weatherId = weatherList.get(position).getNow().getCondCode();
        toolbarWeek.setText(week);
        toolbarTime.setText(time);
        toolbarTitle.setText(title);
        backgroundImage.setBackgroundResource(ReadyIconAndBackground.getWeatherBackground(weatherId));
    }

    /**
     * 其他按钮点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.last_page_button:
                viewPager.setCurrentItem(pagePosition - 1);
            case R.id.next_page_button:
                viewPager.setCurrentItem(pagePosition + 1);
        }
    }

    /**
     * 获取当前页面位置
     */
    public int getPagePosition(){
        return this.pagePosition;
    }

    /**
     * 获取百度定位城市并保存
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //因为定位的市是“XX区，所以要截取前面的内容
            String locationCounty = location.getDistrict();
            String locationCity = location.getCity();
            String locationForUse;
            if (locationCounty.length() > 2){
                if (locationCounty.contains("县")){
                    locationForUse = locationCounty.substring(0,locationCounty.indexOf("县"));
                } else {
                    locationForUse = locationCity.substring(0,locationCity.indexOf("市"));
                }
            }else {
                locationForUse = locationCounty;
            }
            MainActivity.location = locationForUse;
            MainActivity.location = "深圳";
            judgeListInformation();
            initToolbarInformation(0);
        }
    }

    /**
     * 判断权限
     */
    private void judgePermission(){
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,1);
        }else {
            checkSystemNetworkState();
        }
    }

    /**
     * 初始化toolbar及其点击事件
     */
    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.settings:
                        Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.search:
                        Intent intent1 = new Intent(MainActivity.this,SearchActivity.class);
                        startActivityForResult(intent1,2);
                        break;
                    case R.id.places:
                        Intent intent2 = new Intent(MainActivity.this,PlacesActivity.class);
                        startActivityForResult(intent2,1);
                        break;
                    case R.id.share:
                        Weather weather = weatherList.get(pagePosition);
                        String shareTitle = "分享";
                        String shareSubject = "";
                        String shareContent = weather.getBasic().getLocation()
                                + "天气：" + "\n"
                                + weather.getNow().getCondTxt() + "\n"
                                + weather.getLifestyleList().get(0).getTxt() + "\n"
                                + weather.getLifestyleList().get(1).getTxt() + "\n"
                                + weather.getLifestyleList().get(2).getTxt();
                        shareWeatherText(shareTitle,shareSubject,shareContent);
                    default:
                }
                return true;
            }
        });
    }

    /**
     * 创建toolbar的menu菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    /**
     * 判断权限获取结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    checkSystemNetworkState();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    /**
     * 检查当前网络状态
     */
    private void checkSystemNetworkState(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()){
            requestLocation();
        }else {
            Toast.makeText(this,"请检查网络",Toast.LENGTH_SHORT).show();
        }
        judgeListInformation();
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
     * 创建通知渠道
     */
    private void setNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "weatherNotification";
            String channelName = "天气推送";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId,channelName,importance);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId,channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        locationClient.stop();
    }
}
