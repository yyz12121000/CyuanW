package com.yyz.cyuanw.activity.user_model;

import android.os.CountDownTimer;
import android.view.View;
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

public class PhoneSubmitActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_tv_notice) TextView noticeView;
    @BindView(R.id.id_tv_getcode) TextView getCodeView;
    @BindView(R.id.id_tv_phone) TextView tvPhoneView;
    @BindView(R.id.id_et_phone) EditText phoneView;
    @BindView(R.id.id_et_code) EditText codeView;

    private int flag;// 0 更换手机号 1 绑定手机号
    private String code;

    private TimeCount time;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phonesubmit;
    }

    @Override
    public void initView() {

        flag = getIntent().getIntExtra("flag",0);
        code = getIntent().getStringExtra("code");

        if (flag == 0){
            setTitle(titleView,"更换手机号");
            tvPhoneView.setText("新手机号");
            noticeView.setVisibility(View.VISIBLE);
        }else{
            setTitle(titleView,"绑定手机号");
            tvPhoneView.setText("手机号码");
            noticeView.setVisibility(View.GONE);
        }

    }

    @Override
    public void initData() {
        time = new TimeCount(60000, 1000);
    }

    @OnClick({R.id.id_btn_submit,R.id.id_tv_getcode})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_btn_submit:

                String phone = phoneView.getText().toString().trim();
                if (!StringUtil.isNotNull(phone)){
                    App.showToast("请输入新手机号");
                    return;
                }

                String code = codeView.getText().toString().trim();
                if (!StringUtil.isNotNull(code)){
                    App.showToast("请输入短信验证码");
                    return;
                }

                changePhone(phone,code);
                break;
            case R.id.id_tv_getcode:

                String strPhone = phoneView.getText().toString().trim();
                if (!StringUtil.isNotNull(strPhone)){
                    App.showToast("请输入新手机号");
                    return;
                }
                getValidateCode(strPhone,2);
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

    public void changePhone(String phone,String newcode){
        CustomProgress.show(this, "提交中...", false, null);

        HttpData.getInstance().changePhone(phone, code,newcode,App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {
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
                    App.set(Constant.KEY_USER_PHONE,phone);
                    finish();
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


