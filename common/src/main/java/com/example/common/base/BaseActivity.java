package com.example.common.base;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.common.BR;
import com.example.common.util.LogUtil;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

public abstract class BaseActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends AppCompatActivity {

    protected B binding;
    protected VM viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), getLayoutId(), null, false);
        binding.setLifecycleOwner(this);
        //创建viewModel
        //由泛型高出对应的ViewModel的class
        Class<VM> vClass = (Class<VM>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        viewModel = (new ViewModelProvider(this)).get(vClass);
        //绑定布局
        setContentView(binding.getRoot());
        binding.setVariable(BR.viewModel, viewModel);
        initData(savedInstanceState);
        initObserver();
    }

    private void initObserver() {
        viewModel.printMsg.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                logD(s);
            }
        });
    }

    protected abstract void initData(@Nullable Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected void logD(String msg) {
        LogUtil.d(this.getClass().getName().toString() + "|++++++++++++++++++++++++++|", msg);
    }

    //复制功能
    protected void copyTextToSystem(String input, Boolean isShowSuccess) {
        if (isShowSuccess) showToast("已复制");
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", input);
        clipboardManager.setPrimaryClip(clipData);
    }

    //读取手机粘贴板第一个文本
    protected String parseTextFromSystem() {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        //检查时候粘贴板含有文本
        if (clipboardManager.hasPrimaryClip() && Objects.requireNonNull(clipboardManager.getPrimaryClip()).getItemCount() > 0) {
            ClipData.Item item = Objects.requireNonNull(clipboardManager.getPrimaryClip()).getItemAt(0);
            return item.getText().toString();
        }
        return "剪切板为空";
    }

    protected void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();
    }

    protected void startActivity(Class<?> c) {
        Intent intent = new Intent();
        intent.setClass(this, c);
        startActivity(intent);
    }

}