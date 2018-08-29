package com.yyz.cyuanw.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.user_model.UserInfoActivity;
import com.yyz.cyuanw.oss.Oss;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LQRPhotoSelectUtils;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.view.CommonPopupDialog;

import butterknife.BindView;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class LmDetailDetailEditActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView id_tv_title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.desc)
    EditText desc;
    @BindView(R.id.er_code)
    ImageView er_code;
    @BindView(R.id.img)
    ImageView img;

    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;

    public static final String TYPE = "type";
    public static final int TYPE_CREATE = 1;//创建联盟
    public static final int TYPE_EDIT = 2;//编辑联盟

    private int type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lm_detail_detail_edit;
    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra(TYPE, -1);
        switch (type) {
            case 1:
                setTitle(id_tv_title, "创建联盟");
                save.setText("创建联盟");
                save.setBackgroundResource(R.drawable.bt_blue_bg);
                findViewById(R.id.hint_1).setVisibility(View.VISIBLE);
                findViewById(R.id.hint_2).setVisibility(View.VISIBLE);
                break;
            case 2:
                setTitle(id_tv_title, "联盟资料编辑");
                save.setText("保存修改");
                save.setBackgroundResource(R.drawable.bt_yellow_bg);
                break;
            default:
                finish();
        }

    }

    @Override
    public void initData() {

        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, (outputFile,outputUri) -> {
            LogManager.e(outputFile.getAbsolutePath());

            new Oss(outputFile.getAbsolutePath());

//            if (uploadPicType == 0)
//                Img.loadC(ivPhotoView,outputUri.toString());
//
//            uploadFile(getMultipartBody(outputFile));

        },true);

        if (type == TYPE_EDIT) {
            Intent intent = getIntent();
            String imgS = intent.getStringExtra("img");
            String nameS = intent.getStringExtra("name");
            String descS = intent.getStringExtra("desc");
            String ewmS = intent.getStringExtra("ewm");

            Img.loadC(img, imgS);
            edit_name.setText(nameS);
            desc.setText(descS);
        }

    }

    @OnClick({R.id.save, R.id.address, R.id.img})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.save:
                String name = edit_name.getText().toString();
                String descStr = desc.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(descStr)) {

                }
                break;
            case R.id.address:
                Intent intent = new Intent(this, ChooseCityActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.img:
                showPopupDialog(0);
                break;
        }
    }

    private View popupDialogView;
    private CommonPopupDialog mPopupDialog;
    private Dialog mDialog;

    public void showPopupDialog(int showType) {
        if (popupDialogView == null) {
            popupDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_uploadpic, null);

            popupDialogView.findViewById(R.id.id_root_view).setOnClickListener(view -> mPopupDialog.dismiss());
            popupDialogView.findViewById(R.id.id_btn_cancel).setOnClickListener(view -> mPopupDialog.dismiss());

            Button button1 = popupDialogView.findViewById(R.id.id_btn_camera);
            Button button2 = popupDialogView.findViewById(R.id.id_btn_selectpic);

            if (showType == 0) {
                button1.setText("拍照");
                button2.setText("从相册选择");
            } else {
                button1.setText("男");
                button2.setText("女");
            }

            button1.setOnClickListener(view -> {
                if (showType == 0) {
                    PermissionGen.with(LmDetailDetailEditActivity.this)
                            .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            ).request();
                } else {
//                    setInfo("gender","1",tvSexView);
                }

                mPopupDialog.dismiss();
            });

            button2.setOnClickListener(view -> {
                if (showType == 0) {
                    PermissionGen.needPermission(LmDetailDetailEditActivity.this,
                            LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    );
                } else {
//                    setInfo("gender","2",tvSexView);
                }

                mPopupDialog.dismiss();
            });
        }

        if (mPopupDialog == null) {
            mPopupDialog = new CommonPopupDialog(this, android.R.style.Theme_Panel);
            mPopupDialog.setCanceledOnTouchOutside(true);
            mPopupDialog.setContentView(popupDialogView);
        }


        mPopupDialog.showAtLocation(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }
    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        if (1 == 0){
            mLqrPhotoSelectUtils.setParams(1,1,400,400);
        }else{
            mLqrPhotoSelectUtils.setParams(2,1,800,480);
        }
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        if (1 == 0){
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
    public void showDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setTitle("权限申请");
        builder.setMessage("在设置-应用-车缘网-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");
        builder.setPositiveButton("去设置", (dialog,which) -> {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + LmDetailDetailEditActivity.this.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
            switch (requestCode) {
                case 2:
                    String sheng_name = data.getStringExtra("sheng_name");
                    int sheng_id = data.getIntExtra("sheng_id", 0);
                    int shi_id = data.getIntExtra("shi_id", 0);
                    String city = data.getStringExtra("city");
                    address.setText(sheng_name + " " + city);
                    break;
                case LQRPhotoSelectUtils.REQ_TAKE_PHOTO:

                    break;
                case LQRPhotoSelectUtils.REQ_SELECT_PHOTO:

                    break;
            }
        }
    }
    /*private void submit(){
            HttpData.getInstance().createLm(0, new Observer<HttpResult<String>>() {
                @Override
                public void onCompleted() {
//                App.showToast("999");
                }

                @Override
                public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                    LogManager.e(e.getMessage());
                }

                @Override
                public void onNext(HttpResult<String> result) {
                    if (result.status == 200) {
                        adapter.vaHolder.myLmListAdapter.setData(result.data);
                    } else {
//                    App.showToast(result.message);
                    }
                }
            });

    }*/

}
