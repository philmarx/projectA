package com.hzease.tomeet;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.Session;

/**
 * Created by Key on 2016/10/10 14:57
 * email: MrKey.K@gmail.com
 * description: 第一层基类
 */

public abstract class BaseActivity extends AutoLayoutActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        //隐藏掉整个ActionBar
        // getSupportActionBar().hide();
        //启动activity时 不自动弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 竖屏显示，不能转动
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        unbinder = ButterKnife.bind(this);
        // 因为有延迟，先在子线程请求网络数据，拿到后初始化，不影响主线程的本地数据初始化
        beforeInit(savedInstanceState);
        // 先初始化本地数据和布局
        initLayout(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * @return 返回布局文件ID
     */
    protected abstract int getContentViewId();

    /**
     * TODO 初始化布局文件
     */
    protected abstract void initLayout(Bundle savedInstanceState);

    /**
     * TODO 加载网络数据
     */
    protected abstract void beforeInit(Bundle savedInstanceState);

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onResume(this);
        // 魔窗
        Session.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 友盟统计
        MobclickAgent.onPause(this);
        // 魔窗
        Session.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        PTApplication.currentStartActivity = this;
        Uri uri = getIntent().getData();
        if (uri != null) {
            Logger.e("uri: " + uri);
            //MLinkAPIFactory.createAPI(this).router(mLink);
        } else {
            MLinkAPIFactory.createAPI(this).checkYYB();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        PTApplication.currentStartActivity = PTApplication.getInstance();
    }
}
