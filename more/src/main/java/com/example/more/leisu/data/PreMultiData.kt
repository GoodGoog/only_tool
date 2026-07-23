package com.example.more.leisu.data

import com.example.more.accessibility.NodeWrapper
import com.example.more.leisu.isTwoNodeSame

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
 * 预览页-足球-串关，被选中的Item
 */
data class PreMultiFootballSelectedLeague(
    //tag = 左侧对伍名VS左侧队伍名
    val itemTag : String = "",

    //被选中的玩法,spf 或者 rq
    //由第一列scor值为0或者非0区分
    val scoreNodeWrapper: NodeWrapper,

    // 选中的玩法 ，最多两个
    val selectedNodes : ArrayList<NodeWrapper>

){

    override fun toString(): String {
        return "itemTag = $itemTag || + scoreNodeWrapper = $scoreNodeWrapper || selectedNodes = $selectedNodes---------------------------------"
    }

    /**
     * 更新节点数据
     */
    fun upDataClickNodeWrapper(clickNodeWrapper: NodeWrapper) : Boolean{
        //当前item是否需要从列表中移除
        var isNeedRemoveFromList = false
        if (selectedNodes.size == 1){
            //当前只有一个节点被选中
            if (isTwoNodeSame(selectedNodes[0],clickNodeWrapper)){
                //当前点击的节点已被选中了，故删除此已选中节点
                //零当前item已经没有选中的节点，需要冲selectedArray中移除
                selectedNodes.removeAt(0)
                isNeedRemoveFromList = true
            }else{
                selectedNodes.add(clickNodeWrapper)
            }
        }else if (selectedNodes.size == 2){
            //看看当前的点击节点，是否已存在与选中的节点之中
            //1.如果不在，则移除数组第一个节点，并插入新的节点
            //2.如果已在，则移除此重复点击的节点
            var position = -1
            selectedNodes.forEachIndexed { index, wrapper ->
                position = if (isTwoNodeSame(wrapper,clickNodeWrapper)){
                    //已存在
                    index
                }else{
                    //不存在
                    -1
                }
            }
            if (position<0){
                //不在，则移除数组第一个节点，并插入新的节点
                selectedNodes.add(clickNodeWrapper)
                selectedNodes.removeAt(0)
            }else{
                //已在，则移除此重复点击的节点
                selectedNodes.removeAt(position)
                //selectedNodes.add(position,clickNodeWrapper)
            }
        }
        return isNeedRemoveFromList
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
