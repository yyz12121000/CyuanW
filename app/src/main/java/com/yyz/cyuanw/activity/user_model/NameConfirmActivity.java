package com.yyz.cyuanw.activity.user_model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    @BindView(R.id.id_iv_icon)
    ImageView iconView;

    private String name,cardId;

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

        //name = getIntent().getStringExtra("name");
        //cardId = getIntent().getStringExtra("cardId");
//        if (StringUtil.isNotNull(name)){
//            btnView.setVisibility(View.GONE);
//            iconView.setVisibility(View.VISIBLE);
//            getCardInfo();
//        }
        getCardInfo();
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
                if (result.status == 200){
                    if(result.data.is_real_name == 1){
                        btnView.setVisibility(View.GONE);
                        iconView.setVisibility(View.VISIBLE);
                        nameView.setText(result.data.real_name);
                        if (StringUtil.isNotNull(result.data.id_card)){
                            String cardId = result.data.id_card;
                            noView.setText(cardId.replaceAll(cardId.substring(6,cardId.length()),"************"));
                        }

                        nameView.setEnabled(false);
                        noView.setEnabled(false);
                    }else{
                        btnView.setVisibility(View.VISIBLE);
                        iconView.setVisibility(View.GONE);
                    }
                }
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


