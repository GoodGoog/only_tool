package com.example.more

/**
 * FloatWindow 悬浮窗标识
 */

class EasyFloatTag {
    companion object {

        //操作信息窗口
        const val FLOAT_WINDOW_ALL_APP_TAG = "FLOAT_WINDOW_ALL_APP_TAG"

        //全屏高光提示窗口
        const val FLOAT_WINDOW_HIGH_LIGHT_BOX = "FLOAT_WINDOW_HIGH_LIGHT_BOX"
    }
}


/**
 * LiveEventBus
 */

class EventBusTag {
    companion object {
        //无障碍服务开启和结束
        const val ACCESSIBILITY_SERVICE_START_OR_DESTROY = "ACCESSIBILITY_SERVICE_START_OR_DESTROY"

        const val EVENT_BUS_CLICKED_AREA_HIGH_LIGHT_BOX = "EVENT_BUS_CLICKED_AREA_HIGH_LIGHT_BOX"

        //测试赛事列表页页面切换
        const val TEST_PRE_POST_PAGE_SWITCH = "TEST_PRE_POST_PAGE_SWITCH"

        //开始发布
        //true为发布，false为停止
        const val START_OR_STOP_CUR_AUTO_POST = "START_OR_STOP_CUR_AUTO_POST"

        //传递向AI提问的问题
        const val POST_CHARGE_QUESTION_TO_AI = "POST_CHARGE_QUESTION_TO_AI"

        //传递向AI回答
        const val POST_CHARGE_ANSWER_FROM_AI = "POST_CHARGE_ANSWER_FROM_AI"

    }
}

