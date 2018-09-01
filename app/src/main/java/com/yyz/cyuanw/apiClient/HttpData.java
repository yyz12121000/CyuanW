package com.yyz.cyuanw.apiClient;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.bean.AdData;
import com.yyz.cyuanw.bean.CheyListData;
import com.yyz.cyuanw.bean.CyListData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data10;
import com.yyz.cyuanw.bean.Data15;
import com.yyz.cyuanw.bean.Data3;
import com.yyz.cyuanw.bean.Data5;
import com.yyz.cyuanw.bean.Data6;
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

import okhttp3.RequestBody;
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
    public void login(String phone,String code, Observer<HttpResult<LoginData>> observer){
        Observable observable = service.login(phone,code);
        setSubscribe(observable, observer);
    }

    public void getUserInfo(String token,Observer<HttpResult<LoginData>> observer){
        Observable observable = service.getUserInfo(token);
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

    public void nameConfirm(String name,String no,String token,Observer<HttpCodeResult> observer){
        Observable observable = service.nameConfrim(name,no,token);
        setSubscribe(observable, observer);
    }

    public void changeSend(String phone,int type,String token,Observer<HttpCodeResult> observer){
        Observable observable = service.changeSend(phone,type,token);
        setSubscribe(observable, observer);
    }

    public void changePhone(String phone,String oldcode,String newcode,String token,Observer<HttpCodeResult> observer){
        Observable observable = service.changePhone(phone,oldcode,newcode,token);
        setSubscribe(observable, observer);
    }

    public void setUserInfo(String name,String value,String token,Observer<HttpCodeResult> observer){
        Observable observable = null;
        switch (name){
            case "gender":
                observable = service.setGender(value,token);
                break;
            case "signature":
                observable = service.setSignature(value,token);
                break;
            case "virtual_number":
                observable = service.setVirtualNumber(value,token);
                break;
        }
        setSubscribe(observable, observer);
    }

    public void uploadFile(RequestBody body, int type, Observer<HttpResult<ImgData>> observer){
        Observable observable = null;
        switch (type){
            case 0:
                observable = service.setPic(body);
                break;
            case 1:
                observable = service.setBgPic(body);
                break;
        }
        setSubscribe(observable, observer);
    }

    public void logout(String token,Observer<HttpCodeResult> observer){
        Observable observable = service.logout(token);
        setSubscribe(observable, observer);
    }

    public void getHotLmData(Observer<HttpListResult<HotLmData>> observer) {
        Observable observable = service.getHotLmData();
        setSubscribe(observable, observer);
    }
    public void getAdData(Observer<HttpResult<AdData>> observer){
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

    public void getLmMyList(int page, Observer<HttpListResult<LmMyListData>> observer) {
        Observable observable = service.getLmMyList(page);
        setSubscribe(observable, observer);
    }
    public void getMyLmList(int page, Observer<HttpResult<Data15>> observer) {
        Observable observable = service.getMyLmList(page);
        setSubscribe(observable, observer);
    }

    public void createLm(String name, String logo,
                         String intro, int province_id,
                         int city_id, int region_id, String qr_code, Observer<HttpResult> observer) {
        Observable observable = service.createLm(name, logo, intro, province_id, city_id, region_id, qr_code,App.get(Constant.KEY_USER_TOKEN));
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
    public void lmEdital(int lm_id,String ewm, Observer<HttpResult> observer) {
        Observable observable = service.lmEdital(App.get(Constant.KEY_USER_TOKEN),ewm,"alliances/" + lm_id);
        setSubscribe(observable, observer);
    }
    public void cancel_administrator(int lm_id,int set_user_id, Observer<HttpListResult> observer) {
        Observable observable = service.cancel_administrator("alliances/cancel_administrator/" + lm_id+"/"+set_user_id,App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }
    public void set_administrator(int lm_id,int set_user_id, Observer<HttpListResult<String>> observer) {
        Observable observable = service.set_administrator("alliances/set_administrator/" + lm_id+"/"+set_user_id,App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }
    public void searchCy(int page,int province_id,int city_id,int region_id,int source,int order,int min_price,int max_price,int brand_id,int series_id,int color,int gearbox, int emission_standard ,
                         int  fuel_type ,int  min_year ,int  max_year , int min_mileage, int max_mileage,String key_word,Observer<HttpResult<Data1>> observer) {
        Observable observable = service.searchCy(page,province_id,city_id,region_id,source,order,min_price,max_price,brand_id,series_id,color,gearbox, emission_standard , fuel_type ,
                min_year , max_year , min_mileage, max_mileage,key_word,0,App.get(Constant.KEY_USER_TOKEN));
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
    public void search_criteria(Observer<HttpResult<Data3>> observer) {
        Observable observable = service.search_criteria(App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }
    public void carScreeningCount(int source,int order,int min_price,int max_price,int brand_id,int series_id,int color,int gearbox, int emission_standard ,
                                  int  fuel_type ,int  min_year ,int  max_year , int min_mileage, int max_mileage,String key_word,Observer<HttpResult<Data5>> observer) {
        Observable observable = service.carScreeningCount(source,order,min_price,max_price,brand_id,series_id,color,gearbox, emission_standard , fuel_type ,
                min_year , max_year , min_mileage, max_mileage,key_word,0,App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }

    public void carInfo(int id,Observer<HttpResult<Data6>> observer) {
        Observable observable = service.carInfo(App.get(Constant.KEY_USER_TOKEN),id);
        setSubscribe(observable, observer);
    }
    public void collections(int car_resources_id,Observer<HttpListResult<String>> observer) {
        Observable observable = service.collections(App.get(Constant.KEY_USER_TOKEN),car_resources_id);
        setSubscribe(observable, observer);
    }
    public void rmCollections(int car_resources_id,Observer<HttpListResult<String>> observer) {
        Observable observable = service.rmCollections(App.get(Constant.KEY_USER_TOKEN),"collections/"+car_resources_id);
        setSubscribe(observable, observer);
    }

    public void delete_alliances(int alliance_id,int delete_user_id,Observer<HttpListResult> observer) {
        Observable observable = service.delete_alliances(App.get(Constant.KEY_USER_TOKEN),"alliances/"+alliance_id+"/users/"+delete_user_id);
        setSubscribe(observable, observer);
    }
    public void apply_join_users(int alliance_id,Observer<HttpResult<Data10>> observer) {
        Observable observable = service.apply_join_users("alliances/"+alliance_id+"/apply_join_users",App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }
    public void audit_apply(int alliance_id,int apply_user_id,int is_pass,Observer<HttpResult<Data10>> observer) {
        Observable observable = service.audit_apply("alliances/"+alliance_id+"/audit_apply/"+apply_user_id,is_pass,App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }
    public void apply_join(int alliance_id,Observer<HttpListResult> observer) {
        Observable observable = service.apply_join("alliances/"+alliance_id+"/apply_join",App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }
    public void ossToken(Observer<ResponseBody> observer) {
        Observable observable = service.ossToken(App.get(Constant.KEY_USER_TOKEN));
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