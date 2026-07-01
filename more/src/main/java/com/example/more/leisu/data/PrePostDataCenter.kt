package com.example.more.leisu.data

import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.delayClick
import com.example.more.accessibility.findNodeByText

class PrePostDataCenter private constructor() {
    companion object {
        enum class PrePostType {
            SINGLE_BASKETBALL,
            SINGLE_FOOTBALL,
            MULTI_BASKETBALL,
            MULTI_FOOTBALL
        }

        const val TAB_TITLE_BASKETBALL = "篮球"
        const val TAB_TITLE_FOOTBALL = "足球"
        const val SUB_TAB_TITLE_SINGLE = "单关"
        const val SUB_TAB_TITLE_MULTI = "串关"

        private var instance: PrePostDataCenter? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PrePostDataCenter {
            if (instance == null) {
                instance = PrePostDataCenter()
            }
            return instance!!
        }
    }

    var curPageType = PrePostType.SINGLE_BASKETBALL

    fun jumpSubPage(type: PrePostType,result: AnalyzeSourceResult) {
        when (type) {
            PrePostType.SINGLE_BASKETBALL -> {
                result.jump(TAB_TITLE_BASKETBALL,SUB_TAB_TITLE_SINGLE)
            }

            PrePostType.SINGLE_FOOTBALL -> {
                result.jump(TAB_TITLE_FOOTBALL,SUB_TAB_TITLE_SINGLE)
            }

            PrePostType.MULTI_BASKETBALL -> {
                result.jump(TAB_TITLE_BASKETBALL,SUB_TAB_TITLE_MULTI)
            }

            PrePostType.MULTI_FOOTBALL -> {
                result.jump(TAB_TITLE_FOOTBALL,SUB_TAB_TITLE_MULTI)
            }
        }
        curPageType = type
    }

    fun AnalyzeSourceResult.jump(title: String, subTitle : String){
        findNodeByText(title).delayClick()
        findNodeByText(subTitle).delayClick()
    }
}