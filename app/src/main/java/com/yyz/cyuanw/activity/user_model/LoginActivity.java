package com.yyz.cyuanw.activity.user_model;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity{

    @BindView(R.id.id_iv_back) ImageView backView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_iv_back,R.id.id_btn_login})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_iv_back:

                finish();
                break;
            case R.id.id_btn_login:

                startActivity(new Intent(this, UserActivity.class));
                break;
        }
    }

}


