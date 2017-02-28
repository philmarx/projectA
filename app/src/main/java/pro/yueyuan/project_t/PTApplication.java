package pro.yueyuan.project_t;

import android.app.Application;
import android.content.Context;
import android.util.Log;

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
    private String token="SjykilqBw8E0WUL0RLPHkOwm9bVOtDZvjYeASaGUo4L3cH/HHTUlV1WumXlPwZDYuC4vKmvdErT+gB9/ydg1dA==";
    private IPTRepositoryComponent mIPTRepositoryComponent;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
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
    public IPTRepositoryComponent getIPTRepositoryComponent() {
        return mIPTRepositoryComponent;
    }
    public void RongCloudInit() {
        RongIM.init(mContext);
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
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
}
