package com.hzease.tomeet.game;

import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;

/**
 * Created by Key on 2017/5/4 14:21
 * email: MrKey.K@gmail.com
 * description:
 */

public interface IGameChatRoomContract {
    interface View extends IBaseView<Presenter> {

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
    }
}
