package com.example.main;

import static com.example.common.util.ToastUtilKt.showToast;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.more.psot.PostActivity;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 加载fragment布局
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        // 在根布局中findViewById
        TextView tvStart = rootView.findViewById(R.id.tv_start_leisu);
        TextView tvUpload = rootView.findViewById(R.id.tv_upload);
        TextView tvDownload = rootView.findViewById(R.id.tv_download);
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), PostActivity.class);
                startActivity(intent);
            }
        });
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getContext(),"上传被点击了");
            }
        });
        tvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getContext(),"下载被点击了");
            }
        });
        return rootView;
    }
}