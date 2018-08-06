package com.yyz.cyuanw.net;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import com.yyz.cyuanw.net.HCallback;
import com.yyz.cyuanw.tools.DToast;
import com.yyz.cyuanw.tools.Final;
import com.yyz.cyuanw.tools.GlobalDatas;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.Tools;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Http {
    private static final int TYPE_GET = 0;
    private static final int TYPE_POST = TYPE_GET + 1;
    private static final int TYPE_POST_JSON = TYPE_POST + 1;

    private static MediaType JSON_TYPE;
    //    private static MediaType IMAGE_TYPE;
    public static final int SUCCESS_CODE = 200;
    private static OkHttpClient mOkHttpClient;

    static {
        JSON_TYPE = MediaType.parse("application/json;charset=UTF-8");
//        IMAGE_TYPE = MediaType.parse("image/jpg");
    }

    public static void get(final String url, Map<String, Objects> params, final HCallback callbackInMainThread) {
        get(url, params, null, callbackInMainThread);
    }


    //    public static void post(final String url, Map<String, Objects> params, Map<String, Objects> heads, final HCallback callbackInMainThread) {
//        post(url, params, null, callbackInMainThread);
//    }
    public static void postJson(final String url, Map<String, Objects> params, final HCallback callbackInMainThread) {
        postJson(url, params, null, callbackInMainThread);
    }

    public static void get(final String url, Map<String, Objects> params, final HCallback callbackInWorkThread, final HCallback callbackInMainThread) {
        call(TYPE_GET, url, params, null, callbackInWorkThread, callbackInMainThread);
    }

    public static void postJson(final String url, Map<String, Objects> params, final HCallback callbackInWorkThread, final HCallback callbackInMainThread) {
        call(TYPE_POST_JSON, url, params, null, callbackInWorkThread, callbackInMainThread);
    }

    public static void post(final String url, Map<String, Objects> params, final HCallback callbackInMainThread) {
        post(url, params, null, callbackInMainThread);
    }

    public static void postHead(final String url, Map<String, Objects> params, Map<String, Objects> heads, final HCallback callbackInMainThread) {
        call(TYPE_POST, url, params, heads, null, callbackInMainThread);
    }

    public static void post(final String url, Map<String, Objects> params, final HCallback callbackInWorkThread, final HCallback callbackInMainThread) {
        call(TYPE_POST, url, params, null, callbackInWorkThread, callbackInMainThread);
    }

    private static void call(final int type, final String url, final Map<String, Objects> params, final Map<String, Objects> heads, final HCallback callbackInWorkThread, final HCallback callbackInMainThread) {
        if (Tools.getConnectedType() == -1) {//没有连接网络
            DToast.show("您好像没连接上网络");
            return;
        }
        Observable observable = Observable.just("").observeOn(Schedulers.io()).map(new Func1<String, HResult>() {
            @Override
            public HResult call(String s) {
                Response resultResponse = null;
                HResult result = new HResult();
                try {
                    switch (type) {
                        case TYPE_GET:
                            resultResponse = requestByGet(params, url);
                            break;
                        case TYPE_POST:
                            resultResponse = requestByPost(params, heads, url);
                            break;
                        case TYPE_POST_JSON:
                            resultResponse = requestByPostJson(params, url);
                            break;
                    }
                    if (resultResponse == null) {
                        return result;
                    }
                    result.init(resultResponse, url);

                    boolean isSuccessful = resultResponse.isSuccessful();
                    resultResponse.body().close();
                    if (isSuccessful) {
                        if (null != callbackInWorkThread) {
                            callbackInWorkThread.onResponse(result);
                        }
                    }
                } catch (Exception e) {
                    result.setE(e);
                }
                return result;
            }
        });
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<HResult>() {
            @Override
            public void call(HResult result) {
                if (null != callbackInMainThread) {
                    callbackInMainThread.initResponse(result);
                }
            }
        });
    }

    /**
     * 获取client对象
     *
     * @return
     */
    private static OkHttpClient getClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(Final.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Final.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Final.READ_TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }
        return mOkHttpClient;
    }

    /**
     * 获取统一的头部设置
     *
     * @return
     */
    public static Request.Builder getRequestBuilder(boolean isAddToken) {
        Request.Builder builder = new Request.Builder();
//        builder.addHeader("Content-Type", "application/json");
        builder.addHeader("Content-Type", "multipart/form-data");
        String version = "ver" + GlobalDatas.getLocalVersionName();
        String app_client = "android";
        builder.addHeader("version", version);
        builder.addHeader("app_client", app_client);
//        String user_token = GlobalDatas.getLoginedUserToken();
//        builder.addHeader("user_token", user_token);
//        LogManager.d("head info:user_token=" + user_token + "  version=" + version + "  app_client=" + app_client);
        if (isAddToken) {

        }
        return builder;
    }

    private static String getLanguageAndCountry() {
        //返回的是es或者zh；通过
        String l = Locale.getDefault().getLanguage();
        //获取当前国家或地区
        String c = Locale.getDefault().getCountry();
        return l + "," + c;
    }

    /**
     * 使用 get 方式发送请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    private static Response requestByGet(Map<String, Objects> params, String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        StringBuffer sb = new StringBuffer(url);
        String urlStr = "";
        if (null != params) {
            Iterator iter = params.entrySet().iterator();
            boolean isFirst = true;
            while (iter.hasNext()) {
                if (isFirst) {
                    isFirst = false;
                    sb.append("?");
                }
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                Object val = entry.getValue();
                sb.append(key);
                sb.append("=");
                sb.append(val);
                sb.append("&");
            }
            if (sb.toString().endsWith("&")) {
                urlStr = sb.substring(0, sb.length() - 1);
            } else {
                urlStr = sb.toString();
            }
        }
        LogManager.d("requestByGet:" + urlStr);
        OkHttpClient client = getClient();
        Request request = getRequestBuilder(true)
                .url(urlStr)
                .build();
        Response response = executeResponse(client, request);
        return response;
    }

    private static Response requestByPostJson(Map<String, Objects> params, String url) throws IOException {
        OkHttpClient client = getClient();
        String json = JSON.toJSONString(params);
        LogManager.d("requestByPostJson:" + url);
        LogManager.d("requestByPostJson:" + json);
        RequestBody body = RequestBody.create(JSON_TYPE, json);
        Request request = getRequestBuilder(true)
//                .addHeader("Accept-Language", getLanguageAndCountry())
                .url(url)
                .post(body)
                .build();
        Response response = executeResponse(client, request);
        return response;
    }

//    private static Response requestByPost(Map<String, Objects> params, String url) {
//        return requestByPost(params, null, url);
//    }

    /**
     * @param params
     * @param url
     * @return
     */
    private static Response requestByPost(Map<String, Objects> params, Map<String, Objects> heads, String url) {
        OkHttpClient client = getClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        LogManager.d("requestByPost:" + url);
        if (null != params && params.size() > 0) {
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                Object val = entry.getValue();
               /* if ("files".equals(key)) {
                    File[] files = (File[]) val;
                    for(File file : files){
                        if (null == file){
                            builder.addFormDataPart("files", "");
                        }else {
                            // MediaType.parse() 里面是上传的文件类型。
                            RequestBody body = RequestBody.create(MediaType.parse("image*//**//*"), file);
                            // 参数分别为， 请求key ，文件名称 ， RequestBody
                            builder.addFormDataPart(key, file.getName(), body);
                        }
                    }
                } else*/
                if (val instanceof File) {
                    if (null == val) {
                        builder.addFormDataPart(key, "");
                        LogManager.d("requestByPost:" + key + "=" + "");
                    } else {
                        File file = (File) val;
                        // MediaType.parse() 里面是上传的文件类型。
                        RequestBody body = RequestBody.create(MediaType.parse("image*//*"), file);
                        // 参数分别为， 请求key ，文件名称 ， RequestBody
                        builder.addFormDataPart(key, file.getName(), body);
                        LogManager.d("requestByPost:" + key + "=" + file.getAbsolutePath());
                    }
                } else if (val instanceof File[]) {
                    File[] files = (File[]) val;
                    // MediaType.parse() 里面是上传的文件类型。
                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];
                        if (file.isFile() && file.exists()) {
                            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                            builder.addFormDataPart(key, file.getName(), fileBody);
                        }
                        LogManager.d("requestByPost:" + key + "=" + file.getAbsolutePath());
                    }
                } else {
                    if (null == val) {
                        builder.addFormDataPart(key, "");
                    } else {
                        String value = val.toString();
                        builder.addFormDataPart(key, value);
                        LogManager.d("requestByPost:" + key + "=" + value);
                    }
                }
            }
        } else {
            Response response = null;
            try {
                response = requestByPostJson(params, url);
            } catch (IOException e) {
                LogManager.e(e);
            }
            return response;
        }

        Request.Builder requestBuilder = getRequestBuilder(true);
        if (null != heads && heads.size() > 0) {
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = entry.getKey().toString();
                Object val = entry.getValue();
                requestBuilder.addHeader(key, val.toString());
            }
        }
        Request request = requestBuilder
//                .addHeader("Accept-Language", getLanguageAndCountry())
                .url(url)
                .post(builder.build())
                .build();
        Response response = executeResponse(client, request);
        return response;
    }


   /* *//**
     * 根据url执行下载操作
     *
     * @param url
     * @return
     * @throws IOException
     *//*
    public static Response downloadRequest(String url) throws Exception {
        OkHttpClient client = getClient();
        Request request = new Request.Builder().url(url).build();
        Response response = executeResponse(client, request);
        return response;
    }*/

    /**
     * 执行网络请求,拦截部分异常不上报
     *
     * @param client
     * @param request
     * @return
     * @throws Exception
     */

    private static Response executeResponse(OkHttpClient client, Request request) {
        if (client == null || request == null) {
            return null;
        }
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*catch (ConnectException e){
            LogManager.d(TAG, "requestByHttpGet catch ConnectException!!!");
        }catch (SocketException e){
            LogManager.d(TAG, "requestByHttpGet catch SocketException!!!");
        }catch (UnknownHostException e){
            LogManager.d(TAG, "requestByHttpGet catch UnknownHostException!!!");
        }catch (SSLHandshakeException e){
            LogManager.d(TAG, "requestByHttpGet catch SSLHandshakeException!!!");
        }catch (SocketTimeoutException e){
            LogManager.d(TAG, "requestByHttpGet catch SocketTimeoutException!!!");
        }*/

        return response;
    }

    public static void uploadImg(final String url, final Map<String, String> filePaths, final String bucketName, final HCallback callbackInMainThread) {
        uploadImg(url, filePaths, bucketName, null, callbackInMainThread);
    }

    public static void uploadImg(final String url, final Map<String, String> filePaths, final String bucketName, final HCallback callbackInWorkThread, final HCallback callbackInMainThread) {
        Observable observable = Observable.just("").observeOn(Schedulers.io()).map(new Func1<String, HResult>() {
            @Override
            public HResult call(String s) {
                HResult result = new HResult();
                JSONObject obj = new JSONObject();
                try {
                    if (null != filePaths) {
                        Iterator iter = filePaths.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            String key = entry.getKey().toString();
                            String val = entry.getValue().toString();

                            HResult tempResult = new HResult();
                            Map map = new HashMap();
                            map.put("file", new File(val));
                            map.put("bucketName", bucketName);
                            Response resultResponse = requestByPost(map, null, url);
                            tempResult.init(resultResponse, url);
                            resultResponse.body().close();
                            if (!tempResult.isSuccess()) {
                                return tempResult;
                            }
                            String url = tempResult.getData();
                            obj.put(key, url);
                        }
                    }
                    result.setSuccess(true);
                    result.setData(obj.toString());
                    if (result.isSuccess()) {
                        if (null != callbackInWorkThread) {
                            callbackInWorkThread.onResponse(result);
                        }
                    }
                } catch (Exception e) {
                    result.setE(e);
                }
                return result;
            }
        });
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<HResult>() {
            @Override
            public void call(HResult result) {
                if (null != callbackInMainThread) {
                    callbackInMainThread.initResponse(result);
                }
            }
        });
    }


}
