package com.example.more.setting

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.common.util.showToast
import com.example.more.R
import com.example.more.databinding.MoreActivitySettingResultBinding

class ResultActivity : BaseActivity<MoreActivitySettingResultBinding, BaseViewModel>() {

    var left_team_name = "没有传来数据左"
    var right_team_name = "没有传来数据右"

    override fun initData(savedInstanceState: Bundle?) {
        left_team_name = (intent.getStringExtra("TEAM_LEFT_NAME") ?: "") as String
        right_team_name = (intent.getStringExtra("TEAM_RIGHT_NAME") ?: "") as String

        binding.etTitle.setText(left_team_name + "VS" + right_team_name)
        binding.tvRightWin.text = right_team_name
        binding.tvLeftWin.setText(left_team_name)

        binding.etAnalyseHead.setText(readCache())
        binding.tvInsertAnalyseHead.setOnClickListener {
            writeCache(binding.etAnalyseHead.text?.toString() ?: "请设置默认前缀")
            binding.etAnalyseResult.setText(binding.etAnalyseHead.text.toString()
                    + "，下面由我为大家带来" + left_team_name
                    + "VS"
                    + right_team_name
                    + "的前瞻分析。")
        }

        binding.tvLeftWin.setOnClickListener { chooseWinner(left_team_name) }
        binding.tvRightWin.setOnClickListener { chooseWinner(right_team_name) }
    }

    //选择胜方
    fun chooseWinner(winner : String){
        val tail : String = "\n" + "最后、比分预测\n"+ left_team_name+ "VS"+right_team_name + "，" + winner + "受让分胜。"
        binding.etFinalAnswer.setText(binding.etFinalAnswer.text.toString() + tail)
    }

    //分析前瞻默认前缀缓存数据的 存取
    fun writeCache(cache : String){
        this.getSharedPreferences(ANALYSE_HEAD_SHARED_PREFERENCE,MODE_PRIVATE).edit()
            .putString(ANALYSE_HEAD_CACHE,cache).commit()
    }

    fun readCache() : String = getSharedPreferences(ANALYSE_HEAD_SHARED_PREFERENCE,MODE_PRIVATE).getString(ANALYSE_HEAD_CACHE,"请设置默认前缀") as String

    override fun getLayoutId() = R.layout.more_activity_setting_result
}