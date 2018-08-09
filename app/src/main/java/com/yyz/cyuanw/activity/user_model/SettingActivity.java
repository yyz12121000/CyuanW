package com.yyz.cyuanw.activity.user_model;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_userinfo) LinearLayout infoView;
    @BindView(R.id.id_oper_safe) LinearLayout safeView;
    @BindView(R.id.id_oper_carsource) LinearLayout carsourceView;
    @BindView(R.id.id_oper_clear) LinearLayout clearView;
    @BindView(R.id.id_oper_about) LinearLayout aboutView;
    @BindView(R.id.id_oper_exit) LinearLayout exitView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        setTitle(titleView,"设置");
        ((TextView)infoView.getChildAt(0)).setText("个人资料设置");
        ((TextView)safeView.getChildAt(0)).setText("安全验证");
        ((TextView)carsourceView.getChildAt(0)).setText("店铺车辆来源");
        ((TextView)clearView.getChildAt(0)).setText("清理缓存");
        ((TextView)aboutView.getChildAt(0)).setText("关于车缘");
        ((TextView)exitView.getChildAt(0)).setText("退出登录");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_oper_userinfo,R.id.id_oper_safe,R.id.id_oper_carsource,R.id.id_oper_clear,R.id.id_oper_about,R.id.id_oper_exit})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_userinfo:
                startActivity(UserInfoActivity.class);
                break;
            case R.id.id_oper_safe:
                startActivity(SafeConfirmActivity.class);
                break;
            case R.id.id_oper_carsource:
                finish();
                break;
            case R.id.id_oper_clear:
                finish();
                break;
            case R.id.id_oper_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.id_oper_exit:
                finish();
                break;
        }
    }

    public void startActivity(Class<?> cls){
        startActivity(new Intent(this,cls));
    }

}


