package com.example.more.leisu.data

import com.example.more.leisu.getWeekDayByCalendar

class PostDataCenter private constructor() {
    companion object {

        private var instance: PostDataCenter? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PostDataCenter {
            if (instance == null) {
                instance = PostDataCenter()
            }
            return instance!!
        }

    }

    val postArray = ArrayList<PostConfigData>()

    init {
        var defaultTimes = 0
        val date = getWeekDayByCalendar()
        if (date in intArrayOf(1, 2, 3, 4)) {
            //周一到周四,默认七次
            defaultTimes = 7
        } else if (date in intArrayOf(5, 6, 7)) {
            //周五到周天，默认十次
            defaultTimes = 10
        }

        postArray.apply {
            add(PostConfigData(PostConfigData.ConfigType.SingleBasketball,
                "篮球|单关",
                true,
                true,
                defaultTimes
            ))
            add(PostConfigData(PostConfigData.ConfigType.MultiBasketball,
                "篮球|串关",
                true,
                true,
                defaultTimes
            ))
            add(PostConfigData(PostConfigData.ConfigType.SingleFootball,
                "足球|单关",
                true,
                true,
                defaultTimes
            ))
            add(PostConfigData(PostConfigData.ConfigType.MultiFootball,
                "足球|串关",
                true,
                true,
                defaultTimes
            ))
        }

    }

    //打印当前信息校验
    fun printMsg() : String {
        var msg = ""
        postArray.forEach {
            it.apply {
                msg += "postType :" + type.toString() + "-->title:"+ title +"-->isFree: $isFree" +"--> isPost:$isPost" +"--> times :$postTimes" +"\n"
            }
        }
        return msg
    }
}