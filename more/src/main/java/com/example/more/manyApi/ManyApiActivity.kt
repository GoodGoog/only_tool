package com.example.more.manyApi

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseActivity
import com.example.common.base.BaseAdapter
import com.example.common.base.BasePopWindow
import com.example.common.base.BaseViewModel
import com.example.common.network.NetworkCallBack
import com.example.more.R
import com.example.more.bean.ApiBean
import com.example.more.convertStringArrayToHashMap
import com.example.more.convertValueStringToValueList
import com.example.more.createApiBeanList
import com.example.more.createKeyValueMap
import com.example.more.databinding.MoreActivityManyApiBinding
import com.example.more.databinding.MoreItemApiListBinding
import com.example.more.databinding.MoreItemApiParamsInputBinding
import com.example.more.databinding.MoreItemValueChooseWindowBinding
import com.example.more.databinding.MoreWindowMultiValueChooseBinding
import com.example.more.getFirstValue
import com.example.more.getMapKey
import com.example.more.getMapValueArrayString

class ManyApiActivity : BaseActivity<MoreActivityManyApiBinding, BaseViewModel>() {
    override fun initData(savedInstanceState: Bundle?) {

        initRecyclerView()
    }

    fun initRecyclerView() {
        val adapter = object : BaseAdapter<MoreItemApiListBinding, ApiBean>(
            createApiBeanList(),
            R.layout.more_item_api_list
        ) {
            override fun bindViewHolder(
                mbinding: MoreItemApiListBinding,
                datas: ArrayList<ApiBean>,
                position: Int
            ) {
                val newParamsMaps = datas[position].paramsMaps
                mbinding.tvFunctionName.text = "功能：" + datas[position].functionName
                initParamValueInputRecyclerView(
                    mbinding.rvFunctionValuesInput, datas[position].paramsMaps
                ) { newKeyValue, eventPosition ->
                    //参数光标改变后传来的数据
                    newParamsMaps[eventPosition] = newKeyValue
                }
                mbinding.tvSure.setOnClickListener {
                    //先夺走光标，出发item中EditView的数据回调
                    //结束时光标变化
                    binding.etCursorHolder.apply {
                        setFocusable(true);
                        setFocusableInTouchMode(true);
                        requestFocus();
                    }
                    //网络请求
                    networkRequest(datas[position].baseUrl, newParamsMaps)
                }
            }
        }
        binding.rvManyApi.setAdapter(adapter)
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvManyApi.setLayoutManager(manager)
    }

    /**
     * 点击确认后网络请求
     */
    fun networkRequest(baseUrl: String, paramsMapStr: ArrayList<String>) {
        val paramsMap = convertStringArrayToHashMap(paramsMapStr)
        viewModel.request<Any>(
            baseUrl,
            paramsMap,
            Any::class.java,
            object : NetworkCallBack<Any>() {
                override fun onSuccess(qqInfoResponse: Any) {
                    showResultPopWindow(qqInfoResponse)
                }

                override fun onFailure(t: Throwable) {
                    super.onFailure(t)
                    showToast("网络请求失败" + t.message)
                }
            })
    }

    fun showResultPopWindow(any: Any) {
        val mWindow = NetworkResultPopWindow(binding.root, any)
        mWindow.showAtLocation(binding.root, Gravity.BOTTOM, 0, 0);
    }

    fun initParamValueInputRecyclerView(
        recyclerView: RecyclerView,
        paramsMaps: ArrayList<String>,
        onEventChangeListener: OnEventChangeListener
    ) {
        val adapter = object : BaseAdapter<MoreItemApiParamsInputBinding, String>(
            paramsMaps,
            R.layout.more_item_api_params_input
        ) {
            override fun bindViewHolder(
                mBinding: MoreItemApiParamsInputBinding,
                paramsMaps: ArrayList<String>,
                position: Int
            ) {
                //getValue获取参数默认值列表字符串
                //getKey或者参数名
                val paramsMap = paramsMaps[position]
                mBinding.tvParamsName.text = paramsMap.getMapKey()
                mBinding.etParamsValue.setText(getFirstValue(paramsMap.getMapValueArrayString()))
                mBinding.etParamsValue.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                    } else {
                        //拼接新的参数键值对
                        val keyValue = createKeyValueMap(
                            paramsMap.getMapKey(),
                            mBinding.etParamsValue.text.toString()
                        )
                        onEventChangeListener.onCursorChangeEvent(keyValue, position)
                    }
                }
                mBinding.etParamsValue.setOnLongClickListener {
                    showMultiValueChoosePopWindow(
                        mBinding.etParamsValue,
                        paramsMap.getMapValueArrayString()
                    ) { newValue -> //显示弹窗中选中的参数值
                        mBinding.etParamsValue.setText(newValue)
                    }
                    return@setOnLongClickListener true
                }
            }
        }
        recyclerView.setAdapter(adapter)
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.setLayoutManager(manager)
    }

    /**
     * 从多个参数值中选择
     */
    fun showMultiValueChoosePopWindow(
        anchorView: View,
        valueStr: String,
        onValueChooseListener: OnValueChooseListener
    ) {
        val mWindow = object : BasePopWindow<MoreWindowMultiValueChooseBinding, String>(
            binding.root, valueStr, ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, R.layout.more_window_multi_value_choose
        ) {
            override fun initView(
                mBinding: MoreWindowMultiValueChooseBinding,
                mData: String
            ) {
                initValueChooseRecyclerView(
                    mBinding.rvValueChoose,
                    convertValueStringToValueList(mData)
                ) { newValue -> //这里拦截一下数值选择事件，以隐藏当前弹窗
                    dismiss()
                    onValueChooseListener.onValueChooseFromPopWindow(newValue)
                }
            }
        }
        mWindow.showAsDropDown(anchorView,0,0)
    }

    fun initValueChooseRecyclerView(
        recyclerView: RecyclerView,
        valuesArray: ArrayList<String>,
        onValueChooseListener: OnValueChooseListener
    ) {
        val adapter = object : BaseAdapter<MoreItemValueChooseWindowBinding, String>(
            valuesArray,
            R.layout.more_item_value_choose_window
        ) {
            override fun bindViewHolder(
                binding: MoreItemValueChooseWindowBinding,
                data: ArrayList<String>,
                position: Int
            ) {
                binding.tvValue.text = data[position]
                binding.tvValue.setOnClickListener {
                    onValueChooseListener.onValueChooseFromPopWindow(data[position])
                }
            }

        }
        recyclerView.setAdapter(adapter)
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.setLayoutManager(manager)
    }


    override fun getLayoutId() = R.layout.more_activity_many_api

}