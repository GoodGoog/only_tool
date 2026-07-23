package com.example.more.leisu

import android.util.Log
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.findNodesByExpression
import com.example.more.leisu.data.IDPostMultiDouble
import com.example.more.leisu.data.IDPostSingleCommonId
import com.example.more.leisu.data.IDPrePostHeader
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.post_detail.PostMultiBasketball
import com.example.more.leisu.post_detail.PostMultiFootball
import com.example.more.leisu.post_detail.PostSingleBasketball
import com.example.more.leisu.post_detail.PostSingleFootball
import com.example.more.leisu.pre_post.PreMultiFootball
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
        //Log.d(TAG, "onEventCome: result = " + result.nodes)
        if (isInExpertHomePage(result)) {
            //情况预览页-足球-串关-选中的数据
            PreMultiFootball.instance().clearAll()
        }
        if (isInPrePostPage(result)) {
            //在比赛信息选择页
            PrePostDispatch.instance().eventCome(wrapper, result)
        }
        if (isInPostPage(result)) {
            //在发布页
            when (PreJumpUtils.instance().curPageType) {
                PostConfigData.ConfigType.SingleFootball -> {
                    Log.d(TAG, "onEventCome: SingleFootball")
                    //单关足球-发布页
                    PostSingleFootball.instance().eventCome(wrapper, result)
                }

                PostConfigData.ConfigType.MultiFootball -> {
                    Log.d(TAG, "onEventCome: MultiFootball")
                    PostMultiFootball.instance().eventCome(wrapper,result)
                }

                PostConfigData.ConfigType.SingleBasketball -> {
                    Log.d(TAG, "onEventCome: SingleBasketball")
                    //单关篮球-发布页
                    PostSingleBasketball.instance().eventCome(wrapper, result)
                }

                PostConfigData.ConfigType.MultiBasketball -> {
                    Log.d(TAG, "onEventCome: MultiBasketball")
                    PostMultiBasketball.instance().eventCome(wrapper,result)
                }
            }

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
     * 在多关发布页
     */
    fun isInPostPage(result: AnalyzeSourceResult): Boolean {
        val remains =
            result.getTextById(IDPostMultiDouble.id_multi_post_today_remains_times)
        val submit = result.getTextById(IDPostMultiDouble.id_multi_post_submit_button)
        return remains.isNotEmpty() && submit.isNotEmpty()
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

        //发布页
        PostSingleBasketball.instance().start()
        PostSingleFootball.instance().start()
        PostMultiBasketball.instance().start()
        PostMultiFootball.instance().start()
    }

    override fun onDestroy() {
        PrePostDispatch.instance().destroy()

        //发布页
        PostSingleBasketball.instance().destroy()
        PostSingleFootball.instance().destroy()
        PostMultiBasketball.instance().destroy()
        PostMultiFootball.instance().destroy()
    }

}