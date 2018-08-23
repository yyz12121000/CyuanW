package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.CheyData;
import com.yyz.cyuanw.bean.CheyListData;
import com.yyz.cyuanw.bean.CyData;
import com.yyz.cyuanw.bean.CyListData;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LmListData;
import com.yyz.cyuanw.bean.LmMyListData;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;

public class LmDetailActivity extends BaseActivity {
    @BindView(R.id.title_right_icon)
    ImageView title_right_icon;
    @BindView(R.id.id_tv_title)
    TextView id_tv_title;
    @BindView(R.id.list)
    RecyclerView list;

    private ListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lm_detail;
    }

    @Override
    public void initView() {
        title_right_icon.setVisibility(View.VISIBLE);
        title_right_icon.setImageResource(R.mipmap.ing_35);
        setTitle(id_tv_title, "联盟主页");


        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter();
        list.setAdapter(adapter);
    }

    private int lm_id;

    @Override
    public void initData() {
        Intent intent = getIntent();
        lm_id = intent.getIntExtra("id", -1);
        String imgUrl = intent.getStringExtra("img");
        String name = intent.getStringExtra("name");
        String intro = intent.getStringExtra("intro");

        list.post(new Runnable() {
            @Override
            public void run() {
                adapter.vAHolder.setData1(imgUrl, name, intro);

                loadCyList(lm_id);
                loadCheyList(lm_id);
            }
        });


    }


    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public VAHolder vAHolder;
        private List<CheyData> dataList = new ArrayList();

        public ListAdapter() {
            dataList.add(null);
        }

        public void setData(List<CheyData> data) {
            if (null == data) {
                return;
            }
            dataList.clear();
            dataList.add(null);
            dataList.addAll(data);
            notifyDataSetChanged();
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
//                ((VAHolder) viewHolder).setData();
            } else {
                ((VBHolder) viewHolder).setData();
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        private class VAHolder extends RecyclerView.ViewHolder {
            public ImageView img, iv1, iv2, iv3, iv4, iv5, right_arrow;
            public TextView name, desc, rs, total;


            public VAHolder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                name = itemView.findViewById(R.id.name);
                desc = itemView.findViewById(R.id.desc);

                iv1 = itemView.findViewById(R.id.iv1);
                iv2 = itemView.findViewById(R.id.iv2);
                iv3 = itemView.findViewById(R.id.iv3);
                iv4 = itemView.findViewById(R.id.iv4);
                iv5 = itemView.findViewById(R.id.iv5);

                right_arrow = itemView.findViewById(R.id.right_arrow);
                rs = itemView.findViewById(R.id.rs);
                total = itemView.findViewById(R.id.total);

                itemView.findViewById(R.id.top).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LmDetailActivity.this, LmDetailDetailActivity.class);
                        intent.putExtra("id", lm_id);
                        startActivity(intent);
                    }
                });
                itemView.findViewById(R.id.member).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LmDetailActivity.this, LmMemberActivity.class);
                        intent.putExtra("lm_id",lm_id);
                        startActivity(intent);
                    }
                });

            }

            public void setData1(String imgUrl, String nameS, String intro) {
                Img.loadC(img, imgUrl);
                name.setText(nameS);
                desc.setText(intro);

            }

            public void setData2(List<CyData> data) {
                int size = data.size();
//                int position = getAdapterPosition();
                switch (size) {
                    default:
                        right_arrow.setVisibility(View.VISIBLE);
                        rs.setVisibility(View.VISIBLE);
                        rs.setText("等" + size + "人");
                    case 5:
                        Img.loadC(iv5, data.get(4).user.profile_picture);
                        iv5.setVisibility(View.VISIBLE);
                    case 4:
                        Img.loadC(iv4, data.get(3).user.profile_picture);
                        iv4.setVisibility(View.VISIBLE);
                    case 3:
                        Img.loadC(iv3, data.get(2).user.profile_picture);
                        iv3.setVisibility(View.VISIBLE);
                    case 2:
                        Img.loadC(iv2, data.get(1).user.profile_picture);
                        iv2.setVisibility(View.VISIBLE);
                    case 1:
                        Img.loadC(iv1, data.get(0).user.profile_picture);
                        iv1.setVisibility(View.VISIBLE);
                }


            }

            public void setData3(int totaln) {
                total.setText("联盟车源（" + totaln + "）");
            }

        }

        private class VBHolder extends RecyclerView.ViewHolder {
            public ImageView gx_iv, icon;
            public TextView tv_title, tv_sp, price, name, dz, time;

            public VBHolder(@NonNull View itemView) {
                super(itemView);
                gx_iv = itemView.findViewById(R.id.gx_iv);
                icon = itemView.findViewById(R.id.icon);
                tv_title = itemView.findViewById(R.id.tv_title);
                tv_sp = itemView.findViewById(R.id.tv_sp);
                price = itemView.findViewById(R.id.price);
                name = itemView.findViewById(R.id.name);
                dz = itemView.findViewById(R.id.dz);
                time = itemView.findViewById(R.id.time);
            }

            public void setData() {
                int position = getAdapterPosition();
                CheyData cheyData = dataList.get(position);
                Img.load(gx_iv, cheyData.user_info.dealer_info.logo);
                tv_title.setText(cheyData.user_info.dealer_info.name);
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
                    adapter.vAHolder.setData2(result.data.data);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }

    private void loadCheyList(int id) {
        HttpData.getInstance().lmcheylist(id, new Observer<HttpResult<CheyListData>>() {
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
            public void onNext(HttpResult<CheyListData> result) {
                if (result.status == 200) {
                    adapter.vAHolder.setData3(result.data.total);
                    adapter.setData(result.data.data);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }

}
