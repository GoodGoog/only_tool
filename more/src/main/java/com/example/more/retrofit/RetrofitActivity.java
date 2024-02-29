package com.example.more.retrofit;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.common.base.BaseActivity;
import com.example.common.base.BaseViewModel;
import com.example.common.network.INetworkService;
import com.example.common.network.NetworkCallBack;
import com.example.common.network.NetworkParams;
import com.example.common.network.NetworkUrl;
import com.example.common.util.LogUtil;
import com.example.more.R;
import com.example.more.databinding.MoreActivityRetrofitBinding;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public class RetrofitActivity extends BaseActivity<MoreActivityRetrofitBinding, BaseViewModel> {

//    @Override
//    public void onSuccess(String qqInfoResponse) {
//        logD("success--------------------" + qqInfoResponse);
//    }
//
//    @Override
//    public void onFailure(Call<String> call, Throwable t) {
//        super.onFailure(call, t);
//        logD("failure--------------" + t.getMessage());
//    }
    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //testWeather();
        //testJoker();
        HashMap<String, String> params = NetworkParams.createQqInfoRequestParams("1213715120");
        execute(NetworkUrl.QQ_NUMBER_INFO_URL, params, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                logD("success--------------------" + response);
                QqInfoResponse qqInfoResponse = (new Gson()).fromJson(response.body(),QqInfoResponse.class);
                logD("++++++++"+qqInfoResponse.getData().getEmail());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                logD("success--------------------" + t.getMessage());
            }
        });
    }

    public void execute(String baseUrl, HashMap<String, String> requestMsg, Callback<String> callBack) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 创建一个retrofit
        Retrofit retrofit = (new Retrofit.Builder())
                .client(builder.build())
                // 设置基址
                .baseUrl(baseUrl)
                // 适配rxjava，目的在于使用观察者模式，分解上层请求的过程，便于我们横加干预（比如请求嵌套）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 使用Gson框架解析请求返回的结果，因为返回的是xml，只有解析过后，才能将数据变为对象，放置到我们刚刚创建你的实体类当中，便于数据的传递使用
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        INetworkService iService = retrofit.create(INetworkService.class);
        Call<String> dataCall = iService.postResult(requestMsg);
        dataCall.enqueue(callBack);

    }

    public interface INetworkService {

        @POST(".")
        Call<String> postResult(@QueryMap Map<String, String> map);

    }


//    private void testWeather(){
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//
//        // 创建一个retrofit
//        Retrofit retrofit =(new Retrofit.Builder())
//                .client(builder.build())
//                // 设置基址
//                .baseUrl(TestUrl.WEATHER_SEARCH_URL)
//                // 适配rxjava，目的在于使用观察者模式，分解上层请求的过程，便于我们横加干预（比如请求嵌套）
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                // 使用Gson框架解析请求返回的结果，因为返回的是xml，只有解析过后，才能将数据变为对象，放置到我们刚刚创建你的实体类当中，便于数据的传递使用
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        TestRequest request = retrofit.create(TestRequest.class);
//        HashMap<String,String> map = new HashMap<>();
//        map.put("city","北京");
//        map.put("key",AppKey.TODAY_WEATHER_APP_KEY);
//
//        Call<WeatherBean> dataCall = request.postResult(map);
//
//        dataCall.enqueue(new Callback<WeatherBean>() {
//            @Override
//            public void onResponse(Call<WeatherBean> call, Response<WeatherBean> response) {
//                logD("下载成功"+response);
//            }
//
//            @Override
//            public void onFailure(Call<WeatherBean> call, Throwable t) {
//                logD("下载失败" + t.getMessage());
//            }
//        });
//    }

    private void testJoker() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 创建一个retrofit
        Retrofit retrofit = (new Retrofit.Builder())
                .client(builder.build())
                // 设置基址
                .baseUrl(TestUrl.QQ_NUMBER_INFO_URL)
                // 适配rxjava，目的在于使用观察者模式，分解上层请求的过程，便于我们横加干预（比如请求嵌套）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                // 使用Gson框架解析请求返回的结果，因为返回的是xml，只有解析过后，才能将数据变为对象，放置到我们刚刚创建你的实体类当中，便于数据的传递使用
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TestRequest request = retrofit.create(TestRequest.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("qq", "1213715120");

        Call<QqInfoResponse> dataCall = request.postResult(map);

        dataCall.enqueue(new Callback<QqInfoResponse>() {
            @Override
            public void onResponse(Call<QqInfoResponse> call, Response<QqInfoResponse> response) {
                logD("下载成功" + response.body().getData().getEmail());
            }

            @Override
            public void onFailure(Call<QqInfoResponse> call, Throwable t) {
                logD("下载失败" + t.getMessage());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.more_activity_retrofit;
    }


}