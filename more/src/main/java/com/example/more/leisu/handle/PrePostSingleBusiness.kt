package com.example.more.leisu.handle

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.leisu.data.PrePostDataCenter

class PrePostSingleBusiness(val eventWrapper: EventWrapper, val result: AnalyzeSourceResult) {

    companion object {
        const val TAG = "PrePostSingleBusiness"
    }

    init {

    }

    fun execute() {

        when (eventWrapper.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                PrePostDataCenter.Companion.instance().jumpSubPage(PrePostDataCenter.Companion.PrePostType.SINGLE_BASKETBALL,result)
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                //Log.d(TAG, "onSinglePostPage: 点击事件" + result)
                //点击事件
                //result.findNodeById(id_post_submit_button).click()
                Log.d(TAG, "execute: 点击事件来了")
            }

            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {

            }

            else -> {}
        }
    }


}