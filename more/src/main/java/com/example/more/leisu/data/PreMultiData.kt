package com.example.more.leisu.data

/**
 * 篮球单关页 中 左主队  右  客队
 */
//单关-篮球，预测-让分 玩法
data class PreMultiFootBallData(
    //赛事名
    val leagueName: String = "",

    //赛事开始时间
    val leagueStartTime: String = "",

    //左侧队伍名  主队
    val leftTeamName: String = "",

    //右侧队伍名  客队
    val rightTeamName: String = "",

    //spf 为主客队互不让分
    val spfScore : String = "0",
    //主队胜赔率
    val spfWinValue : String = "",
    //平局赔率
    val spfFlatValue : String = "",
    //主队败赔率
    val spfLoseValue : String = "",

    //rq 为主队 让分-3 或者 受让分+3
    val rqScore : String = "0",
    //主队胜赔率
    val rqWinValue : String = "",
    //平局赔率
    val rqFlatValue : String = "",
    //主队败赔率
    val rqLoseValue : String = "",
) {
    override fun toString(): String {
        return "leagueName= $leagueName ||leagueStartTime = $leagueStartTime||leftTeamName = $leftTeamName ||rightTeamName = $rightTeamName \n" +
                "spfScore = $spfScore ||spfWinValue = $spfWinValue||spfFlatValue = $spfFlatValue ||spfLoseValue = $spfLoseValue \n" +
                "rqScore = $rqScore ||rqWinValue = $rqWinValue||rqFlatValue = $rqFlatValue ||rqLoseValue = $rqLoseValue"
    }
}
