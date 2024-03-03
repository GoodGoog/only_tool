package com.example.common.network

/**
 * Created by zhangqy
 * Data : 2024/3/3
 */

/**
 * msg	string	必填	所问的内容
 * type	string	否	输出类型，可选wifi/false，wifi联网，false单机
 * id	number	否	连续对话id，可以不填
 * mos	string	否	输出类型，可选json/text，text
 * http://se.csnmb.com/API/gpt.php?msg=你是谁&type=wifi&id=&mos=json
 */
fun createChatGptParams(msg: String,type : String = "wifi",id :String = "",mos :String = "text") = HashMap<String, String>().apply {

    put("msg",msg)
    put("type",type)
    put("id",id)
    put("mos",mos)
}