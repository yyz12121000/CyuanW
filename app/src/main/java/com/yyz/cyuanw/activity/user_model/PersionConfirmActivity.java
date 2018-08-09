package com.yyz.cyuanw.activity.user_model;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class PersionConfirmActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_carset) RelativeLayout carView;

    private TextView tvCarView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_persionconfirm;
    }

    @Override
    public void initView() {
        setTitle(titleView,"经纪人认证");
        ((TextView)carView.getChildAt(0)).setText("关联车商");
        tvCarView = (TextView) carView.getChildAt(1);

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_oper_carset})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_carset:

                break;
        }
    }

}


