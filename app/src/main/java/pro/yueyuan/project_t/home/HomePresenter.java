package pro.yueyuan.project_t.home;

import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.data.HomeRoomsBean;
import pro.yueyuan.project_t.data.ShowGameListBean;
import pro.yueyuan.project_t.data.source.PTRepository;
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


    /**
     * 加载我的头像
     */
    @Override
    public void loadMyAvatar() {
        mHomeView.showMyAvatar();
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

                    }

                    @Override
                    public void onNext(ShowGameListBean showGameListBean) {
                        mHomeView.initGameList(showGameListBean.getData());
                    }
                });
    }

    @Override
    public void loadAllRooms(Integer gameId, String games, double latitude, double longitude, Integer page, Integer size, String sort, Integer state) {
        PTApplication.getRequestService().getRoomsByGameOrder(gameId,games,latitude,longitude,page,size,sort,state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeRoomsBean>() {
                    @Override
                    public void onCompleted() {
                        //
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(HomeRoomsBean homeRoomsBean) {
                        if (homeRoomsBean.isSuccess()){
                            mHomeView.initRoomsList(homeRoomsBean.getData());
                        }else{
                            Logger.e("连接失败");
                        }
                    }
                });
    }
}
