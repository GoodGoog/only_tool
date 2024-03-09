package com.example.more.thread

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.databinding.MoreActivityThreadBinding
import com.example.more.thread.handler.HandlerActivity

class ThreadActivity : BaseActivity<MoreActivityThreadBinding,BaseViewModel>() {

    override fun initData(savedInstanceState: Bundle?) {
        binding.tvHandlerTest.setOnClickListener{
            startActivity(HandlerActivity::class.java)
        }
    }

    override fun getLayoutId() = R.layout.more_activity_thread
}