package com.example.more.touch

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.common.util.EventBusInfo
import com.example.more.R
import com.example.more.databinding.MoreActivityTouchBinding
import com.example.more.accessibility.isAccessibilityEnable
import com.example.more.accessibility.requireAccessibility
import com.example.more.accessibility.startApp
import com.example.more.leisu.PostDataCenter

import com.jeremyliao.liveeventbus.LiveEventBus


class TouchActivity : BaseActivity<MoreActivityTouchBinding, BaseViewModel>() {

    companion object{
        const val TAG = "TouchActivity"
    }

    override fun initData(savedInstanceState: Bundle?) {

        initUI()

        binding.tvIsEnable.setOnClickListener {
            if (isAccessibilityEnable) showToast("无障碍服务已开启")
            else requireAccessibility()
        }
        binding.tvShowAccessWindow.setOnClickListener {

        }
        binding.tvStartLeisu.setOnClickListener {
            //initWindow()
            //startApp("com.tencent.mm", "com.tencent.mm.ui.LauncherUI", "未安装微信")
            startApp("com.leisu.sports", "com.leisu.sports.ui.main.MainActivity", "未安装雷速")
        }

        initListener()
    }

    fun initUI(){
        PostDataCenter.instance().apply {
            binding.etTimesFreeSingleBasketball.setText(remainTimesArray[PostDataCenter.PostType.SINGLE_BASKETBALL].toString())
            binding.etTimesFreeSingleFootball.setText(remainTimesArray[PostDataCenter.PostType.SINGLE_FOOTBALL].toString())
            binding.etTimesFreeMultiBasketball.setText(remainTimesArray[PostDataCenter.PostType.MULTI_BASKETBALL].toString())
            binding.etTimesFreeMultiFootball.setText(remainTimesArray[PostDataCenter.PostType.MULTI_FOOTBALL].toString())
        }

    }

    fun initListener() {
        LiveEventBus.get<Any?>(EventBusInfo.FLOAT_WINDOW_TEST_TOUCH)
            .observe(this) {

            }

        isFreeClickListener(binding.tvIsFreeSingleBasketball)
        isFreeClickListener(binding.tvIsFreeMultiBasketball)
        isFreeClickListener(binding.tvIsFreeSingleFootball)
        isFreeClickListener(binding.tvIsFreeMultiFootball)

        //保存信息
        binding.tvSaveConfig.setOnClickListener {
            PostDataCenter.instance().apply {
                isFreeArray[PostDataCenter.PostType.MULTI_FOOTBALL] = isFree(binding.tvIsFreeMultiFootball)
                isFreeArray[PostDataCenter.PostType.SINGLE_FOOTBALL] = isFree(binding.tvIsFreeSingleFootball)
                isFreeArray[PostDataCenter.PostType.MULTI_BASKETBALL] = isFree(binding.tvIsFreeMultiBasketball)
                isFreeArray[PostDataCenter.PostType.SINGLE_BASKETBALL] = isFree(binding.tvIsFreeSingleBasketball)

                remainTimesArray[PostDataCenter.PostType.SINGLE_BASKETBALL]  = getRemainsCount(binding.etTimesFreeSingleBasketball)
                remainTimesArray[PostDataCenter.PostType.MULTI_BASKETBALL]  = getRemainsCount(binding.etTimesFreeMultiBasketball)
                remainTimesArray[PostDataCenter.PostType.SINGLE_FOOTBALL] = getRemainsCount(binding.etTimesFreeSingleFootball)
                remainTimesArray[PostDataCenter.PostType.MULTI_FOOTBALL]  = getRemainsCount(binding.etTimesFreeMultiFootball)

                Log.d(TAG, "initListener: msg ===" + printMsg())
            }
        }
    }

    fun isFreeClickListener(tv: TextView) {
        tv.setOnClickListener {
            if (tv.text == "否") {
                tv.text = "是"
            } else {
                tv.text = "否"
            }
        }
    }

    fun isFree(tv: TextView): Boolean {
        return tv.text == "是"
    }

    fun getRemainsCount(tv: TextView): Int {
        tv.text.toString().let {
            if (it.isEmpty()){
                showToast("请输入有效可发布次数")
                return 0
            }
            return it.toInt()
        }
    }

    fun initWindow() {

    }

    override fun getLayoutId() = R.layout.more_activity_touch
}