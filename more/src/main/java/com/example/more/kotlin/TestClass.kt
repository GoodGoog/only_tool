package com.example.more.kotlin

import com.example.common.util.LogUtil

/**
 * Created by zhangqy
 * Data : 2024/3/2
 */
class TestClass {

    val testFunction : (String,String) -> String = {str1,str2 ->

        "你的名字"
    }

    fun testFunc(preFix : String,match : (String,String) -> String) : String{
        return  preFix + match
    }

}


fun main(args: Array<String>) {

    val test = TestClass()
    print(test.testFunction("1","2"))

}
