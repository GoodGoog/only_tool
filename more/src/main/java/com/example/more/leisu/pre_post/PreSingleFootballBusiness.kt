package com.example.more.leisu.pre_post

import android.util.Log
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.logD
import com.example.more.leisu.BaseLifecycleOwner
import kotlin.math.log

class PreSingleFootballBusiness private constructor() : BaseLifecycleOwner(){
    companion object {

        private var instance: PreSingleFootballBusiness? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreSingleFootballBusiness {
            if (instance == null) {
                instance = PreSingleFootballBusiness()
            }
            return instance!!
        }

        const val TAG = "PreSingleFootballBusiness"
    }

    /**
     * 来这里的只有
     */
    fun  onWindowStatusChange(eventWrapper: EventWrapper,result: AnalyzeSourceResult) {

        //result.findNodeById(id_post_submit_button).click()

    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

}