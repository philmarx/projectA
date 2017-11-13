package com.hzease.tomeet.game.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.ChatRoomSetMembersAdapter;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2017/11/10 17:54
 * email: MrKey.K@gmail.com
 * description:
 */

public class ChatRoomSetActivity extends NetActivity {

    @BindView(R.id.rv_chatroomset_memberlist_fmt)
    RecyclerView rv_chatroomset_memberlist_fmt;
    @BindView(R.id.sv_chatroomset_isready_fmt)
    SwitchView sv_chatroomset_isready_fmt;
    @BindView(R.id.tv_chatroom_set_fmt)
    TextView tv_chatroom_set_fmt;

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
                onBackPressed();
                break;
        }
    }

    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.fragment_chatroomset;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        String gameChatRoomBean = getIntent().getStringExtra("gameChatRoomBean");
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
                    public void toggleToOn(final SwitchView view) {
                        PTApplication.getRequestService().gameReady(PTApplication.userToken, PTApplication.userId, String.valueOf(chatRoomBean.getData().getId()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        // 转圈
                                        showLoadingDialog();
                                    }
                                })
                                .doAfterTerminate(new Action0() {
                                    @Override
                                    public void call() {
                                        // 关闭转圈
                                        hideLoadingDialog();
                                    }
                                })
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e(e.getMessage());
                                        ToastUtils.getToast("准备失败，请重试");
                                        view.setOpened(false);
                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        Logger.e(noDataBean.toString());
                                        if (!noDataBean.isSuccess()) {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                            view.setOpened(false);
                                        } else {
                                            view.setOpened(true);
                                        }
                                    }
                                });
                    }

                    @Override
                    public void toggleToOff(final SwitchView view) {
                        PTApplication.getRequestService().gameCancelReady(PTApplication.userToken, PTApplication.userId, String.valueOf(chatRoomBean.getData().getId()))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(new Action0() {
                                    @Override
                                    public void call() {
                                        // 转圈
                                        showLoadingDialog();
                                    }
                                })
                                .doAfterTerminate(new Action0() {
                                    @Override
                                    public void call() {
                                        // 关闭转圈
                                        hideLoadingDialog();
                                    }
                                })
                                .subscribe(new Subscriber<NoDataBean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Logger.e(e.getMessage());
                                        ToastUtils.getToast("取消失败，请重试");
                                        view.setOpened(true);
                                    }

                                    @Override
                                    public void onNext(NoDataBean noDataBean) {
                                        Logger.e(noDataBean.toString());
                                        if (!noDataBean.isSuccess()) {
                                            ToastUtils.getToast(noDataBean.getMsg());
                                            view.setOpened(true);
                                        } else {
                                            view.setOpened(false);
                                        }
                                    }
                                });
                    }
                });
            }
            //设置rv为横向滑动
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rv_chatroomset_memberlist_fmt.setLayoutManager(linearLayoutManager);
            ChatRoomSetMembersAdapter membersAdapter = new ChatRoomSetMembersAdapter(this, chatRoomBean.getData(), this);
            rv_chatroomset_memberlist_fmt.setAdapter(membersAdapter);
        } else {
            ToastUtils.getToast("获取数据失败");
        }
    }
}
