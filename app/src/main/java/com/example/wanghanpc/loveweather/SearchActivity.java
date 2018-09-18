package com.example.wanghanpc.loveweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wanghanpc.loveweather.cityGson.City;
import com.example.wanghanpc.loveweather.cityGson.CityBackResult;
import com.example.wanghanpc.loveweather.util.HttpUtil;
import com.example.wanghanpc.loveweather.util.Utility;
import com.example.wanghanpc.loveweather.weatherGson.Weather;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editText;
    private static CityBackResult cityBackResult;
    private ListView listView;
    private List<String> cityNameList = new ArrayList<>();
    private List<String> placeNameList = new ArrayList<>();
    private List<Weather> weatherList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String cityText;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getPlaceNameListFromShared();

        listView = (ListView) findViewById(R.id.search_listView);
        editText = (EditText) findViewById(R.id.search_edit_text);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        adapter = new ArrayAdapter<>(SearchActivity.this,android.R.layout.simple_list_item_1,cityNameList);
        listView.setAdapter(adapter);
        prepareListViewListener();
        prepareEditTextListener();
        initToolbar();
        progressBar.setVisibility(View.GONE);
    }

    /**
     *ListView点击事件
     */
    private void prepareListViewListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = cityNameList.get(position);
                prepareNewCityInformation(selectedCity);
            }
        });
    }

    /**
     * 查询新增城市的天气数据
     */
    private void prepareNewCityInformation(String city){
        if (placeNameList.contains(city)){
            Toast.makeText(SearchActivity.this,"已注册",Toast.LENGTH_SHORT).show();
        }else {
            progressBar.setVisibility(View.VISIBLE);
            placeNameList.add(city);
            setPlaceNameListToShared();
            Utility.requestWeather(city,this);
            while (true){
                if (Utility.getWeather() != null && Utility.getWeather().result.today.city.equals(city)){
                    break;
                }
            }
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent();
            intent.putExtra("position",placeNameList.size() - 1);
            setResult(RESULT_OK,intent);
            finish();
        }
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
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 请求城市搜索数据
     */
    private void requestCityList(final String searchName){
        String cityUrl = "https://search.heweather.com/find?&location=" + searchName + "&key=e7b4b21007f048a9a4fe2cb236ce5569" + "&group=cn";
        HttpUtil.sendOkHttpRequest(cityUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this,"获取城市失败1",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final CityBackResult cityBackResult = Utility.handleCityResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (cityBackResult != null && "ok".equals(cityBackResult.status)){
                            SearchActivity.cityBackResult = cityBackResult;
                            showCityList();
                        }else {
                            Toast.makeText(SearchActivity.this,"获取城市失败2",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 加载并显示城市列表
     */
    private void showCityList(){
        cityNameList.clear();
        if (cityBackResult.cityList != null && cityBackResult.cityList.size() != 0) {
            for (City city : cityBackResult.cityList) {
                cityNameList.add(city.locationName);
            }
        }
        adapter.notifyDataSetChanged();
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
                    case R.id.settings:
                        Intent intent = new Intent(SearchActivity.this,SettingActivity.class);
                        startActivity(intent);
                        break;
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
                finish();
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
        getMenuInflater().inflate(R.menu.toolbar_search,menu);
        return true;
    }
}
