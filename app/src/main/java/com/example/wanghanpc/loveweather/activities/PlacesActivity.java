package com.example.wanghanpc.loveweather.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wanghanpc.loveweather.tools.ReadyIconAndBackground;
import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.adapters.PlaceItemTouchCallback;
import com.example.wanghanpc.loveweather.adapters.PlacesAdapter;
import com.example.wanghanpc.loveweather.gson.cityGson.City;
import com.example.wanghanpc.loveweather.tools.ParseData;
import com.example.wanghanpc.loveweather.gson.weatherGson.Weather;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlacesActivity extends BaseActivity implements PlacesAdapter.OnItemClickListener,PlaceItemTouchCallback.ItemTouchAdapterInterface {

    private static final int PLACE_MODE_CHECK = 0;  //点击回到主页
    private static final int PLACE_MODE_EDIT = 1;  //编辑状态
    private static final int PLACE_MODE_EDIT_CLICK = 2;  //编辑状态被点击
    private static final int PLACE_MODE_BACK_TO_CHECK = 3;  //退出编辑

    private List<String> needToDelete = new ArrayList<>();
    private PlacesAdapter placesAdapter;
    private PlaceItemTouchCallback placeItemTouchCallback;
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

        getCityListFromDatabase();
        getWeatherListFromShared();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.place_item_recyclerView);
        placesAdapter = new PlacesAdapter(weatherList,placeNameList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(placesAdapter);
        ImageView placeBackground = (ImageView) findViewById(R.id.place_background);
        placeBackground.setImageResource(getBackground());
        deleteTextButton = (TextView) findViewById(R.id.delete_text_button);
        placeToolbarTitle = (TextView) findViewById(R.id.place_toolbar_title);
        actionButton = (FloatingActionButton) findViewById(R.id.place_add_button);

        placeItemTouchCallback = new PlaceItemTouchCallback(placesAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(placeItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!editStatus){
                    if (dy > 0){
                        actionButton.hide();
                    }else {
                        actionButton.show();
                    }
                }
            }
        });
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
            getCityListFromDatabase();
            getWeatherListFromShared();
            editMode = PLACE_MODE_CHECK;
            placesAdapter.setEditMode(editMode);
        }
        hasBeenOnRestart = false;
    }

    /**
     * 从SharedPreference中获取天气信息
     */
    private void getWeatherListFromShared(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        for (City city : placeNameList) {
            String cityId = city.getCityId();
            String weatherString = preferences.getString(cityId, null);
            if (weatherString != null) {
                Weather weather = ParseData.handleWeatherResponse(weatherString);
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
                        startToEdit();
                        break;
                    case R.id.settings:
                        Intent intent1 = new Intent(PlacesActivity.this,SettingActivity.class);
                        startActivity(intent1);
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
     * 进入编辑模式
     */
    private void startToEdit(){
        editStatus = true;
        initNavigationIcon(editStatus,allSelected);
        placeToolbarTitle.setText("选择位置");
        invalidateOptionsMenu();//刷新toolbar的menu按钮
        editMode = PLACE_MODE_EDIT;
        actionButton.hide();
        placesAdapter.setEditMode(editMode);
        placeItemTouchCallback.setEditMode(editMode);
        setDeleteButtonVisible(index);
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
                placesAdapter.getWeatherList().get(i).setSelected(true);
            }
            allSelected = true;
            index = placesAdapter.getWeatherList().size();
            placeToolbarTitle.setText(String.valueOf(index));
        }else {
            for (int i = 0; i < placesAdapter.getWeatherList().size(); i ++){
                placesAdapter.getWeatherList().get(i).setSelected(false);
            }
            index = 0;
            placeToolbarTitle.setText("选择位置");
            allSelected = false;
        }
        initNavigationIcon(editStatus,allSelected);
        setDeleteButtonVisible(index);
        placesAdapter.setEditMode(PLACE_MODE_EDIT_CLICK);
    }

    /**
     * 删除条目
     */
    private void deletePlace(){
        needToDelete.clear();
        for (int i = placesAdapter.getWeatherList().size(); i > 0; i --){
            Weather weather = placesAdapter.getWeatherList().get(i-1);
            if (weather.getSelected()){
                placesAdapter.getWeatherList().remove(weather);
                needToDelete.add(weather.getBasic().getCityId());
            }
        }
        int size = needToDelete.size();
        for (int i = 0; i < size; i++){
            for (Iterator iterator = placeNameList.iterator(); iterator.hasNext();){
                City city = (City)iterator.next();
                if (city.getCityId().equals(needToDelete.get(i))){
                    iterator.remove();
                }
            }
        }
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
            boolean isSelect = weather.getSelected();
            if (!isSelect){
                index ++;
                weather.setSelected(true);
                if (index == weatherList.size()){
                    allSelected = true;
                    toolbar.setNavigationIcon(R.mipmap.selected_blue);
                }
            }else {
                weather.setSelected(false);
                index --;
                allSelected = false;
                toolbar.setNavigationIcon(R.mipmap.unselected);
            }
            setDeleteButtonVisible(index);
            if (index == 0){
                placeToolbarTitle.setText("选择位置");
            }else {
                placeToolbarTitle.setText(String.valueOf(index));
            }
            placesAdapter.setEditMode(PLACE_MODE_EDIT_CLICK);
        }else {
            //返回天气详情页面
            Intent intent = new Intent();
            intent.putExtra("position",position);
            intent.putExtra("placeNameList",(Serializable)placeNameList);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    @Override
    public void onItemLongClickListener(int position, List<Weather> weatherList) {
        if (!editStatus){
            startToEdit();
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
        intent.putExtra("placeNameList",(Serializable)placeNameList);
        setResult(RESULT_OK,intent);
    }

    /**
     * 退出编辑模式
     */
    private void exitEditMode(){
        for (int i = 0; i < placesAdapter.getWeatherList().size(); i ++){
            placesAdapter.getWeatherList().get(i).setSelected(false);
        }
        index = 0;
        editStatus = false;
        allSelected = false;
        placeToolbarTitle.setText("地点");
        initNavigationIcon(editStatus,allSelected);
        invalidateOptionsMenu();
        setDeleteButtonVisible(index);

        editMode = PLACE_MODE_BACK_TO_CHECK;
        actionButton.show();
        placesAdapter.setEditMode(editMode);
        placeItemTouchCallback.setEditMode(editMode);
        this.placeNameList = placesAdapter.getPlaceNameList();
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onSwiped(int position) {
        placeNameList.remove(position);
        weatherList.remove(position);
    }

    @Override
    public void onClearView(int position) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveCityListToDatabase(placeNameList);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 获取背景资源
     * @return
     */
    private int getBackground(){
        Weather weather = weatherList.get(0);
        return ReadyIconAndBackground.getWeatherBackground(weather.getNow().getCondCode());
    }
}
