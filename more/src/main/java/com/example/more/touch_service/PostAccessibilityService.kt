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
        wrapper?.let { it ->
            //当前所在app
            when (it.packageName) {
                app_packageName_lei_su -> {
                    currentOnLeiSuApp(it, result)
                }

                else -> {}
            }
        }
    }


    /**
     * 当前在雷速app
     */
    fun currentOnLeiSuApp(mWrapper: EventWrapper, result: AnalyzeSourceResult) {
//        result.findAllTextNode(true).nodes.forEach {
//            Log.d(TAG, "$mWrapper | $it ")
//        }
        Log.d(TAG, "currentOnLeiSuApp: ----" + result.nodes)
        //Log.d(TAG, "currentOnLeiSuApp: ----" + rootInActiveWindow?.transNodeInfoToNodeWrapper())
        if (isInPostSinglePage(result)) {
            onSinglePostPage(mWrapper, result)
        }
        if (isInPostMultiPage(result)) {
            onMultiPostPage(mWrapper, result)
        }

    }

    /**
     * 在单关发布页
     */
    fun onSinglePostPage(mWrapper: EventWrapper, result: AnalyzeSourceResult) {
        when (mWrapper.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //解析rv子视图
                result.findNodeById(id_single_post_player_detail_action).analyzeRecyclerView()
                    ?.forEach { mResult ->
                        val itemTitle =
                            mResult.findNodeById(id_single_post_prospect_item_title)?.text.blankOrThis()
                        when (itemTitle) {
                            "预测-让球" -> {
//                                mResult.findNodeById(id_post_prospect_left_layout_container).click()
                            }

                            "预测-总进球" -> {
                                //mResult.findNodeById(id_post_prospect_right_layout_container).click()
                            }

                            else -> {}
                        }

                    }
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                //Log.d(TAG, "onSinglePostPage: 点击事件" + result)
                //点击事件
                //result.findNodeById(id_post_submit_button).click()
            }

            else -> {}
        }
    }

    /**
     * 在串关发布页
     */
    fun onMultiPostPage(mWrapper: EventWrapper, result: AnalyzeSourceResult) {

    }


    /**
     * 单串
     */
    fun isInPostSinglePage(result: AnalyzeSourceResult): Boolean {
        val remains = result.findNodeById(id_single_post_today_remains_times)?.text.blankOrThis()
        val analyzeEt = result.findNodeById(id_single_post_analyse_edit)
        return remains.isNotEmpty() && analyzeEt != null
    }

    /**
     * 多串
     */
    fun isInPostMultiPage(result: AnalyzeSourceResult): Boolean {
        val remains = result.findNodeById(id_multi_post_today_remains_times)?.text.blankOrThis()
        val analyzeEt = result.findNodeById(id_single_post_analyse_edit)
        return remains.isNotEmpty() && analyzeEt == null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "onServiceConnected: ")
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