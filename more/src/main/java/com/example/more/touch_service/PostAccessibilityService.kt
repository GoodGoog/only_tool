package com.example.more.touch_service

import android.util.Log
import com.example.more.logD
import com.example.more.showToast

class PostAccessibilityService : FastAccessibilityService() {
    companion object {
        private const val TAG = "PostAccessibilityService"
    }

    override val enableListenApp = true

    override fun analyzeCallBack(wrapper: EventWrapper?, result: AnalyzeSourceResult) {
        result.findNodeByText("搜索").click()
        result.findAllTextNode(true).nodes.forEach { Log.d(TAG, "$wrapper | $it ") }
        logD("analyzeCallBack" + "事件过来")
    }

}