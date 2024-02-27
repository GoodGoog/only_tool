package com.example.more;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.common.base.BaseActivity;
import com.example.common.base.BaseViewModel;
import com.example.more.databinding.MoreActivityOkHttpBinding;

public class OkHttpActivity extends BaseActivity<MoreActivityOkHttpBinding, BaseViewModel> {

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.more_activity_ok_http;
    }
}