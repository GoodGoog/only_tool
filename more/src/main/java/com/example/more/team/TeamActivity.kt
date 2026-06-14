package com.example.more.team

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.common.util.showToast
import com.example.more.R
import com.example.more.adapter.TeamAdapter
import com.example.more.bean.TeamBean
import com.example.more.databinding.MoreActivityTeamChooseBinding
import com.example.more.setting.SettingActivity
import com.example.more.setting.TEAM_CUP_NAME
import com.example.more.setting.TEAM_LEFT_NAME
import com.example.more.setting.TEAM_LEFT_RAW_SCORE
import com.example.more.setting.TEAM_RIGHT_NAME
import com.example.more.setting.judgeLeftTeamScoreTips
import com.example.more.setting.splitStringToStrArray

class TeamActivity : BaseActivity<MoreActivityTeamChooseBinding, BaseViewModel>() {
    override fun initData(savedInstanceState: Bundle?) {

        initClickEvent()
    }

    fun initClickEvent() {
        //输入原始数据
        binding.tvParseRawMatchText.setOnClickListener {
            binding.etTextFormatTrans.setText(parseTextFromSystem())
        }

        binding.tvTransRawMatchText.setOnClickListener {
            var rawStr: String = binding.etTextFormatTrans.text.toString()
            if (rawStr.isEmpty()) return@setOnClickListener
            rawStr = rawStr.replace("[", "")
            rawStr = rawStr.replace("]", "")
            //删除数字
            rawStr = rawStr.replace(Regex("[0-9]+"), "")

//            //插入换行符
            var aimStr = ""
            splitStringToStrArray(rawStr, "\n").let {
                for (index in 0..it.size - 1) {
                    showToast(index.toString())
                    //不是最后一行字符串
                    if (index + 1 != it.size) {
                        aimStr += it[index] + "\n"
                    }
                    //背三整除 且 不是最后一行字符串
                    if ((index + 1) % 3 == 0 && index + 1 != it.size) {
                        aimStr += "\n\n"
                    }
                }
            }
            binding.etTextFormatTrans.setText(aimStr)
        }

        binding.tvCopyTransformMatchText.setOnClickListener {
            binding.etTextFormatTrans.text.let {
                if (it.isNotEmpty()) {
                    copyTextToSystem(it.toString(), true)
                } else {
                    showToast(this, "复制内容不能为空")
                }
            }
        }

        binding.tvInputParse.setOnClickListener {
            binding.etMatchInput.setText(parseTextFromSystem())
        }

        //生成组合问题
        binding.tvCombineAllQuestion.setOnClickListener {
            if (binding.etMatchInput.text.isEmpty()) showToast(this, "先输入比赛信息")
            else {
                val builder = StringBuilder()
                builder.append("分别预测以下比赛中哪一个队伍有可能获胜。")
                val beans = splitRawMatchStrToArray()
                for (index in beans.indices) {
                    beans[index].let { bean ->
                        builder.append(
                            "${index + 1}." + bean.cupName + "比赛中，" + bean.left_team_name + "对阵" + bean.right_team_name
                                    + if (bean.left_team_raw_score.toFloat() == 0.toFloat()) "。" else "，"
                                    + judgeLeftTeamScoreTips(
                                bean.left_team_name,
                                bean.left_team_raw_score.toFloat(),
                                "。"
                            )
                        )
                    }
                }
                builder.append("回答限制在" + "${beans.size * 50}字以内，不需要分析。")
                binding.etFinalQuestion.setText(builder.toString())
            }
        }
        //复制问题
        binding.tvCopyQuestion.setOnClickListener {
            binding.etFinalQuestion.text.let {
                if (it.isNotEmpty()) {
                    copyTextToSystem(it.toString(), true)
                } else {
                    showToast(this, "复制内容不能为空")
                }
            }
        }

        binding.tvSplitToSingleMatch.setOnClickListener {
            if (binding.etMatchInput.text.isEmpty()) showToast(this, "先输入比赛信息")
            else {
                initView(splitRawMatchStrToArray())
            }
        }
    }

    //解析原始比赛信息字符串
    fun splitRawMatchStrToArray(): ArrayList<TeamBean> {
        val array = ArrayList<TeamBean>()
        splitStringToStrArray(binding.etMatchInput.text.toString(), "\n\n").let {
            if (it.size > 0) {
                //再单独解析被分割出来的单个赛程字符串
                for (singleStr in it) {
                    splitStringToStrArray(singleStr, "\n").let { beans ->
                        array.add(
                            createTeamBean(
                                beans[0], beans[1], beans[2], beans[3]
                            )
                        )
                    }
                }
            }
        }
        return array
    }


    //拿到解析数据再显示列表
    fun initView(beans: ArrayList<TeamBean>) {
        val adapter = TeamAdapter(beans)
        binding.teamRv.setAdapter(adapter)
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.teamRv.setLayoutManager(manager)
    }


    private fun createTeamBean(
        cupName: String,
        left_team_name: String,
        right_team_name: String,
        left_name_raw_score: String
    ): TeamBean {
        return TeamBean(
            cupName, left_team_name, right_team_name, left_name_raw_score,
            View.OnClickListener { v: View? ->
                //跳转
                Intent(this, SettingActivity::class.java).let { mIntent ->
                    mIntent.putExtra(TEAM_CUP_NAME, cupName)
                    mIntent.putExtra(TEAM_LEFT_NAME, left_team_name)
                    mIntent.putExtra(TEAM_RIGHT_NAME, right_team_name)
                    mIntent.putExtra(TEAM_LEFT_RAW_SCORE, left_name_raw_score)
                    startActivity(mIntent)
                }
            })
    }


    override fun getLayoutId() = R.layout.more_activity_team_choose
}