package com.example.more.touch_service

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Author: CoderPig
 * Date: 2023-03-24
 * Desc: 实体包装类
 */
data class EventWrapper(
    var packageName: String,
    var className: String,
    var eventType: Int
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
    override fun toString() = "$className → $text → $id → $description → $bounds → $clickable → $scrollable → $editable"
}


data class AnalyzeSourceResult(
    //此处2026-6-25 ， val ——> var ，后果不明
    val nodes: ArrayList<NodeWrapper> = arrayListOf(),
    //包含子视图的视图节点 ， 在RecyclerView中为对应的layout_container,用来响应点击item
    val parentNode : NodeWrapper? = null
)