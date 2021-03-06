package com.yyz.cyuanw.apiClient;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.bean.AdData;
import com.yyz.cyuanw.bean.CarData;
import com.yyz.cyuanw.bean.CarStatusData;
import com.yyz.cyuanw.bean.CardIDData;
import com.yyz.cyuanw.bean.CheyListData;
import com.yyz.cyuanw.bean.CyListData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data10;
import com.yyz.cyuanw.bean.Data15;
import com.yyz.cyuanw.bean.Data2;
import com.yyz.cyuanw.bean.Data3;
import com.yyz.cyuanw.bean.Data5;
import com.yyz.cyuanw.bean.Data6;
import com.yyz.cyuanw.bean.DealerData;
import com.yyz.cyuanw.bean.GwListData;
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
import com.yyz.cyuanw.bean.MessageData;
import com.yyz.cyuanw.bean.PreparationData;
import com.yyz.cyuanw.bean.PublicCarData;
import com.yyz.cyuanw.bean.SettingData;
import com.yyz.cyuanw.bean.ShopInfo;
import com.yyz.cyuanw.bean.ShopListData;
import com.yyz.cyuanw.bean.VersionInfo;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.view.sortrecyclerview.SortModel;

import java.util.List;

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

    public void getShopInfo(int id,Observer<HttpResult<ShopInfo>> observer){
        Observable observable = service.getShopInfo(Constant.API_SERVER+"/shopUserInfo/"+id);
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

    public void getMessageList(int page,String token,Observer<HttpResult<MessageData>> observer){
        Observable observable = service.getMessageList(page,token);
        setSubscribe(observable, observer);
    }

    public void getApplicationList(int status,int page,String token,Observer<HttpResult<GwListData>> observer){
        Observable observable = service.getApplicationList(status,page,token);
        setSubscribe(observable, observer);
    }

    public void setManage(int id,String token,Observer<HttpCodeResult> observer){
        Observable observable = service.setManage(id,token);
        setSubscribe(observable, observer);
    }

    public void delBroker(int id,String token,Observer<HttpCodeResult> observer){
        Observable observable = service.delBroker(id,token);
        setSubscribe(observable, observer);
    }

    public void cancelDealerManager(int id,String token,Observer<HttpCodeResult> observer){
        Observable observable = service.cancelDealerManager(id,token);
        setSubscribe(observable, observer);
    }

    public void auditBroker(int id,int status,String token,Observer<HttpCodeResult> observer){
        Observable observable = service.auditBroker(id,status,token);
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

    public void setAddress(int provice,int city,int region,String address,String token,Observer<HttpCodeResult> observer){
        Observable observable = service.setAddress(provice,city,region,address,token);
        setSubscribe(observable, observer);
    }

    public void getShopList(int type,int id,int page,Observer<HttpResult<ShopListData>> observer){
        Observable observable = null;
        switch (type){
            case 0:
                observable = service.getSelfCarList(id,page,App.get(Constant.KEY_USER_TOKEN));
                break;
            case 1:
                observable = service.getShareCarList(id,page,App.get(Constant.KEY_USER_TOKEN));
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

    public void dealerEnter(RequestBody body, Observer<HttpCodeResult> observer){
        Observable observable = service.dealerEnter(body);
        setSubscribe(observable, observer);
    }

    public void dealerStatus(String token, Observer<HttpResult<CarStatusData>> observer){
        Observable observable = service.dealerStatus(token);
        setSubscribe(observable, observer);
    }

    public void relationDealerStatus(String token, Observer<HttpResult<DealerData>> observer){
        Observable observable = service.relationDealerStatus(token);
        setSubscribe(observable, observer);
    }

    public void unRelationDealer(String token, Observer<HttpCodeResult> observer){
        Observable observable = service.unRelationDealer(token);
        setSubscribe(observable, observer);
    }

    public void relation(String id,String token, Observer<HttpCodeResult> observer){
        Observable observable = service.relation(id,token);
        setSubscribe(observable, observer);
    }

    public void dealer(String word,int page,String token, Observer<HttpResult<CarData>> observer){
        Observable observable = service.dealer(word,page,token);
        setSubscribe(observable, observer);
    }

    public void certificationBroker(RequestBody body, Observer<HttpCodeResult> observer){
        Observable observable = service.certificationBroker(body);
        setSubscribe(observable, observer);
    }

    public void changeCarSource(String token,int source,Observer<HttpCodeResult> observer){
        Observable observable = service.changeCarSource(token,source);
        setSubscribe(observable, observer);
    }

    public void logout(String token,Observer<HttpCodeResult> observer){
        Observable observable = service.logout(token);
        setSubscribe(observable, observer);
    }

    public void getCardInfo(String token,Observer<HttpResult<CardIDData>> observer){
        Observable observable = service.getCardInfo(token);
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

    public void getJjrData(String longitude, String latitude,int page, Observer<HttpResult<JjrResultData>> observer) {
        Observable observable = service.getJjrData(longitude, latitude,page);
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

    public void publishCar(String token,int is_share,int is_new,String name, String position_number,
                         String cover, List<String> images,String vin_code,int brand_id,
                         int series_id, int styles_id, String license_plate_time,double mileage,
                           String color,String gearbox,String fuel_type,String emission_standard,String displacement,
                           String car_style,String annual_inspection_expiry_time,String insurance_expiry_time,double retail_offer,double wholesale_offer,
                           double floor_price,String describe,String share_cover,String share_describe,List<String> share_images,Observer<HttpCodeResult> observer) {

        Observable observable = service.publishCar(token,is_share, is_new, name, position_number, cover, images, vin_code,brand_id,
                series_id,styles_id,license_plate_time,mileage,color,gearbox,fuel_type,emission_standard,displacement,car_style,annual_inspection_expiry_time,insurance_expiry_time,retail_offer,wholesale_offer,
                floor_price,describe,share_cover,share_describe,share_images);
        setSubscribe(observable, observer);
    }

    public void editCarInfo(String token,int id,int is_share,int is_new,String name, String position_number,
                           String cover, List<String> images,String vin_code,int brand_id,
                           int series_id, int styles_id, String license_plate_time,double mileage,
                           String color,String gearbox,String fuel_type,String emission_standard,String displacement,
                           String car_style,String annual_inspection_expiry_time,String insurance_expiry_time,double retail_offer,double wholesale_offer,
                           double floor_price,String describe,String share_cover,String share_describe,List<String> share_images,Observer<HttpCodeResult> observer) {

        Observable observable = service.editCarInfo(token,id,is_share, is_new, name, position_number, cover, images, vin_code,brand_id,
                series_id,styles_id,license_plate_time,mileage,color,gearbox,fuel_type,emission_standard,displacement,car_style,annual_inspection_expiry_time,insurance_expiry_time,retail_offer,wholesale_offer,
                floor_price,describe,share_cover,share_describe,share_images);
        setSubscribe(observable, observer);
    }

    public void getCarInfo(int id,String token, Observer<HttpResult<PublicCarData>> observer) {
        Observable observable = service.getCarInfo(id,token);
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
    public void searchCy(int page, List<Integer> province_id, List<Integer> city_id, int region_id, int source, int order, int min_price, int max_price, int brand_id, int series_id, int color, int gearbox, int emission_standard ,
                         int  fuel_type , int  min_year , int  max_year , int min_mileage, int max_mileage, String key_word, Observer<HttpResult<Data1>> observer) {
        Observable observable = service.searchCy(page,province_id,city_id,region_id,source,order,min_price,max_price,brand_id,series_id,color,gearbox, emission_standard , fuel_type ,
                min_year , max_year , min_mileage, max_mileage,key_word,0,App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }
    public void searchCyGl(int type,String key_word,int page,Observer<HttpResult<Data1>> observer) {
        Observable observable = service.searchCyGl(type,key_word,page,App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }

    public void getSettingData(Observer<HttpResult<SettingData>> observer) {
        Observable observable = service.getSettingData();
        setSubscribe(observable, observer);
    }

    public void collection_list(String key_word,int page,Observer<HttpResult<Data1>> observer) {
        Observable observable = service.collection_list(key_word,page,App.get(Constant.KEY_USER_TOKEN));
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

    public void seriesNext(int id,Observer<ResponseBody> observer) {
        Observable observable = service.seriesNext(App.get(Constant.KEY_USER_TOKEN),id);
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

    public void carInfo(int id,int type,Observer<HttpResult<Data6>> observer) {
        Observable observable = service.carInfo(App.get(Constant.KEY_USER_TOKEN),id,type);
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

    public void getPreparation(int id,Observer<HttpResult<PreparationData>> observer) {

        Observable observable = service.getPreparation("getPreparation/"+id,App.get(Constant.KEY_USER_TOKEN));
        setSubscribe(observable, observer);
    }

    public void preparation(int id,double price1,double price2,String content,Observer<HttpCodeResult> observer) {
        Observable observable = service.preparation(id,price1,price2,content,App.get(Constant.KEY_USER_TOKEN));
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

    public void checkVersion(String version,Observer<HttpResult<VersionInfo>> observer) {
        Observable observable = service.checkVersion(version);
        setSubscribe(observable, observer);
    }

    public void refreshCar(int id,String token,Observer<HttpCodeResult> observer) {
        Observable observable = service.refreshCar(id,token);
        setSubscribe(observable, observer);
    }
    public void delCar(int id,String token,Observer<HttpCodeResult> observer) {
        Observable observable = service.delCar(id,token);
        setSubscribe(observable, observer);
    }
    public void updateSold(int id,String token,Observer<HttpCodeResult> observer) {
        Observable observable = service.updateSold(id,token);
        setSubscribe(observable, observer);
    }
    public void updateUrgent(int id,String token,Observer<HttpCodeResult> observer) {
        Observable observable = service.updateUrgent(id,token);
        setSubscribe(observable, observer);
    }
    public void wholesale(int id,double price,String token,Observer<HttpCodeResult> observer) {
        Observable observable = service.wholesale(id,price,token);
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