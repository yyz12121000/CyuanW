package com.yyz.cyuanw.apiClient;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.bean.AdData;
import com.yyz.cyuanw.bean.CheyListData;
import com.yyz.cyuanw.bean.CyListData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data3;
import com.yyz.cyuanw.bean.HotLmData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.ImgData;
import com.yyz.cyuanw.bean.JjrData;
import com.yyz.cyuanw.bean.JjrResultData;
import com.yyz.cyuanw.bean.LmDetail;
import com.yyz.cyuanw.bean.LmListData;
import com.yyz.cyuanw.bean.LmMyListData;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.view.sortrecyclerview.SortModel;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
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
    public void login(String phone, String code, Observer<HttpResult<LoginData>> observer) {
        Observable observable = service.login(phone, code);
        setSubscribe(observable, observer);
    }

    public void getImgCode(String phone, Observer<HttpResult<ImgData>> observer) {
        Observable observable = service.getImgCode(phone);
        setSubscribe(observable, observer);
    }

    public void getValidateCode(String phone, String imgCode, Observer<HttpCodeResult> observer) {
        Observable observable = service.getValidateCode(phone, imgCode);
        setSubscribe(observable, observer);
    }

    public void logout(String token, Observer<HttpCodeResult> observer) {
        Observable observable = service.logout(token, "");
        setSubscribe(observable, observer);
    }

    public void getHotLmData(Observer<HttpListResult<HotLmData>> observer) {
        Observable observable = service.getHotLmData();
        setSubscribe(observable, observer);
    }

    public void getAdData(Observer<HttpResult<AdData>> observer) {
        Observable observable = service.getAdData();
        setSubscribe(observable, observer);
    }

    public void getJjrData(String longitude, String latitude, Observer<HttpResult<JjrResultData>> observer) {
        Observable observable = service.getJjrData(longitude, latitude);
        setSubscribe(observable, observer);
    }

    public void getLmList(String keyword, int page, Observer<HttpResult<LmListData>> observer) {
        Observable observable = service.getLmList(keyword, page);
        setSubscribe(observable, observer);
    }

    public void getLmMyList(int keyword, Observer<HttpListResult<LmMyListData>> observer) {
        Observable observable = service.getLmMyList(keyword);
        setSubscribe(observable, observer);
    }

    public void createLm(String name, String logo,
                         String intro, int province_id,
                         int city_id, int region_id, String qr_code, Observer<HttpResult> observer) {
        Observable observable = service.createLm(name, logo, intro, province_id, city_id, region_id, qr_code);
        setSubscribe(observable, observer);
    }

    public void lmcylist(int lm_id, Observer<HttpResult<CyListData>> observer) {
        Observable observable = service.lmcylist("alliances/" + lm_id + "/users");
        setSubscribe(observable, observer);
    }
    public void lmcheylist(int lm_id, Observer<HttpResult<CheyListData>> observer) {
        Observable observable = service.lmcheylist("alliances/" + lm_id + "/cars");
        setSubscribe(observable, observer);
    }
     public void lmDetail(int lm_id, Observer<HttpResult<LmDetail>> observer) {
        Observable observable = service.lmDetail("alliances/" + lm_id);
        setSubscribe(observable, observer);
    }
    public void searchCy(int source,int order,int min_price,int max_price,int brand_id,int series_id,String key_word,Observer<HttpResult<Data1>> observer) {
        Observable observable = service.searchCy(source,order,min_price,max_price,brand_id,series_id,key_word);
        setSubscribe(observable, observer);
    }
    public void dictionary(Observer<HttpResult<Data3>> observer) {
        Observable observable = service.dictionary(App.get(Constant.KEY_USER_TOKEN),"");
        setSubscribe(observable, observer);
    }
    public void dictionary2(Observer<ResponseBody> observer) {
        Observable observable = service.dictionary2(App.get(Constant.KEY_USER_TOKEN),"");
        setSubscribe(observable, observer);
    }
    public void top_brand(Observer<HttpListResult<SortModel>> observer) {
        Observable observable = service.top_brand(App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }
    public void brandNext(int id,Observer<ResponseBody> observer) {
        Observable observable = service.brandNext(App.get(Constant.KEY_USER_TOKEN),id);
        setSubscribe(observable, observer);
    }


    /**
     * 插入观察者
     *
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
