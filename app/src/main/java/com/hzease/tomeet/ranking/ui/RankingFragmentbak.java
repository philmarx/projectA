package com.hzease.tomeet.ranking.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameTypeBean;
import com.hzease.tomeet.data.RankingBean;
import com.hzease.tomeet.ranking.IRankContract;
import com.hzease.tomeet.ranking.fragment.FragmentWerewolfkilled;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/6/12.
 */

public class RankingFragmentbak extends BaseFragment implements IRankContract.View {
    @BindView(R.id.tl_ranking_tab_type_fmt)
    TabLayout tl_ranking_tab_type_fmt;
    @BindView(R.id.vp_ranking_rank_type_fmt)
    ViewPager vp_ranking_rank_type_fmt;
    private List<Fragment> mRankingView;
    private String[] strings = {"","","","","","","","","","","","","",""};
    private int[] gameType = {R.drawable.two_one1_1,R.drawable.two_one1_2,R.drawable.two_one1_3,R.drawable.two_one1_4,R.drawable.two_one1_5,R.drawable.two_one1_6,
            R.drawable.two_one2_1,R.drawable.two_one2_2,R.drawable.two_one2_3,R.drawable.two_one2_4,R.drawable.two_one2_5,R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2};
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IRankContract.Presenter mPresenter;

    public static RankingFragmentbak newInstance(){
        return new RankingFragmentbak();
    }
    @Override
    public void setPresenter(IRankContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void reflush(RankingBean rankingBean) {

    }

    @Override
    public void showMainList(List<GameTypeBean.ChildrenBean> data) {

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_rankingbak;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initFragment();
        tl_ranking_tab_type_fmt.setTabMode(TabLayout.MODE_SCROLLABLE);
        vp_ranking_rank_type_fmt.setAdapter(new FragmentPagerAdapter(getActivity().getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mRankingView.get(position);
            }

            @Override
            public int getCount() {
                return mRankingView.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return strings[position];
            }
        });
        tl_ranking_tab_type_fmt.setupWithViewPager(vp_ranking_rank_type_fmt);
        for (int i = 0; i < gameType.length; i++) {
            tl_ranking_tab_type_fmt.getTabAt(i).setIcon(gameType[i]);
        }
    }



    private void initFragment() {
        mRankingView = new ArrayList<>();
       /* FragmentBadminton badminton = new FragmentBadminton();
        FragmentBasketball basketball = new FragmentBasketball();
        FragmentChess chess = new FragmentChess();
        FragmentEscape escape = new FragmentEscape();
        FragmentFootball football = new FragmentFootball();
        FragmentGo go = new FragmentGo();
        FragmentMahjong mahjong = new FragmentMahjong();
        FragmentPoker poker = new FragmentPoker();
        FragmentSing sing = new FragmentSing();
        FragmentTabletennis tabletennis = new FragmentTabletennis();
        FragmentTennis tennis = new FragmentTennis();
        FragmentThreekingdoms threekingdoms = new FragmentThreekingdoms();
        FragmentVolleyball volleyball = new FragmentVolleyball();
        FragmentWerewolfkilled werewolfkilled = new FragmentWerewolfkilled();
        mRankingView.add(badminton);
        mRankingView.add(basketball);
        mRankingView.add(chess);
        mRankingView.add(escape);
        mRankingView.add(football);
        mRankingView.add(go);
        mRankingView.add(mahjong);
        mRankingView.add(poker);
        mRankingView.add(sing);
        mRankingView.add(tabletennis);
        mRankingView.add(tennis);
        mRankingView.add(threekingdoms);
        mRankingView.add(volleyball);
        mRankingView.add(werewolfkilled);*/
        for (int i = 0; i < 14; i++) {
            FragmentWerewolfkilled werewolfkilled = new FragmentWerewolfkilled(i + 6);
            mRankingView.add(werewolfkilled);
        }

    }
}
