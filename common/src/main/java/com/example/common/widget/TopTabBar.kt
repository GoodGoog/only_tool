package com.example.common.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.example.common.R
import com.example.common.databinding.CommonTopTabBarBinding

/**
 * Created by zhangqy
 * Data : 2024/3/5
 */
class TopTabBar(var mContext: Context, var attrs: AttributeSet, var defStyleAttr: Int) :
    FrameLayout(mContext, attrs, defStyleAttr) {

    private val binding : CommonTopTabBarBinding

    companion object {

    }

    //xml文件中使用此类需要两个参数的构造方法
    constructor(mContext: Context, attrs: AttributeSet) : this(mContext, attrs, 0)

    init {
        binding = DataBindingUtil.inflate(LayoutInflater.from(mContext),R.layout.common_top_tab_bar,this,false)
        addView(binding.root)
    }



}
