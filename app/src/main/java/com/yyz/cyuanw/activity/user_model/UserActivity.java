package com.yyz.cyuanw.activity.user_model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.tools.GlideCircleTransformWithBorder;

import butterknife.BindView;
import butterknife.OnClick;

public class UserActivity extends BaseActivity{

    @BindView(R.id.id_iv_back) ImageView backView;
    @BindView(R.id.id_iv_photo) ImageView photoView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    public void initView() {

        mImmersionBar.reset().transparentBar().init();

        Glide.with(this).load("http://c.hiphotos.baidu.com/image/pic/item/f9198618367adab4b025268587d4b31c8601e47b.jpg")
                .apply(new RequestOptions().error(getResources().getDrawable(R.mipmap.ic_launcher))
                        .placeholder(R.mipmap.ic_launcher).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .transform(new GlideCircleTransformWithBorder(this,2, Color.parseColor("#ccffffff")))
                ).into(photoView);

//        RequestOptions mRequestOptions = RequestOptions.circleCropTransform().diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true).placeholder(R.mipmap.ic_launcher);
//
//        Glide.with(this).load("http://c.hiphotos.baidu.com/image/pic/item/f9198618367adab4b025268587d4b31c8601e47b.jpg").apply(mRequestOptions).into(photoView);

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_iv_back,R.id.id_tv_setting,R.id.id_tv_help})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_iv_back:

                finish();
                break;
            case R.id.id_tv_setting:

                startActivity(SettingActivity.class);
                break;
            case R.id.id_tv_help:

                startActivity(HelpActivity.class);
                break;
        }
    }

    public void startActivity(Class<?> cls){
        startActivity(new Intent(this,cls));
    }

}


