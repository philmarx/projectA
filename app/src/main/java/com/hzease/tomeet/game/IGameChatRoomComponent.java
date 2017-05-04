package com.hzease.tomeet.game;

import com.hzease.tomeet.data.source.IPTRepositoryComponent;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.hzease.tomeet.utils.FragmentScoped;

import dagger.Component;

/**
 * Created by Key on 2017/5/4 14:21
 * email: MrKey.K@gmail.com
 * description:
 */
@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = GameChatRoomPresenterModule.class)
public interface IGameChatRoomComponent {
    void inject(GameChatRoomActivity gameChatRoomActivity);
}
