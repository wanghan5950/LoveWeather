package com.example.wanghanpc.loveweather.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.adapters.NewsFragmentPagerAdapter;
import com.example.wanghanpc.loveweather.fragments.NewsFragment;
import com.example.wanghanpc.loveweather.myDefined.MySwipeRefreshLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements NewsFragment.ControlRefresh{

    private MySwipeRefreshLayout swipeRefreshLayout;
    private String[] titles = {"头条","社会","国内","国际","娱乐","体育","军事","科技","财经","时尚"};
    private List<String> titleList = new ArrayList<>();
    private List<NewsFragment> fragmentList = new ArrayList<>();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int pagePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        titleList.addAll(Arrays.asList(titles));

        ImageView backToMainButton = (ImageView) findViewById(R.id.backButton);
        swipeRefreshLayout = (MySwipeRefreshLayout) findViewById(R.id.news_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,R.color.colorPrimaryDark);

        viewPager = (ViewPager) findViewById(R.id.news_viewPager);
        tabLayout = (TabLayout) findViewById(R.id.news_tabLayout);

        for (int i = 0; i < titleList.size(); i++){
            fragmentList.add(NewsFragment.newInstance(i));
        }

        initTabLayout();
        initViewPager();
        setSwipeRefreshLayoutListener();

        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_next_enter,R.anim.activity_next_exit);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化tabLayout
     */
    private void initTabLayout(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 初始化viewPager
     */
    private void initViewPager(){
        NewsFragmentPagerAdapter fragmentPagerAdapter = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pagePosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

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
        getMenuInflater().inflate(R.menu.toolbar_news,menu);
        return true;
    }

    /**
     * 下拉刷新监听
     */
    private void setSwipeRefreshLayoutListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragmentList.get(pagePosition).updateData();
            }
        });
    }

    @Override
    public void startRefresh() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 返回键点击事件
     */
    @Override
    public void onBackPressed(){
        finish();
        overridePendingTransition(R.anim.activity_next_enter,R.anim.activity_next_exit);
    }
}
