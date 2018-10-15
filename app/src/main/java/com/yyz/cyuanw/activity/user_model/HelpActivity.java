package com.yyz.cyuanw.activity.user_model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.bean.SettingData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.ImageTools;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.Tools;
import com.yyz.cyuanw.view.CustomProgress;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class HelpActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_issue) LinearLayout issueView;
    @BindView(R.id.id_oper_phone) RelativeLayout phoneView;
    @BindView(R.id.id_oper_weixin) RelativeLayout weixinView;

    private TextView tvPhoneView;
    private TextView tvWeixinView;

    private Dialog mDialog;

    private SettingData data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public void initView() {
        setTitle(titleView,"帮助中心");
        ((TextView)issueView.getChildAt(0)).setText("常见问题");
        ((TextView)phoneView.getChildAt(0)).setText("联系电话");
        ((TextView)weixinView.getChildAt(0)).setText("客服微信");

        tvPhoneView = (TextView)phoneView.getChildAt(1);
        tvWeixinView = (TextView) weixinView.getChildAt(1);
        tvWeixinView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_ewm,0);

    }

    @Override
    public void initData() {
        getData();
    }

    @OnClick({R.id.id_oper_phone,R.id.id_oper_issue,R.id.id_oper_weixin})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_phone:

                Tools.openSysPhone(this, tvPhoneView.getText().toString());
                break;
            case R.id.id_oper_issue:

                Intent intent = new Intent(this,QuestionActivity.class);
                if (data != null){
                    intent.putExtra("url",data.faq_url);
                }
                startActivity(intent);
                break;
            case R.id.id_oper_weixin:

                showWeixinDialog();
                break;
        }
    }

    public void showWeixinDialog(){
        if (mDialog == null){
            mDialog = new AlertDialog.Builder(this,R.style.MyDialog).create();
        }
        mDialog.show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_weixin, null);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getWindow().setContentView(dialogView);

        ImageView ivCode = dialogView.findViewById(R.id.id_iv_code);
        TextView tvName = dialogView.findViewById(R.id.id_tv_text);

        if (data != null){
            Img.load(ivCode,data.customer_service_qr_code);
            tvName.setText(getString(R.string.str_help_weixin,data.customer_service_wechat));
        }

        dialogView.findViewById(R.id.id_tv_ok).setOnClickListener(view -> mDialog.dismiss() );
    }

    public void getData(){

        HttpData.getInstance().getSettingData(new Observer<HttpResult<SettingData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<SettingData> result) {

                if (result.status == 200 && result.data != null){
                    data = result.data;

                    tvPhoneView.setText(data.customer_service_phone);
                    tvWeixinView.setText(data.customer_service_wechat+"  ");
                }

            }
        });
    }
}


