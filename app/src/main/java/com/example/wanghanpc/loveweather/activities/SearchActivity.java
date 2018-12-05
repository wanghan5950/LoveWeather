package com.example.wanghanpc.loveweather.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.gson.cityGson.City;
import com.example.wanghanpc.loveweather.gson.cityGson.CityBackResult;
import com.example.wanghanpc.loveweather.gson.cityGson.HotCityBackResult;
import com.example.wanghanpc.loveweather.tools.SendOkHttp;
import com.example.wanghanpc.loveweather.tools.ParseData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends BaseActivity implements View.OnClickListener{

    private EditText editText;
    private static CityBackResult cityBackResult;
    private ListView listView;
    private List<String> cityNameList = new ArrayList<>();
    private List<City> hotCitiesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String cityText;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private List<TextView> textViewList = new ArrayList<>();
    private boolean hasBeenOnRestart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getCityListFromDatabase();
        prepareAllTextView();

        linearLayout = (LinearLayout) findViewById(R.id.allTextVIew_layout);
        listView = (ListView) findViewById(R.id.search_listView);
        editText = (EditText) findViewById(R.id.search_edit_text);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        adapter = new ArrayAdapter<>(SearchActivity.this,android.R.layout.simple_list_item_1,cityNameList);
        listView.setAdapter(adapter);
        prepareListViewListener();
        prepareEditTextListener();
        initToolbar();
        getHotCityList();
        showHotCities();
        progressBar.setVisibility(View.GONE);
        prepareTextViewListener();
        for (TextView textView : textViewList){
            textView.setOnClickListener(this);
        }
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
            placeNameList.clear();
            getCityListFromDatabase();
        }
        hasBeenOnRestart = false;
    }

    /**
     *ListView点击事件
     */
    private void prepareListViewListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity = cityBackResult.getCityList().get(position);
                prepareNewCityInformation(selectedCity);
            }
        });
    }

    /**
     * 查询新增城市的天气数据
     */
    private void prepareNewCityInformation(City city){
        if (placeNameList.contains(city)){
            Toast.makeText(SearchActivity.this,"已注册",Toast.LENGTH_SHORT).show();
        }else {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()){
                progressBar.setVisibility(View.VISIBLE);
                placeNameList.add(city);
                requestWeather(city.getCityId());
            }else {
                Toast.makeText(SearchActivity.this,"请检查网络",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void updateWeatherUI() {
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent();
        intent.putExtra("position",placeNameList.size() - 1);
        setResult(RESULT_OK,intent);
        finish();
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
                if (!cityText.equals("")) {
                    String cityTextHead = cityText.substring(0,1);
                    Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");//判断输入的内容是否为汉字
                    Matcher matcher = pattern.matcher(cityTextHead);
                    if (matcher.matches()){
                        requestCityList(cityText);
                    }
                    linearLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }else {
                    linearLayout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void updateCity(CityBackResult result) {
        SearchActivity.cityBackResult = result;
        showCityList();
    }

    /**
     * 请求热门城市搜索数据
     */
    private void requestHotCity(){
        String hotCityUrl = "https://search.heweather.com/top?&group=cn&key=e7b4b21007f048a9a4fe2cb236ce5569&number=18";
        SendOkHttp.sendOkHttpRequest(hotCityUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this,"获取热门城市失败1",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final HotCityBackResult hotCityBackResult = ParseData.handleHotCityResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (hotCityBackResult != null && "ok".equals(hotCityBackResult.getStatus())){
                            hotCitiesList = hotCityBackResult.getHotCityList();
                            setHotCityList(hotCitiesList);
                            showHotCities();
                        }else {
                            Toast.makeText(SearchActivity.this,"获取热门城市失败2",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 保存热门城市列表
     */
    private void setHotCityList(List<City> hotCityList){
        databaseModel.saveHotToDatabase(hotCityList);
    }

    /**
     * 获取缓存的热门城市列表
     */
    private void getHotCityList(){
        hotCitiesList = databaseModel.getHotFromDatabase();
    }

    /**
     * 加载并显示城市列表
     */
    private void showCityList(){
        cityNameList.clear();
        if (cityBackResult.getCityList() != null && cityBackResult.getCityList().size() != 0) {
            int size = cityBackResult.getCityList().size();
            List<City> list = cityBackResult.getCityList();
            for (int i = 0; i < size; i++)
                cityNameList.add(list.get(i).getLocationName());
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 显示热门城市列表
     */
    private void showHotCities(){
        if (hotCitiesList.size() != 0){
            for (int i = 0; i < textViewList.size(); i++){
                textViewList.get(i).setText(hotCitiesList.get(i).getLocationName());
            }
        }else {
            requestHotCity();
        }
    }

    /**
     * 初始化toolbar及其点击事件
     */
    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        toolbar.inflateMenu(R.menu.toolbar_search);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.map:
                        Intent intent1 = new Intent(SearchActivity.this,MapActivity.class);
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
                Intent intent = new Intent();
                intent.putExtra("position",0);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position",0);
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }

    /**
     * 创建toolbar的menu菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_search,menu);
        return true;
    }

    /**
     * 加载TextView布局
     */
    private void prepareAllTextView(){
        TextView item1 = (TextView) findViewById(R.id.hotCity_item_1);
        TextView item2 = (TextView) findViewById(R.id.hotCity_item_2);
        TextView item3 = (TextView) findViewById(R.id.hotCity_item_3);
        TextView item4 = (TextView) findViewById(R.id.hotCity_item_4);
        TextView item5 = (TextView) findViewById(R.id.hotCity_item_5);
        TextView item6 = (TextView) findViewById(R.id.hotCity_item_6);
        TextView item7 = (TextView) findViewById(R.id.hotCity_item_7);
        TextView item8 = (TextView) findViewById(R.id.hotCity_item_8);
        TextView item9 = (TextView) findViewById(R.id.hotCity_item_9);
        TextView item10 = (TextView) findViewById(R.id.hotCity_item_10);
        TextView item11 = (TextView) findViewById(R.id.hotCity_item_11);
        TextView item12 = (TextView) findViewById(R.id.hotCity_item_12);
        TextView item13 = (TextView) findViewById(R.id.hotCity_item_13);
        TextView item14 = (TextView) findViewById(R.id.hotCity_item_14);
        TextView item15 = (TextView) findViewById(R.id.hotCity_item_15);
        TextView item16 = (TextView) findViewById(R.id.hotCity_item_16);
        TextView item17 = (TextView) findViewById(R.id.hotCity_item_17);
        TextView item18 = (TextView) findViewById(R.id.hotCity_item_18);
        textViewList.add(item1);
        textViewList.add(item2);
        textViewList.add(item3);
        textViewList.add(item4);
        textViewList.add(item5);
        textViewList.add(item6);
        textViewList.add(item7);
        textViewList.add(item8);
        textViewList.add(item9);
        textViewList.add(item10);
        textViewList.add(item11);
        textViewList.add(item12);
        textViewList.add(item13);
        textViewList.add(item14);
        textViewList.add(item15);
        textViewList.add(item16);
        textViewList.add(item17);
        textViewList.add(item18);
    }

    private void prepareTextViewListener(){
        for (TextView textView : textViewList){
            textView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < textViewList.size(); i++){
            if (textViewList.get(i).equals(v)){
                prepareNewCityInformation(hotCitiesList.get(i));
            }
        }
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
}
