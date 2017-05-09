package com.hzease.tomeet.circle;


import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentConfig;
import com.hzease.tomeet.data.CommentItemBean;

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
         * 展示喊话内容
         */
        void showDeclaration(CommentItemBean commentItemBean,boolean isLoadMore);

        void updateEditTextBodyVisible(int visible, CommentConfig config);

        /**
         * 完成喊话后的展示
         */
        void showDeclareSucccess(boolean isSuccess,String msg);

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
         * 显示文本框
         * @param config
         */
        void showEditTextBody(CommentConfig config);

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
    }


}
