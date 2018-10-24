package com.yyz.cyuanw.activity.user_model;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.SettingData;
import com.yyz.cyuanw.tools.Tools;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class AboutActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_phone) RelativeLayout phoneView;
    //    @BindView(R.id.id_oper_check) RelativeLayout checkView;
    @BindView(R.id.id_tv_version) TextView versionView;

    private TextView tvPhoneView;

    private SettingData data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        setTitle(titleView,"关于车缘");
        ((TextView)phoneView.getChildAt(0)).setText("联系客服");
        tvPhoneView = ((TextView)phoneView.getChildAt(1));
//        ((TextView)checkView.getChildAt(0)).setText("检查新版本");
//        versionStateView = (TextView) checkView.getChildAt(1);
//        versionStateView.setText("已是最新版本");

        versionView.setText("版本号 V"+Tools.getVerName(this));
    }

    @Override
    public void initData() {
        getData();
    }

    @OnClick({R.id.id_oper_phone})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_phone:

                if (data != null)
                    Tools.openSysPhone(this,data.customer_service_phone);
                break;
//            case R.id.id_oper_check:
//
//                break;
        }
    }

    public void getData(){

        HttpData.getInstance().getSettingData(new Observer<HttpResult<SettingData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<SettingData> result) {

                if (result.status == 200 && result.data != null){
                    data = result.data;

                    tvPhoneView.setText(data.customer_service_phone);

                }

            }
        });
    }

}


