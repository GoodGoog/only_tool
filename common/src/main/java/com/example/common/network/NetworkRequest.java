package com.example.common.network;

import com.example.common.util.LogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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


        //baseUrl需要以/结尾
         Retrofit retrofit = new Retrofit.Builder()
                 .baseUrl(getAimBaseUrl(baseUrl))
                 .addConverterFactory(GsonConverterFactory.create())
                 //.client(getOkHttpClient())
                 .build();

        INetworkService request = retrofit.create(INetworkService.class);

        //无法返回Call<T>,故使用Object接收数据网络数据,后自行转换为目标类型
        Call<Object> dataCall = request.getResult(getLastPath(baseUrl) ,requestMsg);

        dataCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Gson gson = new Gson();
                T t = gson.fromJson(gson.toJson(response.body()), tClass);
                LogUtil.d("网络返回的原始Json数据：",response.toString());
                listener.onSuccess(t);
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                listener.onFailure(t);
            }
        });

    }

    //拆分字符串
    String getAimBaseUrl(String baseUrl){
        if (isEndWithLine(baseUrl)) return baseUrl;
        String[] strArray = baseUrl.split("/");
        String lastPath = strArray[strArray.length -1];
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < strArray.length - 1;i ++){
            stringBuffer.append(strArray[i] + "/");
        }
        return stringBuffer.toString();
    }

    boolean isEndWithLine(String baseUrl){
        //'/'结尾
        return baseUrl.charAt(baseUrl.length() - 1) == '/';
    }

    String getLastPath(String baseUrl){
        if (isEndWithLine(baseUrl)) return "";
        String[] strArray = baseUrl.split("/");
        return strArray[strArray.length -1];
    }

}
