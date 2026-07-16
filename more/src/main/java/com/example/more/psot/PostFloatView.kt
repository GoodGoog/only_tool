package com.example.more.psot

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.more.EventBusTag
import com.example.more.R
import com.example.more.databinding.MoreWindowFloatPostContentViewBinding
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PreDataCenter
import com.jeremyliao.liveeventbus.LiveEventBus

class PostFloatView(var mContext: Context, var attrs: AttributeSet, var defStyleAttr: Int) :
    FrameLayout(mContext, attrs, defStyleAttr) {

    private val binding: MoreWindowFloatPostContentViewBinding

    var isConnect: Boolean = false

    companion object {

    }

    //xml文件中使用此类需要两个参数的构造方法
    constructor(mContext: Context, attrs: AttributeSet) : this(mContext, attrs, 0)

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.more_window_float_post_content_view, this, false
        )
        addView(binding.root)

        binding.tvStartPost.setOnClickListener {
            if (isConnect){
                LiveEventBus.get<Boolean>(EventBusTag.START_POST).post(true)
            }
        }
    }

    fun setIsStartAccess(isStart: Boolean) {
        isConnect = isStart
        binding.apply {
            if (isStart) {
                ivTipsAccess.setImageResource(R.drawable.ic_access_status_green)
                tvTipsAccess.text = mContext.getText(R.string.float_access_tips_green_text)
                tvTipsAccess.setTextColor(mContext.getColor(R.color.post_float_window_tips_color_green))
            } else {
                ivTipsAccess.setImageResource(R.drawable.ic_access_status_red)
                tvTipsAccess.text = mContext.getText(R.string.float_access_tips_red_text)
                tvTipsAccess.setTextColor(mContext.getColor(R.color.post_float_window_tips_color_red))
                changeCurPostInfo(null)
            }
        }
    }

    fun quitWindowClicked(quit: () -> Unit) {
        binding.tvQuitWindow.setOnClickListener {
            quit.invoke()
        }
    }

    fun taskVisualizeClicked(quit: () -> Unit) {
        binding.tvTaskVisualize.setOnClickListener {
            quit.invoke()
        }
    }

    fun testPageSwitchClick(switch: (Int) -> Unit) {
        binding.tv1.addClick(0, switch)
        binding.tv2.addClick(1, switch)
        binding.tv3.addClick(2, switch)
        binding.tv4.addClick(3, switch)
    }

    fun TextView.addClick(pageIndex: Int, switch: (Int) -> Unit) {
        setOnClickListener {
            if (isConnect) {
                changeCurPostInfo(PreDataCenter.instance().postArray[pageIndex])
                switch(pageIndex)
            }
        }
    }

    fun changeCurPostInfo(data: PostConfigData?) {
        data?.apply {
            binding.tvCurExecuteTask.text = title + "|" + if (isFree) "免费" else "收费"
            binding.tvTaskRemainsTimes.text = "剩余发布次数:" + postTimes
            return
        }
        binding.tvCurExecuteTask.text = "xx|xx|xx"
        binding.tvTaskRemainsTimes.text = "剩余发布次数:x"
    }
}
