package com.example.more.leisu

import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById
import com.example.more.leisu.data.IDPostDoubleSingle
import com.example.more.leisu.data.IDPostMultiDouble
import com.example.more.leisu.data.IDPrePostHeader
import com.example.more.leisu.data.id_expert_home_page_title
import com.example.more.leisu.post_detail.PostFreeSingleBusiness
import com.example.more.leisu.pre_post.PrePostDispatch

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

    fun refresh(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        taskDispatch(wrapper, result)
    }


    /**
     *  先判断在什么页面，然后执行具体业务
     */
    //业务分发
    fun taskDispatch(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        if (isInExpertHomePage(result)){
            //在专家列表页
            //设置下一次进入比赛选择页 会自动跳转 tab
            PostJumpUtils.instance().hasJumpExpertHomeAction = true
        }
        if (isInPrePostPage(result)) {
            //在比赛信息选择页
            PrePostDispatch.instance().dispatchTask(wrapper,result)
        }
        if (isInPostSinglePage(result)) {
            //在单关发布页
            PostFreeSingleBusiness(wrapper, result).execute()
        }
        if (isInPostMultiPage(result)) {

        }
    }

    /**
     * 在发布页的前一页
     */
    fun isInPrePostPage(result: AnalyzeSourceResult): Boolean {
        result.findNodeById(IDPrePostHeader.id_filter_league_info)?.let {
            return true
        }
        return false
    }

    /**
     * 在单关发布页
     */
    fun isInPostSinglePage(result: AnalyzeSourceResult): Boolean {
        val remains =
            result.findNodeById(IDPostDoubleSingle.id_single_post_today_remains_times)?.text.blankOrThis()
        val analyzeEt = result.findNodeById(IDPostDoubleSingle.id_single_post_analyse_edit)
        return remains.isNotEmpty() && analyzeEt != null
    }

    /**
     * 在多关发布页
     */
    fun isInPostMultiPage(result: AnalyzeSourceResult): Boolean {
        val remains =
            result.findNodeById(IDPostMultiDouble.id_multi_post_today_remains_times)?.text.blankOrThis()
        //val analyzeEt = result.findNodeById(PostMultiDoubleId.)
        return false
    }

    /**
     * 在专家列表页面
     */
    fun isInExpertHomePage(result: AnalyzeSourceResult): Boolean{
        result.findNodeById(id_expert_home_page_title)?.let {
            if (it.text == "专家主页") return true
            return@let
        }
        return false
    }

}