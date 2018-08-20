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
import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.GlideCircleTransformWithBorder;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class UserActivity extends BaseActivity{

    @BindView(R.id.id_iv_back) ImageView backView;
    @BindView(R.id.id_iv_photo) ImageView photoView;
    @BindView(R.id.id_iv_bg) ImageView bgView;
    @BindView(R.id.id_iv_modify) ImageView modifyView;
    @BindView(R.id.id_iv_confirm) ImageView confirmView;
    @BindView(R.id.id_iv_persion) ImageView persionView;
    @BindView(R.id.id_tv_name) TextView nameView;
    @BindView(R.id.id_tv_no) TextView noView;
    @BindView(R.id.id_tv_shop) TextView shopView;
    @BindView(R.id.id_tv_sign) TextView signView;
    @BindView(R.id.id_tv_mycar) TextView carNumView;
    @BindView(R.id.id_tv_myshop) TextView shopNumView;
    @BindView(R.id.id_tv_myunion) TextView unionNumView;

    private LoginData userBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    public void initView() {

        mImmersionBar.reset().transparentBar().init();

    }

    @Override
    public void initData() {

        String jsonStr = App.get(Constant.KEY_USER_DATA);
        if (StringUtil.isNotNull(jsonStr)){
            userBean = new Gson().fromJson(jsonStr,LoginData.class);

            if (StringUtil.isNotNull(userBean.pic))
                Img.loadC(photoView,userBean.pic);

            if (StringUtil.isNotNull(userBean.bg_image))
                Img.loadP(bgView,userBean.bg_image,R.mipmap.bg_user);

            if (StringUtil.isNotNull(userBean.name)){
                nameView.setText(userBean.name);
            }else {
                nameView.setText(userBean.phone);
            }

            if (StringUtil.isNotNull(userBean.bind_dealer)){
                shopView.setText(userBean.bind_dealer);
            }else {
                shopView.setText("暂时还未绑定车商");
            }

            if (StringUtil.isNotNull(userBean.signature)){
                signView.setText(userBean.signature);
            }else {
                signView.setText("暂时还没有个性签名");
            }

            carNumView.setText(getString(R.string.str_user_mycar,userBean.my_cars));
            shopNumView.setText(getString(R.string.str_user_myshop,userBean.my_shops));
            unionNumView.setText(getString(R.string.str_user_myunion,userBean.my_union));

        }
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


