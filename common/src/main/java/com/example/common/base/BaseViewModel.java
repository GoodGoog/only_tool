package com.example.common.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.common.util.LogUtil;

public class BaseViewModel extends ViewModel {

    MutableLiveData<String> printMsg = new MutableLiveData<>();

    protected void logD(String msg){
        printMsg.setValue(msg);
    }

}
