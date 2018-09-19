package com.yyz.cyuanw.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.CheyListData;
import com.yyz.cyuanw.bean.Data14;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LmDetail;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class LmDetailDetailActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView id_tv_title;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.exit)
    TextView exit;
    @BindView(R.id.cjr)
    TextView cjr;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.ewm)
    ImageView ewm;
    @BindView(R.id.id_rl_qrcode)
    RelativeLayout qrcodeView;
    @BindView(R.id.id_view_line)
    View lineView;

    private int lm_id;

    LmDetail lmDetail;
    private Dialog mDialog;
    private Dialog qrcodeDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lm_detail_detail;
    }

    @Override
    public void initView() {

        setTitle(id_tv_title, "联盟详情");

    }

    public void showHintDialog(int type) {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(this, R.style.MyDialog).create();
        }
        mDialog.show();
        View dialogView = null;
        if (type == 1) {
            dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_lm_hint_1, null);
        } else {
            dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_lm_hint_2, null);
        }

        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setContentView(dialogView);
        mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialogView.findViewById(R.id.know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

    }

    @Override
    public void initData() {
        int lm_id = getIntent().getIntExtra("id", -1);
        if (lm_id != -1) {
            load(lm_id);
        }

    }

    @OnClick({R.id.edit, R.id.exit,R.id.id_rl_qrcode})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.edit:
                Intent intent = new Intent(LmDetailDetailActivity.this, LmDetailDetailEditActivity.class);
                intent.putExtra("id", lmDetail.id);
                intent.putExtra("img", lmDetail.logo);
                intent.putExtra("name", lmDetail.name);
                intent.putExtra("desc", lmDetail.intro);
                intent.putExtra("ewm", lmDetail.qr_code);
//                intent.putExtra("address",lmDetail.logo);
                intent.putExtra(LmDetailDetailEditActivity.TYPE, LmDetailDetailEditActivity.TYPE_EDIT);
                startActivity(intent);
                break;
            case R.id.exit:
                if ("申请解散联盟".equals(exit.getText().toString())) {
                    showHintDialog(1);
                } else {
                    showHintDialog(2);
                }
                break;
            case R.id.id_rl_qrcode:

                showQrcodeDialog();
                break;
        }
    }

    public void showQrcodeDialog(){
        if (qrcodeDialog == null){
            qrcodeDialog = new AlertDialog.Builder(this,R.style.MyDialog).create();
        }
        qrcodeDialog.show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_weixin, null);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        ImageView codeView = dialogView.findViewById(R.id.id_iv_code);
        TextView textView = dialogView.findViewById(R.id.id_tv_text);

        textView.setText("请您扫描上方二维码");
        if (lmDetail != null)
            Img.loadC(codeView, lmDetail.qr_code);

        qrcodeDialog.setCanceledOnTouchOutside(false);
        qrcodeDialog.getWindow().setContentView(dialogView);

        dialogView.findViewById(R.id.id_tv_ok).setOnClickListener(view -> qrcodeDialog.dismiss() );
    }

    private void load(int id) {
        HttpData.getInstance().lmDetail(id, new Observer<HttpResult<LmDetail>>() {
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
            public void onNext(HttpResult<LmDetail> result) {
                if (result.status == 200) {
                    lmDetail = result.data;

                    Img.loadC(img, lmDetail.logo);
                    name.setText(lmDetail.name);
                    desc.setText(lmDetail.intro);
                    cjr.setText(lmDetail.leader.real_name);

                    address.setText(lmDetail.province_city_region_text);

                    if (StringUtil.isNotNull(lmDetail.qr_code)){
                        Img.loadC(ewm, lmDetail.qr_code);
                    }else{
                        qrcodeView.setVisibility(View.GONE);
                        lineView.setVisibility(View.GONE);
                    }

                    if (lmDetail.audit_status == 1)//审核状态 1未通过 2已通过
                    {
                        edit.setText("审核中");
                        edit.setBackgroundColor(Color.parseColor("#D9D9D9"));
                    } else {
                        exit.setVisibility(View.VISIBLE);
                    }

                    String login_idS = App.get(Constant.KEY_USER_ID);
                    if (!TextUtils.isEmpty(login_idS)) {
                        int login_id = Integer.parseInt(login_idS);
                        if (login_id == lmDetail.leader.id) {//是盟主
                            exit.setText("申请解散联盟");
                        } else if (isCy(login_id, lmDetail.users)) {//是成员
                            exit.setText("申请退出联盟");

                        }
                    }
                    exit.setVisibility(View.GONE);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }

    private boolean isCy(int id, List<Data14> list) {
        for (int i = 0; i < list.size(); i++) {
            if (id == list.get(i).id) {
                return true;
            }
        }
        return false;
    }

}
