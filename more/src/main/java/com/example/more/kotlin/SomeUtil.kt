package com.example.more.kotlin

/**
 * Created by zhangqy
 * Data : 2024/3/2
 */


fun main(args: Array<String>) {
    print("我就试试".fuck("不准"))
    "123".let {

    }
}

inline fun String.fuck(name : String) = this + name