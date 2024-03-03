package com.example.only_tool;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by zhangqy
 * Data : 2024/3/2
 */
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //ARouter初始化配置
//        ARouter.init(this);
//        ARouter.openLog();
//        ARouter.openDebug();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
       // ARouter.getInstance().destroy();
    }
}
