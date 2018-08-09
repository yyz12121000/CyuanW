package com.yyz.cyuanw.activity.user_model;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivity{
    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_setphoto) RelativeLayout photoView;
    @BindView(R.id.id_oper_setbg) LinearLayout bgpicView;
    @BindView(R.id.id_oper_setname) RelativeLayout nameView;
    @BindView(R.id.id_oper_setsex) RelativeLayout sexView;
    @BindView(R.id.id_oper_setno) RelativeLayout noView;
    @BindView(R.id.id_oper_setshortno) RelativeLayout shortnoView;
    @BindView(R.id.id_oper_setcity) RelativeLayout cityView;
    @BindView(R.id.id_oper_setsign) RelativeLayout signView;

    private TextView tvNameView,tvSexView,tvNoView,tvShortNoView,tvCityView,tvSignView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initView() {
        setTitle(titleView,"个人信息");
        ((TextView)photoView.getChildAt(0)).setText("头像设置");
        ((TextView)bgpicView.getChildAt(0)).setText("背景图片设置");
        ((TextView)nameView.getChildAt(0)).setText("姓名");
        nameView.getChildAt(2).setVisibility(View.INVISIBLE);
        tvNameView = (TextView) nameView.getChildAt(1);
        ((TextView)sexView.getChildAt(0)).setText("性别");
        tvSexView = (TextView) nameView.getChildAt(1);
        ((TextView)noView.getChildAt(0)).setText("手机号");
        tvNoView = (TextView) nameView.getChildAt(1);
        ((TextView)shortnoView.getChildAt(0)).setText("短号");
        tvShortNoView = (TextView) nameView.getChildAt(1);
        ((TextView)cityView.getChildAt(0)).setText("城市");
        tvCityView = (TextView) nameView.getChildAt(1);
        ((TextView)signView.getChildAt(0)).setText("个性签名");
        tvSignView = (TextView) nameView.getChildAt(1);

        tvNameView.setText("黄劲");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_oper_setphoto,R.id.id_oper_setbg})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_setphoto:

                break;
            case R.id.id_oper_setbg:

                break;
        }
    }

}


