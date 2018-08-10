package com.yyz.cyuanw.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;

import butterknife.BindView;
import butterknife.OnClick;


public class GdsxActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView titleView;
    @BindView(R.id.item_1)
    RelativeLayout item_1;
    @BindView(R.id.item_2)
    RelativeLayout item_2;
    @BindView(R.id.item_3)
    RelativeLayout item_3;
    @BindView(R.id.item_4)
    RelativeLayout item_4;
    @BindView(R.id.item_5)
    RelativeLayout item_5;
    @BindView(R.id.item_6)
    RelativeLayout item_6;
    @BindView(R.id.item_7)
    RelativeLayout item_7;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gdsx;
    }

    @Override
    public void initView() {
        setTitle(titleView, "更多筛选");
        ((TextView) item_1.getChildAt(0)).setText("地区");
        ((TextView) item_2.getChildAt(0)).setText("车龄");
        ((TextView) item_3.getChildAt(0)).setText("里程");
        ((TextView) item_4.getChildAt(0)).setText("排放标准");
        ((TextView) item_5.getChildAt(0)).setText("变速箱");
        ((TextView) item_6.getChildAt(0)).setText("燃油类型");
        ((TextView) item_7.getChildAt(0)).setText("颜色");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.reset, R.id.sure})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.reset:

                break;
            case R.id.sure:
                finish();
                break;
        }
    }

}
