package com.example.more.leisu.pre_post

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.LeisuServiceCenter
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.PreJumpUtils
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PostDataCenter
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlin.math.log

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

    init {
        // 注意：单例要用 observeForever，因为没有生命周期
        // 或者用 Application 的 lifecycle
        if (LeisuServiceCenter.instance().isAccessServiceConnect) {
            LiveEventBus.get<Int>(EventBusTag.TEST_PRE_POST_PAGE_SWITCH)
                .observe(this) {
                    val type = when (it) {
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
                    PreJumpUtils.instance()
                        .jumpSubPage(type, LeisuServiceCenter.instance().result) { isSuccess ->

                        }

                }
        }
    }

    fun dispatchTask(wrapper: EventWrapper, result: AnalyzeSourceResult) {
        LeisuServiceCenter.instance().result = result
        when (wrapper.event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                PostDataCenter.instance().postArray.let { postArray ->
                    if (postArray.isEmpty()) {
                        //所有文章发布结束
                        //do something
                        return
                    }

                    //UI分配
                    postArray[0].let { configData ->
                        //从专家主页进入比赛选择页，而不是从其他子页面退回赛事选择页，也要执行跳转
                        if (PreJumpUtils.instance().hasJumpExpertHomeAction) {
                            Log.d(TAG, "dispatchTask: 需要跳转" + configData)
                            PreJumpUtils.instance().hasJumpExpertHomeAction = false
                            PreJumpUtils.instance().jumpSubPage(configData.type, result) {
                            }
                        }
                    }

                    //业务分配
                    when (PreJumpUtils.instance().curPageType) {
                        PostConfigData.ConfigType.SingleBasketball -> {
                            PreSingleBasketballBusiness.instance()
                                .onWindowStatusChange(wrapper, result)
                        }

                        PostConfigData.ConfigType.SingleFootball -> {
                            PreSingleFootballBusiness.instance()
                                .onWindowStatusChange(wrapper, result)
                        }

                        PostConfigData.ConfigType.MultiBasketball -> {
                            PreMultiBasketballBusiness.instance()
                                .onWindowStatusChange(wrapper, result)
                        }

                        PostConfigData.ConfigType.MultiFootball -> {
                            PreMultiFootballBusiness.instance()
                                .onWindowStatusChange(wrapper, result)
                        }
                    }

                    //测试数据
                    //获取当前的recyclerView信息
                    //Log.d(TAG, "dispatchTask: ==================" + result)
                }
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                //足球 / 篮球页面都打开过，能找到 4 个按钮
                //这是因为页面缓存 / 复用导致的，两个页面的 View 都存在于视图树中。

                //如果只在足球页面，且不曾点开篮球页面，则只能找到2个单关+串关按钮，如果足球篮球页面都打开过，则能找到四个串关+单关按钮！！！！！！
                // wrapper.event.source获取的时发生点击事件控件的 父视图 节点

                val eventNode = wrapper.event.source ?: return

                try {
                    // 找到真正被点击的最底层节点
                    val clickedNode = findDeepestClickableNode(eventNode)
                    Log.d(TAG, "=== 原生事件打印 ===")
                    Log.d(TAG, "事件时间戳：${wrapper.event.eventTime}")
                    Log.d(TAG, "原始节点（可能是父视图）：${eventNode.text}, ${eventNode.className}")
                    Log.d(TAG, "实际点击节点（最底层）：${clickedNode?.text}, ${clickedNode?.className}")
                    Log.d(TAG, "节点ID：${clickedNode?.viewIdResourceName}")
                    Log.d(TAG, "节点包名：${clickedNode?.packageName}")

                    // 用 clickedNode 做后续处理
                    //handleClick(clickedNode)

                } finally {
                    eventNode.recycle()
                }

            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                //Log.d(TAG, "dispatchTask: TYPE_VIEW_SCROLLED---")
            }

            else -> {
                //Log.d(TAG, "dispatchTask: else")
            }
        }
    }

    /**
     * 找到最底层的可点击节点
     */
    private fun findDeepestClickableNode(node: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        // 如果当前节点不可点击，找它的子节点中可点击的
        if (!node.isClickable) {
            for (i in 0 until node.childCount) {
                val child = node.getChild(i) ?: continue
                if (child.isClickable) {
                    return findDeepestClickableNode(child)
                }
            }
        }

        // 如果当前节点可点击，继续往下找有没有更小的可点击子节点
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            if (child.isClickable) {
                return findDeepestClickableNode(child)
            }
        }

        return node
    }

    override fun onStart() {
        PreSingleBasketballBusiness.instance().start()
        PreSingleFootballBusiness.instance().start()
        PreMultiBasketballBusiness.instance().start()
        PreMultiFootballBusiness.instance().start()
    }

    override fun onDestroy() {
        PreSingleBasketballBusiness.instance().destroy()
        PreSingleFootballBusiness.instance().destroy()
        PreMultiBasketballBusiness.instance().destroy()
        PreMultiFootballBusiness.instance().destroy()
    }

}