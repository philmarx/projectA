package com.hzease.tomeet.splash.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.TextView;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.utils.RongCloudInitUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import io.rong.eventbus.EventBus;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends NetActivity {

    @BindView(R.id.tv_version_splash)
    TextView tv_version_splash;
    private long startTime;
    private long waitTime = 3000;

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
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {
        // 初始化用户.查看本地是否已保存
        final SharedPreferences sp = getSharedPreferences("wonengzhemerongyirangnirenchulai", MODE_PRIVATE);
        // 先用临时变量确认本地存的用户名和token还是否有效
        final String userId_temp = sp.getString("userId", "");
        final String userToken_temp = sp.getString("userToken", "");

        Logger.i("SplashActivity读取本地:  \nuserId: " + userId_temp + "\nuserToken: " + userToken_temp);
        if (!TextUtils.isEmpty(userId_temp) && !TextUtils.isEmpty(userToken_temp)) {
            PTApplication.getRequestService().getMyInfomation(userToken_temp, userId_temp)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UserInfoBean>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e(e.getMessage());
                        }

                        @Override
                        public void onNext(UserInfoBean userInfoBean) {
                            if (userInfoBean.isSuccess()) {
                                PTApplication.myInfomation = userInfoBean;
                                Logger.e(PTApplication.myInfomation + "");
                                PTApplication.userId = userId_temp;
                                PTApplication.userToken = userToken_temp;
                                // 融云
                                new RongCloudInitUtils().RongCloudInit();

                            } else {
                                ToastUtils.getToast(SplashActivity.this, userInfoBean.getMsg() + "，请重新登录");
                                // 清除本地记录
                                SharedPreferences.Editor editor = sp.edit();
                                editor.clear().apply();
                            }
                            // EventBus发送更新界面消息
                            EventBus.getDefault().post(userInfoBean);
                        }
                    });
        }
        startHome();
    }


    public void startHome() {
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
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class).setFlags(AppConstants.YY_PT_NAVIGATION_SPLASH_REQUEST_CODE));
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


    public void getUserInfo() {

    }
}
