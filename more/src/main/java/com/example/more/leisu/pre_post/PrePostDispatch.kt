package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.findNodesByExpression
import com.example.more.accessibility.findNodesById
import com.example.more.accessibility.transNodeInfoToNodeWrapper
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

                    //测试数据
                    //获取当前的recyclerView信息
                    //Log.d(TAG, "dispatchTask: ==================" + result)
                }
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED ->{
                //如果只在足球页面，且不曾点开篮球页面，则只能找到2个单关+串关按钮，如果足球篮球页面都打开过，则能找到四个串关+单关按钮！！！！！！
                // wrapper.event.source获取的时发生点击事件控件的 父视图 节点

                //Log.d(TAG, "dispatchTask: TYPE_VIEW_CLICKED"+ result.nodes )
                wrapper.event.source.let { rootNode ->
                    val t = result.findNodesByExpression {
                        it.text == "单关" || it.text == "串关"
                    }.nodes

                    Log.d(TAG, "dispatchTask: size===" + t.size)
                }

            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                Log.d(TAG, "dispatchTask: TYPE_VIEW_SCROLLED---")
            }

            else -> {
                Log.d(TAG, "dispatchTask: else")
            }
        }
    }



}