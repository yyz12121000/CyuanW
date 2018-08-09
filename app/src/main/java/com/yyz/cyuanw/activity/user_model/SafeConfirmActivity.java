package com.yyz.cyuanw.activity.user_model;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SafeConfirmActivity extends BaseActivity{
    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_bindphone) RelativeLayout phoneView;
    @BindView(R.id.id_oper_nameconfirm) RelativeLayout nameView;
    @BindView(R.id.id_oper_persionconfirm) RelativeLayout persionView;
    @BindView(R.id.id_oper_bindcar) RelativeLayout carView;
    @BindView(R.id.id_oper_carconfirm) RelativeLayout carConfirmView;

    private TextView tvPhoneView,tvNameView,tvPersionView,tvCarView,tvCarconfirmView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_safeconfirm;
    }

    @Override
    public void initView() {
        setTitle(titleView,"安全认证");
        ((TextView)phoneView.getChildAt(0)).setText("绑定手机");
        ((TextView)nameView.getChildAt(0)).setText("实名认证");
        ((TextView)persionView.getChildAt(0)).setText("经纪人认证");
        ((TextView)carView.getChildAt(0)).setText("绑定车商");
        ((TextView)carConfirmView.getChildAt(0)).setText("车商认证");

        tvPhoneView = (TextView) phoneView.getChildAt(1);
        tvNameView = (TextView) nameView.getChildAt(1);
        tvPersionView = (TextView) persionView.getChildAt(1);
        tvCarView = (TextView) carView.getChildAt(1);
        tvCarconfirmView = (TextView) carConfirmView.getChildAt(1);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_oper_bindphone,R.id.id_oper_nameconfirm,R.id.id_oper_persionconfirm})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_bindphone:
                startActivity(PhoneBindActivity.class);
                break;
            case R.id.id_oper_nameconfirm:
                startActivity(NameConfirmActivity.class);
                break;
            case R.id.id_oper_persionconfirm:
                startActivity(PersionConfirmActivity.class);
                break;
        }
    }

    public void startActivity(Class<?> cls){
        startActivity(new Intent(this,cls));
    }

}


