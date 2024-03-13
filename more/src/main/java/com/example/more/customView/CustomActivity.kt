package com.example.more.customView

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.customView.clickEvent.ClickEventActivity
import com.example.more.customView.clock.ClockActivity
import com.example.more.databinding.MoreActivityCustomBinding

class CustomActivity : BaseActivity<MoreActivityCustomBinding, BaseViewModel>() {

    override fun initData(savedInstanceState: Bundle?) {
        binding.tvClock.setOnClickListener {
            startActivity(ClockActivity::class.java)
        }
        binding.tvClickEvent.setOnClickListener {
            startActivity(ClickEventActivity::class.java)
        }
        testLife()
    }

    override fun getLayoutId() = R.layout.more_activity_custom

    private fun testLife(){
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logD("onNewIntent")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD("onCreate")
    }

    override fun onStart() {
        super.onStart()
        logD("onStart")
    }

    override fun onRestart() {
        super.onRestart()
        logD("onRestart")
    }

    override fun onResume() {
        super.onResume()
        logD("onResume")
    }

    override fun onPause() {
        super.onPause()
        logD("onPause")
    }

    override fun onStop() {
        super.onStop()
        logD("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        logD("onDestroy")
    }

}