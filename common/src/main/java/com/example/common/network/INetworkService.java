package com.example.common.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by zhangqy
 * Data : 2024/2/29
 */
public interface INetworkService<T> {
    @POST(".")
    Call<T> postResult(@QueryMap Map<String,String> map);

}
