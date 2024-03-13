package com.example.more.jetpack.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by zhangqy
 * Data : 2024/3/12
 */
@Database(entities = arrayOf(User::class), version = 1)
abstract class UserDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao
}