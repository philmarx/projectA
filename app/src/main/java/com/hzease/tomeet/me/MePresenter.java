package com.hzease.tomeet.me;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.FeedBackBean;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.MyJoinRoomsBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.PropsMumBean;
import com.hzease.tomeet.data.UpdatePwdBean;
import com.hzease.tomeet.data.UserInfoBean;
import com.hzease.tomeet.data.WaitEvaluateBean;
import com.hzease.tomeet.data.source.PTRepository;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import javax.inject.Inject;

import io.realm.Realm;
import io.rong.imkit.RongIM;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2016/11/25 01:15
 * email: MrKey.K@gmail.com
 * description:
 */

public final class MePresenter implements IMeContract.Presenter {

    private final PTRepository mPTRepository;

    private final IMeContract.View mMeView;

    @Inject
    public MePresenter(PTRepository mPTRepository, IMeContract.View mMeView) {
        this.mPTRepository = mPTRepository;
        this.mMeView = mMeView;
    }

    @Inject
    void setupListeners() {
        mMeView.setPresenter(this);
    }

    @Override
    public void start() {
        // 我在onResume()里面调用了，可以写跟生命周期相关的东西
        loadMyInfo();
    }

    /**
     * 加载我的账户信息
     */
    @Override
    public void loadMyInfo() {
        PTApplication.getRequestService().getMyInfomation(PTApplication.userToken, PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        if (userInfoBean.isSuccess()) {
                            PTApplication.myInfomation = userInfoBean;
                            mMeView.showMyInfo();
                        } else {
                            ToastUtils.getToast(PTApplication.getInstance(), userInfoBean.getMsg());
                        }
                    }
                });
    }


    /**
     * 注销用户
     */
    @Override
    public void logoutUser() {
        PTApplication.userId = "";
        PTApplication.userToken = "";
        // 注销个人信息
        PTApplication.myInfomation = null;
        // 清空本地保存
        mPTRepository.saveUserIdAndToken();
        // 注销融云
        if (PTApplication.isRongCloudInit) {
            RongIM.getInstance().logout();
            PTApplication.isRongCloudInit = false;
        }
        Realm.removeDefaultConfiguration();
        // 注销阿里云OSS
        PTApplication.aliyunOss = null;
        PTApplication.aliyunOssExpiration = 0;
       // 停止发送友盟用户信息
        MobclickAgent.onProfileSignOff();

        // 移除未读监听
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(PTApplication.unReadMessageObserver);
        PTApplication.badge.setBadgeNumber(0);
    }

    @Override
    public void getMyJoinRooms(Integer page, Integer size, String token, String userId, final boolean isLoadMore) {
        PTApplication.getRequestService().getMyRooms(page,size,token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MyJoinRoomsBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("getMyJoinRooms - onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(MyJoinRoomsBean myJionRoomBean) {
                        //Logger.e("getMyJoinRooms....onNext:::  " + myJionRoomBean.isSuccess());
                        if (myJionRoomBean.isSuccess()){
                            mMeView.showMyRooms(myJionRoomBean,isLoadMore);
                        }
                    }
                });
    }
    @Override
    public void updatePwd(String password, String password2, String token, String userId) {
        PTApplication.getRequestService().updatePwd(password,password2,token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdatePwdBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        mMeView.updatePwdSuccess(false,"网络连接失败");
                    }

                    @Override
                    public void onNext(UpdatePwdBean updatePwdBean) {
                        Logger.e("onNext");
                        if (updatePwdBean.isSuccess()){
                            Logger.e(""+updatePwdBean.isSuccess());
                            mMeView.updatePwdSuccess(true,updatePwdBean.getMsg());
                            Logger.e(updatePwdBean.getMsg());
                        }else{
                            mMeView.updatePwdSuccess(false,updatePwdBean.getMsg());
                        }
                    }
                });
    }

    /**
     * 提交反馈
     *
     * @param content
     * @param token
     * @param userId
     */
    @Override
    public void feedBack(String content, String token, String userId) {
        PTApplication.getRequestService().feedBack(content,token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<FeedBackBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mMeView.feedBackSuccess(false,"网络连接失败");
                    }

                    @Override
                    public void onNext(FeedBackBean feedBackBean) {
                        if (feedBackBean.isSuccess()){
                            mMeView.feedBackSuccess(true,feedBackBean.getMsg());
                        }else {
                            mMeView.feedBackSuccess(false,feedBackBean.getMsg());
                        }
                    }
                });
    }

    /**
     * 实名认证
     * @param idCard
     * @param realName
     * @param token
     * @param userId
     */
    @Override
    public void authorized(String idCard, String realName, String token, String userId) {
        PTApplication.getRequestService().authorized(idCard,realName,token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        if (noDataBean.isSuccess()){
                            mMeView.authorizedSuccess();
                        }
                    }
                });
    }

    @Override
    public void gameFinish(long roomId) {
        PTApplication.getRequestService().gameFinishInfo(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GameFinishBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GameFinishBean gameFinishBean) {
                        if (gameFinishBean.isSuccess()){
                            mMeView.showFinishInfo(gameFinishBean.getData());
                        }else{
                            Logger.e(gameFinishBean.getMsg());
                        }

                    }
                });
    }

    @Override
    public void waitEvaluate(long roomId, String token, String userId) {
        PTApplication.getRequestService().findRoomFriends(roomId,token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WaitEvaluateBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(WaitEvaluateBean waitEvaluateBean) {
                        Logger.e(waitEvaluateBean.isSuccess()+"");
                        if (waitEvaluateBean.isSuccess()){
                            mMeView.showWaitEvaluateMember(waitEvaluateBean.getData());
                            waitEvaluateBean.getData().toString();
                        }
                    }
                });
    }

    /**
     * 查看道具数量
     *
     * @param token
     * @param userId
     */
    @Override
    public void findPropsMum(String token, String userId) {
        PTApplication.getRequestService().findPropsMum(token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PropsMumBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(PropsMumBean propsMumBean) {
                        if (propsMumBean.isSuccess()){
                            mMeView.showPropsMum(propsMumBean.getData());
                        }
                    }
                });
    }

    /**
     * 修改昵称
     *
     * @param newNickName
     * @param token
     * @param userId
     */
    @Override
    public void changeNickName(String newNickName, String token, String userId) {
        PTApplication.getRequestService().changeName(newNickName,token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        if (noDataBean.isSuccess()){
                            mMeView.showChangeNameSuccess();
                        }
                    }
                });
    }

    /**
     * 购买道具
     * @param count
     * @param token
     * @param type
     * @param userId
     */
    @Override
    public void buyProps(final int index, Integer count, String token, Integer type, String userId) {
        PTApplication.getRequestService().buyProp(count,token,type,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                            mMeView.showBuyPropsResult(index,noDataBean.isSuccess(),noDataBean.getMsg());
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
