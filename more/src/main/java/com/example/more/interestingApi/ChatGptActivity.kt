package com.example.more.interestingApi

import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.network.NetworkBaseUrl
import com.example.common.network.NetworkCallBack
import com.example.common.network.createChatGptParams
import com.example.more.R
import com.example.more.databinding.MoreActivityChatGptBinding
import com.google.gson.Gson

class ChatGptActivity : BaseActivity<MoreActivityChatGptBinding, ChatGptVM>() {

    override fun initData(savedInstanceState: Bundle?) {
        val params = createChatGptParams("今天是周几呀");
        viewModel.request(
            NetworkBaseUrl.CHAT_GPT_3_POINT_5_MODEL,
            params,
            ChatGptResponse::class.java,object : NetworkCallBack<ChatGptResponse>() {
                override fun onSuccess(t: ChatGptResponse?) {
                    logD("这是返回的正确信息")
                    logD(Gson().toJson(t))
                }

                override fun onFailure(t: Throwable?) {
                    super.onFailure(t)
                    logD("失败了" + t?.message)
                }
            })
    }

    override fun getLayoutId() = R.layout.more_activity_chat_gpt

}