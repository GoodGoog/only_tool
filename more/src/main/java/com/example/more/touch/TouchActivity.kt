package com.example.more.touch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.common.util.EventBusInfo
import com.example.more.R
import com.example.more.databinding.MoreActivityTouchBinding
import com.example.more.setting.TEAM_FLOAT_WINDOW_TRAM_MATCH_INFO
import com.example.more.showToast
import com.example.more.team.FloatingWindowService
import com.example.more.touch_service.isAccessibilityEnable
import com.example.more.touch_service.requireAccessibility
import com.example.more.touch_service.startApp
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
        binding.tvStartAccess.setOnClickListener {
            //initWindow()
            startApp("com.tencent.mm", "com.tencent.mm.ui.LauncherUI", "未安装微信")
        }

        initListener()
    }

    fun initListener(){
        LiveEventBus.get<Any?>(EventBusInfo.FLOAT_WINDOW_TEST_TOUCH)
            .observe(this){
//                Intent(this, AutoAccessibilityService::class.java).let { mIntent ->
//                    startService(mIntent)
//                    //bindService(mIntent,serviceConn, Context.BIND_AUTO_CREATE)
//                }
//                showToast("!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            }
    }

    fun initWindow(){
        //是否有系统悬浮窗显示权限
        if (Settings.canDrawOverlays(this)) {
            Intent(this, TouchWindowService::class.java).let { mIntent ->
                startService(mIntent)
            }
        } else {
            // 没权限
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 100)
        }
    }

    override fun getLayoutId() = R.layout.more_activity_touch
}