package com.example.more.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.more.R;
import com.example.more.bean.TeamBean;
import com.example.more.databinding.MoreItemTeamChooseRvBinding;

import java.util.ArrayList;

public class  TeamAdapter extends RecyclerView.Adapter<TeamAdapter.MyViewHolder> {

    public ArrayList<TeamBean> data;

    public TeamAdapter(ArrayList<TeamBean> data) {
        super();
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_more_rv,parent,false);
        MoreItemTeamChooseRvBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.more_item_team_choose_rv, parent, false);
        MyViewHolder holder = new MyViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TeamBean bean = data.get(position);
        holder.binding.setBean(bean);
        holder.binding.tvCupName.setText(bean.getCupName());
        holder.binding.tvDoubleTeamName.setText(bean.getLeft_team_name() + "VS" + bean.getRight_team_name());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        MoreItemTeamChooseRvBinding binding;

        public MyViewHolder(MoreItemTeamChooseRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}