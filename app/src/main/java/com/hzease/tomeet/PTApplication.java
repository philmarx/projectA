package com.hzease.tomeet;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.alibaba.sdk.android.oss.OSS;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.data.source.DaggerIPTRepositoryComponent;
import com.hzease.tomeet.data.source.IPTRepositoryComponent;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * Created by Key on 2016/11/30 15:49
 * email: MrKey.K@gmail.com
 * description:
 */

public class PTApplication extends Application {

    // 上线开关
    private boolean mDebug = true;

    // 用户信息
    public static UserInfoBean myInfomation = null;

    // 融云是否初始化
    public static boolean isRongCloudInit = false;

    // Retrofit
    private static RequestService mRequestService;

    // 用户ID
    public static String userId = "";
    // 用户TOKEN
    public static String userToken = "";
    //经度
    public static double myLongitude = 222.22;
    //纬度
    public static double myLatitude = 222.22;
    // 城市名
    public static String cityName = "杭州市";
    // 阿里云操作OSS对象
    public static OSS aliyunOss;
    // OSS的过期时间
    public static long aliyunOssExpiration;
    private IPTRepositoryComponent mIPTRepositoryComponent;

    private static PTApplication mContext;

    // 全局未读书
    public static int unReadNumber;
    // 小红点
    public static Badge badge;
    // 未读监听
    public static IUnReadMessageObserver unReadMessageObserver;


    public static PTApplication getInstance() {
        return mContext;
    }

    public static RequestService getRequestService() {
        return mRequestService;
    }

    // 本地图片上传缓存路径,只有一个,上传下一张时覆盖上一次,节约空间
    // public static File imageLocalCachePath = new File(Environment.getExternalStorageDirectory(), "/ease/image");
    public static File imageLocalCachePath;
    public static Uri imageLocalCache;

    @Override
    public void onCreate() {
        super.onCreate();
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓最早初始化的值↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        mContext = this;
        imageLocalCachePath = new File(getExternalCacheDir(), "/ease/image");
        imageLocalCache = Uri.fromFile(new File(imageLocalCachePath, "/imageTemp"));
        //------------------↑↑↑↑↑↑需要在成员初始化却不能在成员初始化的,必须最早完成初始化↑↑↑↑↑↑--------------------------

        // 小红点和监听器的初始化
        badge= new QBadgeView(this);
        unReadMessageObserver = new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                PTApplication.unReadNumber = i;
                PTApplication.badge.setBadgeNumber(i);
            }
        };


        Config.DEBUG = mDebug;

        //友盟分享登录初始化完成
        UMShareAPI.get(this);

        // Logger 开关
        Logger.init("后会有期").logLevel(mDebug ? LogLevel.FULL : LogLevel.NONE);

        // dagger2
        mIPTRepositoryComponent = DaggerIPTRepositoryComponent.builder()
                .pTApplicationModule(new PTApplicationModule(mContext))
                .build();

        //极光初始化
        JPushInterface.setDebugMode(mDebug);
        JPushInterface.init(this);

        // 初始化
        RongIM.init(PTApplication.getInstance());

        // Realm 初始化
        // Call `Realm.init(Context)` before creating a RealmConfiguration
        Realm.init(getApplicationContext());


        mRequestService = new Retrofit.Builder()
                .baseUrl(AppConstants.YY_PT_SERVER_PATH)
                .addConverterFactory(ScalarsConverterFactory.create())      //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())         //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //增加返回值为Oservable<T>的支持
                .build()
                .create(RequestService.class); //这里采用的是Java的动态代理模式，把请求方式写这里



        // 打印本地路径
        Logger.i("file_path: " + imageLocalCachePath + "\nuri: " + imageLocalCache);
        Logger.i("VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
    }
    {
        //设置微信的APPID和APPSCRET
        PlatformConfig.setWeixin(AppConstants.TOMEET_WX_APP_ID, AppConstants.TOMEET_WX_APP_SECRET);
        //设置QQ的APPID和APPKEY
        PlatformConfig.setQQZone("1105938565", "ztPSsqEHbkb4iShv");
    }

    /**
     * 仓库初始化
     *
     * @return
     */
    public IPTRepositoryComponent getIPTRepositoryComponent() {
        return mIPTRepositoryComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
