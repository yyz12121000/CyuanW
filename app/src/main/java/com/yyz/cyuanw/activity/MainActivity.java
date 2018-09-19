package com.yyz.cyuanw.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.fragment.CyFragment;
import com.yyz.cyuanw.activity.fragment.LmFragment;
import com.yyz.cyuanw.activity.fragment.SyFragment;
import com.yyz.cyuanw.activity.user_model.LoginActivity;
import com.yyz.cyuanw.activity.user_model.NameConfirmActivity;
import com.yyz.cyuanw.activity.user_model.UserActivity;
import com.yyz.cyuanw.adapter.MyFragPagerAdapter;
import com.yyz.cyuanw.bean.LocationO;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.Location;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.Tools;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private TabLayout mTab;
    private MyFragPagerAdapter mAdapter;
    private List<android.support.v4.app.Fragment> mFragments;
    private List<String> titleList;
    private ViewPager mViewPager;
    private View search_ll;
    private TextView left_text;
    private EditText search_text;
    private ImageView right_icon;
    private int index = 0;

    private Dialog mDialog;
    private Location location;

    private LoginData userData;

    private long lastBackTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        lastBackTime = System.currentTimeMillis();
        location = new Location(this, new Location.ILocationListener() {
            @Override
            public void location(LocationO locationO) {
                if (null != locationO.city) {
                    left_text.setText(locationO.city);
                    SyFragment syFragment = (SyFragment) mFragments.get(0);
                    syFragment.loadJjr(locationO.longitude, locationO.latitude);
                }
            }
        });

        String jsonStr = App.get(Constant.KEY_USER_DATA);
        if (StringUtil.isNotNull(jsonStr)) {
            userData = new Gson().fromJson(jsonStr, LoginData.class);
            if (StringUtil.isNotNull(userData.pic))
                Img.loadC(right_icon, userData.pic);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        location.stop();
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
        search_ll = findViewById(R.id.search_ll);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        left_text = (TextView) findViewById(R.id.left_text);
        search_text = (EditText) findViewById(R.id.search_text);
        right_icon = (ImageView) findViewById(R.id.right_icon);
        //设置ViewPager里面也要显示的图片
        mFragments = new ArrayList<>();

        Fragment cyFragment = new CyFragment();
        Fragment syFragment = new SyFragment();
        Fragment lmFragment = new LmFragment();

        mFragments.add(syFragment);
        mFragments.add(cyFragment);
        mFragments.add(lmFragment);

        //设置标题
        titleList = new ArrayList<>();

        titleList.add("首页");
        titleList.add("车源");
        titleList.add("联盟");

        mTab = (TabLayout) findViewById(R.id.main_tab);
        mTab.setBackgroundResource(R.drawable.tab_backgroud);
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
        mViewPager.setOffscreenPageLimit(2);
        //给ViewPager绑定Adapter
        mViewPager.setAdapter(mAdapter);

        left_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChooseCityActivity.class);
                startActivityForResult(intent, 2);
            }
        });


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                index = i;
                switch (index) {
                    case 0:
                        search_ll.setVisibility(View.GONE);
                        search_text.setHint("搜索经纪人");
                        break;
                    case 1:
                        search_ll.setVisibility(View.VISIBLE);
                        search_text.setHint("搜索您需要的车源");
                        break;
                    case 2:
                        search_ll.setVisibility(View.VISIBLE);
                        search_text.setHint("搜索你感兴趣的联盟吧~");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    String text = search_text.getText().toString();

                    Tools.hideSoftInput(MainActivity.this, search_text);

                    switch (index) {
                        case 0:

                            break;
                        case 1:
                            ((CyFragment) mFragments.get(1)).doSearch(text);
                            break;
                        case 2:
                            ((LmFragment) mFragments.get(2)).doSearch(text);
                            break;
                    }
                }
                return false;
            }

        });

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 2:
                    int sheng_id = data.getIntExtra("sheng_id", 0);
                    int shi_id = data.getIntExtra("shi_id", 0);
                    String city = data.getStringExtra("city");
                    left_text.setText(city);
                    ((CyFragment) mFragments.get(1)).doSearchByAdress(sheng_id, shi_id);
                    break;
            }
        }
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

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            location.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long time = System.currentTimeMillis();
            if ((time - lastBackTime) > 2000) {
                App.showToast("再按一次退出应用程序");

                lastBackTime = time;
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}