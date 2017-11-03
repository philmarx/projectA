package com.hzease.tomeet.home.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.home.IHomeContract;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by xuq on 2017/11/3.
 */

public class ChatRoomSetFragment extends BaseFragment implements IHomeContract.View {

    @BindView(R.id.rv_chatroomset_memberlist_fmt)
    RecyclerView rv_chatroomset_memberlist_fmt;
    @BindView(R.id.sv_chatroomset_isready_fmt)
    SwitchView sv_chatroomset_isready_fmt;

    FragmentTransaction transaction;
    HomeActivity homeActivity;

    private IHomeContract.Presenter mPresenter;
    private LinearLayoutManager linearLayoutManager;

    @OnClick({
            R.id.rl_chatroomset_tolookmemberlist_fmt
    })
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_chatroomset_tolookmemberlist_fmt:
                //TODO 查看群成员
                break;
        }
    }

    public static ChatRoomSetFragment newInstance(){
        return new ChatRoomSetFragment();
    }
    @Override
    public void setPresenter(IHomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_chatroomset;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        transaction = homeActivity.getSupportFragmentManager().beginTransaction();
        //设置rv为横向滑动
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_chatroomset_memberlist_fmt.setLayoutManager(linearLayoutManager);
    }


    //其他fragment中的代码
    @Override
    public void initRoomsList(boolean isSuccess, List<HomeRoomsBean.DataBean> date, boolean isLoadMore) {

    }

    @Override
    public void setAvatarAndNickname() {

    }

    @Override
    public void joinTheRoom(NoDataBean noDataBean, String roomId, String password) {

    }

    @Override
    public void changeLoadView(boolean isShown) {

    }

    @Override
    public void loadChatRoomInfo(GameChatRoomBean gameChatRoomBean) {

    }

    @Override
    public void exitSuccess() {

    }

}
