package com.yyz.cyuanw.activity.user_model;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.view.CustomProgress;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class PhoneChangeActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_tv_phone) TextView phoneView;
    @BindView(R.id.id_tv_getcode) TextView getCodeView;
    @BindView(R.id.id_et_code) EditText codeView;

    private String phone;
    private TimeCount time;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phonechange;
    }

    @Override
    public void initView() {
        phone = getIntent().getStringExtra("phone");

        setTitle(titleView,"更换手机号");
        phoneView.setText("原手机号："+phone);

    }

    @Override
    public void initData() {
        time = new TimeCount(60000, 1000);

    }

    @OnClick({R.id.id_btn_next,R.id.id_tv_getcode})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_btn_next:

                String strCode = codeView.getText().toString().trim();
                if (!StringUtil.isNotNull(strCode)){
                    App.showToast("请输入短信验证码");
                    return;
                }

                Intent intent = new Intent(this,PhoneSubmitActivity.class);
                intent.putExtra("flag",0);
                intent.putExtra("code",strCode);
                startActivity(intent);
                finish();
                break;
            case R.id.id_tv_getcode:

                getValidateCode(phone,1);
                break;
        }
    }

    public void getValidateCode(String phone,int type){
        CustomProgress.show(this, "提交中...", false, null);

        HttpData.getInstance().changeSend(phone,type,App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {
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
                    time.start();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (time != null)
            time.cancel();
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            if (getCodeView != null) {
                getCodeView.setText("获取验证码");
                getCodeView.setEnabled(true);
            }
        }
        @Override
        public void onTick(long millisUntilFinished){
            if (getCodeView != null) {
                getCodeView.setEnabled(false);
                getCodeView.setText(millisUntilFinished /1000+"秒");
            }
        }
    }
}


