package com.hzease.tomeet.ranking;


import com.hzease.tomeet.IBasePresenter;
import com.hzease.tomeet.IBaseView;
import com.hzease.tomeet.data.RankingBean;

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