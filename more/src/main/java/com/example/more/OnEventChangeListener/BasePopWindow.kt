package com.example.more.OnEventChangeListener

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BasePopWindow<VB : ViewDataBinding,B>(container : View, data: B,mWidth : Int,mHeight : Int,layoutId : Int) : PopupWindow(container.context) {

    private var binding : VB
    private var data: B

    init {
        width = mWidth
        height = mHeight
//        height = mWindow.decorView.height / 2
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DataBindingUtil.inflate(LayoutInflater.from(container.context)
            , layoutId,null,false)
        // val contentView = LayoutInflater.from(context).inflate(R.layout.more_window_pop_show,null,false)
        setContentView(binding.root)
        this.data = data
        initView(binding,data)
    }

    abstract fun initView(mBinding : VB,mData :B)

    //showAtLocation 相对于整个窗口

}
