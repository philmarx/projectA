package com.hzease.tomeet.game;

import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.NoDataBean;

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

        /**
         * 签到后改变状态
         * @param noDataBean 签到是否成功
         */
        void changeCheckButton(NoDataBean noDataBean);

        /**
         * 改变加载动画显隐
         * @param isShown 是否显示
         */
        void changeLoadView(boolean isShown);
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
         * 成员准备或取消
         */
        void memberReadyOrCancel(boolean amIReady, String roomId);
        /**
         * 退出房间
         */
        void exitRoom(String roomId);

        /**
         * 房主 准备或取消
         */
        void managerReadyOrCancel(String roomId, boolean isBegin);

        /**
         * 出发
         * @param roomId 房间ID
         */
        void memberGo(String roomId);

        /**
         * 签到发送位置
         * @param roomId 房间ID
         */
        void checkSendLocation(String roomId);

        /**
         * 点击我没迟到
         * @param roomId 房间ID
         */
        void iAmNotLate(String roomId);
    }
}
