package com.example.more.leisu.pre_post

import android.util.Log
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter

class PreSingleBasketball private constructor() : BaseLeisuDispatch() {

    companion object {

        private var instance: PreSingleBasketball? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreSingleBasketball {
            if (instance == null) {
                instance = PreSingleBasketball()
            }
            return instance!!
        }

        const val TAG = "PreSingleBasketball"
    }

    /**
     * 来这里的只有
     */
    fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {
        if (!PreDataCenter.instance().getCurPrePageAllowAutoPost(PostConfigData.ConfigType.SingleBasketball)) return
        Log.d(TAG, "onWindowStatusChange: ++++++++++++++++++++++++++++++++++++++++++++++++++++++==")
        getCurPrePageMatchList(result, PostConfigData.ConfigType.SingleBasketball) { itemResults ->
            itemResults.forEach { itemResult ->
                Log.d(TAG, "onWindowStatusChange: -----" + itemResult.nodes)
                Log.d(TAG, "onWindowStatusChange: -------____________________________________________________")
                if (itemResult.isCurItemTypeTimeFlags()) {
                } else {
                }
            }
        }

//        Log.d(TAG, "onWindowStatusChange: 单篮")
//
//        result.findNodeById(IDPrePostSingleBalls.id_single_league_list_rv).analyzeRecyclerView()?.apply {
//            this.forEach {
//                Log.d(TAG, "onWindowStatusChange: " + it.nodes)
//                Log.d(TAG, "onWindowStatusChange: ------------------------------------------------------")
//            }
//        }

    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }


}