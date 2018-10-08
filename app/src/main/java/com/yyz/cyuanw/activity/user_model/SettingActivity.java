package com.yyz.cyuanw.activity.user_model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.MainActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.FileUtils;
import com.yyz.cyuanw.view.CustomProgress;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class SettingActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_userinfo) LinearLayout infoView;
    @BindView(R.id.id_oper_safe) LinearLayout safeView;
    @BindView(R.id.id_oper_carsource) LinearLayout carsourceView;
    @BindView(R.id.id_oper_clear) RelativeLayout clearView;
    @BindView(R.id.id_oper_about) LinearLayout aboutView;
    @BindView(R.id.id_oper_exit) LinearLayout exitView;

    private TextView tvCacheView;

    private int carSource;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        setTitle(titleView,"设置");
        ((TextView)infoView.getChildAt(0)).setText("个人资料设置");
        ((TextView)safeView.getChildAt(0)).setText("安全验证");
        ((TextView)carsourceView.getChildAt(0)).setText("店铺车辆来源");
        ((TextView)clearView.getChildAt(0)).setText("清理缓存");
        ((TextView)aboutView.getChildAt(0)).setText("关于车缘");
        ((TextView)exitView.getChildAt(0)).setText("退出登录");

        tvCacheView = ((TextView)clearView.getChildAt(1));
        tvCacheView.setText(FileUtils.getCacheSize(this));
    }

    @Override
    public void initData() {

        carSource = getIntent().getIntExtra("carSource",0);
    }

    @OnClick({R.id.id_oper_userinfo,R.id.id_oper_safe,R.id.id_oper_carsource,R.id.id_oper_clear,R.id.id_oper_about,R.id.id_oper_exit})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_userinfo:
                startActivity(UserInfoActivity.class);
                break;
            case R.id.id_oper_safe:
                startActivity(SafeConfirmActivity.class);
                break;
            case R.id.id_oper_carsource:
                Intent intent = new Intent(this,CarsourceChangeActivity.class);
                intent.putExtra("carSource",carSource);
                startActivityForResult(intent,1);

                break;
            case R.id.id_oper_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.id_oper_exit:
                showAlertDialog(1);
                break;
            case R.id.id_oper_clear:
                showAlertDialog(2);
                break;
        }
    }

    public void showAlertDialog(int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(type == 1 ? "退出登录" : "清理缓存");
        builder.setMessage(type == 1 ? "是否注销登录?" : "是否清空缓存?");
        builder.setPositiveButton("确定",(dialogView,which) -> {
            dialogView.dismiss();
            if (type == 1){
                logout();
            }else{
                tvCacheView.setText("0KB");
                FileUtils.clearAppCache(SettingActivity.this);
            }
        });
        builder.setNegativeButton("取消",(dialogView,which) -> dialogView.dismiss());
        builder.show();
    }

    public void startActivity(Class<?> cls){
        startActivity(new Intent(this,cls));
    }

    public void logout(){
        CustomProgress.show(this, "用户注销中...", false, null);

        HttpData.getInstance().logout(App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {
            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                Log.e("showLog--",e.getMessage());
                CustomProgress.dismis();
                App.showToast("服务器请求超时");
            }

            @Override
            public void onNext(HttpCodeResult result) {
                //App.set(Constant.KEY_USER_TOKEN,"");
                if (result.status == 200){
                    App.isExitLogin = true;
                    App.set(Constant.KEY_USER_ISLOGIN,"");
                    App.set(Constant.KEY_USER_TOKEN,"");
                    App.set(Constant.KEY_USER_DATA,"");
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(intent);
                    finish();
                }else{
                    App.showToast(result.message);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            carSource = data.getIntExtra("carSource",0);
        }
    }
}


