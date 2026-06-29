package com.example.more.leisu

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


    //剩余可发布次数
    //依次为 单篮 单足 串蓝 串足
    val remainTimesArray = intArrayOf(0,0,0,0)

    //是否收费
    //依次为 单篮 单足 串蓝 串足
    val isFreeArray = booleanArrayOf(true,true,true,true)

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

        remainTimesArray[PostType.SINGLE_BASKETBALL] = defaultTimes
        remainTimesArray[PostType.SINGLE_FOOTBALL] = defaultTimes
        remainTimesArray[PostType.MULTI_BASKETBALL] = defaultTimes
        remainTimesArray[PostType.MULTI_FOOTBALL] = defaultTimes
    }

    //打印当前信息校验
    fun printMsg() : String{
        return "篮球|单关:" + isFreeArray[PostType.SINGLE_BASKETBALL] + "__" + remainTimesArray[PostType.SINGLE_BASKETBALL] + "\n" +
                "足球|单关:" + isFreeArray[PostType.SINGLE_FOOTBALL] + "__" + remainTimesArray[PostType.SINGLE_FOOTBALL] + "\n" +
                "篮球|串关:" + isFreeArray[PostType.MULTI_BASKETBALL] + "__" + remainTimesArray[PostType.MULTI_BASKETBALL] + "\n" +
                "足球|串关:" + isFreeArray[PostType.MULTI_FOOTBALL] + "__" + remainTimesArray[PostType.MULTI_FOOTBALL]
    }

    class PostType {
        companion object{
            const val SINGLE_BASKETBALL = 0
            const val SINGLE_FOOTBALL = 1
            const val MULTI_BASKETBALL = 2
            const val MULTI_FOOTBALL = 3
        }
    }
}