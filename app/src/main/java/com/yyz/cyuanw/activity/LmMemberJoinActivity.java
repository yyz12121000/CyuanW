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
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.CyData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data10;
import com.yyz.cyuanw.bean.Data11;
import com.yyz.cyuanw.bean.Data9;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;

public class LmMemberJoinActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView textView;

    private RecyclerView list;

    private int lm_id;
    private ListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lm_member_join;
    }

    @Override
    public void initView() {
        textView.setText("加入申请");

        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter adapter = new LmMemberJoinActivity.ListAdapter();
        list.setAdapter(adapter);
    }

    @Override
    public void initData() {
        lm_id = getIntent().getIntExtra("lm_id", 0);
        apply_join_users();
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public List<Data11> dataList = new ArrayList();

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public void setData(List<Data11> dataList) {
            if (null == dataList) return;
            this.dataList = dataList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View viewB = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_lm_member_join, viewGroup, false);
            LmMemberJoinActivity.ListAdapter.VBHolder vBHolder = new LmMemberJoinActivity.ListAdapter.VBHolder(viewB);
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
            private ImageView gx_iv;
            private TextView name, address, desc;
            private Button pass, dispass;

            public VBHolder(@NonNull View itemView) {
                super(itemView);
                gx_iv = itemView.findViewById(R.id.gx_iv);
                name = itemView.findViewById(R.id.name);
                address = itemView.findViewById(R.id.address);
                desc = itemView.findViewById(R.id.desc);
                pass = itemView.findViewById(R.id.pass);
                dispass = itemView.findViewById(R.id.dispass);
                pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Data11 data11 = dataList.get(position);
                        audit_apply(data11, 1);//拒绝传0 通过传1
                    }
                });
                dispass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        Data11 data11 = dataList.get(position);
                        audit_apply(data11, 0);//拒绝传0 通过传1
                    }
                });

            }

            public void setData() {
                int position = getAdapterPosition();
                Data11 data11 = dataList.get(position);
                Img.loadC(gx_iv, data11.user.profile_picture);
                name.setText(data11.user.real_name);
                address.setText(data11.user.address);
                desc.setText(data11.user.dealer_info.name);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.left:
                        ToastUtil.show(LmMemberJoinActivity.this, "取消管理员");
                        break;
                    case R.id.delete:
                        ToastUtil.show(LmMemberJoinActivity.this, "删除");
                        break;
                }
            }
        }

    }

    private void apply_join_users() {
        HttpData.getInstance().apply_join_users(lm_id, new Observer<HttpResult<Data10>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogManager.e(e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Data10> result) {
                if (result.status == 200) {
                    adapter.setData(result.data.data);
                }
//                ToastUtil.show(LmMemberActivity.this, result.message);
            }
        });
    }

    private void audit_apply(Data11 data11, int is_pass) {
        HttpData.getInstance().audit_apply(lm_id, data11.user.id, is_pass, new Observer<HttpResult<Data10>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogManager.e(e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Data10> result) {
                if (result.status == 200) {
                    adapter.dataList.remove(data11);
                    adapter.notifyDataSetChanged();
                }
//                ToastUtil.show(LmMemberActivity.this, result.message);
            }
        });
    }
}
