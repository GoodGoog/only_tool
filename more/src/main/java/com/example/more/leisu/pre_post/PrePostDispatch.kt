package com.example.more.leisu.pre_post

import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.LeisuServiceCenter
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.PreJumpUtils
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.transToPostConfigType
import com.jeremyliao.liveeventbus.LiveEventBus

class PrePostDispatch private constructor() : BaseLeisuDispatch() {
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

    //是否自动点击
    var isAllowAutoPost = false

    init {
        // 注意：单例要用 observeForever，因为没有生命周期
        // 或者用 Application 的 lifecycle
        if (LeisuServiceCenter.instance().isAccessServiceConnect) {
            LiveEventBus.get<Int>(EventBusTag.TEST_PRE_POST_PAGE_SWITCH)
                .observe(this) { pageIndex ->
                    val result = LeisuServiceCenter.instance().result
                    PreDataCenter.instance()
                        .setCurPrePageAllowAutoPost(pageIndex.transToPostConfigType(), true)
                    PreJumpUtils.instance().jumpSubPage(
                        pageIndex.transToPostConfigType(),
                        result
                    ) { isSuccess ->
                        //跳转成功，默认开始自动发布
                        ////////////////////////////////////////////////////////////////////////测试
                        //return@jumpSubPage


                        if (isSuccess) {
                            when (pageIndex.transToPostConfigType()) {
                                PostConfigData.ConfigType.SingleBasketball -> {
                                    PreSingleBasketball.instance()
                                        .startAutoPost(result)
                                }

                                PostConfigData.ConfigType.SingleFootball -> {
                                    PreSingleFootball.instance()
                                        .startAutoPost(result)
                                }

                                PostConfigData.ConfigType.MultiBasketball -> {
                                    PreMultiBasketball.instance()
                                        .startAutoPost(result)
                                }

                                PostConfigData.ConfigType.MultiFootball -> {
                                    PreMultiFootball.instance()
                                        .startAutoPost(result)
                                }
                            }
                        }
                    }
                }

            LiveEventBus.get<Boolean>(EventBusTag.STOP_CUR_PAGE_POST)
                .observe(this) { _ ->
                    //终止当前页面的下一次发布，作用与 发布页--->赛事列表页
                    PreDataCenter.instance()
                        .setCurPrePageAllowAutoPost(PreJumpUtils.instance().curPageType, false)
                }

        }
    }

    fun dispatchTask(wrapper: EventWrapper, result: AnalyzeSourceResult) {

        LeisuServiceCenter.instance().result = result

        when (PreJumpUtils.instance().curPageType) {
            PostConfigData.ConfigType.SingleBasketball -> {
                PreSingleBasketball.instance()
                    .onEventCome(wrapper, result)
            }

            PostConfigData.ConfigType.SingleFootball -> {
                PreSingleFootball.instance()
                    .onEventCome(wrapper, result)
            }

            PostConfigData.ConfigType.MultiBasketball -> {
                PreMultiBasketball.instance()
                    .onEventCome(wrapper, result)
            }

            PostConfigData.ConfigType.MultiFootball -> {
                PreMultiFootball.instance()
                    .onEventCome(wrapper, result)
            }
        }

//        when (wrapper.event.eventType) {
//            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
//                Log.d(TAG, "dispatchTask: ---------------result\n" + cResult )
//                PreDataCenter.instance().postArray.let { postArray ->
//                    if (postArray.isEmpty()) {
//                        //所有文章发布结束
//                        //do something
//                        return
//                    }
//
//                    //UI分配
//                    postArray[0].let { configData ->
//                        //从专家主页进入比赛选择页，而不是从其他子页面退回赛事选择页，也要执行跳转
////                        if (PreJumpUtils.instance().hasJumpExpertHomeAction) {
////                            Log.d(TAG, "dispatchTask: 需要跳转" + configData)
////                            PreJumpUtils.instance().hasJumpExpertHomeAction = false
////                            PreJumpUtils.instance().jumpSubPage(configData.type, result) {
////                            }
////                        }
//                    }
//
//                    //业务分配
//                    when (PreJumpUtils.instance().curPageType) {
//                        PostConfigData.ConfigType.SingleBasketball -> {
//                            PreSingleBasketballBusiness.instance()
//                                .onWindowStatusChange(wrapper, cResult)
//                        }
//
//                        PostConfigData.ConfigType.SingleFootball -> {
//                            PreSingleFootballBusiness.instance()
//                                .onWindowStatusChange(wrapper, cResult)
//                        }
//
//                        PostConfigData.ConfigType.MultiBasketball -> {
//                            PreMultiBasketballBusiness.instance()
//                                .onWindowStatusChange(wrapper, cResult)
//                        }
//
//                        PostConfigData.ConfigType.MultiFootball -> {
//                            PreMultiFootballBusiness.instance()
//                                .onWindowStatusChange(wrapper, cResult)
//                        }
//                    }
//                }
//            }
//
//            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
//                //足球 / 篮球页面都打开过，能找到 4 个按钮
//                //这是因为页面缓存 / 复用导致的，两个页面的 View 都存在于视图树中。
//
//                val eventNode = wrapper.event.source ?: return
//                Log.d(TAG, "dispatchTask: ----" + eventNode.transNodeInfoToNodeWrapper())
//                try {
//
//                } finally {
//                    eventNode.recycle()
//                }
//
//
//            }
//
//            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
//                //Log.d(TAG, "dispatchTask: TYPE_VIEW_SCROLLED---")
//            }
//
//            else -> {
//                //Log.d(TAG, "dispatchTask: else")
//            }
//        }
    }

    override fun onStart() {
        PreSingleBasketball.instance().start()
        PreSingleFootball.instance().start()
        PreMultiBasketball.instance().start()
        PreMultiFootball.instance().start()
    }

    override fun onDestroy() {
        PreSingleBasketball.instance().destroy()
        PreSingleFootball.instance().destroy()
        PreMultiBasketball.instance().destroy()
        PreMultiFootball.instance().destroy()
    }

}