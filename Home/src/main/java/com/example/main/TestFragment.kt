package com.example.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.more.psot.PostActivity


class TestFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 加载fragment布局
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        // 在根布局中findViewById
        val tvStart = rootView.findViewById<TextView?>(R.id.tv_start_leisu)
        tvStart.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(requireContext(), PostActivity::class.java)
                startActivity(intent)
            }
        })
        return rootView
    }
}