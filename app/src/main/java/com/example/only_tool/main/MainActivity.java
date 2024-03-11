package com.example.only_tool.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.common.base.BaseActivity;
import com.example.main.HomeFragment;
import com.example.mine.MineFragment;
import com.example.more.MoreFragment;
import com.example.mystery.MysteryFragment;
import com.example.only_tool.R;
import com.example.only_tool.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainViewModel> implements View.OnClickListener {

    private ImageView currentIv;
    private TextView currentTV;

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //viewModel.getName().setValue("换个优雅的名字");
        initPager();
        initView();
    }

    private void initPager(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MysteryFragment());
        fragments.add(new MoreFragment());
        fragments.add(new MineFragment());
        MainFragmentAdapter mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(),getLifecycle(),fragments);
        binding.viewPagerContainer.setAdapter(mainFragmentAdapter);
        //fragment切换时改变底部导航栏的选中状态
        binding.viewPagerContainer.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pageChange(position);
            }
        });
    }

    private void initView(){
        binding.tabHome.setOnClickListener(this);
        binding.tabMystery.setOnClickListener(this);
        binding.tabMore.setOnClickListener(this);
        binding.tabMine.setOnClickListener(this);
        //默认选中Home页
        currentIv = binding.ivTabHome;
        currentTV = binding.tvTabHome;
        currentIv.setSelected(true);
        currentTV.setSelected(true);
    }


    private void pageChange(int position) {
        currentIv.setSelected(false);
        currentTV.setSelected(false);
        switch (position){
            case 0:
                binding.ivTabHome.setSelected(true);
                currentIv = binding.ivTabHome;
                binding.tvTabHome.setSelected(true);
                currentTV = binding.tvTabHome;
                break;
            case 1:
                binding.ivTabMystery.setSelected(true);
                currentIv = binding.ivTabMystery;
                binding.tvTabMystery.setSelected(true);
                currentTV = binding.tvTabMystery;
                break;
            case  2:
                binding.ivTabMore.setSelected(true);
                currentIv = binding.ivTabMore;
                binding.tvTabMore.setSelected(true);
                currentTV = binding.tvTabMore;
                break;
            case 3:
                binding.ivTabMine.setSelected(true);
                currentIv = binding.ivTabMine;
                binding.tvTabMine.setSelected(true);
                currentTV = binding.tvTabMine;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        //pageChage(v.getId());
        int vId = v.getId();
        int curTabNum = 0;
        if (vId == R.id.tab_home){
            //smoothScroll:为false是表示不平滑滚动
            binding.viewPagerContainer.setCurrentItem(0,false);
            curTabNum = 0;
        }else if (vId == R.id.tab_mystery){
            binding.viewPagerContainer.setCurrentItem(1,false);
            curTabNum = 1;
        }else if (vId == R.id.tab_more){
            binding.viewPagerContainer.setCurrentItem(2,false);
            curTabNum = 2;
        }else if (vId == R.id.tab_mine){
            binding.viewPagerContainer.setCurrentItem(3,false);
            curTabNum = 3;
        }else {

        }
        pageChange(curTabNum);

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

}