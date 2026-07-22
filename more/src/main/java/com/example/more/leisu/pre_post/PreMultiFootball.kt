package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.IDPreMultiFootball
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.data.PreMultiFootBallData
import com.example.more.leisu.getCurPrePageMatchList
import com.example.more.leisu.getNumberTextByIdAndFilterOther
import com.example.more.leisu.getTextById

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
        if (!PreDataCenter.instance()
                .isCurPrePageAllowAutoPost(curType)
        ) return
        Log.d(TAG, "onWindowStatusChange: ++++++++++++++++++++++++++++++++++++++++++++++++++++++==")
        getCurPrePageMatchList(result, curType) { itemResults ->
            itemResults.forEach { itemResult ->
                Log.d(TAG, "startAutoPost: itemResult == " +  itemResult)
                itemResult.analyzeItemResult()
                Log.d(
                    TAG,
                    "onWindowStatusChange: -------____________________________________________________"
                )
            }
        }
    }

    fun AnalyzeSourceResult.analyzeItemResult(){
        PreMultiFootBallData(
            leagueName = getTextById(IDPreMultiFootball.id_league_name),
            leagueStartTime = getTextById(IDPreMultiFootball.id_league_start_time),
            leftTeamName = getTextById(IDPreMultiFootball.id_left_team_name),
            rightTeamName = getTextById(IDPreMultiFootball.id_right_team_name),

            //主客互不让分
            spfScore = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_spf),
            spfWinValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_spf_win_value),
            spfFlatValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_spf_flat_value),
            spfLoseValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_spf_lose_value),

            //主队 让[-1] 或 受让[+1]
            rqScore = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_rq),
            rqWinValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_rq_win_value),
            rqFlatValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_rq_flat_value),
            rqLoseValue = getNumberTextByIdAndFilterOther(IDPreMultiFootball.id_tv_rq_lose_value)
        ).let {
            Log.d(TAG, "analyzeItemResult " + it)
        }
    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

}