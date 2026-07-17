package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.findNodesById
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.PreJumpUtils
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.delayClickAndShowHighLight
import com.example.more.leisu.delayClickWithShowHighLight
import com.example.more.leisu.getCurPrePageMatchList

class PreSingleBasketball private constructor() : BaseLeisuDispatch() {

    companion object {

        private var instance: PreSingleBasketball? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreSingleBasketball {
            if (instance == null) {
                instance = PreSingleBasketball()
            }
            return instance!!
        }

        const val TAG = "PreSingleBasketball"
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

    fun startAutoPost(result: AnalyzeSourceResult){
        if (!PreDataCenter.instance()
                .getCurPrePageAllowAutoPost(PostConfigData.ConfigType.SingleBasketball)
        ) {
            return
        }
        Log.d(TAG, "startAutoPost: ++++++++++++++++++++++++++++++++++++++++++++++++++++++==")
        getCurPrePageMatchList(result, PostConfigData.ConfigType.SingleBasketball) { itemResults ->
            itemResults.forEach { itemResult ->
                Log.d(TAG, "startAutoPost: parent ==" +  itemResult.parentNode)
                Log.d(TAG, "startAutoPost: -----" + itemResult.nodes)
                Log.d(
                    TAG,
                    "startAutoPost: -------____________________________________________________"
                )
            }
            //默认点击第一个控件
            if (itemResults.isNotEmpty()){
                itemResults[0].parentNode?.let {
                    Log.d(TAG, "startAutoPost: clickNode =" + it)
                    it.bounds?.let { clickRect ->
                        val aimRect = PreJumpUtils.instance().getCurItemRect(clickRect)
                        aimRect.delayClickWithShowHighLight {
                            Log.d(TAG, "startAutoPost: clickResult" + it)
                        }
                    }

                }
            }
        }
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }


}