package com.example.more.setting

import android.content.Intent
import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.databinding.MoreActivitySettingBinding

class SettingActivity : BaseActivity<MoreActivitySettingBinding, BaseViewModel>() {
    override fun initData(savedInstanceState: Bundle?) {
        addClickEvent()
        binding.tvJumpResult.setOnClickListener {
            if (binding.etLeftName.text.toString().isEmpty() || binding.etLeftName.text.toString()
                    .isEmpty()
            )
                return@setOnClickListener
            val newIntent = Intent(this, ResultActivity::class.java)
            newIntent.putExtra(TEAM_LEFT_NAME, binding.etLeftName.text.toString())
            newIntent.putExtra(TEAM_RIGHT_NAME, binding.etRightName.text.toString())
//            newIntent.extras?.putString("TEAM_LEFT_NAME", "binding.etLeftName.text.toString()" ?: "没有做数据")
//            newIntent.extras?.putString("TEAM_RIGHT_NAME", binding.etRightName.text.toString()?: "没有右数据")
            startActivity(newIntent)
        }
    }

    fun addClickEvent() {
        binding.tvQuestionDetail.setOnClickListener {
            binding.etAiQuestion.setText(
                "分析一下" + binding.etCupName.text + "，"
                        + binding.etLeftName.text + "VS" + binding.etRightName.text
                        + "各自的优势和近况，"
                        + "用中文（一、二、三、四）和数字（1.2.3.4）做好分段，（500字以内）,并预测哪一队更有可能获胜"
            )
        }
        binding.tvQuestionRude.setOnClickListener {
            binding.etAiQuestion.setText(
                binding.etCupName.text.toString() + "赛事中，" + binding.etLeftName.text + "VS" + binding.etRightName.text
                        + "这一场比赛中"
                        + "，预测哪一队更有可能获胜"
            )
        }

    }

    override fun getLayoutId() = R.layout.more_activity_setting
}