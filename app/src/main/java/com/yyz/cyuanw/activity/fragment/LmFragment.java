package com.yyz.cyuanw.activity.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.LmDetailActivity;
import com.yyz.cyuanw.activity.LmDetailDetailEditActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.AdData;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LmData;
import com.yyz.cyuanw.bean.LmListData;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

public class LmFragment extends Fragment {

    private RecyclerView list;

    private ListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lm, container, false);

        init(view);

        return view;
    }

    private void init(View view) {


        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LmFragment.ListAdapter();
        list.setAdapter(adapter);

        loadLmList();
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<LmData> dataList = new ArrayList();

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public void setData(List<LmData> dataList) {
            if (null == dataList) return;
            this.dataList.clear();
            this.dataList.add(null);
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            switch (i) {
                case 0:
                    View viewA = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chunk_lm_top, viewGroup, false);
                    LmFragment.ListAdapter.VAHolder vAHolder = new LmFragment.ListAdapter.VAHolder(viewA);
                    return vAHolder;
                default:
                    View viewB = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_lm, viewGroup, false);
                    LmFragment.ListAdapter.VBHolder vBHolder = new LmFragment.ListAdapter.VBHolder(viewB);
                    return vBHolder;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof VAHolder) {

            } else if (viewHolder instanceof VBHolder) {
                ((VBHolder) viewHolder).setData();
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        private class VAHolder extends RecyclerView.ViewHolder {
            public RecyclerView my_lm_list;
            public MyLmListAdapter myLmListAdapter;
            private List myLmdataList = new ArrayList();

            public VAHolder(@NonNull View itemView) {
                super(itemView);
                my_lm_list = itemView.findViewById(R.id.my_lm_list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LmFragment.this.getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                my_lm_list.setLayoutManager(linearLayoutManager);
                myLmListAdapter = new MyLmListAdapter();
                myLmdataList.add("");
                myLmdataList.add("");
                myLmdataList.add("");
                myLmdataList.add("");
                myLmdataList.add("");
                myLmdataList.add("");
                myLmdataList.add("");

                my_lm_list.setAdapter(myLmListAdapter);

            }

            private class MyLmListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

                @Override
                public int getItemViewType(int position) {
                    return position;
                }

                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View viewA = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_my_lm, viewGroup, false);
                    VMyLmHolder vAHolder = new VMyLmHolder(viewA);
                    return vAHolder;
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    VMyLmHolder vMyLmHolder = (VMyLmHolder) viewHolder;
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
                                    Intent intent = new Intent(LmFragment.this.getActivity(), LmDetailDetailEditActivity.class);
                                    intent.putExtra(LmDetailDetailEditActivity.TYPE, LmDetailDetailEditActivity.TYPE_CREATE);
                                    startActivity(intent);
                                } else if (position == (getItemCount() - 1)) {
                                    ToastUtil.show(LmFragment.this.getActivity(), "查看全部");
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
                            name.setText("查看全部");
                            img.setImageResource(R.mipmap.img_36);
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
                        Intent intent = new Intent(LmFragment.this.getActivity(), LmDetailActivity.class);
                        startActivity(intent);
                    }
                });
            }

            public void setData() {
                int position = getAdapterPosition();
                LmData lmData = dataList.get(position);
                Img.loadC(gx_iv, lmData.logo);
                tv_title.setText(lmData.name);
                tv_name.setText(lmData.leader.real_name);
                tv_num.setText(lmData.users_count + "人关注");
                desc.setText(lmData.intro);
                add.setText(lmData.join_status_text);
                if (lmData.join_status == 1) {//1未加入
                    add.setTextColor(Color.parseColor("#EA6F5A"));
                    add.setBackgroundResource(R.drawable.bt_bg_1);
                    right_arrow.setVisibility(View.GONE);
                } else if (lmData.join_status == 2) {//2申请中
                    add.setTextColor(Color.parseColor("#ffffff"));
                    add.setBackgroundResource(R.drawable.bt_bg_2);
                    right_arrow.setVisibility(View.GONE);
                } else if (lmData.join_status == 3) {// 3已加入
                    add.setTextColor(Color.parseColor("#EA6F5A"));
                    add.setBackground(null);
                    right_arrow.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    public void loadLmList() {
        HttpData.getInstance().getLmList("", 1, new Observer<HttpResult<LmListData>>() {
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
            public void onNext(HttpResult<LmListData> result) {
                if (result.status == 200) {
                    adapter.setData(result.data.data);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }
}
