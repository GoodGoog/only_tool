package com.example.common.zhipuAi

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

//    private val API_KEY = "d9dbbde78e4f46d5bf2641a059ff76a4.TjXGVQ4kim7cDK18"
class ZhipuAiHelper {
    private val API_KEY = "d9dbbde78e4f46d5bf2641a059ff76a4.TjXGVQ4kim7cDK18"
    private val BASE_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions"
    private val gson = Gson()
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    // 配置超时
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun sendChatMsg(prompt: String): String = withContext(Dispatchers.IO) {
        return@withContext try {
            val msgList = listOf(ZhipuMessage("user", prompt))
            val reqJson = gson.toJson(ZhipuRequest(messages = msgList))
            val body = reqJson.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(BASE_URL)
            .header("Authorization", "Bearer $API_KEY")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val resBodyStr = response.body?.string() ?: "接口无返回数据"

            // HTTP状态码异常
            if (!response.isSuccessful) {
                return@withContext "请求失败，状态码：${response.code}，返回：$resBodyStr"
            }

            // 判断是否返回错误信息
            val rawMap = gson.fromJson(resBodyStr, Map::class.java)
            if (rawMap.containsKey("error")) {
                return@withContext "接口错误：${rawMap["error"]}"
            }

            val resp = gson.fromJson(resBodyStr, ZhipuResponse::class.java)
            resp.choices?.firstOrNull()?.message?.content ?: "AI未输出内容"

        } catch (e: SocketTimeoutException) {
            "请求超时，请切换网络或缩短提问内容"
        } catch (e: Exception) {
            "网络异常：${e.message}"
        }
    }
}

/**
 * 使用方法
 */
//lifecycleScope.launch {
//    val answer = ZhipuAiHelper().sendChatMsg(question)
//    Log.d(TAG, "initListener: answer = " + answer)
//}