package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.findNodesById
import com.example.more.leisu.LeisuServiceDispatch
import com.example.more.leisu.PostJumpUtils
import com.example.more.leisu.data.IDPrePostHeader
import com.example.more.leisu.data.IDPrePostMultiBasketBall
import com.example.more.leisu.data.IDPrePostMultiFootball
import com.example.more.leisu.data.IDPrePostSingleBalls
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PostDataCenter

class PrePostDispatch {
    companion object {

        private var instance: PrePostDispatch? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PrePostDispatch {
            if (instance == null) {
                instance = PrePostDispatch()
            }
            return instance!!
        }

        const val TAG = "PrePostDispatch"
    }

    fun dispatchTask(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        when(wrapper.event.eventType){
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //点击试试能否触发cicled
                PostDataCenter.instance().postArray.let { postArray->
                    if (postArray.isEmpty()) {
                        //所有文章发布结束
                        //do something
                        return
                    }

                    //UI分配
                    postArray[0].let { configData ->
                        //从专家主页进入比赛选择页，而不是从其他子页面退回赛事选择页，也要执行跳转
                        if (PostJumpUtils.instance().hasJumpExpertHomeAction){
                            PostJumpUtils.instance().jumpSubPage(configData.type, result){
                                PostJumpUtils.instance().hasJumpExpertHomeAction = false
                            }
                        }
                    }

                    //业务分配
                    when(PostJumpUtils.instance().curPageType){
                        PostConfigData.ConfigType.SingleBasketball -> {
                            PreSingleBasketballBusiness.instance().onWindowStatusChange(wrapper,result)
                        }

                        PostConfigData.ConfigType.SingleFootball -> {
                            PreSingleFootballBusiness.instance().onWindowStatusChange(wrapper,result)
                        }

                        PostConfigData.ConfigType.MultiBasketball -> {
                            PreMultiBasketballBusiness.instance().onWindowStatusChange(wrapper,result)
                        }

                        PostConfigData.ConfigType.MultiFootball -> {
                            PreMultiFootballBusiness.instance().onWindowStatusChange(wrapper,result)
                        }
                    }

//                    //获取当前的recyclerView信息
//                    result.findNodesById(IDPrePostHeader.id_league_lsit_rv).nodes.let { rvNodes ->
//                        Log.d(TAG, "dispatchTask: ==================" + rvNodes)
//                    }
                }
            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED ->{}
        }
    }


    /**
     * 在发布页的入口页,即比赛列表选择页
     */
    fun onPrePostPage(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        //PrePostSingleBusiness(wrapper, result).execute()
        //根据数据中心判断当前应该干什么
        PostDataCenter.Companion.instance().postArray.let {
            if (it.isEmpty()) {
                //所有文章发布结束
                //do something
                return
            }
            Log.d(LeisuServiceDispatch.Companion.TAG, "====onPrePostPage: ")
            if (wrapper.event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ) return
            Log.d(LeisuServiceDispatch.Companion.TAG, "====onPrePostPage: 来了马")
            //从第一大类开始发布
            //-->!!跳转对应页面
            PostJumpUtils.Companion.instance().jumpSubPage(it[0].type, result){
                //当前信息列表已显示 欲跳转的 子页面
                //延时 1秒钟等带网络数据拉取

            }
//            result.findNodeById(IDPrePostHeader.id_first_level_view_pager).analyzeNextLevelSubView()
//                .forEach { viewPageNode ->
//                    //遍历两个ViewPager
//                    viewPageNode.analyzeNextLevelSubView().let { recyclerViewNode ->
//                        //遍历单个ViewPage包含的RecyclerView
//                        recyclerViewNode.
//                    }
//                }
//
            result.findNodesById(IDPrePostHeader.Companion.id_league_lsit_rv).nodes.let { rvNodes ->
                Log.d(TAG, "onPrePostPage: -------------------------------------------------------------\n")
                Log.d(TAG, "onPrePostPage: ==================" + rvNodes)
            }

            //-->!!在对应页面中办事
            when (it[0].type) {
                PostConfigData.ConfigType.SingleBasketball -> {
                    result.findNodeById(IDPrePostSingleBalls.Companion.id_single_league_list_rv)
                        .analyzeRecyclerView().let { itemArray ->
                            //Log.d(TAG, "onPrePostPage: 来了一次----")
                            itemArray?.forEach { it ->
                                //Log.d(TAG, "SingleBasketball: 但应单篮数据-" + it.nodes)
                            }
                        }
                }

                PostConfigData.ConfigType.SingleFootball -> {
                    result.findNodeById(IDPrePostSingleBalls.Companion.id_single_league_list_rv)
                        .analyzeRecyclerView().let { itemArray ->
                            //Log.d(TAG, "onPrePostPage: 来了一次----")
                            itemArray?.forEach { it ->
                                //Log.d(TAG, "SingleFootball: 但应单篮数据-" + it.nodes)
                            }
                        }
                }

                PostConfigData.ConfigType.MultiBasketball -> {
                    result.findNodeById(IDPrePostMultiBasketBall.Companion.id_multi_basket_league_list)
                        .analyzeRecyclerView().let { itemArray ->
                            //Log.d(TAG, "onPrePostPage: 来了一次----")
                            itemArray?.forEach { it ->
                                //Log.d(TAG, "MultiBasketball: 但应单篮数据-" + it.nodes)
                            }
                        }
                }

                PostConfigData.ConfigType.MultiFootball -> {
                    result.findNodeById(IDPrePostMultiFootball.Companion.id_multi_foot_league_list)
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


}