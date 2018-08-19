package com.yyz.cyuanw.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.view.sortrecyclerview.SortModel;

import java.util.ArrayList;
import java.util.List;

public class PpxzSecondPopuwindow extends BasePopuwindow {
    public View mView;
    private ListAdapter adapter;
    private RecyclerView list;
    private List<SortModel> datas = new ArrayList<>();

    public PpxzSecondPopuwindow(Activity context) {
        super(context);
        int width = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置弹出的popuwindow的宽
        this.setWidth(width/2);
        // 设置弹出的popuwndow的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 刷新状态
        this.update();


        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_list_ppxz_second, null);
        // 设置选择的popuwndow的View
        this.setContentView(mView);

        list = mView.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ListAdapter();
        list.setAdapter(adapter);
    }

    public void setDatas(List<SortModel> datas) {
        this.datas = datas;
        adapter.notifyDataSetChanged();
    }

    private class ListAdapter extends RecyclerView.Adapter {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_pop, viewGroup, false);
            VHolder vHolder = new VHolder(view);
            return vHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

            ((VHolder) viewHolder).setData(datas.get(i));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }


        private int index = 0;

        class VHolder extends RecyclerView.ViewHolder {
            TextView tv;

            public VHolder(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        index = getAdapterPosition();
                        if (null != listenner){
                            listenner.onItemClick(datas.get(index).getId(),datas.get(index).getName());
                        }
                        notifyDataSetChanged();
                        PpxzSecondPopuwindow.this.dismiss();
                    }
                });
            }

            public void setData(SortModel sortModel) {
                tv.setText(sortModel.getName());
                if (sortModel.getType() == 1){
                    tv.setTextColor(Color.parseColor("#333333"));
                }else {
                    tv.setTextColor(Color.parseColor("#999999"));
                }
//                tv.setTextColor(Color.parseColor("#999999"));
//                int position = getAdapterPosition();
//                if (index == position) {
//                    tv.setTextColor(Color.parseColor("#ff0000"));
//                }
            }
        }
    }


}
