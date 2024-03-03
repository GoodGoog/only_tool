package com.example.more.kotlin

/**
 * Created by zhangqy
 * Data : 2024/3/2
 */
class Person(var name :String) {

    //伴生对象
    companion object {
        val instance : Person by lazy {
            Person("李四",100)
        }

        fun testObject(){
            print("测试testObject")
        }
    }

    init {
        print("这是刺客的name = $name")
    }

    constructor(name :String, age : Int) : this(name) {

    }

    constructor(name :String, age : Int,num :String) : this(name,age) {

    }
}