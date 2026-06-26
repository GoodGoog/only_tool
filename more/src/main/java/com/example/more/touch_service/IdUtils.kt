package com.example.more.touch_service

//雷速的包名
const val app_packageName_lei_su = "com.leisu.sports"

//专家号所在layout的id
const val id_layout_author_container = "com.leisu.sports:id/ll_predictor_layou"

//首页 - 底部|我的按钮 id
const val id_main_page_tab_mine = "com.leisu.sports:id/title"


//发布页的上一页，赛事列表 RecyclerView
const val id_post_entry_league_list = "com.leisu.sports:id/recycler_view"
const val id_post_entry_league_list_item_name = "com.leisu.sports:id/tv_league_name"

//一 ！！！！！！！！！！！！！！发布页

//今日剩余发布次数
const val id_post_today_remains_times = "com.leisu.sports:id/tv_remain_times"
//发布按钮
const val id_post_submit_button = "com.leisu.sports:id/tv_submit"


//一.1发布页顶部比赛基础信息信息
//联赛名称
const val id_league_name = "com.leisu.sports:id/tv_league_name"
//比赛时间
const val id_post_league_time = "com.leisu.sports:id/tv_time"
//主队/左侧队伍 名称
const val id_post_left_name = "com.leisu.sports:id/home_name"
//客队/右侧队伍 名称
const val id_post_right_name = "com.leisu.sports:id/away_name"



//一.2发布页 预测RecyclerView信息
//预测控件由RecyclerView实现,其id
const val id_post_player_detail_action = "com.leisu.sports:id/rv_player_detail"

//预测item,分两种类型 1.预测-让球，2.预测-总进球,二者之间的差异为中间的 [上方总分+下方具体数字]两个TextView
//预测-让球 预测-总进球 预测-奇偶，标题
const val id_post_prospect_item_title = "com.leisu.sports:id/tv_title"

//左侧标签名称 [主胜 \ 大 \全场]
const val id_post_prospect_left_title = "com.leisu.sports:id/tv_left"
//左侧的layout容器，包裹住[让\受让分 与 左胜赔率],并响应点击事件
const val id_post_prospect_left_layout_container = "com.leisu.sports:id/ll_left"
//左侧盘[让\受让分]
const val id_post_prospect_left_plate_value = "com.leisu.sports:id/tv_left_plate"
//[左侧胜赔率  \ 奇 ]
const val id_post_prospect_left_win_odds_value = "com.leisu.sports:id/tv_left_value"

//中间标签名称 [总] [预测-总进球玩法时出现]
const val id_post_prospect_center_title = "com.leisu.sports:id/tv_center"
//总分数值
const val id_post_prospect_center_total_score = "com.leisu.sports:id/tv_center_value"

//右边标签名称 [客胜 \ 小 \ 全场]
const val id_post_prospect_right_title = "com.leisu.sports:id/tv_right"
//右侧的layout容器，包裹住[让\受让分 与 左胜赔率],并响应点击事件
const val id_post_prospect_right_layout_container = "com.leisu.sports:id/ll_right"
//右侧盘[让\受让分]
const val id_post_prospect_right_plate_value = "com.leisu.sports:id/tv_right_plate"
//[右侧胜赔率 \ 偶 ]
const val id_post_prospect_right_win_odds_value = "com.leisu.sports:id/tv_right_value"



//一.3 发布页，具体需要发布的信息
//发布文章的标题输入框
const val id_post_title_edit = "com.leisu.sports:id/et_title"
//发布文章的前瞻输入框
const val id_post_prospect_edit = "com.leisu.sports:id/et_prospect"
//发布文章的分析输入框
const val id_post_analyse_edit = "com.leisu.sports:id/et_analyse"


//价格 [免费 \ 具体价格]
const val id_post_price = "com.leisu.sports:id/tv_price "

//！！！！！！！！！！！！！！发布页