package pro.yueyuan.project_t;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import pro.yueyuan.project_t.data.source.DaggerIPTRepositoryComponent;
import pro.yueyuan.project_t.data.source.IPTRepositoryComponent;


/**
 * Created by Key on 2016/11/30 15:49
 * email: MrKey.K@gmail.com
 * description:
 */

public class PTApplication extends Application {

    // 用户ID
    public static String userId = "";
    // 用户TOKEN
    public static String userToken ="SjykilqBw8E0WUL0RLPHkOwm9bVOtDZvjYeASaGUo4L3cH/HHTUlV1WumXlPwZDYuC4vKmvdErT+gB9/ydg1dA==";
    // OSS AccessKey
    public static String OssAccessKey = "";
    // OSS AccessSecret
    public static String OssAccessSecret = "";
    // OSS Token
    public static String OssToken = "";

    // 阿里云操作OSS对象 , 在splash中初始化
    public static OSS aliyunOss;

    private IPTRepositoryComponent mIPTRepositoryComponent;
    private static PTApplication mContext;

    public static PTApplication getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        // dagger2
        mIPTRepositoryComponent = DaggerIPTRepositoryComponent.builder()
                .pTApplicationModule(new PTApplicationModule(mContext))
                .build();
        //融云初始化
        RongCloudInit();
        //极光初始化
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }



    /**
     * 仓库初始化
     * @return
     */
    public IPTRepositoryComponent getIPTRepositoryComponent() {
        return mIPTRepositoryComponent;
    }

    /**
     * 融云初始化
     */
    public void RongCloudInit() {
        RongIM.init(mContext);
        RongIM.connect(userToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e("CCC","1111111");
            }
            @Override
            public void onSuccess(String s) {
                Log.e("AAA","userid:"+s);
            }
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("BBB","111");
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
