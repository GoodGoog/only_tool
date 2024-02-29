package com.example.common.base;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.common.BR;
import com.example.common.network.NetworkCallBack;
import com.example.common.network.NetworkRequest;
import com.example.common.util.LogUtil;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

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
        initObserver();
    }

    private void initObserver(){
        viewModel.printMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                logD(s);
            }
        });
    }

    protected abstract void initData(@Nullable Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected void logD(String msg){
        LogUtil.d(this.getClass().getName().toString() + "-log",msg);
    }
}