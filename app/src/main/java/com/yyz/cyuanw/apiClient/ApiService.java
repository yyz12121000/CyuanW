package com.yyz.cyuanw.apiClient;


import com.yyz.cyuanw.bean.AdData;
import com.yyz.cyuanw.bean.HotLmData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.ImgData;
import com.yyz.cyuanw.bean.LoginData;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
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
    Observable<HttpCodeResult> logout(@Header("token") String token,@Field("token") String empty);

    //获取短信验证码
    @FormUrlEncoded
    @POST("regSend")
    Observable<HttpCodeResult> getValidateCode(@Field("phone") String phone, @Field("image_code") String image_code);

    //获取热门联盟数据
    @GET("alliances/hot")
    Observable<HttpListResult<HotLmData>> getHotLmData();

    //获取热门联盟数据
    @GET("ad_positions/slideshow")
    Observable<HttpResult<AdData>> getAdData();

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

