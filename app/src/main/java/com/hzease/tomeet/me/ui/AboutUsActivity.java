package com.hzease.tomeet.me.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;

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
    protected void initLayout(Bundle savedInstanceState) {
        TextView tv_version_splash_act = (TextView) findViewById(R.id.tv_version_splash_act);
        tv_version_splash_act.setText("版本号：".concat(PTApplication.appVersion));
    }
}
