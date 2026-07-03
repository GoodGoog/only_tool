package com.example.more.psot

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.more.EasyFloatTag
import com.example.more.R
import com.example.more.databinding.MoreWindowFloatPostBinding
import com.example.more.psot.PostActivity.Companion.TAG
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.utils.DisplayUtils.getStatusBarHeight

class FloatUtils private constructor() {
    companion object {
        private var instance: FloatUtils? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): FloatUtils {
            if (instance == null) {
                instance = FloatUtils()
            }
            return instance!!
        }

    }

    //按钮高光
    private fun showHighLightWindow(parent: ViewGroup) {
// 先销毁旧窗口
        if (EasyFloat.isShow(EasyFloatTag.FLOAT_WINDOW_HIGH_LIGHT_BOX)) {
            EasyFloat.dismiss(EasyFloatTag.FLOAT_WINDOW_HIGH_LIGHT_BOX)
        }

        // 延迟100ms再创建，避开dismiss异步销毁的时序冲突
        Handler(Looper.getMainLooper()).postDelayed({
            // 初始化全屏浮窗
            EasyFloat.with(parent.context)
                .setTag(EasyFloatTag.FLOAT_WINDOW_HIGH_LIGHT_BOX)
                //试了几种方法,只有直接输入布局才能实现事件向底层传递
                .setLayout(R.layout.more_window_high_light_box)
                .setMatchParent(true, true)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setDragEnable(false)
                .setAnimator(null)
                .registerCallback {
                    // 浮窗创建完成后，动态修改窗口参数
                    createResult { isCreated, msg, windowRootView ->
                        if (isCreated && windowRootView != null) {
                            windowRootView.post {
                                //全屏悬浮窗不接受任何事件
                                enableTouchThrough(windowRootView)
                                //悬浮窗覆盖状态栏
                                windowFullScreenStatusBar(windowRootView)
                            }
                        }
                    }
                }
                .show()
        }, 500)
    }

    fun showAllLeisuWindow(parent: ViewGroup) {
        showHighLightWindow(parent)
        showAccessTaskWindow(parent)
    }

    //
    private fun showAccessTaskWindow(parent: ViewGroup) {
        showHighLightWindow(parent)
        // 先销毁旧窗口
        if (EasyFloat.isShow(EasyFloatTag.FLOAT_WINDOW_ALL_APP_TAG)) {
            EasyFloat.dismiss(EasyFloatTag.FLOAT_WINDOW_ALL_APP_TAG)
        }
        val context = parent.context
        val mbinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.more_window_float_post, parent, false
        ) as MoreWindowFloatPostBinding
        mbinding.pvWindowContentView.apply {
            quitWindowClicked {
                instance().destroyAllLeisuWindow()
            }
            taskVisualizeClicked {
                EasyFloat.getFloatView(EasyFloatTag.FLOAT_WINDOW_HIGH_LIGHT_BOX)
                    ?.let { rootWindowView ->
                        val hlv: HighLightView =
                            rootWindowView.findViewById(R.id.hv_high_light_box)
                        hlv.showOrHideWindow(false)
                    }
            }
        }
        EasyFloat.with(context)
            .setTag(EasyFloatTag.FLOAT_WINDOW_ALL_APP_TAG)
            .setLayout(mbinding.root) { rootView ->

            }
            // 关键：禁止窗口铺满全屏，空白区域自动穿透点击底层
            .setMatchParent(widthMatch = false, heightMatch = false)
            // 开启拖动
            .setDragEnable(true)
            // 水平自动贴左右边缘（替代失效的setAutoEdge）
            .setSidePattern(SidePattern.RESULT_HORIZONTAL)
            // 对齐右侧垂直居中，横向偏移20px，纵向0
            .setGravity(Gravity.END or Gravity.CENTER_VERTICAL, 0, 0)
            // 仅当前页面显示，应用内浮窗无需悬浮权限
            .setShowPattern(ShowPattern.ALL_TIME)
            .show()
    }

    /**
     * 即时更新悬浮窗子控件内容
     */
    fun upAccessWindowContent(isStartAccess: Boolean) {
        //开启无障碍服务时： 显示弹窗 + 更新弹窗内容
        //关闭无障碍服务时 只要更新弹窗内容
        if (isStartAccess) {
            EasyFloat.show(EasyFloatTag.FLOAT_WINDOW_ALL_APP_TAG)
        }
        // 先判断浮窗是否存在
        if (!EasyFloat.isShow(EasyFloatTag.FLOAT_WINDOW_ALL_APP_TAG)) return
        // 获取浮窗根布局
        val root = EasyFloat.getFloatView(EasyFloatTag.FLOAT_WINDOW_ALL_APP_TAG) ?: return

        root.findViewById<PostFloatView>(R.id.pv_window_content_view).apply {
            setIsStartAccess(isStartAccess)
        }

    }

    //销毁所有雷速相关弹窗
    fun destroyAllLeisuWindow() {
        //销毁
        if (EasyFloat.isShow(EasyFloatTag.FLOAT_WINDOW_ALL_APP_TAG)) {
            EasyFloat.dismiss(EasyFloatTag.FLOAT_WINDOW_ALL_APP_TAG)
        }
        if (EasyFloat.isShow(EasyFloatTag.FLOAT_WINDOW_HIGH_LIGHT_BOX)) {
            EasyFloat.dismiss(EasyFloatTag.FLOAT_WINDOW_HIGH_LIGHT_BOX)
        }
    }

    /**
     * 外部传来了需要高光的按钮Rect
     */
    fun setHighLightRect(rect: Rect?) {
        EasyFloat.getFloatView(EasyFloatTag.FLOAT_WINDOW_HIGH_LIGHT_BOX)?.let { rootWindowView ->
            // 2. 获取自定义方框视图并全局持有
            val hlv: HighLightView =
                rootWindowView.findViewById(R.id.hv_high_light_box)
            //点击时的高光区域
            if (rect != null) {
                //不为空
                hlv.setTargetRect(rect)
            } else {
                //为空
                Log.d(TAG, "initListener: rect ==空")
            }
        }
    }


    /**
     * 全屏窗口覆盖状态栏
     */
    fun windowFullScreenStatusBar(windowRootView: View) {
        val context = windowRootView.context
        val wlp = windowRootView.layoutParams as WindowManager.LayoutParams
        val statusBarH = getStatusBarHeight(context)
        val dm = context.resources.displayMetrics
        val screenFullHeight = dm.heightPixels + statusBarH

        // 1. 重置坐标原点，不向上偏移y=0
        wlp.y = 0
        // 2. 核心Flag组合：必须同时保留NO_LIMITS + LAYOUT_IN_SCREEN
        wlp.flags = wlp.flags or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        // 移除冲突的FLAG_FULLSCREEN
        wlp.flags =
            wlp.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()

        // 3. 宽度=屏幕宽，高度=屏幕高度+状态栏高度（底部不会裁切）
        wlp.width = dm.widthPixels
        wlp.height = screenFullHeight

        // 刷新窗口参数生效
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.updateViewLayout(windowRootView, wlp)

    }

    /**
     * 全屏窗口不消费任何事件
     */
    private fun enableTouchThrough(view: View) {
        try {
            val wm = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val params = view.layoutParams as WindowManager.LayoutParams

            // 全套穿透标记
            params.flags = params.flags or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

            // Android12+ 穿透必须透明度≤0.8
            //params.alpha = 0.5f

            wm.updateViewLayout(view, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}