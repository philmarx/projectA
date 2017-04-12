package pro.yueyuan.project_t.ranking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.PersonOrderInfoActivity;
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
    public BottomNavigationView bottomNavigationView;
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
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
        ranking_mainlist.setAdapter(new MainListAdapter());
        ranking_mainlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.getRankingOrder(6);
            }
        });

    }



    @Override
    public void reflush(RankingBean rankingBean) {
        list = rankingBean.getData();
        ranking_morelist.setAdapter(new MoreListAdapter((ArrayList<RankingBean.DataBean>) list));
        ranking_morelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long userId = list.get(position).getUserId();
                Intent intent = new Intent(getActivity(), PersonOrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("userId",userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


}
