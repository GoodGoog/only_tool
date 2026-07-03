package com.example.more.leisu.data

import android.util.Log
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


    var initialArray = ArrayList<PostConfigData>()

    //必须被强引用，否则切换app时可能被销毁
    var postArray = ArrayList<PostConfigData>()

    var isTaskVisualized = true


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

        initialArray.apply {
            add(
                PostConfigData(
                    PostConfigData.ConfigType.SingleFootball,
                    "足球|单关",
                    true,
                    true,
                    defaultTimes
                )
            )
            add(
                PostConfigData(
                    PostConfigData.ConfigType.SingleBasketball,
                    "篮球|单关",
                    true,
                    true,
                    defaultTimes
                )
            )
            add(
                PostConfigData(
                    PostConfigData.ConfigType.MultiFootball,
                    "足球|串关",
                    true,
                    true,
                    defaultTimes
                )
            )
            add(
                PostConfigData(
                    PostConfigData.ConfigType.MultiBasketball,
                    "篮球|串关",
                    true,
                    true,
                    defaultTimes
                )
            )
        }
    }

    //发布了一次
    fun postOneTime(postData: PostConfigData) {
        postArray.forEachIndexed { index, it ->
            if (it.type == postData.type) {
                if (it.postTimes - 1 == 0) postArray.removeAt(index)
                else postArray[index].postTimes -= 1
            }
        }
    }

    //过滤需要发布的数据
    fun filterUselessPostInfo(hasData: (Boolean) -> Unit) {
        //可能会多次点击保存，每次执行前先清空
        postArray.clear()
        initialArray.forEach {
            if (it.isPost) {
                postArray.add(PostConfigData(it.type,it.title,it.isPost,it.isFree,it.postTimes))
            }
        }
        if (postArray.isEmpty()) hasData.invoke(false)
        else hasData.invoke(true)
    }

    //打印当前信息校验
    fun printMsg(isPostArray: Boolean): String {
        var msg = ""
        val aimArray = if (isPostArray) postArray else initialArray
        aimArray.forEach {
            it.apply {
                msg += "postType :$type-->title:$title-->isFree: $isFree--> isPost:$isPost--> times :$postTimes\n"
            }
        }
        return msg
    }

    fun changeTaskVisualized(){
        isTaskVisualized = !isTaskVisualized
    }

}