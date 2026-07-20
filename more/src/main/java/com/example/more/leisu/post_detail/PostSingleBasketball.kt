package com.example.more.leisu.post_detail

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.IDPostBasketballSingle
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PostSingleBasketBallHandicapTypeData
import com.example.more.leisu.data.PostSingleBasketBallTotalScoreTypeData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.delayClickWithShowHighLight
import com.example.more.leisu.filterNumberOrZero
import com.example.more.leisu.getRandomInt
import com.example.more.leisu.getTextById
import com.example.more.leisu.transAccessibilityEventToString
import com.example.more.leisu.transToSingleHandicapAnalyseAiQuestion
import com.example.more.leisu.transToSingleTotalScoreAnalyseAiQuestion

class PostSingleBasketball private constructor() : BaseLeisuDispatch() {

    companion object {
        const val PLAY_TYPE_HANDICAP = "预测-让分"
        const val PLAY_TYPE_TOTAL_SCORE = "预测-总分"

        private var instance: PostSingleBasketball? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PostSingleBasketball {
            if (instance == null) {
                instance = PostSingleBasketball()
            }
            return instance!!
        }

        const val TAG = "PostFreeBasketball"
    }

    val curType = PostConfigData.ConfigType.SingleBasketball

    override fun onEventCome(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        Log.d(
            TAG,
            "onTaskDispatch: --------------------" + wrapper.eventType.transAccessibilityEventToString()
        )
        Log.d(TAG, "onTaskDispatch: result ==" + result.nodes)
        if (!PreDataCenter.instance()
                .isCurPrePageAllowAutoPost(curType)
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
        result.findNodeById(IDPostBasketballSingle.id_single_post_player_detail_action)
            .analyzeRecyclerView()
            .let { itemResults ->
                itemResults.forEach {
                    Log.d(TAG, "startAutoPost: ||||||||||||||||||||||||" + it)
                    Log.d(TAG, "startAutoPost: ------------------------------------------------")
                }
                if (itemResults.isNotEmpty()) {
                    //默认执行第一种玩法
                    if (getCurIsFreePost()) {
                        doFreePost(result, itemResults[0])
                    } else {
                        doNotFreePost(result, itemResults[1])
                    }
                }
            }
    }

    //收费
    fun doNotFreePost(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult) {
        val itemTitle =
            itemResult.findNodeById(IDPostBasketballSingle.id_single_post_prospect_item_title)?.text.blankOrThis()
        when (itemTitle) {
            PLAY_TYPE_HANDICAP -> {
                //让分玩法
                doHandicapType(rootResult,itemResult)
            }

            PLAY_TYPE_TOTAL_SCORE -> {
                //预判总分大小
                doTotalScoreType(rootResult,itemResult)
            }

            else -> {

            }
        }
    }

    //让分-收费  左侧队伍为 客队， 右侧队伍为 主队
    private fun doHandicapType(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult){
        val data = PostSingleBasketBallHandicapTypeData(
            leagueName = rootResult.getTextById(IDPostBasketballSingle.id_single_league_name),
            leagueStartTime = rootResult.getTextById(IDPostBasketballSingle.id_single_post_league_start_time),
            leftTeamName = rootResult.getTextById(IDPostBasketballSingle.id_single_post_left_team_name),
            rightTeamName = rootResult.getTextById(IDPostBasketballSingle.id_single_post_right_team_name),

            leftPlate = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_left_plate),
            leftValue = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_left_win_value),
            rightPlate = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_right_plate),
            rightValue = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_right_win_value),
        ).apply {
            val it = transToSingleHandicapAnalyseAiQuestion(this)
            Log.d(TAG, "doChargeHandicap: ++++++++++++++++++++ " + it)
        }
    }

    //总分-收费
    private fun doTotalScoreType(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult){
        val data = PostSingleBasketBallTotalScoreTypeData(
            leagueName = rootResult.getTextById(IDPostBasketballSingle.id_single_league_name),
            leagueStartTime = rootResult.getTextById(IDPostBasketballSingle.id_single_post_league_start_time),
            leftTeamName = rootResult.getTextById(IDPostBasketballSingle.id_single_post_left_team_name),
            rightTeamName = rootResult.getTextById(IDPostBasketballSingle.id_single_post_right_team_name),

            biggerThanTotalValue = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_left_win_value),
            totalScore = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_center_total_score),
            smallerThanTotalValue = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_right_win_value),
        ).apply {
            val it = transToSingleTotalScoreAnalyseAiQuestion(this)
            Log.d(TAG, "doTotalScoreType: ++++++++++++++++++++ " + it)
        }
    }

    //免费
    fun doFreePost(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult) {
        Log.d(TAG, "analysePlayType: --------------------")
        val itemTitle =
            itemResult.findNodeById(IDPostBasketballSingle.id_single_post_prospect_item_title)?.text.blankOrThis()
        val playNodeWrapper = when (itemTitle) {
            PLAY_TYPE_HANDICAP -> {
                //让分玩法
                //随机选择胜利
                if (getRandomInt() % 2 == 0) {
                    itemResult.findNodeById(IDPostBasketballSingle.id_single_post_prospect_left_layout_container)
                } else {
                    itemResult.findNodeById(IDPostBasketballSingle.id_single_post_prospect_right_layout_container)
                }
            }

            PLAY_TYPE_TOTAL_SCORE -> {
                if (getRandomInt() % 2 == 0) {
                    itemResult.findNodeById(IDPostBasketballSingle.id_single_post_prospect_left_layout_container)
                } else {
                    itemResult.findNodeById(IDPostBasketballSingle.id_single_post_prospect_right_layout_container)
                }
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
//                                .postOneTime(PostConfigData.ConfigType.SingleBasketball,getCurRemainCount(rootResult))
//                        }
//                    }
//                }
            }
        }
    }

    fun getCurRemainCount(result: AnalyzeSourceResult) =
        result.findNodeById(IDPostBasketballSingle.id_single_post_today_remains_times)?.text.filterNumberOrZero()

    fun getCurIsFreePost(): Boolean = false
        //PreDataCenter.instance().postArray[curType.transToPostArrayIndex()].isFree

    override fun onStart() {

    }

    override fun onDestroy() {

    }

    /***
     * 设置窗口状态变化接受间隔
     */
    override fun getCurNeedReceptTimeSeparator(): BaseLeisuDispatch.Companion.TimeSeparator {
        return BaseLeisuDispatch.Companion.TimeSeparator(setOf(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED),500L)
    }

}