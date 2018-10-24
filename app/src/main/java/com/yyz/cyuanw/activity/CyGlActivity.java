package com.yyz.cyuanw.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.fragment.CyFragment;
import com.yyz.cyuanw.activity.user_model.SendCarActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data2;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.bean.PreparationData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.ToastUtil;
import com.yyz.cyuanw.tools.Tools;
import com.yyz.cyuanw.view.CommonPopupDialog;
import com.yyz.cyuanw.view.CustomProgress;
import com.yyz.cyuanw.view.PullRV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class CyGlActivity extends BaseActivity {

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.black)
    View black;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.xrefreshview)
    PullRV pullRV;
    @BindView(R.id.search_text)
    EditText search_text;


    private ListAdapter adapter;
    private String key_word = "";//搜索关键词

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cygl;
    }

    @Override
    public void initView() {

        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter();
        list.setAdapter(adapter);


        pullRV.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRV.startRefresh();
            }
        }, 500);
        pullRV.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                searchCy(true, pullRV.page = 1);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                searchCy(false, ++pullRV.page);
            }
        });
        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    String text = search_text.getText().toString();
                    Tools.hideSoftInput(CyGlActivity.this, search_text);

                    key_word = text;
                    searchCy(true, pullRV.page = 1);
                }
                return false;
            }

        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv1, R.id.tv2,R.id.title_right})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv1:
                tv1.setTextColor(Color.parseColor("#ffffff"));
                tv2.setTextColor(Color.parseColor("#EA6F5A"));
                tv1.setBackgroundResource(R.drawable.bg_4);
                tv2.setBackground(null);
                type = 0;
                searchCy(true, pullRV.page = 1);
                break;
            case R.id.tv2:
                tv1.setTextColor(Color.parseColor("#EA6F5A"));
                tv2.setTextColor(Color.parseColor("#ffffff"));
                tv1.setBackground(null);
                tv2.setBackgroundResource(R.drawable.bg_5);
                type = 1;
                searchCy(true, pullRV.page = 1);
                break;
            case R.id.title_right:
                String jsonStr = App.get(Constant.KEY_USER_DATA);
                if (StringUtil.isNotNull(jsonStr)) {
                    LoginData userData = new Gson().fromJson(jsonStr, LoginData.class);
                    if (userData.publish_car == 1){
                        startActivity(new Intent(this, SendCarActivity.class));
                    }else{
                        App.showToast(userData.publish_message);
                    }
                }
                break;
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Data2> data = new ArrayList<>();

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

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_cy, viewGroup, false);
            ListAdapter.VHolder vHolder = new ListAdapter.VHolder(view);
            return vHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ((ListAdapter.VHolder) viewHolder).setData();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class VHolder extends RecyclerView.ViewHolder {
            private ImageView gx_iv, the_new, iv_j, iv_p;
            private TextView tv_title, tv_sp, tv_gl, tv_dd, tv_jg, gx_time;
            private Button sx, gj, gd, szws, sc;

            public VHolder(@NonNull View itemView) {
                super(itemView);
                gx_iv = itemView.findViewById(R.id.gx_iv);
                the_new = itemView.findViewById(R.id.the_new);
                iv_j = itemView.findViewById(R.id.iv_j);
                iv_p = itemView.findViewById(R.id.iv_p);

                tv_title = itemView.findViewById(R.id.tv_title);
                tv_sp = itemView.findViewById(R.id.tv_sp);
                tv_gl = itemView.findViewById(R.id.tv_gl);
                tv_dd = itemView.findViewById(R.id.tv_dd);
                tv_jg = itemView.findViewById(R.id.tv_jg);
                gx_time = itemView.findViewById(R.id.gx_time);

                sx = itemView.findViewById(R.id.sx);
                gj = itemView.findViewById(R.id.gj);
                gd = itemView.findViewById(R.id.gd);
                szws = itemView.findViewById(R.id.szws);
                sc = itemView.findViewById(R.id.sc);

                sx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //刷新
                        int position = getAdapterPosition();
                        refreshCar(data.get(position).id);
                    }
                });
                gj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //编辑
                        int position = getAdapterPosition();
                        Intent intent = new Intent(CyGlActivity.this,SendCarActivity.class);
                        intent.putExtra("flag", 1);
                        intent.putExtra("id", data.get(position).id);
                        startActivity(intent);
                    }
                });
                gd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Data2 data2 = data.get(position);

                        showPopupDialog(data2);
                    }
                });
                szws.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //设置未售
                        int position = getAdapterPosition();
                        updateSold(data.get(position).id);
                    }
                });
                sc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //删除
                        int position = getAdapterPosition();
                        delCar(data.get(position).id);
                    }
                });

//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        int position = getAdapterPosition();
//                        Intent intent = new Intent(CyGlActivity.this, CyDetailActivity.class);
//                        intent.putExtra("id", data.get(position).id);
//                        startActivity(intent);
//                    }
//                });

                itemView.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent(CyGlActivity.this, CyDetailActivity.class);
                        intent.putExtra("id", data.get(position).id);
                        intent.putExtra("from_type",5);
                        CyGlActivity.this.startActivity(intent);
                    }
                });
            }

            public void setData() {
                int position = getAdapterPosition();
                Data2 data2 = data.get(position);

                Img.loadD(gx_iv, data2.cover,R.mipmap.ic_cy);
                tv_title.setText(data2.name);
                tv_sp.setText(data2.license_plate_time);
                tv_gl.setText(data2.mileage + "公里");
                tv_dd.setText(data2.location);
                tv_jg.setText(data2.retail_offer + "万");
                gx_time.setText(data2.publish_time);

                if (data2.is_new_car == 1)//是否新车 0否 1是
                {
                    the_new.setVisibility(View.VISIBLE);
                } else {
                    the_new.setVisibility(View.GONE);
                }
                if (data2.urgent == 1)//急售 0否 1是
                {
                    iv_j.setVisibility(View.VISIBLE);
                } else {
                    iv_j.setVisibility(View.GONE);
                }
                if (data2.wholesale == 1)//批发 0否 1是
                {
                    iv_p.setVisibility(View.VISIBLE);
                } else {
                    iv_p.setVisibility(View.GONE);
                }

                if (type == 0) {
                    sx.setVisibility(View.VISIBLE);
                    gj.setVisibility(View.VISIBLE);
                    gd.setVisibility(View.VISIBLE);

                    szws.setVisibility(View.GONE);
                    sc.setVisibility(View.GONE);
                } else {
                    sx.setVisibility(View.GONE);
                    gj.setVisibility(View.GONE);
                    gd.setVisibility(View.GONE);

                    szws.setVisibility(View.VISIBLE);
                    sc.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    private View popupDialogView;
    private CommonPopupDialog mPopupDialog;
    private int type = 0;//是否已售 0否 1是
    private Dialog mDialog1,mDialog2,mDialog3;
    private TextView js,pf,zb,szys;

    public void showPopupDialog(Data2 data) {
        if (popupDialogView == null) {
            popupDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_1, null);
            popupDialogView.findViewById(R.id.id_root_view).setOnClickListener(view -> mPopupDialog.dismiss());
            pf = popupDialogView.findViewById(R.id.pf);
            zb = popupDialogView.findViewById(R.id.zb);
            szys = popupDialogView.findViewById(R.id.szys);
            js = popupDialogView.findViewById(R.id.js);

        }

        pf.setOnClickListener(view -> {
            if (data.wholesale == 1){
                showDialog3(data);
            }else{
                showDialog1(data);
            }
            mPopupDialog.dismiss();

        });
        zb.setOnClickListener(view -> {
            showDialog2(data.id);
            mPopupDialog.dismiss();
        });
        szys.setOnClickListener(view -> {
            updateSold(data.id);
            mPopupDialog.dismiss();
        });
        js.setOnClickListener(view -> {
            updateUrgent(data.id);
            mPopupDialog.dismiss();
        });

        if (data.urgent == 1){
            js.setText("取消急售");
            js.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.js,0,0);
        }else{
            js.setText("急售");
            js.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.img_001,0,0);
        }

        if (data.wholesale == 1){
            pf.setText("修改批发");
            pf.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.pf,0,0);
        }else{
            pf.setText("批发");
            pf.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.img_002,0,0);
        }

        if (mPopupDialog == null) {
            mPopupDialog = new CommonPopupDialog(this, android.R.style.Theme_Panel);
            mPopupDialog.setCanceledOnTouchOutside(true);
            mPopupDialog.setContentView(popupDialogView);
        }
        mPopupDialog.showAtLocation(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }
    public void showDialog1(Data2 data) {
        if (mDialog1 == null) {
            mDialog1 = new AlertDialog.Builder(this, R.style.MyDialog).create();
        }
        mDialog1.show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_a1, null);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        mDialog1.setCanceledOnTouchOutside(true);
        mDialog1.getWindow().setContentView(dialogView);
        mDialog1.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mDialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        EditText pfjg = dialogView.findViewById(R.id.pfjg);

        dialogView.findViewById(R.id.submit).setOnClickListener(view -> {

            String strContent = pfjg.getText().toString().trim();
            if (!StringUtil.isNotNull(strContent)){
                App.showToast("请输入正确的批发价格");
                return;
            }

            wholesale(data.id,Double.parseDouble(strContent));

            mDialog1.dismiss();
        });

    }
    public void showDialog2(int id) {
        if (mDialog2 == null) {
            mDialog2 = new AlertDialog.Builder(this, R.style.MyDialog).create();
        }
        mDialog2.show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_a2, null);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        mDialog2.setCanceledOnTouchOutside(true);
        mDialog2.getWindow().setContentView(dialogView);
        mDialog2.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mDialog2.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        EditText sgjg = dialogView.findViewById(R.id.sgjg);
        EditText zbjg = dialogView.findViewById(R.id.zbjg);
        EditText ms = dialogView.findViewById(R.id.ms);

        getPreparation(id,sgjg,zbjg,ms);

        dialogView.findViewById(R.id.submit).setOnClickListener(view -> {

            double price1 = 0;
            double price2 = 0;

            if (StringUtil.isNotNull(sgjg.getText().toString())){
                price1 = Double.parseDouble(sgjg.getText().toString());
            }
            if (StringUtil.isNotNull(zbjg.getText().toString())){
                price2 = Double.parseDouble(zbjg.getText().toString());
            }

            preparation(id,price1,price2,ms.getText().toString());

            mDialog2.dismiss();
        });

    }

    public void showDialog3(Data2 data) {
        if (mDialog3 == null) {
            mDialog3 = new AlertDialog.Builder(this, R.style.MyDialog).create();
        }
        mDialog3.show();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_a3, null);
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        mDialog3.setCanceledOnTouchOutside(true);
        mDialog3.getWindow().setContentView(dialogView);
        mDialog3.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        mDialog3.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        EditText pfjg = dialogView.findViewById(R.id.pfjg);
        TextView pf = dialogView.findViewById(R.id.pf);
        pf.setText(getString(R.string.str_pf,data.wholesale_offer));
        pfjg.setText(data.wholesale_offer+"万");
        pfjg.setSelection(pfjg.getText().toString().length());

        dialogView.findViewById(R.id.submit).setOnClickListener(view -> {

            String strContent = pfjg.getText().toString().trim();
            if (!StringUtil.isNotNull(strContent)){
                App.showToast("请输入正确的批发价格");
                return;
            }

            wholesale(data.id,Double.parseDouble(strContent));

            mDialog3.dismiss();
        });

        dialogView.findViewById(R.id.cancel).setOnClickListener(view -> {

            wholesale(data.id,0);

            mDialog3.dismiss();
        });

    }

    public void searchCy(boolean isRefresh, int page) {
        HttpData.getInstance().searchCyGl(type, key_word, page, new Observer<HttpResult<Data1>>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                pullRV.stopRefresh();
                LogManager.e("解析出错" + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Data1> result) {
                if (result.status == 200) {
                    if (isRefresh) {
                        adapter.setData(result.data.info);
                        pullRV.stopRefresh();
                    } else {
                        adapter.appendData(result.data.info);
                        pullRV.checkhasMore(result.data.info.size());
                    }

//                    adapter.setData(result.data.info);
//                    adapter.startBanner(result.data.ads);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }

    public void refreshCar(int id) {
        HttpData.getInstance().refreshCar(id, App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpCodeResult result) {
                if (result.status == 200) {
                    App.showToast(result.message);

                    pullRV.startRefresh();
                }
            }
        });
    }

    public void delCar(int id) {
        CustomProgress.show(this, "删除中...", false, null);
        HttpData.getInstance().delCar(id, App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {

            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
            }

            @Override
            public void onNext(HttpCodeResult result) {
                if (result.status == 200) {
                    App.showToast(result.message);

                    pullRV.startRefresh();
                }
            }
        });
    }

    public void updateSold(int id) {
        CustomProgress.show(this, "修改中...", false, null);
        HttpData.getInstance().updateSold(id, App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {

            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
            }

            @Override
            public void onNext(HttpCodeResult result) {
                if (result.status == 200) {
                    App.showToast(result.message);

                    pullRV.startRefresh();
                }
            }
        });
    }


    public void updateUrgent(int id) {
        CustomProgress.show(this, "修改中...", false, null);
        HttpData.getInstance().updateUrgent(id, App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {

            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
            }

            @Override
            public void onNext(HttpCodeResult result) {
                if (result.status == 200) {
                    App.showToast(result.message);

                    pullRV.startRefresh();
                }
            }
        });
    }

    public void wholesale(int id,double price) {
        CustomProgress.show(this, "请稍等...", false, null);
        HttpData.getInstance().wholesale(id,price, App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {

            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
            }

            @Override
            public void onNext(HttpCodeResult result) {
                if (result.status == 200) {
                    App.showToast(result.message);

                    pullRV.startRefresh();
                }
            }
        });
    }

    public void getPreparation(int id,EditText et1,EditText et2,EditText et3) {
        HttpData.getInstance().getPreparation(id, new Observer<HttpResult<PreparationData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpResult<PreparationData> result) {
                if (result.status == 200) {
                    et1.setText(result.data.purchasing_price);
                    et1.setSelection(et1.getText().toString().length());
                    et2.setText(result.data.preparation_price);
                    et3.setText(result.data.preparation_describe);
                }
            }
        });
    }

    public void preparation(int id,double price1,double price2,String content) {
        CustomProgress.show(this, "修改中...", false, null);
        HttpData.getInstance().preparation(id, price1,price2,content,new Observer<HttpCodeResult>() {
            @Override
            public void onCompleted() {
                CustomProgress.dismis();
            }

            @Override
            public void onError(Throwable e) {
                CustomProgress.dismis();
            }

            @Override
            public void onNext(HttpCodeResult result) {
                if (result.status == 200) {

                    App.showToast(result.message);
                }
            }
        });
    }
}


