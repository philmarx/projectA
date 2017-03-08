package pro.yueyuan.project_t.me;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Key on 2016/12/3 16:24
 * email: MrKey.K@gmail.com
 * description:
 */
@Module
public class MinePresenterModule {

    private final IMineContract.View mMineView;

    public MinePresenterModule(IMineContract.View mHomeView) {
        this.mMineView = mHomeView;
    }

    @Provides
    IMineContract.View provideHomeContractView() {
        return mMineView;
    }

}
