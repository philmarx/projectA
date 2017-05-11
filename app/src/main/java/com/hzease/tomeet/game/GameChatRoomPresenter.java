package com.hzease.tomeet.game;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.GameChatRoomBean;
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
                        Logger.d("离开房间: " + noDataBean.toString());
                        if (!noDataBean.isSuccess()) {
                            ToastUtils.getToast(PTApplication.getInstance(), "离开房间失败：" + noDataBean.getMsg());
                        }
                    }
                });
    }

    /**
     * 获取房间数据
     *
     * @param roomId
     */
    @Override
    public void getGameChatRoomInfo(String roomId) {
        PTApplication.getRequestService().getGameChatRoomInfo(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GameChatRoomBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("error: " + e.getMessage());
                    }

                    @Override
                    public void onNext(GameChatRoomBean gameChatRoomBean) {
                        if (gameChatRoomBean.isSuccess()) {
                            mView.refreshGameChatRoomInfo(gameChatRoomBean);
                        } else {
                            Logger.e(gameChatRoomBean.getMsg());
                            ToastUtils.getToast(PTApplication.getInstance(), "加入房间失败，请退出一下试试");
                        }
                    }
                });
    }

    /**
     * 准备或取消
     *
     * @param amIReady 当前状态，去改变
     * @param amIManager
     */
    @Override
    public void ReadyOrCancel(boolean amIReady, String roomId, boolean amIManager) {
        if (amIManager) {
            PTApplication.getRequestService().startRoom(PTApplication.userToken, PTApplication.userId, roomId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NoDataBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e(e.getMessage());
                            ToastUtils.getToast(PTApplication.getInstance(), "开始失败，请重试");
                        }

                        @Override
                        public void onNext(NoDataBean noDataBean) {
                            Logger.w(noDataBean.toString());
                            if (noDataBean.isSuccess()) {
                                // // TODO: 2017/5/11 如果成功，做点什么
                            } else {
                                ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                            }
                        }
                    });
        } else {
            if (amIReady) {
                PTApplication.getRequestService().gameCancelReady(PTApplication.userToken, PTApplication.userId, roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                                ToastUtils.getToast(PTApplication.getInstance(), "取消失败，请重试");
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()) {
                                    mView.changeReadyOrCancel();
                                } else {
                                    ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                                }
                            }
                        });
            } else {
                PTApplication.getRequestService().gameReady(PTApplication.userToken, PTApplication.userId, roomId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                                ToastUtils.getToast(PTApplication.getInstance(), "准备失败，请重试");
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                if (noDataBean.isSuccess()) {
                                    mView.changeReadyOrCancel();
                                } else {
                                    ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                                }
                            }
                        });
            }
        }
    }

    /**
     * 退出房间
     *
     * @param roomId
     */
    @Override
    public void exitRoom(String roomId) {
        PTApplication.getRequestService().exitRoom(PTApplication.userToken, PTApplication.userId, roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        ToastUtils.getToast(PTApplication.getInstance(), "退出房间失败，请重试");
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        Logger.w("退出房间: " + noDataBean.toString());
                        if (!noDataBean.isSuccess()) {
                            ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                        }
                    }
                });
    }
}
