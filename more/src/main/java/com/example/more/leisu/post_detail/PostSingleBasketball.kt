package com.example.more.leisu.post_detail

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
import com.example.more.leisu.data.PostSingleBasketBallHandicapTypeData
import com.example.more.leisu.data.PostSingleBasketBallTotalScoreTypeData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.delayClickWithShowHighLight
import com.example.more.leisu.getNumberTextByIdAndFilterOther
import com.example.more.leisu.getRandomInt
import com.example.more.leisu.getTextById
import com.example.more.leisu.isTwoNodeSame
import com.example.more.leisu.transToPostArrayIndex
import com.example.more.leisu.transToSingleBasketballHandicapAnalyseAiQuestion
import com.example.more.leisu.transToSingleBasketballTotalScoreAnalyseAiQuestion
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.collections.contains

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
                //startAutoPost(result)
            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                val node: AccessibilityNodeInfo? = eventWrapper.event.source
                node ?: return
                try {
                    //如果不是当前页面信息列表的节点被点击，就不关注
                    val validIds = setOf<String>(
                        IDPostBasketballSingle.id_single_post_prospect_left_layout_container,
                        IDPostBasketballSingle.id_single_post_prospect_right_layout_container
                    )
                    val nodeWrapper = node.transNodeInfoToNodeWrapper()
                    //无效点击不响应
                    if (!validIds.contains(nodeWrapper.id)) return
                    //将接受点击
                    doSomething(result, nodeWrapper)
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
    fun doSomething(result: AnalyzeSourceResult, clickNodeWrapper: NodeWrapper) {
        //解析rv子视图
        val itemResults =
            result.findNodeById(IDPostBasketballSingle.id_single_post_player_detail_action)
                .analyzeRecyclerView()

        var position = -1
        run {
            itemResults.forEachIndexed { index, itemResult ->
                itemResult.nodes.forEach { subNode ->
                    if (isTwoNodeSame(clickNodeWrapper, subNode,isCompareBounds = true)) {
                        position = index
                        return@run
                    }
                }
            }
        }

        if (position == -1){
            //无效点击快走开
            return
        }

        itemResults[position].apply {
            when (getTextById(IDPostBasketballSingle.id_single_post_prospect_item_title)) {
                PLAY_TYPE_HANDICAP -> {
                    //让分玩法
                    doHandicapType(result, this,clickNodeWrapper)
                }

                PLAY_TYPE_TOTAL_SCORE -> {
                    //预判总分大小
                    doTotalScoreType(result, this,clickNodeWrapper)
                }
            }
        }
    }

    //让分-收费  左侧队伍为 客队， 右侧队伍为 主队
    private fun doHandicapType(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult,clickNodeWrapper: NodeWrapper) {
        PostSingleBasketBallHandicapTypeData(
            leagueName = rootResult.getTextById(IDPostBasketballSingle.id_single_league_name),
            leagueStartTime = rootResult.getTextById(IDPostBasketballSingle.id_single_post_league_start_time),
            leftTeamName = rootResult.getTextById(IDPostBasketballSingle.id_single_post_left_team_name),
            rightTeamName = rootResult.getTextById(IDPostBasketballSingle.id_single_post_right_team_name),

            leftPlate = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_left_plate),
            leftValue = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_left_win_value),
            rightPlate = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_right_plate),
            rightValue = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_right_win_value),
        ).apply {
            val it = transToSingleBasketballHandicapAnalyseAiQuestion(this,clickNodeWrapper)
            LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(it)
        }
    }

    //总分-收费
    private fun doTotalScoreType(rootResult: AnalyzeSourceResult, itemResult: AnalyzeSourceResult,clickNodeWrapper: NodeWrapper) {
        PostSingleBasketBallTotalScoreTypeData(
            leagueName = rootResult.getTextById(IDPostBasketballSingle.id_single_league_name),
            leagueStartTime = rootResult.getTextById(IDPostBasketballSingle.id_single_post_league_start_time),
            leftTeamName = rootResult.getTextById(IDPostBasketballSingle.id_single_post_left_team_name),
            rightTeamName = rootResult.getTextById(IDPostBasketballSingle.id_single_post_right_team_name),

            biggerThanTotalValue = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_left_win_value),
            totalScore = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_center_total_score),
            smallerThanTotalValue = itemResult.getTextById(IDPostBasketballSingle.id_single_post_prospect_right_win_value),
        ).apply {
            val it = transToSingleBasketballTotalScoreAnalyseAiQuestion(this,clickNodeWrapper)
            LiveEventBus.get<String>(EventBusTag.POST_CHARGE_QUESTION_TO_AI).post(it)
        }
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
        itemResult.findNodeById(playNodeWrapperID).delayClickWithShowHighLight(gestureClick = false) { isSuccess ->
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