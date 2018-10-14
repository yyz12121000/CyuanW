package com.yyz.cyuanw.activity.user_model;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.GwData;
import com.yyz.cyuanw.bean.GwListData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.view.CustomProgress;
import com.yyz.cyuanw.view.PullRV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;

public class DshActivity extends BaseActivity {
    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.black)
    View black;
    @BindView(R.id.list)
    RecyclerView list;
    @BindView(R.id.xrefreshview)
    PullRV pullRV;

    private ListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dsh;
    }

    @Override
    public void initView() {
        setTitle(titleView,"待审核");

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
                getApplicationList(
                        true, pullRV.page = 1);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                getApplicationList(false, ++pullRV.page);
            }
        });

    }

    @Override
    public void initData() {

    }

    public void getApplicationList(boolean isRefresh, int page) {
        HttpData.getInstance().getApplicationList(0,page, App.get(Constant.KEY_USER_TOKEN),new Observer<HttpResult<GwListData>>() {
            @Override
            public void onCompleted() {
                pullRV.stopRefresh();
            }

            @Override
            public void onError(Throwable e) {
                LogManager.e("解析出错" + e.getMessage());
                pullRV.stopRefresh();
                black.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(HttpResult<GwListData> result) {
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

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<GwData> data = new ArrayList<>();

        public void setData(List<GwData> data) {
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

        public void appendData(List<GwData> data) {
            if (null != data) {
                this.data.addAll(data);
                notifyDataSetChanged();
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_dsh, viewGroup, false);
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
            private ImageView iconView;
            private TextView nameView, phoneView;
            private Button jj, tg;

            public VHolder(@NonNull View itemView) {
                super(itemView);
                iconView = itemView.findViewById(R.id.tx);
                nameView = itemView.findViewById(R.id.name);
                phoneView = itemView.findViewById(R.id.tv_sp);
                jj = itemView.findViewById(R.id.jj);
                tg = itemView.findViewById(R.id.tg);

                jj.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        auditBroker(data.get(position).id,2);

                    }
                });
                tg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        auditBroker(data.get(position).id,1);
                    }
                });

            }

            public void setData() {
                int position = getAdapterPosition();
                GwData item = data.get(position);

                Img.loadC(iconView, item.pic);
                nameView.setText(item.name);
                phoneView.setText("手机号："+item.phone);

            }
        }
    }

    public void auditBroker(int id,int status) {
        CustomProgress.show(this, "请稍等...", false, null);
        HttpData.getInstance().auditBroker(id,status, App.get(Constant.KEY_USER_TOKEN),new Observer<HttpCodeResult>() {

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
                App.showToast(result.message);
                if (result.status == 200) {
                    pullRV.startRefresh();
                }
            }
        });
    }


}


