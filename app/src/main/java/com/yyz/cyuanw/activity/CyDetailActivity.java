package com.yyz.cyuanw.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyz.cyuanw.R;

import butterknife.BindView;

public class CyDetailActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView title;
    @BindView(R.id.title_right_icon)
    ImageView right_icon;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_cy_detail;
    }

    @Override
    public void initView() {
        title.setText("车源详情");
        right_icon.setImageResource(R.mipmap.img_25);
        right_icon.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

    }
}
