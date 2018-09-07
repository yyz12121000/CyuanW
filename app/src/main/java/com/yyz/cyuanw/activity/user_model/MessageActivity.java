package com.yyz.cyuanw.activity.user_model;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.MessageData;
import com.yyz.cyuanw.bean.MessageItem;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.view.PullRV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

public class MessageActivity extends BaseActivity {
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
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        setTitle(titleView,"我的消息");

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
                getMessageList(true, pullRV.page = 1);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                getMessageList(false, ++pullRV.page);
            }
        });

    }

    @Override
    public void initData() {

    }

    public void getMessageList(boolean isRefresh, int page) {
        HttpData.getInstance().getMessageList(page, App.get(Constant.KEY_USER_TOKEN),new Observer<HttpResult<MessageData>>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
                pullRV.stopRefresh();
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                LogManager.e("解析出错" + e.getMessage());
                pullRV.stopRefresh();
                black.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNext(HttpResult<MessageData> result) {
                if (result.status == 200) {
                    if (result.data.data.size() > 0){
                        if (isRefresh) {
                            adapter.setData(result.data.data);
                            pullRV.stopRefresh();
                            black.setVisibility(View.GONE);
                        } else {
                            adapter.appendData(result.data.data);
                            pullRV.checkhasMore(result.data.data.size());
                        }
                    }else{
                        pullRV.stopRefresh();
                        black.setVisibility(View.VISIBLE);
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
        private List<MessageItem> data = new ArrayList<>();

        public void setData(List<MessageItem> data) {
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

        public void appendData(List<MessageItem> data) {
            if (null != data) {
                this.data.addAll(data);
                notifyDataSetChanged();
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_message, viewGroup, false);
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
            private TextView titleView, timeView, contentView;

            public VHolder(@NonNull View itemView) {
                super(itemView);
                iconView = itemView.findViewById(R.id.id_item_icon);
                titleView = itemView.findViewById(R.id.id_item_title);
                timeView = itemView.findViewById(R.id.id_item_time);
                contentView = itemView.findViewById(R.id.id_item_content);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        int position = getAdapterPosition();
//                        Intent intent = new Intent(MessageActivity.this, CyDetailActivity.class);
//                        intent.putExtra("id", data.get(position).id);
//                        startActivity(intent);
                    }
                });
            }

            public void setData() {
                int position = getAdapterPosition();
                MessageItem item = data.get(position);

                Img.loadP(iconView, item.message_type.message_type_icon,R.mipmap.ic_notice);
                titleView.setText(item.message_type.content);
                timeView.setText(item.created_at);
                contentView.setText(item.content);

            }
        }
    }


}


