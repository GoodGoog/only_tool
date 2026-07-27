package com.example.more.leisu.post_detail

import android.view.accessibility.AccessibilityEvent
import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.findNodeById
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.PreJumpUtils
import com.example.more.leisu.data.IDPostFootballSingle
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PostSingleFootBallHandicapTypeData
import com.example.more.leisu.data.PostSingleFootBallTotalScoreTypeData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.delayClickWithShowHighLight
import com.example.more.leisu.getNumberTextByIdAndFilterOther
import com.example.more.leisu.getRandomInt
import com.example.more.leisu.getTextById
import com.example.more.leisu.transToPostArrayIndex
import com.example.more.leisu.transToSingleFootballHandicapAnalyseAiQuestion
import com.example.more.leisu.transToSingleFootballTotalScoreAnalyseAiQuestion
import com.jeremyliao.liveeventbus.LiveEventBus

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

    val curType = PostConfigData.ConfigType.SingleFootball

    init {
        LiveEventBus.get<String>(EventBusTag.POST_CHARGE_ANSWER_FROM_AI).observe(this) {
            //不是当前页面 或 无障碍服务连接已断开
            if (PreJumpUtils.instance().curPageType != curType || !isServiceConnect) return@observe
            //拿到了Ai返回的答案
        }
    }

    override fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {
        //if (!PreDataCenter.instance().isCurPrePageAllowAutoPost(curType)) return
        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //如果没有发布次数就不干了
//                if (getCurRemainCount(result) > 0) {
//                    startAutoPost(result)
//                }
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
        //解析rv子视图
        val itemResults =
            result.findNodeById(IDPostFootballSingle.id_single_post_player_detail_action)
                .analyzeRecyclerView()
        if (itemResults.isNotEmpty()) {
            //默认执行第一种玩法
            val firstItemResult = itemResults[0]
//            if (isCurFreePost()) {
//                doFreePost(result, firstItemResult)
//            } else {
//                //收费
//                when (firstItemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_item_title)) {
//                    PLAY_TYPE_HANDICAP -> {
//                        //让分玩法
//                        doHandicapType(result, firstItemResult)
//                    }
//
//                    PLAY_TYPE_TOTAL_SCORE -> {
//                        //预判总分大小
//                        doTotalScoreType(result, firstItemResult)
//                    }
//                }
//            }
            //默认当前不区分收费免费，一律生成ai提问
            when (firstItemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_item_title)) {
                PLAY_TYPE_HANDICAP -> {
                    //让分玩法
                    doHandicapType(result, firstItemResult)
                }

                PLAY_TYPE_TOTAL_SCORE -> {
                    //预判总分大小
                    doTotalScoreType(result, firstItemResult)
                }
            }
        }
    }

    //让分-收费  左侧队伍为 主队， 右侧队伍为 客队
    private fun doHandicapType(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult) {
        val data = PostSingleFootBallHandicapTypeData(
            leagueName = rootResult.getTextById(IDPostFootballSingle.id_single_league_name),
            leagueStartTime = rootResult.getTextById(IDPostFootballSingle.id_single_post_league_start_time),
            leftTeamName = rootResult.getTextById(IDPostFootballSingle.id_single_post_left_team_name),
            rightTeamName = rootResult.getTextById(IDPostFootballSingle.id_single_post_right_team_name),

            leftPlate = itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_left_plate),
            leftValue = itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_left_win_value),
            rightPlate = itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_right_plate),
            rightValue = itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_right_win_value),
        ).apply {
            val it = transToSingleFootballHandicapAnalyseAiQuestion(this)
            //传递向AI发送的问题
            LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(it)
        }
    }

    //总分-收费 左侧队伍为 主队， 右侧队伍为 客队
    private fun doTotalScoreType(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult) {
        val data = PostSingleFootBallTotalScoreTypeData(
            leagueName = rootResult.getTextById(IDPostFootballSingle.id_single_league_name),
            leagueStartTime = rootResult.getTextById(IDPostFootballSingle.id_single_post_league_start_time),
            leftTeamName = rootResult.getTextById(IDPostFootballSingle.id_single_post_left_team_name),
            rightTeamName = rootResult.getTextById(IDPostFootballSingle.id_single_post_right_team_name),

            biggerThanTotalValue = itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_left_win_value),
            totalScore = itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_center_total_score),
            smallerThanTotalValue = itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_right_win_value),
        ).apply {
            val it = transToSingleFootballTotalScoreAnalyseAiQuestion(this)
            LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(it)
        }
    }

    //免费
    fun doFreePost(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult) {
        val playNodeWrapperID =
            when (itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_item_title)) {
                PLAY_TYPE_HANDICAP -> {
                    //让分玩法
                    //随机选择胜利
                    if (getRandomInt() % 2 == 0) {
                        IDPostFootballSingle.id_single_post_prospect_left_layout_container
                    } else {
                        IDPostFootballSingle.id_single_post_prospect_right_layout_container
                    }
                }

                PLAY_TYPE_TOTAL_SCORE -> {
                    if (getRandomInt() % 2 == 0) {
                        IDPostFootballSingle.id_single_post_prospect_left_layout_container
                    } else {
                        IDPostFootballSingle.id_single_post_prospect_right_layout_container
                    }
                }

                else -> {
                    ""
                }
            }
        //点击玩法
        itemResult.findNodeById(playNodeWrapperID).delayClickWithShowHighLight(gestureClick = false) { isSuccess ->
            if (isSuccess) {
                //点击提交
                rootResult.findNodeById(IDPostFootballSingle.id_single_post_submit_button)
//                    .delayClickWithShowHighLight {
//
//                    }
            }
        }
    }

    fun getCurRemainCount(result: AnalyzeSourceResult) =
        result.getNumberTextByIdAndFilterOther(IDPostFootballSingle.id_single_post_today_remains_times)
            .toInt()

    fun isCurFreePost(): Boolean =
        PreDataCenter.instance().postArray[curType.transToPostArrayIndex()].isFree

    override fun onStart() {

    }

    override fun onDestroy() {

    }

    /***
     * 设置窗口状态变化接受间隔
     */
    override fun getCurNeedReceptTimeSeparator(): BaseLeisuDispatch.Companion.TimeSeparator {
        return BaseLeisuDispatch.Companion.TimeSeparator(
            setOf(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED),
            500L
        )
    }


}