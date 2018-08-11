package com.yyz.cyuanw.activity;

import android.widget.TextView;

import com.yyz.cyuanw.R;

import butterknife.BindView;

public class LmDetailDetailEditActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView id_tv_title;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_lm_detail_detail_edit;
    }

    @Override
    public void initView() {

        setTitle(id_tv_title, "联盟资料编辑");

    }

    @Override
    public void initData() {
    }

}
