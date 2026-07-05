package com.example.more.leisu

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

abstract class BaseLifecycleOwner : LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    /**
     * 供外部调用和
     */
    open fun start() {
        // 服务连接成功，标记为 ON_START
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
        onStart()
    }

    /**
     * 供子类继承
     */
    abstract fun onStart()

    open fun destroy() {
        // 服务销毁，标记为 DESTROYED
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        onDestroy()
    }

    abstract fun onDestroy()

}