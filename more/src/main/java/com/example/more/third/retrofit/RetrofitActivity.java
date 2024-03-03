package com.example.more.third.retrofit;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.common.base.BaseActivity;
import com.example.common.base.BaseViewModel;
import com.example.common.network.NetworkCallBack;
import com.example.common.network.NetworkParams;
import com.example.common.network.NetworkBaseUrl;
import com.example.more.R;
import com.example.more.databinding.MoreActivityRetrofitBinding;

import java.util.HashMap;

public class RetrofitActivity extends BaseActivity<MoreActivityRetrofitBinding, BaseViewModel> {

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

        //requestQqInfo();
        requestHotSearch();
    }

    void requestQqInfo(){
        HashMap<String, String> params = NetworkParams.createQqInfoRequestParams("1213715120");
        viewModel.request(NetworkBaseUrl.QQ_NUMBER_INFO_URL, params, QqInfoResponse.class, new NetworkCallBack<QqInfoResponse>() {
            @Override
            public void onSuccess(QqInfoResponse qqInfoResponse) {
                logD("success+++++" + qqInfoResponse.getData().getAvatar());
            }
        });
    }

    void requestHotSearch(){
        HashMap<String, String> params = NetworkParams.createHotSearchParams("百度");
        viewModel.request(NetworkBaseUrl.HOT_SEARCH_URL, params, HotSearchResponse.class, new NetworkCallBack<HotSearchResponse>() {
            @Override
            public void onSuccess(HotSearchResponse response) {
                logD("success+++++" + response.getCode());
                logD("success+++++" + response.getData().size());
                logD("success+++++" + response.getTitle());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.more_activity_retrofit;
    }


}