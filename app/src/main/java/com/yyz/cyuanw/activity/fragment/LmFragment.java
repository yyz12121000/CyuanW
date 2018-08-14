package com.yyz.cyuanw.activity.fragment;

import android.content.Intent;
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
import com.yyz.cyuanw.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class LmFragment extends Fragment {

    private RecyclerView list;
    private List dataList = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lm, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");

        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(new LmFragment.ListAdapter());

    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemViewType(int position) {
            return position;
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
            public VBHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LmFragment.this.getActivity(), LmDetailActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }

    }

}
