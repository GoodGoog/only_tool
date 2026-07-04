package com.example.more.leisu

import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.findNodeByText
import com.example.more.leisu.data.PostConfigData
import kotlin.Unit

class PostJumpUtils private constructor() {
    companion object {
        const val TAB_TITLE_BASKETBALL = "篮球"
        const val TAB_TITLE_FOOTBALL = "足球"
        const val SUB_TAB_TITLE_SINGLE = "单关"
        const val SUB_TAB_TITLE_MULTI = "串关"

        private var instance: PostJumpUtils? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PostJumpUtils {
            if (instance == null) {
                instance = PostJumpUtils()
            }
            return instance!!
        }
    }

    //每次进入到专家主页，无论是外部进入|比赛选择页退出，都设置为true
    //以至于下一次在信息列表选择页产生window_status_change事件时，判断是否需要跳转至对应的子页面
    //true 需要跳转, false反之
    var hasJumpExpertHomeAction = true

    //默认实在足球-单关页面
    var curPageType = PostConfigData.ConfigType.SingleFootball

    fun jumpSubPage(type: PostConfigData.ConfigType, result: AnalyzeSourceResult,doAfterFinish : () -> Unit) {
        when (type) {
            PostConfigData.ConfigType.SingleBasketball -> {
                result.jump(TAB_TITLE_BASKETBALL,SUB_TAB_TITLE_SINGLE,doAfterFinish)
            }

            PostConfigData.ConfigType.SingleFootball -> {
                result.jump(TAB_TITLE_FOOTBALL,SUB_TAB_TITLE_SINGLE,doAfterFinish)
            }

            PostConfigData.ConfigType.MultiBasketball -> {
                result.jump(TAB_TITLE_BASKETBALL,SUB_TAB_TITLE_MULTI,doAfterFinish)
            }

            PostConfigData.ConfigType.MultiFootball -> {
                result.jump(TAB_TITLE_FOOTBALL,SUB_TAB_TITLE_MULTI,doAfterFinish)
            }
        }
        curPageType = type
    }

    fun AnalyzeSourceResult.jump(title: String, subTitle : String,doAfterFinish : () -> Unit){
        findNodeByText(title).delayClickAndShowHighLight(){
            //连续点击时,需要等待上安妮执行完成,避免拥挤
            findNodeByText(subTitle).delayClickAndShowHighLight(){
                    doAfterFinish.invoke()
            }
        }
    }
}