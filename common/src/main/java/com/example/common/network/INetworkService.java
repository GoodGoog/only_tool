package com.example.common.network;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by zhangqy
 * Data : 2024/2/29
 */
public interface INetworkService {
    @POST(".")
    Call<Object> postResult(@QueryMap Map<String,String> map);

    //baseUrl必须以"/"结尾，否则会报错，故把url拆分成baseUrl(以/结尾)与lastPath。此处lastPath是最后一个路径
    //@GET("str"),其中的str会自动拼接在baseUrl后面,即baseurl + str
    //@Path("lastPath") String path,指此处传来的path值即为占位符@Path("lastPath")的实际值
    @GET("{lastPath}")
    Call<Object> getResult(@Path("lastPath") String path, @QueryMap Map<String,String> params);

    //baseUrl以/结尾，不用再拼接
    @GET
    Call<Object> getResult( @QueryMap Map<String,String> params);

}
