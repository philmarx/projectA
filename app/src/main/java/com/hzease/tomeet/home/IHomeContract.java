package com.hzease.tomeet.home;


import java.util.List;

import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.ShowGameListBean;

/**
 * Created by Key on 2016/11/25 01:13
 * email: MrKey.K@gmail.com
 * description: 用来写View接口和Presenter接口的契约
 */

public interface IHomeContract {

    interface View extends IBaseView<Presenter> {

        /**
         *
         * @param data
         */
        void initGameList(List<ShowGameListBean.DataBean> data);

        /**
         * 显示所有房间
         */
        void initRoomsList(List<HomeRoomsBean.DataBean> date,boolean isLoadMore);

        /**
         * 显示或刷新 昵称和头像
         */
        void setAvatarAndNickname();

        /**
         * 加载更多
         * @param data
         */
    }

    interface Presenter extends IBasePresenter {

        /**
         * 加载活动名称
         */
        void loadGameList(String key,String value);

        /**
         * 查看所有房间
         */
        void loadAllRooms(String city,Integer gameId,String games,double latitude,double longitude,Integer page,Integer size,String sort,Integer state,boolean isLoadMore);

    }
}
