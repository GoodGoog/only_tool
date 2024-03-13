package com.example.more.jetpack.room

import android.os.Bundle
import androidx.room.Room
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.databinding.MoreActivityRoomBinding

class RoomActivity : BaseActivity<MoreActivityRoomBinding,BaseViewModel>() {

    // application's Database name
    private val DATABASE_NAME: String = "USER_DATABASE"



    override fun initData(savedInstanceState: Bundle?) {
        // get the instance of the application's database
//        val db = Room.databaseBuilder(
//            applicationContext, UserDatabase::class.java, DATABASE_NAME
//        ).build()
//        val userDao = db.userDao()

//        binding.tvInsert.setOnClickListener {
//            userDao.insertAll(User(12345,"123","456"))
//        }
//        binding.tvDelete.setOnClickListener {
//
//        }
//        binding.tvQuery.setOnClickListener {
//            userDao.getAll().forEach {
//                logD(it.firstName + it.lastName)
//            }
//        }
    }

    fun testRoom(){
        // using the same DAO perform the Database operations
//        val users: List<User> = userDao.getAll()

    }

    override fun getLayoutId() = R.layout.more_activity_room
}