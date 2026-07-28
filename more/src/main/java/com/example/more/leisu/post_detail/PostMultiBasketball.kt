package com.example.more.leisu.post_detail

import android.view.accessibility.AccessibilityEvent
import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.pre_post.PreMultiBasketball
import com.example.more.leisu.transToMultiBasketballHandicapTypeAnalyseAiQuestion
import com.example.more.leisu.transToMultiBasketballTotalScoreAnalyseAiQuestion
import com.jeremyliao.liveeventbus.LiveEventBus

class PostMultiBasketball private constructor() : BaseLeisuDispatch() {

    companion object {
        const val PLAY_TYPE_HANDICAP = "预测-让分"
        const val PLAY_TYPE_TOTAL_SCORE = "预测-总分"

        private var instance: PostMultiBasketball? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PostMultiBasketball {
            if (instance == null) {
                instance = PostMultiBasketball()
            }
            return instance!!
        }

        const val TAG = "PostMultiBasketball"
    }

    val curType = PostConfigData.ConfigType.MultiBasketball
    override fun onEventCome(
        eventWrapper: EventWrapper,
        result: AnalyzeSourceResult
    ) {
//        Log.d(
//            TAG,
//            "onTaskDispatch: --------------------" + eventWrapper.eventType.transAccessibilityEventToString()
//        )
//        Log.d(TAG, "onTaskDispatch: result ==" + result.nodes)
//        if (!PreDataCenter.instance()
//                .isCurPrePageAllowAutoPost(curType)
//        ) return
        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                loadAiQuestion()
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {

            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

            }

            else -> {

            }
        }
    }

    fun loadAiQuestion() {
        var totalQuestion : String = ""
        PreMultiBasketball.instance().selectedItemArray.let {
            it.forEach { league ->
                if (league.isHandicap) {
                    totalQuestion += league.transToMultiBasketballHandicapTypeAnalyseAiQuestion() + "\n"
                }else{
                    totalQuestion += league.transToMultiBasketballTotalScoreAnalyseAiQuestion() + "\n"
                }
            }
        }
        LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(totalQuestion)
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

}

