package com.example.more.leisu

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.util.TypedValue
import com.example.more.EventBusTag
import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.click
import com.jeremyliao.liveeventbus.LiveEventBus
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
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

/**
 * 获取dimens中的尺寸，并 dp -> px]
 */
fun Context.getPxFromDimens(resourceId: Int) =
    dpToPx(resources.getDimensionPixelSize(resourceId).toFloat())

/**
 * 为避免连续模拟点击过快，影响数据显示,故在延时实现点击
 * 点击时给被点击区域实现高光
 * 需要手势点击，不触发TYPE_VIEW_CLICKED
 */
@OptIn(DelicateCoroutinesApi::class)
fun NodeWrapper?.delayClickAndShowHighLight(
    delayTime: Long =500,
    doAfterEnd: () -> Unit
) {
    LiveEventBus.get<Rect?>(EventBusTag.EVENT_BUS_CLICKED_AREA_HIGH_LIGHT_BOX).post(this?.bounds)
    GlobalScope.launch(Dispatchers.IO) {
        //协程非阻塞休眠，不用sleep
        //避免系统检测，让间隔时间浮动
        delay(delayTime + Random.nextInt(50))
        click(gestureClick = true)
        //LiveEventBus.get<Rect?>(EventBusTag.EVENT_BUS_CLICKED_AREA_HIGH_LIGHT_BOX).post(null)
        //本次事件执行完毕之后
        doAfterEnd.invoke()
    }
}

fun Rect.toNewString(): String {
    return "Rect($left, $top , $right, $bottom)"
}
