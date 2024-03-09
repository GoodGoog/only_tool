package com.example.more.thread.handler

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.databinding.MoreActivityHandlerBinding

class HandlerActivity : BaseActivity<MoreActivityHandlerBinding, BaseViewModel>() {

    companion object{
        const val MESSAGE_TAG = 1
    }

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what != MESSAGE_TAG) return
            logD("当前的threadName:" + Thread.currentThread().name + "${msg.arg1}--${msg.arg2}")
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        binding.tvStartIoTask.setOnClickListener {
            startChildThread()
        }
    }

    private fun startChildThread() {
        Thread {
           Message().apply {
               what = MESSAGE_TAG
               arg1 = 123
               arg2 = 321
               mHandler.sendMessage(this)
           }
        }.start()
    }

    override fun getLayoutId() = R.layout.more_activity_handler
}