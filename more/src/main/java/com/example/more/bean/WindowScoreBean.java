package com.example.more.bean;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class WindowScoreBean extends BaseObservable {
    private String cupName ;

    private String left_team_name;

    private String right_team_name;

    private String left_team_raw_score ;
    private OnScoreItemClickListener onClickListener ;

    public WindowScoreBean(String cupName, String left_team_name, String right_team_name, OnScoreItemClickListener onClickListener) {
        this.cupName = cupName;
        this.left_team_name = left_team_name;
        this.right_team_name = right_team_name;
        this.onClickListener = onClickListener;
    }

    public WindowScoreBean(String cupName, String left_team_name, String right_team_name,String left_team_raw_score, OnScoreItemClickListener onClickListener) {
        this.cupName = cupName;
        this.left_team_name = left_team_name;
        this.right_team_name = right_team_name;
        this.left_team_raw_score = left_team_raw_score;
        this.onClickListener = onClickListener;
    }


    public String getCupName() {
        return cupName;
    }

    public void setCupName(String cupName) {
        this.cupName = cupName;
    }

    public String getLeft_team_name() {
        return left_team_name;
    }

    public String getLeft_team_raw_score() {
        return left_team_raw_score;
    }

    public void setLeft_team_raw_score(String left_team_raw_score) {
        this.left_team_raw_score = left_team_raw_score;
    }

    public void setLeft_team_name(String left_team_name) {
        this.left_team_name = left_team_name;
    }

    public String getRight_team_name() {
        return right_team_name;
    }

    public void setRight_team_name(String right_team_name) {
        this.right_team_name = right_team_name;
    }

    @Bindable
    public OnScoreItemClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnScoreItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
