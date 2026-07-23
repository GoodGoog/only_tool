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
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.data.PreMultiFootBallData
import com.example.more.leisu.data.PreMultiFootBallSubData
import com.example.more.leisu.data.PreMultiFootballSelectedLeague
import com.example.more.leisu.getCurPrePageMatchList
import com.example.more.leisu.getNumberTextByIdAndFilterOther
import com.example.more.leisu.getTextById
import com.example.more.leisu.isClickNodeInCurLeagueList
import com.example.more.leisu.isContainsNodeWrapper

class PreMultiFootball private constructor() : BaseLeisuDispatch() {
    companion object {

        private var instance: PreMultiFootball? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreMultiFootball {
            if (instance == null) {
                instance = PreMultiFootball()
            }
            return instance!!
        }

        const val TAG = "PreMultiFootball"
    }

    val curType = PostConfigData.ConfigType.MultiFootball

    //被选中的item,以及其中的详细被选中玩法
    val selectedItemArray = ArrayList<PreMultiFootballSelectedLeague>()

    /**
     * 来这里的只有
     */
    override
    fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {
        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //串关-足球只支持手动开启
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
        val itemResults = getCurPrePageMatchList(result, curType)

        var curClickInItemTag = ""
        var isClickSpf = true
        //只在当前item首次被点击时才被需要
        var newSelectedLeague: PreMultiFootballSelectedLeague? = null
        //找出被点击的节点对应的itemTag
        run {
            itemResults.forEach { itemResult ->
                if (itemResult.isContainsNodeWrapper(clickedNodeWrapper)) {
                    //被点击的节点在当前的item内
                    curClickInItemTag =
                        itemResult.findNodeById(IDPreMultiFootball.id_left_team_name)?.text?.blankOrThis() + "VS" +
                                itemResult.findNodeById(IDPreMultiFootball.id_right_team_name)?.text?.blankOrThis()

                    //存储一下数据，如果这个item是第一次被点击时会被用来存储进selectedItemArray
                    //根据节点id是否包含spf[不让球] 或者 rq[主队让/不让球],来判断是那种类型的玩法[让分或者不让分]
                    val scoreNodeWrapper =
                        if (clickedNodeWrapper.id.blankOrThis().contains("spf")) {
                            isClickSpf = true
                            itemResult.findNodeById(IDPreMultiFootball.id_tv_spf)
                        } else {
                            isClickSpf = false
                            itemResult.findNodeById(IDPreMultiFootball.id_tv_rq)
                        }
                    val selectedNodes = ArrayList<NodeWrapper>().apply {
                        add(clickedNodeWrapper)
                    }
                    scoreNodeWrapper?.let {
                        newSelectedLeague = PreMultiFootballSelectedLeague(
                            itemTag = curClickInItemTag,
                            isSpf = isClickSpf,
                            scoreNodeWrapper = it,
                            selectedNodes
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
                upDataClickNodeWrapper(
                    isClickSpf = isClickSpf,
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
//        val itemNodesArray = getCurPrePageMatchList(result, curType)
//        itemNodesArray.forEach { itemNodesResult ->
//            val itemData = itemNodesResult.analyzeItemResult()
//            val itemDataTage = itemData.leftTeamName + "VS" + itemData.rightTeamName
//            selectedItemArray.forEachIndexed { index, selectedItem ->
//                if (selectedItem.itemTag == itemDataTage) {
//                    //当前item已被选中，判断是要取消玩法，还是跟新玩法
//
//                }
//            }
//        }
    }

    fun AnalyzeSourceResult.analyzeItemResult(): PreMultiFootBallData {
        val subDataArray = ArrayList<PreMultiFootBallSubData>().apply {
            //主客互不让分
            PreMultiFootBallSubData(
                isSpf = true,
                score = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_spf),
                notOpenText = getTextById(IDPreMultiFootball.id_tv_spf_not_open),
                winValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_spf_win_value),
                flatValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_spf_flat_value),
                loseValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_spf_lose_value)
            ).let {
                //当前玩法开放
                if (it.notOpenText.isEmpty()) {
                    add(it)
                }
            }
            //主队 让[-1] 或 受让[+1]
            PreMultiFootBallSubData(
                isSpf = false,
                score = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_rq),
                notOpenText = getTextById(IDPreMultiFootball.id_tv_rq_not_open),
                winValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_rq_win_value),
                flatValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_rq_flat_value),
                loseValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_rq_lose_value)
            ).let {
                //当前玩法开放
                if (it.notOpenText.isEmpty()) {
                    add(it)
                }
            }
        }
        PreMultiFootBallData(
            leagueName = getTextById(IDPreMultiFootball.id_league_name),
            leagueStartTime = getTextById(IDPreMultiFootball.id_league_start_time),
            leftTeamName = getTextById(IDPreMultiFootball.id_left_team_name),
            rightTeamName = getTextById(IDPreMultiFootball.id_right_team_name),

            subDataArray = subDataArray,
        ).apply {
            Log.d(TAG, "analyzeItemResult $this")
            return this
        }
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

    fun clearAll() {
        selectedItemArray.clear()
        Log.d(TAG, "clearAll: ----------------- ${selectedItemArray.size}")
    }

}