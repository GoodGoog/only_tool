package com.example.more.leisu

import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.delayClick
import com.example.more.accessibility.findNodeByText
import com.example.more.leisu.data.PostConfigData

class PostUtils private constructor() {
    companion object {
        const val TAB_TITLE_BASKETBALL = "篮球"
        const val TAB_TITLE_FOOTBALL = "足球"
        const val SUB_TAB_TITLE_SINGLE = "单关"
        const val SUB_TAB_TITLE_MULTI = "串关"

        private var instance: PostUtils? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PostUtils {
            if (instance == null) {
                instance = PostUtils()
            }
            return instance!!
        }
    }

    //默认实在足球-单关页面
    var curPageType = PostConfigData.ConfigType.SingleFootball

    fun jumpSubPage(type: PostConfigData.ConfigType, result: AnalyzeSourceResult) {
        when (type) {
            PostConfigData.ConfigType.SingleBasketball -> {
                result.jump(TAB_TITLE_BASKETBALL,SUB_TAB_TITLE_SINGLE)
            }

            PostConfigData.ConfigType.SingleFootball -> {
                result.jump(TAB_TITLE_FOOTBALL,SUB_TAB_TITLE_SINGLE)
            }

            PostConfigData.ConfigType.MultiBasketball -> {
                result.jump(TAB_TITLE_BASKETBALL,SUB_TAB_TITLE_MULTI)
            }

            PostConfigData.ConfigType.MultiFootball -> {
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