package com.hzease.tomeet.game;

import com.hzease.tomeet.data.source.PTRepository;

import javax.inject.Inject;

/**
 * Created by Key on 2017/5/4 14:20
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomPresenter implements IGameChatRoomContract.Presenter{

    /**
     * 仓库
     */
    private final PTRepository mPTRepository;
    private final IGameChatRoomContract.View mView;

    @Inject
    public GameChatRoomPresenter(PTRepository mPTRepository, IGameChatRoomContract.View mView) {
        this.mPTRepository = mPTRepository;
        this.mView = mView;
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }
    @Override
    public void start() {

    }
}
