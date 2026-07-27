package com.example.more.leisu.pre_post

import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.findNodeById
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.PreJumpUtils
import com.example.more.leisu.data.IDPrePostSingleBall
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.delayClickWithShowAnotherHighLight
import com.example.more.leisu.getCurPrePageMatchList
import com.example.more.leisu.getTextById
import com.example.more.leisu.isLegalPostTime

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

    val curType = PostConfigData.ConfigType.SingleBasketball

    /**
     * 来这里的只有
     */
    override fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {
        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //关闭自动点击
                //startAutoPost(result)
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
        if (!PreDataCenter.instance().isCurPrePageAllowAutoPost(curType)) return
        val itemResults = getCurPrePageMatchList(result, curType)

        //默认点击第一个 时间合法的控件
        run {
            itemResults.forEach { itemResult ->
                val startTime = itemResult.getTextById(IDPrePostSingleBall.id_single_start_time)
                if (!isLegalPostTime(startTime)) return@forEach //只跳出此次循环 ,不退出整个循环
                //时间不冲突，可以发布此Item
                itemResult.parentNode?.let {
                    val clickRect = it.bounds ?: Rect(0, 0, 0, 0)
                    val highLightRect =
                        PreJumpUtils.instance().getCurItemRect(clickRect)
                    itemResult.findNodeById(IDPrePostSingleBall.id_single_league_title)
                        .delayClickWithShowAnotherHighLight(
                            highLightRect,
                            delayTime = 500L
                        ) {

                        }
                }
                //点了第一个有效的Item就走
                return@run //退出整个run
            }
        }
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }


}