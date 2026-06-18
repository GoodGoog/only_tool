package com.example.more.OnEventChangeListener

import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.base.BaseActivity
import com.example.common.base.BaseAdapter
import com.example.common.base.BaseViewModel
import com.example.common.network.NetworkCallBack
import com.example.more.R
import com.example.more.bean.ApiBean
import com.example.more.convertStringArrayToHashMap
import com.example.more.createApiBeanList
import com.example.more.createKeyValueMap
import com.example.more.databinding.MoreActivityManyApiBinding
import com.example.more.databinding.MoreItemApiListBinding
import com.example.more.databinding.MoreItemApiParamsInputBinding
import com.example.more.getMapKey
import com.example.more.getMapValue
import com.example.more.team.ChoosePopupWindow

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
                    showToast("变了" + newKeyValue)
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
        logD("baseUrl ==" + baseUrl)
        logD("pramsStr ====" + paramsMap)
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
                }
            })
    }

    fun showResultPopWindow(any: Any){
        val mWindow = NetworkResultPopWindow(binding.root,any)
        mWindow.showAtLocation(binding.root, Gravity.BOTTOM,0,0);
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
                binding: MoreItemApiParamsInputBinding,
                paramsMaps: ArrayList<String>,
                position: Int
            ) {
                val paramsMap = paramsMaps[position]
                binding.tvParamsName.text = paramsMap.getMapKey()
                binding.etParamsValue.setText(paramsMap.getMapValue())
                binding.etParamsValue.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                    } else {
                        //拼接新的参数键值对
                        val keyValue = createKeyValueMap(
                            paramsMap.getMapKey(),
                            binding.etParamsValue.text.toString()
                        )
                        onEventChangeListener.onCursorChangeEvent(keyValue, position)
                    }
                }
            }
        }
        recyclerView.setAdapter(adapter)
        val manager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.setLayoutManager(manager)
    }

    override fun getLayoutId() = R.layout.more_activity_many_api

}