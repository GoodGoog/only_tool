package com.example.more.post_service

import com.example.more.accessibility.NodeWrapper
import com.example.more.accessibility.blankOrThis
import com.example.more.accessibility.click
import com.example.more.accessibility.findNodeById
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * 生成一个随机数
 */
fun getRandomInt() :Int{
    // [0~99] + 1 → [1~100]
    return Random.nextInt(100) + 1
}

/**
 * 提取字符串中数字
 */
fun String?.filterNumberOrZero(): Int{
    if (this == null) return 0
    if (this.isEmpty()) return 0
    return this.blankOrThis().filter {
        it.isDigit()
    }.toInt()
}
