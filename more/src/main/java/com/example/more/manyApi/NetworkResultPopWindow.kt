package com.example.more.manyApi

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import com.example.more.R
import com.example.more.databinding.MoreWindowNetworkResultBinding

class NetworkResultPopWindow(container : View,any: Any) : PopupWindow(container.context) {

    private var binding : MoreWindowNetworkResultBinding
    private var any: Any

    init {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
//        height = mWindow.decorView.height / 2
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DataBindingUtil.inflate(LayoutInflater.from(container.context)
            , R.layout.more_window_network_result,null,false)
        // val contentView = LayoutInflater.from(context).inflate(R.layout.more_window_pop_show,null,false)
        setContentView(binding.root)
        this.any = any
        initView()
    }

    fun initView() {
        binding.etResultShow.setText(any.toString())
    }

}