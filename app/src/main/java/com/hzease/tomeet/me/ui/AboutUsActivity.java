package com.hzease.tomeet.me.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.R;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.concurrent.ExecutionException;

public class AboutUsActivity extends NetActivity {

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_about_us;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) throws ExecutionException, InterruptedException {

    }
}
