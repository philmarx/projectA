package com.hzease.tomeet.splash.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.orhanobut.logger.Logger;

import java.util.Map;

import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.MWConfiguration;
import cn.magicwindow.MagicWindowSDK;
import cn.magicwindow.mlink.MLinkCallback;
import cn.magicwindow.mlink.MLinkIntentBuilder;
import cn.magicwindow.mlink.YYBCallback;

public class SchemeActivity extends NetActivity {
    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_scheme;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Logger.e("Scheme!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {
        this.setTheme(android.R.style.Theme_NoDisplay);

        MWConfiguration config = new MWConfiguration(this);

        //开启Debug模式，显示Log，release时注意关闭
        config.setLogEnable(PTApplication.mDebug)
                //设置渠道，非必须（渠道推荐在AndroidManifest.xml内填写）
                //.setChannel("你的渠道名称")
                //带有Fragment的页面。具体查看2.2.2
                .setPageTrackWithFragment(true)
                //设置分享方式，如果之前有集成sharesdk，可在此开启
                .setSharePlatform(MWConfiguration.UMENG);
        MagicWindowSDK.initSDK(config);

        // 注册魔窗
        // MLinkAPIFactory.createAPI(this).registerWithAnnotation(this);
        magicWindowRegister(this);


        Uri uri = getIntent().getData();
        Logger.e("uri: " + uri);
        if (uri != null) {
            Logger.w("scheme: " + uri.getScheme() + "\nuri: " + uri + "\nhost: " + uri.getHost() + "  key1: " + uri.getQueryParameter("key1") + "   action: " + uri.getQueryParameter("action"));
            switchDestination(uri);
        }
    }


    public void switchDestination(Uri uri) {

    }

    private void magicWindowRegister(Context context) {
        MLinkAPIFactory.createAPI(context).registerDefault(new MLinkCallback() {
            @Override
            public void execute(Map paramMap, Uri uri, Context context) {
                Logger.e("registerDefault:   map: " + paramMap.toString() + "  uri: " + uri);
                //HomeActivity 为你的首页
                MLinkIntentBuilder.buildIntent(paramMap, context, HomeActivity.class);
            }
        });
        // mLinkKey:  mLink 的 key, mLink的唯一标识
        MLinkAPIFactory.createAPI(context).register("ToMeet", new MLinkCallback() {
            public void execute(Map paramMap, Uri uri, Context context) {
                Logger.e("register:   map: " + paramMap.toString() + "  uri: " + uri);
                //MLinkIntentBuilder.buildIntent(paramMap, context, HomeActivity.class);
                if (uri != null) {
                    Logger.w("scheme: " + uri.getScheme() + "\nuri: " + uri + "\nhost: " + uri.getHost() + "  key1: " + uri.getQueryParameter("key1") + "   action: " + uri.getQueryParameter("action"));
                    switchDestination(uri);
                }
            }
        });

        MLinkAPIFactory.createAPI(context).checkYYB(this, new YYBCallback() {
            @Override
            public void onFailed(Context context) {
                Logger.e("应用宝  进入  失败!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        });
    }
}
