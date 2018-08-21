package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.os.Handler;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.yyz.cyuanw.tools.Location;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ChooseCityActivity extends BaseActivity {
    private Location location;
    private String addressJson = "";

    @Override
    public void initView() {
        showCity();
    }

    @Override
    public void initData() {
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("address.txt"));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";

            while ((line = bufReader.readLine()) != null)
                addressJson += line;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        location.stop();
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
//                .enableAnimation(true)    //启用动画效果
//                .setAnimationStyle(anim)    //自定义动画
//                .setLocatedCity(new LocatedCity("定位中...", "", "-1"))  //APP自身已定位的城市，默认为null（定位失败）
                .setHotCities(hotCities)    //指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        if (null != data) {
                            Intent intent = new Intent();
                            intent.putExtra("city", data.getName());
                            setResult(RESULT_OK, intent);
                        }
                        finish();
                    }

                    @Override
                    public void onLocate() {
                        location = new Location(ChooseCityActivity.this, new AMapLocationListener() {
                            @Override
                            public void onLocationChanged(AMapLocation aMapLocation) {
                                if (aMapLocation.getErrorCode() == 0) {
                                    String city = aMapLocation.getCity();

                                    //定位完成之后更新数据
                                    CityPicker.getInstance().locateComplete(new LocatedCity(city, aMapLocation.getProvince(), aMapLocation.getCityCode()), LocateState.SUCCESS);
                                }
                                location.stop();
                            }
                        });
                        location.start();
                    }
                })
                .show();
    }
}
