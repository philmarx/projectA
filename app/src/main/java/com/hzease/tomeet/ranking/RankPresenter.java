package com.hzease.tomeet.ranking;

import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.RankingBean;
import com.hzease.tomeet.data.source.PTRepository;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2016/11/25 01:15
 * email: MrKey.K@gmail.com
 * description:
 */

public final class RankPresenter implements IRankContract.Presenter {

    private final PTRepository mPTRepository;

    private final IRankContract.View mRankingView;

    @Inject
    public RankPresenter(PTRepository mPTRepository, IRankContract.View mRankingView) {
        this.mPTRepository = mPTRepository;
        this.mRankingView = mRankingView;
    }

    @Inject
    void setupListeners() {
        mRankingView.setPresenter(this);
    }

    @Override
    public void start() {
        // 我在onResume()里面调用了，可以写跟生命周期相关的东西
    }

    @Override
    public void getRankingOrder(Integer gameId) {
        PTApplication.getRequestService().getRanking(gameId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RankingBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError");
                    }

                    @Override
                    public void onNext(RankingBean rankingBean) {
                        Logger.e("onNext");
                        mRankingView.reflush(rankingBean);
                        Logger.e(rankingBean.getData().toString());
                    }
                });
    }
}
