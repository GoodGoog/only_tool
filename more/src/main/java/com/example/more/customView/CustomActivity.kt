package com.example.more.customView

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.customView.clickEvent.ClickEventActivity
import com.example.more.customView.clock.ClockActivity
import com.example.more.databinding.MoreActivityCustomBinding

class CustomActivity : BaseActivity<MoreActivityCustomBinding, BaseViewModel>() {

    override fun initData(savedInstanceState: Bundle?) {
        binding.tvClock.setOnClickListener {
            startActivity(ClockActivity::class.java)
        }
        binding.tvClickEvent.setOnClickListener {
            startActivity(ClickEventActivity::class.java)
        }
    }

    override fun getLayoutId() = R.layout.more_activity_custom
}