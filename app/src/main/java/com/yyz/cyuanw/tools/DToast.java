package com.yyz.cyuanw.tools;

import android.widget.Toast;

import com.yyz.cyuanw.App;


/**
 * Created by Administrator on 2017/4/15.
 */

public class DToast {
    private static final Toast toast = Toast.makeText(App.context, "", Toast.LENGTH_SHORT);

    public static void show(String text) {
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.show();
    }

    public static void show(int textId) {
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(textId);
        toast.show();
    }

    public static void show(String text, int duration) {
        toast.setDuration(duration);
        toast.setText(text);
        toast.show();
    }

    public static void show(int textId, int duration) {
        toast.setDuration(duration);
        toast.setText(textId);
        toast.show();
    }
}
