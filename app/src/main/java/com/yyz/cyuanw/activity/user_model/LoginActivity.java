package com.yyz.cyuanw.activity.user_model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.ImgData;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.ImageTools;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.view.CustomProgress;

import java.util.LinkedHashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class LoginActivity extends BaseActivity{

    @BindView(R.id.id_iv_back) ImageView backView;
    @BindView(R.id.id_et_phone) EditText phoneView;
    @BindView(R.id.id_et_code) EditText codeView;
    @BindView(R.id.id_tv_getcode) TextView getCodeView;

    private Dialog mDialog;
    private TimeCount time;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        time = new TimeCount(60000, 1000);
    }

    @OnClick({R.id.id_iv_back,R.id.id_btn_login,R.id.id_tv_getcode})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_iv_back:

                finish();
                break;
            case R.id.id_btn_login:

                String strMobile = phoneView.getText().toString().trim();
                if (!StringUtil.isNotNull(strMobile)){
                    App.showToast("请输入手机号码");
                    return;
                }
                String strCode = codeView.getText().toString().trim();
                if (!StringUtil.isNotNull(strCode)){
                    App.showToast("请输入短信验证码");
                    return;
                }
                login(strMobile,strCode);
                break;
            case R.id.id_tv_getcode:

                String strPhone = phoneView.getText().toString().trim();
                if (!StringUtil.isNotNull(strPhone)){
                    App.showToast("请输入手机号码");
                    return;
                }
                getImgCode(strPhone,null);
                break;
        }
    }

    public void showImgDialog(String imageData){
        if (mDialog == null){
            mDialog = new AlertDialog.Builder(this,R.style.MyDialog).create();
        }
        mDialog.show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_code, null);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setContentView(dialogView);
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ImageView imageView = dialogView.findViewById(R.id.id_iv_code);
        final EditText codeView = dialogView.findViewById(R.id.id_et_code);

        dialogView.findViewById(R.id.id_iv_close).setOnClickListener(view -> mDialog.dismiss() );
        imageView.setImageBitmap(ImageTools.base64ToBitmap(imageData));
        imageView.setOnClickListener(view -> getImgCode(phoneView.getText().toString(),imageView));
        dialogView.findViewById(R.id.id_tv_submit).setOnClickListener(view -> {
            String strCode = codeView.getText().toString().trim();
            if (!StringUtil.isNotNull(strCode)){
                App.showToast("请输入图形验证码");
                return;
            }
            getValidateCode(phoneView.getText().toString(),strCode);
            mDialog.dismiss();
        });

    }

    public void getImgCode(String phone,ImageView imageView){
        if (imageView == null)
            CustomProgress.show(this, "请稍等...", false, null);

        HttpData.getInstance().getImgCode(phone, new Observer<HttpResult<ImgData>>() {
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
            public void onNext(HttpResult<ImgData> result) {

                if (result.status == 200 && result.data != null){
                    if (imageView == null){
                        showImgDialog(result.data.image);
                    }else {
                        imageView.setImageBitmap(ImageTools.base64ToBitmap(result.data.image));
                    }
                }else{
                    App.showToast(result.message);
                }
            }
        });
    }

    public void getValidateCode(String phone,String imgCode){
        CustomProgress.show(this, "提交中...", false, null);

        HttpData.getInstance().getValidateCode(phone, imgCode,new Observer<HttpCodeResult>() {
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

    public void login(String phone,String code){
        CustomProgress.show(this, "正在登录...", false, null);

        HttpData.getInstance().login(phone, code,new Observer<HttpResult<LoginData>>() {
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
            public void onNext(HttpResult<LoginData> result) {

                if (result.status == 200 && result.data != null){
                    App.set(Constant.KEY_USER_ISLOGIN,"true");
                    App.set(Constant.KEY_USER_TOKEN,result.data.token);
                    //App.set(Constant.KEY_USER_DATA,new Gson().toJson(result.data));

                    finish();
                }else{
                    App.showToast(result.message);
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


