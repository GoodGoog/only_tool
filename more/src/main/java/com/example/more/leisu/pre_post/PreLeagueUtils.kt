package com.example.more.leisu.pre_post

class PreLeagueUtils private constructor() {
    companion object {
        private var instance: PreLeagueUtils? = null

        // synchronized 保证多线程安全
        @Synchronized
        fun instance(): PreLeagueUtils {
            if (instance == null) {
                instance = PreLeagueUtils()
            }
            return instance!!
        }
    }


}