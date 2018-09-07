package com.yyz.cyuanw.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.CyDetailActivity;
import com.yyz.cyuanw.activity.fragment.CyFragment;
import com.yyz.cyuanw.bean.Data2;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.StringUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyShopListViewAdapter extends BaseAdapter {
    private Context context;
    public LayoutInflater flater;
    private List<Data2> list;

    private int flag;

    public void setFlag(int flag){
        this.flag = flag;
    }

    public MyShopListViewAdapter(Context context, List<Data2> list,int flag) {
        this.context = context;
        this.flater = LayoutInflater.from(context);
        this.list = list;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = flater.inflate(R.layout.list_item_myshop, null);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Data2 data2 = list.get(position);

        Img.load(holder.imageView, data2.cover);
        holder.titleView.setText(data2.name);
        holder.spView.setText(data2.license_plate_time);
        holder.glView.setText(data2.mileage + "公里");
        holder.ddView.setText(data2.location);
        holder.priceView.setText(data2.retail_offer + "万");
        holder.timeView.setText(data2.publish_time);

        if (data2.is_new_car == 0)//是否新车 0否 1是
        {
            holder.newView.setVisibility(View.VISIBLE);
        } else {
            holder.newView.setVisibility(View.GONE);
        }
        if (data2.urgent == 1)//急售 0否 1是
        {
            holder.jView.setVisibility(View.VISIBLE);
        } else {
            holder.jView.setVisibility(View.GONE);
        }
        if (data2.wholesale == 1)//批发 0否 1是
        {
            holder.pView.setVisibility(View.VISIBLE);
        } else {
            holder.pView.setVisibility(View.GONE);
        }

        if (flag == 0){
            holder.comeView.setVisibility(View.INVISIBLE);
        }else{
            holder.comeView.setVisibility(View.VISIBLE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CyDetailActivity.class);
                intent.putExtra("id", data2.id);
                context.startActivity(intent);
            }
        });


        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.gx_iv)
        ImageView imageView;
        @BindView(R.id.the_new)
        ImageView newView;
        @BindView(R.id.tv_title)
        TextView titleView;
        @BindView(R.id.the_come)
        ImageView comeView;
        @BindView(R.id.tv_sp)
        TextView spView;
        @BindView(R.id.tv_gl)
        TextView glView;
        @BindView(R.id.tv_dd)
        TextView ddView;
        @BindView(R.id.tv_jg)
        TextView priceView;
        @BindView(R.id.iv_j)
        ImageView jView;
        @BindView(R.id.iv_p)
        ImageView pView;
        @BindView(R.id.gx_time)
        TextView timeView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}