package com.example.more.accessibility

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.more.EventBusTag
import com.example.more.leisu.LeisuServiceDispatch
import com.example.more.leisu.data.app_packageName_lei_su
import com.jeremyliao.liveeventbus.LiveEventBus

class AllAppService : FastAccessibilityService(), LifecycleOwner {
    companion object {
        private const val TAG = "AllAppService"

    }

    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    //是否监听APP的标记，不重写默认不监听
    override val enableListenApp = true

    override fun analyzeCallBack(wrapper: EventWrapper?, result: AnalyzeSourceResult) {
        wrapper?.let { it ->
            //当前所在app
            when (it.packageName) {
                app_packageName_lei_su -> {
                    LeisuServiceDispatch.instance().refresh(it,result)
                }

                else -> {}
            }
        }
    }


    override fun onServiceConnected() {
        super.onServiceConnected()
        LiveEventBus.get<Boolean>(EventBusTag.ACCESSIBILITY_SERVICE_START_OR_DESTROY).post(true)
        Log.d(TAG, "onServiceConnected: ")
        // 服务连接成功，标记为 ON_START
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    override fun onDestroy() {
        LiveEventBus.get<Boolean>(EventBusTag.ACCESSIBILITY_SERVICE_START_OR_DESTROY).post(false)
        Log.d(TAG, "onDestroy: ")
        // 服务销毁，标记为 DESTROYED
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

}