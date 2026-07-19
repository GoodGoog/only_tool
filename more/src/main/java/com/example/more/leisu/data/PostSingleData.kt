package com.example.more.leisu.data

/**
 * 篮球单关页 中 左客队  右  主队
 */
//单关-篮球，预测-让分 玩法
data class PostSingleBasketBallHandicapTypeData(
    //赛事名
    val leagueName: String = "",

    //赛事开始时间
    val leagueStartTime: String = "",

    //左侧队伍名  客队
    val leftTeamName: String = "",

    //右侧队伍名  主队
    val rightTeamName: String = "",

    //左侧队伍/客队  受让球/让求 +3 或 -3
    //com.leisu.sports:id/tv_left_plate
    val leftPlate: String = "",

    //左侧队伍/客队 胜时赔率
    //com.leisu.sports:id/tv_left_value
    val leftValue: String = "",

    //右侧队伍/主队  让球/受让求 -3 或 +3
    //com.leisu.sports:id/tv_right_plate
    val rightPlate: String = "",

    //右侧队伍/主队 胜时赔率
    //com.leisu.sports:id/tv_right_value
    val rightValue: String = "",
) {
    override fun toString(): String {
        return "leagueName= $leagueName ||leagueStartTime = $leagueStartTime||leftTeamName = $leftTeamName ||rightTeamName = $rightTeamName \n" +
                "leftPlate = $leftPlate ||leftValue = $leftValue||rightPlate = $rightPlate ||rightValue = $rightValue"
    }
}

//单关-篮球，预测-总分 玩法
data class PostSingleBasketBallTotalScoreTypeData(
    //赛事名
    val leagueName: String = "",

    //赛事开始时间
    val leagueStartTime: String = "",

    //左侧队伍名  客队
    val leftTeamName: String = "",

    //右侧队伍名  主队
    val rightTeamName: String = "",

    //总分大于预测分时赔率    com.leisu.sports:id/tv_left_value
    val biggerThanTotalValue: String = "",
    //预测总分              com.leisu.sports:id/tv_center_value
    val totalScore: String = "",
    //总分小于预测分时赔率    com.leisu.sports:id/tv_right_value
    val smallerThanTotalValue: String = "",
) {
    override fun toString(): String {
        return "leagueName= $leagueName ||leagueStartTime = $leagueStartTime||leftTeamName = $leftTeamName ||rightTeamName = $rightTeamName \n" +
                "biggerThanTotalValue = $biggerThanTotalValue ||totalScore = $totalScore||smallerThanTotalValue = $smallerThanTotalValue"
    }
}