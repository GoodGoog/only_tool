package com.example.common.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.common.BR;

import java.lang.reflect.ParameterizedType;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends BaseViewModel> extends Fragment {

    protected B binding ;

    protected VM viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,getLayoutId(),container,false);
        //创建viewModel
        //由泛型高出对应的ViewModel的class
        Class<VM> vClass = (Class<VM>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        viewModel = (new ViewModelProvider(this)).get(vClass);
        binding.setVariable(BR.viewModel,viewModel);
        initData(savedInstanceState);

        return binding.getRoot();
    }


    protected abstract void initData(@Nullable Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected void startActivity(Class c){
        Intent intent = new Intent();
        intent.setClass(getContext(),c);
        startActivity(intent);
    }

}
