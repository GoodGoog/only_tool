package com.example.more.customView.clickEvent

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.example.common.util.LogUtil
import com.example.common.util.showToast
import com.example.more.R
import com.example.more.databinding.MoreViewClickEnventBinding

/**
 * Created by zhangqy
 * Data : 2024/3/6
 */
class ClickEventView(mContext: Context, attrs: AttributeSet, defStyleAttr: Int) :
    FrameLayout(mContext, attrs, defStyleAttr) {

    companion object {

    }

    constructor(mContext: Context, attrs: AttributeSet) : this(mContext, attrs, 0)

    private val binding: MoreViewClickEnventBinding = DataBindingUtil.inflate(
        LayoutInflater.from(mContext), R.layout.more_view_click_envent, this, false
    )

    init {
        addView(binding.root)
        binding.vChild.setOnClickListener {
            logD("vChild被点击了")
        }
    }

    /**
     * 1、分发：dispatchTouchEvent;
     * 2、拦截：onInterceptTouchEvent；
     * 3、处理：onTouchEvent；
     */

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    //如果事件被消耗了，通常就返回true， 如果不做处理，则发挥false
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                logD("ClickEventView被点击了")
                showToast(mContext = context, "ClickEventView被点击了")
            }
            else -> {}
        }
        //return super.onTouchEvent(event)
        return false
    }

    private fun logD(msg: String) {
        LogUtil.d("ClickEventView", msg)
    }

}