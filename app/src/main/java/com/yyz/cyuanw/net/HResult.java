package com.yyz.cyuanw.net;

import android.text.TextUtils;


import com.yyz.cyuanw.tools.LogManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Louis on 16/4/8.
 */
public class HResult {

    private String url = "";

    private boolean isSuccess = false;
    private int code = 0;
    private int is_encrypt = 0;
    private String result = "";

    private String msg = "操作失败，请检查网络或联系系统管理员";
    private JSONObject dataJsonObj;
    private JSONArray dataJsonArr;
    private String data;

    private int pageSize = -1;
    private String dataList = "";

    private Throwable e = null;

    public void init(Response response, String url) throws Exception {
        this.url = url;
        if (null == response || !response.isSuccessful()) {
            ResponseBody body = response.body();
            if (null == body) {
                LogManager.d("body is 空的");
            } else {
                LogManager.d("body = " + body.string());
            }
            return;
        }
        result = response.body().string();
        JSONObject obj = new JSONObject(result);
        String dataYs = obj.optString("data");
        code = obj.optInt("code");
        is_encrypt = obj.optInt("is_encrypt");
        if (code == 200) {
            isSuccess = true;
                    data = dataYs;
        } else {
            data = dataYs;
        }
        msg = obj.optString("msg");
        LogManager.d(url + " ==> " + result);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public int getCode() {
        return code;
    }

    public int getIs_encrypt() {
        return is_encrypt;
    }

    public void setIs_encrypt(int is_encrypt) {
        this.is_encrypt = is_encrypt;
    }

    public String getResult() {
        return result;
    }

    public String getMsg() {
        return msg;
    }

    public void setData(String data) {
        this.data = data;
    }

    public JSONObject getJsonObj() {
        if (null == dataJsonObj) {
            try {
                dataJsonObj = new JSONObject(data);
            } catch (JSONException e1) {
                LogManager.e(e1);
            }
        }
        return dataJsonObj;
    }

    public JSONArray getJsonArr() {
        if (null == dataJsonArr) {
            try {
                dataJsonArr = new JSONArray(data);
            } catch (JSONException e1) {
                LogManager.e(e1);
            }
        }
        return dataJsonArr;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getData() {
        return data;
    }

    public Throwable getE() {
        return e;
    }

    public void setE(Throwable e) {
        isSuccess = false;
        this.e = e;
        LogManager.e(e);
    }

    public int getPageSize() {
        if (-1 == pageSize) {
            JSONObject object = getJsonObj();
            if (null == object) {
                pageSize = 0;
            } else {
                pageSize = object.optInt("pageSize");
            }
        }
        return pageSize;
    }

    public String getDataList() {
        if (TextUtils.isEmpty(dataList)) {
            JSONObject object = getJsonObj();
            if (null != object) {
                dataList = object.optString("dataList");
            }
        }
        return dataList;
    }
}
