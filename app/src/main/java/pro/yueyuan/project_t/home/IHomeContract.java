package pro.yueyuan.project_t.home;


import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;

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

public interface IHomeContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 显示我的头像
         */
        void showMyAvatar();

        /**
         *
         * @param data
         */
        void initGameList(List<ShowGameListBean.DataBean> data);

        /**
         * 显示所有房间
         */
        void initRoomsList(List<HomeRoomsBean.DataBean> date);

    }

    interface Presenter extends IBasePresenter {

        /**
         * 加载我的头像
         */
        void loadMyAvatar();

        /**
         * 加载活动名称
         */
        void loadGameList(String key,String value);

        /**
         * 查看所有房间
         */
        void loadAllRooms(Integer gameId,String games,double latitude,double longitude,Integer page,Integer size,String sort,Integer state);
    }
}
