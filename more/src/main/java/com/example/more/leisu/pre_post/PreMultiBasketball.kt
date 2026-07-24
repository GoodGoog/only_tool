package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.transNodeInfoToNodeWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.IDPreMultiFootball
import com.example.more.leisu.data.IDPrePostMultiBasketBall
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.data.PreMultiBasketBallData
import com.example.more.leisu.data.PreMultiBasketBallSubData
import com.example.more.leisu.data.PreMultiBasketballSelectedLeague
import com.example.more.leisu.data.PreMultiFootballSelectedLeague
import com.example.more.leisu.getCurPrePageMatchList
import com.example.more.leisu.getNumberTextByIdAndFilterOther
import com.example.more.leisu.getTextById
import com.example.more.leisu.isClickNodeInCurLeagueList
import com.example.more.leisu.isContainsNodeWrapper

class PreMultiBasketball private constructor() : BaseLeisuDispatch() {
    companion object {

        private var instance: PreMultiBasketball? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreMultiBasketball {
            if (instance == null) {
                instance = PreMultiBasketball()
            }
            return instance!!
        }

        const val TAG = "PreMultiBasketball"
    }

    val curType = PostConfigData.ConfigType.MultiBasketball

    //被选中的item,以及其中的详细被选中玩法
    val selectedItemArray = ArrayList<PreMultiBasketballSelectedLeague>()

    /**
     * 来这里的只有
     */
    override
    fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {

        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //多关需要手动开启 次次都是
                //startAutoPost(result)
            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                val node: AccessibilityNodeInfo? = eventWrapper.event.source
                node ?: return
                try {
                    //如果不是当前页面信息列表的节点被点击，就不关注
                    if (!isClickNodeInCurLeagueList(
                            result,
                            curType,
                            node.transNodeInfoToNodeWrapper()
                        )
                    ) return
                    //将接受点击的Item 和 item中的响应点击节点信息记录
                    doSomething(node.transNodeInfoToNodeWrapper(), result)
                    printCurSelectedArray()
                } finally {
                    // 【强制】必须回收，否则内存泄漏、系统杀服务
                    node.recycle()
                }
            }

            else -> {

            }
        }
    }


    fun doSomething(clickedNodeWrapper: NodeWrapper, result: AnalyzeSourceResult) {
        //有效点击，只有点击到了 胜 平 负 这三个按钮，才算是有限点击，其他的统统无效
        setOf<String>(
            IDPrePostMultiBasketBall.id_handicap_win_value,
            IDPrePostMultiBasketBall.id_handicap_lose_value,
            IDPrePostMultiBasketBall.id_total_win_value,
            IDPrePostMultiBasketBall.id_total_lose_value,
        ).let {
            //不接受无效点击
            if (!it.contains(clickedNodeWrapper.id)) return
        }


        val itemResults = getCurPrePageMatchList(result, curType)

        var curClickInItemTag = ""
        var isClickHandicap = true
        //只在当前item首次被点击时才被需要
        var newSelectedLeague: PreMultiBasketballSelectedLeague? = null
        //第一列分数相关 让分数 / 总分数
        var scoreNodeWrapper: NodeWrapper? = null

        //找出被点击的节点对应的itemTag
        run {
            itemResults.forEach { itemResult ->
                if (itemResult.isContainsNodeWrapper(clickedNodeWrapper)) {
                    //被点击的节点在当前的item内
                    curClickInItemTag =
                        itemResult.findNodeById(IDPrePostMultiBasketBall.id_left_team_name)?.text?.blankOrThis() + "VS" +
                                itemResult.findNodeById(IDPrePostMultiBasketBall.id_right_team_name)?.text?.blankOrThis()

                    //存储一下数据，如果这个item是第一次被点击时会被用来存储进selectedItemArray
                    //根据节点id是否包含spf[不让球] 或者 rq[主队让/不让球],来判断是那种类型的玩法[让分或者不让分]
                    scoreNodeWrapper =
                        if (clickedNodeWrapper.id.blankOrThis().contains("rf")) {
                            //放分
                            isClickHandicap = true
                            itemResult.findNodeById(IDPrePostMultiBasketBall.id_handicap_score)
                        } else {
                            //
                            isClickHandicap = false
                            itemResult.findNodeById(IDPrePostMultiBasketBall.id_total_score)
                        }
                    scoreNodeWrapper?.let {
                        newSelectedLeague = PreMultiBasketballSelectedLeague(
                            itemTag = curClickInItemTag,
                            isHandicap = isClickHandicap,
                            scoreNodeWrapper = it,
                            selectedNode = clickedNodeWrapper
                        )
                    }
                    return@run
                }
            }
        }

        //记录点击的控件，在被选中数组的第几个位置
        var position = -1
        selectedItemArray.forEachIndexed { index, league ->
            if (league.itemTag == curClickInItemTag) {
                position = index
            }
        }

        if (position >= 0) {
            //刚好有一个满足条件,更新记载的数据，或这删除
            selectedItemArray[position].apply {
                scoreNodeWrapper?.let { scoreNodeWrapper ->
                    upDataClickNodeWrapper(
                        isClickHandicap,
                        scoreNodeWrapper,
                        clickedNodeWrapper
                    ).let { isNeedRemoveFormList ->
                        Log.d(TAG, "doSomething:isNeedRemoveFormList = $isNeedRemoveFormList ")
                        if (isNeedRemoveFormList) {
                            //需要从选中列表中移除
                            selectedItemArray.removeAt(position)
                        } else {
                            //只是更新选中列表中对应item的数据，不需要额外处理
                        }
                    }
                }
            }
        } else {
            //没有满足条件的，就将当前点击的加进去
            newSelectedLeague?.let { it ->
                selectedItemArray.add(it)
            }
        }

    }

    /**
     * 打印当前选中的数据
     */
    fun printCurSelectedArray() {
        selectedItemArray.forEachIndexed { index, league ->
            Log.d(TAG, "printCurSelectedArray: item == " + league)
        }
    }


    fun startAutoPost(result: AnalyzeSourceResult) {
        if (!PreDataCenter.instance()
                .isCurPrePageAllowAutoPost(curType)
        ) return
        getCurPrePageMatchList(result, curType) { itemResults ->
            itemResults.forEach { itemResult ->
                Log.d(TAG, "startAutoPost: itemResult = $itemResult")
                itemResult.analyzeItemResult()
            }
        }
    }

    fun AnalyzeSourceResult.analyzeItemResult() {
        val subDataArray = ArrayList<PreMultiBasketBallSubData>().apply {
            //主队让分/ 受让分玩法
            PreMultiBasketBallSubData(
                isHandicap = true,
                score = getNumberTextByIdAndFilterOther(IDPrePostMultiBasketBall.id_handicap_score),
                notOpenText = "", //getTextById(IDPrePostMultiBasketBall.id_handicap_win_value), //不明确是否会不开放此玩法
                winValue = getNumberTextByIdAndFilterOther(IDPrePostMultiBasketBall.id_handicap_win_value),


                loseValue = getNumberTextByIdAndFilterOther(IDPrePostMultiBasketBall.id_handicap_lose_value)
            ).let {
                //当前玩法开放
                if (it.notOpenText.isEmpty()) {
                    add(it)
                }
            }
            //总分比较大小玩法
            PreMultiBasketBallSubData(
                isHandicap = false,
                score = getNumberTextByIdAndFilterOther(IDPrePostMultiBasketBall.id_total_score),
                notOpenText = "", //getTextById(IDPrePostMultiBasketBall.id_handicap_win_value), //不明确是否会不开放此玩法
                winValue = getNumberTextByIdAndFilterOther(IDPrePostMultiBasketBall.id_total_win_value),
                loseValue = getNumberTextByIdAndFilterOther(IDPrePostMultiBasketBall.id_total_lose_value)
            ).let {
                //当前玩法开放
                if (it.notOpenText.isEmpty()) {
                    add(it)
                }
            }
        }
        PreMultiBasketBallData(
            leagueName = getTextById(IDPrePostMultiBasketBall.id_league_name),
            leagueStartTime = getTextById(IDPrePostMultiBasketBall.id_league_start_time),
            leftTeamName = getTextById(IDPrePostMultiBasketBall.id_left_team_name),
            rightTeamName = getTextById(IDPrePostMultiBasketBall.id_right_team_name),
            subDataArray = subDataArray,
        ).apply {
            Log.d(TAG, "analyzeItemResult $this")

        }
    }

    fun getCurLeagueListOfSelectableNodeWrapper(result: AnalyzeSourceResult) {

    }


    override fun onStart() {

    }

    override fun onDestroy() {

    }

}