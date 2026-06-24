package com.example.more.touch

import android.util.Log
import com.example.more.touch_service.AnalyzeSourceResult
import com.example.more.touch_service.EventWrapper
import com.example.more.touch_service.FastAccessibilityService
import com.example.more.touch_service.click
import com.example.more.touch_service.findAllTextNode
import com.example.more.touch_service.findNodeByText
//
//class MyAccessibilityService : FastAccessibilityService() {
//    companion object {
//        private const val TAG = "CpFastAccessibility"
//    }
//
//    override val enableListenApp = true
//
//    override fun analyzeCallBack(wrapper: EventWrapper?, result: AnalyzeSourceResult) {
//        result.findNodeByText("搜索").click()
//        result.findAllTextNode(true).nodes.forEach { Log.e(TAG, "$wrapper | $it ") }
//    }
//
//}