package com.example.more.component

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.util.*

/**
 * Created by zhangqy
 * Data : 2024/3/3
 */
class JumpAppService : Service() {

    companion object{
        const val TAG = "JumpAppService-"
    }

    private var data :String? = ""
    private var isRunning = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate")
        object : Thread() {
            override fun run() {
                super.run()
                isRunning = true
                while (isRunning){
                    Log.d(TAG, "这是另一个app传来的的数据:$data")
                }
                try {
                    sleep(1000)
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }.start()
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.d(TAG,"onBind")

        return object : IDifferentAppBinder.Stub() {
            override fun basicTypes(
                anInt: Int,
                aLong: Long,
                aBoolean: Boolean,
                aFloat: Float,
                aDouble: Double,
                aString: String?
            ) {

            }

            override fun setData(data: String?) {
                this@JumpAppService.data = data
            }


        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG,"onUnbind")
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.d(TAG,"onDestroy")
        super.onDestroy()
        isRunning = false
    }


}