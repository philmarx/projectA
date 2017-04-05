package pro.yueyuan.project_t.ranking.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.RankingBean;
import pro.yueyuan.project_t.ranking.IRankContract;
import pro.yueyuan.project_t.widget.adapters.MainListAdapter;
import pro.yueyuan.project_t.widget.adapters.MoreListAdapter;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/4/5.
 */
public class RankingFragment extends BaseFragment implements IRankContract.View {
    @BindView(R.id.ranking_mainlist)
    ListView ranking_mainlist;
    @BindView(R.id.ranking_morelist)
    ListView ranking_morelist;

    public List<RankingBean.DataBean> list = new ArrayList<>();
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IRankContract.Presenter mPresenter;

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.start();
    }

    public static RankingFragment newInstance() {
        return new RankingFragment();
    }


    @Override
    public void setPresenter(IRankContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_ranking;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mPresenter.getRankingOrder(28);
        ranking_mainlist.setAdapter(new MainListAdapter());

    }



    @Override
    public void reflush(RankingBean rankingBean) {
        list = rankingBean.getData();
        ranking_morelist.setAdapter(new MoreListAdapter((ArrayList<RankingBean.DataBean>) list));
    }
}
