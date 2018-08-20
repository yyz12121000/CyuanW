package com.yyz.cyuanw.activity.user_model;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.apiClient.HttpData;
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

public class UserInfoActivity extends BaseActivity{
    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_setphoto) RelativeLayout photoView;
    @BindView(R.id.id_oper_setbg) LinearLayout bgpicView;
    @BindView(R.id.id_oper_setname) RelativeLayout nameView;
    @BindView(R.id.id_oper_setsex) RelativeLayout sexView;
    @BindView(R.id.id_oper_setno) RelativeLayout noView;
    @BindView(R.id.id_oper_setshortno) RelativeLayout shortnoView;
    @BindView(R.id.id_oper_setcity) RelativeLayout cityView;
    @BindView(R.id.id_oper_setsign) RelativeLayout signView;

    private TextView tvNameView,tvSexView,tvNoView,tvShortNoView,tvCityView,tvSignView;
    private ImageView ivPhotoView;

    private View popupDialogView;
    private CommonPopupDialog mPopupDialog;
    private Dialog mDialog;

    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;

    private int uploadPicType = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initView() {
        setTitle(titleView,"个人信息");
        ((TextView)photoView.getChildAt(0)).setText("头像设置");
        ivPhotoView = (ImageView) photoView.getChildAt(1);
        ((TextView)bgpicView.getChildAt(0)).setText("背景图片设置");
        ((TextView)nameView.getChildAt(0)).setText("姓名");
        nameView.getChildAt(2).setVisibility(View.INVISIBLE);
        tvNameView = (TextView) nameView.getChildAt(1);
        ((TextView)sexView.getChildAt(0)).setText("性别");
        tvSexView = (TextView) nameView.getChildAt(1);
        ((TextView)noView.getChildAt(0)).setText("手机号");
        tvNoView = (TextView) nameView.getChildAt(1);
        ((TextView)shortnoView.getChildAt(0)).setText("短号");
        tvShortNoView = (TextView) nameView.getChildAt(1);
        ((TextView)cityView.getChildAt(0)).setText("城市");
        tvCityView = (TextView) nameView.getChildAt(1);
        ((TextView)signView.getChildAt(0)).setText("个性签名");
        tvSignView = (TextView) nameView.getChildAt(1);

        tvNameView.setText("黄劲");
    }

    @Override
    public void initData() {
        getInfo();

        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, (outputFile,outputUri) -> {
            LogManager.e(outputFile.getAbsolutePath());

            if (uploadPicType == 0)
                Img.loadC(ivPhotoView,outputUri.toString());

            uploadFile(getMultipartBody(outputFile));

        },true);
    }

    @OnClick({R.id.id_oper_setphoto,R.id.id_oper_setbg})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_setphoto:
                uploadPicType = 0;
                showPopupDialog();
                break;
            case R.id.id_oper_setbg:
                uploadPicType = 1;
                showPopupDialog();
                break;
        }
    }

    public MultipartBody getMultipartBody(File file) {
        MultipartBody.Builder builder = new MultipartBody.Builder();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/"+ FileUtils.getFileFormat(file.getName())), file);
        builder.addFormDataPart("token",App.get(Constant.KEY_USER_TOKEN));
        if (uploadPicType == 0) {
            builder.addFormDataPart("pic", file.getName(), requestBody);
        }else{
            builder.addFormDataPart("bg_image", file.getName(), requestBody);
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();

        return multipartBody;
    }

    public void showModifyDialog(String title){
        if (mDialog == null){
            mDialog = new AlertDialog.Builder(this,R.style.MyDialog).create();
        }
        mDialog.show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_modify, null);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setContentView(dialogView);
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TextView titleView = dialogView.findViewById(R.id.id_tv_title);
        final EditText codeView = dialogView.findViewById(R.id.id_et_content);

        titleView.setText(title);

        dialogView.findViewById(R.id.id_iv_close).setOnClickListener(view -> mDialog.dismiss() );
        dialogView.findViewById(R.id.id_tv_submit).setOnClickListener(view -> {
            String strCode = codeView.getText().toString().trim();
            if (!StringUtil.isNotNull(strCode)){
                App.showToast("内容不能为空");
                return;
            }

            mDialog.dismiss();
        });

    }

    public void showPopupDialog() {
        if (popupDialogView == null) {
            popupDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_uploadpic, null);

            popupDialogView.findViewById(R.id.id_root_view).setOnClickListener(view -> mPopupDialog.dismiss() );
            popupDialogView.findViewById(R.id.id_btn_cancel).setOnClickListener(view -> mPopupDialog.dismiss() );
            popupDialogView.findViewById(R.id.id_btn_camera).setOnClickListener(view -> {
                PermissionGen.with(UserInfoActivity.this)
                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        ).request();
                mPopupDialog.dismiss();
            });

            popupDialogView.findViewById(R.id.id_btn_selectpic).setOnClickListener(view -> {
                PermissionGen.needPermission(UserInfoActivity.this,
                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                );
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

    public void getInfo(){
        CustomProgress.show(this, "加载中...", false, null);

        HttpData.getInstance().getUserInfo(App.get(Constant.KEY_USER_TOKEN),new Observer<HttpResult<LoginData>>() {
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
                    tvNameView.setText(result.data.phone);
                    if (StringUtil.isNotNull(result.data.pic))
                        Img.loadC(ivPhotoView,result.data.pic);
                }else{
                    App.showToast(result.message);
                }
            }
        });
    }

    public void setInfo(String name){
        CustomProgress.show(this, "请求中...", false, null);

        HttpData.getInstance().setUserInfo(name,App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {
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

                }
            }
        });
    }

    public void uploadFile(MultipartBody body){
        CustomProgress.show(this, "图片上传中...", false, null);

        HttpData.getInstance().uploadFile(body,uploadPicType, new Observer<HttpResult<ImgData>>() {
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
            public void onNext(HttpResult<ImgData> result) {
                App.showToast(result.message);

                if(result.status == 200){
                    if (uploadPicType == 0){
                        LogManager.e(result.data.pic);
                    }else {
                        LogManager.e(result.data.bg_image);
                    }
                }

            }
        });

    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        if (uploadPicType == 0){
            mLqrPhotoSelectUtils.setParams(1,1,400,400);
        }else{
            mLqrPhotoSelectUtils.setParams(1,2,480,800);
        }
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        if (uploadPicType == 0){
            mLqrPhotoSelectUtils.setParams(1,1,400,400);
        }else{
            mLqrPhotoSelectUtils.setParams(1,2,480,800);
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
        // 在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void showDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-车缘网-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");
        builder.setPositiveButton("去设置", (dialog,which) -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + UserInfoActivity.this.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }



}


