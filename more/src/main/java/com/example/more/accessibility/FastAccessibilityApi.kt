package com.example.more.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import androidx.annotation.RequiresApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Author: CoderPig
 * Date: 2023-03-24
 * Desc:
 */
/**
 * 无障碍服务是否可用
 * */
val isAccessibilityEnable get() = FastAccessibilityService.Companion.isServiceEnable

/**
 * 请求无障碍服务
 * @param autoJump 是否自动跳转无障碍设置页
 * */
fun requireAccessibility() = FastAccessibilityService.Companion.requireAccessibility()

/**
 * 全局操作快速调用
 * performAction 是针对 具体界面控件（AccessibilityNodeInfo）执行操作，如点击按钮、输入文本
 * performGlobalAction 则是 系统级全局操作，不依赖于特定控件，作用范围更广
 * */
fun performAction(action: Int) = FastAccessibilityService.Companion.require.performGlobalAction(action)

// 返回
fun back() = performAction(AccessibilityService.GLOBAL_ACTION_BACK)

// Home键
fun home() = performAction(AccessibilityService.GLOBAL_ACTION_HOME)

// 最近任务
fun recent() = performAction(AccessibilityService.GLOBAL_ACTION_RECENTS)

// 电源菜单
fun powerDialog() = performAction(AccessibilityService.GLOBAL_ACTION_POWER_DIALOG)

// 通知栏
fun notificationBar() = performAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS)

// 通知栏 → 快捷设置
fun quickSettings() = performAction(AccessibilityService.GLOBAL_ACTION_QUICK_SETTINGS)

// 锁屏
@RequiresApi(Build.VERSION_CODES.P)
fun lockScreen() = performAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN)

// 应用分屏
fun splitScreen() = performAction(AccessibilityService.GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN)

// 休眠
fun sleep(millis: Long) = Thread.sleep(millis)

/**
 * 结点操作快速调用
 * */
// 结点点击，现在很多APP屏蔽了结点点击，默认采用手势模拟
fun NodeWrapper?.click(gestureClick: Boolean = true, duration: Long = 200L) {
    if (this == null) return
    if (gestureClick) {
        bounds?.let {
            val x = ((it.left + it.right) / 2).toFloat()
            val y = ((it.top + it.bottom) / 2).toFloat()
            FastAccessibilityService.Companion.require.dispatchGesture(
                GestureDescription.Builder().apply {
                    addStroke(
                        GestureDescription.StrokeDescription(
                            Path().apply { moveTo(x, y) },
                            0L,
                            duration
                        )
                    )
                }.build(), object : AccessibilityService.GestureResultCallback() {
                    override fun onCompleted(gestureDescription: GestureDescription?) {
                        super.onCompleted(gestureDescription)
                        // 手势执行完成回调
                    }
                }, null
            )
        }
    } else {
        nodeInfo?.let {
            var depthCount = 0  // 查找最大深度
            var tempNode = it
            while (true) {
                if (depthCount < 10) {
                    if (tempNode.isClickable) {
                        if (duration >= 1000L) {
                            tempNode.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK)
                        } else {
                            tempNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        }
                        break
                    } else {
                        tempNode = tempNode.parent
                        depthCount++
                    }
                } else break
            }
        }
    }
}


/**
 * 为避免连续模拟点击过快，影响数据显示,故在延时实现点击
 */
@OptIn(DelicateCoroutinesApi::class)
fun NodeWrapper?.delayClick(delayTime: Long = 1500){
    GlobalScope.launch(Dispatchers.IO) {
        //协程非阻塞休眠，不用sleep
        //避免系统检测，让间隔时间浮动
        delay(delayTime + Random.nextInt(50))
        click()
    }
}

// 结点长按
fun NodeWrapper?.longClick(gestureClick: Boolean = true, duration: Long = 1000L) {
    if (this == null) return
    click(gestureClick, duration)
}

// 向前滑动
fun NodeWrapper?.scrollForward(isForward: Boolean = true) {
    if (this == null) return
    nodeInfo?.let {
        var depthCount = 0  // 查找最大深度
        var tempNode = it
        while (true) {
            if (depthCount < 10) {
                if (tempNode.isScrollable) {
                    tempNode.performAction(
                        if (isForward) AccessibilityNodeInfo.ACTION_SCROLL_FORWARD
                        else AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
                    )
                    Log.e("测试", "执行了")
                    break
                } else {
                    tempNode = tempNode.parent
                    depthCount++
                }
            } else break
        }
    }
}

// 向后滑动
fun NodeWrapper?.backward() = scrollForward(false)

// 从一个坐标点滑动到另一个坐标点
fun NodeWrapper?.swipe(
    startX: Int,
    startY: Int,
    endX: Int,
    endY: Int,
    duration: Long = 1000L
) {
    FastAccessibilityService.Companion.require.dispatchGesture(
        GestureDescription.Builder().apply {
            addStroke(GestureDescription.StrokeDescription(Path().apply {
                moveTo(startX.toFloat(), startY.toFloat())
                lineTo(endX.toFloat(), endY.toFloat())
            }, 0L, duration))
        }.build(), object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                super.onCompleted(gestureDescription)
                // 手势执行完成回调
            }
        }, null
    )
}

// 列表滑动一屏


// 文本填充
fun NodeWrapper?.input(content: String) {
    if (this == null) return
    nodeInfo?.let {
        var depthCount = 0  // 查找最大深度
        var tempNode = it
        while (true) {
            if (depthCount < 10) {
                if (tempNode.isEditable) {
                    tempNode.performAction(
                        AccessibilityNodeInfo.ACTION_SET_TEXT, Bundle().apply {
                            putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                                content
                            )
                        }
                    )
                    break
                } else {
                    tempNode = tempNode.parent
                    depthCount++
                }
            } else break
        }
    }
}


/**
 * 1.解析RecyclerView中的子内容
 * 2.获得包裹item的Layout
 * 3.获取item的layout包裹的子视图
 * 4.再通过item中对应控件的id，获取item控件的具体数值
 */
fun NodeWrapper?.analyseRecyclerViewSubView() {
    if (this == null) return
    if (nodeInfo == null) return
    nodeInfo?.let { mNodeInfo ->
        val childCount = mNodeInfo.childCount
        for (i in 0 until childCount) {
            val childNodeInfo = mNodeInfo.getChild(i)
            if (childNodeInfo != null) {
                Log.d(
                    "PostAccessibilityService",
                    "analyseRecyclerViewSubView: " + childNodeInfo.className.toString()
                )
                //也许child中还有child，需要递归
            }
        }
    }
}


/**
 * 结点解析结果快速调用
 * */

/**
 * 根据文本查找结点
 *
 * @param text 匹配的文本
 * @param textAllMatch 文本全匹配
 * @param includeDesc 同时匹配desc
 * @param descAllMatch desc全匹配
 * @param enableRegular 是否启用正则
 * */
fun AnalyzeSourceResult.findNodeByText(
    text: String,
    textAllMatch: Boolean = false,
    includeDesc: Boolean = false,
    descAllMatch: Boolean = false,
    enableRegular: Boolean = false,
): NodeWrapper? {
    if (enableRegular) {
        val regex = Regex(text)
        nodes.forEach { node ->
            if (!node.text.isNullOrBlank()) {
                if (regex.find(node.text!!) != null) return node
            }
            if (includeDesc && !node.description.isNullOrBlank()) {
                if (regex.find(node.description!!) != null) return node
            }
        }
    } else {
        nodes.forEach { node ->
            if (!node.text.isNullOrBlank()) {
                if (textAllMatch) {
                    if (text == node.text) return node
                } else {
                    if (node.text!!.contains(text)) return node
                }
            }
            if (includeDesc && !node.description.isNullOrBlank()) {
                if (descAllMatch) {
                    if (text == node.description) return node
                } else {
                    if (node.description!!.contains(text)) return node
                }
            }
        }
    }
    return null
}

/**
 * 根据文本查找结点列表
 *
 * @param text 匹配的文本
 * @param textAllMatch 文本全匹配
 * @param includeDesc 同时匹配desc
 * @param descAllMatch desc全匹配
 * @param enableRegular 是否启用正则
 * */
fun AnalyzeSourceResult.findNodesByText(
    text: String,
    textAllMatch: Boolean = false,
    includeDesc: Boolean = false,
    descAllMatch: Boolean = false,
    enableRegular: Boolean = false,
): AnalyzeSourceResult {
    val result = AnalyzeSourceResult()
    if (enableRegular) {
        val regex = Regex(text)
        nodes.forEach { node ->
            if (!node.text.isNullOrBlank()) {
                if (regex.find(node.text!!) != null) {
                    result.nodes.add(node)
                    return@forEach
                }
            }
            if (includeDesc && !node.description.isNullOrBlank()) {
                if (regex.find(node.description!!) != null) {
                    result.nodes.add(node)
                    return@forEach
                }
            }
        }
    } else {
        nodes.forEach { node ->
            if (!node.text.isNullOrBlank()) {
                if (textAllMatch) {
                    if (text == node.text) {
                        result.nodes.add(node)
                        return@forEach
                    }
                } else {
                    if (node.text!!.contains(text)) {
                        result.nodes.add(node)
                        return@forEach
                    }
                }
            }
            if (includeDesc && !node.description.isNullOrBlank()) {
                if (descAllMatch) {
                    if (text == node.description) {
                        result.nodes.add(node)
                        return@forEach
                    }
                } else {
                    if (node.description!!.contains(text)) {
                        result.nodes.add(node)
                        return@forEach
                    }
                }
            }
        }
    }
    return result
}

/**
 * 根据id查找结点 (模糊匹配)
 *
 * @param id 结点id
 * */
fun AnalyzeSourceResult.findNodeById(id: String): NodeWrapper? {
    nodes.forEach { node ->
        if (!node.id.isNullOrBlank()) {
            if (node.id!!.contains(id)) return node
        }
    }
    return null
}

/**
 * 根据id查找结点列表 (模糊匹配)
 *
 * @param id 结点id
 * */
fun AnalyzeSourceResult.findNodesById(id: String): AnalyzeSourceResult {
    val result = AnalyzeSourceResult()
    nodes.forEach { node ->
        if (!node.id.isNullOrBlank()) {
            if (node.id!!.contains(id)) result.nodes.add(node)
        }
    }
    return result
}

/**
 * 根据传入的表达式结果查找结点
 *
 * @param expression 匹配条件表达式
 * */
fun AnalyzeSourceResult.findNodeByExpression(expression: (NodeWrapper) -> Boolean): NodeWrapper? {
    nodes.forEach { node ->
        if (expression.invoke(node)) return node
    }
    return null
}

/**
 * 根据传入的表达式结果查找结点列表
 *
 * @param expression 匹配条件表达式
 * */
fun AnalyzeSourceResult.findNodesByExpression(expression: (NodeWrapper) -> Boolean): AnalyzeSourceResult {
    val result = AnalyzeSourceResult()
    nodes.forEach { node ->
        if (expression.invoke(node)) result.nodes.add(node)
    }
    return result
}

/**
 * 查找所有文本不为空的结点
 * */
fun AnalyzeSourceResult.findAllTextNode(includeDesc: Boolean = false): AnalyzeSourceResult {
    val result = AnalyzeSourceResult()
    nodes.forEach { node ->
        if (!node.text.isNullOrBlank()) {
            result.nodes.add(node)
            return@forEach
        }
        if (includeDesc && !node.description.isNullOrBlank()) {
            result.nodes.add(node)
            return@forEach
        }
    }
    return result
}

/**
 * 查找RecyclerView所有的Item-Layout节点，与Item-Layout节点所包裹的所有子视图
 */
fun NodeWrapper?.analyzeRecyclerView(): ArrayList<AnalyzeSourceResult>? {
    if (this == null) return null
    if (nodeInfo == null) return null
    val rvResults = ArrayList<AnalyzeSourceResult>()
    //获取RecyclerView的所有ItemLayout对应节点
    analyzeNextLevelSubView().forEachIndexed { layoutIndex, layoutWrapper ->
        //获取当前ItemLayout包裹的所有子视图节点
        rvResults.add(AnalyzeSourceResult(ArrayList<NodeWrapper>(), layoutWrapper))
        analyzeAllSubNode(layoutWrapper.nodeInfo, rvResults[layoutIndex].nodes)
    }
    return rvResults
}

/**
 * 只遍历当前节点的下一层子节点，不处理再往下的层次
 * */
fun NodeWrapper?.analyzeNextLevelSubView(): ArrayList<NodeWrapper> {
    val list = ArrayList<NodeWrapper>()
    if (this == null) return list
    nodeInfo?.let { it ->
        if (it.childCount > 0) {
            for (index in 0 until it.childCount) {
                val childNode = it.getChild(index) ?: return list
                list.add(childNode.transNodeInfoToNodeWrapper())
            }
        }
    }
    return list
}

/**
 * 递归遍历结点的方法
 * */
fun analyzeAllSubNode(node: AccessibilityNodeInfo?, list: ArrayList<NodeWrapper>) {
    if (node == null) return
    list.add(node.transNodeInfoToNodeWrapper())
    if (node.childCount > 0) {
        for (index in 0 until node.childCount) analyzeAllSubNode(node.getChild(index), list)
    }
}

/**
 * nodeInfo转换为NodeWrapper
 */
fun AccessibilityNodeInfo.transNodeInfoToNodeWrapper() : NodeWrapper {
    val bounds = Rect()
    this.getBoundsInScreen(bounds)
    return NodeWrapper(
        text = text.blankOrThis(),
        id = viewIdResourceName.blankOrThis(),
        bounds = bounds,
        className = className.blankOrThis(),
        description = contentDescription.blankOrThis(),
        clickable = isClickable,
        scrollable = isScrollable,
        editable = isEditable,
        nodeInfo = this
    )
}