package com.yyz.cyuanw.tools;

import android.content.SharedPreferences;
import android.text.TextUtils;


import com.yyz.cyuanw.App;

public class SPUtils {
    private static final String COMMON_KEY_VALUE = "common_key_value";


    public static void putString(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        SharedPreferences sharedPreferences = App.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences = App.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        return sharedPreferences.getString(key, "");
    }

    public static void putBoolean(String key, boolean value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences sharedPreferences = App.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key,boolean defualtValue) {
        SharedPreferences sharedPreferences = App.context.getSharedPreferences(COMMON_KEY_VALUE, 0);
        return sharedPreferences.getBoolean(key, defualtValue);
    }

}
