package pro.yueyuan.project_t.ranking;


import pro.yueyuan.project_t.IBasePresenter;
import pro.yueyuan.project_t.IBaseView;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.data.MyJoinRoomBean;
import pro.yueyuan.project_t.data.RankingBean;
import pro.yueyuan.project_t.data.UserOrderBean;

/**
 * Created by Key on 2016/11/25 01:13
 * email: MrKey.K@gmail.com
 * description: 用来写View接口和Presenter接口的契约
 */

public interface IRankContract {

    interface View extends IBaseView<Presenter> {

        /**
         * 刷新adapter
         */
        void reflush(RankingBean rankingBean);
    }

    interface Presenter extends IBasePresenter {
        /**
         * 请求排行数据
         */
        void getRankingOrder(Integer gameId);
    }
}
