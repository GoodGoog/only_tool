package com.example.main.upyun

import com.UpYun
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets

object UpyunStore {
    // 替换自己USS信息
    private val bucket = "leileilei.b0.aicdn.com"
    private val opName = "qw1213715120"
    private val opPwd = "Qw1213715120"

    // 构造：仅按顺序传3个字符串，不写任何参数名
    private val client by lazy {
        UpYun(bucket, opName, opPwd)
    }

    // 上传文本，4.2.3标准三参数 writeFile(path, byte[], autoCreateDir)
    suspend fun save(remotePath: String, content: String): Boolean {
        return try {
            val byteArr = content.toByteArray(StandardCharsets.UTF_8)
            withContext(Dispatchers.IO) {
                client.writeFile(remotePath, byteArr, true)
            }
            true
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

    // 读取文本
    suspend fun read(remotePath: String): String? {
        return try {
            withContext(Dispatchers.IO) {
                //String(client.readFile(remotePath), StandardCharsets.UTF_8)
                client.readFile(remotePath)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    // 删除文件
    suspend fun del(remotePath: String): Boolean {
        return withContext(Dispatchers.IO) {
            client.deleteFile(remotePath, HashMap<String, String>())
        }
    }
}
