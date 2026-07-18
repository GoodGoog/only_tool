package com.example.more.leisu

import android.graphics.Rect
import android.util.Log
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.LeisuServiceCenter
import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.clickGestureWithResult
import com.example.more.accessibility.clickPerformWithResult
import com.example.more.accessibility.findNodeByText
import com.example.more.accessibility.findNodesByExpression
import com.example.more.accessibility.findNodesByText
import com.example.more.accessibility.logD
import com.example.more.leisu.data.PostConfigData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.Unit
import kotlin.math.log
import kotlin.random.Random

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

    //一次记录 篮球 足球  单关 串关 四类按钮的Rect
    val jumpButtonRectArray = ArrayList<Rect>().apply {
        add(Rect(0,0,0,0))
        add(Rect(0,0,0,0))
        add(Rect(0,0,0,0))
        add(Rect(0,0,0,0))
    }

    //默认实在足球-单关页面
    var curPageType = PostConfigData.ConfigType.SingleFootball

    //当前赛事页面中Item俯容器ViewGroup的左右边界(left, right)
    private var itemLeft = 20
    private var itemRight = 20

    fun setWindowWidth(windowWidth : Int){
        itemRight = windowWidth - itemLeft
    }

    fun getCurItemRect(rect: Rect) : Rect{
        return Rect(itemLeft,rect.top,itemRight,rect.bottom)
    }

    fun jumpSubPage(
        type: PostConfigData.ConfigType,
        result: AnalyzeSourceResult,
        clickResult: (Boolean) -> Unit
    ) {
        curPageType = type
        when (type) {
            PostConfigData.ConfigType.SingleBasketball -> {
                result.jump(result, TAB_TITLE_BASKETBALL, SUB_TAB_TITLE_SINGLE, clickResult)
            }

            PostConfigData.ConfigType.SingleFootball -> {
                result.jump(result, TAB_TITLE_FOOTBALL, SUB_TAB_TITLE_SINGLE, clickResult)
            }

            PostConfigData.ConfigType.MultiBasketball -> {
                result.jump(result, TAB_TITLE_BASKETBALL, SUB_TAB_TITLE_MULTI, clickResult)
            }

            PostConfigData.ConfigType.MultiFootball -> {
                result.jump(result, TAB_TITLE_FOOTBALL, SUB_TAB_TITLE_MULTI, clickResult)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun AnalyzeSourceResult.jump(
        result: AnalyzeSourceResult,
        title: String,
        subTitle: String,
        clickResult: (Boolean) -> Unit
    ) {
        //篮球/足球控件被设置clickable = → false,故点击蓝球/足球，其本身TextView不响应事件，并将事件传递给了父视图RelativeLayout
        //只要最终有控件响应了点击事件，最终都会触发TYPE_VIEW_CLICKED，只不过此时event.source[点击事件响应按钮]为篮球/足球的父视图RelativeLayout
        //不论手势点击还是perAction，最终都会触发TYPE_VIEW_CLICKED
        findNodeByText(title).delayClickWithShowHighLight(false) { isTitleClickSuccess ->
            if (!isTitleClickSuccess) {
                Log.d("跳转页点击结果111", "jump: ${title}事件点击失败了")
                clickResult.invoke(false)
                //点击失败
            } else {
                Log.d("跳转页点击结果111", "jump: ${title}事件点击成功了")
                GlobalScope.launch(Dispatchers.IO) {
                    //延时点击
                    delay(500)
                    result.jumpSubTab(subTitle, clickResult)
                }
            }
        }
    }

    fun AnalyzeSourceResult.jumpSubTab(
        subTitle: String,
        clickResult: (Boolean) -> Unit
    ) {
        findNodesByExpression {
            subTitle == it.text && it.bounds.isLegal()
        }.nodes.let {
            var aimNode: NodeWrapper? = null
            if (it.size == 1) aimNode = it[0]
            if (it.size == 2) {
                //result中排列顺序为 足球[单关，串关],篮球[单关，串关]
                //故当一个子按钮有两个节点时，足球选第一个，篮球选第二个
                aimNode =
                    if (curPageType == PostConfigData.ConfigType.SingleFootball || curPageType == PostConfigData.ConfigType.MultiFootball) {
                        it[0]
                    } else {
                        it[1]
                    }
            }
            aimNode?.let { lastNode ->
                lastNode.delayClickWithShowHighLight(false) { isSubTitleClickSuccess ->
                    if (!isSubTitleClickSuccess) {
                        Log.d("跳转页点击结果222", "jump: ${subTitle}事件点击失败了")
                        clickResult.invoke(false)
                    } else {
                        Log.d("跳转页点击结果222", "jump: ${subTitle}事件点击成功了")
                        clickResult.invoke(true)
                    }
                }
            }
        }
    }
}