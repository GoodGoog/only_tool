package com.example.more.rxjava;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.common.base.BaseActivity;
import com.example.common.base.BaseViewModel;
import com.example.more.R;
import com.example.more.databinding.MoreActivityRxJavaBinding;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaActivity extends BaseActivity<MoreActivityRxJavaBinding, BaseViewModel> {

    Observable<String> observable ;
    Observer<String> observer ;

    Disposable mDisposable;

    ObservableEmitter<String> mEmitter ;

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

        //3.订阅
        createObservable()
                .subscribeOn(Schedulers.io())              //子线程中干事
                .observeOn(AndroidSchedulers.mainThread()) //主线程中观察
                .subscribe(createObserver());

        //4.切断观察者 与 被观察者 之间的连接
        //if (mDisposable.isDisposed()) mDisposable.dispose();
    }

    public Observable<String> createObservable() {
        if (observable != null) return observable;
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                logD("开始发射数据" + "当前线程" + Thread.currentThread().getName());
                emitter.onNext("First");
                mEmitter = emitter;
            }
        });
    }

    public Observer<String> createObserver() {
        if (observer != null) return observer;
        return new Observer<String>() {
            // 观察者接收事件前，默认最先调用复写 onSubscribe（）
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
                logD("开始采用subscribe连接");
            }

            // 当被观察者生产Next事件 & 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onNext(String value) {
                logD("对Next事件作出响应" + value + "当前线程" + Thread.currentThread().getName());
            }

            // 当被观察者生产Error事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onError(Throwable e) {
                logD("对Error事件作出响应");
            }

            // 当被观察者生产Complete事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onComplete() {
                logD("对Complete事件作出响应");
            }
        };
    }


    @Override
    protected int getLayoutId() {
        return R.layout.more_activity_rx_java;
    }
}