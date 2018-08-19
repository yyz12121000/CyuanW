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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.CyDetailActivity;
import com.yyz.cyuanw.activity.GdsxActivity;
import com.yyz.cyuanw.activity.PpxzActivity;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data2;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.view.JgqjPopuwindow;
import com.yyz.cyuanw.view.ListPopuwindow;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

public class CyFragment extends Fragment implements View.OnClickListener, PopupWindow.OnDismissListener {
    private RecyclerView list;
    private ListPopuwindow cyPopuwindow;
    private ListPopuwindow pxPopuwindow;
    private JgqjPopuwindow jgqjPopuwindow;
    private TextView tv_1, tv_2, tv_3, tv_4;
    private ListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cy, container, false);

        init(view);

        LogManager.d("===onCreateView");
        return view;
    }

    private void init(View view) {
        tv_1 = view.findViewById(R.id.tv_1);
        tv_1.setOnClickListener(this);
        tv_2 = view.findViewById(R.id.tv_2);
        tv_2.setOnClickListener(this);
        tv_3 = view.findViewById(R.id.tv_3);
        tv_3.setOnClickListener(this);
        tv_4 = view.findViewById(R.id.tv_4);
        tv_4.setOnClickListener(this);
        view.findViewById(R.id.gdsx).setOnClickListener(this);

        List<String> cydatas = new ArrayList<>();
        cydatas.add("全部车源");
        cydatas.add("批发车源");
        cydatas.add("急售车源");
        List<String> pxDatas = new ArrayList<>();
        pxDatas.add("默认排序（发布时间降序）");
        pxDatas.add("价格最低");
        pxDatas.add("价格最高");
        pxDatas.add("车龄最短");
        pxDatas.add("里程最少");
        cyPopuwindow = new ListPopuwindow(getActivity());
        pxPopuwindow = new ListPopuwindow(getActivity());
        jgqjPopuwindow = new JgqjPopuwindow(getActivity());
        cyPopuwindow.setDatas(cydatas);
        pxPopuwindow.setDatas(pxDatas);
        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CyFragment.ListAdapter();
        list.setAdapter(adapter);

        cyPopuwindow.setItemListenner(new ListPopuwindow.IOnListItemClickListenner() {
            @Override
            public void onItemClick(int position, String text) {
                tv_1.setText(text);
            }
        });
        pxPopuwindow.setItemListenner(new ListPopuwindow.IOnListItemClickListenner() {
            @Override
            public void onItemClick(int position, String text) {
                tv_2.setText(text);
            }
        });
        jgqjPopuwindow.setItemListenner(new JgqjPopuwindow.IOnListItemClickListenner() {
            @Override
            public void onItemClick(int position, String text) {
                tv_4.setText(text);
            }
        });
        cyPopuwindow.setOnDismissListener(this);
        pxPopuwindow.setOnDismissListener(this);
        jgqjPopuwindow.setOnDismissListener(this);

        searchCy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                ((TextView) view).setTextColor(Color.parseColor("#EA6F5A"));
                if (!cyPopuwindow.isShowing()) {
                    cyPopuwindow.showAsDropDown(view, view.getLayoutParams().width / 2, 18);
                } else {
                    cyPopuwindow.dismiss();
                }
                break;
            case R.id.tv_2:
                ((TextView) view).setTextColor(Color.parseColor("#EA6F5A"));
                if (!pxPopuwindow.isShowing()) {
                    pxPopuwindow.showAsDropDown(view, view.getLayoutParams().width / 2, 18);
                } else {
                    pxPopuwindow.dismiss();
                }
                break;
            case R.id.tv_3:
                startActivity(new Intent(CyFragment.this.getActivity(), PpxzActivity.class));
                break;
            case R.id.tv_4:
                ((TextView) view).setTextColor(Color.parseColor("#EA6F5A"));
                if (!pxPopuwindow.isShowing()) {
                    jgqjPopuwindow.showAsDropDown(view, view.getLayoutParams().width / 2, 18);
                } else {
                    jgqjPopuwindow.dismiss();
                }
                break;
            case R.id.gdsx:
                Intent intent = new Intent(CyFragment.this.getActivity(), GdsxActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDismiss() {
        tv_1.setTextColor(Color.parseColor("#666666"));
        tv_2.setTextColor(Color.parseColor("#666666"));
        tv_3.setTextColor(Color.parseColor("#666666"));
        tv_4.setTextColor(Color.parseColor("#666666"));
    }


    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Data2> data = new ArrayList<>();

        public void setData(List<Data2> data) {
            if (null != data) {
                this.data = data;
                notifyDataSetChanged();
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_cy, viewGroup, false);
            CyFragment.ListAdapter.VHolder vHolder = new CyFragment.ListAdapter.VHolder(view);
            return vHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ((VHolder) viewHolder).setData();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class VHolder extends RecyclerView.ViewHolder {
            private ImageView gx_iv, the_new, iv_j, iv_p;
            private TextView tv_title, tv_sp, tv_gl, tv_dd, tv_jg, gx_time;

            public VHolder(@NonNull View itemView) {
                super(itemView);
                gx_iv = itemView.findViewById(R.id.gx_iv);
                the_new = itemView.findViewById(R.id.the_new);
                iv_j = itemView.findViewById(R.id.iv_j);
                iv_p = itemView.findViewById(R.id.iv_p);

                tv_title = itemView.findViewById(R.id.tv_title);
                tv_sp = itemView.findViewById(R.id.tv_sp);
                tv_gl = itemView.findViewById(R.id.tv_gl);
                tv_dd = itemView.findViewById(R.id.tv_dd);
                tv_jg = itemView.findViewById(R.id.tv_jg);
                gx_time = itemView.findViewById(R.id.gx_time);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CyFragment.this.getActivity(), CyDetailActivity.class);
                        CyFragment.this.getActivity().startActivity(intent);
                    }
                });
            }

            public void setData() {
                int position = getAdapterPosition();
                Data2 data2 = data.get(position);

                Img.load(gx_iv, data2.profile_picture);
                tv_title.setText(data2.name);
                tv_sp.setText(data2.license_plate_time);
                tv_gl.setText(data2.mileage + "公里");
                tv_dd.setText(data2.location);
                tv_jg.setText(data2.retail_offer + "万");
                gx_time.setText(data2.publish_time);

                if (data2.is_new_car == 1)//是否新车 0否 1是
                {
                    the_new.setVisibility(View.VISIBLE);
                } else {
                    the_new.setVisibility(View.GONE);
                }
                if (data2.urgent == 1)//急售 0否 1是
                {
                    iv_j.setVisibility(View.VISIBLE);
                } else {
                    iv_j.setVisibility(View.GONE);
                }
                if (data2.wholesale == 1)//批发 0否 1是
                {
                    iv_p.setVisibility(View.VISIBLE);
                } else {
                    iv_p.setVisibility(View.GONE);
                }
            }
        }
    }

    public void searchCy() {
        HttpData.getInstance().searchCy("", new Observer<HttpResult<Data1>>() {
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
            public void onNext(HttpResult<Data1> result) {
                if (result.status == 200) {
                    adapter.setData(result.data.info);
//                    adapter.setData(result.data.info);
//                    adapter.startBanner(result.data.ads);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }

}
