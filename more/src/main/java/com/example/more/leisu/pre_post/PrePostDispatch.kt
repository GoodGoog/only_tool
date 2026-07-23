package com.example.more.leisu.pre_post

import android.util.Log
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

    lateinit var result: AnalyzeSourceResult

    init {
        if (LeisuServiceCenter.instance().isAccessServiceConnect) {
            LiveEventBus.get<Int>(EventBusTag.TEST_PRE_POST_PAGE_SWITCH)
                .observe(this) { pageIndex ->
                    //允许自动发布
                    PreDataCenter.instance()
                        .setCurPrePageAllowAutoPost(pageIndex.transToPostConfigType(), true)
                    PreJumpUtils.instance().jumpSubPage(
                        pageIndex.transToPostConfigType(),
                    ) { isSuccess ->
                        //跳转成功
                        Log.d(TAG, "PrePostDispatch是否跳转成功$isSuccess")
                    }
                }

            LiveEventBus.get<Boolean>(EventBusTag.START_OR_STOP_CUR_AUTO_POST)
                .observe(this) { isStart ->
                    //开始 或 终止当前页面自动发布，作用与 发布页--->赛事列表页
                    if (isStart){
                        //发布
                        when (PreJumpUtils.instance().curPageType) {
                            PostConfigData.ConfigType.SingleFootball -> {
                                PreSingleFootball.instance()
                                    .startAutoPost(result)
                            }
                            PostConfigData.ConfigType.MultiFootball -> {
                                PreMultiFootball.instance()
                                    .startAutoPost(result)
                            }
                            PostConfigData.ConfigType.SingleBasketball -> {
                                PreSingleBasketball.instance()
                                    .startAutoPost(result)
                            }
                            PostConfigData.ConfigType.MultiBasketball -> {
                                PreMultiBasketball.instance()
                                    .startAutoPost(result)
                            }
                        }
                    }else {
                        //禁止自动发布
                        PreDataCenter.instance()
                            .setCurPrePageAllowAutoPost(PreJumpUtils.instance().curPageType, false)
                    }
                }

        }
    }

    override fun onEventCome(eventWrapper: EventWrapper, result: AnalyzeSourceResult) {
        Log.d("jumpSubTab", "onEventCome: 数据刷新了")
        this.result = result
        PreJumpUtils.instance().refreshResult(result)

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

    }

    override fun onDestroy() {
        PreSingleBasketball.instance().destroy()
        PreSingleFootball.instance().destroy()
        PreMultiBasketball.instance().destroy()
        PreMultiFootball.instance().destroy()

    }

    /***
     * 设置窗 事件 化接受间隔
     */
//    override fun getCurNeedReceptTimeSeparator(): BaseLeisuDispatch.Companion.TimeSeparator {
//        return BaseLeisuDispatch.Companion.TimeSeparator(
//            setOf(AccessibilityEvent.TYPE_VIEW_CLICKED),
//            500L
//        )
//    }
//

}