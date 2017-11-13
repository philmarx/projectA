package com.hzease.tomeet.home;


import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.NoDataBean;

import java.util.List;

/**
 * Created by Key on 2016/11/25 01:13
 * email: MrKey.K@gmail.com
 * description: 用来写View接口和Presenter接口的契约
 */

public interface IHomeContract {

    interface View extends IBaseView<Presenter> {
        /**
         * 显示所有房间
         */
        void initRoomsList(boolean isSuccess, List<HomeRoomsBean.DataBean> date,boolean isLoadMore);

        /**
         * 显示或刷新 昵称和头像
         */
        void setAvatarAndNickname();

        /**
         * 加载更多
         * @param data
         */

        /**
         * 进入房间
         */
        void joinTheRoom(NoDataBean noDataBean, String roomId, String password);

        void changeLoadView(boolean isShown);

        void loadChatRoomInfo(GameChatRoomBean gameChatRoomBean);
    }

    interface Presenter extends IBasePresenter {
        /**
         * 查看所有房间
         */
        void loadAllRooms(String city,String gameId,String games,double latitude,double longitude,Integer page,Integer size,String sort,Integer state,boolean isLoadMore);

        /**
         * 检查是否可以加入房间
         */
        void canIJoinTheRoom(String roomId, String password);

        /**
         * 加载聊天室的数据
         */
        void loadChatRoom(String roomId);
    }
}
