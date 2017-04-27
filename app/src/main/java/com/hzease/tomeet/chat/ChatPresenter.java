package com.hzease.tomeet.chat;

import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import io.realm.Realm;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;
import com.hzease.tomeet.data.RealmFriendBean;
import com.hzease.tomeet.data.source.PTRepository;

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
            realm.executeTransaction(new Realm.Transaction() {
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

    /**
     * 发送消息后更新数据库(为了更新界面)
     *
     * @param message 消息实体类
     */
    @Override
    public void sendMessage(final Message message) {
        Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmFriendBean first = realm.where(RealmFriendBean.class).equalTo("id", Long.valueOf(message.getTargetId())).findFirst();
                    if (first != null) {
                        first.setLastTime(message.getSentTime());
                        String messageContent = "[未能识别消息类型]";
                        switch (message.getObjectName()) {
                            case "RC:TxtMsg":
                                // 文字消息TextMessage
                                messageContent = new TextMessage(message.getContent().encode()).getContent();
                                break;
                            case "RC:VcMsg":
                                // 语音消息VoiceMessage
                                messageContent = "[语音消息]";
                                break;
                            case "RC:ImgMsg":
                                // 图片消息ImageMessage
                                messageContent = "[图片消息]";
                                break;
                            case "RC:ImgTextMsg":
                                // 图文消息RichContentMessage
                                messageContent = "[图文消息]";
                                break;
                            case "RC:LBSMsg":
                                // 位置消息LocationMessage
                                messageContent = "[位置消息]";
                                break;
                            case "RC:StkMsg":
                                // 表情贴纸消息StickerMessage
                                messageContent = "[表情贴纸消息]";
                                break;
                            case "RC:PSImgTxtMsg":
                                // 公众服务单图文消息 PublicServiceRichContentMessage
                                messageContent = "[公众服务单图文消息]";
                                break;
                            case "RC:PSMultiImgTxtMsg":
                                // 公众服务多图文消息 PublicServiceMultiRichContentMessage
                                messageContent = "[公众服务多图文消息]";
                                break;
                        }
                        Logger.d(message.getSentStatus());
                        if (message.getSentStatus().equals(Message.SentStatus.FAILED)) {
                            messageContent = "[发送失败] " + messageContent;
                        }
                        first.setLastMessage(messageContent);
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
