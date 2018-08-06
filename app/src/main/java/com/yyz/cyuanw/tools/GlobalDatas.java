package com.yyz.cyuanw.tools;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.yyz.cyuanw.App;


/**
 * Created by Administrator on 2017/5/11.
 */

public class GlobalDatas {
    private static int versionCode = -1;
    private static String versionName = "";

//    private static UserBeen user;

//    public static final String WX_APP_ID = "wxf01496e43b03a153";
//    public static final String WX_SECRET = "45ff99ed1279a647f9589fcadf94607b";
//    private static IWXAPI wx_api; //全局的微信api对象
//
//    public static IWXAPI getWx_api() {
//        if (null == wx_api) {
//            wx_api = WXAPIFactory.createWXAPI(App.context, GlobalDatas.WX_APP_ID, true);
//            wx_api.registerApp(GlobalDatas.WX_APP_ID);
//        }
//        return wx_api;
//    }

    /**
     * 获取登录用户对象
     *
     * @return
     */
//    public static UserBeen getLoginedUser() {
//        if (null == user) {
//            Realm realm = Realm.getDefaultInstance();
//            UserBeen tempUser = realm.where(UserBeen.class).equalTo("state", UserBeen.STATE_CURR_LOGIN).findFirst();
//            if (null != tempUser) {
//                user = realm.copyFromRealm(tempUser);
//            }
//            realm.close();
//        }
//        return user;
//    }

    /**
     * 获取登录用的id
     *
     * @return
     */
//    public static int getLoginedUserId() {
//        UserBeen user = getLoginedUser();
//        if (null != user) {
//            return user.getId();
//        }
//        return 0;
//    }

    /**
     * 获取登录用的token
     *
     * @return
     */
//    public static String getLoginedUserToken() {
//        UserBeen user = getLoginedUser();
//        if (null != user) {
//            return user.getToken();
//        }
//        return "";
//    }

    /**
     * 判断是否有用户已经登录
     *
     * @return
     */
//    public static boolean isLogin() {
//        return null != getLoginedUser();
//    }

    /**
     * 清除用户信息
     */
//    public static void clearUser() {
//        GlobalDatas.user = null;
//    }

    /**
     * 清除登录用户信息
     */
//    public static void clearLoginedUser() {
//        UserBeen user = getLoginedUser();
//        if (null != user) {
//            App.getRealm().beginTransaction();
//            user.setState(UserBeen.STATE_OTHER);
//            App.getRealm().copyToRealmOrUpdate(user);
//            App.getRealm().commitTransaction();
//            GlobalDatas.user = null;
//        }
//    }

//    public static void logout() {
//        GlobalDatas.clearLoginedUser();
//        Intent intent = new Intent(App.context, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        App.context.startActivity(intent);
//    }

    /**
     * 获取本地软件版本号
     */
    public static int getLocalVersionCode() {
        if (versionCode > 0) {
            return versionCode;
        }
        try {
            PackageInfo packageInfo = App.context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(App.context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName() {
        if (!TextUtils.isEmpty(versionName)) {
            return versionName;
        }
        try {
            PackageInfo packageInfo = App.context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(App.context.getPackageName(), 0);
            versionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
