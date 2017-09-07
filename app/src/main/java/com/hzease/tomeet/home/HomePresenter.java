package com.hzease.tomeet.home;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.source.PTRepository;
import com.hzease.tomeet.home.ui.HomeActivity;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2016/11/25 01:15
 * email: MrKey.K@gmail.com
 * description:
 */

public final class HomePresenter implements IHomeContract.Presenter {

    private final PTRepository mPTRepository;

    private final IHomeContract.View mHomeView;
    private boolean sync;
    private boolean loadAllRoomsError;

    @Inject
    public HomePresenter(PTRepository mPTRepository, IHomeContract.View mHomeView) {
        this.mPTRepository = mPTRepository;
        this.mHomeView = mHomeView;
    }

    @Inject
    void setupListeners() {
        mHomeView.setPresenter(this);
    }

    @Override
    public void start() {
        // 我在onResume()里面调用了，可以写跟生命周期相关的东西
    }

    @Override
    public void loadAllRooms(String city, String gameId, String games, double latitude, double longitude, Integer page, Integer size, String sort, Integer state, final boolean isLoadmore) {
        sync = false;
        loadAllRoomsError = false;
        final List<HomeRoomsBean.DataBean> data = new ArrayList<>();
        if (PTApplication.myInfomation == null || page != 0) {
            sync = true;
        }

        // 我已加入的房间
        if (PTApplication.myInfomation != null && page == 0) {
            PTApplication.getRequestService().findMyRunningRooms(PTApplication.userToken, PTApplication.userId, gameId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<HomeRoomsBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e("onError: " + e.getMessage());
                            if (!loadAllRoomsError) {
                                loadAllRoomsError = true;
                                mHomeView.initRoomsList(false, null, false);
                            }
                        }

                        @Override
                        public void onNext(HomeRoomsBean homeRoomsBean) {
                            if (!loadAllRoomsError) {
                                if (homeRoomsBean.isSuccess()) {
                                    data.addAll(0, homeRoomsBean.getData());
                                    if (sync) {
                                        mHomeView.initRoomsList(homeRoomsBean.isSuccess(), data, isLoadmore);
                                    } else {
                                        sync = true;
                                    }
                                } else {
                                    loadAllRoomsError = true;
                                    mHomeView.initRoomsList(false, null, false);
                                    ToastUtils.getToast("数据加载失败，请重试");
                                    Logger.e("已加入 - 房间加载失败：" + homeRoomsBean.getMsg());
                                }
                            }
                        }
                    });
        }

        // 大厅房间
        PTApplication.getRequestService().getRoomsByGameOrder(city, gameId, games, latitude, longitude, page, size, sort, state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeRoomsBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError: " + e.getMessage());
                        if (!loadAllRoomsError) {
                            loadAllRoomsError = true;
                            mHomeView.initRoomsList(false, null, false);
                        }
                    }

                    @Override
                    public void onNext(HomeRoomsBean homeRoomsBean) {
                        if (!loadAllRoomsError) {
                            if (homeRoomsBean.isSuccess()) {
                                data.addAll(homeRoomsBean.getData());
                                if (sync) {
                                    mHomeView.initRoomsList(homeRoomsBean.isSuccess(), data, isLoadmore);
                                } else {
                                    sync = true;
                                }
                            } else {
                                loadAllRoomsError = true;
                                mHomeView.initRoomsList(false, null, false);
                                ToastUtils.getToast("数据加载失败，请重试");
                                Logger.e("大厅 - 房间加载失败：" + homeRoomsBean.getMsg());
                            }
                        }
                    }
                });
    }

    /**
     * 检查是否可以加入房间
     *
     * @param roomId
     */
    @Override
    public void canIJoinTheRoom(final String roomId, final String password) {
        PTApplication.getRequestService().joinRoom(PTApplication.userToken, PTApplication.userId, roomId, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 转圈
                        mHomeView.changeLoadView(true);
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        // 关闭转圈
                        mHomeView.changeLoadView(false);
                    }
                })
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
                        mHomeView.joinTheRoom(noDataBean,roomId, password);
                    }
                });
    }

}
