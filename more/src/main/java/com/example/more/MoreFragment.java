package com.example.more;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.example.common.base.BaseFragment;
import com.example.more.adapter.MoreAdapter;
import com.example.more.bean.MoreBean;
import com.example.more.databinding.FragmentMoreBinding;

import java.util.ArrayList;

public class MoreFragment extends BaseFragment<FragmentMoreBinding,MoreViewModel> {


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        ArrayList<MoreBean> beans = new ArrayList<>();
        //组件化与路由测试入口
        beans.add(new MoreBean(R.drawable.more_icon_list_access, "Retrofit测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RetrofitActivity.class);
            }
        }));
        beans.add(new MoreBean(R.drawable.more_icon_list_access, "OkHttp测试", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(OkHttpActivity.class);
            }
        }));

        //binding.moreRv.setBackgroundColor(Color.parseColor("#665544"));
        MoreAdapter adapter = new MoreAdapter(beans);
        binding.moreRv.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(this.getContext(),2, RecyclerView.VERTICAL,false);
        binding.moreRv.setLayoutManager(manager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }
}