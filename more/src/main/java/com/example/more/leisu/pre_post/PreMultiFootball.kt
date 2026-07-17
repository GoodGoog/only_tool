package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.getCurPrePageMatchList
import com.example.more.leisu.isCurItemTypeTimeFlags

class PreMultiFootball private constructor() : BaseLeisuDispatch() {
    companion object {

        private var instance: PreMultiFootball? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreMultiFootball {
            if (instance == null) {
                instance = PreMultiFootball()
            }
            return instance!!
        }

        const val TAG = "PreMultiFootball"
    }

    /**
     * 来这里的只有
     */
    fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {

        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                startAutoPost(result)
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {

            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

            }

            else -> {

            }
        }

    }

    fun startAutoPost(result: AnalyzeSourceResult) {
        if (!PreDataCenter.instance()
                .getCurPrePageAllowAutoPost(PostConfigData.ConfigType.MultiFootball)
        ) return
        Log.d(TAG, "onWindowStatusChange: ++++++++++++++++++++++++++++++++++++++++++++++++++++++==")
        getCurPrePageMatchList(result, PostConfigData.ConfigType.MultiFootball) { itemResults ->
            itemResults.forEach { itemResult ->
                Log.d(TAG, "onWindowStatusChange: -----" + itemResult.nodes)
                Log.d(
                    TAG,
                    "onWindowStatusChange: -------____________________________________________________"
                )
                if (itemResult.isCurItemTypeTimeFlags()) {
                } else {
                }
            }
        }
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

}