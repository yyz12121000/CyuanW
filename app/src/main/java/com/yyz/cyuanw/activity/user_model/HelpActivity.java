package com.yyz.cyuanw.activity.user_model;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

public class HelpActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.id_oper_issue) LinearLayout issueView;
    @BindView(R.id.id_oper_phone) RelativeLayout phoneView;
    @BindView(R.id.id_oper_weixin) RelativeLayout weixinView;

    private TextView tvWeixinView;

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

    @OnClick({R.id.id_oper_phone})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.id_oper_phone:

                break;
        }
    }

}


