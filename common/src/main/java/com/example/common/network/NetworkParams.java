package com.example.common.network;

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

}
