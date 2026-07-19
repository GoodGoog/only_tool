package com.example.common.zhipuAi

data class ZhipuRequest(
    val model: String = "glm-4.7-flash",
    val messages: List<ZhipuMessage>
)

data class ZhipuMessage(
    val role: String, // user / assistant
    val content: String
)

data class ZhipuResponse(
    val choices: List<ZhipuChoice>
)

data class ZhipuChoice(
    val message: ZhipuMessage
)
