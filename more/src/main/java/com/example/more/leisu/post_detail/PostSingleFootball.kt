package com.example.more.leisu.post_detail

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.IDPostDoubleSingle
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.delayClickWithShowHighLight
import com.example.more.leisu.filterNumberOrZero
import com.example.more.leisu.getRandomInt
import com.example.more.leisu.transAccessibilityEventToString

class PostSingleFootball private constructor() : BaseLeisuDispatch() {

    companion object {

        const val PLAY_TYPE_HANDICAP = "预测-让球"
        const val PLAY_TYPE_TOTAL_SCORE = "预测-总进球"

        private var instance: PostSingleFootball? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PostSingleFootball {
            if (instance == null) {
                instance = PostSingleFootball()
            }
            return instance!!
        }

        const val TAG = "PostFreeSingleFootball"
    }

    fun onTaskDispatch(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        Log.d(TAG, "onTaskDispatch: --------------------" + wrapper.eventType.transAccessibilityEventToString())
        if (!PreDataCenter.instance()
                .isCurPrePageAllowAutoPost(PostConfigData.ConfigType.SingleFootball)
        ) return
        when (wrapper.event.eventType) {
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
        Log.d(TAG, "startAutoPost: -----------------------")
        //解析rv子视图
        result.findNodeById(IDPostDoubleSingle.id_single_post_player_detail_action)
            .analyzeRecyclerView()
            .let { results ->
                if (results.isNotEmpty()) {
                    //默认执行第一种玩法
                    analysePlayType(result, results[0])
                }
            }
    }

    //解析选中的玩法
    fun analysePlayType(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult) {
        Log.d(TAG, "analysePlayType: --------------------")
        val itemTitle =
            itemResult.findNodeById(IDPostDoubleSingle.id_single_post_prospect_item_title)?.text.blankOrThis()
        val playNodeWrapper = when (itemTitle) {
            PLAY_TYPE_HANDICAP -> {
                //让分玩法
                getHandicapPlayTypeNodeWrapper(itemResult)
            }

            PLAY_TYPE_TOTAL_SCORE -> {
                getTotalScorePlayTypeNodeWrapper(itemResult)
            }

            else -> {
                null
            }
        }
        //点击玩法
        playNodeWrapper.delayClickWithShowHighLight(gestureClick = false) { isSuccess ->
            Log.d(TAG, "analysePlayType: playType --- isSuccess" + isSuccess)
            Log.d(TAG, "analysePlayType: node ===" + playNodeWrapper)
            if (isSuccess) {
                //点击提交
//                rootResult.findNodeById(IDPostDoubleSingle.id_single_post_submit_button).apply {
//                    Log.d(TAG, "analysePlayType: submit" + this)
//                    delayClickWithShowHighLight {
//                        if (it) {
//                            PreDataCenter.instance()
//                                .postOneTime(PostConfigData.ConfigType.SingleFootball,getCurRemainCount(rootResult))
//                        }
//                    }
//                }
            }
        }
    }


    /**
     * 让分玩法
     */
    fun getHandicapPlayTypeNodeWrapper(itemResult: AnalyzeSourceResult): NodeWrapper? {
        //随机选择胜利
        return if (getRandomInt() % 2 == 0) {
            itemResult.findNodeById(IDPostDoubleSingle.id_single_post_prospect_left_layout_container)
        } else {
            itemResult.findNodeById(IDPostDoubleSingle.id_single_post_prospect_right_layout_container)
        }
    }

    /**
     * 总分大小预测玩法
     */
    fun getTotalScorePlayTypeNodeWrapper(itemResult: AnalyzeSourceResult): NodeWrapper? {
        //随机选择胜利
        return if (getRandomInt() % 2 == 0) {
            itemResult.findNodeById(IDPostDoubleSingle.id_single_post_prospect_left_layout_container)
        } else {
            itemResult.findNodeById(IDPostDoubleSingle.id_single_post_prospect_right_layout_container)
        }
    }


    fun getCurRemainCount(result: AnalyzeSourceResult) =
        result.findNodeById(IDPostDoubleSingle.id_single_post_today_remains_times)?.text.filterNumberOrZero()

    fun getCurIsFree() {

    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }



}