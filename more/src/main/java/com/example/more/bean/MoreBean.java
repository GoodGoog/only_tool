package com.example.more.bean;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class MoreBean extends BaseObservable {
    private int iconId ;

    private String name;

    private View.OnClickListener onClickListener ;

    public MoreBean(int iconId, String name, View.OnClickListener onClickListener) {
        this.iconId = iconId;
        this.name = name;
        this.onClickListener = onClickListener;
    }

    @Bindable
    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}

