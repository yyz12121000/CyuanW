package com.yyz.cyuanw.activity.user_model;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.CyGlActivity;
import com.yyz.cyuanw.activity.WdLmActivity;
import com.yyz.cyuanw.adapter.MyShopListViewAdapter;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data2;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.bean.ShopInfo;
import com.yyz.cyuanw.bean.ShopListData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.Tools;
import com.yyz.cyuanw.view.CustomProgress;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class MyShopActivity extends BaseActivity {

    @BindView(R.id.id_iv_back)
    ImageView backView;
    @BindView(R.id.id_iv_photo)
    ImageView photoView;
    @BindView(R.id.id_iv_bg)
    ImageView bgView;
    @BindView(R.id.id_iv_phone)
    ImageView phoneView;
    @BindView(R.id.id_iv_confirm)
    ImageView confirmView;
    @BindView(R.id.id_iv_persion)
    ImageView persionView;
    @BindView(R.id.id_tv_name)
    TextView nameView;
    @BindView(R.id.id_tv_no)
    TextView noView;
    @BindView(R.id.id_tv_shop)
    TextView shopView;
    @BindView(R.id.id_tv_sign)
    TextView signView;
    @BindView(R.id.id_text1)
    TextView text1View;
    @BindView(R.id.id_text2)
    TextView text2View;
    @BindView(R.id.id_index1)
    View index1View;
    @BindView(R.id.id_index2)
    View index2View;
    @BindView(R.id.id_list_view)
    ListView listView;
    @BindView(R.id.black)
    View black;

    private MyShopListViewAdapter adapter;
    private ShopInfo shopInfo;
    private int id;

    private List<Data2> listItems = new ArrayList<>();
    private int type;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myshop;
    }

    @Override
    public void initView() {
        mImmersionBar.reset().transparentBar().init();
    }

    @Override
    public void initData() {
        id = getIntent().getIntExtra("id",0);
        getShopInfo();
        getShopCarList(type);

        adapter = new MyShopListViewAdapter(this,listItems,type);
        listView.setAdapter(adapter);
    }

    public void setViewData() {
        if (shopInfo != null) {

            if (StringUtil.isNotNull(shopInfo.profile_picture))
                Img.loadC(photoView, shopInfo.profile_picture);

            if (StringUtil.isNotNull(shopInfo.background_image))
                Img.loadP(bgView, shopInfo.background_image, R.mipmap.bg_user);

            noView.setText("NO."+shopInfo.broker_number);

            if (StringUtil.isNotNull(shopInfo.name)) {
                nameView.setText(shopInfo.name);
            } else {
                nameView.setText(shopInfo.phone);
            }

            if (StringUtil.isNotNull(shopInfo.bind_dealer)) {
                shopView.setText(shopInfo.bind_dealer);
            } else {
                shopView.setText("暂时还未绑定车商");
            }

            if (StringUtil.isNotNull(shopInfo.signature)) {
                signView.setText(shopInfo.signature);
            } else {
                signView.setText("暂时还没有个性签名");
            }

        }
    }

    @OnClick({R.id.id_iv_back,R.id.id_text1,R.id.id_text2,R.id.id_iv_phone})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.id_iv_back:

                finish();
                break;
            case R.id.id_iv_phone:

                if (shopInfo != null)
                    Tools.openSysPhone(this, shopInfo.phone);
                break;
            case R.id.id_text1:

                text1View.setTextColor(Color.parseColor("#EA6F5A"));
                text2View.setTextColor(Color.parseColor("#666666"));
                index1View.setVisibility(View.VISIBLE);
                index2View.setVisibility(View.INVISIBLE);

                type = 0;
                getShopCarList(type);
                break;
            case R.id.id_text2:

                text2View.setTextColor(Color.parseColor("#EA6F5A"));
                text1View.setTextColor(Color.parseColor("#666666"));
                index2View.setVisibility(View.VISIBLE);
                index1View.setVisibility(View.INVISIBLE);

                type = 1;
                getShopCarList(type);
                break;

        }
    }

    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void getShopInfo() {
        //CustomProgress.show(this, "加载中...", false, null);

        HttpData.getInstance().getShopInfo(id, new Observer<HttpResult<ShopInfo>>() {
            @Override
            public void onCompleted() {
                //CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                //CustomProgress.dismis();
                App.showToast("服务器请求超时");
            }

            @Override
            public void onNext(HttpResult<ShopInfo> result) {
                if (result.status == 200 && result.data != null) {
                    shopInfo = result.data;
                    setViewData();
                } else {
                    App.showToast(result.message);
                }
            }
        });
    }

    public void getShopCarList(int type) {
        CustomProgress.show(this, "加载中...", false, null);

        HttpData.getInstance().getShopList(type,id, 1,new Observer<HttpResult<ShopListData>>() {
            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
                LogManager.e("解析出错" + e.getMessage());
                //App.showToast("服务器请求超时");
            }

            @Override
            public void onNext(HttpResult<ShopListData> result) {
                if (result.status == 200 && result.data != null) {

                    if (result.data.info.size() > 0){
                        black.setVisibility(View.GONE);
                        listItems.clear();
                        listItems.addAll(result.data.info);
                        adapter.setFlag(type);
                        adapter.notifyDataSetChanged();
                    }else{
                        listItems.clear();
                        adapter.notifyDataSetChanged();
                        black.setVisibility(View.VISIBLE);
                    }

                } else {
                    App.showToast(result.message);
                }
            }
        });
    }

}


