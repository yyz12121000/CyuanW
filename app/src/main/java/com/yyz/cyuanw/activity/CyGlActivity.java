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
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.user_model.SendCarActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data2;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.Tools;
import com.yyz.cyuanw.view.CommonPopupDialog;
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
                startActivity(new Intent(this, SendCarActivity.class));
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

                    }
                });
                gj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                gd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPopupDialog();
                    }
                });
                szws.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                sc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent(CyGlActivity.this, CyDetailActivity.class);
                        intent.putExtra("id", data.get(position).id);
                        startActivity(intent);
                    }
                });
            }

            public void setData() {
                int position = getAdapterPosition();
                Data2 data2 = data.get(position);

                Img.load(gx_iv, data2.cover);
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
    private Dialog mDialog1,mDialog2;
    public void showPopupDialog() {
        if (popupDialogView == null) {
            popupDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_1, null);
            popupDialogView.findViewById(R.id.id_root_view).setOnClickListener(view -> mPopupDialog.dismiss());
            popupDialogView.findViewById(R.id.pf).setOnClickListener(view -> {
                showDialog1();
                mPopupDialog.dismiss();
            });
            popupDialogView.findViewById(R.id.zb).setOnClickListener(view -> {
                showDialog2();
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
    public void showDialog1() {
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
        }
    public void showDialog2() {
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
}


