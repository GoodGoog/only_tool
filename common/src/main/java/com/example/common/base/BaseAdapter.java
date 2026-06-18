package com.example.common.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//binding,data
public abstract class BaseAdapter<B extends ViewDataBinding, D> extends RecyclerView.Adapter<BaseAdapter.CommonViewHolder> {

    public ArrayList<D> beans;
    public int layoutId;

    public BaseAdapter(ArrayList<D> data, int layoutId) {
        super();
        this.beans = data;
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public BaseAdapter.CommonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_more_rv,parent,false);
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutId, parent, false);
        return new CommonViewHolder<B>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder holder, int position) {
//        holder.binding.tvCupName.setText(bean.getCupName());
//        holder.binding.tvDoubleTeamName.setText(bean.getLeft_team_name() + "VS" + bean.getRight_team_name());
        bindViewHolder((B) holder.binding, beans,position);
    }

    public abstract void bindViewHolder(B binding,ArrayList<D> data,int position);

    @Override
    public int getItemCount() {
        return beans.size();
    }

    static class CommonViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

        B binding;

        public CommonViewHolder(B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
