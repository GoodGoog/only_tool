package com.example.only_tool;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.only_tool.databinding.ActivityMainBinding;
//


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel viewModel = (new ViewModelProvider(this)).get(MainViewModel.class);
        binding.setVariable(com.example.common.BR.viewModel, viewModel);
        setContentView(binding.getRoot());
        binding.btnFirst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/main/SecondActivity").navigation();
            }
        });
    }
}

//public class MainActivity extends BaseActivity<ActivityMainBinding,MainViewModel>{
//    @Override
//    public void initData(@Nullable Bundle savedInstanceState) {
//        //getBinding().setVariable(BR.viewModel,getViewModel());
//    }
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_main;
//    }
//}