package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.yyz.cyuanw.R;

import butterknife.BindView;
import butterknife.OnClick;

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

    @OnClick({R.id.edit})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.edit:
                Intent intent = new Intent(LmDetailDetailActivity.this, LmDetailDetailEditActivity.class);
                startActivity(intent);
                break;
        }
    }
}
