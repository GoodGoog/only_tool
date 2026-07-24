package com.example.more.leisu.post_detail

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.data.PreMultiFootballSelectedLeague
import com.example.more.leisu.pre_post.PreMultiFootball
import com.example.more.leisu.transAccessibilityEventToString
import com.example.more.leisu.transToMultiFootballSpfAnalyseAiQuestion

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
        PreMultiFootball.instance().selectedItemArray.let {
            it.forEach { league ->
                val question = league.transToMultiFootballSpfAnalyseAiQuestion()
                Log.d(TAG, "printCurSelectedArray: item == $question")
            }
        }
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

}

