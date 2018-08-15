package com.yyz.cyuanw.activity.fragment;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;
import com.yyz.cyuanw.R;

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
            if (null != vAHolder && null != vAHolder.banner)
                vAHolder.banner.stopAutoPlay();
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
            public Banner banner;
            private ImageView chunk_a_img_a;
            private RecyclerView hot_lm_list;
            public HotLmListAdapter hotLmListAdapter;
            private List hotLmdataList = new ArrayList();

            private LinearLayout banner_root;

            public VAHolder(@NonNull View itemView) {
                super(itemView);
                //可以在布局里面写
                banner = (Banner) itemView.findViewById(R.id.banner);
                chunk_a_img_a = itemView.findViewById(R.id.chunk_a_img_a);
                hot_lm_list = itemView.findViewById(R.id.hot_lm_list);

                banner_root = itemView.findViewById(R.id.banner_root);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SyFragment.this.getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                hot_lm_list.setLayoutManager(linearLayoutManager);
                hotLmListAdapter = new HotLmListAdapter();
                hotLmdataList.add("");
                hotLmdataList.add("");
                hotLmdataList.add("");
                hotLmdataList.add("");
                hotLmdataList.add("");
                hotLmdataList.add("");
                hotLmdataList.add("");
                hotLmdataList.add("");
                hotLmdataList.add("");
                hotLmdataList.add("");
                hot_lm_list.setAdapter(hotLmListAdapter);
            }

            public void setData() {


                //Img.load(chunk_a_img_a, "http://c.hiphotos.baidu.com/image/pic/item/f9198618367adab4b025268587d4b31c8601e47b.jpg");

                List<String> imgs = new ArrayList<>();
                imgs.add("http://c.hiphotos.baidu.com/image/pic/item/f9198618367adab4b025268587d4b31c8601e47b.jpg");
                imgs.add("http://c.hiphotos.baidu.com/image/pic/item/f9198618367adab4b025268587d4b31c8601e47b.jpg");
                imgs.add("http://c.hiphotos.baidu.com/image/pic/item/f9198618367adab4b025268587d4b31c8601e47b.jpg");
                imgs.add("http://c.hiphotos.baidu.com/image/pic/item/f9198618367adab4b025268587d4b31c8601e47b.jpg");
                imgs.add("http://c.hiphotos.baidu.com/image/pic/item/f9198618367adab4b025268587d4b31c8601e47b.jpg");

                List<String> titles = new ArrayList<>();
                titles.add("");
                titles.add("");
                titles.add("");
                titles.add("");
                titles.add("");


                //设置banner样式
//                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                //设置图片加载器
                banner.setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        //Img.load(imageView, path.toString());
                    }
                });
                //设置图片集合
                banner.setImages(imgs);
                //设置banner动画效果
                banner.setBannerAnimation(Transformer.Default);
                //设置标题集合（当banner样式有显示title时）
//                banner.setBannerTitles(titles);
                //设置自动轮播，默认为true
                banner.isAutoPlay(true);
                //设置轮播时间
                banner.setDelayTime(6000);
                //设置指示器位置（当banner模式中有指示器时）
                banner.setIndicatorGravity(BannerConfig.CENTER);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
            }

            private class HotLmListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

                @Override
                public int getItemViewType(int position) {
                    return position;
                }

                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                    View viewA = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_hot_lm, viewGroup, false);
                    VHotLmHolder vAHolder = new VHotLmHolder(viewA);
                    return vAHolder;
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                    VHotLmHolder vMyLmHolder = (VHotLmHolder) viewHolder;
                    vMyLmHolder.setData();
                }

                @Override
                public int getItemCount() {
                    return hotLmdataList.size();
                }

                private class VHotLmHolder extends RecyclerView.ViewHolder {
                    private ImageView img;
                    private TextView name, gz_num;

                    public VHotLmHolder(@NonNull View itemView) {
                        super(itemView);
                        img = itemView.findViewById(R.id.img);
                        name = itemView.findViewById(R.id.name);
                        gz_num = itemView.findViewById(R.id.gz_num);

                        itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int position = getAdapterPosition();

                            }
                        });
                    }

                    public void setData() {
                        int position = getAdapterPosition();

                    }
                }
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
            adapter.stopBanner();
        }
    }
}
