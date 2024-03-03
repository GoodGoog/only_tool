package com.example.more.component.curProcess;

import androidx.annotation.Nullable;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.example.common.base.BaseActivity;
import com.example.common.base.BaseViewModel;
import com.example.more.R;
import com.example.more.databinding.MoreActivityCurProcessServiceBinding;

import java.util.Timer;
import java.util.TimerTask;

//测试当前进程内的Service服务
public class CurProcessServiceActivity extends BaseActivity<MoreActivityCurProcessServiceBinding, BaseViewModel> {

    //获得service实例后,如下载到网络数据后,可结合isBound判断是否能够调用CurProcessService中的方法
    private CurProcessService mService;

    private boolean isBound = false;

    private final Timer mTimer = new Timer();

    //绑定时回调
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBound = true;
            CurProcessService.CurProcessServiceBinder binder = (CurProcessService.CurProcessServiceBinder) iBinder;
            mService = binder.getService();
            logD("onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            logD("onServiceDisconnected");
        }
    };

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

        initClickListener();
    }

    private void initClickListener() {
        Intent intent = new Intent(this, CurProcessService.class);
        binding.tvStartService.setOnClickListener(view -> {
            logD("点击了tvStartService");
            startService(intent);
        });
        binding.tvStopService.setOnClickListener(view -> {
            logD("点击了tvStopService");
            stopService(intent);
        });
        binding.tvBindService.setOnClickListener(view -> {
            logD("点击了tvBindService");
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        });
        binding.tvUnBindService.setOnClickListener(view -> {
            logD("点击了tvUnBindService");
            if (isBound) {
                unbindService(connection);
            }
        });
        binding.tvSimulateDownload.setOnClickListener(view -> {
            //模拟持续搞到数据
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isBound) {
                        mService.handleAfterDownloadData("这是每2s发一次的数据");
                    }
                }
            }, 0, 2000);
        });
        binding.tvStopSimulateDownload.setOnClickListener(view -> {
            mTimer.cancel();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.more_activity_cur_process_service;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}