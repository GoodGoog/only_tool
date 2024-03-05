package com.example.common.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.example.common.network.NetworkCallBack;
import com.example.common.network.NetworkRequest;

import java.util.HashMap;

public class BaseViewModel extends ViewModel implements DefaultLifecycleObserver {

    MutableLiveData<String> printMsg = new MutableLiveData<>();

    protected void logD(String msg){
        printMsg.setValue(msg);
    }

    /**
     * 网络请求
     */
    public <T> void request(String baseUrl,HashMap<String,String> requestMsg,Class<T> tClass, NetworkCallBack<T> callBack){
        NetworkRequest
                .build()
                .execute(baseUrl,requestMsg,tClass,callBack);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onCreate(owner);
        logD("onCreate");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        logD("onStart");
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onResume(owner);
        logD("onResume");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onPause(owner);
        logD("onPause");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStop(owner);
        logD("onStop");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onDestroy(owner);
        logD("onDestroy");
    }
}
