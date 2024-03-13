package com.example.more;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.base.BaseFragment;
import com.example.more.adapter.MoreAdapter;
import com.example.more.bean.MoreBean;
import com.example.more.component.JumpAppServiceActivity;
import com.example.more.customView.CustomActivity;
import com.example.more.databinding.FragmentMoreBinding;
import com.example.more.interestingApi.ChatGptActivity;
import com.example.more.jetpack.room.RoomActivity;
import com.example.more.kotlin.BaseKotlinActivity;
import com.example.more.third.glide.GlideActivity;
import com.example.more.third.okHttp.OkHttpActivity;
import com.example.more.third.retrofit.RetrofitActivity;
import com.example.more.third.rxjava.RxJavaActivity;
import com.example.more.thread.ThreadActivity;

import java.util.ArrayList;

public class MoreFragment extends BaseFragment<FragmentMoreBinding, MoreViewModel> {


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        ArrayList<MoreBean> beans = new ArrayList<>();
        //测试入口
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "Retrofit", RetrofitActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "OkHttp", OkHttpActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "Glide", GlideActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "RxJava", RxJavaActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "kotlin", BaseKotlinActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "chatGpt", ChatGptActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "service", JumpAppServiceActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "自定义视图", CustomActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "thread", ThreadActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "Room", RoomActivity.class));


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