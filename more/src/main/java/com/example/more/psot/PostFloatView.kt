package com.example.more.psot

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.common.util.showToast
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

    //客复制的ai提问
    var aiQuestion: String = ""

    companion object {

    }

    //xml文件中使用此类需要两个参数的构造方法
    constructor(mContext: Context, attrs: AttributeSet) : this(mContext, attrs, 0)

    val bgTvNormal = Color.parseColor("#E0E0E0")
    val bgTvAfterClick = Color.parseColor("#FF6347")
    val bgTvBeforeClick = Color.parseColor("#03A89E")

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.more_window_float_post_content_view, this, false
        )
        addView(binding.root)

//        binding.tvStopAutoPost.setOnClickListener {
//            if (isConnect){
//                LiveEventBus.get<Boolean>(EventBusTag.START_OR_STOP_CUR_AUTO_POST).post(false)
//            }
//        }
//        binding.tvStartAutoPost.setOnClickListener {
//            if (isConnect) {
//                LiveEventBus.get<Boolean>(EventBusTag.START_OR_STOP_CUR_AUTO_POST).post(true)
//            }
//        }
        binding.tvCopyAiQuestion.setOnClickListener {
            //复制文本
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("aiQuestion", aiQuestion)
            clipboardManager.setPrimaryClip(clipData)

            binding.tvCopyAiQuestion.setBackgroundColor(bgTvAfterClick)
            // 提交延时任务
            handler.postDelayed({
                //一秒后执行 按钮背景颜色复原
                binding.tvCopyAiQuestion.setBackgroundColor(bgTvNormal)
            }, 500)
        }

    }

    /**
     * 开启服务
     */
    fun setIsStartAccess(isStart: Boolean) {
        isConnect = isStart
        binding.apply {
            if (isStart) {
                ivTipsAccess.setImageResource(R.drawable.ic_access_status_green)
                //tvTipsAccess.text = mContext.getText(R.string.float_access_tips_green_text)
                //tvTipsAccess.setTextColor(mContext.getColor(R.color.post_float_window_tips_color_green))
            } else {
                ivTipsAccess.setImageResource(R.drawable.ic_access_status_red)
                //tvTipsAccess.text = mContext.getText(R.string.float_access_tips_red_text)
                //tvTipsAccess.setTextColor(mContext.getColor(R.color.post_float_window_tips_color_red))
                changeCurPostInfo(null)
            }
        }
    }

    fun quitWindowClicked(quit: () -> Unit) {
        binding.tvQuitWindow.setOnClickListener {
            quit.invoke()
        }
    }

    fun refreshAiQuestion(aiQuestion: String) {
        this.aiQuestion = aiQuestion
        binding.tvCopyAiQuestion.setBackgroundColor(bgTvBeforeClick)
        // 提交延时任务
        handler.postDelayed({
            //一秒后执行 按钮背景颜色复原
            binding.tvCopyAiQuestion.setBackgroundColor(bgTvNormal)
        }, 500)
    }

//    fun taskVisualizeClicked(quit: () -> Unit) {
//        binding.tvStartAutoPost.setOnClickListener {
//            quit.invoke()
//        }
//    }

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
        if (data == null) {
            binding.tvCurExecuteTask.text = "xx|xx|xx"
            //binding.tvTaskRemainsTimes.text = "剩余发布次数:x"
        } else {
            //binding.tvCurExecuteTask.text = title + "|" + if (isFree) "免费" else "收费"
            //binding.tvTaskRemainsTimes.text = "剩余发布次数:" + postTimes
            binding.tvCurExecuteTask.text = data.title + "ing"

            //变换颜色提醒一下
            binding.tvCurExecuteTask.setBackgroundColor(bgTvBeforeClick)
            // 提交延时任务
            handler.postDelayed({
                //一秒后执行 按钮背景颜色复原
                binding.tvCurExecuteTask.setBackgroundColor(bgTvNormal)
            }, 500)
            return
        }

    }
}
