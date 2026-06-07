package com.example.more.setting

const val TEAM_CUP_NAME = "TEAM_CUP_NAME"
const val TEAM_LEFT_NAME = "TEAM_LEFT_NAME"
const val TEAM_RIGHT_NAME = "TEAM_RIGHT_NAME"

//分析前瞻默认前缀缓存
const val ANALYSE_HEAD_SHARED_PREFERENCE = "ANALYSE_HEAD_SHARED_PREFERENCE"
const val ANALYSE_HEAD_CACHE = "ANALYSE_HEAD_CACHE"

fun splitStringToStrArray(raw: String, separator: String): ArrayList<String> {
    return ArrayList<String>().apply {
        if (raw.isEmpty()) return@apply
        raw.split(separator).let {
            for (str in it)
                add(str)
        }
    }
}

