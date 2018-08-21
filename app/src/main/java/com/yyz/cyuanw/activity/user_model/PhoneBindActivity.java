package com.yyz.cyuanw.activity.user_model;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.tools.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PhoneBindActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_iv_image) ImageView imageView;
    @BindView(R.id.id_tv_content) TextView contentView;
    @BindView(R.id.id_btn_bind) Button bindView;

    private String phone;

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
        phone = getIntent().getStringExtra("phone");
        if (StringUtil.isNotNull(phone)){
            imageView.setBackgroundResource(R.mipmap.ic_phonebind);
            contentView.setText("绑定的手机号："+phone);
            bindView.setText("更换手机号");
        }else{
            imageView.setBackgroundResource(R.mipmap.ic_phoneunbind);
            contentView.setText("您当前还未绑定手机号哦！");
            bindView.setText("立即绑定");
        }
    }

    @OnClick({R.id.id_btn_bind})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_btn_bind:

                if (StringUtil.isNotNull(phone)){
                    Intent intent = new Intent(this,PhoneChangeActivity.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }else{
                    startActivity(new Intent(this,PhoneSubmitActivity.class));
                }
                finish();
                break;
        }
    }

}


