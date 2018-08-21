package com.yyz.cyuanw.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data6;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.tools.ImageTools;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.ToastUtil;
import com.yyz.cyuanw.tools.Tools;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
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
    @BindView(R.id.see_all_cy)
    TextView see_all_cy;
    @BindView(R.id.to_wx)
    TextView to_wx;
    @BindView(R.id.imgs)
    LinearLayout imgs;


    private int cy_id;

    public Banner banner;
    private Data6 data6;

    private Dialog mDialog;

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
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

      /*  ImageView imageView = dialogView.findViewById(R.id.id_iv_code);
        final EditText codeView = dialogView.findViewById(R.id.id_et_code);

        dialogView.findViewById(R.id.id_iv_close).setOnClickListener(view -> mDialog.dismiss() );
        imageView.setImageBitmap(ImageTools.base64ToBitmap(imageData));
        imageView.setOnClickListener(view -> getImgCode(phoneView.getText().toString(),imageView));
        dialogView.findViewById(R.id.id_tv_submit).setOnClickListener(view -> {
            String strCode = codeView.getText().toString().trim();
            if (!StringUtil.isNotNull(strCode)){
                App.showToast("请输入图形验证码");
                return;
            }
            getValidateCode(phoneView.getText().toString(),strCode);
            mDialog.dismiss();
        });*/

    }

    @Override
    public void initView() {
        title.setText("车源详情");
        right_icon.setImageResource(R.mipmap.img_25);
        right_icon.setVisibility(View.VISIBLE);
        banner = (Banner) findViewById(R.id.banner);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Img.load(imageView, path.toString());
            }
        });

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
        cy_id = getIntent().getIntExtra("id", 0);
        carInfo(cy_id);
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
        pfbz.setText(data6.emission_standard);
        bsx.setText(data6.gearbox);
        pl.setText(data6.displacement);
        rylx.setText(data6.fuel_type);
        csys.setText(data6.color);
        desc.setText(data6.describe);
        adapterImgs(data6.images);

        setSc(data6.collection);

    }

    public void setSc(int flag) {
        if (flag == 1) {//是否收藏 0否 1是
            sc_tv.setText("取消收藏");
        } else {
            sc_tv.setText("收藏该车源");
        }
    }

    private void adapterImgs(List<String> urls) {
        if (null == urls) return;
        for (int i = 0; i < urls.size(); i++) {
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Tools.dip2px(this, 240));
            imgs.addView(iv, llp);
            Img.load(iv, urls.get(i));
        }
    }

    @OnClick({R.id.sc, R.id.lx, R.id.see_all_cy, R.id.to_wx})
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
                showImgDialog();
                break;
            case R.id.see_all_cy:

                break;
            case R.id.to_wx:

                break;
        }
    }

    public void carInfo(int id) {
        HttpData.getInstance().carInfo(id, new Observer<HttpResult<Data6>>() {
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
}
