package com.example.more.leisu.post_detail

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.data.PreMultiFootballSelectedLeague
import com.example.more.leisu.numberTransToChinese
import com.example.more.leisu.pre_post.PreMultiFootball
import com.example.more.leisu.transAccessibilityEventToString
import com.example.more.leisu.transToMultiFootballSpfAnalyseAiQuestion
import com.jeremyliao.liveeventbus.LiveEventBus

class PostMultiFootball private constructor() : BaseLeisuDispatch() {

    companion object {
        private var instance: PostMultiFootball? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PostMultiFootball {
            if (instance == null) {
                instance = PostMultiFootball()
            }
            return instance!!
        }

        const val TAG = "PostMultiFootball"
    }

    val curType = PostConfigData.ConfigType.MultiFootball

    override fun onEventCome(
        eventWrapper: EventWrapper,
        result: AnalyzeSourceResult
    ) {
        Log.d(TAG, "onEventCome: result ==" + result.nodes)
//        if (!PreDataCenter.instance()
//                .isCurPrePageAllowAutoPost(curType)
//        ) return
        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                loadAiQuestion()
            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                //发生点击事件了，
            }

            else -> {

            }
        }
    }

    fun loadAiQuestion() {
        var totalQuestion: String = ""
        PreMultiFootball.instance().selectedItemArray.let {
            it.forEachIndexed { index, league ->
                totalQuestion += (index + 1).numberTransToChinese() + "、" + league.transToMultiFootballSpfAnalyseAiQuestion() + "\n"
            }
            totalQuestion += (it.size + 1).numberTransToChinese() + "、" + "为这篇${it.size}串1文章生成一个能够体现连红与信心，并且不带确定性结果的标题，控制在15字以内。\n" +
                    (it.size + 2).numberTransToChinese() + "、" + "再给这段文章写一份60字以内的前瞻，要体现连红概率大，并且期待大家解锁购买这篇文章。"
            LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(totalQuestion)
        }
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

}

