package com.example.more;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.base.BaseFragment;
import com.example.more.adapter.MoreAdapter;
import com.example.more.bean.MoreBean;
import com.example.more.databinding.FragmentMoreBinding;
import com.example.more.manyApi.ManyApiActivity;
import com.example.more.setting.SettingActivity;
import com.example.more.team.TeamActivity;
import com.example.more.touch.PostActivity;

import java.util.ArrayList;

public class MoreFragment extends BaseFragment<FragmentMoreBinding, MoreViewModel> {


    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        ArrayList<MoreBean> beans = new ArrayList<>();
        //测试入口
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "玩球", SettingActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "球员选择", TeamActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "许多api", ManyApiActivity.class));
        beans.add(createMoreBean(R.drawable.more_icon_list_access, "自动点击", PostActivity.class));

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