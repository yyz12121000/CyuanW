package com.yyz.cyuanw.activity.user_model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.DealerData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.SettingData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.Tools;
import com.yyz.cyuanw.view.CustomProgress;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class CarRelationActivity extends BaseActivity{

    @BindView(R.id.title_right) TextView cancelView;
    @BindView(R.id.id_car_view) FrameLayout carView;
    @BindView(R.id.id_iv_icon) ImageView iconView;
    @BindView(R.id.id_iv_image) ImageView imageView;
    @BindView(R.id.id_tv_name) TextView nameView;
    @BindView(R.id.id_tv_city) TextView cityView;
    @BindView(R.id.black) LinearLayout blackView;
    @BindView(R.id.id_btn_submit) Button submitView;
    @BindView(R.id.search_text) TextView searchView;

    private DealerData data;
    private int carId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_carrelation;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        relationDealerStatus();
    }

    @OnClick({R.id.id_btn_submit,R.id.title_right,R.id.search_text})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_btn_submit:

                relation();
                break;
            case R.id.title_right:

                unRelationDealer();
                break;
            case R.id.search_text:

                startActivityForResult(new Intent(this,CarSearchActivity.class),10);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            blackView.setVisibility(View.GONE);
            carView.setVisibility(View.VISIBLE);
            cancelView.setVisibility(View.GONE);
            submitView.setVisibility(View.VISIBLE);
            iconView.setVisibility(View.GONE);

            carId = data.getIntExtra("id",0);
            nameView.setText(data.getStringExtra("name"));
            cityView.setText(data.getStringExtra("city_name"));
            Img.loadC(imageView,data.getStringExtra("logo"));
        }
    }

    public void relationDealerStatus(){

        HttpData.getInstance().relationDealerStatus(App.get(Constant.KEY_USER_TOKEN),new Observer<HttpResult<DealerData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<DealerData> result) {

                if (result.status == 200 && result.data != null){
                    data = result.data;

                    switch (data.status){
                        case -1:

                            blackView.setVisibility(View.VISIBLE);
                            carView.setVisibility(View.GONE);
                            cancelView.setVisibility(View.GONE);
                            submitView.setVisibility(View.GONE);
                            searchView.setEnabled(true);
                            break;
                        case 0:
                        case 1:

                            blackView.setVisibility(View.GONE);
                            carView.setVisibility(View.VISIBLE);
                            submitView.setVisibility(View.GONE);
                            searchView.setEnabled(false);
                            if (data.status == 0){
                                iconView.setImageResource(R.mipmap.ic_confirming);
                                cancelView.setVisibility(View.GONE);
                            }else{
                                iconView.setImageResource(R.mipmap.ic_confirm);
                                cancelView.setVisibility(View.VISIBLE);
                            }
                            Img.loadC(imageView,data.logo);
                            nameView.setText(data.abbreviation);
                            cityView.setText(data.city);
                            break;
                    }
                }

            }
        });
    }

    public void unRelationDealer(){

        CustomProgress.show(this, "取消中...", false, null);
        HttpData.getInstance().unRelationDealer(App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {
            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
            }

            @Override
            public void onNext(HttpCodeResult result) {
                App.showToast(result.message);
                if (result.status == 200 ){
                    setResult(22);
                    finish();
                }

            }
        });
    }

    public void relation(){

        CustomProgress.show(this, "关联中...", false, null);
        HttpData.getInstance().relation(carId+"",App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {
            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
            }

            @Override
            public void onNext(HttpCodeResult result) {

                App.showToast(result.message);
                if (result.status == 200 ){
                    setResult(22);
                    finish();
                }

            }
        });
    }
}


