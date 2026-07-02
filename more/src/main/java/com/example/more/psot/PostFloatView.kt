package com.example.more.psot

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.more.databinding.MoreWindowFloatPostContentViewBinding
import com.example.more.showToast

class PostFloatView(var mContext: Context, var attrs: AttributeSet, var defStyleAttr: Int) :
    ConstraintLayout(mContext, attrs, defStyleAttr) {

    private val binding : MoreWindowFloatPostContentViewBinding

    companion object {

    }

    //xml文件中使用此类需要两个参数的构造方法
    constructor(mContext: Context, attrs: AttributeSet) : this(mContext, attrs, 0)

    init {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),
            com.example.more.R.layout.more_window_float_post_content_view,this,false)
        addView(binding.root)
        initClick()
    }

    fun initClick(){
        binding.tvSure.setOnClickListener {
            mContext.showToast("tvSure")
        }
        binding.tvTestTouch.setOnClickListener {
            mContext.showToast("tvTestTouch")
        }
    }


}
