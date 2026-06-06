package com.example.more.setting

import android.content.Intent
import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.common.util.showToast
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
            Intent(this, ResultActivity::class.java).let {
                it.putExtra(TEAM_LEFT_NAME, binding.etLeftName.text.toString())
                it.putExtra(TEAM_RIGHT_NAME, binding.etRightName.text.toString())
                startActivity(it)
            }
        }
    }

    fun addClickEvent() {
        binding.tvQuestionDetail.setOnClickListener {
            isNeedSplit()
            binding.etAiQuestion.setText(
                "分析一下" + binding.etCupName.text + "赛事中，"
                        + binding.etLeftName.text + "VS" + binding.etRightName.text
                        + "各自的优势和近况，"
                        + "用中文（一、二、三、四）和数字（1.2.3.4）做好分段，（500字以内）,并预测哪一队更有可能获胜"
            )
        }
        binding.tvQuestionRude.setOnClickListener {
            isNeedSplit()
            binding.etAiQuestion.setText(
                binding.etCupName.text.toString() + "赛事中，" + binding.etLeftName.text + "VS" + binding.etRightName.text
                        + "这一场比赛中"
                        + "，预测哪一队更有可能获胜"
            )
        }

    }

    //检测是否需要一键解析
    fun isNeedSplit() {
        binding.etQuickInputThree.text.toString().apply {
            if (this.isEmpty()) return@apply
            split("\n", limit = 3).let {
                if (it.size == 3) {
                    binding.etCupName.setText(it[0])
                    binding.etLeftName.setText(it[1])
                    binding.etRightName.setText(it[2])
                }
            }
        }
    }


    override fun getLayoutId() = R.layout.more_activity_setting
}