package com.yyz.cyuanw.tools;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.activity.fragment.SyFragment;
import com.yyz.cyuanw.bean.LocationO;
import com.yyz.cyuanw.common.Constant;

public class Location {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public ILocationListener locationListener;
    private boolean flag = false;

    public Location(Context context, ILocationListener locationListener) {
        this.locationListener = locationListener;

        res();

        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    stop();
                    String city = aMapLocation.getCity();
                    if (null != city) {
                        App.set(Constant.KEY_LOCATION_CITY, city);
                        App.set(Constant.KEY_LOCATION_LON, aMapLocation.getLongitude() + "");
                        App.set(Constant.KEY_LOCATION_LAT, aMapLocation.getLatitude() + "");

                        App.set(Constant.KEY_LOCATION_PROVINCE, aMapLocation.getProvince());
                        App.set(Constant.KEY_LOCATION_CITYCODE, aMapLocation.getCityCode());

                        res();
                    }
                }
                stop();
            }
        });
        start();
    }

    private void res() {
        if (flag) return;
        String city = App.get(Constant.KEY_LOCATION_CITY);
        String longitude = App.get(Constant.KEY_LOCATION_LON);
        String latitude = App.get(Constant.KEY_LOCATION_LAT);
        String province = App.get(Constant.KEY_LOCATION_PROVINCE);
        String citycode = App.get(Constant.KEY_LOCATION_CITYCODE);

        if (!TextUtils.isEmpty(city)) {
            LocationO locationO = new LocationO();
            locationO.city = city;
            locationO.longitude = longitude;
            locationO.latitude = latitude;
            locationO.province = province;
            locationO.cityCode = citycode;

            locationListener.location(locationO);
            flag = true;
        }
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

    public interface ILocationListener {
        void location(LocationO locationO);
    }


}
