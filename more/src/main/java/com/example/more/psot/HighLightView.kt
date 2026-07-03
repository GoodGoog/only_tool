package com.example.more.psot

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout

class HighLightView(var mContext: Context, var attrs: AttributeSet, var defStyleAttr: Int) :
    FrameLayout(mContext, attrs, defStyleAttr) {


    companion object {

    }

    //xml文件中使用此类需要两个参数的构造方法
    constructor(mContext: Context, attrs: AttributeSet) : this(mContext, attrs, 0)

    init {
        alpha = 0.2f
        setBackgroundColor(Color.RED)
        isFocusable = false
        isClickable = false
    }
}