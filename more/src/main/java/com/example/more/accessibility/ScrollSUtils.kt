package com.example.more.accessibility

import android.graphics.Path
import android.view.accessibility.AccessibilityEvent
import kotlin.math.abs

class ScrollUtils {
    companion object {

        private var instance: ScrollUtils? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): ScrollUtils {
            if (instance == null) {
                instance = ScrollUtils()
            }
            return instance!!
        }

        const val TAG = "ScrollUtils"
    }

    var event : EventWrapper? = null

    //默认无效滚动
    var curDirection = ScrollDirection.DirectionInvalid

    private val horizontalKeyword = setOf(
        "Horizontal", "ViewPager", "HorizontalScrollView", "ViewPager2"
    )

    /**
     * 子业务调用，结合实际业务，进一步判断是否滚动有效
     */
    fun isValidScroll(): ScrollDirection {
        return curDirection
    }

    /**
     * 提前过滤无效滚动,避免消耗内存
     */
    fun isScrollInvalid(eventWrapper: EventWrapper) : ScrollDirection{
        event = eventWrapper
        val className = eventWrapper.event.source.transNodeInfoToNodeWrapper().className
        // 2. 黑名单直接过滤横向容器
        if (horizontalKeyword.any { className.contains(it, ignoreCase = true) }) {
            curDirection = ScrollDirection.DirectionInvalid
        }else{
            curDirection = ScrollDirection.DirectionValid
        }
        return curDirection
    }

}

enum class ScrollDirection {
    //竖向
    DirectionY,

    //横向
    DirectionX,
    //无效
    DirectionInvalid,
    //所有有效类型
    DirectionValid
}
