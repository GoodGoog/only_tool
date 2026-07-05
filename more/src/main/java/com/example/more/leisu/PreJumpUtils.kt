package com.example.more.leisu

import android.graphics.Rect
import android.util.Log
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.LeisuServiceCenter
import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.findNodeByText
import com.example.more.accessibility.findNodesByExpression
import com.example.more.accessibility.findNodesByText
import com.example.more.leisu.data.PostConfigData
import kotlin.Unit
import kotlin.math.log

class PreJumpUtils private constructor() {
    companion object {
        const val TAB_TITLE_BASKETBALL = "篮球"
        const val TAB_TITLE_FOOTBALL = "足球"
        const val SUB_TAB_TITLE_SINGLE = "单关"
        const val SUB_TAB_TITLE_MULTI = "串关"

        private var instance: PreJumpUtils? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreJumpUtils {
            if (instance == null) {
                instance = PreJumpUtils()
            }
            return instance!!
        }
    }

    //每次进入到专家主页，无论是外部进入|比赛选择页退出，都设置为true
    //以至于下一次在信息列表选择页产生window_status_change事件时，判断是否需要跳转至对应的子页面
    //true 需要跳转, false反之
    var hasJumpExpertHomeAction = true

    //默认实在足球-单关页面
    var curPageType = PostConfigData.ConfigType.SingleFootball

    fun jumpSubPage(
        type: PostConfigData.ConfigType,
        result: AnalyzeSourceResult,
        doAfterFinish: () -> Unit
    ) {
        curPageType = type
        when (type) {
            PostConfigData.ConfigType.SingleBasketball -> {
                result.jump(TAB_TITLE_BASKETBALL, SUB_TAB_TITLE_SINGLE, doAfterFinish)
            }

            PostConfigData.ConfigType.SingleFootball -> {
                result.jump(TAB_TITLE_FOOTBALL, SUB_TAB_TITLE_SINGLE, doAfterFinish)
            }

            PostConfigData.ConfigType.MultiBasketball -> {
                result.jump(TAB_TITLE_BASKETBALL, SUB_TAB_TITLE_MULTI, doAfterFinish)
            }

            PostConfigData.ConfigType.MultiFootball -> {
                result.jump(TAB_TITLE_FOOTBALL, SUB_TAB_TITLE_MULTI, doAfterFinish)
            }
        }
    }

    fun AnalyzeSourceResult.jump(title: String, subTitle: String, doAfterFinish: () -> Unit) {
        findNodeByText(title).delayClickAndShowHighLight() {
            LeisuServiceCenter.instance().result.findNodesByExpression {
                subTitle == it.text && it.bounds.isLegal()
            }.nodes.let {
                var aimNode: NodeWrapper? = null
                if (it.size == 1) aimNode = it[0]
                if (it.size == 2) {
                    //result中排列顺序为 足球[单关，串关],篮球[单关串关]
                    //故当一个子按钮有两个节点时，足球选第一个，篮球选第二个
                    aimNode = if (curPageType == PostConfigData.ConfigType.SingleFootball || curPageType == PostConfigData.ConfigType.MultiFootball) {
                        it[0]
                    } else {
                        it[1]
                    }
                }
                aimNode?.let { lastNode ->
                    lastNode.delayClickAndShowHighLight { }
                }
            }
        }
    }
}