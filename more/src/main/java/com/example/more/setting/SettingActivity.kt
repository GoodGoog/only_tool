package com.example.more.setting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.common.util.showToast
import com.example.more.R
import com.example.more.databinding.MoreActivitySettingBinding

class SettingActivity : BaseActivity<MoreActivitySettingBinding, BaseViewModel>() {

    override fun initData(savedInstanceState: Bundle?) {
        initJump()
        addClickEvent()
    }

    fun initJump(){
        //从球队选择页面传来了球队数据
        val left_team_name = (intent.getStringExtra(TEAM_LEFT_NAME) ?: "") as String
        val right_team_name = (intent.getStringExtra(TEAM_RIGHT_NAME) ?: "") as String
        val team_cup_name = (intent.getStringExtra(TEAM_CUP_NAME) ?: "") as String
        if (team_cup_name.isNotEmpty() && left_team_name.isNotEmpty() && right_team_name.isNotEmpty()){
            //是从选球赛页面传来的，有数据
            binding.etQuickInputThree.setText(team_cup_name + "\n" + left_team_name + "\n" + right_team_name)
            isNeedSplit()
        }
    }

    fun addClickEvent() {
        binding.tvQuestionDetail.setOnClickListener {
            isNeedSplit()
            binding.etAiQuestion.setText(
                "分析一下" + binding.etCupName.text + "赛事中，"
                        + binding.etLeftName.text + "VS" + binding.etRightName.text
                        + "各自的近况和优劣势，对每个球队的分析控制在一个大点之内，"
                        + "用中文（一、二、三、四）和数字（1.2.3.4）做好分段，（450字以内）,段与段之间不能空行，每一个小点间要换行，并预测哪一队更有可能获胜"
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
//        binding.tvJumpObtain.setOnClickListener {
//            startActivity(ObtainActivity::class.java)
//        }

        //点击复制
        binding.tvCopyQuestionAndJumpResult.setOnClickListener {
            binding.etAiQuestion.text.let { it ->
                if (it.isEmpty()){
                    showToast(this,"生成问题后才能复制并跳转")
                    return@setOnClickListener
                }
                //复制
                copyTextToSystem(it.toString())
                //跳转
                Intent(this, ResultActivity::class.java).let {mIntent ->
                    mIntent.putExtra(TEAM_LEFT_NAME, binding.etLeftName.text.toString())
                    mIntent.putExtra(TEAM_RIGHT_NAME, binding.etRightName.text.toString())
                    startActivity(mIntent)
                }
            }
        }

        //点击粘贴比赛信息
        binding.tvParseKnewMatch.setOnClickListener {
            parseTextFromSystem().let {
                binding.etQuickInputThree.setText(it)
                isNeedSplit()
            }
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