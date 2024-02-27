package com.example.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.common.BR;

import java.lang.reflect.ParameterizedType;

public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

    protected B binding ;
    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this),getLayoutId(),null,false);
        //创建viewModel
        //由泛型高出对应的ViewModel的class
        Class<VM> vClass = (Class<VM>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        viewModel = (new ViewModelProvider(this)).get(vClass);
        //绑定布局
        setContentView(binding.getRoot());
        binding.setVariable(BR.viewModel,viewModel);
        initData(savedInstanceState);
    }

    protected abstract void initData(@Nullable Bundle savedInstanceState);

    protected abstract int getLayoutId();
}