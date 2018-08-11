package com.yyz.cyuanw.activity;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LmMemberActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView textView;
    @BindView(R.id.title_right_icon)
    ImageView right;
    private RecyclerView list;
    private List dataList = new ArrayList();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lm_member;
    }

    @Override
    public void initView() {
        textView.setText("联盟成员");
        right.setImageResource(R.mipmap.icon_2);
        right.setVisibility(View.VISIBLE);


        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");

        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new LmMemberActivity.ListAdapter());
    }

    @Override
    public void initData() {

    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View viewB = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_lm_member, viewGroup, false);
            LmMemberActivity.ListAdapter.VBHolder vBHolder = new LmMemberActivity.ListAdapter.VBHolder(viewB);
            return vBHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        private class VBHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private Button left;

            public VBHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Intent intent = new Intent(LmMemberActivity.this, LmDetailActivity.class);
//                        startActivity(intent);
                    }
                });
                left = itemView.findViewById(R.id.left);
                left.setOnClickListener(this);
                itemView.findViewById(R.id.delete).setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.left:
                        ToastUtil.show(LmMemberActivity.this,"取消管理员");
                        break;
                    case R.id.delete:
                        ToastUtil.show(LmMemberActivity.this,"删除");
                        break;
                }
            }
        }

    }

}
