package com.example.more.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.more.R;
import com.example.more.bean.MoreBean;
import com.example.more.databinding.MoreItemMoreRvBinding;

import java.util.ArrayList;

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MyViewHolder> {

    public ArrayList<MoreBean> data;

    public MoreAdapter(ArrayList<MoreBean> data) {
        super();
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_more_rv,parent,false);
        MoreItemMoreRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.more_item_more_rv, parent, false);
        MyViewHolder holder = new MyViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setBean(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        MoreItemMoreRvBinding binding;

        public MyViewHolder(MoreItemMoreRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
