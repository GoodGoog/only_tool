package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.IDPreMultiFootball
import com.example.more.leisu.data.IDPrePostMultiBasketBall
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.data.PreMultiBasketBallData
import com.example.more.leisu.data.PreMultiBasketBallSubData
import com.example.more.leisu.data.PreMultiFootBallData
import com.example.more.leisu.data.PreMultiFootBallSubData
import com.example.more.leisu.getCurPrePageMatchList
import com.example.more.leisu.getNumberTextByIdAndFilterOther
import com.example.more.leisu.getTextById
import com.example.more.leisu.isCurItemTypeTimeFlags

class PreMultiBasketball private constructor(): BaseLeisuDispatch(){
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

    /**
     * 来这里的只有
     */
    override
    fun  onEventCome(eventWrapper: EventWrapper,result: AnalyzeSourceResult) {

        when (eventWrapper.event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //多关需要手动开启 次次都是
                //startAutoPost(result)
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


    override fun onStart() {

    }

    override fun onDestroy() {

    }

}