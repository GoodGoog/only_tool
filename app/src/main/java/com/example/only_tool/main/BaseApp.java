package com.example.only_tool.main;

import android.app.Application;

import com.jeremyliao.liveeventbus.LiveEventBus;

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

        //配置
        LiveEventBus
                .config()
                .autoClear(true)
                .lifecycleObserverAlwaysActive(true);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
       // ARouter.getInstance().destroy();
    }
}
