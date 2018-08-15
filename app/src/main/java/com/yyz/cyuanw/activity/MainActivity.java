package com.yyz.cyuanw.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.activity.fragment.CyFragment;
import com.yyz.cyuanw.activity.fragment.SyFragment;
import com.yyz.cyuanw.activity.fragment.LmFragment;
import com.yyz.cyuanw.activity.user_model.LoginActivity;
import com.yyz.cyuanw.activity.user_model.NameConfirmActivity;
import com.yyz.cyuanw.activity.user_model.UserActivity;
import com.yyz.cyuanw.adapter.MyFragPagerAdapter;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.ImageTools;
import com.yyz.cyuanw.tools.StringUtil;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private TabLayout mTab;
    private MyFragPagerAdapter mAdapter;
    private List<android.support.v4.app.Fragment> mFragments;
    private List<String> titleList;
    private ViewPager mViewPager;

    private TextView left_text;

    private Dialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (StringUtil.isNotNull(App.get(Constant.KEY_USER_ISLOGIN))) {
            showNoticeDialog();
            App.set(Constant.KEY_USER_ISLOGIN, "");
        }

    }

    /**
     * 实例化控件
     */
    @Override
    public void initView() {
        setSwipeBackEnable(false);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        left_text = (TextView) findViewById(R.id.left_text);
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

        left_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCity();
            }
        });
    }

    public void showNoticeDialog() {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(this, R.style.MyDialog).create();
        }
        mDialog.show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_loginsuc, null);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setContentView(dialogView);

        dialogView.findViewById(R.id.id_tv_cancel).setOnClickListener(view -> mDialog.dismiss());
        dialogView.findViewById(R.id.id_tv_go).setOnClickListener(view -> {
            mDialog.dismiss();

            startActivity(new Intent(MainActivity.this, NameConfirmActivity.class));
        });

    }

    public void userBtnOnclik(View view) {
        if (!StringUtil.isNotNull(App.get(Constant.KEY_USER_TOKEN))) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            startActivity(new Intent(this, UserActivity.class));
        }

    }

    private void showCity() {
        List<HotCity> hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));


        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())    //此方法必须调用
                .enableAnimation(true)    //启用动画效果
//                .setAnimationStyle(anim)    //自定义动画
                .setLocatedCity(new LocatedCity("杭州", "浙江", "101210101"))  //APP自身已定位的城市，默认为null（定位失败）
                .setHotCities(hotCities)    //指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        left_text.setText(data.getName());
                    }

                    @Override
                    public void onLocate() {
                        //开始定位，这里模拟一下定位
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //定位完成之后更新数据
                                CityPicker.getInstance()
                                        .locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
                            }
                        }, 2000);
                    }
                })
                .show();
    }

}
