package com.yyz.cyuanw.okhttp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


@SuppressLint("HandlerLeak")
public class OkhttpAsyncTask extends AsyncTask<Integer, Integer, Integer> {
	private OkHttpClient mOkHttpClient;
	private Request request;
	private long requestCancelTime=0;
	private Call call = null;
	private ICallback responseCallback;
	private Handler mHandler=new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			removeMessages(OkHttp3Util.CANCEL_REQUEST);
			if(msg.what==OkHttp3Util.CANCEL_REQUEST){
				if(call!=null){
					call.cancel();
					Log.i("shiyong", "OkhttpAsyncTask mHandler 请求取消");
				}
			}
		}
	};
	public OkhttpAsyncTask(OkHttpClient mOkHttpClient,Request request,ICallback responseCallback) {
		this.mOkHttpClient=mOkHttpClient;
		this.request=request;
		this.responseCallback=responseCallback;
	}
	public long getRequestCancelTime() {
		return requestCancelTime;
	}
	public void setRequestCancelTime(long requestCancelTime) {
		this.requestCancelTime = requestCancelTime;
	}

	@Override
	protected Integer doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		Call call = mOkHttpClient.newCall(request);
		try {
			if(requestCancelTime>0){
				mHandler.sendEmptyMessageDelayed(OkHttp3Util.CANCEL_REQUEST, requestCancelTime);
			}
			Response response=call.execute();
			String url=response.request().url().toString();
			String requestUrl=request.url().toString();
			if(requestUrl.equals(url)){
				responseCallback.onResponse(response);
			}else{
				mHandler.removeMessages(OkHttp3Util.CANCEL_REQUEST);
				call=null;
				responseCallback.onFailure(request, new MalformedURLException("url 地址和请求的地址不相等，网络做了重跳转"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			mHandler.removeMessages(OkHttp3Util.CANCEL_REQUEST);
			call=null;
			responseCallback.onFailure(request, e);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			mHandler.removeMessages(OkHttp3Util.CANCEL_REQUEST);
			call=null;
			responseCallback.onFailure(request,e);
		}
		return 0;
	}
}