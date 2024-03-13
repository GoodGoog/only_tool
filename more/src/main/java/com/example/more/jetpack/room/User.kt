package com.example.more.jetpack.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by zhangqy
 * Data : 2024/3/12
 */
@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val firstName: String?,
    @ColumnInfo(name = "city") val lastName: String?
)
