package com.yyz.cyuanw.activity.user_model;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SendCarActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.title_right_text) TextView sendView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sendcar;
    }

    @Override
    public void initView() {
        setTitle(titleView,"发布车源");
        sendView.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {

    }

//    @OnClick({R.id.id_oper_phone,R.id.id_oper_check})
//    public void onClickEvent(View view){
//        switch (view.getId()){
//            case R.id.id_oper_phone:
//
//                break;
//            case R.id.id_oper_check:
//
//                break;
//        }
//    }

}


