package com.example.more.leisu.data

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

}