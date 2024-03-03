package com.example.more.component

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.component.curProcess.CurProcessServiceActivity
import com.example.more.databinding.MoreActivityeJumpAppServiceBinding

class JumpAppServiceActivity : BaseActivity<MoreActivityeJumpAppServiceBinding,BaseViewModel>() {
    override fun initData(savedInstanceState: Bundle?) {
        binding.tvCurProcessServiceActivity.setOnClickListener {
            this@JumpAppServiceActivity.startActivity(CurProcessServiceActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.more_activitye_jump_app_service
}