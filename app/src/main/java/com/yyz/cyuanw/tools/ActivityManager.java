package com.yyz.cyuanw.tools;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/1 0001.
 */

public class ActivityManager {
    private static List<Activity> activities = new ArrayList<>();

    public static void add(Activity activity) {
        activities.add(activity);
    }

    public static void remove(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public static List<Activity> getActivities() {
        return activities;
    }
}
