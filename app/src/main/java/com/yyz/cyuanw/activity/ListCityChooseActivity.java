package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.bean.Data9;
import com.yyz.cyuanw.view.choosecity.DBManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ListCityChooseActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView titleView;

    private ListAdapter adapter;
    private RecyclerView list;
    private List<Data9> datas = new ArrayList<>();
    DBManager dbManager;
    private int type;

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
        dbManager = new DBManager(this);

    }

    @Override
    public void initData() {
        type = getIntent().getIntExtra("type", -100);

        if (type == 4) {
            int id = getIntent().getIntExtra("shi_id", -100);
            datas = dbManager.getQuByShengId(id);
            setTitle(titleView, getIntent().getStringExtra("city"));
            adapter.notifyDataSetChanged();
        } else {
            int id = getIntent().getIntExtra("sheng_id", -100);
            datas = dbManager.getCityByShengId(id);
            setTitle(titleView, getIntent().getStringExtra("sheng_name"));
            adapter.notifyDataSetChanged();
        }


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
            String text = datas.get(i).name;
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

                        notifyDataSetChanged();
                        if (type == 4) {
                            Intent intent = getIntent();
                            intent.putExtra("qu_id", datas.get(index).id);
                            intent.putExtra("qu", datas.get(index).name);
                            setResult(RESULT_OK, intent);
                            finish();
                        } else if (type == 3) {
                            Intent intent = getIntent();
                            intent.setClass(ListCityChooseActivity.this, ListCityChooseActivity.class);

                            intent.putExtra("type", 4);
                            intent.putExtra("shi_id", datas.get(index).id);
                            intent.putExtra("city", datas.get(index).name);
                            startActivityForResult(intent, 2);
                        } else {
                            Intent intent = getIntent();
                            intent.putExtra("shi_id", datas.get(index).id);
                            intent.putExtra("city", datas.get(index).name);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case 2:
                if (null == data) return;
                setResult(RESULT_OK, data);
                finish();
                break;
        }
    }

}
