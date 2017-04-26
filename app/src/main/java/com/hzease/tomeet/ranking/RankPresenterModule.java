package com.hzease.tomeet.ranking;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Key on 2016/12/3 16:24
 * email: MrKey.K@gmail.com
 * description:
 */
@Module
public class RankPresenterModule {

    private final IRankContract.View mMeView;

    public RankPresenterModule(IRankContract.View mHomeView) {
        this.mMeView = mHomeView;
    }

    @Provides
    IRankContract.View provideHomeContractView() {
        return mMeView;
    }

}
