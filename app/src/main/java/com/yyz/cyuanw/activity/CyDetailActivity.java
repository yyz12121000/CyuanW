package com.yyz.cyuanw.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.user_model.MyShopActivity;
import com.yyz.cyuanw.activity.user_model.UserActivity;
import com.yyz.cyuanw.activity.user_model.UserInfoActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data6;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.tools.ImageTools;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LQRPhotoSelectUtils;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.ToastUtil;
import com.yyz.cyuanw.tools.Tools;
import com.yyz.cyuanw.tools.WX;
import com.yyz.cyuanw.view.CommonPopupDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionGen;
import rx.Observer;

public class CyDetailActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView title;
    @BindView(R.id.title_right_icon)
    ImageView right_icon;
    @BindView(R.id.sc_iv)
    ImageView sc_iv;

    @BindView(R.id.title)
    TextView title_;
    @BindView(R.id.bh)
    TextView bh;
    @BindView(R.id.jg)
    TextView jg;
    @BindView(R.id.id_iv_j)
    ImageView iv_j;
    @BindView(R.id.id_iv_p)
    ImageView iv_p;
    @BindView(R.id.rq)
    TextView rq;
    @BindView(R.id.gl)
    TextView gl;
    @BindView(R.id.cprq)
    TextView cprq;
    @BindView(R.id.szd)
    TextView szd;
    @BindView(R.id.pfbz)
    TextView pfbz;
    @BindView(R.id.bsx)
    TextView bsx;
    @BindView(R.id.pl)
    TextView pl;
    @BindView(R.id.rylx)
    TextView rylx;
    @BindView(R.id.csys)
    TextView csys;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.sc_tv)
    TextView sc_tv;
    @BindView(R.id.cx)
    TextView cx;
    @BindView(R.id.lxly)
    TextView lxly;
    /*  @BindView(R.id.see_all_cy)
      TextView see_all_cy;
      @BindView(R.id.to_wx)
      TextView to_wx;*/
    @BindView(R.id.imgs)
    LinearLayout imgs;


    private int cy_id;
    private int from_type;
    private int intent_flag;// 1 共享车源

    public Banner banner;
    private Data6 data6;

    private Dialog mDialog;

    private WX wx;

    private WebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cy_detail;
    }

    public void showImgDialog() {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(this, R.style.MyDialog).create();
        }
        mDialog.show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_llcy, null);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setContentView(dialogView);

        if (data6 != null){
            ((TextView)dialogView.findViewById(R.id.id_tv_name)).setText(data6.user.name);
            ((TextView)dialogView.findViewById(R.id.id_tv_address)).setText(data6.user.bind_dealer);
            ((TextView)dialogView.findViewById(R.id.id_tv_phone)).setText(data6.user.phone);
            ((TextView)dialogView.findViewById(R.id.id_tv_shortphone)).setText(data6.user.virtual_number);
            ((TextView)dialogView.findViewById(R.id.id_tv_price1)).setText(data6.retail_offer+"万");
            ((TextView)dialogView.findViewById(R.id.id_tv_price2)).setText("底价: "+data6.retail_bottom_price+"万");

            dialogView.findViewById(R.id.see_all_cy).setOnClickListener(view -> {
                Intent intent = new Intent(CyDetailActivity.this, MyShopActivity.class);
                intent.putExtra("id",data6.user.id);
                startActivity(intent);

                mDialog.dismiss();
            });

            dialogView.findViewById(R.id.to_wx).setOnClickListener(view -> {
                showPopupDialog();

                mDialog.dismiss();
            });
        }

    }

    @Override
    public void initView() {
        setSwipeBackEnable(false);
        cy_id = getIntent().getIntExtra("id", 0);
        from_type = getIntent().getIntExtra("from_type", 0);
        intent_flag = getIntent().getIntExtra("flag",0);
        if (intent_flag != 1){
            right_icon.setImageResource(R.mipmap.img_25);
            right_icon.setVisibility(View.VISIBLE);
        }
        title.setText("车源详情");
        if (from_type == 3 || from_type == 4 || from_type == 6){
            lxly.setText("拨打电话");
        }
        banner = (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Img.loadD(imageView, path.toString(),R.mipmap.ic_cybg);
            }
        });

        wx = new WX();

        webView = new WebView(getApplicationContext());
        imgs.addView(webView);

        WebSettings settings = webView.getSettings();
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()+"/webcache";
        settings.setDatabasePath(cacheDirPath);
        settings.setAppCachePath(cacheDirPath);
        settings.setAppCacheEnabled(true);

    }

    public void startBanner(List<String> imgs) {
        //设置图片集合
        banner.setImages(imgs);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
//                banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(false);
        //设置指示器位置（当banner模式中有指示器时）
//        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void initData() {
        carInfo(cy_id,from_type);
    }

    private void setData(Data6 data6) {
        startBanner(data6.images);
        title_.setText(data6.name);

        bh.setText("车源编号：" + data6.car_number);
        jg.setText(data6.retail_offer + "万元");
        rq.setText(data6.pageviews + "人浏览\n" + data6.publish_time);
        gl.setText(data6.mileage);
        cprq.setText(data6.license_plate_time);
        szd.setText(data6.location);
        pfbz.setText(StringUtil.isNotNull(data6.emission_standard) ? data6.emission_standard : "-");
        bsx.setText(StringUtil.isNotNull(data6.gearbox) ? data6.gearbox : "-");
        pl.setText(StringUtil.isNotNull(data6.displacement) ? data6.displacement : "-");
        rylx.setText(StringUtil.isNotNull(data6.fuel_type) ? data6.fuel_type : "-");
        csys.setText(StringUtil.isNotNull(data6.color) ? data6.color : "-");
        desc.setText(StringUtil.isNotNull(data6.describe) ? data6.describe : "暂无");
        cx.setText(data6.car_style > 0 ? data6.car_style_string : "-");
        iv_j.setVisibility(data6.urgent == 1 ? View.VISIBLE : View.GONE);
        iv_p.setVisibility(data6.wholesale == 1 ? View.VISIBLE : View.GONE);

        adapterImgs(data6.images);
        setSc(data6.collection);
    }

    public void setSc(int flag) {
        if (flag == 1) {//是否收藏 0否 1是
            sc_tv.setText("取消收藏");
            sc_iv.setImageResource(R.mipmap.img_90);
        } else {
            sc_tv.setText("收藏该车源");
            sc_iv.setImageResource(R.mipmap.img_27);
        }
    }

    private void adapterImgs(List<String> urls) {
        if (null == urls) return;
//        for (int i = 0; i < urls.size(); i++) {
//            ImageView iv = new ImageView(this);
//            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT );//Tools.dip2px(this, 240)
//            imgs.addView(iv, llp);
//            Img.loadD(iv, urls.get(i),R.mipmap.ic_cybg);
//        }
        if (urls.size() > 0) {
            webView.loadDataWithBaseURL(null,StringUtil.getCarDetailHtml(urls),"text/html","utf-8", null);
        }
    }

    @OnClick({R.id.sc, R.id.lx, /*R.id.see_all_cy, R.id.to_wx,*/ R.id.title_right_icon})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.sc:
                if (null == data6) {
                    ToastUtil.show(CyDetailActivity.this, "没有找到车源");
                }
                if (data6.collection == 1) {
                    rmCollections(data6.id);
                } else {
                    collections(data6.id);
                }
                break;
            case R.id.lx:

                if (data6 != null){
                    if (from_type == 3 || from_type == 4 || from_type == 6){
                        Tools.openSysPhone(this, data6.user.phone);
                    }else{
                        if (data6.power == 1){
                            showImgDialog();
                        }else{
                            App.showToast(data6.power_message);
                        }
                    }
                }
                break;
          /*  case R.id.see_all_cy:

                break;
            case R.id.to_wx:

                break;*/
            case R.id.title_right_icon:
                showPopupDialog();
                break;
        }
    }

    public void carInfo(int id,int type) {
        HttpData.getInstance().carInfo(id,type, new Observer<HttpResult<Data6>>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                LogManager.e("解析出错" + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Data6> result) {
                if (result.status == 200) {
                    data6 = result.data;
                    setData(result.data);
                } else {

                }
            }
        });
    }

    public void collections(int car_resources_id) {
        HttpData.getInstance().collections(car_resources_id, new Observer<HttpListResult<String>>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.show(CyDetailActivity.this, "收藏失败");
            }

            @Override
            public void onNext(HttpListResult<String> result) {
                if (result.status == 200) {
                    ToastUtil.show(CyDetailActivity.this, "收藏成功！");
                    data6.collection = 1;
                    setSc(data6.collection);
                } else {
                    ToastUtil.show(CyDetailActivity.this, "收藏失败");
                }
            }
        });
    }

    public void rmCollections(int car_resources_id) {
        HttpData.getInstance().rmCollections(car_resources_id, new Observer<HttpListResult<String>>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.show(CyDetailActivity.this, "取消收藏失败");
            }

            @Override
            public void onNext(HttpListResult<String> result) {
                if (result.status == 200) {
                    ToastUtil.show(CyDetailActivity.this, "取消收藏成功！");
                    data6.collection = 0;
                    setSc(data6.collection);
                } else {
                    ToastUtil.show(CyDetailActivity.this, "取消收藏失败");
                }
            }
        });
    }


    private View popupDialogView;
    private CommonPopupDialog mPopupDialog;

    public void showPopupDialog() {
        if (null == data6){
            ToastUtil.show(this,"没找到车源数据");
            return;
        }
        if (popupDialogView == null) {
            popupDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_share_wx, null);

            popupDialogView.findViewById(R.id.id_root_view).setOnClickListener(view -> mPopupDialog.dismiss());


            popupDialogView.findViewById(R.id.wx_1).setOnClickListener(view -> {
                wx.share(false, data6.share_info.title, data6.share_info.content,  data6.share_info.img, data6.share_info.url);
                mPopupDialog.dismiss();
            });

            popupDialogView.findViewById(R.id.wx_2).setOnClickListener(view -> {
                wx.share(true,  data6.share_info.title, data6.share_info.content,  data6.share_info.img, data6.share_info.url);
                mPopupDialog.dismiss();
            });
        }

        if (mPopupDialog == null) {
            mPopupDialog = new CommonPopupDialog(this, android.R.style.Theme_Panel);
            mPopupDialog.setCanceledOnTouchOutside(true);
            mPopupDialog.setContentView(popupDialogView);
        }


        mPopupDialog.showAtLocation(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    @Override
    protected void onDestroy() {
        webView.removeAllViews();
        webView.destroy();
        webView = null;

        super.onDestroy();
    }
}
