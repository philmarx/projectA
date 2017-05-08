package com.hzease.tomeet.game;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.source.PTRepository;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2017/5/4 14:20
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomPresenter implements IGameChatRoomContract.Presenter{

    /**
     * 仓库
     */
    private final PTRepository mPTRepository;
    private final IGameChatRoomContract.View mView;

    @Inject
    public GameChatRoomPresenter(PTRepository mPTRepository, IGameChatRoomContract.View mView) {
        this.mPTRepository = mPTRepository;
        this.mView = mView;
    }

    @Inject
    void setupListeners() {
        mView.setPresenter(this);
    }
    @Override
    public void start() {

    }

    /**
     * 离开房间,不退出
     *
     * @param roomId 聊天室ID
     */
    @Override
    public void leaveRoom(String roomId) {
        PTApplication.getRequestService().leaveRoom(PTApplication.userToken, PTApplication.userId, roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        Logger.d(noDataBean.toString());
                        if (!noDataBean.isSuccess()) {
                            ToastUtils.getToast(PTApplication.getInstance(), "离开房间失败：" + noDataBean.getMsg());
                        }
                    }
                });
    }
}
