package com.example.more.leisu

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.accessibility.AccessibilityEvent
import com.example.more.EventBusTag
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.clickGestureWithResult
import com.example.more.accessibility.clickPerformWithResult
import com.example.more.accessibility.findNodeById
import com.example.more.accessibility.findNodesById
import com.example.more.leisu.data.IDPrePostHeader
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PostSingleBasketBallHandicapTypeData
import com.example.more.leisu.data.PostSingleBasketBallTotalScoreTypeData
import com.example.more.setting.judgeLeftTeamScoreTips
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.abs
import kotlin.random.Random

/**
 * 生成一个随机数
 */
fun getRandomInt(): Int {
    // [0~99] + 1 → [1~100]
    return Random.nextInt(100) + 1
}

/**
 * 提取字符串中数字
 */
fun String?.filterNumberOrZero(): Int {
    if (this == null) return 0
    if (this.isEmpty()) return 0
    return this.blankOrThis().filter {
        it.isDigit()
    }.toInt()
}

/**
 * 获取当前为周几
 */
fun getWeekDayByCalendar(): Int {
    val cal = Calendar.getInstance()
    val day = cal.get(Calendar.DAY_OF_WEEK)
    return when (day) {
        Calendar.SUNDAY -> 7
        Calendar.MONDAY -> 1
        Calendar.TUESDAY -> 2
        Calendar.WEDNESDAY -> 3
        Calendar.THURSDAY -> 4
        Calendar.FRIDAY -> 5
        Calendar.SATURDAY -> 6
        else -> 0
    }
}

/** dp转px */
fun Context.dpToPx(dpValue: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dpValue,
        resources.displayMetrics
    )
}

/** sp转px（文字专用） */
fun Context.spToPx(spValue: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        spValue,
        resources.displayMetrics
    )
}

/** dp转px */
fun Float.dpToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )
}

/** sp转px（文字专用） */
fun Float.spToPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        context.resources.displayMetrics
    )
}


/**
 * 获取dimens中的尺寸，并 dp -> px]
 */
fun Context.getPxFromDimens(resourceId: Int) =
    dpToPx(resources.getDimensionPixelSize(resourceId).toFloat())

/**
 * 为避免连续模拟点击过快，影响数据显示,故在延时实现点击
 * 点击时给被点击区域实现高光
 * 需要手势点击，不触发TYPE_VIEW_CLICKED
 * 临近页面切换时，尽量不适用手势切换，否则可能导致页面已经切换了才点击，导致点击失败
 */
@OptIn(DelicateCoroutinesApi::class)
fun NodeWrapper?.delayClickWithShowHighLight(
    gestureClick: Boolean = true,
    delayTime: Long = 1000,
    clickResult: ((Boolean) -> Unit)? = null
) {
    if (this == null) {
        clickResult?.invoke(false)
        return
    }
    //需要提前判断位置是否合法，可能会有异常地址 Rect(1654, 237 - 1080, 342) | Rect(1080, 229 - 1080, 349)
    if (!bounds.isLegal()) {
        clickResult?.invoke(false)
        return
    }
    LiveEventBus.get<Rect>(EventBusTag.EVENT_BUS_CLICKED_AREA_HIGH_LIGHT_BOX).post(this.bounds)
    GlobalScope.launch(Dispatchers.IO) {
        //协程非阻塞休眠，不用sleep
        //避免系统检测，让间隔时间浮动
        delay(delayTime - Random.nextInt(50))
        if (gestureClick) {
            //手势点击
            clickGestureWithResult { isSuccess ->
                clickResult?.invoke(isSuccess)
            }
        } else {
            //perform点击
            val isSuccess = clickPerformWithResult()
            clickResult?.invoke(isSuccess)
        }
    }
}


fun Rect?.isLegal(): Boolean {
    this?.let {
        return left < right && top < bottom
    }
    return false
}

fun Rect.toNewString(): String {
    return "Rect($left, $top , $right, $bottom)"
}


/**
 * ViewPager自带页面数据缓存机制，假如ViewPager有两个页面那么，当前处于页面2，而页面1被切换不可见
 *
 *一。如果页面1已经显示过，但是被切换，则任然能查找到页面1中，所有切换之前在窗口显示的控件节点
 *二。如果页面1未显示过，则只能查到到页面2的数据，页面1的数据为空
 */

/**
 * 获取当前显示在窗口李的比赛信息列表
 */
fun getCurPrePageMatchList(
    result: AnalyzeSourceResult,
    type: PostConfigData.ConfigType,
    isFilterTimeItem: Boolean = true,
    doAfterGetItemResult: (ArrayList<AnalyzeSourceResult>) -> Unit
) {
    result.getAimRecyclerViewNodeWrapper(type)?.let { curPageRvNode ->
        curPageRvNode.analyzeRecyclerView().let { itemResults ->
            ArrayList<AnalyzeSourceResult>().apply {
                itemResults.forEach {
                    if (isFilterTimeItem) {
                        //过滤日期标签item 周五  07月17日 26场 → com.leisu.sports:id/tv_header
                        if (!it.isCurItemTypeTimeFlags()) add(it)
                    } else {
                        add(it)
                    }
                }
            }.let { aimResults ->
                doAfterGetItemResult.invoke(aimResults)
            }
        }
    }
}

/**
 * 每个RecyclerView都有一种Item类型,就是某一条单独的时间栏目
 * eg:[android.widget.TextView → 周二  07月07日 4场 → com.leisu.sports:id/tv_header →  → Rect(1080, 882 - 1080, 978) →|| clickable = → true  → || scrollable → false
 */
fun AnalyzeSourceResult.getAimRecyclerViewNodeWrapper(type: PostConfigData.ConfigType): NodeWrapper? {
    var aimNodes: NodeWrapper? = null
    val rvNodes = findNodesById(IDPrePostHeader.id_league_lsit_recycler_view).nodes
    when (type) {
        PostConfigData.ConfigType.SingleFootball -> {
            if (rvNodes.size >= 2) {
                aimNodes = rvNodes[0]
            }
        }

        PostConfigData.ConfigType.MultiFootball -> {
            if (rvNodes.size >= 2) {
                aimNodes = rvNodes[1]
            }
        }

        PostConfigData.ConfigType.SingleBasketball -> {
            if (rvNodes.size == 4) {
                aimNodes = rvNodes[2]
            }
        }

        PostConfigData.ConfigType.MultiBasketball -> {
            if (rvNodes.size == 4) {
                aimNodes = rvNodes[3]
            }
        }
    }
    return aimNodes
}

/**
 * 判断当前Item是否为时间提示类型
 * android.widget.TextView → 周二  07月07日 4场 → com.leisu.sports:id/tv_header
 */
fun AnalyzeSourceResult.isCurItemTypeTimeFlags(): Boolean {
    findNodesById(IDPrePostHeader.id_recycler_view_item_time_flags).nodes.let {
        return it.isNotEmpty()
    }
}

/**
 * Rect手势点击
 */
@OptIn(DelicateCoroutinesApi::class)
fun Rect.delayClickWithShowHighLight(
    delayTime: Long = 1000,
    clickResult: ((Boolean) -> Unit)? = null
) {
    //需要提前判断位置是否合法，可能会有异常地址 Rect(1654, 237 - 1080, 342) | Rect(1080, 229 - 1080, 349)
    if (!this.isLegal()) {
        clickResult?.invoke(false)
        return
    }
    LiveEventBus.get<Rect>(EventBusTag.EVENT_BUS_CLICKED_AREA_HIGH_LIGHT_BOX).post(this)
    GlobalScope.launch(Dispatchers.IO) {
        //协程非阻塞休眠，不用sleep
        //避免系统检测，让间隔时间浮动
        delay(delayTime - Random.nextInt(50))
        clickGestureWithResult { isSuccess ->
            clickResult?.invoke(isSuccess)
        }
    }
}


fun Int.transToPostConfigType(): PostConfigData.ConfigType {
    return when (this) {
        0 -> {
            PostConfigData.ConfigType.SingleFootball
        }

        1 -> {
            PostConfigData.ConfigType.MultiFootball
        }

        2 -> {
            PostConfigData.ConfigType.SingleBasketball
        }

        else -> {
            PostConfigData.ConfigType.MultiBasketball
        }
    }
}

fun PostConfigData.ConfigType.transToPostArrayIndex(): Int {
    return when (this) {
        PostConfigData.ConfigType.SingleFootball -> {
            0
        }

        PostConfigData.ConfigType.MultiFootball -> {
            1
        }

        PostConfigData.ConfigType.SingleBasketball -> {
            2
        }

        PostConfigData.ConfigType.MultiBasketball -> {
            3
        }
    }
}

/**
 * 判断发布时间是否合法
 * 当前时间不能大于比赛开始时间 07/18 11:00
 */
fun isPostTimeLegal(startText: String): Boolean {
    if (startText.isEmpty()) return false
    val curYears = "2026"
    val startTime = "$curYears/$startText"
    val curTime = curYears + "/" + getCurrentTime()

    return compareYMdHmTime(startTime, curTime)
}

/**
 * 比较两个 MM/dd HH:mm 格式时间
 * @return -1：time1更早，0：相等，1：time1更晚
 */
fun compareYMdHmTime(time1: String, time2: String): Boolean {
    val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm")
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()

    cal1.time = sdf.parse(time1)
    cal2.time = sdf.parse(time2)

// 对比时间戳
    return cal1.timeInMillis > cal2.timeInMillis
}

/**
 * 获取当前为周几
 */
fun getCurrentTime(): String {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("MM/dd HH:mm")
    return sdf.format(calendar.time)
}

fun Int.transAccessibilityEventToString(): String {
    return when (this) {
        AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> "TYPE_WINDOW_STATE_CHANGED"
        AccessibilityEvent.TYPE_VIEW_CLICKED -> "TYPE_VIEW_CLICKED"
        AccessibilityEvent.TYPE_VIEW_SCROLLED -> "TYPE_VIEW_SCROLLED"
        else -> "else"
    }
}

/**
 * 通过result和tv的id,获取节点的text
 */
fun AnalyzeSourceResult.getTextById(tvId: String): String {
    return findNodeById(tvId)?.text.blankOrThis()
}

/**
 * 拼接分析的ai提问 , 左客队，右主队
 */
fun transToSingleHandicapAnalyseAiQuestion(data: PostSingleBasketBallHandicapTypeData) : String{
    data.apply {
        //受让情况
        val handicapText = if (data.leftPlate.toFloat() == 0F) {
            ""
        } else {
            "如果" + rightTeamName +
                    (if (rightPlate.toFloat() > 0F) "受让" else "让") +
                    "${abs(rightPlate.toFloat())}" + "分，"
        }
       return  "在" + leagueName + "赛事中，" +
                leftTeamName + "对阵" + rightTeamName + "，" +
                "分析比赛双方各自的近况和优劣势。" +
                handicapText +
                "预测最终哪个队伍更有可能获胜。" +
                "对每个球队的分析控制在一个大内容点之内，每个大点用（一、二、三、四、）等数字标识，" +
                "每一个大内容点内，小内容点用（1.2.3.4.）等标识， " +
                "全文不能有空白行，任意内容点之间都要换行。" +
                "答案控制在400字以内，结尾不要有任何无关提醒！" +
                "单独再给一个回答，为这篇文章生成一个充满激情与吸引力，并且不带确定性结果的标题，控制在25字以内。"
    }
}

/**
 * 拼接分析的ai提问 , 左客队，右主队
 */
fun transToSingleTotalScoreAnalyseAiQuestion(data: PostSingleBasketBallTotalScoreTypeData) : String{
    data.apply {
        //受让情况
       return "在" + leagueName + "赛事中，" +
                leftTeamName + "对阵" + rightTeamName + "，" +
                "分析比赛双方各自的近况和优劣势，" +
                "并预测双方总得分是否会大于" + totalScore + "。" +
                "对每个球队的分析控制在一个大内容点之内，每个大点用（一、二、三、四、）等数字标识，" +
                "每一个大内容点内，小内容点用（1.2.3.4.）等标识， " +
                "全文不能有空白行，任意内容点之间都要换行。" +
                "答案控制在400字以内，结尾不要有任何无关提醒！" +
                "单独再给一个回答，为这篇文章生成一个充满激情与吸引力，并且不带确定性结果的标题，控制在25字以内。"
    }

}