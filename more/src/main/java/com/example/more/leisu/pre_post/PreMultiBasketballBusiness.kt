package com.example.more.leisu.pre_post

import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.BaseLeisuDispatch

class PreMultiBasketballBusiness private constructor(): BaseLeisuDispatch(){
    companion object {

        private var instance: PreMultiBasketballBusiness? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreMultiBasketballBusiness {
            if (instance == null) {
                instance = PreMultiBasketballBusiness()
            }
            return instance!!
        }

        const val TAG = "PreMultiBasketballBusiness"
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