package com.example.more.psot

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseActivity
import com.example.common.base.BaseAdapter
import com.example.common.base.BaseViewModel
import com.example.common.util.EventBusInfo
import com.example.more.ACCESSIBILITY_SERVICE_START_OR_DESTROY
import com.example.more.FLOAT_WINDOW_ALL_APP_TAG
import com.example.more.R
import com.example.more.accessibility.isAccessibilityEnable
import com.example.more.accessibility.requireAccessibility
import com.example.more.accessibility.startApp
import com.example.more.databinding.MoreActivityTouchBinding
import com.example.more.databinding.MoreItemInitialConfigBinding
import com.example.more.databinding.MoreWindowFloatPostBinding
import com.example.more.leisu.data.PostConfigData
import com.example.more.leisu.data.PostDataCenter
import com.example.more.showToast
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern


class PostActivity : BaseActivity<MoreActivityTouchBinding, BaseViewModel>() {

    companion object {
        const val TAG = "TouchActivity"
    }

    override fun initData(savedInstanceState: Bundle?) {

        initUI()
        initRecyclerView()
        initListener()
    }

    fun initRecyclerView() {
        val adapter = object : BaseAdapter<MoreItemInitialConfigBinding, PostConfigData>(
            PostDataCenter.instance().initialArray,
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
                        tvIsPost.text = if (data.isPost) {
                            datas[position].isPost = false
                            "否"
                        } else {
                            datas[position].isPost = true
                            "是"
                        }
                    }
                    tvIsFree.setOnClickListener {
                        tvIsFree.text = if (data.isFree) {
                            datas[position].isFree = false
                            "否"
                        } else {
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

        //保存时拿走光标,触发rv回调
        binding.tvSaveConfig.setOnClickListener {
            //先夺走光标，触发item中EditView的数据回调
            //结束时光标变化
            binding.etCursorHolder.apply {
                requestFocus();
            }
            PostDataCenter.instance().apply {
                initialArray = adapter.beans
                filterUselessPostInfo { hasData ->
                    if (hasData) {
                        showToast("保存成功！")
                    } else {
                        //无可发布文章时
                        showToast("先选择需要发布的内容！")
                    }
                }
            }
        }

    }

    fun initUI() {

    }

    fun initListener() {

        LiveEventBus.get<Any?>(EventBusInfo.FLOAT_WINDOW_TEST_TOUCH)
            .observe(this) {

            }
        binding.tvIsEnable.setOnClickListener {
            if (isAccessibilityEnable) showToast("无障碍服务已开启")
            else requireAccessibility()
        }
        binding.tvShowAccessWindow.setOnClickListener {
            showFloatWindow()
        }
        binding.tvStartLeisu.setOnClickListener {
            //initWindow()
            //startApp("com.tencent.mm", "com.tencent.mm.ui.LauncherUI", "未安装微信")
            startApp("com.leisu.sports", "com.leisu.sports.ui.main.MainActivity", "未安装雷速")
        }

    }

    fun showFloatWindow() {
        // 先销毁旧窗口
        if (EasyFloat.isShow(FLOAT_WINDOW_ALL_APP_TAG)) {
            EasyFloat.dismiss(FLOAT_WINDOW_ALL_APP_TAG)
        }

        //EasyFloat.getFloatView(FLOAT_WINDOW_ALL_APP_TAG)
        val mbinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.more_window_float_post, window.decorView as ViewGroup?, false
        ) as MoreWindowFloatPostBinding
        mbinding.pvWindowContentView.apply {
            quitWindowClicked {
                EasyFloat.dismiss(FLOAT_WINDOW_ALL_APP_TAG)
            }
            taskVisualizeClicked {
                PostDataCenter.instance().changeTaskVisualized()
            }
        }
        EasyFloat.with(this)
            .setTag(FLOAT_WINDOW_ALL_APP_TAG)
            .setLayout(mbinding.root){ rootView ->

            }
            // 关键：禁止窗口铺满全屏，空白区域自动穿透点击底层
            .setMatchParent(widthMatch = false, heightMatch = false)
            // 开启拖动
            .setDragEnable(true)
            // 水平自动贴左右边缘（替代失效的setAutoEdge）
            .setSidePattern(SidePattern.RESULT_HORIZONTAL)
            // 对齐右侧垂直居中，横向偏移20px，纵向0
            .setGravity(Gravity.END or Gravity.CENTER_VERTICAL, 0, 0)
            // 仅当前页面显示，应用内浮窗无需悬浮权限
            .setShowPattern(ShowPattern.ALL_TIME)
            .show()

        LiveEventBus.get<Boolean>(ACCESSIBILITY_SERVICE_START_OR_DESTROY).observe(this){ isStart ->
//            if (isStart) {
//                EasyFloat.show(FLOAT_WINDOW_ALL_APP_TAG)
//            }else{
//                EasyFloat.hide(FLOAT_WINDOW_ALL_APP_TAG)
//            }
            upDateFloatWindowContent(isStart)
        }
    }

    /**
     * 即时更新悬浮窗子控件内容
     */
    fun upDateFloatWindowContent(isAccess: Boolean){
        // 先判断浮窗是否存在
        if (!EasyFloat.isShow(FLOAT_WINDOW_ALL_APP_TAG)) return
        // 获取浮窗根布局
        val root = EasyFloat.getFloatView(FLOAT_WINDOW_ALL_APP_TAG) ?: return

        root.findViewById<PostFloatView>(R.id.pv_window_content_view).apply {
            setIsStartAccess(isAccess)
        }

    }

    fun getRemainsCount(tv: TextView): Int {
        tv.text.toString().let {
            if (it.isEmpty()) {
                showToast("请输入有效可发布次数")
                return 0
            }
            return it.toInt()
        }
    }


    override fun getLayoutId() = R.layout.more_activity_touch

    override fun onDestroy() {
        super.onDestroy()
        //销毁
        if (EasyFloat.isShow(FLOAT_WINDOW_ALL_APP_TAG)) {
            EasyFloat.dismiss(FLOAT_WINDOW_ALL_APP_TAG)
        }
    }

}