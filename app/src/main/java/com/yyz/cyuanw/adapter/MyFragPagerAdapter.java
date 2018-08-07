package com.yyz.cyuanw.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ImageView;

import java.util.List;

public class MyFragPagerAdapter extends FragmentPagerAdapter {
    private List<ImageView> list;
    private List<String> titleList;

    private List<android.support.v4.app.Fragment> fragments;

    public MyFragPagerAdapter(FragmentManager fm, List<android.support.v4.app.Fragment> mFragments,List<String> titleList) {
        super(fm);
        fragments = mFragments;
        this.titleList = titleList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);//页卡标题
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
