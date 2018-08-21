package com.yyz.cyuanw.activity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data6;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
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
    @BindView(R.id.imgs)
    LinearLayout imgs;


    private int cy_id;

    public Banner banner;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cy_detail;
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
    }

    private void adapterImgs(List<String> urls) {
        if (null == urls) return;
        for (int i = 0; i < urls.size(); i++) {
            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Tools.dip2px(this, 240));
            imgs.addView(iv,llp);
            Img.load(iv, urls.get(i));
        }
    }

    @OnClick({R.id.sc, R.id.lx})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.sc:

                break;
            case R.id.lx:

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
                    setData(result.data);
                } else {

                }
            }
        });
    }
}
