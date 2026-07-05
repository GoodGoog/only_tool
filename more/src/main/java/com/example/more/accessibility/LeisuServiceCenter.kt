package com.example.more.accessibility

class LeisuServiceCenter private constructor() {
    companion object {
        private var instance: LeisuServiceCenter? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): LeisuServiceCenter {
            if (instance == null) {
                instance = LeisuServiceCenter()
            }
            return instance!!
        }

    }

    //无障碍服务连接状态
    var isAccessServiceConnect = false

    var result: AnalyzeSourceResult = AnalyzeSourceResult()

}
