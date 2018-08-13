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
import android.widget.EditText;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.tools.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class JgqjPopuwindow extends BasePopuwindow implements View.OnClickListener {
    public View mView;
    private TextView gj_a, gj_b, gj_c, gj_d, gj_e, gj_f, gj_g, gj_h, gj_i, gj_j, sure;
    private EditText jg_a, jg_b;


    public JgqjPopuwindow(Activity context) {
        super(context);
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_jgqj, null);
        // 设置选择的popuwndow的View
        this.setContentView(mView);

        init();

    }

    private void init() {
        gj_a = mView.findViewById(R.id.gj_a);
        gj_b = mView.findViewById(R.id.gj_b);
        gj_c = mView.findViewById(R.id.gj_c);
        gj_d = mView.findViewById(R.id.gj_d);
        gj_e = mView.findViewById(R.id.gj_e);
        gj_f = mView.findViewById(R.id.gj_f);
        gj_g = mView.findViewById(R.id.gj_g);
        gj_h = mView.findViewById(R.id.gj_h);
        gj_i = mView.findViewById(R.id.gj_i);

        jg_a = mView.findViewById(R.id.jg_a);
        jg_b = mView.findViewById(R.id.jg_b);

        sure = mView.findViewById(R.id.sure);


        gj_a.setOnClickListener(this);
        gj_b.setOnClickListener(this);
        gj_c.setOnClickListener(this);
        gj_d.setOnClickListener(this);
        gj_e.setOnClickListener(this);
        gj_f.setOnClickListener(this);
        gj_g.setOnClickListener(this);
        gj_h.setOnClickListener(this);
        gj_i.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sure://确定
                int jgA = Integer.parseInt(jg_a.getText().toString());
                int jgB = Integer.parseInt(jg_b.getText().toString());
                if (jgA >= jgB) {
                    ToastUtil.show(context, "请输入正确的价格区间!");
                    return;
                }
                gj_a.setTextColor(Color.parseColor("#666666"));
                gj_b.setTextColor(Color.parseColor("#666666"));
                gj_c.setTextColor(Color.parseColor("#666666"));
                gj_d.setTextColor(Color.parseColor("#666666"));
                gj_e.setTextColor(Color.parseColor("#666666"));
                gj_f.setTextColor(Color.parseColor("#666666"));
                gj_g.setTextColor(Color.parseColor("#666666"));
                gj_h.setTextColor(Color.parseColor("#666666"));
                gj_i.setTextColor(Color.parseColor("#666666"));
                if (listenner != null) {
                    listenner.onItemClick(-1, jgA + "万-" + jgB + "万");
                }
                break;
            default:
                TextView tv = (TextView) view;
                tv.setTextColor(Color.parseColor("#EA6F5A"));
                if (listenner != null) {
                    listenner.onItemClick(-1, tv.getText().toString());
                }
        }
        dismiss();
    }
}
