package com.yyz.cyuanw;

import android.app.Application;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;

/**
 * Created by Administrator on 2017/4/3.
 */

public class App extends Application {
    public static Context context;

    private static String PREF_NAME = "my.pref";

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogManager.e("===============App start=====================");
        context = getApplicationContext();


    }

    public static void showToast(String text) {
        if (StringUtil.isNotNull(text))
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static SharedPreferences getPreferences() {
        SharedPreferences pre = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pre;
    }

    public static void set(String key, String value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String get(String key) {
        return getPreferences().getString(key, "");
    }


}
