package com.example.more.team

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.adapter.MoreAdapter
import com.example.more.adapter.TeamAdapter
import com.example.more.bean.MoreBean
import com.example.more.bean.TeamBean
import com.example.more.databinding.MoreActivityTeamChooseBinding
import com.example.more.setting.ResultActivity
import com.example.more.setting.SettingActivity
import com.example.more.setting.TEAM_CUP_NAME
import com.example.more.setting.TEAM_LEFT_NAME
import com.example.more.setting.TEAM_RIGHT_NAME
import com.example.more.third.retrofit.RetrofitActivity

class TeamActivity : BaseActivity<MoreActivityTeamChooseBinding, BaseViewModel>() {
    override fun initData(savedInstanceState: Bundle?) {

        val beans = ArrayList<TeamBean?>()

        //测试入口
        beans.add(
            createTeamBean(
                "牛杯",
                "left",
                "right"
            )
        )
        beans.add(
            createTeamBean(
                "牛杯11",
                "left11",
                "right11"
            )
        )
        beans.add(
            createTeamBean(
                "牛杯22",
                "left22",
                "right22"
            )
        )
        //binding.moreRv.setBackgroundColor(Color.parseColor("#665544"));
        val adapter = TeamAdapter(beans)
        binding.teamRv.setAdapter(adapter)
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.teamRv.setLayoutManager(manager)
    }

    private fun createTeamBean(
        cupName: String,
        left_team_name: String,
        right_team_name: String
    ): TeamBean {
        return TeamBean(
            cupName, left_team_name, right_team_name,
            View.OnClickListener { v: View? ->
                //跳转
                Intent(this, SettingActivity::class.java).let { mIntent ->
                    mIntent.putExtra(TEAM_CUP_NAME, cupName)
                    mIntent.putExtra(TEAM_LEFT_NAME, left_team_name)
                    mIntent.putExtra(TEAM_RIGHT_NAME, right_team_name)
                    startActivity(mIntent)
                }
            })
    }


    override fun getLayoutId() = R.layout.more_activity_team_choose
}