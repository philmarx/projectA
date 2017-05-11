package com.hzease.tomeet.me.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.me.IMeContract;
import com.hzease.tomeet.widget.SpacesItemDecoration;
import com.hzease.tomeet.widget.adapters.WaitEvaluateAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/5/11.
 */

public class GameEvaluateFragment extends BaseFragment implements IMeContract.View {


    @BindView(R.id.rv_gameevaluate_show_fmt)
    RecyclerView rv_gameevaluate_show_fmt;
    private IMeContract.Presenter mPresenter;
    /**
     * 创建底部导航栏对象
     */
    BottomNavigationView bottomNavigationView;

    public static GameEvaluateFragment newInstance() {
        return new GameEvaluateFragment();
    }

    @Override
    public void setPresenter(IMeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showMyInfo() {

    }

    @Override
    public void showMyRooms(HomeRoomsBean myJoinRoomBean, boolean isLoadMore) {

    }

    @Override
    public void updatePwdSuccess(boolean isSuccess, String msg) {

    }

    @Override
    public void feedBackSuccess(boolean isSuccess, String msg) {

    }

    @Override
    public void authorizedSuccess() {

    }

    @Override
    public void showFinishInfo(GameFinishBean.DataBean data) {

    }

    /**
     * 显示待评价成员
     * @param data
     */
    @Override
    public void showWaitEvaluateMember(List<WaitEvaluateBean.DataBean> data) {
        rv_gameevaluate_show_fmt.setAdapter(new WaitEvaluateAdapter(data,getContext()));
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_evaluate;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        long roomId = getArguments().getLong("roomId");
        rv_gameevaluate_show_fmt.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_gameevaluate_show_fmt.addItemDecoration(new SpacesItemDecoration(20));
        mPresenter.waitEvaluate(roomId, PTApplication.userToken,PTApplication.userId);
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        bottomNavigationView.setVisibility(View.GONE);
    }
}
