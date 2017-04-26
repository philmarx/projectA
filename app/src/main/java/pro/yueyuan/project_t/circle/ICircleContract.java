package pro.yueyuan.project_t.circle;


import java.util.List;

import pro.yueyuan.project_t.IBasePresenter;
import pro.yueyuan.project_t.IBaseView;
import pro.yueyuan.project_t.data.HomeRoomsBean;
import pro.yueyuan.project_t.data.ShowGameListBean;

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
    }

    interface Presenter extends IBasePresenter {
        /**
         * 创建圈子
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
        void createCircle(String avatarSignature,String bgSignature,String city,
                          double latitude,double longitude,String name,String notice,
                          String place,String token,String userId);
    }
}
