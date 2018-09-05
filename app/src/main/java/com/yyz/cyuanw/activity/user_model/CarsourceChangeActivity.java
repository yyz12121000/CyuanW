package com.yyz.cyuanw.activity.user_model;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
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

public class CarsourceChangeActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_cb_view1) CheckBox checkBoxView1;
    @BindView(R.id.id_cb_view2) CheckBox checkBoxView2;

    private int carSource;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_carsource;
    }

    @Override
    public void initView() {
        setTitle(titleView,"店铺车源来源");

        carSource = getIntent().getIntExtra("carSource",0);

        if (carSource == 1){
            checkBoxView1.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_select,0,0,0);
        }else{
            checkBoxView2.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_select,0,0,0);
        }

        checkBoxView1.setOnClickListener( view -> {
            carSource = 1;
            checkBoxView1.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_select,0,0,0);
            checkBoxView2.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_unselect,0,0,0);
            changeCarSource(carSource);
        });

        checkBoxView2.setOnClickListener( view -> {
            carSource = 2;
            checkBoxView2.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_select,0,0,0);
            checkBoxView1.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_unselect,0,0,0);
            changeCarSource(carSource);
        });

    }

    @Override
    public void initData() {

    }


    public void changeCarSource(int source){
        CustomProgress.show(this, "请稍等...", false, null);

        HttpData.getInstance().changeCarSource(App.get(Constant.KEY_USER_TOKEN),source,new Observer<HttpCodeResult>() {
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
                    Intent intent = new Intent();
                    intent.putExtra("carSource",carSource);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

}


