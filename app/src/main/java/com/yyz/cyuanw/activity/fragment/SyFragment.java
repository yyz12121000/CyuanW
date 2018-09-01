package com.yyz.cyuanw.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.yyz.cyuanw.activity.WdLmActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.AdData;
import com.yyz.cyuanw.bean.Ads;
import com.yyz.cyuanw.bean.HotLmData;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.JjrData;
import com.yyz.cyuanw.bean.JjrResultData;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

public class SyFragment extends Fragment {
    private RecyclerView list;

    private ListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sy, container, false);

        init(view);

        return view;
    }


    private void init(View view) {
        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ListAdapter();
        list.setAdapter(adapter);

        loadHotLm();
        loadAd();
        //loadJjr("234","898");
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private VAHolder vAHolder;
        private List<JjrData> dataList = new ArrayList();

        public ListAdapter() {
            dataList.add(null);
        }

        public void setData(List<JjrData> data) {
            if (null == data) {
                return;
            }
            dataList.clear();
            dataList.add(null);
            dataList.addAll(data);
            notifyDataSetChanged();
        }

        private void stopBanner() {
            if (null != vAHolder && null != vAHolder.banner)
                vAHolder.banner.stopAutoPlay();
        }

        public void setHotLmdataList(List<HotLmData> hotLmdataList) {
            vAHolder.setHotLmdataList(hotLmdataList);
        }

        public void startBanner(List<Ads> ads) {
            if (null == ads) return;
            List<String> imgs = new ArrayList<>();
            for (int i = 0; i < ads.size(); i++) {
                imgs.add(ads.get(i).image);
            }
            vAHolder.startBanner(imgs);
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
                ((VBHolder) viewHolder).setData();
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        private class VAHolder extends RecyclerView.ViewHolder {
            public Banner banner;
            private ImageView chunk_a_img_a,chunk_a_img_b;
            private RecyclerView hot_lm_list;
            public HotLmListAdapter hotLmListAdapter;


            private LinearLayout banner_root;

            public void setHotLmdataList(List<HotLmData> hotLmdataList) {
                hotLmListAdapter.setHotLmdataList(hotLmdataList);
            }

            public VAHolder(@NonNull View itemView) {
                super(itemView);
                //可以在布局里面写
                banner = (Banner) itemView.findViewById(R.id.banner);
                chunk_a_img_a = itemView.findViewById(R.id.chunk_a_img_a);
                chunk_a_img_b = itemView.findViewById(R.id.chunk_a_img_b);
                hot_lm_list = itemView.findViewById(R.id.hot_lm_list);

                banner_root = itemView.findViewById(R.id.banner_root);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SyFragment.this.getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                hot_lm_list.setLayoutManager(linearLayoutManager);
                hotLmListAdapter = new HotLmListAdapter();

                chunk_a_img_b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SyFragment.this.getActivity(), WdLmActivity.class);
                        SyFragment.this.getActivity().startActivity(intent);
                    }
                });

                hot_lm_list.setAdapter(hotLmListAdapter);
            }

            public void setData() {


           /*     List<String> titles = new ArrayList<>();
                titles.add("");
                titles.add("");
                titles.add("");
                titles.add("");
                titles.add("");*/

                //设置banner样式
//                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                //设置图片加载器
                banner.setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        Img.load(imageView, path.toString());
                    }
                });
            }

            public void startBanner(List<String> imgs) {
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
                private List<HotLmData> hotLmdataList = new ArrayList();

                public void setHotLmdataList(List<HotLmData> hotLmdataList) {
                    if (null == hotLmdataList) return;
                    this.hotLmdataList = hotLmdataList;
                    HotLmListAdapter.this.notifyDataSetChanged();
                }

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
                        HotLmData hotLmData = hotLmdataList.get(position);
                        name.setText(hotLmData.name);
                        Img.load(img, hotLmData.logo);
                        gz_num.setText(hotLmData.users_count + "人关注");

                    }
                }
            }
        }

        private class VBHolder extends RecyclerView.ViewHolder {
            private ImageView tx, iv_j, iv_p;
            private TextView name, tv_sp, bh, tc;

            public VBHolder(@NonNull View itemView) {
                super(itemView);
                tx = itemView.findViewById(R.id.tx);
                iv_j = itemView.findViewById(R.id.iv_j);
                iv_p = itemView.findViewById(R.id.iv_p);
                name = itemView.findViewById(R.id.name);
                tv_sp = itemView.findViewById(R.id.tv_sp);
                bh = itemView.findViewById(R.id.bh);
                tc = itemView.findViewById(R.id.tc);
            }

            public void setData() {
                int position = getAdapterPosition();
                JjrData jjrData = dataList.get(position);
                Img.loadC(tx, jjrData.profile_picture);
                name.setText(jjrData.real_name);
                if (!TextUtils.isEmpty(jjrData.broker_number)){
                    bh.setText("编号："+jjrData.broker_number);
                }
                tv_sp.setText(jjrData.bind_dealer);
                tc.setText(jjrData.signature);

                if (jjrData.is_broker == 1){
                    iv_j.setVisibility(View.VISIBLE);
                }else {
                    iv_j.setVisibility(View.GONE);
                }
                if (jjrData.is_adviser == 1){
                    iv_p.setVisibility(View.VISIBLE);
                }else {
                    iv_p.setVisibility(View.GONE);
                }

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

    public void loadHotLm() {
        HttpData.getInstance().getHotLmData(new Observer<HttpListResult<HotLmData>>() {
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
            public void onNext(HttpListResult<HotLmData> result) {
                if (result.status == 200) {
                    adapter.setHotLmdataList(result.data);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }

    public void loadAd() {
        HttpData.getInstance().getAdData(new Observer<HttpResult<AdData>>() {
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
            public void onNext(HttpResult<AdData> result) {
                if (result.status == 200) {
                    adapter.startBanner(result.data.ads);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }

    public void loadJjr(String longitude, String latitude) {
        HttpData.getInstance().getJjrData(longitude, latitude, new Observer<HttpResult<JjrResultData>>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                LogManager.e("解析出错" + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<JjrResultData> result) {
                if (result.status == 200) {
                    adapter.setData(result.data.info);
//                    adapter.startBanner(result.data.ads);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }
}
