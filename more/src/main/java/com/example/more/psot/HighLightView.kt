package com.example.more.psot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.more.R
import com.example.more.leisu.dpToPx

class HighLightView(var mContext: Context, var attrs: AttributeSet, var defStyleAttr: Int) :
    FrameLayout(mContext, attrs, defStyleAttr) {


    companion object {
        const val TAG = "HighLightView"
    }


    // 需要框选的目标矩形
    var targetRect = RectF()

    var strokeColor = Color.RED // 方框颜色，适配深色悬浮窗

    // 描边画笔（只画线，不填充）
    private val strokePaint = Paint().apply {
        color = strokeColor
        strokeWidth = mContext.dpToPx(2f)
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeCap = Paint.Cap.ROUND
    }

    //xml文件中使用此类需要两个参数的构造方法
    constructor(mContext: Context, attrs: AttributeSet) : this(mContext, attrs, 0)

    init {
        alpha = 0.5f
        setBackgroundColor(mContext.getColor(R.color.post_float_window_tips_color_red))
        isFocusable = false
        isClickable = false
    }

    // 对外接口：传入Rect自动更新框选区域
    fun setTargetRect(rect: Rect) {
        targetRect.set(rect)
        Log.d(TAG, "setTargetRect: 来修改了" + rect)
        invalidate()
    }

    // 清除方框（隐藏）
    fun clearRect() {
        targetRect.setEmpty()
        invalidate()
    }

    // 动态修改边框颜色
    fun setStrokeColorRes(colorId: Int) {
        strokeColor = ContextCompat.getColor(context, colorId)
        strokePaint.color = strokeColor
        invalidate()
    }

//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        // 矩形无效直接跳过绘制
//        if (targetRect.isEmpty || targetRect.width() <= 0 || targetRect.height() <= 0) return
//        // 绘制直角矩形方框
//        canvas.drawRect(targetRect, strokePaint)
//    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 空矩形/有效尺寸不足直接跳过绘制
        if (targetRect.isEmpty || targetRect.height() <= 0) return

        // 关键：圆角半径 = 矩形高度的一半
        val radius = targetRect.height() / 2f

        // 绘制胶囊圆角矩形
        canvas.drawRoundRect(
            targetRect,
            radius,
            radius,
            strokePaint
        )
    }

}