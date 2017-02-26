package pro.yueyuan.project_t.data.source;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pro.yueyuan.project_t.data.source.local.PTLocalDataSource;
import pro.yueyuan.project_t.data.source.remote.PTRemoteDataSource;

/**
 * Created by Key on 2016/11/28 22:41
 * email: MrKey.K@gmail.com
 * description:
 */
@Module
public class PTRepositoryModule {
    @Singleton
    @Provides
    @Local
    IPTDataSource providePTLocalDataSource(Context context) {
        return new PTLocalDataSource(context);
    }

    @Singleton
    @Provides
    @Remote
    IPTDataSource providePTRemoteDataSource(Context context) {
        return new PTRemoteDataSource(context);
    }
}
