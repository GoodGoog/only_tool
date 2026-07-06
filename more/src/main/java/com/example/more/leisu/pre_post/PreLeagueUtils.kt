package com.example.more.leisu.pre_post

import android.util.Log
import com.example.more.accessibility.AnalyzeSourceResult
import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.analyzeRecyclerView
import com.example.more.accessibility.findNodesById
import com.example.more.leisu.BaseLeisuDispatch
import com.example.more.leisu.PreJumpUtils.Companion.SUB_TAB_TITLE_MULTI
import com.example.more.leisu.PreJumpUtils.Companion.SUB_TAB_TITLE_SINGLE
import com.example.more.leisu.PreJumpUtils.Companion.TAB_TITLE_BASKETBALL
import com.example.more.leisu.PreJumpUtils.Companion.TAB_TITLE_FOOTBALL
import com.example.more.leisu.data.IDPrePostHeader
import com.example.more.leisu.data.PostConfigData

/**
 * 处理赛事预览页，刷新 切换 等
 */
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

        const val TAG = "PreLeagueUtils"
    }

    /**
     * ViewPager自带页面数据缓存机制，假如ViewPager有两个页面那么，当前处于页面2，而页面1被切换不可见
     *
     *一。如果页面1已经显示过，但是被切换，则任然能查找到页面1中，所有切换之前在窗口显示的控件节点
     *二。如果页面1未显示过，则只能查到到页面2的数据，页面1的数据为空
     */

    /**
     * 获取当前显示在窗口李的比赛信息列表
     */
    fun getCurShowingLeague(result: AnalyzeSourceResult, type: PostConfigData.ConfigType) {
        Log.d(
            TAG,
            "getCurShowingLeague: 来了一次+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
        )
        result.getAimRecyclerViewNodeWrapper(type)?.let { curRvNode ->
            curRvNode.analyzeRecyclerView().let {results ->
                Log.d(TAG, "getCurShowingLeague: itemsize = " + results.size)
                results.forEach { cResult ->
                    Log.d(TAG, "getLeagueShowOnWindow: " + cResult.nodes)
                    if (cResult.isCurItemTypeTimeFlags()){
                        Log.d(TAG, "getLeagueShowOnWindow: ------------------------时间标签")
                    }else{
                        Log.d(TAG, "getLeagueShowOnWindow: ------------------------普通item")
                    }
                }
            }
        }
    }

    /**
     * 每个RecyclerView都有一种Item类型,就是某一条单独的时间栏目
     * eg:[android.widget.TextView → 周二  07月07日 4场 → com.leisu.sports:id/tv_header →  → Rect(1080, 882 - 1080, 978) →|| clickable = → true  → || scrollable → false
     */
    fun AnalyzeSourceResult.getAimRecyclerViewNodeWrapper(type: PostConfigData.ConfigType): NodeWrapper? {
        var aimNodes: NodeWrapper? = null
        val rvNodes = findNodesById(IDPrePostHeader.id_league_lsit_recycler_view).nodes
        when (type) {
            PostConfigData.ConfigType.SingleFootball -> {
                if (rvNodes.size >= 2) {
                    aimNodes = rvNodes[0]
                }
            }

            PostConfigData.ConfigType.MultiFootball -> {
                if (rvNodes.size >= 2) {
                    aimNodes = rvNodes[1]
                }
            }

            PostConfigData.ConfigType.SingleBasketball -> {
                if (rvNodes.size == 4) {
                    aimNodes = rvNodes[2]
                }
            }

            PostConfigData.ConfigType.MultiBasketball -> {
                if (rvNodes.size == 4) {
                    aimNodes = rvNodes[3]
                }
            }
        }
        return aimNodes
    }


    /**
     * 判断当前Item是否为时间提示类型
     * android.widget.TextView → 周二  07月07日 4场 → com.leisu.sports:id/tv_header
     */
    fun AnalyzeSourceResult.isCurItemTypeTimeFlags(): Boolean {
        findNodesById(IDPrePostHeader.id_recycler_view_item_time_flags).nodes.let {
            return it.isNotEmpty()
        }
    }

}