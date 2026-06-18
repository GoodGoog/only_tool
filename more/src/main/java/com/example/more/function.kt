package com.example.more

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseAdapter
import com.example.more.bean.ApiBean
import com.example.more.databinding.MoreItemApiListBinding

fun createApiBeanList() : ArrayList<ApiBean>{
    val array = arrayListOf<ApiBean>().let { aimArray->
        arrayListOf<String>().apply {
            //参数
            add(createKeyValueMap("action", "search_song"))
            //歌曲名字
            add(createKeyValueMap("msg", ""))
            //默认一次返回30个
            add(createKeyValueMap("pageSize","7"))
        }.let { paramsMap ->
            aimArray.add(ApiBean("https://api.mmp.cc/api/kuwo", "搜索歌曲",paramsMap))
        }

        arrayListOf<String>().apply {
            add(createKeyValueMap("msg", "美打"))
        }.let { paramsMap ->
            aimArray.add(ApiBean("https://api.52vmy.cn/api/wl/s/dog", "狗屁不通文章生成",paramsMap))
        }

        arrayListOf<String>().apply {
            add(createKeyValueMap("msg", "今天周几？"))
        }.let { paramsMap ->
            aimArray.add(ApiBean("https://api.52vmy.cn/api/chat/spark", "纯货ai",paramsMap))
        }

        //最终数据再次返回
        aimArray
    }
    return array
}

/**
 * 生成字典，可获取key,value
 */
fun createKeyValueMap(key : String,value : String) : String{
    return "$key|||$value"
}

fun String.getMapKey() = this.split("|||")[0]

fun String.getMapValue() = this.split("|||")[1]

/**
 * 数组转换为网络请求参数字典
 */
fun convertStringArrayToHashMap(paramsStr : ArrayList<String>) : HashMap<String, String>{
    return HashMap<String, String>().apply {
        paramsStr.forEach { rawStr ->
            put(rawStr.getMapKey(),rawStr.getMapValue())
        }
    }
}
