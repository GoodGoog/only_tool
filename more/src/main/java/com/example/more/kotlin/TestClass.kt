package com.example.more.kotlin

import com.example.common.util.LogUtil

/**
 * Created by zhangqy
 * Data : 2024/3/2
 */
class TestClass {

}

fun main(args: Array<String>) {


    print("test-" + "这是测试")

    val a = 9
    when (a) {
        in 88..99 -> print("in 0..99 \n")
        9 -> print("==9\n")
        else -> print("else\n")
    }

    val lisi = Person("张三", 99, "123456")
    print(lisi.name)

    print(Person.instance.name)
    Person.testObject()

}
