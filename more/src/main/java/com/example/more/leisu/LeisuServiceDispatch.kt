package com.example.more.leisu

import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById
import com.example.more.leisu.data.PostDoubleSingleId
import com.example.more.leisu.data.PostMultiDoubleId
import com.example.more.leisu.data.PrePostHeaderId
import com.example.more.leisu.handle.PostFreeSingleBusiness
import com.example.more.leisu.handle.PrePostSingleBusiness

class LeisuServiceDispatch {
    companion object {

        private var instance: LeisuServiceDispatch? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): LeisuServiceDispatch {
            if (instance == null) {
                instance = LeisuServiceDispatch()
            }
            return instance!!
        }

        const val TAG = "LeisuServiceDispatch"
    }

    var wrapper: EventWrapper? = null
    var result: AnalyzeSourceResult? = null

    fun refresh(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        this.wrapper = wrapper
        this.result = result
        taskDispatch(wrapper, result)
    }


    //业务分发
    fun taskDispatch(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        if (isInPostSinglePage(result)) {
            //在单关发布页
            wrapper.let {
                PostFreeSingleBusiness(it, result).execute()
            }
        }
        if (isInPostMultiPage(result)) {
            onMultiPostPage(result)
        }
        if (isInPrePostPage(result)) {
            onPrePostPage(wrapper, result)
        }
    }

    /**
     * 在发布页的入口页,即比赛列表选择页
     */
    fun onPrePostPage(wrapper: EventWrapper, result: AnalyzeSourceResult) {
//        result.findNodeByExpression { nodeWrapper ->
//            nodeWrapper.text == "篮球"
//        }.delayClick()
//        result.findNodeByText("单关").delayClick(2000)
        PrePostSingleBusiness(wrapper, result).execute()
    }

    /**
     * 在串关发布页
     */
    fun onMultiPostPage(result: AnalyzeSourceResult) {

    }

    /**
     * 在发布页的前一页
     */
    fun isInPrePostPage(result: AnalyzeSourceResult): Boolean {
        result.findNodeById(PrePostHeaderId.id_filter_league_info)?.let {
            return true
        }
        return false
    }

    /**
     * 单关发布页
     */
    fun isInPostSinglePage(result: AnalyzeSourceResult): Boolean {
        val remains =
            result.findNodeById(PostDoubleSingleId.id_single_post_today_remains_times)?.text.blankOrThis()
        val analyzeEt = result.findNodeById(PostDoubleSingleId.id_single_post_analyse_edit)
        return remains.isNotEmpty() && analyzeEt != null
    }

    /**
     * 多关发布页
     */
    fun isInPostMultiPage(result: AnalyzeSourceResult): Boolean {
        val remains =
            result.findNodeById(PostMultiDoubleId.id_multi_post_today_remains_times)?.text.blankOrThis()
        //val analyzeEt = result.findNodeById(PostMultiDoubleId.)
        return false
    }

}