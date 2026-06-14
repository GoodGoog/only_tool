package com.example.more.team

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import com.example.common.util.showToast


class FloatingWindowService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: View

    private lateinit var layoutParams: WindowManager.LayoutParams

    override fun onBind(intent: Intent?): IBinder? {
        // Binding is not used in this example
        return null
    }

    override fun onCreate() {
        super.onCreate()
        try {
            windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

            val inflater = LayoutInflater.from(this)
            floatingView = inflater.inflate(com.example.more.R.layout.more_window_test, null)

            layoutParams = WindowManager.LayoutParams()
//          layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
//          layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.width = 400
            layoutParams.height = 400
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
//        layoutParams.format = PixelFormat.TRANSPARENT
            layoutParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH         // 2. 设置行为标志
            // 解释一些常用 flags:
            // FLAG_NOT_FOCUSABLE: 窗口不会获得输入焦点，点击事件会直接透传到下方的窗口。这对于不需要输入文本的悬浮窗非常有用，避免影响下层应用。
            // FLAG_NOT_TOUCH_MODAL: 窗口区域内的触摸事件由自己处理，区域外的事件传递给其他窗口。通常与 FLAG_NOT_FOCUSABLE 结合使用。
            // FLAG_WATCH_OUTSIDE_TOUCH: 允许接收窗口区域外的触摸事件（MotionEvent.ACTION_OUTSIDE）。

            // Initial position
            layoutParams.gravity = Gravity.TOP or Gravity.LEFT
            layoutParams.x = 100
            layoutParams.y = 100

            windowManager.addView(floatingView, layoutParams)
            setupClickListeners()
        } catch (e: Exception) {
            showToast(this, e.message)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun setupClickListeners() {
        // 关闭按钮
        floatingView.findViewById<View?>(com.example.more.R.id.iv_close).setOnClickListener {
            stopSelf()
        }


        // 悬浮窗主体点击事件
        floatingView.findViewById<View?>(com.example.more.R.id.layout_container_floating_windowr).setOnClickListener {

        }


        // 实现拖拽逻辑
        val dragArea = floatingView.findViewById<View?>(com.example.more.R.id.layout_container_floating_windowr) // 整个布局作为拖拽区域

        dragArea.setOnTouchListener(object : OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f

            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                when (event.getAction()) {
                    MotionEvent.ACTION_DOWN -> {
                        // 按下时记录初始位置
                        initialX = layoutParams.x
                        initialY = layoutParams.y
                        initialTouchX = event.getRawX()
                        initialTouchY = event.getRawY()
                        return true // 消费事件
                    }

                    MotionEvent.ACTION_MOVE -> {
                        // 计算移动的偏移量
                        val deltaX = (event.getRawX() - initialTouchX).toInt()
                        val deltaY = (event.getRawY() - initialTouchY).toInt()


                        // 更新窗口位置
                        layoutParams.x = initialX + deltaX
                        layoutParams.y = initialY + deltaY


                        // 刷新窗口
                        windowManager.updateViewLayout(floatingView, layoutParams)
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        // 抬起时，可以添加一些边界吸附效果（可选）
                        // 例如，如果靠近屏幕边缘，自动吸附到边缘
                        snapToEdge()
                        return true
                    }
                }
                return false
            }
        })

    }

    // 边界吸附效果示例
    private fun snapToEdge() {
        val screenWidth = getResources().getDisplayMetrics().widthPixels
        val middleX = screenWidth / 2


        // 如果当前位置在屏幕右半部分，吸附到右边；否则吸附到左边。
        if (layoutParams.x > middleX) {
            layoutParams.x = screenWidth - floatingView.getWidth() // 吸附到右边缘
        } else {
            layoutParams.x = 0 // 吸附到左边缘
        }
        windowManager.updateViewLayout(floatingView, layoutParams)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (floatingView != null) windowManager.removeView(floatingView)
    }

}