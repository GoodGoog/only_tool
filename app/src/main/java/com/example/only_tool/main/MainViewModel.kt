package com.example.only_tool.main

import androidx.lifecycle.MutableLiveData
import com.example.common.base.BaseViewModel

class MainViewModel : BaseViewModel() {

    val name : MutableLiveData<String> = MutableLiveData("默认咯饿了")

}