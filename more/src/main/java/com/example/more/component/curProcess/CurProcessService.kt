package com.example.more.component.curProcess

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.common.util.LogUtil

/**
 * Created by zhangqy
 * Data : 2024/3/3
 */
class CurProcessService : Service() {

    companion object{
        const val TAG = "CurProcessService-"
    }

    private val binder = CurProcessServiceBinder()

    override fun onCreate() {
        super.onCreate()
        LogUtil.d(TAG,"onCreate")
    }

    /**
     *  bindService启动的服务在调用者和服务之间是典型的client-server的接口，即调用者是客户端，service是服务端，
     *  service就一个，但是连接绑定到service上面的客户端client可以是一个或多个。
     *  这里所提到的client指的是组件，比如某个Activity。
     */
    override fun onBind(p0: Intent?): IBinder? {
        LogUtil.d(TAG,"onBind")
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        LogUtil.d(TAG,"onUnbind")
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtil.d(TAG,"onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        LogUtil.d(TAG,"onDestroy")
        super.onDestroy()
    }

    fun handleAfterDownloadData(data :String){
        LogUtil.d(TAG, "这是刚到手的数据真的6：：：：：$data")
    }

    //配合ServiceConnection使得客户端能够持有CurProcessService实例
    inner class CurProcessServiceBinder : Binder(){
        fun getService() = this@CurProcessService
    }

}