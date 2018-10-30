package com.yyz.cyuanw.activity.user_model;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.MainActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

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
                    tvPersionView.setText("认证中");
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
                    tvCarconfirmView.setText("认证中");
                    break;
            }

        }
    }

    @OnClick({R.id.id_oper_bindphone,R.id.id_oper_nameconfirm,R.id.id_oper_persionconfirm,R.id.id_oper_carconfirm,R.id.id_oper_bindcar})
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

                Intent intent2 = new Intent(this,NameConfirmActivity.class);
                intent2.putExtra("name",userData.name);
                startActivityForResult(intent2,10);
                break;
            case R.id.id_oper_persionconfirm:

                if(StringUtil.isNotNull(userData.name)){
                    Intent intent3 = new Intent(this,PersionConfirmActivity.class);
                    intent3.putExtra("status",userData.is_broker);
                    startActivityForResult(intent3,20);
                }else{
                    App.showToast("请先实名认证");
                }
                break;
            case R.id.id_oper_carconfirm:

                if(StringUtil.isNotNull(userData.name)){
                    if (userData.have_dealer == 1){
                        App.showToast("您已经有绑定的车商，无法创建车商");
                    }else{
                        Intent intent4 = new Intent(this,CarConfirmActivity.class);
                        startActivityForResult(intent4,21);
                    }
                }else{
                    App.showToast("请先实名认证");
                }
                break;
            case R.id.id_oper_bindcar:

                if(StringUtil.isNotNull(userData.name)){
                    Intent intent4 = new Intent(this,CarRelationActivity.class);
                    startActivityForResult(intent4,22);
                }else{
                    App.showToast("请先实名认证");
                }
                break;
        }
    }

    public void startActivity(Class<?> cls){
        startActivity(new Intent(this,cls));
    }

    public void getUserInfo() {

        HttpData.getInstance().getUserInfo(App.get(Constant.KEY_USER_TOKEN), new Observer<HttpResult<LoginData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<LoginData> result) {
                if (result.status == 200 && result.data != null) {
                    App.set(Constant.KEY_USER_DATA, new Gson().toJson(result.data));
                    initData();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (resultCode == 10){
//            tvNameView.setText("已认证");
//        }
//
//        if (resultCode == 20){
//            tvPersionView.setText("认证中");
//        }
//
//        if (resultCode == 21){
//            tvCarconfirmView.setText("认证中");
//        }

        getUserInfo();
    }
}


