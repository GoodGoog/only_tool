package com.example.more.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.more.R;
import com.example.more.bean.TeamBean;
import com.example.more.databinding.MoreItemWindowScoreBinding;

import java.util.ArrayList;

public class WindowScoreAdapter extends RecyclerView.Adapter<WindowScoreAdapter.MyViewHolder>{
    public ArrayList<TeamBean> data;

    public WindowScoreAdapter(ArrayList<TeamBean> data) {
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
        TeamBean bean = data.get(position);
        holder.binding.tvCupName.setText(bean.getCupName());
        holder.binding.tvDoubleTeamName.setText(bean.getLeft_team_name() + "VS" + bean.getRight_team_name());
        holder.binding.etScoreInput.setText(bean.getLeft_team_raw_score());
//        holder.binding.etScoreInput.addTextChangedListener(new OnScoreChangedListener() {
//            @Override
//            public void onTextChangedFinish(Editable s) {
//                //文本改变了
//                String score = "0";
//                if (s.toString().isEmpty()){
//                    score = "0";
//                }else {
//                    score = s.toString();
//                }
//                TeamBean data = new TeamBean(bean.getCupName(),bean.getLeft_team_name(),bean.getRight_team_name(),score);
//                bean.getOnClickListener().onClick(data);
//            }
//        });
        holder.binding.etScoreInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                }else {
                    //失去焦点
//                    String text = holder.binding.etScoreInput.getText().toString();
//                    String score = "0";
//                    if (text.isEmpty()){
//                        score = "0";
//                    }else {
//                        score = text;
//                    }
                    TeamBean data = new TeamBean(bean.getCupName(),bean.getLeft_team_name(),bean.getRight_team_name(),holder.binding.etScoreInput.getText().toString());
                    bean.getOnScoreEditTextChangeListener().onChange(data,position);
                }
            }
        });
//        holder.binding.layoutContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        MoreItemWindowScoreBinding binding;

        public MyViewHolder(MoreItemWindowScoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    static abstract class OnScoreChangedListener implements TextWatcher{

        public abstract void onTextChangedFinish(Editable s);

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
           onTextChangedFinish(s);
        }
    }
}
