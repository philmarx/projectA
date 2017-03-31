package pro.yueyuan.project_t.chat;

import io.rong.imlib.model.Message;
import pro.yueyuan.project_t.IBasePresenter;
import pro.yueyuan.project_t.IBaseView;

/**
 * Created by Key on 2017/3/24 17:12
 * email: MrKey.K@gmail.com
 * description:
 */

public interface IChatContract {
    interface View extends IBaseView<Presenter> {
        /**
         * 更新好友列表
         */
        void updateFriendList();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 更新好友数据
         */
        void updateFriendsDate();

        /**
         * 清除会话的未读
         * @param targetId 会话ID
         */
        void clearUnread(String targetId);

        /**
         * 发送消息后更新数据库(为了更新界面)
         * @param message 消息实体类
         */
        void sendMessage(Message message);
    }
}
