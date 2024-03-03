package com.example.more.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.databinding.ActivityBaseKotlinBinding

class BaseKotlinActivity : BaseActivity<ActivityBaseKotlinBinding,BaseKotlinVM>() {

    override fun initData(savedInstanceState: Bundle?) {
        initObserver()

        viewModel.title.value = "测试ok"
        logD("a + b "+ sum(5))
    }

    private fun initObserver() {
        viewModel.title.observe(this, Observer {
            binding.tvTitle.text = it
        })
    }

    private fun sum(a :Int , b : Int = 5) : Int{
        return a + b;
    }

    override fun getLayoutId() = R.layout.activity_base_kotlin

}