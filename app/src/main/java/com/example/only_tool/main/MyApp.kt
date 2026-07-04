package com.example.only_tool.main

import android.app.Application
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.FastAccessibilityService
import com.example.more.accessibility.AllAppService
import com.jeremyliao.liveeventbus.LiveEventBus

class MyApp : Application() {
    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        //左右需要监听的事件在这里设置就可以了,动态设置，也是最终实际监听的内容
        FastAccessibilityService.init(
            instance, AllAppService::class.java, arrayListOf(
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                AccessibilityEvent.TYPE_VIEW_CLICKED,
                AccessibilityEvent.TYPE_VIEW_SCROLLED
//                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
            )
        )

        //配置
        LiveEventBus
            .config()
            .autoClear(true)
            .lifecycleObserverAlwaysActive(true)

    }
}