package com.hzease.tomeet.ranking.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.RankingBean;
import com.hzease.tomeet.ranking.IRankContract;
import com.hzease.tomeet.widget.adapters.MainListAdapter;
import com.hzease.tomeet.widget.adapters.MoreListAdapter;

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
