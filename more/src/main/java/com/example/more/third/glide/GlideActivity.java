package com.example.more.third.glide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.example.common.base.BaseActivity;
import com.example.common.base.BaseViewModel;
import com.example.common.util.EventBusInfo;
import com.example.more.R;
import com.example.more.databinding.MoreActivityGlideBinding;
import com.jeremyliao.liveeventbus.LiveEventBus;

public class GlideActivity extends BaseActivity<MoreActivityGlideBinding, BaseViewModel> {

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        String imgUrl = "https://tse2-mm.cn.bing.net/th/id/OIP-C.YErx6MN5bOJWOPC5t7615QHaJW?rs=1&pid=ImgDetMain";

        binding.tvSuccess.setOnClickListener(view -> {
            Glide.with(this)
                    .load(imgUrl)
                    .into(binding.iv1);
        });

        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)//加载成功之前占位图
                .error(R.mipmap.ic_launcher)//加载错误之后的错误图
                .override(400, 400)//指定图片的尺寸
                //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                .fitCenter()
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                .centerCrop()
                .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                ;

        binding.tvFailure.setOnClickListener(view -> {
            Glide.with(this)
                    .load("error_url")
                    .apply(options)
                    .into(binding.iv1);
        });
        binding.tvTestEventBus.setOnClickListener(view -> {
            testEventBus();
        });

    }

    public void testEventBus() {
        LiveEventBus.get(EventBusInfo.TEST_EVENT)
                .post("test_info_from_glide_activity");
    }

    public void testARouter() {
        //跳转到SecondActivity
        ARouter.getInstance()
                .build("")
                .withString("name","zhangsan")
                .withInt("age",22)
                .navigation();

        //获取Fragment对象
//        MainFragment f=
//                ARouter.getInstance().build(ARouterPath.MainFragment).navigation() as MainFragment

    }

    @Override
    protected int getLayoutId() {
        return R.layout.more_activity_glide;
    }
}