package com.yyz.cyuanw.activity.fragment;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.xfhy.easybanner.ui.EasyBanner;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class SyFragment extends Fragment {
    private RecyclerView list;
    private List dataList = new ArrayList();
    private ListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sy, container, false);

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
        adapter = new ListAdapter();
        list.setAdapter(adapter);
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private VAHolder vAHolder;

        private void stopBanner() {
            if (null != vAHolder && null != vAHolder.mBanner)
                vAHolder.mBanner.stop();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            switch (i) {
                case 0:
                    View viewA = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chunk_sy_a, viewGroup, false);
                    vAHolder = new VAHolder(viewA);
                    return vAHolder;
                default:
                    View viewB = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sy, viewGroup, false);
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
            private EasyBanner mBanner;
            private ImageView chunk_a_img_a;

            public VAHolder(@NonNull View itemView) {
                super(itemView);
                //可以在布局里面写
                mBanner = (EasyBanner) itemView.findViewById(R.id.sy_banner);
                chunk_a_img_a = itemView.findViewById(R.id.chunk_a_img_a);

            }

            public void setData() {

//                Glide.with(getActivity()).load("http://img1.mm131.me/pic/4232/22.jpg").into(chunk_a_img_a);
//                Img.load(chunk_a_img_a, "http://img1.mm131.me/pic/4232/22.jpg");

                List<String> imgs = new ArrayList<>();
                imgs.add("http://img1.mm131.me/pic/4220/20.jpg");
                imgs.add("http://img1.mm131.me/pic/4222/1.jpg");
                imgs.add("http://img1.mm131.me/pic/4139/21.jpg");
                imgs.add("http://img1.mm131.me/pic/4232/22.jpg");
                imgs.add("http://img1.mm131.me/pic/4147/18.jpg");

                List<String> titles = new ArrayList<>();
                titles.add("");
                titles.add("");
                titles.add("");
                titles.add("");
                titles.add("");

                //初始化:设置图片url和图片标题
                mBanner.initBanner(imgs, titles);


                //设置图片加载器
                mBanner.setImageLoader(new EasyBanner.ImageLoader() {
                    @Override
                    public void loadImage(ImageView imageView, String url) {
//                        Img.load(imageView, url);

                    }
                });
                //监听banner的item点击事件
                mBanner.setOnItemClickListener(new EasyBanner.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, String title) {
                        ToastUtil.show(getContext(), "position:" + position + "   title:" + title);
                    }
                });

                // 开始轮播
                mBanner.start();
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
