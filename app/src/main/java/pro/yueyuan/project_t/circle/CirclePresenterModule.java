package pro.yueyuan.project_t.circle;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Key on 2016/12/3 16:24
 * email: MrKey.K@gmail.com
 * description:
 */
@Module
public class CirclePresenterModule {

    private final ICircleContract.View mCircleView;

    public CirclePresenterModule(ICircleContract.View mCircleView) {
        this.mCircleView = mCircleView;
    }

    @Provides
    ICircleContract.View provideHomeContractView() {
        return mCircleView;
    }

}
