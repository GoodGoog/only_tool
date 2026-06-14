package com.example.more.setting

import android.util.Log
import com.example.common.util.showToast
import kotlin.let
import kotlin.math.abs

const val TEAM_CUP_NAME = "TEAM_CUP_NAME"
const val TEAM_LEFT_NAME = "TEAM_LEFT_NAME"
const val TEAM_RIGHT_NAME = "TEAM_RIGHT_NAME"
const val TEAM_LEFT_RAW_SCORE = "TEAM_LEFT_RAW_SCORE"

const val TEAM_FLOAT_WINDOW_TRAM_MATCH_INFO = "TEAM_FLOAT_WINDOW_TRAM_MATCH_INFO"

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

//由分数判断是否受让
fun judgeLeftTeamScoreTips(
    leftTeamName: String,
    rScore: Float,
    endSign: String
): String {
    //为0什么都不做
    if (rScore == 0.toFloat()) return ""
    Log.d("judgeLeftTeamScoreTips", "到底怎么回事啊!!!!!! rScore = ${rScore}: ")
    return leftTeamName +
            (if (rScore > 0) "受让" else "让") +
            "${abs(rScore)}" + "分" + endSign
}

