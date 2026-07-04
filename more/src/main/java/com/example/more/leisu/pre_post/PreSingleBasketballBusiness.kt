package com.example.more.leisu.pre_post

import android.util.Log
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.findNodeById
import com.example.more.leisu.data.IDPostDoubleSingle
import com.example.more.leisu.data.IDPrePostSingleBalls

class PreSingleBasketballBusiness() {

    companion object {

        private var instance: PreSingleBasketballBusiness? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreSingleBasketballBusiness {
            if (instance == null) {
                instance = PreSingleBasketballBusiness()
            }
            return instance!!
        }

        const val TAG = "PreSingleBasketballBusiness"
    }

    /**
     * 来这里的只有
     */
    fun onWindowStatusChange(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {

        Log.d(TAG, "onWindowStatusChange: 单篮")

        result.findNodeById(IDPrePostSingleBalls.id_single_league_list_rv).analyzeRecyclerView()?.apply {
            this.forEach {
                Log.d(TAG, "onWindowStatusChange: " + it.nodes)
                Log.d(TAG, "onWindowStatusChange: ------------------------------------------------------")
            }
        }

    }


}