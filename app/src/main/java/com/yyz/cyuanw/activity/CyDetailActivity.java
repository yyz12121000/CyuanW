package com.yyz.cyuanw.activity;

import android.widget.TextView;

import com.yyz.cyuanw.R;

import butterknife.BindView;

public class CyDetailActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView title;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cy_detail;
    }

    @Override
    public void initView() {
        title.setText("车源详情");
    }

    @Override
    public void initData() {

    }
}
