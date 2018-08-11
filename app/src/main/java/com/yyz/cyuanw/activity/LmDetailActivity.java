package com.yyz.cyuanw.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xfhy.easybanner.ui.EasyBanner;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LmDetailActivity extends BaseActivity {
    @BindView(R.id.title_right_icon) ImageView title_right_icon;
    @BindView(R.id.list) RecyclerView list;

    private List dataList = new ArrayList();
    private ListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lm_detail;
    }

    @Override
    public void initView() {
        title_right_icon.setVisibility(View.VISIBLE);
        title_right_icon.setImageResource(R.mipmap.ic_launcher);

        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");

        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter();
        list.setAdapter(adapter);
    }

    @Override
    public void initData() {
    }


    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private VAHolder vAHolder;


        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            switch (i) {
                case 0:
                    View viewA = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_lm_detail_top, viewGroup, false);
                    vAHolder = new VAHolder(viewA);
                    return vAHolder;
                default:
                    View viewB = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_lm_detail, viewGroup, false);
                    VBHolder vBHolder = new VBHolder(viewB);
                    return vBHolder;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof VAHolder) {
                ((VAHolder) viewHolder).setData();
            } else {
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        private class VAHolder extends RecyclerView.ViewHolder {
            private ImageView chunk_a_img_a;

            public VAHolder(@NonNull View itemView) {
                super(itemView);
                //可以在布局里面写
//                chunk_a_img_a = itemView.findViewById(R.id.chunk_a_img_a);

            }

            public void setData() {

//                Glide.with(getActivity()).load("http://img1.mm131.me/pic/4232/22.jpg").into(chunk_a_img_a);
//                Img.load(chunk_a_img_a, "http://img1.mm131.me/pic/4232/22.jpg");



            }

        }

        private class VBHolder extends RecyclerView.ViewHolder {
            public VBHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

    }


    @Override
    public void onStop() {
        super.onStop();

        if (null != adapter) {
//            adapter.stopBanner();
        }
    }
}
