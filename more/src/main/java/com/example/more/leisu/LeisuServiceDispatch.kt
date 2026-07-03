package com.example.more.leisu

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.analyseRecyclerViewSubView
import com.example.more.accessibility.analyzeNextLevelSubView
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.findNodesById
import com.example.more.leisu.data.IDPostDoubleSingle
import com.example.more.leisu.data.IDPostMultiDouble
import com.example.more.leisu.data.IDPrePostHeader
import com.example.more.leisu.data.IDPrePostMultiBasketBall
import com.example.more.leisu.data.IDPrePostMultiFootball
import com.example.more.leisu.data.IDPrePostSingleBalls
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PostDataCenter
import com.example.more.leisu.handle.PostFreeSingleBusiness

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


    /**
     *  先判断在什么页面，然后执行具体业务
     */
    //业务分发
    fun taskDispatch(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        if (isInPrePostPage(result)) {
            //在比赛信息选择页
            onPrePostPage(wrapper, result)
        }
        if (isInPostSinglePage(result)) {
            //在单关发布页
            wrapper.let {
                PostFreeSingleBusiness(it, result).execute()
            }
        }
        if (isInPostMultiPage(result)) {
            onMultiPostPage(result)
        }
    }

    /**
     * 在发布页的入口页,即比赛列表选择页
     */
    fun onPrePostPage(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        //PrePostSingleBusiness(wrapper, result).execute()
        //根据数据中心判断当前应该干什么
        PostDataCenter.instance().postArray.let {
            if (it.isEmpty()) {
                //所有文章发布结束
                //do something
                return
            }
            Log.d(TAG, "====onPrePostPage: ")
            if (wrapper.event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ) return
            Log.d(TAG, "====onPrePostPage: 来了马")
            //从第一大类开始发布
            //-->!!跳转对应页面
            PostUtils.instance().jumpSubPage(it[0].type, result)
//            result.findNodeById(IDPrePostHeader.id_first_level_view_pager).analyzeNextLevelSubView()
//                .forEach { viewPageNode ->
//                    //遍历两个ViewPager
//                    viewPageNode.analyzeNextLevelSubView().let { recyclerViewNode ->
//                        //遍历单个ViewPage包含的RecyclerView
//                        recyclerViewNode.
//                    }
//                }
//
            result.findNodesById(IDPrePostHeader.id_league_lsit_rv).nodes.let { rvNodes ->
                Log.d(TAG, "onPrePostPage: -------------------------------------------------------------\n")
                Log.d(TAG, "onPrePostPage: ==================" + rvNodes)

            }

            //-->!!在对应页面中办事
            when (it[0].type) {
                PostConfigData.ConfigType.SingleBasketball -> {
                    result.findNodeById(IDPrePostSingleBalls.id_single_league_list_rv)
                        .analyzeRecyclerView().let { itemArray ->
                            //Log.d(TAG, "onPrePostPage: 来了一次----")
                            itemArray?.forEach { it ->
                                //Log.d(TAG, "SingleBasketball: 但应单篮数据-" + it.nodes)
                            }
                        }
                }

                PostConfigData.ConfigType.SingleFootball -> {
                    result.findNodeById(IDPrePostSingleBalls.id_single_league_list_rv)
                        .analyzeRecyclerView().let { itemArray ->
                            //Log.d(TAG, "onPrePostPage: 来了一次----")
                            itemArray?.forEach { it ->
                                //Log.d(TAG, "SingleFootball: 但应单篮数据-" + it.nodes)
                            }
                        }
                }

                PostConfigData.ConfigType.MultiBasketball -> {
                    result.findNodeById(IDPrePostMultiBasketBall.id_multi_basket_league_list)
                        .analyzeRecyclerView().let { itemArray ->
                            //Log.d(TAG, "onPrePostPage: 来了一次----")
                            itemArray?.forEach { it ->
                                //Log.d(TAG, "MultiBasketball: 但应单篮数据-" + it.nodes)
                            }
                        }
                }

                PostConfigData.ConfigType.MultiFootball -> {
                    result.findNodeById(IDPrePostMultiFootball.id_multi_foot_league_list)
                        .analyzeRecyclerView().let { itemArray ->
                            //Log.d(TAG, "onPrePostPage: 来了一次----")
                            itemArray?.forEach { it ->
                                //Log.d(TAG, "MultiFootball: 但应单篮数据-" + it.nodes)
                            }
                        }
                }
            }
        }
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
        result.findNodeById(IDPrePostHeader.id_filter_league_info)?.let {
            return true
        }
        return false
    }

    /**
     * 单关发布页
     */
    fun isInPostSinglePage(result: AnalyzeSourceResult): Boolean {
        val remains =
            result.findNodeById(IDPostDoubleSingle.id_single_post_today_remains_times)?.text.blankOrThis()
        val analyzeEt = result.findNodeById(IDPostDoubleSingle.id_single_post_analyse_edit)
        return remains.isNotEmpty() && analyzeEt != null
    }

    /**
     * 多关发布页
     */
    fun isInPostMultiPage(result: AnalyzeSourceResult): Boolean {
        val remains =
            result.findNodeById(IDPostMultiDouble.id_multi_post_today_remains_times)?.text.blankOrThis()
        //val analyzeEt = result.findNodeById(PostMultiDoubleId.)
        return false
    }

}