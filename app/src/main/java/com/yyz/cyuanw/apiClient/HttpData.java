package com.yyz.cyuanw.apiClient;

import com.yyz.cyuanw.bean.AdData;
import com.yyz.cyuanw.bean.HotLmData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.ImgData;
import com.yyz.cyuanw.bean.LoginData;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 所有的请求数据的方法
 * 根据MovieService的定义编写合适的方法
 * 其中observable是获取API数据
 * observableCahce获取缓存数据
 * new EvictDynamicKey(false) false使用缓存  true 加载数据不使用缓存
 */
public class HttpData extends RetrofitUtils {


    protected static final ApiService service = getRetrofit().create(ApiService.class);

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpData INSTANCE = new HttpData();
    }

    //获取单例
    public static HttpData getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //获取Api请求类
    public ApiService getApiService() {
        return service;
    }

    //用户登录
    public void login(String phone,String code, Observer<HttpResult<LoginData>> observer){
        Observable observable = service.login(phone,code);
        setSubscribe(observable, observer);
    }

    public void getImgCode(String phone, Observer<HttpResult<ImgData>> observer){
        Observable observable = service.getImgCode(phone);
        setSubscribe(observable, observer);
    }

    public void getValidateCode(String phone,String imgCode,Observer<HttpCodeResult> observer){
        Observable observable = service.getValidateCode(phone,imgCode);
        setSubscribe(observable, observer);
    }

    public void logout(String token,Observer<HttpCodeResult> observer){
        Observable observable = service.logout(token,"");
        setSubscribe(observable, observer);
    }

    public void getHotLmData(Observer<HttpListResult<HotLmData>> observer){
        Observable observable = service.getHotLmData();
        setSubscribe(observable, observer);
    }
    public void getAdData(Observer<HttpResult<AdData>> observer){
        Observable observable = service.getAdData();
        setSubscribe(observable, observer);
    }

    /**
     * 插入观察者
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

}
