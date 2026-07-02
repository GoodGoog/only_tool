package com.example.more.accessibility

import android.provider.ContactsContract
import android.util.Log
import com.example.more.leisu.LeisuServiceDispatch
import com.example.more.leisu.data.PostDataCenter
import com.example.more.leisu.data.app_packageName_lei_su
import com.example.more.touch.TouchActivity

class AllAppService : FastAccessibilityService() {
    companion object {
        private const val TAG = "AllAppService"

    }

    //是否监听APP的标记，不重写默认不监听
    override val enableListenApp = true

    override fun analyzeCallBack(wrapper: EventWrapper?, result: AnalyzeSourceResult) {
        wrapper?.let { it ->
            //当前所在app
            when (it.packageName) {
                app_packageName_lei_su -> {
                    LeisuServiceDispatch.instance().refresh(it,result)
                }

                else -> {}
            }
        }
    }


    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
    }

}