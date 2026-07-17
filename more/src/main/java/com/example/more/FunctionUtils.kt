package com.example.more

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import com.example.common.util.LogUtil
import com.example.more.leisu.PreJumpUtils.Companion.SUB_TAB_TITLE_MULTI
import com.example.more.leisu.PreJumpUtils.Companion.SUB_TAB_TITLE_SINGLE
import com.example.more.leisu.PreJumpUtils.Companion.TAB_TITLE_BASKETBALL
import com.example.more.leisu.PreJumpUtils.Companion.TAB_TITLE_FOOTBALL
import com.example.more.leisu.data.PostConfigData

fun Context.showToast(content: String?) {
    Toast.makeText(this, content, Toast.LENGTH_LONG).show()
}

fun Context.logD(msg: String?) {
    LogUtil.d(this.javaClass.getName().toString() + "|++++++++++++++++++++++++++|", msg)
}

fun Int.transToPostConfigType(): PostConfigData.ConfigType {
    return when (this) {
        0 -> {
            PostConfigData.ConfigType.SingleFootball
        }

        1 -> {
            PostConfigData.ConfigType.MultiFootball
        }

        2 -> {
            PostConfigData.ConfigType.SingleBasketball
        }

        else -> {
            PostConfigData.ConfigType.MultiBasketball
        }
    }
}

fun PostConfigData.ConfigType.transToPostArrayIndex(): Int {
    return when (this) {
        PostConfigData.ConfigType.SingleFootball -> {
            0
        }

        PostConfigData.ConfigType.MultiFootball -> {
            1
        }

        PostConfigData.ConfigType.SingleBasketball -> {
            2
        }

        PostConfigData.ConfigType.MultiBasketball -> {
            3
        }
    }
}

fun getScreenWidth(context: Context): Int {
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    wm.defaultDisplay.getMetrics(dm)
    return dm.widthPixels
}

