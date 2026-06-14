package com.example.more.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.more.R;
import com.example.more.bean.TeamBean;
import com.example.more.bean.WindowScoreBean;
import com.example.more.databinding.MoreItemTeamChooseRvBinding;
import com.example.more.databinding.MoreItemWindowScoreBinding;

import java.util.ArrayList;

public class WindowScoreAdapter extends RecyclerView.Adapter<WindowScoreAdapter.MyViewHolder>{
    public ArrayList<WindowScoreBean> data;

    public WindowScoreAdapter(ArrayList<WindowScoreBean> data) {
        super();
        this.data = data;
    }

    @NonNull
    @Override
    public WindowScoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_more_rv,parent,false);
        MoreItemWindowScoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.more_item_window_score, parent, false);
        WindowScoreAdapter.MyViewHolder holder = new WindowScoreAdapter.MyViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WindowScoreAdapter.MyViewHolder holder, int position) {
        WindowScoreBean bean = data.get(0);
        holder.binding.tvCupName.setText(bean.getCupName());
        holder.binding.tvDoubleTeamName.setText(bean.getLeft_team_name() + "VS" + bean.getRight_team_name());
        holder.binding.etScoreInput.setText(bean.getLeft_team_raw_score());
        holder.binding.layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.getOnClickListener().onClick("zhe shi di " + position + "shuju");
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        MoreItemWindowScoreBinding binding;

        public MyViewHolder(MoreItemWindowScoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
