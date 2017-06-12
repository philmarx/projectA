package com.hzease.tomeet.ranking.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import javax.inject.Inject;

import com.hzease.tomeet.NavigationActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.ranking.DaggerIRankComponent;
import com.hzease.tomeet.ranking.IRankContract;
import com.hzease.tomeet.ranking.RankPresenter;
import com.hzease.tomeet.ranking.RankPresenterModule;
import com.hzease.tomeet.utils.ActivityUtils;

public class RankingActivity extends NavigationActivity{

    @Inject
    RankPresenter mRankPresenter;

    /**
     * fragment的集合
     */
    public ArrayList<Fragment> mFragmentList;

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_ranking;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        navigation_bottom.getMenu().findItem(R.id.navigation_ranking).setChecked(true).setEnabled(false);
        if (mFragmentList == null || mFragmentList.size() != 1) {
            mFragmentList = new ArrayList<>();
            //RankingFragment rankingFragment = RankingFragment.newInstance();
            //mFragmentList.add(rankingFragment);
            RankingFragmentbak rankingFragmentbak = RankingFragmentbak.newInstance();
            mFragmentList.add(rankingFragmentbak);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_ranking_activity);
        }

        // dagger2
        int size = mFragmentList.size();
        for (int i = 0; i < size; i++) {
            DaggerIRankComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    .rankPresenterModule(new RankPresenterModule(((IRankContract.View) (mFragmentList.get(i)))))
                    .build().inject(this);
        }

    }
}
