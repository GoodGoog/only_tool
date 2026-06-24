package com.example.only_tool.main

import android.app.Application
import android.view.accessibility.AccessibilityEvent
import com.example.more.touch_service.FastAccessibilityService
import com.example.more.touch_service.PostAccessibilityService
import com.jeremyliao.liveeventbus.LiveEventBus

class MyApp : Application() {
    companion object {
        lateinit var instance: Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        FastAccessibilityService.init(
            instance, PostAccessibilityService::class.java, arrayListOf(
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                AccessibilityEvent.TYPE_VIEW_CLICKED
            )
        )

        //配置
        LiveEventBus
            .config()
            .autoClear(true)
            .lifecycleObserverAlwaysActive(true)

    }
}