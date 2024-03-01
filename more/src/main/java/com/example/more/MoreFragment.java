package com.example.more;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.example.common.base.BaseFragment;
import com.example.more.adapter.MoreAdapter;
import com.example.more.bean.MoreBean;
import com.example.more.databinding.FragmentMoreBinding;
import com.example.more.glide.GlideActivity;
import com.example.more.okHttp.OkHttpActivity;
import com.example.more.retrofit.RetrofitActivity;
import com.example.more.rxjava.RxJavaActivity;

import java.util.ArrayList;

public class MoreFragment extends BaseFragment<FragmentMoreBinding, MoreViewModel> {


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        ArrayList<MoreBean> beans = new ArrayList<>();
        //组件化与路由测试入口
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "Retrofit测试", RetrofitActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "OkHttp测试", OkHttpActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "Glide测试", GlideActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "RxJava测试", RxJavaActivity.class));

        //binding.moreRv.setBackgroundColor(Color.parseColor("#665544"));
        MoreAdapter adapter = new MoreAdapter(beans);
        binding.moreRv.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this.getContext(), 2, RecyclerView.VERTICAL, false);
        binding.moreRv.setLayoutManager(manager);
    }

    private MoreBean createMoreBean(int resIcon, String name, Class<?> tClass) {
        return new MoreBean(resIcon, name, v -> startActivity(tClass));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }
}