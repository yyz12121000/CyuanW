package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data3;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.tools.LogManager;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;


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

    private boolean hasData = false;
    private Data3 data3;

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

        ((TextView) item_1.getChildAt(1)).setHint("请选择");
        ((TextView) item_2.getChildAt(1)).setHint("请选择");
        ((TextView) item_3.getChildAt(1)).setHint("请选择");
        ((TextView) item_4.getChildAt(1)).setHint("请选择");
        ((TextView) item_5.getChildAt(1)).setHint("请选择");
        ((TextView) item_6.getChildAt(1)).setHint("请选择");
        ((TextView) item_7.getChildAt(1)).setHint("请选择");


    }

    @Override
    public void initData() {
        dictionary();
    }

    @OnClick({R.id.reset, R.id.sure, R.id.item_1, R.id.item_2, R.id.item_3, R.id.item_4, R.id.item_5, R.id.item_6, R.id.item_7})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.item_2:
                Intent intent = new Intent(this, ListChooseActivity.class);

                startActivityForResult(intent, 2);
                break;
            case R.id.item_3:

                break;
            case R.id.item_4:
                Intent intent4 = new Intent(this, ListChooseActivity.class);
                intent4.putStringArrayListExtra("list", data3.emission_standard);
                intent4.putExtra("title", "选择排放标准");
                startActivityForResult(intent4, 4);
                break;
            case R.id.item_5:
                Intent intent5 = new Intent(this, ListChooseActivity.class);
                intent5.putStringArrayListExtra("list", data3.gearbox);
                intent5.putExtra("title", "选择变速箱");
                startActivityForResult(intent5, 5);
                break;
            case R.id.item_6:
                Intent intent6 = new Intent(this, ListChooseActivity.class);
                intent6.putStringArrayListExtra("list", data3.fuel_type);
                intent6.putExtra("title", "选择燃油类型");
                startActivityForResult(intent6, 6);
                break;
            case R.id.item_7:
                Intent intent7 = new Intent(this, ListChooseActivity.class);
                intent7.putStringArrayListExtra("list", data3.color);
                intent7.putExtra("title", "选择颜色");
                startActivityForResult(intent7, 7);
                break;
            case R.id.reset:

                break;
            case R.id.sure:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case 2:
                break;
            case 3:
                break;
            case 4:
                String text4 = data.getStringExtra("text");
                ((TextView) item_4.getChildAt(1)).setText(text4);
                break;
            case 5:
                String text5 = data.getStringExtra("text");
                ((TextView) item_5.getChildAt(1)).setText(text5);
                break;
            case 6:
                String text6 = data.getStringExtra("text");
                ((TextView) item_6.getChildAt(1)).setText(text6);
                break;
            case 7:
                String text7 = data.getStringExtra("text");
                ((TextView) item_7.getChildAt(1)).setText(text7);
                break;
        }
    }

    public void dictionary() {
        HttpData.getInstance().dictionary(new Observer<HttpResult<Data3>>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                LogManager.e("解析出错" + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Data3> result) {
                if (result.status == 200) {
                    data3 = result.data;
                    hasData = true;
//                    adapter.setData(result.data.info);
//                    adapter.setData(result.data.info);
//                    adapter.startBanner(result.data.ads);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }
}
