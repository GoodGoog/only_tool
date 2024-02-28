package com.example.more.retrofit;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.common.base.BaseActivity;
import com.example.common.base.BaseViewModel;
import com.example.more.R;
import com.example.more.databinding.MoreActivityRetrofitBinding;

public class RetrofitActivity extends BaseActivity<MoreActivityRetrofitBinding, BaseViewModel> {


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.more_activity_retrofit;
    }
}