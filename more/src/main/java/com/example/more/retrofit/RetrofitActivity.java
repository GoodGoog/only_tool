package com.example.more.retrofit;

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

        HashMap<String, String> params = NetworkParams.createQqInfoRequestParams("1213715120");
        viewModel.request(NetworkBaseUrl.QQ_NUMBER_INFO_URL, params, QqInfoResponse.class, new NetworkCallBack<QqInfoResponse>() {
            @Override
            public void onSuccess(QqInfoResponse qqInfoResponse) {
                logD("success+++++" + qqInfoResponse.getData().getAvatar());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.more_activity_retrofit;
    }


}