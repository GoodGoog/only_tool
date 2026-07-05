package com.example.more.leisu

import android.graphics.Rect
import android.util.Log
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.LeisuServiceCenter
import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.findNodeByText
import com.example.more.accessibility.findNodesByExpression
import com.example.more.accessibility.findNodesByText
import com.example.more.accessibility.logD
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

    //是否跳子页面被点击了
    var isSubTitleClicked = false

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
                result.jump(result,TAB_TITLE_BASKETBALL, SUB_TAB_TITLE_SINGLE, doAfterFinish)
            }

            PostConfigData.ConfigType.SingleFootball -> {
                result.jump(result,TAB_TITLE_FOOTBALL, SUB_TAB_TITLE_SINGLE, doAfterFinish)
            }

            PostConfigData.ConfigType.MultiBasketball -> {
                result.jump(result,TAB_TITLE_BASKETBALL, SUB_TAB_TITLE_MULTI, doAfterFinish)
            }

            PostConfigData.ConfigType.MultiFootball -> {
                result.jump(result,TAB_TITLE_FOOTBALL, SUB_TAB_TITLE_MULTI, doAfterFinish)
            }
        }
    }

    fun AnalyzeSourceResult.jump(result: AnalyzeSourceResult,title: String, subTitle: String, doAfterFinish: () -> Unit) {
        //篮球/足球控件被设置clickable = → false,故点击蓝球/足球，其本身TextView不响应事件，并将事件传递给了父视图RelativeLayout
        //只要最终有控件响应了点击事件，最终都会触发TYPE_VIEW_CLICKED，只不过此时event.source[点击事件响应按钮]为篮球/足球的父视图RelativeLayout
        //不论手势点击还是perAction，最终都会触发TYPE_VIEW_CLICKED
        findNodeByText(title).delayClickAndShowHighLight(true) { isTitleClickSuccess ->
            if (!isTitleClickSuccess){
                Log.d("跳转页点击结果111", "jump: ${title}事件点击失败了")
                //点击失败
                return@delayClickAndShowHighLight
            }
            Log.d("跳转页点击结果111", "jump: ${title}事件点击成功了")
            result.findNodesByExpression {
                subTitle == it.text && it.bounds.isLegal()
            }.nodes.let {
                var aimNode: NodeWrapper? = null
                if (it.size == 1) aimNode = it[0]
                if (it.size == 2) {
                    //result中排列顺序为 足球[单关，串关],篮球[单关，串关]
                    //故当一个子按钮有两个节点时，足球选第一个，篮球选第二个
                    aimNode = if (curPageType == PostConfigData.ConfigType.SingleFootball || curPageType == PostConfigData.ConfigType.MultiFootball) {
                        it[0]
                    } else {
                        it[1]
                    }
                }
                aimNode?.let { lastNode ->
                    lastNode.delayClickAndShowHighLight(false) { isSubTitleClickSuccess ->
                        if (!isSubTitleClickSuccess){
                            Log.d("跳转页点击结果222", "jump: ${subTitle}事件点击失败了")
                            return@delayClickAndShowHighLight
                        }
                        Log.d("跳转页点击结果222", "jump: ${subTitle}事件点击成功了")
                    }
                }
            }
        }
    }
}