package com.yyz.cyuanw.apiClient;

import android.util.Log;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.common.Constant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/*
 *okHttp的配置
 */
public class OkHttp3Utils {

    private static OkHttpClient mOkHttpClient;

    /**
     * 获取OkHttpClient对象
     */
    public static OkHttpClient getOkHttpClient() {

        if (null == mOkHttpClient) {
            //log拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("showLog","OkHttp====Message:"+message);
                }
            });

            if(Constant.DEBUG){
                //显示日志
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }else {
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            }

            //okhttp3后使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
//                    .addInterceptor(new Interceptor() {
//                        @Override
//                        public Response intercept(Interceptor.Chain chain) throws IOException {
//                            Request request = chain.request();
//                            Request.Builder builder1 = request.newBuilder();
//                            Request build = builder1.addHeader("token", App.get(Constant.KEY_USER_TOKEN)).build();
//                            return chain.proceed(build);
//                        }
//                    })
                    .build();
        }

        return mOkHttpClient;
    }

}

