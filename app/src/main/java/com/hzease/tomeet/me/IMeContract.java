package com.hzease.tomeet.me;


import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.data.WaitEvaluateV2Bean;

import java.util.List;

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
        void showMyRooms(boolean isSuccess,MyJoinRoomsBean myJoinRoomBean, boolean isLoadMore);
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

        /**
         * 显示待评价成员
         * @param data
         */
        void showWaitEvaluateMember(WaitEvaluateV2Bean data);

        /**
         * 显示道具数量
         * @param data
         */
        void showPropsMum(PropsMumBean.DataBean data);

        /**
         * 修改昵称成功
         */
        void showChangeNameSuccess(NoDataBean noDataBean);

        /**
         * 显示购买道具结果
         * @param success
         * @param msg
         */
        void showBuyPropsResult(int index,boolean success, String msg);

        /**
         * 判断设置结果
         * @param noDataBean
         */
        void initResult(NoDataBean noDataBean);
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
        void feedBack(String content,String token,String userId,String url);


        /**
         * 实名认证
         */
        void authorized(String idCard,String realName,String token,String userId);

        /**
         * 游戏结束
         * @param roomId
         */
        void gameFinish(long roomId);

        /**
         * 待评价好友
         */
        void waitEvaluate(long roomId,String token,String userId);

        /**
         * 查看道具数量
         * @param token
         * @param userId
         */
        void findPropsMum(String token,String userId);

        /**
         * 修改昵称
         */
        void changeNickName(String newNickName,String token,String userId);

        /**
         * 购买道具
         */
        void buyProps(int index,Integer count,String token,Integer type,String userId);

        /**
         * 设置在线
         * @param isOnline
         * @param roomId
         */
        void setOnline(boolean isOnline, String roomId);

        /**
         * 设置账号
         */
        void setAccount(String account,String token,String userId);
    }
}
