package com.yyz.cyuanw.activity.user_model;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.tools.Tools;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_phone) RelativeLayout phoneView;
//    @BindView(R.id.id_oper_check) RelativeLayout checkView;
    @BindView(R.id.id_tv_version) TextView versionView;

//    private TextView versionStateView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        setTitle(titleView,"关于车缘");
        ((TextView)phoneView.getChildAt(0)).setText("联系客服");
        ((TextView)phoneView.getChildAt(1)).setText("0574-88273310");
//        ((TextView)checkView.getChildAt(0)).setText("检查新版本");
//        versionStateView = (TextView) checkView.getChildAt(1);
//        versionStateView.setText("已是最新版本");

        versionView.setText("版本号 V"+Tools.getVerName(this));
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_oper_phone})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_phone:

                Tools.openSysPhone(this,"0574-88273310");
                break;
//            case R.id.id_oper_check:
//
//                break;
        }
    }

}


