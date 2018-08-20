package com.yyz.cyuanw.activity;

import android.content.Intent;
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

import butterknife.BindView;

public class ListChooseActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView titleView;

    private ListAdapter adapter;
    private RecyclerView list;
    private List<String> datas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_list_choose;
    }


    @Override
    public void initView() {

        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter();
        list.setAdapter(adapter);
    }

    @Override
    public void initData() {
        this.datas = getIntent().getStringArrayListExtra("list");
        String title = getIntent().getStringExtra("title");

        setTitle(titleView, title);
        adapter.notifyDataSetChanged();
    }

    private class ListAdapter extends RecyclerView.Adapter {
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_pop, viewGroup, false);
            ListAdapter.VHolder vHolder = new ListAdapter.VHolder(view);
            return vHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            String text = datas.get(i);
            ((ListAdapter.VHolder) viewHolder).setData(text);
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
//                        if (null != listenner) {
//                            listenner.onItemClick(index, datas.get(index));
//                        }
                        notifyDataSetChanged();
//                        ListPopuwindow.this.dismiss();

                        Intent intent = new Intent();
                        intent.putExtra("index", index);
                        intent.putExtra("text", datas.get(index));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }

            public void setData(String text) {
                tv.setText(text);
//                tv.setTextColor(Color.parseColor("#666666"));
//                int position = getAdapterPosition();
//                if (index == position) {
//                    tv.setTextColor(Color.parseColor("#EA6F5A"));
//                }
            }
        }
    }


}
