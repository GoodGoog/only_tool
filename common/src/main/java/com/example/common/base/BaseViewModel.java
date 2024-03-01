package com.example.common.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.common.network.NetworkCallBack;
import com.example.common.network.NetworkRequest;

import java.util.HashMap;

public class BaseViewModel extends ViewModel {

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

}
