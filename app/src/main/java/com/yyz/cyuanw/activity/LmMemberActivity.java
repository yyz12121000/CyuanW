package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.CyData;
import com.yyz.cyuanw.bean.CyListData;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LmDetail;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;

public class LmMemberActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView textView;
    @BindView(R.id.title_right_icon)
    ImageView right;
    private RecyclerView list;

    private int lm_id;
    ListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lm_member;
    }

    @Override
    public void initView() {
        textView.setText("联盟成员");
        right.setImageResource(R.mipmap.icon_2);
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LmMemberActivity.this, LmMemberJoinActivity.class);
                startActivity(intent);
            }
        });
        adapter = new LmMemberActivity.ListAdapter();
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    @Override
    public void initData() {
        lm_id = getIntent().getIntExtra("lm_id", 0);
        loadCyList(lm_id);
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<CyData> dataList = new ArrayList();

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public void setDataList(List<CyData> dataList) {
            if (null == dataList) return;
            this.dataList = dataList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View viewB = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_lm_member, viewGroup, false);
            LmMemberActivity.ListAdapter.VBHolder vBHolder = new LmMemberActivity.ListAdapter.VBHolder(viewB);
            return vBHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ((VBHolder) viewHolder).setData();
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        private class VBHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private Button left;
            private ImageView gx_iv, is_leader;
            private TextView name, desc;

            public VBHolder(@NonNull View itemView) {
                super(itemView);
                gx_iv = itemView.findViewById(R.id.gx_iv);
                is_leader = itemView.findViewById(R.id.is_leader);
                name = itemView.findViewById(R.id.name);
                desc = itemView.findViewById(R.id.desc);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(LmMemberActivity.this, LmDetailActivity.class);
//                        startActivity(intent);
                    }
                });
                left = itemView.findViewById(R.id.left);
                left.setOnClickListener(this);
                itemView.findViewById(R.id.delete).setOnClickListener(this);
            }

            public void setData() {
                int position = getAdapterPosition();
                CyData cyData = dataList.get(position);
                Img.loadC(gx_iv, cyData.user.profile_picture);
                name.setText(cyData.user.real_name);
                desc.setText(cyData.user.signature);

                if (cyData.is_administrator == 1) {
                    is_leader.setVisibility(View.VISIBLE);
                    left.setText("取消管理員");
                    left.setBackgroundColor(Color.parseColor("#F5A623"));
                } else {
                    is_leader.setVisibility(View.GONE);
                    left.setText("设为管理員");
                    left.setBackgroundColor(Color.parseColor("#76D0A3"));
                }
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                CyData cyData = dataList.get(position);
                switch (view.getId()) {
                    case R.id.left:
                        if (cyData.is_administrator == 1) {
                            cancel_administrator(cyData);
                        } else {
                            set_administrator(cyData);
                        }
                        break;
                    case R.id.delete:
                        ToastUtil.show(LmMemberActivity.this, "删除");
                        break;
                }
            }
        }

    }

    private void set_administrator(CyData cyData) {
        HttpData.getInstance().set_administrator(lm_id, cyData.user.id, new Observer<HttpListResult<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogManager.e(e.getMessage());
            }

            @Override
            public void onNext(HttpListResult<String> result) {
                if (result.status == 200) {
                    cyData.is_administrator = 1;
                    adapter.notifyDataSetChanged();
                }
                ToastUtil.show(LmMemberActivity.this, result.message);
            }
        });
    }

    private void cancel_administrator(CyData cyData) {
        HttpData.getInstance().cancel_administrator(lm_id, cyData.user.id, new Observer<HttpListResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogManager.e(e.getMessage());
            }

            @Override
            public void onNext(HttpListResult result) {
                if (result.status == 200) {
                    cyData.is_administrator = 0;
                    adapter.notifyDataSetChanged();
                }
                ToastUtil.show(LmMemberActivity.this, result.message);
            }
        });
    }

    private void loadCyList(int id) {
        HttpData.getInstance().lmcylist(id, new Observer<HttpResult<CyListData>>() {
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
            public void onNext(HttpResult<CyListData> result) {
                if (result.status == 200) {
                    adapter.setDataList(result.data.data);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }
}
