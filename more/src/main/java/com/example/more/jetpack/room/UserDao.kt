package com.example.more.jetpack.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * Created by zhangqy
 * Data : 2024/3/12
 * Entity 表示数据库中的一个表，必须用@Entity 进行注释。每个实体至少包含一个字段，必须定义一个主键。DAO（数据库访问对象）：
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}