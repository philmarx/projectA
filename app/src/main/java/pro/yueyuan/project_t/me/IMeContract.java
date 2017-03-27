package pro.yueyuan.project_t.me;


import pro.yueyuan.project_t.IBasePresenter;
import pro.yueyuan.project_t.IBaseView;

/**
 * Created by Key on 2016/11/25 01:13
 * email: MrKey.K@gmail.com
 * description: 用来写View接口和Presenter接口的契约
 */

public interface IMeContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 显示我的头像
         */
        void showMyAvatar();

        /**
         * 显示我的信息
         */
        void showMyInfo(String nickName,String amount);
    }

    interface Presenter extends IBasePresenter {

        /**
         * 加载我的头像
         */
        void loadMyAvatar();

        /**
         * 加载我的信息
         */
        void loadMyInfo(String id,String token);

        /**
         * 注销用户
         */
        void logoutUser();
    }
}
