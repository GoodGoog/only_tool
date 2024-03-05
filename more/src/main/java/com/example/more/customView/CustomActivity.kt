package com.example.more.customView

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.databinding.MoreActivityCustomBinding

class CustomActivity : BaseActivity<MoreActivityCustomBinding, BaseViewModel>() {

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun getLayoutId() = R.layout.more_activity_custom
}