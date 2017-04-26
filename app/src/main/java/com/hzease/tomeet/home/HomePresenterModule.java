package com.hzease.tomeet.home;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Key on 2016/12/3 16:24
 * email: MrKey.K@gmail.com
 * description:
 */
@Module
public class HomePresenterModule {

    private final IHomeContract.View mHomeView;

    public HomePresenterModule(IHomeContract.View mHomeView) {
        this.mHomeView = mHomeView;
    }

    @Provides
    IHomeContract.View provideHomeContractView() {
        return mHomeView;
    }

}
