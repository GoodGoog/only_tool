package com.example.more.team

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.more.R
import com.example.more.adapter.TeamAdapter
import com.example.more.bean.TeamBean
import com.example.more.databinding.MoreWindowPopShowBinding


class ChoosePopupWindow(container : View,beans: ArrayList<TeamBean>,onClickListener: OnClickListener) : PopupWindow(container.context) {

    private var binding : MoreWindowPopShowBinding
    private var beans : ArrayList<TeamBean>

    init {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
//        height = mWindow.decorView.height / 2
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = DataBindingUtil.inflate(LayoutInflater.from(container.context)
            , R.layout.more_window_pop_show,null,false)
       // val contentView = LayoutInflater.from(context).inflate(R.layout.more_window_pop_show,null,false)
        setContentView(binding.root)
        binding.layoutContainerAll.setOnClickListener {
            onClickListener.onClick()
        }
        this.beans = beans
        initView()
    }

    fun initView() {
        val adapter = TeamAdapter(beans)
        binding.rvTeamChoose.setAdapter(adapter)
        val manager = LinearLayoutManager(contentView.context, RecyclerView.VERTICAL, false)
        binding.rvTeamChoose.setLayoutManager(manager)
    }


    interface OnClickListener{
        fun onClick()
    }
}