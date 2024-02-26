package com.example.common

import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<V : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {
    //    lateinit var viewModel : VM
    val viewModel: VM by lazy {
        createViewModel()
    }

    //    lateinit var binding: V
    val binding: V by lazy {
        DataBindingUtil.setContentView(this, getLayoutId())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        viewModel = createViewModel()
//        binding = DataBindingUtil.setContentView(this, getLayoutId())
//        if (binding == null){
//            Log.d("测试","null")
//        }else{
//            Log.d("测试","部位Null")
//        }
        setContentView(binding.root)
        //       setContentView(getLayoutId())
        binding.setVariable(com.example.common.BR.viewModel, viewModel)
        initData(savedInstanceState)
    }


    abstract fun initData(savedInstanceState: Bundle?)

    private fun createViewModel(): VM {
        val vm =
            (((this.javaClass.genericSuperclass) as ParameterizedType).actualTypeArguments[0]) as Class<VM>
        Log.d(this.javaClass.name.toString(), vm.name.toString())
        return ViewModelProvider(this)[vm]
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

}
