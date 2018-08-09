package com.yyz.cyuanw.activity.user_model;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class PhoneBindActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phonebind;
    }

    @Override
    public void initView() {
        setTitle(titleView,"绑定手机");

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_btn_bind})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_btn_bind:

                break;
        }
    }

}


