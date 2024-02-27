package com.example.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.common.util.RoutePathUtil;


public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

    }
}