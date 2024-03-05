package com.example.more.kotlin

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.more.R
import com.example.more.databinding.ActivityBaseKotlinBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BaseKotlinActivity : BaseActivity<ActivityBaseKotlinBinding,BaseKotlinVM>() {

    override fun initData(savedInstanceState: Bundle?) {
        initObserver()
        initClickListener()

    }

    private fun initClickListener(){
        logD("当前线程名字initClickListener-" + Thread.currentThread().name)
        binding.tvStartLaunchScope.setOnClickListener {
            GlobalScope.launch {
                logD("当前线程名字GlobalScope-" + Thread.currentThread().name)
            }
        }
        runBlocking {

        }
    }

    private fun initObserver() {

    }


    override fun getLayoutId() = R.layout.activity_base_kotlin

}