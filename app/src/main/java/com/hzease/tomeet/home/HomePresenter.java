package com.hzease.tomeet.home;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.HomeRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.ShowGameListBean;
import com.hzease.tomeet.data.source.PTRepository;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2016/11/25 01:15
 * email: MrKey.K@gmail.com
 * description:
 */

public final class HomePresenter implements IHomeContract.Presenter {

    private final PTRepository mPTRepository;

    private final IHomeContract.View mHomeView;

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
    public void loadGameList(String key, String value) {
        PTApplication.getRequestService().getGameList(key,value)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ShowGameListBean>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(ShowGameListBean showGameListBean) {
                        mHomeView.initGameList(showGameListBean.getData());
                    }
                });
    }

    @Override
    public void loadAllRooms(String city, Integer gameId, String games, double latitude, double longitude, Integer page, Integer size, String sort, Integer state, final boolean isLoadmore) {
        PTApplication.getRequestService().getRoomsByGameOrder(city,gameId,games,latitude,longitude,page,size,sort,state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeRoomsBean>() {
                    @Override
                    public void onCompleted() {
                        //Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(HomeRoomsBean homeRoomsBean) {
                        if (homeRoomsBean.isSuccess()){
                            mHomeView.initRoomsList(homeRoomsBean.getData(),isLoadmore);
                        }else{
                            Logger.e("连接失败");
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
    public void canIJoinTheRoom(final String roomId , final String password) {
        PTApplication.getRequestService().joinRoom(PTApplication.userToken, PTApplication.userId, roomId, password)
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
                        Logger.w(noDataBean.toString());
                        if (noDataBean.isSuccess()) {
                            mHomeView.joinTheRoom(roomId, password);
                        } else {
                            ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                        }
                    }
                });
    }

}
