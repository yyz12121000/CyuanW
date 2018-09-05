package com.yyz.cyuanw.activity.user_model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.MainActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.CardIDData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.view.CustomProgress;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class NameConfirmActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_et_name) EditText nameView;
    @BindView(R.id.id_et_no) EditText noView;
    @BindView(R.id.id_btn_confirm) Button btnView;

    private String name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nameconfirm;
    }

    @Override
    public void initView() {
        setTitle(titleView,"实名认证");
    }

    @Override
    public void initData() {

        name = getIntent().getStringExtra("name");
        if (StringUtil.isNotNull(name)){
            btnView.setVisibility(View.GONE);
            getCardInfo();
        }
    }

    @OnClick({R.id.id_btn_confirm})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_btn_confirm:

                String strName = nameView.getText().toString().trim();
                String strNo = noView.getText().toString().trim();

                if (StringUtil.isNotNull(strName) && StringUtil.isNotNull(strNo)){
                    nameConfirm(strName,strNo);
                }else{
                    App.showToast("请输入验证信息");
                }

                break;
        }
    }

    public void getCardInfo(){
        //CustomProgress.show(this, "请稍等...", false, null);

        HttpData.getInstance().getCardInfo(App.get(Constant.KEY_USER_TOKEN),new Observer<HttpResult<CardIDData>>() {
            @Override
            public void onCompleted() {
                //CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                //CustomProgress.dismis();
                App.showToast("服务器请求超时");
            }

            @Override
            public void onNext(HttpResult<CardIDData> result) {
                nameView.setText(result.data.real_name);
                noView.setText(result.data.id_card);
                nameView.setEnabled(false);
                noView.setEnabled(false);
            }
        });
    }

    public void nameConfirm(String name,String no){
        CustomProgress.show(this, "请稍等...", false, null);

        HttpData.getInstance().nameConfirm(name,no,App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {
            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
                App.showToast("服务器请求超时");
            }

            @Override
            public void onNext(HttpCodeResult result) {
                App.showToast(result.message);

                if (result.status == 200){
                    App.updataUserData = true;
                    setResult(10);
                    finish();
                }
            }
        });
    }

}


