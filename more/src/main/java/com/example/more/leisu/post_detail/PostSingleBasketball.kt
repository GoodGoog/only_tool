package com.example.more.leisu.post_detail

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.transNodeInfoToNodeWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.PreJumpUtils
import com.example.more.leisu.data.IDPostBasketballSingle
import com.example.more.leisu.data.IDPostFootballSingle
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.delayClickWithShowHighLight
import com.example.more.leisu.getNumberTextByIdAndFilterOther
import com.example.more.leisu.getRandomInt
import com.example.more.leisu.getTextById
import com.example.more.leisu.isTwoNodeSame
import com.example.more.leisu.transToPostArrayIndex
import com.example.more.leisu.transToSingleBasketballModeRaceTotalAiQuestion
import com.example.more.leisu.transToSingleBasketballRaceModeHandicapAiQuestion
import com.jeremyliao.liveeventbus.LiveEventBus

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

        const val TAG = "PostSingleBasketball"
    }

    val curType = PostConfigData.ConfigType.SingleBasketball

    init {
        LiveEventBus.get<String>(EventBusTag.POST_CHARGE_ANSWER_FROM_AI).observe(this) {
            //不是当前页面 或 无障碍服务连接已断开
            if (PreJumpUtils.instance().curPageType != curType || !isServiceConnect) return@observe
            //拿到了Ai返回的答案
        }
    }

    override fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {
        Log.d(TAG, "onEventCome: ---" + result.nodes)
        Log.d(TAG, "onEventCome: !!!!!!!!!!!!!!!!!!!!!!!!")
        //if (!PreDataCenter.instance().isCurPrePageAllowAutoPost(curType)) return
        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //如果没有发布次数就不干了
//                if (getCurRemainCount(result) > 0) {
//                    startAutoPost(result)
//                }
                //startAutoPost(result)
            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                val node: AccessibilityNodeInfo? = eventWrapper.event.source
                node ?: return
                val clickedNodeWrapper = node.transNodeInfoToNodeWrapper()
                Log.d(TAG, "onEventCome: 被点击的东西 = $clickedNodeWrapper")
                try {
                    //点击的是预测-左/右 按钮
                    //含[客胜/主胜] + [大/小]
                    setOf<String>(
                        IDPostBasketballSingle.id_single_post_prospect_left_layout_container,
                        IDPostBasketballSingle.id_single_post_prospect_right_layout_container
                    ).let {
                        //无效点击不响应
                        if (it.contains(clickedNodeWrapper.id)) {
                            //将接受点击
                            doProspectMode(result, clickedNodeWrapper)
                        }
                    }


                    //点击的是竟篮-不让分-胜负
                    setOf<String>(
                        IDPostBasketballSingle.id_race_spf_left_value,
                        IDPostBasketballSingle.id_race_spf_right_value
                    ).let {
                        //无效点击不响应
                        if (it.contains(clickedNodeWrapper.id)) {
                            //接受点击
                            doRaceModeSpf(result, clickedNodeWrapper)
                        }
                    }

                    //点击的是竟篮-让分-胜负
                    setOf<String>(
                        IDPostBasketballSingle.id_race_rf_left_click_area,
                        IDPostBasketballSingle.id_race_rf_right_click_area
                    ).let {
                        //无效点击不响应
                        if (it.contains(clickedNodeWrapper.id)) {
                            //接受点击
                            doRaceModeRf(result, clickedNodeWrapper)
                        }
                    }

                    //点击的是竟篮-总分-大小
                    setOf<String>(
                        IDPostBasketballSingle.id_race_total_left,
                        IDPostBasketballSingle.id_race_total_right
                    ).let {
                        //无效点击不响应
                        if (it.contains(clickedNodeWrapper.id)) {
                            //接受点击
                            doRaceModeTotal(result, clickedNodeWrapper)
                        }
                    }


                } finally {
                    // 【强制】必须回收，否则内存泄漏、系统杀服务
                    node.recycle()
                }
            }

            else -> {

            }
        }
    }

    /**
     * 预测模式 -- 手动选择玩法后生成aiQuestion
     */
    fun doProspectMode(result: AnalyzeSourceResult, clickNodeWrapper: NodeWrapper) {
        //解析rv子视图
        val itemResults =
            result.findNodeById(IDPostBasketballSingle.id_single_post_player_detail_action)
                .analyzeRecyclerView()

        var position = -1
        run {
            itemResults.forEachIndexed { index, itemResult ->
                itemResult.nodes.forEach { subNode ->
                    if (isTwoNodeSame(clickNodeWrapper, subNode, isCompareBounds = true)) {
                        position = index
                        return@run
                    }
                }
            }
        }

        if (position == -1) {
            //无效点击快走开
            return
        }

        itemResults[position].apply {
            when (getTextById(IDPostBasketballSingle.id_single_post_prospect_item_title)) {
                PLAY_TYPE_HANDICAP -> {
                    //让分玩法
                    doProspectModeRf(result, this, clickNodeWrapper)
                }

                PLAY_TYPE_TOTAL_SCORE -> {
                    //预判总分大小
                    doProspectModeTotal(result, this, clickNodeWrapper)
                }
            }
        }
    }

    //让分-收费  左侧队伍为 客队， 右侧队伍为 主队
    private fun doProspectModeRf(
        rootResult: AnalyzeSourceResult,
        itemResult: AnalyzeSourceResult,
        clickNodeWrapper: NodeWrapper
    ) {

        val leagueName = rootResult.getTextById(IDPostBasketballSingle.id_single_league_name)
        val leagueStartTime =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_league_start_time)
        val leftTeamName =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_left_team_name)
        val rightTeamName =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_right_team_name)

        val leftValue = rootResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_left_win_value)
        val leftPlate = rootResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_left_plate)

        val rightValue = rootResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_right_win_value)
        val rightPlate = rootResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_right_plate)

        val isLeftClicked = clickNodeWrapper.id == IDPostBasketballSingle.id_single_post_prospect_left_layout_container

        val resultStr = transToSingleBasketballRaceModeHandicapAiQuestion(
            leagueName,
            leagueStartTime,
            leftTeamName,
            rightTeamName,
            isLeftClicked,
            rightPlate,
            leftValue,
            rightValue
        )
        LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(resultStr)
    }

    //总分-收费
    private fun doProspectModeTotal(
        rootResult: AnalyzeSourceResult,
        itemResult: AnalyzeSourceResult,
        clickNodeWrapper: NodeWrapper
    ) {

        val leagueName = rootResult.getTextById(IDPostBasketballSingle.id_single_league_name)
        val leagueStartTime =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_league_start_time)
        val leftTeamName =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_left_team_name)
        val rightTeamName =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_right_team_name)

        val leftValue =
            itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_left_win_value)
        val totalScore =
            itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_center_total_score)
        val rightValue =
            itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_right_win_value)

        val isLeftClicked =
            clickNodeWrapper.id == IDPostFootballSingle.id_single_post_prospect_left_layout_container

        val resultStr = transToSingleBasketballModeRaceTotalAiQuestion(
            leagueName,
            leagueStartTime,
            leftTeamName,
            rightTeamName,
            isLeftClicked,
            totalScore,
            leftValue,
            rightValue
        )
        LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(resultStr)
    }


    /**
     * 竟足模式 | 共有四三个玩个
     * 1.胜负-不让分
     * 2.胜负-让分
     * 3.总分比大小
     * 4.胜分差
     */
    fun doRaceModeSpf(rootResult: AnalyzeSourceResult, clickNodeWrapper: NodeWrapper) {
        val leagueName = rootResult.getTextById(IDPostBasketballSingle.id_single_league_name)
        val leagueStartTime =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_league_start_time)
        val leftTeamName =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_left_team_name)
        val rightTeamName =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_right_team_name)

        val leftValue = rootResult.getTextById(IDPostBasketballSingle.id_race_spf_left_value)
        val rightValue = rootResult.getTextById(IDPostBasketballSingle.id_race_spf_right_value)

        val isLeftClicked = clickNodeWrapper.id == IDPostBasketballSingle.id_race_spf_left_value

        val resultStr = transToSingleBasketballRaceModeHandicapAiQuestion(
            leagueName,
            leagueStartTime,
            leftTeamName,
            rightTeamName,
            isLeftClicked,
            "0",  //不让分
            leftValue,
            rightValue
        )
        LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(resultStr)
    }

    /**
     * 竟足模式 | 共有四三个玩个
     * 1.胜负-让分
     */
    fun doRaceModeRf(rootResult: AnalyzeSourceResult, clickNodeWrapper: NodeWrapper) {
        val leagueName = rootResult.getTextById(IDPostBasketballSingle.id_single_league_name)
        val leagueStartTime =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_league_start_time)
        val leftTeamName =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_left_team_name)
        val rightTeamName =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_right_team_name)

        val leftValue = rootResult.getTextById(IDPostBasketballSingle.id_race_rf_left_value)
        val leftPlate = rootResult.getTextById(IDPostBasketballSingle.id_race_rf_left_plate)

        val rightValue = rootResult.getTextById(IDPostBasketballSingle.id_race_rf_right_value)
        val rightPlate = rootResult.getTextById(IDPostBasketballSingle.id_race_rf_right_plate)

        val isLeftClicked = clickNodeWrapper.id == IDPostBasketballSingle.id_race_rf_left_click_area

        val resultStr = transToSingleBasketballRaceModeHandicapAiQuestion(
            leagueName,
            leagueStartTime,
            leftTeamName,
            rightTeamName,
            isLeftClicked,
            rightPlate,
            leftValue,
            rightValue
        )
        LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(resultStr)
    }


    /**
     * 竟足模式 | 共有四三个玩个
     * 1.总分比大小
     */
    fun doRaceModeTotal(rootResult: AnalyzeSourceResult, clickNodeWrapper: NodeWrapper) {
        val leagueName = rootResult.getTextById(IDPostBasketballSingle.id_single_league_name)
        val leagueStartTime =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_league_start_time)
        val leftTeamName =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_left_team_name)
        val rightTeamName =
            rootResult.getTextById(IDPostBasketballSingle.id_single_post_right_team_name)

        val totalScore = rootResult.getTextById(IDPostBasketballSingle.id_race_total_center_score)

        val leftValue = rootResult.getTextById(IDPostBasketballSingle.id_race_total_left)
        val rightValue = rootResult.getTextById(IDPostBasketballSingle.id_race_total_right)

        val isLeftClicked = clickNodeWrapper.id == IDPostBasketballSingle.id_race_total_left

        val resultStr = transToSingleBasketballModeRaceTotalAiQuestion(
            leagueName,
            leagueStartTime,
            leftTeamName,
            rightTeamName,
            isLeftClicked,
            totalScore,  //不让分
            leftValue,
            rightValue
        )
        LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(resultStr)
    }


    //免费
    fun doFreePost(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult) {
        val playNodeWrapperID =
            when (itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_item_title)) {
                PLAY_TYPE_HANDICAP -> {
                    //让分玩法
                    //随机选择胜利
                    if (getRandomInt() % 2 == 0) {
                        IDPostBasketballSingle.id_single_post_prospect_left_layout_container
                    } else {
                        IDPostBasketballSingle.id_single_post_prospect_right_layout_container
                    }
                }

                PLAY_TYPE_TOTAL_SCORE -> {
                    if (getRandomInt() % 2 == 0) {
                        IDPostBasketballSingle.id_single_post_prospect_left_layout_container
                    } else {
                        IDPostBasketballSingle.id_single_post_prospect_right_layout_container
                    }
                }

                else -> {
                    ""
                }
            }
        //点击玩法
        itemResult.findNodeById(playNodeWrapperID)
            .delayClickWithShowHighLight(gestureClick = false) { isSuccess ->
                if (isSuccess) {
                    //点击提交
                    rootResult.findNodeById(IDPostBasketballSingle.id_single_post_submit_button)
//                    .delayClickWithShowHighLight {
//
//                    }
                }
            }
    }

    fun isCurFreePost(): Boolean =
        PreDataCenter.instance().postArray[curType.transToPostArrayIndex()].isFree

    fun getCurRemainCount(result: AnalyzeSourceResult) =
        result.getNumberTextByIdAndFilterOther(IDPostBasketballSingle.id_single_post_today_remains_times)
            .toInt()

    override fun onStart() {

    }

    override fun onDestroy() {

    }

    /***
     * 设置窗口状态变化接受间隔
     */
//    override fun getCurNeedReceptTimeSeparator(): BaseLeisuDispatch.Companion.TimeSeparator {
//        return BaseLeisuDispatch.Companion.TimeSeparator(
//            setOf(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED),
//            500L
//        )
//    }

}