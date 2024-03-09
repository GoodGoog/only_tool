package com.example.more.kotlin

import android.os.Bundle
import android.os.PersistableBundle
import com.example.common.base.BaseActivity
import com.example.more.R
import com.example.more.databinding.ActivityBaseKotlinBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BaseKotlinActivity : BaseActivity<ActivityBaseKotlinBinding,BaseKotlinVM>() {

    override fun initData(savedInstanceState: Bundle?) {
        if (savedInstanceState == null){

        }else{

        }
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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }

}