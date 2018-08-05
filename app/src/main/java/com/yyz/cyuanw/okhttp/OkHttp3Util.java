package com.yyz.cyuanw.okhttp;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp3Util {
	/**
	 * HTTP请求失败
	 */
	public static final int REQUEST_FAILURE = -1;
	/**
	 * HTTP请求失败
	 */
	public static final int CANCEL_REQUEST = -2;
	/**
	 * HTTP请求超时时间
	 */
	public static final int REQUEST_TIMEOUT = 1000 * 10;// 10秒

	private static OkHttpClient client = new OkHttpClient();
	/*private static final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
			.connectTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS).authenticator(new Authenticator() {
				@Override
				public Request authenticate(Route route, Response response) throws IOException {
					boolean isProxy=false;
					String credential = Credentials.basic(null,null);
					//判断是否是代理
					if (response.request().header("Proxy-Authorization") != null) {
						return response.request().newBuilder()
								.header("Proxy-Authorization", credential)
								.build();
					}
					//判断是否有证书
					if (response.request().header("Authorization") != null) {
						return null;
					}
					return response.request().newBuilder()
							.header("Authorization", credential)
							.build();
				}
			}).build();*/

	private static final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
			.connectTimeout(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS).build();
	static { //mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
		/*builder.sslSocketFactory(sslContext.getSocketFactory());
		builder.hostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String s, SSLSession sslSession) {
				return true;
			}
		});*/
	}

	public static OkHttpClient getOkHttpClient() {
		return mOkHttpClient;
	}
	/**
	 * 获取所有请求头必须设置的Request.Builder
	 *
	 * @return
	 */
	private static Request.Builder defaultBuilder() {
		Request.Builder builder = new Request.Builder();
		builder.header("Accept", "application/json");
		return builder;
	}
	/**
	 * 设置X-Access-Token表示用户身份的Request.Builder
	 *
	 * @return
	 */
	private static Request.Builder xAccessTokenBuilder(String xAccessToken) {
		Request.Builder builder = defaultBuilder();
		if(xAccessToken!=null){
			builder.addHeader("X-Access-Token", xAccessToken);
		}
		return builder;
	}

	/**
	 * 获取get请求的Request
	 * @param url 请求的url
	 * @return
	 */
	private static Request getRequest(String url) {
		Request.Builder builder = defaultBuilder();
		builder.url(url);
		builder.get();
		return builder.build();
	}

    /**
     * get请求
     * @param url
     * @param mCallback
     */
    public static void getEnqueue(String url ,Callback mCallback) {
        Request mRequest=OkHttp3Util.getRequest(url);
        OkHttp3Util.enqueue(mRequest,mCallback);
    }
	/**
	 * 获取get请求的Request
	 *
	 * @param url
	 *            请求的url
	 * @param xAccessToken
	 *            表示用户身份
	 * @return
	 */
	private static Request getRequest(String xAccessToken, String url) {
		Request.Builder builder = xAccessTokenBuilder(xAccessToken);
		builder.url(url);
		builder.get();
		return builder.build();
	}

    /**
     * 有AccessToken的get请求
     * @param xAccessToken
     * @param url
     * @param mCallback
     */
    public static void getEnqueue(String xAccessToken,String url ,Callback mCallback) {
        Request mRequest=OkHttp3Util.getRequest(xAccessToken,url);
        OkHttp3Util.enqueue(mRequest,mCallback);
    }

	/**
	 * 获取post请求的Request
	 *
	 * @param url
	 *            请求的url
	 * @param json
	 *            请求的参数
	 * @return
	 */
	private static Request postRequest(String url, String json) {
		Request.Builder builder = defaultBuilder();
		builder.url(url);
		//RequestBody body = RequestBody.create(MediaType.parse("text/plain;charset=utf-8"), json);
		RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
		builder.post(body);
		return builder.build();
	}

    /**
     * post请求
     * @param url
     * @param json
     * @param mCallback
     */
    public static void postEnqueue(String url , String json,Callback mCallback){
        Request mRequest=OkHttp3Util.postRequest(url,json);
        OkHttp3Util.enqueue(mRequest,mCallback);
    }
    /**
     * 获取post请求的Request
     *
     * @param url
     *            请求的url
     * @param json
     *            请求的参数
     * @param xAccessToken
     *            表示用户身份
     * @return
     */
	private static Request postRequest(String xAccessToken, String url, String json) {
        Request.Builder builder = xAccessTokenBuilder(xAccessToken);
        builder.url(url);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);
        builder.post(body);
        return builder.build();
    }

    /**
     * 有AccessToken的post请求
     * @param xAccessToken
     * @param url
     * @param json
     * @param mCallback
     */
    public static void postEnqueue(String xAccessToken,String url, String json ,Callback mCallback){
        Request mRequest=OkHttp3Util.postRequest(xAccessToken,url,json);
        OkHttp3Util.enqueue(mRequest,mCallback);
    }
	/**
	 * 通过FormEncodingBuilder获取post请求Request
	 * @param builder
	 * @param url
	 * @return
	 */
	private static Request postRequest(FormBody.Builder builder, String url) {
		RequestBody requestBody = builder.build();
		Request.Builder requestBuilder = new Request.Builder();
		requestBuilder.url(url);
		requestBuilder.post(requestBody);
		return requestBuilder.build();
	}

    /**
     * 通过FormBody的post请求
     * @param builder
     * @param url
     * @param mCallback
     */
    public static void postFormBodyEnqueue(FormBody.Builder builder,String url,Callback mCallback){
        Request mRequest=OkHttp3Util.postRequest(builder,url);
        OkHttp3Util.enqueue(mRequest,mCallback);
    }
	/**
	 * 通过MultipartBuilder组建post请求的Request
	 * @param multipartBuilder
	 * @param xAccessToken
	 * @param url
	 * @return
	 */
	private static Request buildMultipartFormPostRequest(MultipartBody.Builder multipartBuilder,String xAccessToken,String url){
		RequestBody requestBody = multipartBuilder.build();
		Request.Builder builder = new Request.Builder();
		builder.addHeader("X-Crypt", "obfsv3"); // 加密算法和版本号
		builder.addHeader("X-Api", "v1");// API版本号
		//builder.addHeader("user-agent",UseAgentUtil.getDefaultUserAgent(context));
		builder.addHeader("X-Access-Token", (xAccessToken!=null)?xAccessToken:"");
		builder.url(url);
		builder.post(requestBody);
		return builder.build();
	}

    /**
     * 获取上传文件的Request
     * @param xAccessToken
     * @param filePath
     * @param url
     * @return
     */
	private static Request fileMultipartBody(String xAccessToken,String filePath,String url){
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
            MultipartBody.Builder multipartBuilder=new MultipartBody.Builder().setType(MultipartBody.FORM);
            multipartBuilder.addFormDataPart("image", file.getName(), fileBody);//上传图片
            //multipartBuilder.addFormDataPart("audio", file.getName(), fileBody);//上传音频
            return OkHttp3Util.buildMultipartFormPostRequest(multipartBuilder,xAccessToken,url);
        }
        return null;
    }

    /**
     *文件上传
     * @param xAccessToken
     * @param fileType audio上传音频,image图片
     * @param filePath
     * @param url
     * @param mCallback
     */
	public static void uploadFile(String xAccessToken,String filePath,String fileType,String url,Callback mCallback){
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
			MultipartBody.Builder multipartBuilder=new MultipartBody.Builder().setType(MultipartBody.FORM);
			multipartBuilder.addFormDataPart(fileType, file.getName(), fileBody);//上传图片
			//multipartBuilder.addFormDataPart("audio", file.getName(), fileBody);//上传音频
			OkHttp3Util.enqueue(OkHttp3Util.buildMultipartFormPostRequest(multipartBuilder,xAccessToken,url), mCallback);
			//Headers mHeaders=response.headers();
			//Date mDate=mHeaders.getDate("Date");
		}
	}

	/**
	 * 获取delete请求的Request
	 * @param url
	 * @param json
	 * @param xAccessToken
	 * @return
	 */
	private static Request deleteRequest(String url,String json,String xAccessToken) {
		Request.Builder builder = xAccessTokenBuilder(xAccessToken);
		builder.url(url);
		RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
		builder.delete(body);
		return builder.build();
	}

    /**
     * 删除请求
     * @param url
     * @param json
     * @param xAccessToken
     * @param mCallback
     */
    public static void deleteEnqueue(String url,String json,String xAccessToken ,Callback mCallback) {
        Request mRequest=OkHttp3Util.deleteRequest(url,json,xAccessToken);
        OkHttp3Util.enqueue(mRequest,mCallback);
    }
	/**
	 * 获取put请求的Request
	 * @param url 请求的url
	 * @param json 请求的参数
	 * @param xAccessToken 表示用户身份
	 * @return
	 */
	private static Request putRequest(String url, String json,
			String xAccessToken) {
		Request.Builder builder = xAccessTokenBuilder(xAccessToken);
		builder.url(url);
		RequestBody body = RequestBody.create(
				MediaType.parse("application/json"), json);
		builder.put(body);
		return builder.build();
	}

    /**
     * put请求
     * @param url
     * @param json
     * @param xAccessToken
     * @param mCallback
     */
    public static void putEnqueue(String url,String json,String xAccessToken ,Callback mCallback) {
        Request mRequest=OkHttp3Util.putRequest(url,json,xAccessToken);
        OkHttp3Util.enqueue(mRequest,mCallback);
    }
	/**
	 * 获取同步Call
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Call getCall(Request request) {
		return mOkHttpClient.newCall(request);
	}

	/**
	 * 该不会开启异步线程。
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Response execute(Request request) throws IOException {
		return mOkHttpClient.newCall(request).execute();
	}

	/**
	 * 异步线程访问网络，并且返回call
	 *
	 * @param request
	 * @param responseCallback
	 * @return 返回Call可以取消请求call.cancel()
	 */
	public static Call enqueue(Request request, Callback responseCallback) {
		Call call = mOkHttpClient.newCall(request);
		call.enqueue(responseCallback);
		return call;
	}


	/**
	 * 自定义异步线程访问网络
	 * @param request
	 * @param responseCallback 自定义ICallback
	 * @param requestCancelTime 请求超时时间
	 */
	public static void enqueue(Request request, ICallback responseCallback,long requestCancelTime) {
		OkhttpAsyncTask mOkhttpAsyncTask = new OkhttpAsyncTask(mOkHttpClient,
				request, responseCallback);
		mOkhttpAsyncTask.setRequestCancelTime(requestCancelTime);
		mOkhttpAsyncTask.execute(0);
	}
	/**
	 * 自定义异步线程访问网络
	 *
	 * @param request
	 * @param responseCallback 自定义ICallback
	 */
	public static void enqueue(Request request, ICallback responseCallback) {
		OkhttpAsyncTask mOkhttpAsyncTask = new OkhttpAsyncTask(mOkHttpClient,
				request, responseCallback);
		mOkhttpAsyncTask.execute(0);
	}

	public static SSLContext getSLLContext(Context mContext){
		SSLContext sslContext = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream cerInputStream = mContext.getAssets().open("ca.cer");
			Certificate ca = cf.generateCertificate(cerInputStream);

			String keyStoreType = KeyStore.getDefaultType();
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);

			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keyStore);
			TrustManager[] trustManagers = tmf.getTrustManagers();

			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustManagers, null);
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		return  sslContext;
	}
}