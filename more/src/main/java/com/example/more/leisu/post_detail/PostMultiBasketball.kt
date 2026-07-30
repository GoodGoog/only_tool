package com.example.more.leisu.post_detail

import android.view.accessibility.AccessibilityEvent
import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.numberTransToChinese
import com.example.more.leisu.pre_post.PreMultiBasketball
import com.example.more.leisu.pre_post.PreMultiFootball
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
            it.forEachIndexed { index, league ->
                if (league.isHandicap) {
                    totalQuestion += (index + 1).numberTransToChinese() + "、" +league.transToMultiBasketballHandicapTypeAnalyseAiQuestion() + "\n"
                }else{
                    totalQuestion += (index + 1).numberTransToChinese() + "、" +league.transToMultiBasketballTotalScoreAnalyseAiQuestion() + "\n"
                }
            }
            totalQuestion += (it.size + 1).numberTransToChinese() + "、" +"为这篇${it.size}串1文章生成一个能够体现连红与信心，并且不带确定性结果的标题，控制在15字以内。\n" +
                    (it.size + 2).numberTransToChinese() + "、" +"再给这段文章写一份60字以内的前瞻，要体现连红概率大，并且期待大家解锁购买这篇文章。"
            LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(totalQuestion)
        }
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

}

