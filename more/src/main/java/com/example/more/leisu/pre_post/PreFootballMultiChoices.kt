package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.transNodeInfoToNodeWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.IDFootballMultiChoices
import com.example.more.leisu.getTextById

class PreFootballMultiChoices private constructor() : BaseLeisuDispatch() {


    companion object {

        private var instance: PreFootballMultiChoices? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreFootballMultiChoices {
            if (instance == null) {
                instance = PreFootballMultiChoices()
            }
            return instance!!
        }

        const val TAG = "PreFootballMultiChoices"
    }

    override fun onEventCome(
        eventWrapper: EventWrapper,
        result: AnalyzeSourceResult
    ) {
        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                val node: AccessibilityNodeInfo? = eventWrapper.event.source
                node ?: return
                val clickNodeWrapper = node.transNodeInfoToNodeWrapper()
                try {
                    val leagueName = result.getTextById(IDFootballMultiChoices.id_league_name)
                    val startTime = result.getTextById(IDFootballMultiChoices.id_start_time)
                    val leftTeamName = result.getTextById(IDFootballMultiChoices.id_left_team_name)
                    val rightTeamName =
                        result.getTextById(IDFootballMultiChoices.id_right_team_name)

                    //添加本条比赛入选中列表
                    if (clickNodeWrapper.id == IDFootballMultiChoices.id_win_spf) {
                        PreMultiFootball.instance()
                            .insertTotalData(leagueName,startTime, leftTeamName, rightTeamName)
                    }

//                    //取消
//                    if (clickNodeWrapper.id == IDFootballMultiChoices.id_flat_spf) {
//                        PreMultiFootball.instance()
//                            .insertTotalData(leagueName,startTime, leftTeamName, rightTeamName)
//                    }

                } finally {
                    // 【强制】必须回收，否则内存泄漏、系统杀服务
                    node.recycle()
                }
            }

            else -> {

            }
        }
    }


    override fun onStart() {

    }

    override fun onDestroy() {

    }

}