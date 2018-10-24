package com.yyz.cyuanw.activity.user_model;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.ChooseCityActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.CarStatusData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.ImgData;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.FileUtils;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LQRPhotoSelectUtils;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.view.CommonPopupDialog;
import com.yyz.cyuanw.view.CustomProgress;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;

public class CarConfirmActivity extends BaseActivity{
    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_tv_city) TextView cityView;
    @BindView(R.id.id_iv_icon) ImageView iconView;
    @BindView(R.id.id_iv_image1) ImageView image1View;
    @BindView(R.id.id_iv_image2) ImageView image2View;
    @BindView(R.id.id_iv_image3) ImageView image3View;
    @BindView(R.id.id_et_name1) EditText name1View;
    @BindView(R.id.id_et_name2) EditText name2View;
    @BindView(R.id.id_et_address) EditText addressView;
    @BindView(R.id.id_btn_confirm) Button submitView;
    @BindView(R.id.id_rl_city) RelativeLayout rlCityView;
    @BindView(R.id.id_rl_image1) RelativeLayout rlImage1View;
    @BindView(R.id.id_rl_image2) RelativeLayout rlImage2View;
    @BindView(R.id.id_rl_image3) RelativeLayout rlImage3View;

    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private int uploadPicType = 0;

    private View popupDialogView;
    private CommonPopupDialog mPopupDialog;

    private int sheng_id = -1, shi_id = -1, qu_id = -1;

    private File image1File,image2File,image3File;

    private int imageType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_carconfirm;
    }


    @Override
    public void initView() {
        setTitle(titleView,"车商认证");

    }

    @Override
    public void initData() {
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, (outputFile,outputUri) -> {
            LogManager.e(outputFile.getAbsolutePath());

            switch (imageType){
                case 1:
                    image1File = outputFile;
                    Img.load(image1View,outputUri.toString());
                    break;
                case 2:
                    image2File = outputFile;
                    Img.load(image2View,outputUri.toString());
                    break;
                case 3:
                    image3File = outputFile;
                    Img.load(image3View,outputUri.toString());
                    break;
            }

        },true);

        getCarStatus();

    }

    @OnClick({R.id.id_btn_confirm,R.id.id_rl_image1,R.id.id_rl_image2,R.id.id_rl_image3,R.id.id_rl_city})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_btn_confirm:

                String name = name1View.getText().toString();
                String shortname = name2View.getText().toString();
                String address = addressView.getText().toString();
                String city = cityView.getText().toString();

                if (StringUtil.isNotNull(name) && StringUtil.isNotNull(shortname) && StringUtil.isNotNull(address) &&
                        StringUtil.isNotNull(city) && image1File != null && image2File != null &&
                        image3File != null){

                    setCarStatus(getMultipartBody(name,shortname,address));
                }else{

                    App.showToast("请完善车商信息");
                }

                break;
            case R.id.id_rl_image1:

                imageType = 1;
                showPopupDialog();
                break;
            case R.id.id_rl_image2:

                imageType = 2;
                showPopupDialog();
                break;
            case R.id.id_rl_image3:

                imageType = 3;
                showPopupDialog();
                break;
            case R.id.id_rl_city:

                Intent intent = new Intent(this, ChooseCityActivity.class);
                intent.putExtra("level", 1);
                intent.putExtra("type", 3);
                startActivityForResult(intent, 2);
                break;
        }
    }

    public MultipartBody getMultipartBody(String name,String shortname,String address) {

        MultipartBody.Builder builder = new MultipartBody.Builder();

        RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/"+ FileUtils.getFileFormat(image1File.getName())), image1File);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/"+ FileUtils.getFileFormat(image2File.getName())), image2File);
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("image/"+ FileUtils.getFileFormat(image3File.getName())), image3File);

        builder.addFormDataPart("token",App.get(Constant.KEY_USER_TOKEN));
        builder.addFormDataPart("name",name);
        builder.addFormDataPart("abbreviation",shortname);
        builder.addFormDataPart("address",address);
        builder.addFormDataPart("province",sheng_id+"");
        builder.addFormDataPart("city",shi_id+"");
        builder.addFormDataPart("region",qu_id+"");
        builder.addFormDataPart("logo", image1File.getName(), requestBody1);
        builder.addFormDataPart("door", image2File.getName(), requestBody2);
        builder.addFormDataPart("business", image3File.getName(), requestBody3);

        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();

        return multipartBody;
    }

    public void showPopupDialog() {
        if (popupDialogView == null) {
            popupDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_uploadpic, null);

            popupDialogView.findViewById(R.id.id_root_view).setOnClickListener(view -> mPopupDialog.dismiss() );
            popupDialogView.findViewById(R.id.id_btn_cancel).setOnClickListener(view -> mPopupDialog.dismiss() );

            Button button1 = popupDialogView.findViewById(R.id.id_btn_camera);
            Button button2 = popupDialogView.findViewById(R.id.id_btn_selectpic);

            button1.setText("拍照");
            button2.setText("从相册选择");

            button1.setOnClickListener(view -> {

                PermissionGen.with(CarConfirmActivity.this)
                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        ).request();
                //takePhotoRequest();

                mPopupDialog.dismiss();
            });

            button2.setOnClickListener(view -> {

                PermissionGen.needPermission(CarConfirmActivity.this,
                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                );
                //selectPhotoRequest();


                mPopupDialog.dismiss();
            });
        }

        if (mPopupDialog == null) {
            mPopupDialog = new CommonPopupDialog(this,android.R.style.Theme_Panel);
            mPopupDialog.setCanceledOnTouchOutside(true);
            mPopupDialog.setContentView(popupDialogView);
        }


        mPopupDialog.showAtLocation(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    public void getCarStatus(){
        CustomProgress.show(this, "加载中...", false, null);

        HttpData.getInstance().dealerStatus(App.get(Constant.KEY_USER_TOKEN),new Observer<HttpResult<CarStatusData>>() {
            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
            }

            @Override
            public void onNext(HttpResult<CarStatusData> result) {

                if (result.status == 200){
                    CarStatusData data = result.data;

                    switch (data.status){
                        case 0:

                            iconView.setVisibility(View.VISIBLE);
                            iconView.setImageResource(R.mipmap.ic_confirming);
                            submitView.setVisibility(View.GONE);
                            break;
                        case 1:

                            iconView.setVisibility(View.VISIBLE);
                            iconView.setImageResource(R.mipmap.ic_confirm);
                            submitView.setVisibility(View.GONE);
                            break;
                    }

                    if (data.status == 0 || data.status == 1){
                        name1View.setText(data.name);
                        name2View.setText(data.abbreviation);
                        addressView.setText(data.address);
                        cityView.setText(data.province+" "+data.city+" "+data.region);
                        Img.loadD(image1View,data.logo,R.mipmap.camera);
                        Img.loadD(image2View,data.door,R.mipmap.camera);
                        Img.loadD(image3View,data.business,R.mipmap.camera);

                        name1View.setEnabled(false);
                        name2View.setEnabled(false);
                        addressView.setEnabled(false);
                        rlCityView.setEnabled(false);
                        rlImage1View.setEnabled(false);
                        rlImage2View.setEnabled(false);
                        rlImage3View.setEnabled(false);
                    }

                }
            }
        });
    }

    public void setCarStatus(MultipartBody body){
        CustomProgress.show(this, "提交中...", false, null);

        HttpData.getInstance().dealerEnter(body,new Observer<HttpCodeResult>() {
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
                if (result.status == 200){
                    App.updataUserData = true;
                    setResult(21);
                    finish();
                }
            }
        });
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        if (uploadPicType == 0){
            mLqrPhotoSelectUtils.setParams(1,1,400,400);
        }else{
            mLqrPhotoSelectUtils.setParams(2,1,800,480);
        }
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        if (uploadPicType == 0){
            mLqrPhotoSelectUtils.setParams(1,1,400,400);
        }else{
            mLqrPhotoSelectUtils.setParams(1,1,480,800);
        }
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);

            switch (requestCode) {
                case 2:
                    String sheng_name = data.getStringExtra("sheng_name");
                    sheng_id = data.getIntExtra("sheng_id", 0);
                    shi_id = data.getIntExtra("shi_id", 0);
                    String city = data.getStringExtra("city");

                    qu_id = data.getIntExtra("qu_id", 0);
                    String qu = data.getStringExtra("qu");

                    cityView.setText(sheng_name + " " + city + " " + qu);

                    break;
            }
        }
    }

    public void showDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-车缘网-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");
        builder.setPositiveButton("去设置", (dialog,which) -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + CarConfirmActivity.this.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


}


