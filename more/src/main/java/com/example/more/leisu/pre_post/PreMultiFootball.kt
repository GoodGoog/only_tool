package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.transNodeInfoToNodeWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.IDPreMultiFootball
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.data.PreMultiFootBallData
import com.example.more.leisu.data.PreMultiFootBallSubData
import com.example.more.leisu.getCurPrePageMatchList
import com.example.more.leisu.getNumberTextByIdAndFilterOther
import com.example.more.leisu.getTextById
import com.example.more.leisu.isClickNodeInCurLeagueList

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
                    if (!isClickNodeInCurLeagueList(
                            result,
                            curType,
                            node.transNodeInfoToNodeWrapper()
                        )
                    ) return
                } finally {
                    // 【强制】必须回收，否则内存泄漏、系统杀服务
                    node.recycle()
                }
            }

            else -> {

            }
        }
    }

    fun startAutoPost(result: AnalyzeSourceResult) {
        if (!PreDataCenter.instance()
                .isCurPrePageAllowAutoPost(curType)
        ) return
        getCurPrePageMatchList(result, curType) { itemResults ->
            itemResults.forEach { itemResult ->
                itemResult.analyzeItemResult()
            }
        }
    }

    fun AnalyzeSourceResult.analyzeItemResult() {
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

        }
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

}