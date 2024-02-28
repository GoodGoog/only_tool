package com.example.more.okHttp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.example.common.base.BaseActivity;
import com.example.common.base.BaseViewModel;
import com.example.more.R;
import com.example.more.databinding.MoreActivityOkHttpBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpActivity extends BaseActivity<MoreActivityOkHttpBinding, BaseViewModel> {

    private String TAG = "OkHttpActivity";

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        getFRequest();
        //postRequest();
    }

    /**
     * 同步Get请求
     */
    private void getFRequest(){
        String url = "https://wwww.baidu.com";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        new Thread(() -> {
            try {
                Response response = call.execute();
                logD(response.body().toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Post请求
     * @return
     */
    private void postRequest(){
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "I am Jdqm.";
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " +response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
                Log.d(TAG, "onResponse: " + response.body().string());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.more_activity_ok_http;
    }
}