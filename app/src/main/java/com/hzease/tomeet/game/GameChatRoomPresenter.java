package com.hzease.tomeet.game;

import com.amap.api.location.AMapLocation;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.GameChatRoomBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.source.PTRepository;
import com.hzease.tomeet.utils.AMapLocUtils;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2017/5/4 14:20
 * email: MrKey.K@gmail.com
 * description:
 */

public class GameChatRoomPresenter implements IGameChatRoomContract.Presenter {

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
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 转圈
                        mView.changeLoadView(true);
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        // 关闭转圈
                        mView.changeLoadView(false);
                    }
                })
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
                            ToastUtils.getToast(PTApplication.getInstance(), gameChatRoomBean.getMsg());
                        }
                    }
                });
    }

    /**
     * 准备或取消
     *
     * @param amIReady 当前状态，去改变
     */
    @Override
    public void memberReadyOrCancel(boolean amIReady, String roomId) {
        // 先发送一个位置
        new AMapLocUtils().getLonLatAndSendLocation(roomId);
        if (amIReady) {
            PTApplication.getRequestService().gameCancelReady(PTApplication.userToken, PTApplication.userId, roomId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            // 转圈
                            mView.changeLoadView(true);
                        }
                    })
                    .doAfterTerminate(new Action0() {
                        @Override
                        public void call() {
                            // 关闭转圈
                            mView.changeLoadView(false);
                        }
                    })
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
                            Logger.e(noDataBean.toString());
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
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            // 转圈
                            mView.changeLoadView(true);
                        }
                    })
                    .doAfterTerminate(new Action0() {
                        @Override
                        public void call() {
                            // 关闭转圈
                            mView.changeLoadView(false);
                        }
                    })
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
                            Logger.e(noDataBean.toString());
                            if (noDataBean.isSuccess()) {
                                mView.changeReadyOrCancel();
                            } else {
                                ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                            }
                        }
                    });
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

    /**
     * 房主 准备或取消
     *
     * @param roomId
     * @param isBegin
     */
    @Override
    public void managerReadyOrCancel(String roomId, boolean isBegin) {
        // 先发送一个位置
        new AMapLocUtils().getLonLatAndSendLocation(roomId);
        if (isBegin) {
            PTApplication.getRequestService().managerCancelBegin(PTApplication.userToken, PTApplication.userId, roomId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            // 转圈
                            mView.changeLoadView(true);
                        }
                    })
                    .doAfterTerminate(new Action0() {
                        @Override
                        public void call() {
                            // 关闭转圈
                            mView.changeLoadView(false);
                        }
                    })
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
            PTApplication.getRequestService().startRoom(PTApplication.userToken, PTApplication.userId, roomId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            // 转圈
                            mView.changeLoadView(true);
                        }
                    })
                    .doAfterTerminate(new Action0() {
                        @Override
                        public void call() {
                            // 关闭转圈
                            mView.changeLoadView(false);
                        }
                    })
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
                                mView.changeReadyOrCancel();
                            } else {
                                ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                            }
                        }
                    });
        }
    }

    /**
     * 出发
     *
     * @param roomId 房间ID
     */
    @Override
    public void memberGo(final String roomId) {
        PTApplication.getRequestService().memberGo(PTApplication.userToken, PTApplication.userId, roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 转圈
                        mView.changeLoadView(true);
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        // 关闭转圈
                        mView.changeLoadView(false);
                    }
                })
                .subscribe();
        new AMapLocUtils().getLonLatAndSendLocation(roomId);
    }

    /**
     * 签到  并 发送位置
     *
     * @param roomId 房间ID
     */
    @Override
    public void checkSendLocation(final String roomId) {
        new AMapLocUtils().getLonLat(PTApplication.getInstance(), new AMapLocUtils.LonLatListener() {
            @Override
            public void getLonLat(AMapLocation aMapLocation) {
                PTApplication.myLongitude = aMapLocation.getLongitude();
                PTApplication.myLatitude = aMapLocation.getLatitude();
                PTApplication.getRequestService().roomCheck(PTApplication.myLatitude, PTApplication.myLongitude, Long.valueOf(roomId), PTApplication.userToken, PTApplication.myInfomation.getData().getId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                // 转圈
                                mView.changeLoadView(true);
                            }
                        })
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                // 关闭转圈
                                mView.changeLoadView(false);
                            }
                        })
                        .subscribe(new Subscriber<NoDataBean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e(e.getMessage());
                            }

                            @Override
                            public void onNext(NoDataBean noDataBean) {
                                Logger.e(noDataBean.toString());
                                mView.changeCheckButton(noDataBean);
                            }
                        });
            }
        });
    }

    /**
     * 点击我没迟到
     *
     * @param roomId 房间ID
     */
    @Override
    public void iAmNotLate(final String roomId) {
        PTApplication.getRequestService().iAmNotLate(PTApplication.userToken, PTApplication.userId, roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 转圈
                        mView.changeLoadView(true);
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        // 关闭转圈
                        mView.changeLoadView(false);
                    }
                })
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        Logger.e(noDataBean.toString());
                        if (noDataBean.isSuccess()) {
                            ToastUtils.getToast(PTApplication.getInstance(), "投诉成功");
                            new AMapLocUtils().getLonLatAndSendLocation(roomId);
                        } else {
                            ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                        }
                    }
                });
    }

    /**
     * 设置在线状态
     *
     * @param isOnline true 在线 false 离线
     */
    @Override
    public void setOnline(boolean isOnline, String roomId) {
        PTApplication.getRequestService().setOnline(isOnline, roomId, PTApplication.userId, PTApplication.userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        Logger.e("setOnline: " + noDataBean.toString());
                    }
                });
    }
}
