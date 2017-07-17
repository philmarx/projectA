package com.hzease.tomeet.splash.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameTypeBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.RongCloudInitUtils;
import com.hzease.tomeet.utils.SpUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.MWConfiguration;
import cn.magicwindow.MagicWindowSDK;
import cn.magicwindow.mlink.MLinkCallback;
import cn.magicwindow.mlink.MLinkIntentBuilder;
import cn.magicwindow.mlink.YYBCallback;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SplashActivity extends NetActivity {

    private long startTime;
    private long waitTime = 1500;
    private boolean isLogined;

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_splash;
    }

    /**
     * 初始化布局文件
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();

        try {
            PTApplication.appVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            ((TextView) findViewById(R.id.tv_version_splash_act)).setText("版本号：".concat(PTApplication.appVersion));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void netInit(Bundle savedInstanceState) {
        // 检查游戏种类版本
        PTApplication.getRequestService().getActivityType("tomeet")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<GameTypeBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(GameTypeBean gameTypeBean) {
                        if (gameTypeBean.isSuccess()) {
                            String gameType = SpUtils.getStringValue(SplashActivity.this, AppConstants.TOMEET_SP_GAME_TYPE);
                            if (!TextUtils.isEmpty(gameType)) {
                                Logger.i("活动类目-网络版本：" + gameTypeBean.getMsg() + "  活动类目-本地版本：" + new Gson().fromJson(gameType, GameTypeBean.class).getMsg());
                                if (!gameTypeBean.getMsg().equals(new Gson().fromJson(gameType, GameTypeBean.class).getMsg())) {
                                    SpUtils.saveString(SplashActivity.this, AppConstants.TOMEET_SP_GAME_TYPE, new Gson().toJson(gameTypeBean));
                                }
                            } else {
                                SpUtils.saveString(SplashActivity.this, AppConstants.TOMEET_SP_GAME_TYPE, new Gson().toJson(gameTypeBean));
                            }
                        }
                    }
                });

        // 获取版本号
        PTApplication.getRequestService().getAppVersion("android")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        Logger.e("服务器版本号：" + s + "  当前app版本号：" + PTApplication.appVersion);
                    }
                });


        // 初始化用户.查看本地是否已保存
        final SharedPreferences sp = getSharedPreferences(AppConstants.TOMMET_SHARED_PREFERENCE, MODE_PRIVATE);
        // 先用临时变量确认本地存的用户名和token还是否有效
        final String userId_temp = sp.getString("userId", "");
        final String userToken_temp = sp.getString("userToken", "");

        //判断用户是否开启极光推送
        boolean isOpenJpush = sp.getBoolean("isOpenJpush", true);
        if (isOpenJpush) {
            JPushInterface.resumePush(this);
        } else {
            JPushInterface.stopPush(this);
        }
        Logger.e("推送是否关闭" + JPushInterface.isPushStopped(this));
        // 测试
        //final String userId_temp = "10000000006";
        //final String userToken_temp = "ntPQXDb6uZSy+qJMjyMWlNwOBZI/D7imBl2AldSB3u9t8nP9hwW7W4rK9vTutRXDi0vjuf82USWzPO0lvNaKP/s3f6+xWeGx";

        Logger.i("SplashActivity读取本地:  \nuserId: " + userId_temp + "\nuserToken: " + userToken_temp);
        // 历史登录记录
        isLogined = !TextUtils.isEmpty(userId_temp) && !TextUtils.isEmpty(userToken_temp);
        if (isLogined) {
            PTApplication.myLoadingStatus = AppConstants.YY_PT_LOGIN_LOADING;
            PTApplication.getRequestService().getMyInfomation(userToken_temp, userId_temp)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserInfoBean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            PTApplication.myLoadingStatus = AppConstants.YY_PT_LOGIN_FAILED;
                            EventBus.getDefault().post(new UserInfoBean());
                            Logger.e(e.getMessage());
                        }

                        @Override
                        public void onNext(UserInfoBean userInfoBean) {
                            if (userInfoBean.isSuccess()) {
                                PTApplication.myLoadingStatus = AppConstants.YY_PT_LOGIN_SUCCEED;
                                Logger.e("logger" + userInfoBean.getData().toString());
                                PTApplication.myInfomation = userInfoBean;
                                Logger.e(PTApplication.myInfomation.toString());
                                PTApplication.userId = userId_temp;
                                PTApplication.userToken = userToken_temp;
                                // 融云
                                new RongCloudInitUtils().RongCloudInit();
                                new AMapLocUtils().getLonLatAndSendLocation("0");
                            } else {
                                PTApplication.myLoadingStatus = AppConstants.YY_PT_LOGIN_FAILED;
                                ToastUtils.getToast(SplashActivity.this, userInfoBean.getMsg() + "，请重新登录");
                                // 清除本地记录
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear().apply();
                                isLogined = false;
                                // EventBus发送更新界面消息
                                EventBus.getDefault().post(userInfoBean);
                                Logger.i("getMyInfomation失败：发送EventBus");
                            }
                        }
                    });
        }

        if (!(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            new AMapLocUtils().getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
                @Override
                public void getLonLat(AMapLocation aMapLocation) {
                    PTApplication.myLongitude = aMapLocation.getLongitude();
                    PTApplication.myLatitude = aMapLocation.getLatitude();
                    PTApplication.cityName = aMapLocation.getCity();
                }
            });
        }

        // 初始化完个人信息后计算下时间
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 检查有没有进入过向导页
                SharedPreferences sp = getSharedPreferences("game_name", Context.MODE_PRIVATE);
                boolean isGuide = sp.getBoolean("isGuide", false);

                // 最后睡,睡完结束
                long endTime = System.currentTimeMillis() - startTime;
                if (endTime < waitTime) {
                    SystemClock.sleep(waitTime - endTime);
                }

                Logger.w("初始化用了: " + endTime + "\n总共用时: " + (System.currentTimeMillis() - startTime));

                // 决定去向
                if (isGuide) {
                    if (PTApplication.myInfomation == null || PTApplication.myInfomation.getData().isIsInit()) {
                        boolean novice = SpUtils.getBooleanValue(SplashActivity.this, "novice");
                        if (novice) {
                            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                            // 检查是否有历史登录记录，如果有，等待加载
                            if (isLogined) {
                                intent.setFlags(AppConstants.YY_PT_NAVIGATION_SPLASH_REQUEST_CODE);
                            }
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SplashActivity.this, NoviceGuideActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        // 先去初始化
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.setFlags(AppConstants.YY_PT_NAVIGATION_SPLASH_REQUEST_CODE);
                        startActivity(intent);
                    }
                } else {
                    startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                }
                finish();
            }
        }).start();
    }
}
