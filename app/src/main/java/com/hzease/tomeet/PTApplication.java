package com.hzease.tomeet;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.alibaba.sdk.android.oss.OSS;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.data.source.DaggerIPTRepositoryComponent;
import com.hzease.tomeet.data.source.IPTRepositoryComponent;
import com.orhanobut.logger.Logger;

import java.io.File;

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

    // 登录状态
    public static int myLoadingStatus = AppConstants.YY_PT_LOGIN_FAILED;
    // 是否在后台，是否打开过home界面
    public static boolean isInBackground = false;

    // 上线开关 false 为关闭 debug 模式
    public static boolean mDebug = true;
    //!AppConstants.YY_PT_SERVER_PATH.equals("http://tomeet-app.hzease.com/");

    // 用户信息
    public static UserInfoBean myInfomation = null;

    // 退出时好友的界面
    public static int[] friendType = AppConstants.GOLD_POINT;

    // 版本号
    public static String appVersion;

    //小纸条数量
    public static int SmallPaperMember = 0;

    // 融云是否初始化
    public static boolean isRongCloudInit = false;

    // Retrofit
    private static RequestService mRequestService;

    // 用户ID
    public static String userId = "";
    // 用户TOKEN
    public static String userToken = "";
    //经度
    public static double myLongitude = 120.076089;
    //纬度
    public static double myLatitude = 30.316719;
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

    // 当前可见Activity
    public static Context currentStartActivity;


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

    private static Dialog loadingDialog;

    public static void showLoadingDialog() {
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    public static void hideLoadingDialog() {
        if (loadingDialog.isShowing()) {
            loadingDialog.hide();
        }
    }
    /*
    .doAfterTerminate(new Action0() {
        @Override
        public void call() {
            // 关闭转圈
            PTApplication.hideLoadingDialog();
        }
    })
            .doOnSubscribe(new Action0() {
        @Override
        public void call() {
            // 转圈
            PTApplication.showLoadingDialog();
        }
    })
*/
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




        // dagger2
        mIPTRepositoryComponent = DaggerIPTRepositoryComponent.builder()
                .pTApplicationModule(new PTApplicationModule(mContext))
                .build();

        // retrofit
        mRequestService = new Retrofit.Builder()
                .baseUrl(AppConstants.YY_PT_SERVER_PATH)
                .addConverterFactory(ScalarsConverterFactory.create())      //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())         //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //增加返回值为Oservable<T>的支持
                .build()
                .create(RequestService.class); //这里采用的是Java的动态代理模式，把请求方式写这里

        // 打印本地路径
        Logger.i("file_path: " + imageLocalCachePath + "\nuri: " + imageLocalCache);
        Logger.i("VERSION.SDK_INT: " + Build.VERSION.SDK_INT + "\nDeBug tyep: " + mDebug);

        loadingDialog = new Dialog(this, R.style.Translucent_NoTitle);
        loadingDialog.setContentView(R.layout.load_view);
        loadingDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        loadingDialog.setCanceledOnTouchOutside(false);
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
