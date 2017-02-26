package pro.yueyuan.project_t;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Key on 2016/11/30 15:51
 * email: MrKey.K@gmail.com
 * description:
 */
@Module
public final class PTApplicationModule {

    private final Context mContext;


    PTApplicationModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }
}
