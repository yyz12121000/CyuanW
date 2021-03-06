package com.yyz.cyuanw.activity.user_model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
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
import com.yyz.cyuanw.activity.CyGlActivity;
import com.yyz.cyuanw.activity.WdLmActivity;
import com.yyz.cyuanw.activity.fragment.SyFragment;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.GlideCircleTransformWithBorder;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.view.CustomProgress;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class UserActivity extends BaseActivity {

    @BindView(R.id.id_iv_back)
    ImageView backView;
    @BindView(R.id.id_iv_photo)
    ImageView photoView;
    @BindView(R.id.id_iv_bg)
    ImageView bgView;
    @BindView(R.id.id_iv_modify)
    ImageView modifyView;
    @BindView(R.id.id_iv_confirm)
    ImageView confirmView;
    @BindView(R.id.id_iv_persion)
    ImageView persionView;
    @BindView(R.id.id_tv_name)
    TextView nameView;
    @BindView(R.id.id_tv_no)
    TextView noView;
    @BindView(R.id.id_tv_shop)
    TextView shopView;
    @BindView(R.id.id_tv_sign)
    TextView signView;
    @BindView(R.id.id_tv_mycar)
    TextView carNumView;
    @BindView(R.id.id_tv_myshop)
    TextView shopNumView;
    @BindView(R.id.id_tv_myunion)
    TextView unionNumView;

    private LoginData userData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (App.updataUserData) {
            App.updataUserData = false;
            getUserInfo();
        }
    }

    @Override
    public void initView() {
        mImmersionBar.reset().transparentBar().init();
    }

    @Override
    public void initData() {
        getUserInfo();
    }

    public void setViewData() {
        String jsonStr = App.get(Constant.KEY_USER_DATA);
        if (StringUtil.isNotNull(jsonStr)) {
            userData = new Gson().fromJson(jsonStr, LoginData.class);

            if (StringUtil.isNotNull(userData.pic))
                Img.loadC(photoView, userData.pic);

            if (StringUtil.isNotNull(userData.bg_image))
                Img.loadP(bgView, userData.bg_image, R.mipmap.bg_user);

            if (StringUtil.isNotNull(userData.name)) {
                nameView.setText(userData.name);
            } else {
                nameView.setText(userData.phone);
            }

            if (StringUtil.isNotNull(userData.bind_dealer)) {
                shopView.setText(userData.bind_dealer);
            } else {
                shopView.setText("暂时还未绑定车商");
            }

            if (StringUtil.isNotNull(userData.signature)) {
                signView.setText(userData.signature);
            } else {
                signView.setText("暂时还没有个性签名");
            }

            if (userData.is_broker == 1){
                confirmView.setVisibility(View.VISIBLE);
            }else {
                confirmView.setVisibility(View.GONE);
            }
            if (userData.is_adviser == 1){
                persionView.setVisibility(View.VISIBLE);
            }else {
                persionView.setVisibility(View.GONE);
            }

            if (Build.VERSION.SDK_INT >= 24){
                carNumView.setText(Html.fromHtml(getString(R.string.str_user_mycar,userData.my_cars),Html.FROM_HTML_MODE_COMPACT));
                shopNumView.setText(Html.fromHtml(getString(R.string.str_user_myshop, userData.my_shops),Html.FROM_HTML_MODE_COMPACT));
                unionNumView.setText(Html.fromHtml(getString(R.string.str_user_myunion,userData.my_union),Html.FROM_HTML_MODE_COMPACT));
            } else{
                carNumView.setText(Html.fromHtml(getString(R.string.str_user_mycar,userData.my_cars)));
                shopNumView.setText(Html.fromHtml(getString(R.string.str_user_myshop,userData.my_shops)));
                unionNumView.setText(Html.fromHtml(getString(R.string.str_user_myunion,userData.my_union)));
            }

            //shopNumView.setText(getString(R.string.str_user_myshop, userData.my_shops));
            //unionNumView.setText(getString(R.string.str_user_myunion, userData.my_union));
            unionNumView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(UserActivity.this, WdLmActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @OnClick({R.id.id_iv_back, R.id.id_tv_setting, R.id.id_tv_help,R.id.id_iv_modify,R.id.cygl,R.id.yggl,R.id.cysc,
            R.id.id_tv_mycar,R.id.id_iv_shop,R.id.id_tv_myshop,R.id.id_tv_message})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.id_iv_back:

                finish();
                break;
            case R.id.id_tv_setting:
                if (userData != null){
                    Intent intent = new Intent(this,SettingActivity.class);
                    intent.putExtra("carSource",userData.car_source);
                    startActivity(intent);
                }
                break;
            case R.id.id_tv_help:

                startActivity(HelpActivity.class);
                break;
            case R.id.id_tv_message:

                startActivity(MessageActivity.class);
                break;
            case R.id.id_iv_modify:

                startActivity(UserInfoActivity.class);
                break;
            case R.id.cygl:
            case R.id.id_tv_mycar:
                Intent intent1 = new Intent(UserActivity.this, CyGlActivity.class);
                startActivity(intent1);
                break;
            case  R.id.id_tv_myshop:
            case R.id.id_iv_shop:
                if (userData != null){
                    Intent intent2 = new Intent(UserActivity.this, MyShopActivity.class);
                    intent2.putExtra("id",userData.id);
                    startActivity(intent2);
                }
                break;
            case R.id.yggl:
                Intent intent3 = new Intent(UserActivity.this, YgglActivity.class);
                startActivity(intent3);
                break;
            case R.id.cysc:
                Intent intent4 = new Intent(UserActivity.this, CySCActivity.class);
                startActivity(intent4);
                break;
        }
    }

    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void getUserInfo() {
        //CustomProgress.show(this, "加载中...", false, null);

        HttpData.getInstance().getUserInfo(App.get(Constant.KEY_USER_TOKEN), new Observer<HttpResult<LoginData>>() {
            @Override
            public void onCompleted() {
                //CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                //CustomProgress.dismis();
                App.showToast("服务器请求超时");
            }

            @Override
            public void onNext(HttpResult<LoginData> result) {
                if (result.status == 200 && result.data != null) {
                    App.set(Constant.KEY_USER_DATA, new Gson().toJson(result.data));
                    setViewData();
                } else {
                    App.showToast(result.message);
                }
            }
        });
    }

}


