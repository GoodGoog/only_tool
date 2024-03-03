package com.example.more.kotlin

import androidx.lifecycle.MutableLiveData
import com.example.common.base.BaseViewModel

/**
 * Created by zhangqy
 * Data : 2024/3/2
 */
class BaseKotlinVM : BaseViewModel() {
    public val title = MutableLiveData<String>()
}