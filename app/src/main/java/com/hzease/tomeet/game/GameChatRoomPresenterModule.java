package com.hzease.tomeet.game;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Key on 2017/5/4 14:21
 * email: MrKey.K@gmail.com
 * description:
 */
@Module
public class GameChatRoomPresenterModule {
    private final IGameChatRoomContract.View mView;

    public GameChatRoomPresenterModule(IGameChatRoomContract.View mView) {
        this.mView = mView;
    }

    @Provides
    IGameChatRoomContract.View provideGameChatRoomContractView() {
        return mView;
    }
}
