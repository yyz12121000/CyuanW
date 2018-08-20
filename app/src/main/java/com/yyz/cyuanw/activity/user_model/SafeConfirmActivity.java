package com.yyz.cyuanw.activity.user_model;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.StringUtil;

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

    private LoginData userData;

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
        String jsonStr = App.get(Constant.KEY_USER_DATA);
        if (StringUtil.isNotNull(jsonStr)) {
            userData = new Gson().fromJson(jsonStr, LoginData.class);

            tvPhoneView.setText(StringUtil.isNotNull(userData.phone) ? "已绑定" : "未绑定");
            tvNameView.setText(StringUtil.isNotNull(userData.name) ? "已认证" : "未认证");
            tvCarView.setText(StringUtil.isNotNull(userData.bind_dealer) ? userData.bind_dealer : "未绑定");

            switch (userData.is_broker){
                case 0:
                    tvPersionView.setText("未认证");
                    break;
                case 1:
                    tvPersionView.setText("已认证");
                    break;
                case 2:
                    tvPersionView.setText("申请中");
                    break;
            }

            switch (userData.have_dealer){
                case 0:
                    tvCarconfirmView.setText("未入驻");
                    break;
                case 1:
                    tvCarconfirmView.setText("已入驻");
                    break;
                case 2:
                    tvCarconfirmView.setText("申请中");
                    break;
            }

        }
    }

    @OnClick({R.id.id_oper_bindphone,R.id.id_oper_nameconfirm,R.id.id_oper_persionconfirm})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_bindphone:

                Intent intent = new Intent(this,PhoneBindActivity.class);
                if (App.updataUserData){
                    intent.putExtra("phone",App.get(Constant.KEY_USER_PHONE));
                }else{
                    intent.putExtra("phone",userData.phone);
                }
                startActivity(intent);
                break;
            case R.id.id_oper_nameconfirm:
                if (!StringUtil.isNotNull(userData.name))
                    startActivity(NameConfirmActivity.class);
                break;
            case R.id.id_oper_persionconfirm:
                if (userData.is_broker == 0)
                    startActivity(PersionConfirmActivity.class);
                break;
        }
    }

    public void startActivity(Class<?> cls){
        startActivity(new Intent(this,cls));
    }

}


