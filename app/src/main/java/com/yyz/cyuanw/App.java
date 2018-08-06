package com.yyz.cyuanw;

import android.app.Application;

import android.content.Context;

import com.yyz.cyuanw.tools.LogManager;

/**
 * Created by Administrator on 2017/4/3.
 */

public class App extends Application {
    public static Context context;


    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogManager.e("===============App start=====================");
        context = getApplicationContext();


    }






}
