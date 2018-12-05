package com.example.wanghanpc.loveweather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.activities.NewsContentActivity;
import com.example.wanghanpc.loveweather.adapters.NewsItemAdapter;
import com.example.wanghanpc.loveweather.gson.newsGson.DataItem;
import com.example.wanghanpc.loveweather.gson.newsGson.News;
import com.example.wanghanpc.loveweather.tools.SendOkHttp;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsFragment extends Fragment implements NewsItemAdapter.OnItemClickListener {

    private String[] pageTitles = {"top","shehui","guonei","guoji","yule","tiyu","junshi","keji","caijing","shishang"};
    private boolean isFirstLoad = false;
    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String URL_DATA = "URL_DATA";
    private int pagePosition;
    private String pageTitle;
    private RecyclerView recyclerView;
    private List<DataItem> dataItemList;
    private ControlRefresh controlRefresh;

    public static NewsFragment newInstance(int page){
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE,page);
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlRefresh = (ControlRefresh)getActivity();
        pagePosition = getArguments().getInt(ARG_PAGE);
        Log.d("NewsFragment","pagePosition------------------------"+String.valueOf(pagePosition));
        pageTitle = pageTitles[pagePosition];
        Log.d("NewsFragment","pageTitle----------------------"+pageTitle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.news_fragment_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        isFirstLoad = true;
        if (getUserVisibleHint()){
            //加载数据
            updateData();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isFirstLoad && isVisibleToUser){
            //加载数据
            updateData();
        }
    }

    @Override
    public void onItemClickListener(int position) {
        String newsUrl = dataItemList.get(position).getUrl();
        Intent intent = new Intent(getActivity(), NewsContentActivity.class);
        intent.putExtra(URL_DATA,newsUrl);
        startActivity(intent);
    }

    public void updateData(){
        controlRefresh.startRefresh();
        requestNews(pageTitle);
        isFirstLoad = false;
    }

    private void initView(){
        NewsItemAdapter itemAdapter = new NewsItemAdapter(getActivity(),dataItemList);
        recyclerView.setAdapter(itemAdapter);
        controlRefresh.stopRefresh();
        itemAdapter.setOnItemClickListener(this);
    }

    private void requestNews(String type){
        Log.d("NewsActivity","type------------------------"+type);
        String newsUrl = "http://v.juhe.cn/toutiao/index?type="+type+"&key=bdcab1dfba80f8a9707c4ac849ed56c4";
        SendOkHttp.sendOkHttpRequest(newsUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"加载失败01",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final News news = new Gson().fromJson(responseText,News.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (news != null && news.getReason().equals("成功的返回")){
                            dataItemList = news.getResult().getData();
                            initView();
                        }else {
                            Toast.makeText(getActivity(),"加载失败02",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public interface ControlRefresh{
        void startRefresh();
        void stopRefresh();
    }
}
