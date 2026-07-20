package com.example.more.leisu.pre_post

import android.view.accessibility.AccessibilityEvent
import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.LeisuServiceCenter
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.PreJumpUtils
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.example.more.leisu.post_detail.PostSingleBasketball
import com.example.more.leisu.post_detail.PostSingleFootball
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

    override fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {

        LeisuServiceCenter.instance().result = result

        when (PreJumpUtils.instance().curPageType) {
            PostConfigData.ConfigType.SingleBasketball -> {
                PreSingleBasketball.instance()
                    .eventCome(eventWrapper, result)
            }

            PostConfigData.ConfigType.SingleFootball -> {
                PreSingleFootball.instance()
                    .eventCome(eventWrapper, result)
            }

            PostConfigData.ConfigType.MultiBasketball -> {
                PreMultiBasketball.instance()
                    .eventCome(eventWrapper, result)
            }

            PostConfigData.ConfigType.MultiFootball -> {
                PreMultiFootball.instance()
                    .eventCome(eventWrapper, result)
            }
        }
    }

    override fun onStart() {
        PreSingleBasketball.instance().start()
        PreSingleFootball.instance().start()
        PreMultiBasketball.instance().start()
        PreMultiFootball.instance().start()

        //发布页
        PostSingleBasketball.instance().start()
        PostSingleFootball.instance().start()
    }

    override fun onDestroy() {
        PreSingleBasketball.instance().destroy()
        PreSingleFootball.instance().destroy()
        PreMultiBasketball.instance().destroy()
        PreMultiFootball.instance().destroy()

        //发布页
        PostSingleBasketball.instance().destroy()
        PostSingleFootball.instance().destroy()

    }

    /***
     * 设置窗 事件 化接受间隔
     */
    override fun getCurNeedReceptTimeSeparator(): BaseLeisuDispatch.Companion.TimeSeparator {
        return BaseLeisuDispatch.Companion.TimeSeparator(setOf(AccessibilityEvent.TYPE_VIEW_CLICKED),500L)
    }


}