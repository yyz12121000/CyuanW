package com.yyz.cyuanw.activity;

import android.widget.TextView;

import com.yyz.cyuanw.R;

import butterknife.BindView;

public class LmDetailDetailActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView id_tv_title;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_lm_detail_detail;
    }

    @Override
    public void initView() {

        setTitle(id_tv_title, "联盟详情");

    }

    @Override
    public void initData() {
    }

}
