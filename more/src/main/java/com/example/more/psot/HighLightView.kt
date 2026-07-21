package com.example.more.psot

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.example.more.R
import com.example.more.leisu.dpToPx
import com.google.android.material.internal.ViewUtils.dpToPx

class HighLightView @JvmOverloads constructor(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(mContext, attrs, defStyleAttr) {

    companion object {
        const val TAG = "HighLightView"
    }

    @get:JvmName("getViewHandler")
    val handler = Handler(Looper.getMainLooper())
    // 把延时任务保存成变量
    val delayTask = Runnable {
        // 延迟执行代码
        // 显示时间已足够，清除高光框
        setTargetRect(Rect(0,0,1,1))
        invalidate()
    }

    var isShowCurWindow = true
    var targetRect = RectF()
    var strokeColor = Color.RED

    // 遮罩画笔：镂空以外区域填充半黑
    private val maskPaint = Paint().apply {
        //暗红色填充屏幕
        color = Color.argb(90, 90, 0, 0)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    // 擦除画笔，镂空核心
    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        isAntiAlias = true
    }

    // 边框画笔
    private val strokePaint = Paint().apply {
        color = Color.parseColor("#FF5555")
        strokeWidth = context.dpToPx(3f)
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    init {
        // 1. 必须开启，保证onDraw正常执行
        setWillNotDraw(false)
        // 2. Xfermode镂空必备硬件缓冲
        setLayerType(LAYER_TYPE_HARDWARE, null)
        // 重点：删除全局alpha！破坏镂空
        // alpha = 0.5f 删掉！
        isFocusable = false
        isClickable = false
        showOrHideWindow(true)
    }

    fun setTargetRect(rect: Rect) {
        // 需要终止时调用
        handler.removeCallbacks(delayTask)

        //直接画
        targetRect.set(rect)
        invalidate()
        //特定时间之后擦除已经画好的框
        // 提交延时任务

        handler.postDelayed(delayTask, 500)
    }

    fun clearRect() {
        targetRect.setEmpty()
        invalidate()
    }

    fun setStrokeColorRes(colorId: Int) {
        strokeColor = ContextCompat.getColor(context, colorId)
        strokePaint.color = strokeColor
        invalidate()
    }

    // 启用镂空完整绘制方法，取消注释，删掉旧的只画边框代码
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        // 第一步：铺满全屏遮罩
        canvas.drawRect(0f, 0f, w, h, maskPaint)

        if (targetRect.isEmpty || targetRect.height() <= 0) return
        var radius = targetRect.height() / 2f
        //设置最大只能时50dp
        radius = if (radius > 20f.dpToPx(context)){
            20f.dpToPx(context)
        } else{
            radius
        }

        // 第二步：擦除目标矩形，镂空透明穿透底层
        canvas.drawRoundRect(targetRect, radius, radius, clearPaint)

        // 第三步：绘制边框
        canvas.drawRoundRect(targetRect, radius, radius, strokePaint)
    }

    // 显隐改用Visibility，禁止使用View.alpha
    fun showOrHideWindow(isInInit: Boolean) {
        if (!isInInit) isShowCurWindow = !isShowCurWindow
        visibility = if (isShowCurWindow) View.VISIBLE else View.INVISIBLE
    }
}

//class HighLightView(var mContext: Context, var attrs: AttributeSet, var defStyleAttr: Int) :
//    View(mContext, attrs, defStyleAttr) {
//
//
//    companion object {
//        const val TAG = "HighLightView"
//    }
//
//    //是否隐藏当前窗口
//    var isShowCurWindow = true
//
//    // 需要框选的目标矩形
//    var targetRect = RectF()
//
//    var strokeColor = Color.BLUE // 方框颜色，适配深色悬浮窗
//
//    // 描边画笔（只画线，不填充）
//    private val strokePaint = Paint().apply {
//        color = strokeColor
//        strokeWidth = mContext.dpToPx(2f)
//        style = Paint.Style.STROKE
//        isAntiAlias = true
//        strokeCap = Paint.Cap.ROUND
//    }
//
//    //xml文件中使用此类需要两个参数的构造方法
//    constructor(mContext: Context, attrs: AttributeSet) : this(mContext, attrs, 0)
//
//    init {
//        setBackgroundColor(mContext.getColor(R.color.post_float_window_tips_color_red))
//        isFocusable = false
//        isClickable = false
//        showOrHideWindow(true)
//
//        // 必须开启硬件离屏缓冲，Xfermode镂空才会生效
//        setLayerType(LAYER_TYPE_HARDWARE, null)
//    }
//
//    // 对外接口：传入Rect自动更新框选区域
//    fun setTargetRect(rect: Rect) {
//        targetRect.set(rect)
//        invalidate()
//    }
//
//        override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        // 空矩形/有效尺寸不足直接跳过绘制
//        if (targetRect.isEmpty || targetRect.height() <= 0) return
//
//        // 关键：圆角半径 = 矩形高度的一半
//        val radius = targetRect.height() / 2f
//
//        // 绘制胶囊圆角矩形
//        canvas.drawRoundRect(
//            targetRect,
//            radius,
//            radius,
//            strokePaint
//        )
//    }
//
//    /**
//     * 通过控制窗口的透明度来实现全屏窗口显示 或者 隐藏
//     * 传来，是否需要更新
//     */
//    fun showOrHideWindow(isInInit: Boolean) {
//        if (!isInInit) isShowCurWindow = !isShowCurWindow
//        alpha = if (isShowCurWindow) {
//            0.3f
//        } else {
//            //完全透明
//            0f
//        }
//    }
//
//
//}