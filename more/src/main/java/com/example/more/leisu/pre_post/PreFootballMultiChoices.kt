package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.analyzeNextLevelSubView
import com.example.more.accessibility.transNodeInfoToNodeWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.IDFootballMultiChoices
import com.example.more.leisu.getTextById
import com.example.more.leisu.numberTransToChinese
import com.example.more.leisu.transAccessibilityEventToString
import com.example.more.leisu.transToPostConfigType

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
        //Log.d(TAG, "onEventCome: -${result.nodes}")
        //Log.d(TAG, "onEventCome: type" + eventWrapper.event.eventType.transAccessibilityEventToString())
//        val node: AccessibilityNodeInfo? = eventWrapper.event.source
//        node ?: return
//        val clickNodeWrapper = node.transNodeInfoToNodeWrapper()
//        Log.d(TAG, "onEventCome: 这是被点击的按钮-$clickNodeWrapper")
        //Log.d(TAG, "onEventCome: -${result.nodes}")
        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                val node: AccessibilityNodeInfo? = eventWrapper.event.source
                node ?: return
                val clickNodeWrapper = node.transNodeInfoToNodeWrapper()
                try {
                    
                    //如果被点击节点的下一层字节的含有 进球数+对应赔率
                    //则当前为进球数玩法
                    clickNodeWrapper.analyzeNextLevelSubView().filter { 
                        it.id == IDFootballMultiChoices.id_total_number || it.id == IDFootballMultiChoices.id_total_cur_number_value
                    }.let { 
                        if (it.size >= 2){
                            //当前是进球数玩法
                            val leagueName = result.getTextById(IDFootballMultiChoices.id_league_name)
                            val startTime = result.getTextById(IDFootballMultiChoices.id_start_time)
                            val leftTeamName = result.getTextById(IDFootballMultiChoices.id_left_team_name)
                            val rightTeamName =
                                result.getTextById(IDFootballMultiChoices.id_right_team_name)

                            val clickNodeResult = AnalyzeSourceResult(ArrayList(it))
                            val number = clickNodeResult.getTextById(IDFootballMultiChoices.id_total_number)
                            val value = clickNodeResult.getTextById(IDFootballMultiChoices.id_total_cur_number_value)

                            PreMultiFootball.instance()
                                .insertMultiChoicesShootNumData(leagueName,startTime, leftTeamName, rightTeamName,number,value)

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


    override fun onStart() {

    }

    override fun onDestroy() {

    }

}