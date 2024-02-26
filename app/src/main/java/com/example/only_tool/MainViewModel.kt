package com.example.only_tool

import androidx.lifecycle.MutableLiveData
import com.example.common.BaseViewModel

class MainViewModel : BaseViewModel() {

    val name : MutableLiveData<String> = MutableLiveData("默认咯饿了")

}