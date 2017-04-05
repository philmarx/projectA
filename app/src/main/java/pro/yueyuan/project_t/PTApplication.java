package pro.yueyuan.project_t;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.alibaba.sdk.android.oss.OSS;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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

    // 融云是否初始化
    public static boolean isRongCloudInit = false;

    // Retrofit
    private static RequestService mRequestService;

    // 用户ID
    public static String userId = "";
    // 用户TOKEN
    public static String userToken = "";

    // 阿里云操作OSS对象 , 在splash中初始化
    public static OSS aliyunOss;

    // 本地图片缓存路径
    public static String imageLocalCachePath = Environment.getExternalStorageDirectory() + "/ease/image/";

    private IPTRepositoryComponent mIPTRepositoryComponent;

    private static PTApplication mContext;

    public static PTApplication getInstance() {
        return mContext;
    }

    public static RequestService getRequestService() {
        return mRequestService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

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

        Logger.d(imageLocalCachePath);

        // Realm 初始化
        // Call `Realm.init(Context)` before creating a RealmConfiguration
        Realm.init(getApplicationContext());
        // 用指定的名称作为数据库名
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().name("yyproject.realm").build();
        Realm.setDefaultConfiguration(realmConfiguration);
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
