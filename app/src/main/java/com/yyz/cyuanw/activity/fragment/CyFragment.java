package com.yyz.cyuanw.activity.fragment;

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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.view.ListPopuwindow;

import java.util.ArrayList;
import java.util.List;

public class CyFragment extends Fragment implements View.OnClickListener, PopupWindow.OnDismissListener {
    private RecyclerView list;
    private ListPopuwindow cyPopuwindow;
    private ListPopuwindow pxPopuwindow;
    private TextView tv_1, tv_2;

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
        cyPopuwindow.setDatas(cydatas);
        pxPopuwindow.setDatas(pxDatas);
        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(new CyFragment.ListAdapter());

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
        cyPopuwindow.setOnDismissListener(this);
        pxPopuwindow.setOnDismissListener(this);
    }

    @Override
    public void onClick(View view) {
        TextView tv = (TextView) view;
        tv.setTextColor(Color.parseColor("#ff0000"));
        switch (view.getId()) {
            case R.id.tv_1:
                if (!cyPopuwindow.isShowing()) {
                    cyPopuwindow.showAsDropDown(view, view.getLayoutParams().width / 2, 18);
                } else {
                    cyPopuwindow.dismiss();
                }
                break;
            case R.id.tv_2:
                if (!pxPopuwindow.isShowing()) {
                    pxPopuwindow.showAsDropDown(view, view.getLayoutParams().width / 2, 18);
                } else {
                    pxPopuwindow.dismiss();
                }
                break;
        }
    }

    @Override
    public void onDismiss() {
        tv_1.setTextColor(Color.parseColor("#888888"));
        tv_2.setTextColor(Color.parseColor("#888888"));
    }


    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_cy, viewGroup, false);
            CyFragment.ListAdapter.VHolder vHolder = new CyFragment.ListAdapter.VHolder(view);
            return vHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }

        private class VHolder extends RecyclerView.ViewHolder {
            public VHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

    }
}
