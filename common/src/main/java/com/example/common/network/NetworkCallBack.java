package com.example.common.network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zhangqy
 * Data : 2024/2/29
 */
public abstract class NetworkCallBack<T> {

    public void onFailure(Throwable t) {

    }

    public abstract void onSuccess(T t);
}
