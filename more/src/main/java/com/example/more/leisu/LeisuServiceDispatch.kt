package com.example.more.leisu

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.delayClick
import com.example.more.accessibility.findAllTextNode
import com.example.more.accessibility.findNodeByExpression
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.findNodeByText

class LeisuServiceDispatch {
    companion object {

        private var instance: LeisuServiceDispatch? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): LeisuServiceDispatch {
            if (instance == null) {
                instance = LeisuServiceDispatch()
            }
            return instance!!
        }

        const val TAG = "LeisuServiceDispatch"
    }

    var wrapper: EventWrapper? = null
    var result: AnalyzeSourceResult? = null

    fun refresh(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        this.wrapper = wrapper
        this.result = result
        when (wrapper.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                taskDispatch(result)
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {}
            else -> {}
        }
    }


    //业务分发
    fun taskDispatch(result: AnalyzeSourceResult) {
        Log.d(TAG, "currentOnLeiSuApp: ----" + result.nodes)
        if (isInPostSinglePage(result)) {
            //在单关发布页
            wrapper?.let {
                PostFreeSingleServiceHandle(it, result).execute()
            }
        }
        if (isInPostMultiPage(result)) {
            onMultiPostPage(result)
        }
        if (isInPrePostPage(result)) {
            onPrePostPage(result)
        }
    }

    /**
     * 在发布页的入口页,即比赛列表选择页
     */
    fun onPrePostPage(result: AnalyzeSourceResult) {
        Log.d(TAG, "onPrePostPage: ------------------")
        result.findNodeByExpression { nodeWrapper ->
            nodeWrapper.text == "篮球"
        }.delayClick()
        result.findNodeByText("单关").delayClick(2000)
    }

    /**
     * 在串关发布页
     */
    fun onMultiPostPage(result: AnalyzeSourceResult) {

    }

    /**
     * 在发布页的前一页
     */
    fun isInPrePostPage(result: AnalyzeSourceResult): Boolean {
        result.findNodeById(id_league_choose_info_filter)?.let {
            return true
        }
        return false
    }

    /**
     * 单关发布页
     */
    fun isInPostSinglePage(result: AnalyzeSourceResult): Boolean {
        val remains = result.findNodeById(id_single_post_today_remains_times)?.text.blankOrThis()
        val analyzeEt = result.findNodeById(id_single_post_analyse_edit)
        return remains.isNotEmpty() && analyzeEt != null
    }

    /**
     * 多关发布页
     */
    fun isInPostMultiPage(result: AnalyzeSourceResult): Boolean {
        val remains = result.findNodeById(id_multi_post_today_remains_times)?.text.blankOrThis()
        val analyzeEt = result.findNodeById(id_single_post_analyse_edit)
        return remains.isNotEmpty() && analyzeEt == null
    }

}