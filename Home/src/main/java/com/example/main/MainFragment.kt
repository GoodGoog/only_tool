package com.example.main

import android.content.Intent
import android.os.Bundle
import com.example.common.base.BaseFragment
import com.example.common.base.BaseViewModel
import com.example.main.databinding.FragmentHomeBinding
import com.example.more.psot.PostActivity

class MainFragment : BaseFragment<FragmentHomeBinding, BaseViewModel>(){
    override fun initData(savedInstanceState: Bundle?) {
        initClickListener()
    }

    fun initClickListener(){
        binding.tvStartLeisu.setOnClickListener {
            val intent = Intent(requireContext(), PostActivity::class.java)
            startActivity(intent)
        }
        binding.tvUpload.setOnClickListener { showToast("上传被点击了") }

        binding.tvDownload.setOnClickListener { showToast("下载被点击了") }


    }

    override fun getLayoutId() = R.layout.fragment_home
}