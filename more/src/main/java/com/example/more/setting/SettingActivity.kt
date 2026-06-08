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
import kotlin.math.abs

class SettingActivity : BaseActivity<MoreActivitySettingBinding, BaseViewModel>() {

    override fun initData(savedInstanceState: Bundle?) {
        initJump()
        addClickEvent()
    }

    fun initJump() {
        //从球队选择页面传来了球队数据
        val left_team_name = (intent.getStringExtra(TEAM_LEFT_NAME) ?: "") as String
        val right_team_name = (intent.getStringExtra(TEAM_RIGHT_NAME) ?: "") as String
        val team_cup_name = (intent.getStringExtra(TEAM_CUP_NAME) ?: "") as String
        val left_team_raw_score = (intent.getStringExtra(TEAM_LEFT_RAW_SCORE) ?: "") as String
        if (team_cup_name.isNotEmpty() && left_team_name.isNotEmpty() && right_team_name.isNotEmpty()) {
            //是从选球赛页面传来的，有数据
            binding.etQuickInputThree.setText(team_cup_name + "\n" + left_team_name + "\n" + right_team_name + "\n" + left_team_raw_score)
        }
    }

    fun addClickEvent() {
        //点击粘贴比赛下信息
        binding.tvMatchInfoParse.setOnClickListener {
            parseTextFromSystem().let {
                binding.etQuickInputThree.setText(it)
            }
        }

        //点击解析比赛信息
        binding.tvMatchInfoSplit.setOnClickListener {
            binding.etQuickInputThree.text.let {
                isNeedSplit()
            }
        }

        binding.tvQuestionDetail.setOnClickListener {
            var leftTeamRawScoreTips = ""
            binding.etLeftTeamRawScore.text?.toString()!!.toInt().let { rawScore ->
                leftTeamRawScoreTips = "如果" + binding.etLeftName.text.toString() +
                        (if (rawScore >= 0) "受让" else "让") +
                        "${abs(rawScore)}" + "分，"
            }
            binding.etAiQuestion.setText(
                "分析一下" + binding.etCupName.text + "赛事中，"
                        + binding.etLeftName.text + "对阵" + binding.etRightName.text
                        + "各自的近况和优劣势，对每个球队的分析控制在一个大段之内，"
                        + "用（一、二、三、四、）分开大段，大段内用（1.2.3.4.）分开小段， "
                        + "全文不能有空白行，任意段之间都要换行。"
                        + judgeLeftTeamScoreTips(binding.etLeftName?.text?.toString() ?: "",binding.etLeftTeamRawScore.text.toString()?.toInt() ?: 0,"，") + "预测哪一队更有可能获胜。"
                        + "答案控制在450字以内，结尾不要任何无关提醒！"

            )
        }
        binding.tvQuestionRude.setOnClickListener {
            binding.etAiQuestion.setText(
                binding.etCupName.text.toString() + "赛事中，" + binding.etLeftName.text + "对阵" + binding.etRightName.text + "，"
                        + judgeLeftTeamScoreTips(binding.etLeftName.text.toString(),binding.etLeftTeamRawScore.text.toString().toInt(),"，")
                        + "预测哪一队更有可能获胜（50字以内，不需要分析）"
            )
        }
//        binding.tvJumpObtain.setOnClickListener {
//            startActivity(ObtainActivity::class.java)
//        }

        //点击复制
        binding.tvQuestionCopy.setOnClickListener {
            binding.etAiQuestion.text.let { it ->
                if (it.isEmpty()) {
                    showToast(this, "生成问题后才能复制")
                    return@setOnClickListener
                }
                //复制
                copyTextToSystem(it.toString(), true)
            }
        }

        //点击复制并跳转
        binding.tvQuestionCopyAndJump.setOnClickListener {
            binding.etAiQuestion.text.let { it ->
                if (it.isEmpty()) {
                    showToast(this, "生成问题后才能复制并跳转")
                    return@setOnClickListener
                }
                //复制
                copyTextToSystem(it.toString(), true)
                //跳转
                Intent(this, ResultActivity::class.java).let { mIntent ->
                    mIntent.putExtra(TEAM_LEFT_NAME, binding.etLeftName.text.toString())
                    mIntent.putExtra(TEAM_RIGHT_NAME, binding.etRightName.text.toString())
                    startActivity(mIntent)
                }
            }
        }

    }

    //检测是否需要一键解析
    fun isNeedSplit() {
        binding.etQuickInputThree.text.toString().apply {
            if (this.isEmpty()) return@apply
            split("\n", limit = 4).let {
                if (it.size == 4) {
                    binding.etCupName.setText(it[0])
                    binding.etLeftName.setText(it[1])
                    binding.etRightName.setText(it[2])
                    binding.etLeftTeamRawScore.setText(it[3])
                }
            }
        }
    }


    override fun getLayoutId() = R.layout.more_activity_setting
}