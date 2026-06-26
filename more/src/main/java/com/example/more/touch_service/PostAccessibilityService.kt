package com.example.more.touch_service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.logD
import com.example.more.showToast

class PostAccessibilityService : FastAccessibilityService() {
    companion object {
        private const val TAG = "PostAccessibilityService"

    }

    //是否监听APP的标记，不重写默认不监听
    override val enableListenApp = true

    override fun analyzeCallBack(wrapper: EventWrapper?, result: AnalyzeSourceResult) {
//        result.findNodeByText("搜索").click()
//        result.findAllTextNode(true).nodes.forEach { Log.d(TAG, "$wrapper | $it ") }
//        logD("analyzeCallBack" + "事件过来")
//        result.findNodeByText("我的").click()
        wrapper?.let { mWrapper ->
            //当前所在app
            when (mWrapper.packageName) {
                app_packageName_lei_su -> {
                    currentOnLeiSuApp(mWrapper, result)
                }

                else -> {}
            }
        }
    }


    /**
     * 当前在雷速app
     */
    fun currentOnLeiSuApp(mWrapper: EventWrapper, result: AnalyzeSourceResult) {
        Log.d(TAG, "currentOnLeiSuApp: 当前信息" + mWrapper)
        when (mWrapper.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //窗口状态改变了
                //Log.d(TAG, "currentOnLeiSuApp: 窗口状态改变了一次")
                //解析rv子视图
                result.findNodeById(id_post_player_detail_action).analyzeRecyclerView()
                    ?.forEachIndexed { index, mResult ->
                        val itemTitle = mResult.findNodeById(id_post_prospect_item_title)?.text
                        //Log.d(TAG, "currentOnLeiSuApp: 找到了：预测-让球----" + itemTitle)
                        when (itemTitle) {
                            "预测-让球" -> {
//                                mResult.findNodeById(id_post_prospect_left_layout_container).click()
                            }
                            "预测-总进球" -> {
                                //mResult.findNodeById(id_post_prospect_right_layout_container).click()
                            }
                            else -> {}
                        }
//                        result.nodes.forEach { node ->
//                            Log.d(TAG, "currentOnLeiSuApp: $index |||||| $node")
//                        }
                    }
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                //点击事件
                //result.findNodeById(id_post_submit_button).click()
            }

            else -> {}
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onInterrupt() {
        super.onInterrupt()
        Log.d(TAG, "onInterrupt: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

}