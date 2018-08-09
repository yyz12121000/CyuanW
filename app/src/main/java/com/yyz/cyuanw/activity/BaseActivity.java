package com.yyz.cyuanw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.interf.BaseViewInterface;
import com.yyz.cyuanw.view.SwipeBack.SwipeBackActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends SwipeBackActivity implements BaseViewInterface{

    protected ImmersionBar mImmersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSwipeBackEnable(true);

        mImmersionBar = ImmersionBar.with(this);
        //mImmersionBar.fitsSystemWindows(true).statusBarColor(R.color.color_statusbar_bg).init();

        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        ButterKnife.bind(this);

        initView();
        initData();
    }

    protected int getLayoutId() {
        return 0;
    }

    public void setTitle(TextView textView,String strTitle){
        textView.setText(strTitle);
    }

    public void backBtnClick(View view){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }


}
