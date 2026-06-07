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
        left_team_name = (intent.getStringExtra("TEAM_LEFT_NAME") ?: "") as String
        right_team_name = (intent.getStringExtra("TEAM_RIGHT_NAME") ?: "") as String

        binding.tvRightWin.text = right_team_name
        binding.tvLeftWin.setText(left_team_name)
        binding.etAnalyseHead.setText(readCache())

        initPopWindow()

        addClickEvent()
    }

    fun addClickEvent(){
        //默认点击隐藏标题选择弹窗
        window.decorView.setOnClickListener {
            binding.layoutPopWindow.apply {
                if (visibility == View.VISIBLE) visibility = View.INVISIBLE
            }
        }

        binding.layoutPopWindow.apply {
            binding.tvChooseTitle.setOnClickListener {
                visibility = if (visibility == View.VISIBLE) View.INVISIBLE
                else View.VISIBLE
            }
        }

        //复制标题点击
        binding.tvCopyTitle.setOnClickListener {
            copyTextToSystem(binding.etTitle.text.toString() ?: "标题为空")
        }

        //点击插入前瞻
        binding.tvInsertAndCachePreAnalyseHead.setOnClickListener {
            writeCache(binding.etAnalyseHead.text?.toString() ?: "请设置默认前缀")
            binding.etPreAnalyse.setText(binding.etAnalyseHead.text.toString()
                    + "，下面由我为大家带来" + left_team_name
                    + "VS"
                    + right_team_name
                    + "的前瞻分析。")
        }

        //点击复制前瞻
        binding.tvCopyPreAnalyse.setOnClickListener {
            copyTextToSystem(binding.etPreAnalyse.text.toString() ?: "前瞻为空")
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


    fun initPopWindow(){
        binding.layoutPopWindow.apply {
            setOnClickListener {
                if (visibility == View.VISIBLE) visibility = View.INVISIBLE
            }
        }

        binding.tvTitle1.apply {
            setAVSBMode(this,"胜利将花落谁家？")
        }
        binding.tvTitle2.apply {
            setAVSBMode(this,"谁能卫冕荣耀？")
        }
        binding.tvTitle3.apply {
            setAVSBMode(this,"昔日格局是否会被重塑？")
        }
        binding.tvTitle4.apply {
            setAComeifBcoulde(this,"来势汹汹","能否逆境封神？")
        }
        binding.tvTitle5.apply {
            setAComeifBcoulde(this,"携登顶之势而来","能否抗住压力？")
        }
        binding.tvTitle6.apply {
            setAComeifBcoulde(this,"如虎冲狼奔","谁能挺身而出？")
        }
        binding.tvTitle7.apply {
            apply {
                text = "势均力敌！" + "A和B谁能登临宝座?"
                setOnClickListener {
                    binding.etTitle.setText("龙争虎斗！" + left_team_name + "和" +right_team_name + "谁能登临宝座？")

                }
            }
        }
        binding.tvTitle8.apply {
            apply {
                text = "难分高下！" + "A能否击败B?"
                setOnClickListener {
                    binding.etTitle.setText("难分高下！" + left_team_name + "能否击败" +right_team_name + "？")

                }
            }
        }
        binding.tvTitle9.apply {
            apply {
                text = "多年宿敌赛场再逢！" + "这次谁能笑道最后？"
                setOnClickListener {
                    binding.etTitle.setText("多年宿敌赛场再逢！" + "这次谁能笑道最后？")
                }
            }
        }
        binding.tvTitle10.apply {
            setAVSBMode(this,"是否会有惊喜发生？")
        }
    }

    fun setAVSBMode(tvTitle : TextView,tail : String){
        tvTitle.apply {
            text = "AvsB，" + tail
            setOnClickListener {
                binding.etTitle.setText(left_team_name + "VS" +right_team_name + "，" + tail)
            }
        }
    }

    //A四字成语，B能否四字成语
    fun setAComeifBcoulde(tvTitle : TextView,aWord: String,bWord : String){
        tvTitle.apply {
            text = "A+成语，B能否+成语"
            setOnClickListener {
                binding.etTitle.setText(left_team_name + aWord + "，" + right_team_name + bWord)
            }
        }
    }
}