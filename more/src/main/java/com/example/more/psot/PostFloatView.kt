package com.example.more.psot

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.more.R
import com.example.more.databinding.MoreWindowFloatPostContentViewBinding

class PostFloatView(var mContext: Context, var attrs: AttributeSet, var defStyleAttr: Int) :
    FrameLayout(mContext, attrs, defStyleAttr) {

    private val binding: MoreWindowFloatPostContentViewBinding

    var isStart: Boolean = false

    companion object {

    }

    //xml文件中使用此类需要两个参数的构造方法
    constructor(mContext: Context, attrs: AttributeSet) : this(mContext, attrs, 0)

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            com.example.more.R.layout.more_window_float_post_content_view, this, false
        )
        addView(binding.root)

    }

    fun setIsStartAccess(isStart: Boolean){
        binding.apply {
            if (isStart) {
                ivTipsAccess.setImageResource(R.drawable.ic_access_status_green)
                tvTipsAccess.text = mContext.getText(R.string.float_access_tips_green_text)
                tvTipsAccess.setTextColor(mContext.getColor(R.color.post_float_window_tips_color_green))
            } else {
                ivTipsAccess.setImageResource(R.drawable.ic_access_status_red)
                tvTipsAccess.text = mContext.getText(R.string.float_access_tips_red_text)
                tvTipsAccess.setTextColor(mContext.getColor(R.color.post_float_window_tips_color_red))
            }
        }
    }

    fun quitWindowClicked(quit : () -> Unit){
        binding.tvQuitWindow.setOnClickListener {
            quit.invoke()
        }
    }

    fun taskVisualizeClicked(quit : () -> Unit){
        binding.tvTaskVisualize.setOnClickListener {
            quit.invoke()
        }
    }

    fun testPageSwitchClick(switch :(Int) -> Unit){
        binding.tv1.addClick(switch)
        binding.tv2.addClick(switch)
        binding.tv3.addClick(switch)
        binding.tv4.addClick(switch)
    }

    fun TextView.addClick(switch :(Int) -> Unit){
        setOnClickListener {
            switch(text.toString().toInt())
        }
    }
}
