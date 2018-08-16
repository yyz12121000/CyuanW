package com.yyz.cyuanw.tools;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;

public class Location {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    public Location(Context context, AMapLocationListener locationListener) {
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationListener(locationListener);
    }


    public void start() {
        //启动定位
        mLocationClient.startLocation();
    }

    public void stop() {
        if (mLocationClient.isStarted()) {
            //启动定位
            mLocationClient.stopLocation();
        }
    }
}
