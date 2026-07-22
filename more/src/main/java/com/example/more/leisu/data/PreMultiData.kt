package com.example.more.leisu.data

/**
 * 足球串关预览页 中 左主队  右  客队
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

    //当前选择哪一种玩法  默认为第一行玩法，双方互不让分
    var curIsSpf: Boolean = true,

    val subDataArray: ArrayList<PreMultiFootBallSubData>,

) {
    override fun toString(): String {
        return "leagueName= $leagueName ||leagueStartTime = $leagueStartTime||leftTeamName = $leftTeamName ||rightTeamName = $rightTeamName || curIsSpf = $curIsSpf \n" +
                 subDataArray.let {
                     var subDataArrayStr = ""
                     it.forEach { subData ->
                         subDataArrayStr += subData.toString() + "\n"
                     }
                     subDataArrayStr
                 }
    }

    fun initData() {

    }
}

/**
 * 预览页-串关-足球的一种玩法
 */
data class PreMultiFootBallSubData(
    //spf 为主客队互不让分
    //rq 为主队 让分-3 或者 受让分+3
    val isSpf: Boolean = true,

    //spf 为主客队互不让分
    val score: String = "0",

    //spf未开放此玩法 [文本不为空时表示未开放]
    val notOpenText: String = "",

    //主队胜赔率
    val winValue: String = "",

    //平局赔率
    val flatValue: String = "",

    //主队败赔率
    val loseValue: String = "",
) {
    override fun toString(): String {
        return "isSpf = $isSpf || score = $score || notOpenText = $notOpenText ||winValue = $winValue||flatValue = $flatValue ||loseValue = $loseValue"
    }
}


/**
 * 篮球串关预览页 中 左客  右  主队
 */
//单关-篮球，预测-让分 玩法
data class PreMultiBasketBallData(
    //赛事名
    val leagueName: String = "",
    //赛事开始时间
    val leagueStartTime: String = "",
    //左侧队伍名  客队
    val leftTeamName: String = "",
    //右侧队伍名  主队
    val rightTeamName: String = "",

    //当前选择哪一种玩法 ,默认让分玩法
    var isCurHandicap: Boolean = true,

    val subDataArray: ArrayList<PreMultiBasketBallSubData>,

    ) {
    override fun toString(): String {
        return "leagueName= $leagueName ||leagueStartTime = $leagueStartTime||leftTeamName = $leftTeamName ||rightTeamName = $rightTeamName || isCurHandicap = $isCurHandicap \n" +
                subDataArray.let {
                    var subDataArrayStr = ""
                    it.forEach { subData ->
                        subDataArrayStr += subData.toString() + "\n"
                    }
                    subDataArrayStr
                }
    }

    fun initData() {

    }
}

/**
 * 预览页-串关-篮球的一种玩法
 * 共有主队让分 或者 预测总分大小
 */
data class PreMultiBasketBallSubData(
    //handicap 主 让分/受让分 , win表示主队赢，lose表示主队输
    //total 为总分比较大小，win表示大于预测总分，lose表示结果小于预测总分
    val isHandicap: Boolean = true,

    //spf 为主客队互不让分
    val score: String = "0",

    //spf未开放此玩法 [文本不为空时表示未开放]
    val notOpenText: String = "",

    //主队胜赔率
    val winValue: String = "",

    //主队败赔率
    val loseValue: String = "",
) {
    override fun toString(): String {
        return "isHandicap = $isHandicap || score = $score || notOpenText = $notOpenText ||winValue = $winValue|| loseValue = $loseValue"
    }
}
