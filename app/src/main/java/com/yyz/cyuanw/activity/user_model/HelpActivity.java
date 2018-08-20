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

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.ImageTools;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.Tools;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_issue) LinearLayout issueView;
    @BindView(R.id.id_oper_phone) RelativeLayout phoneView;
    @BindView(R.id.id_oper_weixin) RelativeLayout weixinView;

    private TextView tvWeixinView;

    private Dialog mDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    public void initView() {
        setTitle(titleView,"帮助中心");
        ((TextView)issueView.getChildAt(0)).setText("常见问题");
        ((TextView)phoneView.getChildAt(0)).setText("联系电话");
        ((TextView)phoneView.getChildAt(1)).setText("0574-88273310");
        ((TextView)weixinView.getChildAt(0)).setText("客服微信");

        tvWeixinView = (TextView) weixinView.getChildAt(1);
        tvWeixinView.setText("CheyuanCN  ");
        tvWeixinView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.ic_ewm,0);

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_oper_phone,R.id.id_oper_issue,R.id.id_oper_weixin})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_phone:

                Tools.openSysPhone(this, Constant.CONSTANT_CONNECTION_PHONE);
                break;
            case R.id.id_oper_issue:

                startActivity(new Intent(this,QuestionActivity.class));
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

        dialogView.findViewById(R.id.id_tv_ok).setOnClickListener(view -> mDialog.dismiss() );
    }

}


