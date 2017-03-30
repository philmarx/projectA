package pro.yueyuan.project_t.chat;

import javax.inject.Inject;

import io.realm.Realm;
import pro.yueyuan.project_t.data.RealmFriendBean;
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
        updateFriendsDate();
    }


    /**
     * 更新好友数据
     */
    @Override
    public void updateFriendsDate() {
        mChatView.updateFriendList();
    }

    /**
     * 清除会话的未读
     *
     * @param targetId 会话ID
     */
    @Override
    public void clearUnread(final String targetId) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmFriendBean first = realm.where(RealmFriendBean.class).equalTo("id", Long.valueOf(targetId)).findFirst();
                    if (first != null) {
                        first.setUnreadCount(0);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            realm.close();
        }
    }
}
