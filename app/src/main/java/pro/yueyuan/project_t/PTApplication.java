package pro.yueyuan.project_t;

import android.app.Application;
import android.content.Context;

import pro.yueyuan.project_t.data.source.DaggerIPTRepositoryComponent;
import pro.yueyuan.project_t.data.source.IPTRepositoryComponent;


/**
 * Created by Key on 2016/11/30 15:49
 * email: MrKey.K@gmail.com
 * description:
 */

public class PTApplication extends Application {
    
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
    }

    public IPTRepositoryComponent getIPTRepositoryComponent() {
        return mIPTRepositoryComponent;
    }



}
