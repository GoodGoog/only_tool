package com.example.more.accessibility

import android.util.Log
import com.example.more.ACCESSIBILITY_SERVICE_START_OR_DESTROY
import com.example.more.leisu.LeisuServiceDispatch
import com.example.more.leisu.data.app_packageName_lei_su
import com.example.more.setting.EVENT_BUS_RETURN_FLOAT_WINDOW_RESULT
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.core.LiveEvent

class AllAppService : FastAccessibilityService() {
    companion object {
        private const val TAG = "AllAppService"

    }

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
        LiveEventBus.get<Boolean>(ACCESSIBILITY_SERVICE_START_OR_DESTROY).post(true)
        Log.d(TAG, "onServiceConnected: ")
    }

    override fun onDestroy() {
        LiveEventBus.get<Boolean>(ACCESSIBILITY_SERVICE_START_OR_DESTROY).post(false)
        Log.d(TAG, "onDestroy: ")
    }

}