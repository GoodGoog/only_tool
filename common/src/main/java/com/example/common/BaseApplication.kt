package com.example.common

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //ARouter初始化配置
        ARouter.init(this)
        ARouter.openLog()
        ARouter.openDebug()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

}