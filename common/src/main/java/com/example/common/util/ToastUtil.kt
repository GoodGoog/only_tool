package com.example.common.util

import android.content.Context
import android.widget.Toast

/**
 * Created by zhangqy
 * Data : 2024/3/6
 */

fun showToast(mContext: Context,content :String){
    Toast.makeText(mContext,content,Toast.LENGTH_LONG).show()
}