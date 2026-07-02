package com.example.more.leisu.handle

import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.click
import com.example.more.accessibility.delayClick
import com.example.more.accessibility.findNodeById
import com.example.more.leisu.data.IDPostDoubleSingle
import com.example.more.leisu.filterNumberOrZero
import com.example.more.leisu.getRandomInt

/**
 * 收费单关发布,传来的result为根节点解析出的节点集合
 */
class PostPaySingleBusiness(val eventWrapper: EventWrapper, val result: AnalyzeSourceResult) {
    var remainPostCount = 0

    companion object {
        const val TAG = "PostPaySingleBusiness"
        const val PLAY_TYPE_HANDICAP = "预测-让球"
        const val PLAY_TYPE_TOTAL_SCORE = "预测-总进球"

        enum class PlayType {
            //让球
            HANDICAP,

            //总分大小
            TOTAL_SCORE,

            //总分 | 让球 都有
            BOTH_HANDICAP_AND_TOTAL_SCORE
        }
    }

    init {
        remainPostCount =
            result.findNodeById(IDPostDoubleSingle.Companion.id_single_post_today_remains_times)?.text.filterNumberOrZero()
    }

    fun execute() {
        if (remainPostCount == 0) {
            //无剩余发布次数
            return
        }
        when (eventWrapper.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //解析rv子视图
                result.findNodeById(IDPostDoubleSingle.Companion.id_single_post_player_detail_action).analyzeRecyclerView()
                    ?.let { results ->
                        if (results.isNotEmpty()) {
                            //默认执行第一种玩法
                            onAnalysePlayType(results[0])
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

    //解析选中的玩法
    fun onAnalysePlayType(cResult: AnalyzeSourceResult) {
        val itemTitle =
            cResult.findNodeById(IDPostDoubleSingle.Companion.id_single_post_prospect_item_title)?.text.blankOrThis()
        when (itemTitle) {
            PLAY_TYPE_HANDICAP -> {
                //让分玩法
                executeHandicapPlayType(cResult)
            }

            PLAY_TYPE_TOTAL_SCORE -> {
                executeTotalScorePlayType(cResult)
            }

            else -> {}
        }
        //点击提交
        result.findNodeById(IDPostDoubleSingle.Companion.id_single_post_submit_button).delayClick()

    }

    /**
     * 让分玩法
     */
    fun executeHandicapPlayType(cResult: AnalyzeSourceResult){
        //随机选择胜利
        if (getRandomInt() % 2 == 0){
            cResult.findNodeById(IDPostDoubleSingle.Companion.id_single_post_prospect_left_layout_container).click()
        }else{
            cResult.findNodeById(IDPostDoubleSingle.Companion.id_single_post_prospect_right_layout_container).click()
        }
    }

    /**
     * 总分预测玩法
     */
    fun executeTotalScorePlayType(cResult: AnalyzeSourceResult){
        //随机选择胜利
        if (getRandomInt() % 2 == 0){
            cResult.findNodeById(IDPostDoubleSingle.Companion.id_single_post_prospect_left_layout_container).click()
        }else{
            cResult.findNodeById(IDPostDoubleSingle.Companion.id_single_post_prospect_right_layout_container).click()
        }
    }


}