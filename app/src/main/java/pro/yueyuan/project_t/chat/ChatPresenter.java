package pro.yueyuan.project_t.chat;

import javax.inject.Inject;

import pro.yueyuan.project_t.data.source.PTRepository;

/**
 * Created by Key on 2017/3/24 17:15
 * email: MrKey.K@gmail.com
 * description:
 */

public class ChatPresenter implements IChatContract.Presenter {

    /**
     * 仓库
     */
    private final PTRepository mPTRepository;

    private final IChatContract.View mChatView;

    @Inject
    public ChatPresenter(PTRepository mPTRepository, IChatContract.View mChatView) {
        this.mPTRepository = mPTRepository;
        this.mChatView = mChatView;
    }

    @Inject
    void setupListeners() {
        mChatView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
