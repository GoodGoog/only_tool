package com.example.more.accessibility

import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Author: CoderPig
 * Date: 2023-03-24
 * Desc: 实体包装类
 */
data class EventWrapper(
    var packageName: String,
    var className: String,
    var eventType: Int,
    var event: AccessibilityEvent
) {
    override fun toString() = "$packageName → $className → $eventType"
}


/**
 * 视图结点包装类
 * */
data class NodeWrapper(
    var text: String? = null,
    var id: String? = null,
    var bounds: Rect? = null,
    var className: String,
    var description: String? = null,
    var clickable: Boolean = false,
    var scrollable: Boolean = false,
    var editable: Boolean = false,
    var nodeInfo: AccessibilityNodeInfo? = null
) {
    //直接打印某个类的对象时，系统会自动调用这个类对应的toString方法
    override fun toString() = "$className → $text → $id → $description → $bounds → $clickable → $scrollable → $editable \n"
}


data class AnalyzeSourceResult(
    val nodes: ArrayList<NodeWrapper> = arrayListOf(),
    //包含子视图的视图节点 ， 在RecyclerView中为对应的layout_container,用来响应点击item，nodes的父节点
    val parentNode : NodeWrapper? = null
)