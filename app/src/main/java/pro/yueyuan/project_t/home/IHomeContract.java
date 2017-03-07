package pro.yueyuan.project_t.home;


import pro.yueyuan.project_t.IBasePresenter;
import pro.yueyuan.project_t.IBaseView;

/**
 * Created by Key on 2016/11/25 01:13
 * email: MrKey.K@gmail.com
 * description: 用来写View接口和Presenter接口的契约
 */

public interface IHomeContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 显示我的头像
         */
        void showMyAvatar();
    }

    interface Presenter extends IBasePresenter {

        /**
         * 加载我的头像
         */
        void loadMyAvatar();

    }
}
