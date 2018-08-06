package com.yyz.cyuanw.net;


import com.yyz.cyuanw.tools.LogManager;

public abstract class HCallback {
    private boolean needCheckLogin = true;

    public HCallback() {
    }

    public HCallback(boolean needCheckLogin) {
        this.needCheckLogin = needCheckLogin;
    }

    public abstract void onResponse(HResult result);

    public void onToLogin() {
    }

    public void initResponse(HResult result) {
        if (needCheckLogin) {
            checkLogin(result);
        }
        try {
            onResponse(result);
        } catch (Exception e) {
            LogManager.e("接口返回的数据解析错误啦，请看下面：");
            e.printStackTrace();
        }
    }

    private void checkLogin(HResult result) {
        if (result.getCode() == 406) {
//            GlobalDatas.logout();
            onToLogin();
//            DToast.show("您还没登录哦");
        }
    }

}