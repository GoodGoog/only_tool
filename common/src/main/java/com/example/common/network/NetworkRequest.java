package com.example.common.network;

import com.example.common.util.LogUtil;
import com.google.gson.Gson;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangqy
 * Data : 2024/2/29
 */
public class NetworkRequest{

    public static NetworkRequest build(){
        return new NetworkRequest();
    }

    public <T> void execute(String baseUrl, HashMap<String,String> requestMsg,Class<T> tClass, NetworkCallBack<T> listener){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //似乎查了一个拦截器
        // 创建一个retrofit
        Retrofit retrofit =(new Retrofit.Builder())
                .client(builder.build())
                // 设置基址
                .baseUrl(baseUrl)
                // 适配rxjava，目的在于使用观察者模式，分解上层请求的过程，便于我们横加干预（比如请求嵌套）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 使用Gson框架解析请求返回的结果，因为返回的是xml，只有解析过后，才能将数据变为对象，放置到我们刚刚创建你的实体类当中，便于数据的传递使用
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        INetworkService request = retrofit.create(INetworkService.class);

        //无法返回Call<T>,故使用Object接收数据网络数据,后自行转换为目标类型
        Call<Object> dataCall = request.postResult(requestMsg);

        dataCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                LogUtil.d(NetworkRequest.class.getName(),gson.toJson(response.body()));
                T t = gson.fromJson(gson.toJson(response.body()),tClass);
                listener.onSuccess(t);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }
}
