package com.example.more.team

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.databinding.MoreActivityTeamChooseBinding

class TeamActivity : BaseActivity<MoreActivityTeamChooseBinding, BaseViewModel>() {
    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun getLayoutId() = R.layout.more_activity_team_choose
}