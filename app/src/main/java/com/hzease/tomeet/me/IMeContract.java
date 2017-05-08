package com.hzease.tomeet.me;


import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeRoomsBean;

/**
 * Created by Key on 2016/11/25 01:13
 * email: MrKey.K@gmail.com
 * description: 用来写View接口和Presenter接口的契约
 */

public interface IMeContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 显示 或 刷新 我的信息
         */
        void showMyInfo();

        /**
         * 显示我的房间
         * @param myJoinRoomBean
         */
        void showMyRooms(HomeRoomsBean myJoinRoomBean,boolean isLoadMore);
        /**
         * 更新密码成功
         */
        void updatePwdSuccess(boolean isSuccess,String msg);

        /**
         * 提交反馈成功
         */
        void feedBackSuccess(boolean isSuccess,String msg);

        /**
         * 认证成功
         */
        void authorizedSuccess();

        /**
         * 显示结束房间信息
         * @param data
         */
        void showFinishInfo(GameFinishBean.DataBean data);
    }

    interface Presenter extends IBasePresenter {

        /**
         * 加载我的信息
         */
        void loadMyInfo();

        /**
         * 注销用户
         */
        void logoutUser();
        /**
         * 显示我加入的房间数据
         */
        void getMyJoinRooms(Integer page,Integer size,String token,String userId,boolean isLoadMore);

        /**
         * 设置新密码
         */
        void updatePwd(String password,String password2,String token,String userId);

        /**
         * 提交反馈
         */
        void feedBack(String content,String token,String userId);


        /**
         * 实名认证
         */
        void authorized(String idCard,String realName,String token,String userId);

        void gameFinish(long roomId);

    }
}
