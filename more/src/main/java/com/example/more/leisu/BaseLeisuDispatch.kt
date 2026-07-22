package com.example.more.leisu

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.data.PostConfigData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.Int
import kotlin.collections.Set

abstract class BaseLeisuDispatch : LifecycleOwner {

    companion object {
        data class TimeSeparator(
            val filterEventSet: Set<Int>? = null,
            val minSeparatorTime: Long = 500L
        )
    }

    private val lifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    private var isReceiptable = true  //是否允许接受event数据，用来规定至少时隔多久才能接受一次数据

    //无障碍服务连接
    var isServiceConnect = false

    /**
     * 外部调用
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun eventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {
        getCurNeedReceptTimeSeparator().apply {
            if (filterEventSet == null) {
                //无需过滤
                onEventCome(eventWrapper, result)
            } else {
                if (filterEventSet.contains(eventWrapper.eventType)) {
                    //需要延时执行
                    //每五百ms只接收一次 目前只关注窗口状态改变
                    if (!isReceiptable) {
                        //不可接收
                        return
                    } else {
                        //可接收
                        onEventCome(eventWrapper, result)
                        isReceiptable = false
                    }
                    GlobalScope.launch(Dispatchers.IO) {
                        delay(minSeparatorTime)
                        isReceiptable = true
                    }
                }
            }
        }
    }

    protected abstract fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult)

    /**
     * 供外部调用和
     */
    fun start() {
        // 服务连接成功，标记为 ON_START
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        isServiceConnect = true
        onStart()
    }

    /**
     * 供子类继承
     */
    protected abstract fun onStart()

    /**
     * 供外部调用
     */
    fun destroy() {
        // 服务销毁，标记为 DESTROYED
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        isServiceConnect = false
        onDestroy()
    }

    protected abstract fun onDestroy()

    /**
     * 是否需要间隔一定时间才接受事件
     */
    protected open fun getCurNeedReceptTimeSeparator(): TimeSeparator {
        return TimeSeparator(
            filterEventSet = null,
            minSeparatorTime = 500L
        )
    }

}