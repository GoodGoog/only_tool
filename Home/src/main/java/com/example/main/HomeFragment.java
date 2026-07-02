package com.example.main;

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
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), PostActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}