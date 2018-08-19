package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.CheyListData;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LmDetail;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class LmDetailDetailActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView id_tv_title;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.cjr)
    TextView cjr;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.ewm)
    ImageView ewm;

    private int lm_id;

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
        int lm_id = getIntent().getIntExtra("id", -1);
        if (lm_id != -1) {
            load(lm_id);
        }

    }

    @OnClick({R.id.edit})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.edit:
                Intent intent = new Intent(LmDetailDetailActivity.this, LmDetailDetailEditActivity.class);
                intent.putExtra(LmDetailDetailEditActivity.TYPE, LmDetailDetailEditActivity.TYPE_EDIT);
                startActivity(intent);
                break;
        }
    }

    private void load(int id) {
        HttpData.getInstance().lmDetail(id, new Observer<HttpResult<LmDetail>>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                LogManager.e(e.getMessage());
            }

            @Override
            public void onNext(HttpResult<LmDetail> result) {
                if (result.status == 200) {
                    Img.loadC(img, result.data.logo);
                    name.setText(result.data.name);
                    desc.setText(result.data.intro);
                    cjr.setText(result.data.leader.real_name);
                    Img.loadC(ewm, result.data.qr_code);
                    address.setText(result.data.province_id + " " + result.data.city_id + " " + result.data.region_id);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }
}
