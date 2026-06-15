package com.example.more.setting

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.common.util.showToast
import com.example.more.R
import com.example.more.databinding.MoreActivitySettingResultBinding

class ResultActivity : BaseActivity<MoreActivitySettingResultBinding, BaseViewModel>() {

    var left_team_name = "没有传来数据左"
    var right_team_name = "没有传来数据右"

    override fun initData(savedInstanceState: Bundle?) {
        left_team_name = (intent.getStringExtra(TEAM_LEFT_NAME) ?: "") as String
        right_team_name = (intent.getStringExtra(TEAM_RIGHT_NAME) ?: "") as String

        binding.tvRightWin.text = right_team_name
        binding.tvLeftWin.text = left_team_name
        binding.etAnalyseHead.setText(readCache())

        initPopWindow()

        addClickEvent()
    }

    fun addClickEvent() {
        binding.tvChooseTitle.setOnClickListener {

        }

        //复制标题点击
        binding.tvCopyTitle.setOnClickListener {
            if (binding.etTitle.text.isEmpty()){
                showToast(this,"标题为空")
                return@setOnClickListener
            }
            copyTextToSystem(binding.etTitle.text.toString(), true)
        }

        //点击插入前瞻
        binding.tvInsertAndCachePreAnalyseHead.setOnClickListener {
            writeCache(binding.etAnalyseHead.text?.toString() ?: "请设置默认前缀")
            binding.etPreAnalyse.setText(
                binding.etAnalyseHead.text.toString()
                        + "，下面我为大家带来" + left_team_name
                        + "VS"
                        + right_team_name
                        + "的前瞻分析，"
                        + "支持的朋友请抓紧解锁！"
            )
        }

        //点击复制前瞻
        binding.tvCopyPreAnalyse.setOnClickListener {
            if (binding.etPreAnalyse.text.isEmpty()){
                showToast(this,"前瞻为空")
                return@setOnClickListener
            }
            copyTextToSystem(binding.etPreAnalyse.text.toString(), true)
        }

        //点击复制最终结果
        binding.tvCopyAnswer.setOnClickListener { _ ->
            if (binding.etFinalAnswer.text.isEmpty()) {
                showToast(this,"结果为空")
                return@setOnClickListener
            }
            copyTextToSystem(binding.etFinalAnswer.text.toString(), true)
        }

        //粘贴插入外来的分析结果
        binding.tvParseAnswer.setOnClickListener {
            binding.etFinalAnswer.setText(parseTextFromSystem())
        }

        binding.tvLeftWin.setOnClickListener { chooseWinner(left_team_name) }
        binding.tvRightWin.setOnClickListener { chooseWinner(right_team_name) }
    }

    //选择胜方
    fun chooseWinner(winner: String) {
        val tail: String =
            "\n" + "最后、比分预测\n" + left_team_name + "VS" + right_team_name + "，" + winner + "胜。"
        binding.etFinalAnswer.setText(binding.etFinalAnswer.text.toString() + tail)
    }

    //分析前瞻默认前缀缓存数据的 存取
    fun writeCache(cache: String) {
        this.getSharedPreferences(ANALYSE_HEAD_SHARED_PREFERENCE, MODE_PRIVATE).edit()
            .putString(ANALYSE_HEAD_CACHE, cache).commit()
    }

    fun readCache(): String =
        getSharedPreferences(ANALYSE_HEAD_SHARED_PREFERENCE, MODE_PRIVATE).getString(
            ANALYSE_HEAD_CACHE,
            "请设置默认前缀"
        ) as String

    override fun getLayoutId() = R.layout.more_activity_setting_result


    fun initPopWindow() {

    }

}