package pro.yueyuan.project_t.me;


import pro.yueyuan.project_t.IBasePresenter;
import pro.yueyuan.project_t.IBaseView;
import pro.yueyuan.project_t.data.MyJoinRoomBean;
import pro.yueyuan.project_t.data.UserOrderBean;

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
        void showMyRooms(MyJoinRoomBean myJoinRoomBean);
        /**
         * 显示请求查看用户的排名信息
         */
        void showRequestUserOrder(UserOrderBean userOrderBean);
        /**
         * 更新密码成功
         */
        void updatePwdSuccess(boolean isSuccess,String msg);

        /**
         * 提交反馈成功
         */
        void feedBackSuccess(boolean isSuccess,String msg);
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
        void getMyJoinRooms(Integer page,Integer size,String token,String userId);
        /**
         * 根据userId请求用户排名数据
         */
        void getOrderById(long userId);

        /**
         * 设置新密码
         */
        void updatePwd(String password,String password2,String token,String userId);

        /**
         * 提交反馈
         */
        void feedBack(String content,String token,String userId);

    }
}
