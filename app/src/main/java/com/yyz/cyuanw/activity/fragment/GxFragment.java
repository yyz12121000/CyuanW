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

public class GxFragment extends Fragment {
    private RecyclerView list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gx, container, false);

        init(view);

        LogManager.d("===onCreateView");
        return view;
    }

    private void init(View view) {
        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(new ListAdapter());
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_gx,viewGroup,false);
            VHolder vHolder = new VHolder(view);
            return vHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 20;
        }

        private class VHolder extends RecyclerView.ViewHolder{
            public VHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

    }

}
