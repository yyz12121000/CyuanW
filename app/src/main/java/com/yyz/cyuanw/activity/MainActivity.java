package com.yyz.cyuanw.activity;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.yyz.cyuanw.activity.fragment.CyFragment;
import com.yyz.cyuanw.activity.fragment.GxFragment;
import com.yyz.cyuanw.activity.fragment.LmFragment;
import com.yyz.cyuanw.adapter.MyFragPagerAdapter;
import com.yyz.cyuanw.R;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {
    private TabLayout mTab;
    private MyFragPagerAdapter mAdapter;
    private List<android.support.v4.app.Fragment> mFragments;
    private List<String> titleList;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 实例化控件
     */
    private void initView() {
        mViewPager = findViewById(R.id.main_viewpager);
        //设置ViewPager里面也要显示的图片
        mFragments = new ArrayList<>();

        Fragment gxFragment = new GxFragment();
        Fragment cyFragment = new CyFragment();
        Fragment lmFragment = new LmFragment();

        mFragments.add(gxFragment);
        mFragments.add(cyFragment);
        mFragments.add(lmFragment);

        //设置标题
        titleList = new ArrayList<>();
        titleList.add("共享");
        titleList.add("车源");
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
        mAdapter = new MyFragPagerAdapter(getSupportFragmentManager(),mFragments,titleList);
        //给ViewPager绑定Adapter
        mViewPager.setAdapter(mAdapter);
    }

}
