package com.example.more.leisu

import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.findNodesByExpression
import com.example.more.leisu.data.IDPostMultiDouble
import com.example.more.leisu.data.IDPostSingleCommonId
import com.example.more.leisu.data.IDPrePostHeader
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.post_detail.PostSingleBasketball
import com.example.more.leisu.post_detail.PostSingleFootball
import com.example.more.leisu.pre_post.PrePostDispatch

class LeisuServiceDispatch private constructor() : BaseLeisuDispatch() {
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

    /**
     *  先判断在什么页面，然后执行具体业务
     */
    //业务分发
    override fun onEventCome(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        if (isInExpertHomePage(result)) {

        }
        if (isInPrePostPage(result)) {
            //在比赛信息选择页
            PrePostDispatch.instance().eventCome(wrapper, result)
        }
        if (isInPostSinglePage(result)) {
            //在单关发布页
            if (PreJumpUtils.instance().curPageType == PostConfigData.ConfigType.SingleFootball) {
                //单关足球-发布页
                PostSingleFootball.instance().eventCome(wrapper, result)
            }
            if (PreJumpUtils.instance().curPageType == PostConfigData.ConfigType.SingleBasketball) {
                //单关篮球-发布页
                PostSingleBasketball.instance().eventCome(wrapper, result)
            }
            //PostFreeSingleBusiness(wrapper, result).execute()
        }
        if (isInPostMultiPage(result)) {

        }
    }

    /**
     * 在发布页的前一页
     */
    fun isInPrePostPage(result: AnalyzeSourceResult): Boolean {
//        result.findNodeById(IDPrePostHeader.id_filter_league_info)?.let {
//            return true
//        }
//        return false
        result.findNodesByExpression {
            it.id == IDPrePostHeader.id_filter_league_info || it.id == IDPrePostHeader.id_switch_speed || it.id == IDPrePostHeader.id_back
        }.nodes.let {
            return it.size >= 3
        }
    }

    /**
     * 在单关发布页
     */
    fun isInPostSinglePage(result: AnalyzeSourceResult): Boolean {
        val remains =
            result.findNodeById(IDPostSingleCommonId.id_single_post_today_remains_times)?.text.blankOrThis()
        val analyzeEt = result.findNodeById(IDPostSingleCommonId.id_single_post_analyse_edit)
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
    fun isInExpertHomePage(result: AnalyzeSourceResult): Boolean {
        result.findNodesByExpression {
            it.text == "专家主页" || it.text == "编辑" || it.text == "达人分" || it.text == "总收益"
        }.nodes.let {
            return it.size >= 4
        }

        return false
    }

    override fun onStart() {
        PrePostDispatch.instance().start()
    }

    override fun onDestroy() {
        PrePostDispatch.instance().destroy()
    }

}