package com.yyz.cyuanw.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yyz.cyuanw.activity.fragment.CyFragment;
import com.yyz.cyuanw.activity.fragment.SyFragment;
import com.yyz.cyuanw.activity.fragment.LmFragment;
import com.yyz.cyuanw.activity.user_model.LoginActivity;
import com.yyz.cyuanw.activity.user_model.UserActivity;
import com.yyz.cyuanw.adapter.MyFragPagerAdapter;
import com.yyz.cyuanw.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private TabLayout mTab;
    private MyFragPagerAdapter mAdapter;
    private List<android.support.v4.app.Fragment> mFragments;
    private List<String> titleList;
    private ViewPager mViewPager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }

    /**
     * 实例化控件
     */
    @Override
    public void initView() {
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        //设置ViewPager里面也要显示的图片
        mFragments = new ArrayList<>();


        Fragment cyFragment = new CyFragment();
        Fragment syFragment = new SyFragment();
        Fragment lmFragment = new LmFragment();


        mFragments.add(cyFragment);
        mFragments.add(syFragment);
        mFragments.add(lmFragment);

        //设置标题
        titleList = new ArrayList<>();

        titleList.add("车源");
        titleList.add("首页");
        titleList.add("联盟");

        mTab = (TabLayout) findViewById(R.id.main_tab);
//        //设置tab的模式
//        mTab.setTabMode(TabLayout.MODE_FIXED);不可滚动的tab
        //app:tabMode="scrollable"可以滑动的tab
        //添加tab选项卡
        for (int i = 0; i < titleList.size(); i++) {
            mTab.addTab(mTab.newTab().setText(titleList.get(i)));
        }
        //把TabLayout和ViewPager关联起来
        mTab.setupWithViewPager(mViewPager);
        //实例化adapter
        mAdapter = new MyFragPagerAdapter(getSupportFragmentManager(), mFragments, titleList);
        //给ViewPager绑定Adapter
        mViewPager.setAdapter(mAdapter);
    }

    public void userBtnOnclik(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

}
