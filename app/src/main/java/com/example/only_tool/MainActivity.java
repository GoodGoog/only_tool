package com.example.only_tool;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.common.base.BaseActivity;
import com.example.only_tool.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainViewModel> {


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        viewModel.getName().setValue("换个优雅的名字");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
}