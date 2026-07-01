package com.example.only_tool.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.main.HomeFragment
import com.example.mine.MineFragment
import com.example.more.MoreFragment
import com.example.mystery.MysteryFragment
import com.example.only_tool.R
import com.example.only_tool.databinding.ActivityNewMainBinding
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener

class NewMainActivity : BaseActivity<ActivityNewMainBinding, BaseViewModel>() {

    // Tab实体封装类
    inner class TabEntity(
        private val title: String,
        private val iconNormal: Int,   // 未选中图标
        private val iconSelect: Int    // 选中图标
    ) : CustomTabEntity {

        // 获取Tab文字
        override fun getTabTitle(): String = title

        // 获取未选中状态图标
        override fun getTabUnselectedIcon(): Int = iconNormal

        // 获取选中状态图标
        override fun getTabSelectedIcon(): Int = iconSelect
    }

    private lateinit var tabList: ArrayList<CustomTabEntity>
    private lateinit var fragmentList: ArrayList<Fragment>

    override fun initData(savedInstanceState: Bundle?) {
        initUI()
        initTabData()
        initViewPager()
        bindTabWithViewPager()
    }

    fun initUI(){

    }

    // 初始化Tab文字、图标
    private fun initTabData() {
        tabList = arrayListOf(
            TabEntity("主页", R.drawable.tab_icon_main_un_selected, R.drawable.tab_icon_main_selected),
            TabEntity("神秘", R.drawable.tab_icon_mystery_un_selected, R.drawable.tab_icon_mystery_selected),
            TabEntity("更多", R.drawable.tab_icon_more_un_selected, R.drawable.tab_icon_more_selected),
            TabEntity("个人", R.drawable.tab_icon_mine_un_selected, R.drawable.tab_icon_mine_selected)
        )

        fragmentList = arrayListOf(
            HomeFragment(),
            MysteryFragment(),
            MoreFragment(),
            MineFragment()
        )
    }

    // 绑定ViewPager2适配器
    private fun initViewPager() {
        val adapter = ViewPagerAdapter(this, fragmentList)
        binding.viewPager2.adapter = adapter
        // 禁止左右滑动（可选，不需要滑动就打开）
        // viewPager2.isUserInputEnabled = false
    }

    // Tab与ViewPager联动
    private fun bindTabWithViewPager() {
        binding.tabLayout.setTabData(tabList)

        // 点击Tab切换页面
        binding.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                binding.viewPager2.currentItem = position
            }
            override fun onTabReselect(position: Int) {}
        })

        // 滑动页面同步Tab选中状态
        binding.viewPager2.registerOnPageChangeCallback(object : androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.currentTab = position
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_new_main
    }

}