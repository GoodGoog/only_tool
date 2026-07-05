package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.LeisuServiceCenter
import com.example.more.accessibility.findNodesByExpression
import com.example.more.leisu.PreJumpUtils
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PostDataCenter
import com.jeremyliao.liveeventbus.LiveEventBus

class PrePostDispatch private constructor(){
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

    init {
        // 注意：单例要用 observeForever，因为没有生命周期
        // 或者用 Application 的 lifecycle
        if (LeisuServiceCenter.instance().isAccessServiceConnect) {
            LiveEventBus.get<Int>(EventBusTag.TEST_PRE_POST_PAGE_SWITCH)
                .observeForever {
                    val type = when(it){
                        1 -> {
                            PostConfigData.ConfigType.SingleFootball
                        }
                        2 -> {
                            PostConfigData.ConfigType.MultiFootball
                        }
                        3 -> {
                            PostConfigData.ConfigType.SingleBasketball
                        }
                        else -> {
                            PostConfigData.ConfigType.MultiBasketball
                        }
                    }
                    PreJumpUtils.instance().jumpSubPage(type, LeisuServiceCenter.instance().result){
                        //PostJumpUtils.instance().hasJumpExpertHomeAction = false
                    }

                }
        }
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
//                        if (PreJumpUtils.instance().hasJumpExpertHomeAction){
//                            PreJumpUtils.instance().jumpSubPage(configData.type, result){
//                                PreJumpUtils.instance().hasJumpExpertHomeAction = false
//                            }
//                        }
                    }

                    //业务分配
                    when(PreJumpUtils.instance().curPageType){
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
                    Log.d(TAG, "dispatchTask: ==================" + result)
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