package com.example.more.psot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.more.R
import com.example.more.leisu.dpToPx

class HighLightView(var mContext: Context, var attrs: AttributeSet, var defStyleAttr: Int) :
    View(mContext, attrs, defStyleAttr) {


    companion object {
        const val TAG = "HighLightView"
    }

    //是否隐藏当前窗口
    var isShowCurWindow = true

    // 需要框选的目标矩形
    var targetRect = RectF()

    var strokeColor = Color.BLUE // 方框颜色，适配深色悬浮窗

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
        setBackgroundColor(mContext.getColor(R.color.post_float_window_tips_color_red))
        isFocusable = false
        isClickable = false
        showOrHideWindow(true)

        // 必须开启硬件离屏缓冲，Xfermode镂空才会生效
        setLayerType(LAYER_TYPE_HARDWARE, null)
    }

    // 对外接口：传入Rect自动更新框选区域
    fun setTargetRect(rect: Rect) {
        targetRect.set(rect)
        invalidate()
    }

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

    /**
     * 通过控制窗口的透明度来实现全屏窗口显示 或者 隐藏
     * 传来，是否需要更新
     */
    fun showOrHideWindow(isInInit: Boolean) {
        if (!isInInit) isShowCurWindow = !isShowCurWindow
        alpha = if (isShowCurWindow) {
            0.3f
        } else {
            //完全透明
            0f
        }
    }


}