package com.example.more.setting

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.databinding.MoreActivitySettingResultBinding

class ResultActivity : BaseActivity<MoreActivitySettingResultBinding, BaseViewModel>() {
    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun getLayoutId() = R.layout.more_activity_setting_result
}