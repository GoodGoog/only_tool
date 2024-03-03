package com.example.common.network;

import androidx.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by zhangqy
 * Data : 2024/2/29
 */
public class NetworkParams {


    /**
     * qq信息查看
     */
    public static HashMap<String,String> createQqInfoRequestParams(String qqNumber){
        HashMap<String,String> map = new HashMap<>();
        map.put("qq",qqNumber);
        return map;
    }

    /**
     *  type = 百度/微博/知乎
     *  热搜榜查询
     */
    public static HashMap<String,String> createHotSearchParams(String type){
        HashMap<String,String> map = new HashMap<>();
        map.put("type",type);
        //model = json / text
        map.put("model","json");
        return map;
    }



}
