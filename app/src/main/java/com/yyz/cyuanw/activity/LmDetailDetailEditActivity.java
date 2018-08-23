package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LmMyListData;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class LmDetailDetailEditActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView id_tv_title;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.edit_name)
    EditText edit_name;
    @BindView(R.id.desc)
    EditText desc;
    @BindView(R.id.er_code)
    ImageView er_code;
    @BindView(R.id.img)
    ImageView img;


    public static final String TYPE = "type";
    public static final int TYPE_CREATE = 1;//创建联盟
    public static final int TYPE_EDIT = 2;//编辑联盟

    private   int type ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lm_detail_detail_edit;
    }

    @Override
    public void initView() {
        type = getIntent().getIntExtra(TYPE, -1);
        switch (type) {
            case 1:
                setTitle(id_tv_title, "创建联盟");
                save.setText("创建联盟");
                save.setBackgroundResource(R.drawable.bt_blue_bg);
                findViewById(R.id.hint_1).setVisibility(View.VISIBLE);
                findViewById(R.id.hint_2).setVisibility(View.VISIBLE);
                break;
            case 2:
                setTitle(id_tv_title, "联盟资料编辑");
                save.setText("保存修改");
                save.setBackgroundResource(R.drawable.bt_yellow_bg);
                break;
            default:
                finish();
        }

    }

    @Override
    public void initData() {
        if(type == TYPE_EDIT){
            Intent intent = getIntent();
            String imgS = intent.getStringExtra("img");
            String nameS = intent.getStringExtra("name");
            String descS = intent.getStringExtra("desc");
            String ewmS = intent.getStringExtra("ewm");

            Img.loadC(img,imgS);
            edit_name.setText(nameS);
            desc.setText(descS);
        }

    }

    @OnClick({R.id.save})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.save:
                String name = edit_name.getText().toString();
                String descStr = desc.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(descStr)){

                }
                break;
        }
    }

    /*private void submit(){
            HttpData.getInstance().createLm(0, new Observer<HttpResult<String>>() {
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
                public void onNext(HttpResult<String> result) {
                    if (result.status == 200) {
                        adapter.vaHolder.myLmListAdapter.setData(result.data);
                    } else {
//                    App.showToast(result.message);
                    }
                }
            });

    }*/

}
