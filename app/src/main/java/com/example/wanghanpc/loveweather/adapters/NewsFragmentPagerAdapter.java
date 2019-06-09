package com.example.wanghanpc.loveweather.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.wanghanpc.loveweather.fragments.NewsFragment;

import java.util.List;

public class NewsFragmentPagerAdapter extends FragmentStatePagerAdapter{

    private List<String> titleList;
    private List<NewsFragment> fragments;

    public NewsFragmentPagerAdapter(FragmentManager fm, List<NewsFragment> fragments, List<String> titleList) {
        super(fm);
        this.fragments = fragments;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * 设置每个tab的标题
     * @param position
     * @return
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
