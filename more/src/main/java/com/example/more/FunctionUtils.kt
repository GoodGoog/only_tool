package com.example.more

import android.content.Context
import android.widget.Toast
import com.example.common.util.LogUtil

fun Context.showToast(content: String?) {
    Toast.makeText(this, content, Toast.LENGTH_LONG).show()
}

fun Context.logD(msg: String?) {
    LogUtil.d(this.javaClass.getName().toString() + "|++++++++++++++++++++++++++|", msg)
}


