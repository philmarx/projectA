package com.hzease.tomeet.home.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.home.IHomeContract;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.ChatRoomSetMembersAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * 已经修改为activity。此fragment废弃
 */

public class ChatRoomSetFragment extends BaseFragment implements IHomeContract.View {

    @BindView(R.id.rv_chatroomset_memberlist_fmt)
    RecyclerView rv_chatroomset_memberlist_fmt;
    @BindView(R.id.sv_chatroomset_isready_fmt)
    SwitchView sv_chatroomset_isready_fmt;
    @BindView(R.id.tv_chatroom_set_fmt)
    TextView tv_chatroom_set_fmt;

    FragmentTransaction transaction;
    HomeActivity homeActivity;

    private IHomeContract.Presenter mPresenter;
    private LinearLayoutManager linearLayoutManager;

    @OnClick({
            R.id.rl_chatroomset_tolookmemberlist_fmt,
            R.id.iv_back_chatroom_set_fmt
    })
    public void onClick(View view){
        switch (view.getId()){
            case R.id.rl_chatroomset_tolookmemberlist_fmt:
                //TODO 查看群成员
                break;
            case R.id.iv_back_chatroom_set_fmt:
                getActivity().onBackPressed();
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
        String gameChatRoomBean = getArguments().getString("gameChatRoomBean");
        if (!TextUtils.isEmpty(gameChatRoomBean)) {
            final GameChatRoomBean chatRoomBean = new Gson().fromJson(gameChatRoomBean, GameChatRoomBean.class);
            if (chatRoomBean.isSuccess()) {
                tv_chatroom_set_fmt.setText(chatRoomBean.getData().getName());
                List<GameChatRoomBean.DataBean.JoinMembersBean> joinMembers = chatRoomBean.getData().getJoinMembers();
                for (GameChatRoomBean.DataBean.JoinMembersBean joinMember : joinMembers) {
                    if (joinMember.getId() == PTApplication.myInfomation.getData().getId()) {
                        sv_chatroomset_isready_fmt.setOpened(joinMember.isReady());
                        break;
                    }
                }
                sv_chatroomset_isready_fmt.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
                    @Override
                    public void toggleToOn(SwitchView view) {
                        ToastUtils.getToast("to On");
                        /*PTApplication.getRequestService().gameReady(PTApplication.userToken, PTApplication.userId, String.valueOf(chatRoomBean.getData().getId()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e(e.getMessage());
                                        ToastUtils.getToast("准备失败，请重试");
                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        Logger.e(noDataBean.toString());
                                        if (!noDataBean.isSuccess()) {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                            sv_chatroomset_isready_fmt.setOpened(false);
                                        }
                                    }
                                });*/
                    }

                    @Override
                    public void toggleToOff(SwitchView view) {
                        ToastUtils.getToast("to Off");
                        /*PTApplication.getRequestService().gameCancelReady(PTApplication.userToken, PTApplication.userId, String.valueOf(chatRoomBean.getData().getId()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e(e.getMessage());
                                        ToastUtils.getToast("取消失败，请重试");
                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        Logger.e(noDataBean.toString());
                                        if (!noDataBean.isSuccess()) {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                            sv_chatroomset_isready_fmt.setOpened(true);
                                        }
                                    }
                                });*/
                    }
                });
            }
            //设置rv为横向滑动
            linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_chatroomset_memberlist_fmt.setLayoutManager(linearLayoutManager);
            ChatRoomSetMembersAdapter membersAdapter = new ChatRoomSetMembersAdapter(mContext, chatRoomBean.getData(), getActivity());
            rv_chatroomset_memberlist_fmt.setAdapter(membersAdapter);
        } else {
            ToastUtils.getToast("获取数据失败");
        }
        homeActivity = (HomeActivity) getActivity();
        transaction = homeActivity.getSupportFragmentManager().beginTransaction();
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

}
