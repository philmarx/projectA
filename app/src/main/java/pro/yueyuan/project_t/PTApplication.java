package pro.yueyuan.project_t;

import android.app.Application;
import android.content.Context;
import android.util.Log;

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
    private String token="T3ZgYhZ1YNskY7AR4f/ap5T8Ur8GfXBKqC4xea9hsp9IuGDSE7tTB17RIJUVpgyunJPpWdIgNcqill4P2qSRrA==";
    private IPTRepositoryComponent mIPTRepositoryComponent;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        RongIM.init(mContext);
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

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

        // dagger2
        mIPTRepositoryComponent = DaggerIPTRepositoryComponent.builder()
                .pTApplicationModule(new PTApplicationModule(mContext))
                .build();
    }

    public IPTRepositoryComponent getIPTRepositoryComponent() {
        return mIPTRepositoryComponent;
    }



}
