package com.hzease.tomeet.game;

import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;
import com.hzease.tomeet.data.GameChatRoomBean;

/**
 * Created by Key on 2017/5/4 14:21
 * email: MrKey.K@gmail.com
 * description:
 */

public interface IGameChatRoomContract {
    interface View extends IBaseView<Presenter> {

        /**
         * 加载房间数据
         */
        void refreshGameChatRoomInfo(GameChatRoomBean gameChatRoomBean);

        /**
         * 改变准备按钮状态
         */
        void changeReadyOrCancel();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 获取房间信息
         */
        //void getRoomInfo();

        /**
         * 离开房间,不退出
         */
        void leaveRoom(String roomId);

        /**
         * 获取房间数据
         */
        void getGameChatRoomInfo(String roomId);
        /**
         * 准备或取消
         */
        void ReadyOrCancel(boolean amIReady, String roomId, boolean amIManager);
        /**
         * 退出房间
         */
        void exitRoom(String roomId);
    }
}
