package com.yyz.cyuanw.activity.user_model;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class UserActivity extends BaseActivity{

    @BindView(R.id.id_iv_back) ImageView backView;

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


