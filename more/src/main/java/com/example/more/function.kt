package com.example.more

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseAdapter
import com.example.more.bean.ApiBean
import com.example.more.databinding.MoreItemApiListBinding

const val KEY_VALUE_SEPARATOR = "****"

const val KEY_VALUE_LIST_SEPARATOR = "^^^^"

fun createApiBeanList(): ArrayList<ApiBean> {
    val array = arrayListOf<ApiBean>().let { aimArray ->
        arrayListOf<String>().apply {
            //参数
            add(createKeyValueMap("action", arrayOf("search_song")))
            //歌曲名字
            add(createKeyValueMap("msg", arrayOf("一笑江湖", "吻别", "无所谓")))
            //默认一次返回30个
            add(createKeyValueMap("pageSize", arrayOf("7")))
        }.let { paramsMap ->
            aimArray.add(ApiBean("https://api.mmp.cc/api/kuwo", "搜索歌曲", paramsMap))
        }

        arrayListOf<String>().apply {
            add(createKeyValueMap("msg", arrayOf("美打", "猛男", "小丑")))
        }.let { paramsMap ->
            aimArray.add(
                ApiBean(
                    "https://api.52vmy.cn/api/wl/s/dog",
                    "狗屁不通文章生成",
                    paramsMap
                )
            )
        }

        arrayListOf<String>().apply {
            add(createKeyValueMap("msg", arrayOf("今天周几？", "你多大了", "不告诉你")))
        }.let { paramsMap ->
            aimArray.add(ApiBean("https://api.52vmy.cn/api/chat/spark", "纯货ai", paramsMap))
        }

        //最终数据再次返回
        aimArray
    }
    return array
}

/**
 * 生成字典，可获取key,value
 */
fun createKeyValueMap(key: String, strArray: Array<String>): String {
    var value = ""
    if (strArray.isEmpty()) value = ""
    if (strArray.size == 1) {
        //参数为1，不需要加入分隔符
        value = strArray[0]
    } else if (strArray.size > 1) {
        value += strArray[0]
        for (i in 1 until strArray.size) {
            value += KEY_VALUE_LIST_SEPARATOR + strArray[i]
        }
    }
    return key + KEY_VALUE_SEPARATOR + value
}

fun createKeyValueMap(key: String, newValue: String): String =
    createKeyValueMap(key, arrayOf(newValue))


fun String.getMapKey() = this.split(KEY_VALUE_SEPARATOR)[0]

fun String.getMapValueArrayString() = this.split(KEY_VALUE_SEPARATOR)[1]

/**
 * 数组转换为网络请求参数字典
 */
fun convertStringArrayToHashMap(paramsStr: ArrayList<String>): HashMap<String, String> {
    return HashMap<String, String>().apply {
        paramsStr.forEach { rawStr ->
            put(rawStr.getMapKey(), rawStr.getMapValueArrayString())
        }
    }
}

/**
 * 根据现有参数值拼接的字符串，获得第一个参数值用于展示
 */
fun getFirstValue(valuesStr: String): String {
    convertValueStringToValueList(valuesStr).let { valuesArray ->
        return if (valuesArray.isEmpty()) {
            ""
        } else {
            valuesArray[0]
        }
    }
}

/**
 * 拆分默认参数列表
 */
fun convertValueStringToValueList(valueStr: String): ArrayList<String> {
    ArrayList<String>().apply {
        valueStr.split(KEY_VALUE_LIST_SEPARATOR).forEach { it ->
            add(it)
        }
    }.let {
        return it
    }
}