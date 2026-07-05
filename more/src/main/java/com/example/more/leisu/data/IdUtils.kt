package com.example.more.leisu.data



//雷速的包名
const val app_packageName_lei_su = "com.leisu.sports"

//专家号所在layout的id
const val id_layout_author_container = "com.leisu.sports:id/ll_predictor_layou"

//首页 - 底部|我的按钮 id
const val id_main_page_tab_mine = "com.leisu.sports:id/title"

//我的 - 专家号 - 专家主页标题
//android.widget.TextView → 专家主页 → com.leisu.sports:id/toolbar_title
const val id_expert_home_page_title = "com.leisu.sports:id/toolbar_title"


//发布页的上一页，赛事列表 RecyclerView
const val id_post_entry_league_list = "com.leisu.sports:id/recycler_view"
const val id_post_entry_league_list_item_name = "com.leisu.sports:id/tv_league_name"



/**
 * 赛事选择页 ---头部信息，不在ViewPage内的id信息
 */
class IDPrePostHeader {
    companion object {
        //三1.头部信息
        //顶部 足球/篮球切换 由 HorizontalScrollView实现
        const val id_switch_basketball_football = "com.leisu.sports:id/tab_layout"
        //足球/篮球的id
        const val id_tab_basket_and_foot_ball = "com.leisu.sports:id/tv_tab_title"

        /**
         *  androidx.viewpager.widget.ViewPager →  → com.leisu.sports:id/view_page →  → Rect(0, 229 - 1080, 2400) → false → true → false
         *  android.widget.TextView → 单关 →  →  → Rect(34, 237 - 506, 342) → true → false → false
         *  android.widget.TextView → 串关 →  →  → Rect(540, 229 - 1080, 349) → true → false → false
         *  androidx.viewpager.widget.ViewPager →  → com.leisu.sports:id/view_page →  → Rect(0, 349 - 1080, 2400) → false → true → false
         *
         */

        // 竞速/分析切换按钮
        //const val id_league_choose_match_switch = "com.leisu.sports:id/iv_switch"

        //筛选按钮
        const val id_filter_league_info = "com.leisu.sports:id/iv_filter"

        //竞速切换按钮
        const val id_switch_speed = "com.leisu.sports:id/iv_switch"

        //返回按钮
        const val id_back = "com.leisu.sports:id/iv_back"

        const val id_first_level_view_pager = "com.leisu.sports:id/view_page"

        const val id_second_level_view_pager = "com.leisu.sports:id/view_page"

        const val id_league_lsit_recycler_view = "com.leisu.sports:id/recycler_view"

        /**
         *            足球  篮球                                     [用来切换ViewPager]
         *     单关1 串关1       单关2   串关2
         *     列表1 列表2       列表3   列表4
         *    别搞错了，所有按钮中一共有两个单关 两个双关
         *
         *    第一层ViewPager包含  单关1和串关1 + 列表1和列表2所在的第二层ViewPager
         *    同上,单关2和串关2 + 列表3和列表4所在在的第二层ViewPager，皆在同一层
         *    故，共有分别有2个单关，2个串关按钮，共四个含有比赛列表的RecyclerView
         */

    }
}

/**
 * 赛事选择页 ---单关赛事选择
 */
class IDPrePostSingleBalls {
    companion object {
        //三2.足/蓝球的单关页

        //比赛信息列表RecyclerView
        const val id_single_league_list_rv = "com.leisu.sports:id/recycler_view"

        //三2.足/蓝球的单关页的 ItemLayout中
//比赛名
        const val id_single_league_title = "com.leisu.sports:id/tv_league_name"

        //左侧主场队名
        const val id_single_left_home_name = "com.leisu.sports:id/home_name"

        //右侧客场队名
        const val id_single_right_away_name = "com.leisu.sports:id/away_name"

        //比赛时间
        const val id_single_start_time = "com.leisu.sports:id/tv_time"

        //本场比赛方案数量
        const val id_single_article_count = "com.leisu.sports:id/tv_scheme_count"

    }
}


/**
 * 赛事选择页-------篮球串关页
 */
class IDPrePostMultiBasketBall{
    companion object{

        //比赛信息列表RecyclerView
        const val id_multi_basket_league_list = "com.leisu.sports:id/recycler_view"

        //比赛名
        const val id_multi_basket_league_title = "com.leisu.sports:id/tv_comp_name"

        //左侧主场队名
        const val id_multi_basket_left_name = "com.leisu.sports:id/home_name"

        //右侧客场队名
        const val id_multi_basket_right_name = "com.leisu.sports:id/away_name"

        //比赛时间
        const val id_multi_basket_start_time = "com.leisu.sports:id/tv_time"

//篮球串关页 右侧队伍似乎是主队，左侧队伍为客队 ，有待确认
        /**
         *            火花 → com.leisu.sports:id/home_name          VS         狂热[主队] → com.leisu.sports:id/away_name
         * 主队 -6.5 → com.leisu.sports:id/tv_bt_rf   客胜 1.75 → com.leisu.sports:id/tv_bt_rf_away   主胜 1.65 → com.leisu.sports:id/tv_bt_rf_home
         * 总分 180.5 → com.leisu.sports:id/tv_bt_zf   大 1.66 → com.leisu.sports:id/tv_bt_zf_away   小 1.74 → com.leisu.sports:id/tv_bt_zf_home
         */
//赔率信息

//让分玩法 ||主队受让分[+5 或 -5]，统称为 home_initial_score
        const val id_multi_basket_home_initial_score = "com.leisu.sports:id/tv_bt_rf"

        //客胜赔率
        const val id_multi_basket_away_win_odds = "com.leisu.sports:id/tv_bt_rf_away"

        //主胜赔率
        const val id_multi_basket_home_win_odds = "com.leisu.sports:id/tv_bt_rf_home"

        //预测总分大小某值玩法
        const val id_multi_basket_total_score = "com.leisu.sports:id/tv_bt_zf"

        //大于预测总分时赔率
        const val id_multi_basket_greater_win_odds = "com.leisu.sports:id/tv_bt_zf_away"

        //小于预测总分时赔率
        const val id_multi_basket_smaller_win_odds = "com.leisu.sports:id/tv_bt_zf_home"

        //下一步按钮
        const val id_multi_basket_submit = "com.leisu.sports:id/tv_confirm"

    }
}


/**
 * 赛事选择页-------足球串关页
 */
class IDPrePostMultiFootball{
    companion object{
        //比赛信息列表RecyclerView
        const val id_multi_foot_league_list = "com.leisu.sports:id/recycler_view"

        //比赛名
        const val id_multi_foot_league_title = "com.leisu.sports:id/tv_comp_name"

        //左侧主场队名
        const val id_multi_foot_left_home_name = "com.leisu.sports:id/home_name"

        //右侧客场队名
        const val id_multi_foot_right_away_name = "com.leisu.sports:id/away_name"

        //比赛时间
        const val id_multi_foot_start_time = "com.leisu.sports:id/tv_time"

        /**
         *
         *  0 → com.leisu.sports:id/tv_spf    胜  1.69 → com.leisu.sports:id/tv_spf_win   平  3.0 → com.leisu.sports:id/tv_spf_flat   负  4.9 → com.leisu.sports:id/tv_spf_lose
         *  -1 → com.leisu.sports:id/tv_rq    胜  3.63 → com.leisu.sports:id/tv_rq_win    平  3.0 → com.leisu.sports:id/tv_rq_flat    负  1.92 → com.leisu.sports:id/tv_rq_lose
         *
         * 也有可能本次比赛未开放 0 分 玩法 ，则为
         *                                                               该玩法未开售 → com.leisu.sports:id/tv_spf_dns
         * -1 → com.leisu.sports:id/tv_rq    胜  3.63 → com.leisu.sports:id/tv_rq_win    平  3.0 → com.leisu.sports:id/tv_rq_flat    负  1.92 → com.leisu.sports:id/tv_rq_lose
         */
        /**
         * 赔率信息,第一列中 -1=[左侧队伍让1球，右侧队伍受让1球] |||||||| +1=[左侧队伍受让1球，右侧队伍让1球]
         * 此处中的 + - 胜 负 平 全是以主队为标准
         * 通常是左侧队伍为主队，右侧队伍为客队
         **/

        //第一列，0分=双方互不让分
        const val id_multi_foot_zero_score = "com.leisu.sports:id/tv_spf"

        //主队胜赔率
        const val id_multi_foot_zero_score_left_win_odds = "com.leisu.sports:id/tv_spf_win "

        //平局赔率
        const val id_multi_foot_zero_score_flat_odds = "com.leisu.sports:id/tv_spf_flat"

        //主队败赔率
        const val id_multi_foot_zero_score_left_lose_odds = "com.leisu.sports:id/tv_spf_lose"


        //未开放0分玩法时显示 该玩法未开售
        const val id_multi_foot_zero_no_sale = "com.leisu.sports:id/tv_spf_dns"


        //第二列，主队 让/受让分[-x / +x]
        const val id_multi_foot_left_extra_score = "com.leisu.sports:id/tv_rq"

        //主队胜赔率
        const val id_multi_foot_left_extra_score_win_odds = "com.leisu.sports:id/tv_rq_win"

        //平局赔率
        const val id_multi_foot_flat_odds = "com.leisu.sports:id/tv_rq_flat"

        //主队败赔率
        const val id_multi_foot_left_extra_score_lose_odds = "com.leisu.sports:id/tv_rq_lose"

        //下一步按钮
        const val id_multi_foot_submit = "com.leisu.sports:id/tv_confirm"
    }
}



//-----------------------------------------------------分割线-----------------------------------------------------------
/**
 * 一 ！！！！！！！！！！！！！！单关！！！！！！！！！！！！！发布页
 */
class IDPostDoubleSingle {
    companion object {
        //今日剩余发布次数
        const val id_single_post_today_remains_times = "com.leisu.sports:id/tv_remain_times"

        //发布按钮
        const val id_single_post_submit_button = "com.leisu.sports:id/tv_submit"


        //一.1发布页顶部比赛基础信息信息
//联赛名称
        const val id_single_league_name = "com.leisu.sports:id/tv_league_name"

        //比赛时间
        const val id_single_post_league_time = "com.leisu.sports:id/tv_time"

        //主队/左侧队伍 名称
        const val id_single_post_left_name = "com.leisu.sports:id/home_name"

        //客队/右侧队伍 名称
        const val id_single_post_right_name = "com.leisu.sports:id/away_name"


        //一.2发布页 预测RecyclerView信息
//预测控件由RecyclerView实现,其id
        const val id_single_post_player_detail_action = "com.leisu.sports:id/rv_player_detail"

        //预测item,分两种类型 1.预测-让球，2.预测-总进球,二者之间的差异为中间的 [上方总分+下方具体数字]两个TextView
//预测-让球 预测-总进球 预测-奇偶，标题
        const val id_single_post_prospect_item_title = "com.leisu.sports:id/tv_title"

        //左侧标签名称 [主胜 \ 大 \全场]
        const val id_single_post_prospect_left_title = "com.leisu.sports:id/tv_left"

        //左侧的layout容器，包裹住[让\受让分 与 左胜赔率],并响应点击事件
        const val id_single_post_prospect_left_layout_container = "com.leisu.sports:id/ll_left"

        //左侧盘[让\受让分]
        const val id_single_post_prospect_left_plate_value = "com.leisu.sports:id/tv_left_plate"

        //[左侧胜赔率  \ 奇 ]
        const val id_single_post_prospect_left_win_odds_value = "com.leisu.sports:id/tv_left_value"

        //中间标签名称 [总] [预测-总进球玩法时出现]
        const val id_single_post_prospect_center_title = "com.leisu.sports:id/tv_center"

        //总分数值
        const val id_single_post_prospect_center_total_score = "com.leisu.sports:id/tv_center_value"

        //右边标签名称 [客胜 \ 小 \ 全场]
        const val id_single_post_prospect_right_title = "com.leisu.sports:id/tv_right"

        //右侧的layout容器，包裹住[让\受让分 与 左胜赔率],并响应点击事件
        const val id_single_post_prospect_right_layout_container = "com.leisu.sports:id/ll_right"

        //右侧盘[让\受让分]
        const val id_single_post_prospect_right_plate_value = "com.leisu.sports:id/tv_right_plate"

        //[右侧胜赔率 \ 偶 ]
        const val id_single_post_prospect_right_win_odds_value =
            "com.leisu.sports:id/tv_right_value"


        //一.3 发布页，具体需要发布的信息
//发布文章的标题输入框
        const val id_single_post_title_edit = "com.leisu.sports:id/et_title"

        //发布文章的前瞻输入框
        const val id_single_post_prospect_edit = "com.leisu.sports:id/et_prospect"

        //发布文章的分析输入框
        const val id_single_post_analyse_edit = "com.leisu.sports:id/et_analyse"


        //价格 [免费 \ 具体价格]
        const val id_single_post_price = "com.leisu.sports:id/tv_price "

//！！！！！！！！！！！！！！单关发布页


    }
}


//-----------------------------------------------------分割线-----------------------------------------------------------

/**
 * ！！！！！！！！！！！！串关发布页
 */
class IDPostMultiDouble{
    companion object{

        //今日剩余发布次数
        const val id_multi_post_today_remains_times = "com.leisu.sports:id/tv_remain_times"

        //发布按钮
        const val id_multi_post_submit_button = "com.leisu.sports:id/tv_submit"

        //价格 [免费 \ 具体价格]
        const val id_multi_post_price = "com.leisu.sports:id/tv_price "


        //二.1 串关-发布页，具体需要发布的信息
        //发布文章的标题输入框
        const val id_multi_post_title_edit = "com.leisu.sports:id/et_title"
        //发布文章的前瞻输入框
        const val id_multi_post_prospect_edit = "com.leisu.sports:id/et_prospect"

    }
}

