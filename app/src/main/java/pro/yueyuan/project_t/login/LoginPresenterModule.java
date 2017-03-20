package pro.yueyuan.project_t.login;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Key on 2016/12/3 16:24
 * email: MrKey.K@gmail.com
 * description:
 */
@Module
public class LoginPresenterModule {

    private final ILoginContract.View mLoginView;

    public LoginPresenterModule(ILoginContract.View mLoginView) {
        this.mLoginView = mLoginView;
    }

    @Provides
    ILoginContract.View provideLoginContractView() {
        return mLoginView;
    }

}
