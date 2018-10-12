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
import android.widget.TextView;

import com.yyz.cyuanw.R;

import java.util.ArrayList;
import java.util.List;

public class ListPopuwindow extends BasePopuwindow {
    public View mView;
    private ListAdapter adapter;
    private RecyclerView list;
    private List<String> datas = new ArrayList<>();

    public ListPopuwindow(Activity context) {
        super(context);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_list, null);
        // 设置选择的popuwndow的View
        this.setContentView(mView);

        list = mView.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(context));
        adapter = new ListAdapter();
        list.setAdapter(adapter);
    }

    public void setDatas(List<String> datas) {
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
            String text = datas.get(i);
            ((VHolder) viewHolder).setData(text);
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
                            listenner.onItemClick(index,datas.get(index),"");
                        }
                        notifyDataSetChanged();
                        ListPopuwindow.this.dismiss();
                    }
                });
            }

            public void setData(String text) {
                tv.setText(text);
                tv.setTextColor(Color.parseColor("#666666"));
                int position = getAdapterPosition();
                if (index == position) {
                    tv.setTextColor(Color.parseColor("#EA6F5A"));
                }
            }
        }
    }


}
