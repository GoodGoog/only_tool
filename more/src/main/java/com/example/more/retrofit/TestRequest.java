package com.example.more.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface TestRequest {

 //   @FormUrlEncoded   //post请求必须加上
    @POST(".")
    Call<QqInfoResponse> postResult(@QueryMap Map <String,String> map);

}

/**
 * Path是网址中的参数,例如:trades/{userId}
 * Query是问号后面的参数,例如:trades/{userId}?token={token}
 * QueryMap 相当于多个@Query
 * Field用于Post请求,提交单个数据,然后要加@FormUrlEncoded
 * Body相当于多个@Field,以对象的方式提交
 * @Streaming:用于下载大文件
 * @Header,@Headers、加请求头
 * ————————————————
 * 原文链接：https://blog.csdn.net/guohaosir/article/details/78942485
 */