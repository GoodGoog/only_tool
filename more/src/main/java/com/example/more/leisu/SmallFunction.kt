package com.example.more.leisu

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.example.more.R
import com.example.more.accessibility.blankOrThis
import java.util.Calendar
import kotlin.random.Random

/**
 * 生成一个随机数
 */
fun getRandomInt() :Int{
    // [0~99] + 1 → [1~100]
    return Random.nextInt(100) + 1
}

/**
 * 提取字符串中数字
 */
fun String?.filterNumberOrZero(): Int{
    if (this == null) return 0
    if (this.isEmpty()) return 0
    return this.blankOrThis().filter {
        it.isDigit()
    }.toInt()
}

/**
 * 获取当前为周几
 */
fun getWeekDayByCalendar() : Int {
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
fun Context.getPxFromDimens(resourceId : Int) = dpToPx(resources.getDimensionPixelSize(resourceId).toFloat())

