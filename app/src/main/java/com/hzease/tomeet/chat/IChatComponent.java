package com.hzease.tomeet.chat;

import dagger.Component;
import com.hzease.tomeet.chat.ui.ChatVersion2Activity;
import com.hzease.tomeet.data.source.IPTRepositoryComponent;
import com.hzease.tomeet.utils.FragmentScoped;

/**
 * Created by Key on 2017/3/24 17:19
 * email: MrKey.K@gmail.com
 * description:
 */

@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = ChatPresenterModule.class)
public interface IChatComponent {
    void inject(ChatVersion2Activity mChatVersion2Activity);
}
