package com.yyz.cyuanw.activity.user_model;

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
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.CyDetailActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.CarData;
import com.yyz.cyuanw.bean.CarInfoData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data2;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.bean.PreparationData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.Tools;
import com.yyz.cyuanw.view.CommonPopupDialog;
import com.yyz.cyuanw.view.CustomProgress;
import com.yyz.cyuanw.view.PullRV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class CarSearchActivity extends BaseActivity {

    @BindView(R.id.black)
    View black;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.xrefreshview)
    PullRV pullRV;
    @BindView(R.id.search_text)
    EditText search_text;

    private ListAdapter adapter;
    private String key_word = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_carsearch;
    }

    @Override
    public void initView() {

        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter();
        list.setAdapter(adapter);

        pullRV.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                searchCar(true, pullRV.page = 1);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                searchCar(false, ++pullRV.page);
            }
        });
        search_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                    String text = search_text.getText().toString();
                    Tools.hideSoftInput(CarSearchActivity.this, search_text);

                    key_word = text;
                    searchCar(true, pullRV.page = 1);
                }
                return false;
            }

        });
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.id_btn_submit})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.id_btn_submit:

                startActivity(new Intent(this,CarConfirmActivity.class));
                break;
        }
    }

    public void searchCar(boolean isRefresh, int page) {
        HttpData.getInstance().dealer(key_word, page,App.get(Constant.KEY_USER_TOKEN), new Observer<HttpResult<CarData>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                pullRV.stopRefresh();
                LogManager.e("解析出错" + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<CarData> result) {
                if (result.status == 200) {
                    if (isRefresh) {
                        adapter.setData(result.data.info);
                        pullRV.stopRefresh();
                    } else {
                        adapter.appendData(result.data.info);
                        pullRV.checkhasMore(result.data.info.size());
                    }

                }
            }
        });
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<CarInfoData> data = new ArrayList<>();

        public void setData(List<CarInfoData> data) {
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

        public void appendData(List<CarInfoData> data) {
            if (null != data) {
                this.data.addAll(data);
                notifyDataSetChanged();
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_car, viewGroup, false);
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
            private ImageView imageView;
            private TextView nameView, cityView, selectView;

            public VHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.id_item_image);
                nameView = itemView.findViewById(R.id.id_item_name);
                cityView = itemView.findViewById(R.id.id_item_city);
                selectView = itemView.findViewById(R.id.id_item_select);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Intent intent = new Intent();
                        intent.putExtra("id", data.get(position).id);
                        intent.putExtra("logo", data.get(position).logo);
                        intent.putExtra("name", data.get(position).name);
                        intent.putExtra("city_name", data.get(position).city_name);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });

            }

            public void setData() {
                int position = getAdapterPosition();
                CarInfoData carInfo = data.get(position);

                Img.loadC(imageView, carInfo.logo);
                nameView.setText(carInfo.name);
                cityView.setText(carInfo.city_name);
            }
        }
    }

}


