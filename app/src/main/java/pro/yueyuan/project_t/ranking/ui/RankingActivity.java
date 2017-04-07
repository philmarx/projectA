package pro.yueyuan.project_t.ranking.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import javax.inject.Inject;

import pro.yueyuan.project_t.NavigationActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.me.DaggerIMeComponent;
import pro.yueyuan.project_t.me.IMeContract;
import pro.yueyuan.project_t.me.MePresenterModule;
import pro.yueyuan.project_t.me.ui.MeActivity;
import pro.yueyuan.project_t.me.ui.fragment.PersonOrderInfoFragment;
import pro.yueyuan.project_t.ranking.DaggerIRankComponent;
import pro.yueyuan.project_t.ranking.IRankContract;
import pro.yueyuan.project_t.ranking.RankPresenter;
import pro.yueyuan.project_t.ranking.RankPresenterModule;
import pro.yueyuan.project_t.utils.ActivityUtils;

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
        if (mFragmentList == null || mFragmentList.size() != 1){
            mFragmentList = new ArrayList<>();
            RankingFragment rankingFragment = RankingFragment.newInstance();
            PersonOrderInfoFragment personOrderInfoFragment = PersonOrderInfoFragment.newInstance();
            mFragmentList.add(rankingFragment);
            mFragmentList.add(personOrderInfoFragment);
            //放到contentFrame_first这个容器中
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragmentList.get(0), R.id.fl_content_ranking_activity);
        }

        // dagger2
        int size = mFragmentList.size();
        for (int i = 0; i < size-1; i++) {
            DaggerIRankComponent.builder()
                    .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                    .rankPresenterModule(new RankPresenterModule(((IRankContract.View) (mFragmentList.get(i)))))
                    .build().inject(this);
        }

        /*DaggerIMeComponent.builder()
                .iPTRepositoryComponent(((PTApplication) getApplication()).getIPTRepositoryComponent())
                .mePresenterModule(new MePresenterModule(((IMeContract.View) (mFragmentList.get(size-1)))))
                .build().inject(this);*/
    }
}
