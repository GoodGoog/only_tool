package com.example.more.leisu.pre_post

import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter

class PreMultiFootball private constructor(): BaseLeisuDispatch(){
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

    /**
     * 来这里的只有
     */
    fun  onEventCome(eventWrapper: EventWrapper,result: AnalyzeSourceResult) {
        if (!PreDataCenter.instance().getCurPrePageAllowAutoPost(PostConfigData.ConfigType.MultiFootball)) return

        //result.findNodeById(id_post_submit_button).click()

    }

    override fun onStart() {

    }

    override fun onDestroy() {

    }

}