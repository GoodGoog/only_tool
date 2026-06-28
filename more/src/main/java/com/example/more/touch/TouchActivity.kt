package com.example.more.touch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.common.util.EventBusInfo
import com.example.more.R
import com.example.more.databinding.MoreActivityTouchBinding
import com.example.more.accessibility.isAccessibilityEnable
import com.example.more.accessibility.requireAccessibility
import com.example.more.accessibility.startApp

import com.jeremyliao.liveeventbus.LiveEventBus


class TouchActivity: BaseActivity<MoreActivityTouchBinding, BaseViewModel>() {
    override fun initData(savedInstanceState: Bundle?) {
//        try {
//            //启动AccessibilityService：
//            startActivity(  Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
//        }
//        catch (e: Exception){
//            startActivity(  Intent(Settings.ACTION_SETTINGS))
//        }

        binding.tvIsEnable.setOnClickListener {
            if (isAccessibilityEnable) showToast("无障碍服务已开启")
            else requireAccessibility()
        }
        binding.tvShowAccessWindow.setOnClickListener {

        }
        binding.tvStartLeisu.setOnClickListener {
            //initWindow()
            //startApp("com.tencent.mm", "com.tencent.mm.ui.LauncherUI", "未安装微信")
            startApp("com.leisu.sports", "com.leisu.sports.ui.main.MainActivity", "未安装雷速")
        }

        initListener()
    }

    fun initListener(){
        LiveEventBus.get<Any?>(EventBusInfo.FLOAT_WINDOW_TEST_TOUCH)
            .observe(this){

            }
    }

    fun initWindow(){

    }

    override fun getLayoutId() = R.layout.more_activity_touch
}