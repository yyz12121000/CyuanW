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
import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.ChooseCityActivity;
import com.yyz.cyuanw.activity.SelectPicActivity;
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

    private LoginData userData;

    private int sheng_id = -1, shi_id = -1, qu_id = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (StringUtil.isNotNull(App.get(Constant.KEY_USER_PHONE))){
            tvNoView.setText(App.get(Constant.KEY_USER_PHONE));
            App.set(Constant.KEY_USER_PHONE,"");
        }
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
        tvSexView = (TextView) sexView.getChildAt(1);
        ((TextView)noView.getChildAt(0)).setText("手机号");
        tvNoView = (TextView) noView.getChildAt(1);
        ((TextView)shortnoView.getChildAt(0)).setText("短号");
        tvShortNoView = (TextView) shortnoView.getChildAt(1);
        ((TextView)cityView.getChildAt(0)).setText("城市");
        tvCityView = (TextView) cityView.getChildAt(1);
        ((TextView)signView.getChildAt(0)).setText("个性签名");
        tvSignView = (TextView) signView.getChildAt(1);
    }

//    @Override
//    protected void callBack(File outputFile,Uri outputUri) {
//        LogManager.e(outputFile.getAbsolutePath());
//
//        if (uploadPicType == 0)
//            Img.loadC(ivPhotoView,outputUri.toString());
//
//        uploadFile(getMultipartBody(outputFile));
//    }

    @Override
    public void initData() {
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, (outputFile,outputUri) -> {
            LogManager.e(outputFile.getAbsolutePath());

            if (uploadPicType == 0)
                Img.loadC(ivPhotoView,outputUri.toString());

            uploadFile(getMultipartBody(outputFile));

        },true);

        String jsonStr = App.get(Constant.KEY_USER_DATA);
        if (StringUtil.isNotNull(jsonStr)){
            userData = new Gson().fromJson(jsonStr,LoginData.class);

            if (StringUtil.isNotNull(userData.pic)){
                Img.loadC(ivPhotoView,userData.pic);
            }else{
                ivPhotoView.setImageResource(R.mipmap.ic_defaultphoto);
            }

            tvNameView.setText(userData.name);
            tvNoView.setText(userData.phone);
            if ("1".equals(userData.gender)){
                tvSexView.setText("男");
            }else if ("2".equals(userData.gender)){
                tvSexView.setText("女");
            }
            tvShortNoView.setText(userData.virtual_number);
            tvCityView.setText(userData.province+" "+userData.city+" "+userData.region);
            tvSignView.setText(userData.signature);
        }
    }

    @OnClick({R.id.id_oper_setphoto,R.id.id_oper_setbg,R.id.id_oper_setsex,R.id.id_oper_setshortno,R.id.id_oper_setsign,R.id.id_oper_setno,R.id.id_oper_setcity})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_setphoto:
                uploadPicType = 0;
                showPopupDialog(0);
                break;
            case R.id.id_oper_setbg:
                uploadPicType = 1;
                showPopupDialog(0);
                break;
            case R.id.id_oper_setsex:
                showPopupDialog(1);
                break;
            case R.id.id_oper_setshortno:
                showModifyDialog("请输入短号",tvShortNoView.getText().toString(),"virtual_number",tvShortNoView);
                break;
            case R.id.id_oper_setsign:
                showModifyDialog("请输入个性签名",tvSignView.getText().toString(),"signature",tvSignView);
                break;
            case R.id.id_oper_setno:

                if (userData != null){
                    Intent intent = new Intent(this,PhoneBindActivity.class);
                    intent.putExtra("phone",userData.phone);
                    startActivity(intent);
                }
                break;
            case R.id.id_oper_setcity:

                Intent intent = new Intent(this, ChooseCityActivity.class);
                intent.putExtra("level", 1);
                intent.putExtra("type", 3);
                startActivityForResult(intent, 2);
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

    public void showModifyDialog(String title,String content,String name,TextView nameView){
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
        final EditText contentView = dialogView.findViewById(R.id.id_et_content);

        titleView.setText(title);
        contentView.setText(content);
        contentView.setSelection(contentView.getText().toString().length());

        dialogView.findViewById(R.id.id_iv_close).setOnClickListener(view -> mDialog.dismiss() );
        dialogView.findViewById(R.id.id_tv_submit).setOnClickListener(view -> {
            String strContent = contentView.getText().toString().trim();
            if (!StringUtil.isNotNull(strContent)){
                App.showToast("内容不能为空");
                return;
            }

            setInfo(name,strContent,nameView);

            mDialog.dismiss();
        });

    }

    public void showPopupDialog(int showType) {
        if (popupDialogView == null) {
            popupDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_uploadpic, null);

            popupDialogView.findViewById(R.id.id_root_view).setOnClickListener(view -> mPopupDialog.dismiss() );
            popupDialogView.findViewById(R.id.id_btn_cancel).setOnClickListener(view -> mPopupDialog.dismiss() );

            Button button1 = popupDialogView.findViewById(R.id.id_btn_camera);
            Button button2 = popupDialogView.findViewById(R.id.id_btn_selectpic);

            if (showType == 0){
                button1.setText("拍照");
                button2.setText("从相册选择");
            }else{
                button1.setText("男");
                button2.setText("女");
            }

            button1.setOnClickListener(view -> {
                if (showType == 0){
                    PermissionGen.with(UserInfoActivity.this)
                            .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            ).request();
                    //takePhotoRequest();
                }else{
                    setInfo("gender","1",tvSexView);
                }

                mPopupDialog.dismiss();
            });

            button2.setOnClickListener(view -> {
                if (showType == 0){
                    PermissionGen.needPermission(UserInfoActivity.this,
                            LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    );
                    //selectPhotoRequest();
                }else{
                    setInfo("gender","2",tvSexView);
                }

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

    public void setInfo(String name,String value,TextView nameView){
        CustomProgress.show(this, "请求中...", false, null);

        HttpData.getInstance().setUserInfo(name,value,App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {
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
                    if (name.equals("gender")){
                        if ("1".equals(value)){
                            nameView.setText("男");
                        }else{
                            nameView.setText("女");
                        }
                    }else{
                        nameView.setText(value);
                    }
                    App.updataUserData = true;
                }
            }
        });
    }

    public void setAddress(String address){
        CustomProgress.show(this, "请求中...", false, null);

        HttpData.getInstance().setAddress(sheng_id,shi_id,qu_id,address,App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {
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
                    setResult(RESULT_OK);
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
                    App.updataUserData = true;
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
        // 在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        //mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
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

                    tvCityView.setText(sheng_name + " " + city + " " + qu);
                    setAddress(tvCityView.getText().toString());

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
            intent.setData(Uri.parse("package:" + UserInfoActivity.this.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


}


