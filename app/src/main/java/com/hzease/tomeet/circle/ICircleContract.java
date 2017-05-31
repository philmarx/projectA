package com.hzease.tomeet.circle;


import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.HomeRoomsBean;

import java.util.List;

/**
 * Created by Key on 2016/11/25 01:13
 * email: MrKey.K@gmail.com
 * description: 用来写View接口和Presenter接口的契约
 */

public interface ICircleContract {


    interface View extends IBaseView<Presenter> {
        /**
         * 创建圈子成功
         */
        void createSuccess();

        /**
         * 显示推荐圈子
         * @param data
         */
        void showRecommandCircle(List<CircleInfoBean.DataBean> data);

        /**
         * 显示附近圈子
         * @param data
         */
        void showNeayByCircle(List<CircleInfoBean.DataBean> data);

        /**
         * 展示圈子详情
         * @param data
         */
        void showCircleInfo(EnterCircleInfoBean.DataBean data);

        /**
         * 加入圈子成功
         */
        void joinCircleSuccess(String msg);

        /**
         * 退出圈子成功
         * @param msg
         */
        void signOutCircleSuccess(String msg);

        /**
         * 修改圈子公告成功
         * @param msg
         */
        void modifitySuccess(String msg);

        /**
         * 显示圈内房间
         * @param data
         */
        void showRoomsByCircle(List<HomeRoomsBean.DataBean> data);

        /**
         * 显示我的圈子
         * @param data
         */
        void showMyCircle(List<CircleInfoBean.DataBean> data);

        //---------喊话的----------------------------------------------------------------------

        /**
         * 展示喊话内容
         */
        void showDeclaration(boolean isSuccess, List<CommentItemBean.DataBean> commentList, boolean isLoadMore);


        /**
         * 完成喊话后的展示
         */
        void showDeclareSucccess(boolean isSuccess,String msg);

        /**
         * 回复后刷新单条消息
         */
        void refreshOneDeclaration(CommentItemBean.DataBean dataBean);

    }

    interface Presenter extends IBasePresenter {
        /**
         * 创建圈子
         *
         * @param avatarSignature
         * @param bgSignature
         * @param city
         * @param latitude
         * @param longitude
         * @param name
         * @param notice
         * @param place
         * @param token
         * @param userId
         */
        void createCircle(String avatarSignature, String bgSignature, String city,
                          double latitude, double longitude, String name, String notice,
                          String place, String token, String userId);
        /**
         * 获取喊话内容
         *
         * @param city
         * @param page
         * @param size
         */
        void getDeclaration(String city, Integer page, Integer size,boolean isLoadMore);

        /**
         * 创建喊话
         * @param city
         * @param content
         * @param token
         * @param userId
         */
        void createDeclare(String city,String content,String token,String userId);

        /**
         * 查看推荐圈子
         */
        void findRecommand();

        /**
         * 查看附近圈子
         */
        void findNearBy(double latitude,double longitude);

        /**
         * 查看我的圈子
         */
        void findMyCircle(int page,int size,String token,String userId);
        /**
         * 获取圈子详情
         * @param circleId
         * @param token
         * @param userId
         */
        void getCircleInfo(long circleId,String token,String userId);

        /**
         * 加入该圈子
         */
        void joinCircle(long circleId,String token,String userId);

        /**
         * 退出该圈子
         */
        void signOutCircle(long circleId,String token,String userId);

        /**
         * 更改圈子公告
         */
        void moditityNotice(long circleId,String notice,String token,String userId);

        /**
         * 回复评论
         */
        void commentWho(String content, long declaration, long toUserId);
    }


}
