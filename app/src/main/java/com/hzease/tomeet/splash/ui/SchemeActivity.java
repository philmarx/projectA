package com.hzease.tomeet.splash.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.Map;

import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.MWConfiguration;
import cn.magicwindow.MagicWindowSDK;
import cn.magicwindow.mlink.MLinkCallback;
import cn.magicwindow.mlink.MLinkIntentBuilder;
import cn.magicwindow.mlink.YYBCallback;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchemeActivity extends NetActivity {
    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return 0;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {

    }

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {
        // this.setTheme(android.R.style.Theme_NoDisplay);

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
        switch (uri.getQueryParameter("action")) {
            case "invited":
                // roomId
                final String roomId = uri.getQueryParameter("key1");
                if (PTApplication.myInfomation != null) {
                    PTApplication.getRequestService().joinRoom(PTApplication.userToken, PTApplication.userId, roomId, AppConstants.TOMEET_EVERY_ROOM_PASSWORD)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<NoDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e(e.getMessage());
                                }

                                @Override
                                public void onNext(NoDataBean noDataBean) {
                                    Logger.e(noDataBean.toString());
                                    if (noDataBean.isSuccess()) {
                                        startActivity(new Intent(SchemeActivity.this, GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, roomId));
                                    } else {
                                        ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                                    }
                                }
                            });
                } else {
                    // 如果用户没登录
                    ToastUtils.getToast(PTApplication.getInstance(), "请先登陆后再加入房间");
                }
                break;
            case "share":
                // userId
                final String userId = uri.getQueryParameter("key1");
                if (PTApplication.myInfomation != null) {
                    PTApplication.getRequestService().becameFriend(PTApplication.userToken, PTApplication.userId, userId, uri.getQueryParameter("key2"))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<NoDataBean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e(e.getMessage());
                                }

                                @Override
                                public void onNext(NoDataBean noDataBean) {
                                    Logger.e(noDataBean.toString());
                                    if (!TextUtils.isEmpty(noDataBean.getMsg()))
                                        ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                                }
                            });
                } else {
                    // 如果用户没登录
                    ToastUtils.getToast(PTApplication.getInstance(), "请先登陆后再点此链接");
                }
                break;
        }
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
                MLinkIntentBuilder.buildIntent(paramMap, context, HomeActivity.class);
            }
        });

        MLinkAPIFactory.createAPI(context).checkYYB(SchemeActivity.this, new YYBCallback() {
            @Override
            public void onFailed(Context context) {
                Logger.e("应用宝  进入  失败");
            }
        });
    }
}
