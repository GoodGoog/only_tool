package com.example.more.touch

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseActivity
import com.example.common.base.BaseAdapter
import com.example.common.base.BaseViewModel
import com.example.common.util.EventBusInfo
import com.example.more.R
import com.example.more.accessibility.isAccessibilityEnable
import com.example.more.accessibility.requireAccessibility
import com.example.more.accessibility.startApp
import com.example.more.databinding.MoreActivityTouchBinding
import com.example.more.databinding.MoreItemInitialConfigBinding
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PostDataCenter
import com.jeremyliao.liveeventbus.LiveEventBus


class TouchActivity : BaseActivity<MoreActivityTouchBinding, BaseViewModel>() {

    companion object{
        const val TAG = "TouchActivity"
    }

    override fun initData(savedInstanceState: Bundle?) {

        initUI()
        initRecyclerView()
        initListener()
    }

    fun initRecyclerView() {
        val adapter = object : BaseAdapter<MoreItemInitialConfigBinding, PostConfigData>(
            PostDataCenter.instance().postArray,
            R.layout.more_item_initial_config
        ) {
            override fun bindViewHolder(
                mbinding: MoreItemInitialConfigBinding,
                datas: ArrayList<PostConfigData>,
                position: Int
            ) {
                val data = datas[position]
                mbinding.apply {
                    tvTitle.text = data.title
                    tvIsFree.text = if (data.isFree) "是" else "否"
                    tvIsPost.text = if (data.isPost) "是" else "否"
                    etPostTimes.setText(data.postTimes.toString())
                    //交互事件
                    tvIsPost.setOnClickListener {
                        tvIsPost.text = if (data.isPost){
                            datas[position].isPost = false
                            "否"
                        }else{
                            datas[position].isPost = true
                            "是"
                        }
                    }
                    tvIsFree.setOnClickListener {
                        tvIsFree.text = if (data.isFree){
                            datas[position].isFree = false
                            "否"
                        }else{
                            datas[position].isFree = true
                            "是"
                        }
                    }
                    //光标改变了
                    etPostTimes.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            // 失去焦点、光标消失
                            datas[position].postTimes = getRemainsCount(etPostTimes)
                        }
                    }

                }
            }
        }
        binding.rvInitialConfig.setAdapter(adapter)
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvInitialConfig.setLayoutManager(manager)
    }

    fun initUI(){

    }

    fun initListener() {
        //拿走光标,触发rv回调
        binding.tvSaveConfig.setOnClickListener {
            //先夺走光标，触发item中EditView的数据回调
            //结束时光标变化
            binding.etCursorHolder.apply {
                requestFocus();
            }
            Log.d(TAG, "initListener: msg ===" + PostDataCenter.instance().printMsg())
        }

        LiveEventBus.get<Any?>(EventBusInfo.FLOAT_WINDOW_TEST_TOUCH)
            .observe(this) {

            }
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


    override fun getLayoutId() = R.layout.more_activity_touch
}