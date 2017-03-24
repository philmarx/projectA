package pro.yueyuan.project_t.chat;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Key on 2017/3/24 17:20
 * email: MrKey.K@gmail.com
 * description:
 */

@Module
public class ChatPresenterModule {

    private final IChatContract.View mChatView;

    public ChatPresenterModule(IChatContract.View mChatView) {
        this.mChatView = mChatView;
    }

    @Provides
    IChatContract.View provideChatContractView() {
        return mChatView;
    }

}
