package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.PreJumpUtils
import com.example.more.leisu.data.IDPrePostSingleBall
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.delayClickWithShowHighLight
import com.example.more.leisu.getCurPrePageMatchList
import com.example.more.leisu.isPostTimeLegal

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
                .isCurPrePageAllowAutoPost(PostConfigData.ConfigType.SingleBasketball)
        ) {
            return
        }
        Log.d(TAG, "startAutoPost: ++++++++++++++++++++++++++++++++++++++++++++++++++++++==")
        getCurPrePageMatchList(result, PostConfigData.ConfigType.SingleBasketball) { itemResults ->
            //默认点击第一个 时间合法的控件
            run {
                itemResults.forEach { itemResult ->
                    val startTime =
                        itemResult.findNodeById(IDPrePostSingleBall.id_single_start_time)?.text.blankOrThis()
                    if (isPostTimeLegal(startTime)) {
                        //时间不冲突，可以发布此Item
                        itemResult.parentNode?.let {
                            Log.d(TAG, "startAutoPost: clickNode =" + it)
                            it.bounds?.let { clickRect ->
                                val aimRect = PreJumpUtils.instance().getCurItemRect(clickRect)
                                aimRect.delayClickWithShowHighLight {
                                    Log.d(TAG, "startAutoPost: clickResult" + it)
                                }
                            }
                        }
                        //点了第一个有效的Item就走
                        //return@forEach // 仅跳过当前这一次循环，下一个继续
                        return@run //退出整个run
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