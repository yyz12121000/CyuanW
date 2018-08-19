package com.yyz.cyuanw.view.sortrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.tools.Img;

import java.util.ArrayList;
import java.util.List;

public class SortAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mInflater;
    private List<SortModel> mData = new ArrayList<>();
    private List<SortModel> topList = new ArrayList<>();
    private Context mContext;

    public SortAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setTopData(List<SortModel> data) {
        topList = data;
        notifyDataSetChanged();
    }
    public void setData(List<SortModel> data) {
        mData.clear();
        mData.add(null);
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    public ViewTopHolder viewTopHolder;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = mInflater.inflate(R.layout.list_ppxz_top, parent, false);
            viewTopHolder = new ViewTopHolder(view);
            viewTopHolder.tvTag = (TextView) view.findViewById(R.id.tag);
            viewTopHolder.tvName = (TextView) view.findViewById(R.id.name);
            return viewTopHolder;
        } else {
            View view = mInflater.inflate(R.layout.list_item_ppxz, parent, false);
            ViewItemHolder viewHolder = new ViewItemHolder(view);
            viewHolder.tvTag = (TextView) view.findViewById(R.id.tag);
            viewHolder.tvName = (TextView) view.findViewById(R.id.name);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (position == 0) {
            viewTopHolder.setData(topList);
            return;
        }
        final ViewItemHolder holder = (ViewItemHolder) viewHolder;
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvTag.setVisibility(View.VISIBLE);
            holder.tvTag.setText(mData.get(position).getLetters());
        } else {
            holder.tvTag.setVisibility(View.GONE);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,  mData.get(position).getId());
                }
            });

        }

        holder.tvName.setText(this.mData.get(position).getName());

//        holder.tvName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, mData.get(position).getName(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //**********************itemClick************************
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    //**************************************************************

    public class ViewTopHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvName;

        LinearLayout row_1, row_2;

        public ViewTopHolder(View itemView) {
            super(itemView);
            row_1 = itemView.findViewById(R.id.row_1);
            row_2 = itemView.findViewById(R.id.row_2);


        }

        public void setData(List<SortModel> list) {
            for (int i = 0; (i < list.size() && i < 5); i++) {
                ImageView icon = (ImageView) ((LinearLayout) row_1.getChildAt(i)).getChildAt(0);
                TextView name = (TextView) ((LinearLayout) row_1.getChildAt(i)).getChildAt(1);

                String nameS = list.get(i).getName();
                String url = list.get(i).getLogo();
                Img.load(icon, url);
                name.setText(nameS);

                int finalI = i;
                row_1.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(row_1,  list.get(finalI).getId());
                        }
                    }
                });
            }

            if (list.size() > 5) {
                for (int i = 0; (i < list.size() - 5 && i < 5); i++) {
                    ImageView icon = (ImageView) ((LinearLayout) row_2.getChildAt(i)).getChildAt(0);
                    TextView name = (TextView) ((LinearLayout) row_2.getChildAt(i)).getChildAt(1);

                    String nameS = list.get(i + 5).getName();
                    String url = list.get(i + 5).getLogo();
                    Img.load(icon, url);
                    name.setText(nameS);

                    int finalI = i;
                    row_2.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(row_1,  list.get(finalI + 5).getId());
                            }
                        }
                    });
                }
            }


        }

    }

    public static class ViewItemHolder extends RecyclerView.ViewHolder {
        TextView tvTag, tvName;

        public ViewItemHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 提供给Activity刷新数据
     *
     * @param list
     */
    public void updateList(List<SortModel> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mData.get(position).getLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 1; i < getItemCount(); i++) {
            String sortStr = mData.get(i).getLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

}
