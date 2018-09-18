package com.example.wanghanpc.loveweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.wanghanpc.loveweather.util.Utility;
import com.example.wanghanpc.loveweather.weatherGson.Weather;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlacesActivity extends AppCompatActivity implements PlacesAdapter.OnItemClickListener {

    private static final int PLACE_MODE_CHECK = 0;
    private static final int PLACE_MODE_EDIT = 1;

    private List<Weather> weatherList = new ArrayList<>();
    private List<String> placeNameList = new ArrayList<>();
    private List<String> needToDelete = new ArrayList<>();
    private RecyclerView recyclerView;
    private PlacesAdapter placesAdapter;
    private LinearLayoutManager layoutManager;
    private Toolbar toolbar;
    private TextView deleteTextButton;
    private TextView placeToolbarTitle;
    private FloatingActionButton actionButton;
    private boolean allSelected = false;
    private boolean editStatus = false;
    private int editMode = PLACE_MODE_CHECK;
    private int index = 0;
    private boolean hasBeenOnRestart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);

        getPlaceNameListFromShared();
        getWeatherListFromShared();
        recyclerView = (RecyclerView) findViewById(R.id.place_item_recyclerView);
        placesAdapter = new PlacesAdapter(weatherList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PlacesItemDecoration itemDecoration = new PlacesItemDecoration(this,PlacesItemDecoration.VERTICAL_LIST);
        itemDecoration.setPlaceDrawable(ContextCompat.getDrawable(this,R.drawable.place_main_bg_heigth_1));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(placesAdapter);
        deleteTextButton = (TextView) findViewById(R.id.delete_text_button);
        placeToolbarTitle = (TextView) findViewById(R.id.place_toolbar_title);
        actionButton = (FloatingActionButton) findViewById(R.id.place_add_button);

        initToolbar();

        //删除按钮点击事件
        deleteTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePlace();
            }
        });
        //添加按钮点击事件
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlacesActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        placesAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        hasBeenOnRestart = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hasBeenOnRestart) {
            getPlaceNameListFromShared();
            getWeatherListFromShared();
            placesAdapter.notifyDataSetChanged();
        }
        hasBeenOnRestart = false;
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

    /**
     * 从SharedPreference中获取天气信息
     */
    private void getWeatherListFromShared(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        for (String placeName : placeNameList) {
            String weatherString = preferences.getString(placeName, null);
            if (weatherString != null) {
                Weather weather = Utility.handleWeatherResponse(weatherString);
                if (!weatherList.contains(weather)){
                    weatherList.add(weather);
                }
            }
        }
    }

    /**
     * 初始化toolbar及其点击事件
     */
    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.places_toolbar);
        toolbar.inflateMenu(R.menu.toolbar_places);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.edit:
                        //编辑功能
                        editStatus = true;
                        initNavigationIcon(editStatus,allSelected);
                        placeToolbarTitle.setText(String.valueOf(index));
                        invalidateOptionsMenu();//刷新toolbar的menu按钮
                        editMode = PLACE_MODE_EDIT;
                        placesAdapter.setEditMode(editMode);
                        setDeleteButtonVisible(index);
                        break;
                    case R.id.settings:
                        Intent intent1 = new Intent(PlacesActivity.this,SettingActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.share:
                        //分享功能

                        break;
                    default:
                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editStatus){
                    selectAllPlaces();
                }else {
                    setInformationForMain();
                    finish();
                }
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
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_places,menu);
        if (editStatus){
            menu.setGroupVisible(0,false);
        }else {
            menu.setGroupVisible(0,true);
        }
        return true;
    }

    /**
     * 根据选择的数量是否为0，来判断“删除”按钮是否可见
     */
    private void setDeleteButtonVisible(int number){
        if (number != 0){
            deleteTextButton.setVisibility(View.VISIBLE);
        }else {
            deleteTextButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 根据是否为编辑模式来选择toolbar的Navigation图标
     */
    private void initNavigationIcon(boolean editStatus, boolean allSelected){
        if (editStatus){
            if (allSelected){
                toolbar.setNavigationIcon(R.mipmap.selected_blue);
            }else {
                toolbar.setNavigationIcon(R.mipmap.unselected);
            }
        }else {
            toolbar.setNavigationIcon(R.mipmap.back);
        }
    }

    /**
     * 全选和反选
     */
    private void selectAllPlaces(){
        if (placesAdapter == null) return;
        if (!allSelected){
            for (int i = 0; i < placesAdapter.getWeatherList().size(); i ++){
                placesAdapter.getWeatherList().get(i).isSelected = true;
            }
            allSelected = true;
            index = placesAdapter.getWeatherList().size();
            placeToolbarTitle.setText(String.valueOf(index));
        }else {
            for (int i = 0; i < placesAdapter.getWeatherList().size(); i ++){
                placesAdapter.getWeatherList().get(i).isSelected = false;
            }
            index = 0;
            placeToolbarTitle.setText(String.valueOf(index));
            allSelected = false;
        }
        initNavigationIcon(editStatus,allSelected);
        setDeleteButtonVisible(index);
        placesAdapter.notifyDataSetChanged();
    }

    /**
     * 删除条目
     */
    private void deletePlace(){
        needToDelete.clear();
        for (int i = placesAdapter.getWeatherList().size(); i > 0; i --){
            Weather weather = placesAdapter.getWeatherList().get(i-1);
            if (weather.isSelected){
                placesAdapter.getWeatherList().remove(weather);
                needToDelete.add(weather.result.today.city);
            }
        }
        for (String city : needToDelete){
            placeNameList.remove(city);
        }
        setPlaceNameListToShared();
        exitEditMode();
    }

    /**
     * 条目点击事件
     * @param position
     * @param weatherList
     */
    @Override
    public void onItemClickListener(int position, List<Weather> weatherList){
        if (editStatus){
            Weather weather = weatherList.get(position);
            boolean isSelect = weather.isSelected;
            if (!isSelect){
                index ++;
                weather.isSelected = true;
                if (index == weatherList.size()){
                    allSelected = true;
                    toolbar.setNavigationIcon(R.mipmap.selected_blue);
                }
            }else {
                weather.isSelected = false;
                index --;
                allSelected = false;
                toolbar.setNavigationIcon(R.mipmap.unselected);
            }
            setDeleteButtonVisible(index);
            placeToolbarTitle.setText(String.valueOf(index));
            placesAdapter.notifyDataSetChanged();
        }else {
            //返回天气详情页面
            Intent intent = new Intent();
            intent.putExtra("position",position);
            intent.putExtra("placeNameList",(Serializable)placeNameList);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    /**
     * 返回键点击事件
     */
    @Override
    public void onBackPressed(){
        if (editStatus){
            exitEditMode();
        }else {
            setInformationForMain();
            finish();
        }
    }

    /**
     * 装载需要返回给MainActivity的数据
     */
    private void setInformationForMain(){
        Intent intent = new Intent();
//        intent.putExtra("position",0);
        intent.putExtra("placeNameList",(Serializable)placeNameList);
        setResult(RESULT_OK,intent);
    }

    /**
     * 退出编辑模式
     */
    private void exitEditMode(){
        index = 0;
        editStatus = false;
        allSelected = false;
        placeToolbarTitle.setText("地点");
        initNavigationIcon(editStatus,allSelected);
        invalidateOptionsMenu();
        setDeleteButtonVisible(index);
        editMode = PLACE_MODE_CHECK;
        placesAdapter.setEditMode(editMode);
    }
}
