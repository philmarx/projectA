package pro.yueyuan.project_t;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.alibaba.sdk.android.oss.OSS;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.realm.Realm;
import pro.yueyuan.project_t.data.UserInfoBean;
import pro.yueyuan.project_t.data.source.DaggerIPTRepositoryComponent;
import pro.yueyuan.project_t.data.source.IPTRepositoryComponent;
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

    // 阿里云操作OSS对象
    public static OSS aliyunOss;
    // OSS的过期时间
    public static long aliyunOssExpiration;

    private IPTRepositoryComponent mIPTRepositoryComponent;

    private static PTApplication mContext;

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

        Config.DEBUG=true;
        //友盟分享登录初始化完成
        UMShareAPI.get(this);
        // Logger 开关
        Logger.init("Project-T").logLevel(LogLevel.FULL);

        // dagger2
        mIPTRepositoryComponent = DaggerIPTRepositoryComponent.builder()
                .pTApplicationModule(new PTApplicationModule(mContext))
                .build();

        //极光初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        JPushInterface.setAlias(mContext, "testAlias", new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
            }
        });

        mRequestService = new Retrofit.Builder()
                .baseUrl(AppConstants.YY_PT_SERVER_PATH)
                .addConverterFactory(ScalarsConverterFactory.create())      //增加返回值为String的支持
                .addConverterFactory(GsonConverterFactory.create())         //增加返回值为Gson的支持(以实体类返回)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   //增加返回值为Oservable<T>的支持
                .build()
                .create(RequestService.class); //这里采用的是Java的动态代理模式，把请求方式写这里

        // Realm 初始化
        // Call `Realm.init(Context)` before creating a RealmConfiguration
        Realm.init(getApplicationContext());
        // 用指定的名称作为数据库名
        // 不在这里初始化了.根据userId来初始化数据库
        // RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name("yyproject.realm").build();
        // Realm.setDefaultConfiguration(realmConfiguration);

        // 打印本地路径
        Logger.i("file_path: " + imageLocalCachePath + "\nuri: " + imageLocalCache);
        Logger.i("VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
    }
    {
        //设置微信的APPID和APPSCRET
        PlatformConfig.setWeixin("wx7c783e373c89fc39","29a48cc52ebf3b647bffdd3b5e617fda");
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
