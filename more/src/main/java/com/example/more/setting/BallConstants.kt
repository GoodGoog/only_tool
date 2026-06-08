package com.example.more.setting

import kotlin.let
import kotlin.math.abs

const val TEAM_CUP_NAME = "TEAM_CUP_NAME"
const val TEAM_LEFT_NAME = "TEAM_LEFT_NAME"
const val TEAM_RIGHT_NAME = "TEAM_RIGHT_NAME"

const val TEAM_LEFT_RAW_SCORE = "TEAM_LEFT_RAW_SCORE"

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
fun judgeLeftTeamScoreTips(leftTeamName : String, rScore : Int) : String{
    var leftTeamRawScoreTips = ""
    leftTeamRawScoreTips.let { rawScore ->
        leftTeamRawScoreTips = "如果" + leftTeamName +
                (if (rScore > 0) "受让" else "让") +
                "${abs(rScore)}" + "分，"
    }
    return leftTeamRawScoreTips
}

