package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.yyz.cyuanw.bean.Data9;
import com.yyz.cyuanw.bean.LocationO;
import com.yyz.cyuanw.tools.Location;
import com.yyz.cyuanw.view.choosecity.CityPicker;
import com.yyz.cyuanw.view.choosecity.DBManager;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

public class ChooseCityActivity extends BaseActivity {
    private Location location;
    private String addressJson = "";

    private int sheng_id, shi_id;
    private DBManager dbManager;

    @Override
    public void initView() {
        dbManager = new DBManager(this);
        showCity();
    }

    @Override
    public void initData() {


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != location) {
            location.stop();
        }
    }

    private void showCity() {
//        List<HotCity> hotCities = new ArrayList<>();
//        hotCities.add(new HotCity("北京", "北京", "101010100"));
//        hotCities.add(new HotCity("上海", "上海", "101020100"));
//        hotCities.add(new HotCity("广州", "广东", "101280101"));
//        hotCities.add(new HotCity("深圳", "广东", "101280601"));
//        hotCities.add(new HotCity("杭州", "浙江", "101210101"));


        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())    //此方法必须调用
//                .enableAnimation(true)    //启用动画效果
//                .setAnimationStyle(anim)    //自定义动画
//                .setLocatedCity(new LocatedCity("定位中...", "", "-1"))  //APP自身已定位的城市，默认为null（定位失败）
//                .setHotCities(hotCities)    //指定热门城市
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {
                        if (null != data) {
                            if (position == 0) {
                                try {
                                    Data9 sheng = dbManager.findLocationCity(data.getName());
                                    Intent intent = getIntent();
                                    intent.putExtra("sheng_name", sheng.name);
                                    intent.putExtra("sheng_id", sheng.id);
                                    intent.putExtra("shi_id", sheng.son.get(0).id);
                                    intent.putExtra("city", sheng.son.get(0).name);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                sheng_id = Integer.parseInt(data.getCode());

                                Intent intent = new Intent(ChooseCityActivity.this, ListCityChooseActivity.class);
                                intent.putExtra("type", getIntent().getIntExtra("type",-1));
                                intent.putExtra("sheng_id", sheng_id);
                                intent.putExtra("sheng_name", data.getName());
                                startActivityForResult(intent, 2);
                            }
                        }else{
                            finish();
                        }
                    }

                    @Override
                    public void onLocate() {
                        location = new Location(ChooseCityActivity.this, new Location.ILocationListener() {
                            @Override
                            public void location(LocationO locationO) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //定位完成之后更新数据
                                        CityPicker.getInstance().locateComplete(new LocatedCity(locationO.city, locationO.province, locationO.cityCode), LocateState.SUCCESS);
                                    }
                                }, 0);
                            }
                        });
                    }
                })
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case 2:
                if (null == data) return;
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }
}
