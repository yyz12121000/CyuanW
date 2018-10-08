package com.yyz.cyuanw.activity.user_model;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.ImgData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.FileUtils;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LQRPhotoSelectUtils;
import com.yyz.cyuanw.tools.LogManager;
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

public class PersionConfirmActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_iv_image) ImageView imageView;;
    @BindView(R.id.id_oper_carset) RelativeLayout carView;
    @BindView(R.id.id_btn_selectpic) Button selectView;
    @BindView(R.id.id_btn_submit) Button submitView;
    @BindView(R.id.id_iv_icon) ImageView iconView;

    private TextView tvCarView;

    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private boolean selectedPic = false;
    private File outputFile;

    private int status;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_persionconfirm;
    }

    @Override
    public void initView() {
        setTitle(titleView,"经纪人认证");
        ((TextView)carView.getChildAt(0)).setText("关联车商");
        tvCarView = (TextView) carView.getChildAt(1);

        status = getIntent().getIntExtra("status",-1);

        switch (status){
            case 0:

                break;
            case 1:
            case 2:

                if (status == 1){
                    iconView.setImageResource(R.mipmap.ic_confirm);
                }else {
                    iconView.setImageResource(R.mipmap.ic_confirming);
                }

                iconView.setVisibility(View.VISIBLE);
                selectView.setEnabled(false);
                carView.setEnabled(false);
                submitView.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void initData() {
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, (outputFile,outputUri) -> {
            LogManager.e(outputFile.getAbsolutePath());
            Img.load(imageView,outputUri.toString());
            this.outputFile = outputFile;

        },1,1,400,300);
    }

    @OnClick({R.id.id_oper_carset,R.id.id_btn_selectpic,R.id.id_btn_submit})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_carset:

                break;
            case R.id.id_btn_selectpic:

                PermissionGen.needPermission(this,
                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                );
                break;
            case R.id.id_btn_submit:

                if (outputFile != null){
                    uploadFile(getMultipartBody(outputFile));
                }else{
                    App.showToast("请上传身份证正面照");
                }
                break;
        }
    }

    public MultipartBody getMultipartBody(File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/"+ FileUtils.getFileFormat(file.getName())), file);
        builder.addFormDataPart("token", App.get(Constant.KEY_USER_TOKEN));
        builder.addFormDataPart("hold_id_card", file.getName(), requestBody);
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();

        return multipartBody;
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
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
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void showDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-车缘网-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");
        builder.setPositiveButton("去设置", (dialog,which) -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + PersionConfirmActivity.this.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void uploadFile(MultipartBody body){
        CustomProgress.show(this, "图片上传中...", false, null);

        HttpData.getInstance().certificationBroker(body,new Observer<HttpCodeResult>() {
            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
                App.showToast("图片上传失败");
            }

            @Override
            public void onNext(HttpCodeResult result) {
                App.showToast(result.message);

                if(result.status == 200){
                    setResult(20);
                    finish();
                }

            }
        });

    }

}


