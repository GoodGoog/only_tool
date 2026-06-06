package com.example.more.setting

import android.os.AsyncTask
import android.os.Bundle
import com.example.common.base.BaseActivity
import com.example.common.base.BaseViewModel
import com.example.more.R
import com.example.more.databinding.MoreActivityObtainBinding
import kotlinx.coroutines.Runnable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class ObtainActivity : BaseActivity<MoreActivityObtainBinding, BaseViewModel>() {
    override fun initData(savedInstanceState: Bundle?) {
        binding.tvObtain.setOnClickListener {
            obtain()
        }
    }

    //爬数据
    fun obtain(){

        try {
            Thread(Runnable {
                val url = "https://www.qie.tv/match"
                val  doc : Document = Jsoup.connect(url).get()
                val content : Element = doc.select("body").first()
                runOnUiThread {
                    binding.etShow.setText(content.text() ?: "没有数据")
                }
            }).start()
        }catch (e : Exception){
            e.printStackTrace()
            runOnUiThread {
                binding.etShow.setText(("报错信息:" + e.message))
            }
        }
    }

    override fun getLayoutId() = R.layout.more_activity_obtain
}