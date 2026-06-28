package com.example.more.post_service

import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.EventWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.findNodeById

/**
 * 收费单关发布
 */
class PostPaySingleServiceHandle(val mWrapper: EventWrapper, val result: AnalyzeSourceResult) {

    companion object{
        const val TAG = "PostPaySingleServiceHandle"
    }

    init {

    }

    fun execute(){
        Log.d(TAG, "execute: 执行多少次")
        when (mWrapper.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                //解析rv子视图
                result.findNodeById(id_single_post_player_detail_action).analyzeRecyclerView()
                    ?.forEach { mResult ->
                        val itemTitle =
                            mResult.findNodeById(id_single_post_prospect_item_title)?.text.blankOrThis()
                        when (itemTitle) {
                            "预测-让球" -> {
//                                mResult.findNodeById(id_post_prospect_left_layout_container).click()
                            }

                            "预测-总进球" -> {
                                //mResult.findNodeById(id_post_prospect_right_layout_container).click()
                            }

                            else -> {}
                        }

                    }
            }

            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                //Log.d(TAG, "onSinglePostPage: 点击事件" + result)
                //点击事件
                //result.findNodeById(id_post_submit_button).click()
            }

            else -> {}
        }
    }
}