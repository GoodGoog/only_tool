package com.example.more.leisu.post_detail

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.transNodeInfoToNodeWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.PreJumpUtils
import com.example.more.leisu.containsChineseWinOrLose
import com.example.more.leisu.data.IDPostFootballSingle
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PostSingleFootBallHandicapTypeData
import com.example.more.leisu.data.PostSingleFootBallTotalScoreTypeData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.delayClickWithShowHighLight
import com.example.more.leisu.getNumberTextByIdAndFilterOther
import com.example.more.leisu.getRandomInt
import com.example.more.leisu.getTextById
import com.example.more.leisu.isClickNodeInCurLeagueList
import com.example.more.leisu.isTwoNodeSame
import com.example.more.leisu.transAccessibilityEventToString
import com.example.more.leisu.transToPostArrayIndex
import com.example.more.leisu.transToSingleFootballHandicapAnalyseAiQuestion
import com.example.more.leisu.transToSingleFootballRaceAiQuestion
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

        const val TAG = "PostSingleFootball"
    }

    val curPageType = PostConfigData.ConfigType.SingleFootball

    //发布竟足时所选中的结果组合
    val selectRaceTexts = ArrayList<String>()

    //当前的竟足让分状况
    var curRaceHandicap = ""

    init {
//        LiveEventBus.get<String>(EventBusTag.POST_CHARGE_ANSWER_FROM_AI).observe(this) {
//            //不是当前页面 或 无障碍服务连接已断开
//            if (PreJumpUtils.instance().curPageType != curType || !isServiceConnect) return@observe
//            //拿到了Ai返回的答案
//        }
    }

    override fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {
        Log.d(
            TAG,
            "onEventCome: curtype！！！ = " + eventWrapper.eventType.transAccessibilityEventToString()
        )
        //Log.d(TAG, "onEventCome: result = " + result.nodes)
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

                try {
                    val clickNodeWrapper = node.transNodeInfoToNodeWrapper()

                    if (clickNodeWrapper.id == IDPostFootballSingle.id_single_post_prospect_button) {
                        //切换预测模式,
                        selectRaceTexts.clear()
                        return
                    }

                    //点击了预测模式中的结果
                    setOf<String>(
                        IDPostFootballSingle.id_single_post_prospect_left_layout_container,
                        IDPostFootballSingle.id_single_post_prospect_right_layout_container
                    ).let {
                        //无效点击不响应
                        if (it.contains(clickNodeWrapper.id)) {
                            doProspectMode(result, clickNodeWrapper)
                            return
                        }
                    }

                    //点击了竟足模式中的结果
                    setOf<String>(
                        IDPostFootballSingle.id_tv_spf_win_value,
                        IDPostFootballSingle.id_tv_spf_flat_value,
                        IDPostFootballSingle.id_tv_spf_lose_value,
                        IDPostFootballSingle.id_tv_rq_win_value,
                        IDPostFootballSingle.id_tv_rq_flat_value,
                        IDPostFootballSingle.id_tv_rq_lose_value
                    ).let {
                        //无效点击不响应
                        if (it.contains(clickNodeWrapper.id)) {
                            doRaceMode(result, clickNodeWrapper)
                            return
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
     * 手动选择玩法后生成aiQuestion
     */
    fun doProspectMode(result: AnalyzeSourceResult, clickNodeWrapper: NodeWrapper) {
        //解析rv子视图
        val itemResults =
            result.findNodeById(IDPostFootballSingle.id_single_post_player_detail_action)
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
            when (getTextById(IDPostFootballSingle.id_single_post_prospect_item_title)) {
                PLAY_TYPE_HANDICAP -> {
                    //让分玩法
                    doHandicapType(result, this, clickNodeWrapper)
                }

                PLAY_TYPE_TOTAL_SCORE -> {
                    //预判总分大小
                    doTotalScoreType(result, this, clickNodeWrapper)
                }
            }
        }
    }

    //让分-收费  左侧队伍为 主队， 右侧队伍为 客队
    private fun doHandicapType(
        rootResult: AnalyzeSourceResult,
        itemResult: AnalyzeSourceResult,
        clickNodeWrapper: NodeWrapper
    ) {
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
            val it = transToSingleFootballHandicapAnalyseAiQuestion(this, clickNodeWrapper)
            Log.d(TAG, "doHandicapType: -----$it")
            //传递向AI发送的问题
            LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(it)
        }
    }

    //总分-收费 左侧队伍为 主队， 右侧队伍为 客队
    private fun doTotalScoreType(
        rootResult: AnalyzeSourceResult,
        itemResult: AnalyzeSourceResult,
        clickNodeWrapper: NodeWrapper
    ) {
        val data = PostSingleFootBallTotalScoreTypeData(
            leagueName = rootResult.getTextById(IDPostFootballSingle.id_single_league_name),
            leagueStartTime = rootResult.getTextById(IDPostFootballSingle.id_single_post_league_start_time),
            leftTeamName = rootResult.getTextById(IDPostFootballSingle.id_single_post_left_team_name),
            rightTeamName = rootResult.getTextById(IDPostFootballSingle.id_single_post_right_team_name),

            biggerThanTotalValue = itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_left_win_value),
            totalScore = itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_center_total_score),
            smallerThanTotalValue = itemResult.getTextById(IDPostFootballSingle.id_single_post_prospect_right_win_value),
        ).apply {
            val it = transToSingleFootballTotalScoreAnalyseAiQuestion(this, clickNodeWrapper)
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
        itemResult.findNodeById(playNodeWrapperID)
            .delayClickWithShowHighLight(gestureClick = false) { isSuccess ->
                if (isSuccess) {
                    //点击提交
                    rootResult.findNodeById(IDPostFootballSingle.id_single_post_submit_button)
//                    .delayClickWithShowHighLight {
//
//                    }
                }
            }
    }


    /**
     * 竟足模式
     */
    fun doRaceMode(result: AnalyzeSourceResult, clickNodeWrapper: NodeWrapper) {
        val leagueName = result.getTextById(IDPostFootballSingle.id_single_league_name)
        val leftTeamName = result.getTextById(IDPostFootballSingle.id_single_post_left_team_name)
        val rightTeamName = result.getTextById(IDPostFootballSingle.id_single_post_right_team_name)
        var handicapText = "+1"
        var winText = "胜 0"
        var flatText = "平 0"
        var loseText = "负 0"

        if (clickNodeWrapper.id.blankOrThis().contains("spf")) {
            //不让球
            handicapText = result.getTextById(IDPostFootballSingle.id_tv_spf)
            winText = result.getTextById(IDPostFootballSingle.id_tv_spf_win_value)
            flatText = result.getTextById(IDPostFootballSingle.id_tv_spf_flat_value)
            loseText = result.getTextById(IDPostFootballSingle.id_tv_spf_lose_value)
        }

        if (clickNodeWrapper.id.blankOrThis().contains("rq")) {
            //让球
            handicapText = result.getTextById(IDPostFootballSingle.id_tv_rq)
            winText = result.getTextById(IDPostFootballSingle.id_tv_rq_win_value)
            flatText = result.getTextById(IDPostFootballSingle.id_tv_rq_flat_value)
            loseText = result.getTextById(IDPostFootballSingle.id_tv_rq_lose_value)
        }

        //更新最新点击的选中选中信息,最多只能同时选中两个
        val curClickText = clickNodeWrapper.text.blankOrThis()
        selectRaceTexts.apply {
            if (curRaceHandicap == handicapText) {
                //当前点击的结果让分状态，和之前的让分是一样的
                if (contains(curClickText)) {
                    //新点击的按钮已被选中
                    val position = indexOf(curClickText)
                    removeAt(position)
                } else {
                    //不能同时选中胜和败，否则会清理已选中内容内容
                    //1.如果平在已选中数组中排第一，则整个已选中数组都要被清空。
                    //2.如果平在已选中数组中不排第一，则移除其他内容，保留平并且排第一。
                    //3.走完1.2.操作，再插入新选中的内容
                    var isNeedClear = false
                    filter { it.containsChineseWinOrLose() }.let {
                        if (it.isNotEmpty() && curClickText.containsChineseWinOrLose())
                            isNeedClear =
                                true
                    }
                    if (isNeedClear) {
                        if (!this[0].containsChineseWinOrLose()){
                            //平排在第一
                            clear()
                        }else{
                            //没有平或者平不在第一位
                            removeAt(0)
                        }
                        add(curClickText)
                    } else {
                        if (size <= 1) {
                            add(curClickText)
                        } else {
                            //移走第一次被选中的
                            removeAt(0)
                            add(curClickText)
                        }
                    }
                }
            } else {
                curRaceHandicap = handicapText
                clear()
                add(curClickText)
            }
        }

        val it = transToSingleFootballRaceAiQuestion(
            leagueName,
            leftTeamName,
            rightTeamName,
            handicapText,
            winText,
            flatText,
            loseText,
            selectRaceTexts
        )
        //传递向AI发送的问题
        LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(it)
    }

    fun getCurRemainCount(result: AnalyzeSourceResult) =
        result.getNumberTextByIdAndFilterOther(IDPostFootballSingle.id_single_post_today_remains_times)
            .toInt()

    fun isCurFreePost(): Boolean =
        PreDataCenter.instance().postArray[curPageType.transToPostArrayIndex()].isFree

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