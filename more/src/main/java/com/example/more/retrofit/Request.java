package com.example.more.retrofit;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface Request {
    // retrofit:以接口形式进行网络请求
    // Delivery用于接收结果，就是我们刚刚创建的实体类
    @GET("")
    Observable<DataBean> delivery();
}
