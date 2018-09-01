package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.fragment.LmFragment;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data15;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LmData;
import com.yyz.cyuanw.bean.LmMyListData;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.ToastUtil;
import com.yyz.cyuanw.view.PullRV;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;

public class WdLmActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView titleView;

    private RecyclerView list;
    private ListAdapter adapter;
    private PullRV pullRV;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_wd_lm;
    }

    @Override
    public void initView() {
        setTitle(titleView, "我的联盟");

        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter();
        list.setAdapter(adapter);


        pullRV = (PullRV) findViewById(R.id.xrefreshview);
        pullRV.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRV.startRefresh();
            }
        }, 500);
        pullRV.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                loadLmMyList(true, pullRV.page = 1);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                loadLmMyList(false, ++pullRV.page);
            }
        });

    }

    @Override
    public void initData() {

    }


    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<LmMyListData> dataList = new ArrayList();
        public ListAdapter.VAHolder vaHolder;


        public ListAdapter() {
            this.dataList.add(null);
        }



        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public void setData(List<LmMyListData> dataList) {
            if (null == dataList) return;
            this.dataList.clear();
            this.dataList.add(null);
            this.dataList.addAll(dataList);


            if (this.dataList.size() == 1) {
                vaHolder.black.setVisibility(View.VISIBLE);
            } else {
                vaHolder.black.setVisibility(View.GONE);
            }

            notifyDataSetChanged();
        }

        public void appendData(List<LmMyListData> dataList) {
            if (null == dataList) return;
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            switch (i) {
                case 0:
                    View viewA = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chunk_lm_top, viewGroup, false);
                    vaHolder = new ListAdapter.VAHolder(viewA);
                    return vaHolder;
                default:
                    View viewB = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_lm, viewGroup, false);
                    ListAdapter.VBHolder vBHolder = new ListAdapter.VBHolder(viewB);
                    return vBHolder;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof ListAdapter.VAHolder) {

            } else if (viewHolder instanceof ListAdapter.VBHolder) {
                ((ListAdapter.VBHolder) viewHolder).setData();
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        private class VAHolder extends RecyclerView.ViewHolder {
            public RecyclerView my_lm_list;
            private View black;
            public ListAdapter.VAHolder.MyLmListAdapter myLmListAdapter;

            public void setMyLmData(List<LmMyListData> data) {
                myLmListAdapter.setData(data);
            }

            public VAHolder(@NonNull View itemView) {
                super(itemView);
                black = itemView.findViewById(R.id.black);
                my_lm_list = itemView.findViewById(R.id.my_lm_list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WdLmActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                my_lm_list.setLayoutManager(linearLayoutManager);
                myLmListAdapter = new ListAdapter.VAHolder.MyLmListAdapter();


                my_lm_list.setAdapter(myLmListAdapter);

            }


            private class MyLmListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
                private List<LmMyListData> myLmdataList = new ArrayList();

                public void setData(List<LmMyListData> data) {
                    if (null == data) {
                        return;
                    }
                    myLmdataList.clear();
                    myLmdataList.add(null);
                    myLmdataList.addAll(data);
//                    myLmdataList.add(null);
                    ListAdapter.VAHolder.MyLmListAdapter.this.notifyDataSetChanged();
                }

                @Override
                public int getItemViewType(int position) {
                    return position;
                }

                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View viewA = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_my_lm, viewGroup, false);
                    ListAdapter.VAHolder.MyLmListAdapter.VMyLmHolder vAHolder = new ListAdapter.VAHolder.MyLmListAdapter.VMyLmHolder(viewA);
                    return vAHolder;
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    ListAdapter.VAHolder.MyLmListAdapter.VMyLmHolder vMyLmHolder = (ListAdapter.VAHolder.MyLmListAdapter.VMyLmHolder) viewHolder;
                    vMyLmHolder.setData();
                }

                @Override
                public int getItemCount() {
                    return myLmdataList.size();
                }

                private class VMyLmHolder extends RecyclerView.ViewHolder {
                    private ImageView img;
                    private TextView name;

                    public VMyLmHolder(@NonNull View itemView) {
                        super(itemView);
                        img = itemView.findViewById(R.id.img);
                        name = itemView.findViewById(R.id.name);

                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = getAdapterPosition();
                                if (position == 0) {

//                                    startActivity(new Intent(getActivity(), AuthTestActivity.class));

                                    Intent intent = new Intent(WdLmActivity.this, LmDetailDetailEditActivity.class);
                                    intent.putExtra(LmDetailDetailEditActivity.TYPE, LmDetailDetailEditActivity.TYPE_CREATE);
                                    startActivity(intent);
                                } else if (position == (getItemCount() - 1)) {
//                                    ToastUtil.show(LmFragment.this.getActivity(), "查看全部");
                                }
                            }
                        });
                    }

                    public void setData() {
                        int position = getAdapterPosition();
                        if (position == 0) {
                            name.setText("创建联盟");
                            img.setImageResource(R.mipmap.img_33);
                        } else if (position == (getItemCount() - 1)) {
//                            name.setText("查看全部");
//                            img.setImageResource(R.mipmap.img_36);
                        } else {
                            LmMyListData lmMyListData = myLmdataList.get(position);
                            Img.loadC(img, lmMyListData.alliance.logo);
                            name.setText(lmMyListData.alliance.name);
                        }
                    }
                }
            }
        }

        private class VBHolder extends RecyclerView.ViewHolder {
            public ImageView gx_iv, xb, right_arrow;
            public TextView tv_title, tv_name, tv_num, add, desc;

            public VBHolder(@NonNull View itemView) {
                super(itemView);
                gx_iv = itemView.findViewById(R.id.gx_iv);
                xb = itemView.findViewById(R.id.xb);
                right_arrow = itemView.findViewById(R.id.right_arrow);
                tv_title = itemView.findViewById(R.id.tv_title);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_num = itemView.findViewById(R.id.tv_num);
                add = itemView.findViewById(R.id.add);
                desc = itemView.findViewById(R.id.desc);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        LmMyListData lmData = dataList.get(position);
                        Intent intent = new Intent(WdLmActivity.this, LmDetailActivity.class);
                        intent.putExtra("id", lmData.id);
                        intent.putExtra("img", lmData.alliance.logo);
                        intent.putExtra("name", lmData.alliance.name);
                        intent.putExtra("intro", lmData.alliance.intro);
                        startActivity(intent);
                    }
                });
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        LmMyListData lmData = dataList.get(position);
                        if (lmData.alliance.audit_status == 1) {//1未加入
                            apply_join(lmData.id);
                        }
                    }
                });
            }

            public void setData() {
                int position = getAdapterPosition();
                LmMyListData lmData = dataList.get(position);
                Img.loadC(gx_iv, lmData.alliance.logo);
                tv_title.setText(lmData.alliance.name);
                tv_name.setText(lmData.alliance.leader.real_name);
                tv_num.setText(lmData.users_count + "人关注");
                desc.setText(lmData.alliance.intro);
                add.setText(lmData.alliance.audit_status_text);
                if (lmData.alliance.audit_status == 1) {//1未加入
                    add.setTextColor(Color.parseColor("#EA6F5A"));
                    add.setBackgroundResource(R.drawable.bt_bg_1);
                    right_arrow.setVisibility(View.GONE);
                } else if (lmData.alliance.audit_status == 2) {//2申请中
                    add.setTextColor(Color.parseColor("#ffffff"));
                    add.setBackgroundResource(R.drawable.bt_bg_2);
                    right_arrow.setVisibility(View.GONE);
                } else if (lmData.alliance.audit_status == 3) {// 3已加入
                    add.setTextColor(Color.parseColor("#EA6F5A"));
                    add.setBackground(null);
                    right_arrow.setVisibility(View.VISIBLE);
                }
            }
        }

    }
    //我的联盟分页
    public void loadLmMyList(boolean isRefesh, int page) {
        HttpData.getInstance().getMyLmList(page, new Observer<HttpResult<Data15>>() {
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
            public void onNext(HttpResult<Data15> result) {
                if (result.status == 200) {
                    if (isRefesh) {
                        adapter.setData(result.data.data);
                        pullRV.stopRefresh();

                    } else {
                        adapter.appendData(result.data.data);
                        pullRV.checkhasMore(result.data.data.size());
                    }
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }
    private void apply_join(int id) {
        HttpData.getInstance().apply_join(id, new Observer<HttpListResult>() {
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
            public void onNext(HttpListResult result) {
                if (result.status == 200) {

                } else {
//                    App.showToast(result.message);
                }
                ToastUtil.show(WdLmActivity.this, result.message);
            }
        });
    }
}
