package pro.yueyuan.project_t.me;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Key on 2016/12/3 16:24
 * email: MrKey.K@gmail.com
 * description:
 */
@Module
public class MePresenterModule {

    private final IMeContract.View mMeView;

    public MePresenterModule(IMeContract.View mHomeView) {
        this.mMeView = mHomeView;
    }

    @Provides
    IMeContract.View provideHomeContractView() {
        return mMeView;
    }

}
