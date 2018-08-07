package com.yyz.cyuanw.activity.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.tools.LogManager;

import java.util.ArrayList;
import java.util.List;

public class SyFragment extends Fragment {
    private RecyclerView list;
    private List dataList = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sy, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");
        dataList.add("");

        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(new ListAdapter());
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            switch (i) {
                case 0:
                    View viewA = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chunk_sy_a, viewGroup, false);
                    VAHolder vAHolder = new VAHolder(viewA);
                    return vAHolder;
//                case 1:
//                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_gx, viewGroup, false);
//                    VAHolder vHolder = new VAHolder(view);
//                    return vHolder;
//                case 2:
//                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_gx, viewGroup, false);
//                    VAHolder vHolder = new VAHolder(view);
//                    return vHolder;
//                case 3:
//                    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_gx, viewGroup, false);
//                    VAHolder vHolder = new VAHolder(view);
//                    return vHolder;
                default:
                    View viewB = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_sy, viewGroup, false);
                    VBHolder vBHolder = new VBHolder(viewB);
                    return vBHolder;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        private class VAHolder extends RecyclerView.ViewHolder {
            public VAHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
        private class VBHolder extends RecyclerView.ViewHolder {
            public VBHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

    }

}
