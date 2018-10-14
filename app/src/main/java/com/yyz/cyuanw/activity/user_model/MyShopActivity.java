package com.yyz.cyuanw.activity.user_model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.CyDetailActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data2;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.ShopInfo;
import com.yyz.cyuanw.bean.ShopListData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.ToastUtil;
import com.yyz.cyuanw.tools.Tools;
import com.yyz.cyuanw.tools.WX;
import com.yyz.cyuanw.view.CommonPopupDialog;
import com.yyz.cyuanw.view.CustomProgress;
import com.yyz.cyuanw.view.PullRV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.id_iv_share)
    ImageView shareView;
    @BindView(R.id.xrefreshview)
    PullRV pullRV;

    private ListViewAdapter adapter;
    private ShopInfo shopInfo;
    private int id;

    private List<Data2> listItems = new ArrayList<>();
    private int type;

    private View popupDialogView;
    private CommonPopupDialog mPopupDialog;

    private WX wx;

    private boolean hideCome = true;

    private Dialog mDialog;

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
        if (id == Integer.parseInt(App.get(Constant.KEY_USER_ID))){
            hideCome = false;
        }

        pullRV.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRV.startRefresh();
            }
        }, 500);

        pullRV.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                getShopCarList(true,type, pullRV.page = 1);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                getShopCarList(false,type, ++pullRV.page);
            }
        });

        getShopInfo();
        //getShopCarList(type,1);

        adapter = new ListViewAdapter(this,listItems);
        listView.setAdapter(adapter);

        wx = new WX();
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

    @OnClick({R.id.id_iv_back,R.id.id_text1,R.id.id_text2,R.id.id_iv_phone,R.id.id_iv_share})
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
                getShopCarList(true,type,1);
                break;
            case R.id.id_text2:

                text2View.setTextColor(Color.parseColor("#EA6F5A"));
                text1View.setTextColor(Color.parseColor("#666666"));
                index2View.setVisibility(View.VISIBLE);
                index1View.setVisibility(View.INVISIBLE);

                type = 1;
                getShopCarList(true,type,1);
                break;
            case R.id.id_iv_share:

                showPopupDialog();
                break;

        }
    }

    public void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    public void showPopupDialog() {
        if (null == shopInfo){
            ToastUtil.show(this,"暂无店铺信息");
            return;
        }
        if (popupDialogView == null) {
            popupDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_share_wx, null);

            popupDialogView.findViewById(R.id.id_root_view).setOnClickListener(view -> mPopupDialog.dismiss());


            popupDialogView.findViewById(R.id.wx_1).setOnClickListener(view -> {
                wx.share(false, shopInfo.share_info.title, shopInfo.share_info.content,  shopInfo.share_info.img, shopInfo.share_info.url);
                mPopupDialog.dismiss();
            });

            popupDialogView.findViewById(R.id.wx_2).setOnClickListener(view -> {
                wx.share(true,  shopInfo.share_info.title, shopInfo.share_info.content,  shopInfo.share_info.img, shopInfo.share_info.url);
                mPopupDialog.dismiss();
            });
        }

        if (mPopupDialog == null) {
            mPopupDialog = new CommonPopupDialog(this, android.R.style.Theme_Panel);
            mPopupDialog.setCanceledOnTouchOutside(true);
            mPopupDialog.setContentView(popupDialogView);
        }


        mPopupDialog.showAtLocation(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

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
                App.showToast("店铺不存在");
                finish();
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

    public void getShopCarList(boolean isRefresh,int type,int page) {
        CustomProgress.show(this, "加载中...", false, null);

        HttpData.getInstance().getShopList(type,id, page,new Observer<HttpResult<ShopListData>>() {
            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
                pullRV.stopRefresh();
                LogManager.e("解析出错" + e.getMessage());
                //App.showToast("服务器请求超时");
            }

            @Override
            public void onNext(HttpResult<ShopListData> result) {
                if (result.status == 200 && result.data != null) {

                    if (isRefresh) {
                        adapter.setData(result.data.info);
                        pullRV.stopRefresh();
                    } else {
                        adapter.appendData(result.data.info);
                        pullRV.checkhasMore(result.data.info.size());
                    }

//                    if (result.data.info.size() > 0){
//                        black.setVisibility(View.GONE);
//                        listItems.clear();
//                        listItems.addAll(result.data.info);
//                        adapter.notifyDataSetChanged();
//                    }else{
//                        listItems.clear();
//                        adapter.notifyDataSetChanged();
//                        black.setVisibility(View.VISIBLE);
//                    }

                } else {
                    App.showToast(result.message);
                }
            }
        });
    }

    public void showLyDialog(Data2 data) {
        if (mDialog == null) {
            mDialog = new AlertDialog.Builder(this, R.style.MyDialog).create();
        }
        mDialog.show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_llcy, null);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setContentView(dialogView);

        if (data != null){
            ((TextView)dialogView.findViewById(R.id.id_tv_name)).setText(data.real_name);
            ((TextView)dialogView.findViewById(R.id.id_tv_address)).setText(data.dealer_name);
            ((TextView)dialogView.findViewById(R.id.id_tv_phone)).setText(data.phone);
            ((TextView)dialogView.findViewById(R.id.id_tv_shortphone)).setText(data.virtual_number);
            ((TextView)dialogView.findViewById(R.id.id_tv_price1)).setText(data.retail_offer+"万");

            if (Double.parseDouble(data.retail_bottom_price) > 0){
                ((TextView)dialogView.findViewById(R.id.id_tv_price2)).setText("底价: "+data.retail_bottom_price+"万");
            }else{
                ((TextView)dialogView.findViewById(R.id.id_tv_price2)).setVisibility(View.GONE);
            }

            dialogView.findViewById(R.id.to_wx).setVisibility(View.GONE);

            dialogView.findViewById(R.id.see_all_cy).setOnClickListener(view -> {
                Intent intent = new Intent(MyShopActivity.this, MyShopActivity.class);
                intent.putExtra("id",data.belong_id);
                startActivity(intent);

                mDialog.dismiss();
            });
        }

    }

    class ListViewAdapter extends BaseAdapter {
        private Context context;
        public LayoutInflater flater;
        private List<Data2> data;

        public ListViewAdapter(Context context, List<Data2> list) {
            this.context = context;
            this.flater = LayoutInflater.from(context);
            this.data = list;
        }

        public void setData(List<Data2> data) {
            if (null != data) {
                this.data = data;

                if (this.data.size() == 0) {
                    black.setVisibility(View.VISIBLE);
                } else {
                    black.setVisibility(View.GONE);
                }

                notifyDataSetChanged();
            }
        }

        public void appendData(List<Data2> data) {
            if (null != data) {
                this.data.addAll(data);
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                convertView = flater.inflate(R.layout.list_item_myshop, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Data2 data2 = data.get(position);

            Img.loadD(holder.imageView, data2.cover,R.mipmap.ic_cy);
            holder.titleView.setText(data2.name);
            holder.spView.setText(data2.license_plate_time);
            holder.glView.setText(data2.mileage + "公里");
            holder.ddView.setText(data2.location);
            holder.priceView.setText(data2.retail_offer + "万");
            holder.timeView.setText(data2.publish_time);

            if (data2.is_new_car == 1)//是否新车 0否 1是
            {
                holder.newView.setVisibility(View.VISIBLE);
            } else {
                holder.newView.setVisibility(View.GONE);
            }
            if (data2.urgent == 1)//急售 0否 1是
            {
                holder.jView.setVisibility(View.VISIBLE);
            } else {
                holder.jView.setVisibility(View.GONE);
            }
            if (data2.wholesale == 1)//批发 0否 1是
            {
                holder.pView.setVisibility(View.VISIBLE);
            } else {
                holder.pView.setVisibility(View.GONE);
            }

            if (type == 0){
                holder.comeView.setVisibility(View.INVISIBLE);
            }else{
                if (hideCome){
                    holder.comeView.setVisibility(View.INVISIBLE);
                }else{
                    holder.comeView.setVisibility(View.VISIBLE);
                }

            }

            holder.comeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showLyDialog(data2);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, CyDetailActivity.class);
                    intent.putExtra("id", data2.id);
                    if (hideCome == false){
                        if (type == 1){
                            intent.putExtra("from_type",6);
                        }else{
                            intent.putExtra("from_type",3);
                        }
                    }else{
                        intent.putExtra("from_type",4);
                    }
                    context.startActivity(intent);
                }
            });


            return convertView;
        }

        class ViewHolder {

            @BindView(R.id.gx_iv)
            ImageView imageView;
            @BindView(R.id.the_new)
            ImageView newView;
            @BindView(R.id.tv_title)
            TextView titleView;
            @BindView(R.id.the_come)
            ImageView comeView;
            @BindView(R.id.tv_sp)
            TextView spView;
            @BindView(R.id.tv_gl)
            TextView glView;
            @BindView(R.id.tv_dd)
            TextView ddView;
            @BindView(R.id.tv_jg)
            TextView priceView;
            @BindView(R.id.iv_j)
            ImageView jView;
            @BindView(R.id.iv_p)
            ImageView pView;
            @BindView(R.id.gx_time)
            TextView timeView;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }

}


