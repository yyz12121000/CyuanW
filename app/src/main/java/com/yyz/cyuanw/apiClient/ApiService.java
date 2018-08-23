package com.yyz.cyuanw.apiClient;


import com.yyz.cyuanw.bean.AdData;
import com.yyz.cyuanw.bean.CheyListData;
import com.yyz.cyuanw.bean.CyListData;
import com.yyz.cyuanw.bean.Data1;
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
import com.yyz.cyuanw.view.sortrecyclerview.SortModel;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * API接口
 */
public interface ApiService {

    //用户登录
    @FormUrlEncoded
    @POST("login")
    Observable<HttpResult<LoginData>> login(@Field("phone") String phone, @Field("code") String code);

    //获取图片验证码
    @FormUrlEncoded
    @POST("captcha")
    Observable<HttpResult<ImgData>> getImgCode(@Field("phone") String phone);

    //注销登录
    @FormUrlEncoded
    @POST("logout")
    Observable<HttpCodeResult> logout(@Field("token") String token);

    //获取个人信息
    @FormUrlEncoded
    @POST("user")
    Observable<HttpResult<LoginData>> getUserInfo(@Field("token") String token);

    //实名验证
    @FormUrlEncoded
    @POST("realNameAuthentication")
    Observable<HttpCodeResult> nameConfrim(@Field("real_name") String real_name,@Field("id_num") String id_num,@Field("token") String token);

    //修改性别
    @FormUrlEncoded
    @POST("setGender")
    Observable<HttpCodeResult> setGender(@Field("gender") String gender,@Field("token") String token);

    //修改个性签名
    @FormUrlEncoded
    @POST("signature")
    Observable<HttpCodeResult> setSignature(@Field("signature") String signature,@Field("token") String token);

    //修改短号
    @FormUrlEncoded
    @POST("setVirtualNumber")
    Observable<HttpCodeResult> setVirtualNumber(@Field("virtual_number") String virtual_number,@Field("token") String token);

    //修改绑定手机短信验证码
    @FormUrlEncoded
    @POST("changeSend")
    Observable<HttpCodeResult> changeSend(@Field("phone") String phone,@Field("send_type") int type,@Field("token") String token);

    //修改绑定手机
    @FormUrlEncoded
    @POST("changePhone")
    Observable<HttpCodeResult> changePhone(@Field("phone") String phone,@Field("old_code") String oldcode,@Field("new_code") String newcode,@Field("token") String token);

    //修改用户头像
    @POST("setPic")
    Observable<HttpResult<ImgData>> setPic(@Body RequestBody Body);

    //修改背景图
    @POST("BGImage")
    Observable<HttpResult<ImgData>> setBgPic(@Body RequestBody Body);

    //经纪人认证
    @POST("certificationBroker")
    Observable<HttpResult<ImgData>> certificationBroker(@Body RequestBody Body);
    Observable<HttpCodeResult> logout(@Header("token") String token, @Field("token") String empty);

    //获取短信验证码
    @FormUrlEncoded
    @POST("regSend")
    Observable<HttpCodeResult> getValidateCode(@Field("phone") String phone, @Field("image_code") String image_code);

    //获取热门联盟数据
    @GET("alliances/hot")
    Observable<HttpListResult<HotLmData>> getHotLmData();

    //获取首页轮播图
    @GET("ad_positions/slideshow")
    Observable<HttpResult<AdData>> getAdData();

    //获取首页轮经纪人
    @FormUrlEncoded
    @POST("aroundBroker")
    Observable<HttpResult<JjrResultData>> getJjrData(@Field("longitude") String longitude, @Field("latitude") String latitude);

    //联盟列表
    @GET("alliances")
    Observable<HttpResult<LmListData>> getLmList(@Query("keyword") String keyword, @Query("page") int page);

    //我的联盟列表[全部or分页]
    @GET("alliances/me_all")
    Observable<HttpListResult<LmMyListData>> getLmMyList(@Query("is_paginate") int is_paginate);

    //创建联盟
    @FormUrlEncoded
    @POST("alliances")
    Observable<HttpResult> createLm(@Field("name") String name, @Field("logo") String logo,
                                    @Field("intro") String intro, @Field("province_id") int province_id,
                                    @Field("city_id") int city_id, @Field("region_id") int region_id, @Field("qr_code") String qr_code);

    @GET
    Observable<HttpResult<CyListData>> lmcylist(@Url String url);

    @GET
    Observable<HttpResult<CheyListData>> lmcheylist(@Url String url);

    @GET
    Observable<HttpResult<LmDetail>> lmDetail(@Url String url);


    @FormUrlEncoded
    @POST
    Observable<HttpListResult<String>> cancel_administrator(@Url String url, @Field("token") String token);

   @FormUrlEncoded
    @POST
    Observable<HttpListResult<String>> set_administrator(@Url String url, @Field("token") String token);


    //车源列表
    @FormUrlEncoded
    @POST("shareList")
    Observable<HttpResult<Data1>> searchCy(@Field("source") int source,
                                           @Field("order") int order,
                                           @Field("min_price") int min_price,
                                           @Field("max_price") int max_price,
                                           @Field("brand_id") int brand_id,
                                           @Field("series_id") int series_id,

                                           @Field("color") int color,
                                           @Field("gearbox") int gearbox,
                                           @Field("emission_standard") int emission_standard,
                                           @Field("fuel_type") int fuel_type,
                                           @Field("min_year") int min_year,
                                           @Field("max_year") int max_year,
                                           @Field("min_mileage") int min_mileage,
                                           @Field("max_mileage") int max_mileage,
                                           @Field("key_word") String key_word,
                                           @Field("is_self ") int is_self,
                                           @Field("token") String token
    );
    //共享车源筛选结果数量
    @FormUrlEncoded
    @POST("carScreeningCount")
    Observable<HttpResult<Data5>> carScreeningCount(@Field("source") int source,
                                                    @Field("order") int order,
                                                    @Field("min_price") int min_price,
                                                    @Field("max_price") int max_price,
                                                    @Field("brand_id") int brand_id,
                                                    @Field("series_id") int series_id,

                                                    @Field("color") int color,
                                                    @Field("gearbox") int gearbox,
                                                    @Field("emission_standard") int emission_standard,
                                                    @Field("fuel_type") int fuel_type,
                                                    @Field("min_year") int min_year,
                                                    @Field("max_year") int max_year,
                                                    @Field("min_mileage") int min_mileage,
                                                    @Field("max_mileage") int max_mileage,
                                                    @Field("key_word") String key_word,

                                                    @Field("is_self ") int is_self,
                                                    @Field("token") String token);

    //
    @FormUrlEncoded
    @POST("dictionary")
    Observable<HttpResult<Data3>> dictionary(@Field("token") String token, @Field("key_word") String key_word);

    @FormUrlEncoded
    @POST("dictionary")
    Observable<ResponseBody> dictionary2(@Field("token") String token, @Field("key_word") String key_word);

    @FormUrlEncoded
    @POST("brandNext")
    Observable<ResponseBody> brandNext(@Field("token") String token, @Field("id") int id);

    //
    @GET("top_brand")
    Observable<HttpListResult<SortModel>> top_brand(@Query("token") String token);
    //搜索筛选参数
    @GET("search_criteria")
    Observable<HttpResult<Data3>> search_criteria(@Query("token") String token);

    //车源详情接口
    @FormUrlEncoded
    @POST("carInfo")
    Observable<HttpResult<Data6>> carInfo(@Field("token") String token, @Field("id") int id);
    //收藏车源
    @FormUrlEncoded
    @POST("collections")
    Observable<HttpListResult<String>> collections(@Field("token") String token, @Field("car_resources_id") int car_resources_id);
    //移除收藏
    @DELETE()
    Observable<HttpListResult<String>> collections(@Field("token") String token, @Url String url);


    @Headers("Content-Type:application/x-www-form-urlencoded")
    @HTTP(method = "DELETE",  hasBody = true)
    @FormUrlEncoded
    Observable<HttpListResult<String>> rmCollections(@Field("token") String token, @Url String url);
//    Call<ResultInfo> setNotAttention(@Field("user_id") String user_id, @Field("token") String token, @Field("attu_id") String attu_id, @Field("type") String type);


//    //修改密码
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/getUpdatePad")
//    Observable<HttpResult<LoginData>> modifyPsw(@Field("password") String mobile, @Field("newpad") String password, @Field("token") String token, @Field("os") String os);
//
//    //用户退出
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/loginOut")
//    Observable<HttpResult<LoginData>> exit(@Field("token") String token, @Field("os") String os);
//

//
//    //获取验证码
//    @FormUrlEncoded
//    @POST("yycs/api/mobile/verificationCode")
//    Observable<HttpResult<LoginData>> getValidateCode(@Field("phone") String phone);
//
//    //找回密码
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/forgetPwd")
//    Observable<HttpResult<LoginData>> getPassword(@Field("loginName") String loginName, @Field("phone") String phone, @Field("code") String code, @Field("password") String password);
//
//    //首页数据
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/home")
//    Observable<HttpResult<HomeData>> getHomeData(@Field("token") String token, @Field("os") String os);
//
//    //交易订单-统计
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/ordersStatistics")
//    Observable<HttpListResult<OrderItem>> getOrderData(@Field("date") String date, @Field("token") String token, @Field("os") String os);
//
//    //交易订单-列表
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/ordersList")
//    Observable<HttpListResult<OrderItem>> getOrderDataList(@Field("date") String date, @Field("pageNo") String page, @Field("pageSize") String size, @Field("token") String token, @Field("os") String os);
//
//    //发票-列表
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/invoice")
//    Observable<HttpListResult<InvoiceItem>> getInvoiceDataList(@Field("hasInvoice") String hasInvoice, @Field("pageNo") String page, @Field("pageSize") String size, @Field("token") String token, @Field("os") String os);
//
//    //发票-订单列表
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/ordersForInvoice")
//    Observable<HttpListResult<OrderItem>> getInvoiceOrderDataList(@Field("invoiceRecordId") String id, @Field("pageNo") String page, @Field("pageSize") String size, @Field("token") String token, @Field("os") String os);
//
//    //发票-设为已开
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/settingHasInvoice")
//    Observable<HttpResult<OrderItem>> setHasInvoice(@Field("invoiceRecordId") String id, @Field("token") String token, @Field("os") String os);
//
//    //商品管理
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/getCommodityPrices")
//    Observable<HttpListResult<BusinessData>> getCommodityPrices(@Field("token") String token, @Field("os") String os);
//
//    //商品管理-修改
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/updateCommodityPrices")
//    Observable<HttpResult<OrderItem>> updateCommodityPrices(@Field("ids") String ids, @Field("prices") String prices, @Field("items") String items, @Field("token") String token, @Field("os") String os);
//
//    //财务对账
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/financialVerification")
//    Observable<HttpResult<FinanceData>> financialVerification(@Field("startTime") String startTime, @Field("endTime") String endTime, @Field("state") String state,
//                                                              @Field("page") String page, @Field("pageSize") String size, @Field("token") String token, @Field("os") String os);
//
//    //财务对账-明细列表
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/dayOrderList")
//    Observable<HttpListResult<OrderItem>> getFinanceDataList(@Field("date") String date, @Field("page") String page, @Field("pageSize") String size, @Field("token") String token, @Field("os") String os);
//
//    //关于
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/getAboutmeInfo")
//    Observable<HttpResult<AboutData>> getAboutInfo(@Field("type") String type, @Field("token") String token, @Field("os") String os);
//
//    //检测更新
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/getValidata")
//    Observable<HttpResult<UpdateData>> getUpdateInfo(@Field("type") String type, @Field("token") String token, @Field("os") String os);
//
//    //更新短信提醒
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/updateRemindPhone")
//    Observable<HttpResult<UpdateData>> getUpdatePhone(@Field("isMsg") String isMsg, @Field("phoneRemind") String phoneRemind, @Field("token") String token, @Field("os") String os);
//
//    //原手机号验证
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/appbeforePhone")
//    Observable<HttpResult<LoginData>> appBeforePhone(@Field("code") String code, @Field("token") String token, @Field("os") String os);
//
//    //新手机号验证
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/appnewPhone")
//    Observable<HttpResult<LoginData>> appNewPhone(@Field("phone") String phone, @Field("code") String code, @Field("token") String token, @Field("os") String os);
//
//    @FormUrlEncoded
//    @POST("yycs/merchant/inspection/getMyShopData")
//    Observable<HttpResult<MyShopData>> getMyShopData(@Field("token") String token, @Field("os") String os);
//
//    @POST("yycs/merchant/inspection/updateMyShopData")
//    Observable<HttpResult<LoginData>> updateMyShopData(@Body RequestBody Body);
//
//    @POST("yycs/merchant/inspection/uploadFile")
//    Observable<HttpListResult<ImagBean>> uploadFile(@Body RequestBody Body);

}

