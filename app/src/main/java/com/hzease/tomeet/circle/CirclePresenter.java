package com.hzease.tomeet.circle;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.EnterCircleInfoBean;
import com.hzease.tomeet.data.JoinCircleBean;
import com.hzease.tomeet.data.NoDataBean;
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

public final class CirclePresenter implements ICircleContract.Presenter {

    private final PTRepository mPTRepository;

    private final ICircleContract.View mCircleView;

    @Inject
    public CirclePresenter(PTRepository mPTRepository, ICircleContract.View mCircleView) {
        this.mPTRepository = mPTRepository;
        this.mCircleView = mCircleView;
    }

    @Inject
    void setupListeners() {
        mCircleView.setPresenter(this);
    }

    @Override
    public void start() {
        // 我在onResume()里面调用了，可以写跟生命周期相关的东西
    }

    /**
     * @param city
     * @param latitude
     * @param longitude
     * @param name
     * @param notice
     * @param place
     * @param token
     * @param userId
     */
    @Override
    public void createCircle(String city, double latitude, double longitude, String name, String notice, String place, String token, String userId) {
        PTApplication.getRequestService().createCircle(city, latitude, longitude, name, notice, place, token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JoinCircleBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onERROR" + e.getMessage());
                    }

                    @Override
                    public void onNext(JoinCircleBean joinCircleBean) {
                        mCircleView.createSuccess(joinCircleBean);
                    }
                });
    }


    @Override
    public void findRecommand() {
        PTApplication.getRequestService().findRecommand()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CircleInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(CircleInfoBean circleInfoBean) {
                        Logger.e(circleInfoBean.getMsg() + "    : " + circleInfoBean.isSuccess());
                        if (circleInfoBean.isSuccess()) {
                            mCircleView.showRecommandCircle(circleInfoBean.getData());
                        }
                    }
                });
    }

    /**
     * 查看附近圈子
     *
     * @param latitude
     * @param longitude
     */
    @Override
    public void findNearBy(double latitude, double longitude) {
        PTApplication.getRequestService().findNearBy(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CircleInfoBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError");
                    }

                    @Override
                    public void onNext(CircleInfoBean circleInfoBean) {
                        Logger.e("onNext");
                        mCircleView.showNeayByCircle(circleInfoBean.getData());
                    }
                });
    }

    /**
     * 查看我的圈子
     *
     * @param page
     * @param size
     * @param token
     * @param userId
     */
    @Override
    public void findMyCircle(int page, int size, String token, String userId) {
        PTApplication.getRequestService().findMyCircle(page, size, token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CircleInfoBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(CircleInfoBean circleInfoBean) {
                        if (circleInfoBean.isSuccess()) {
                            mCircleView.showMyCircle(circleInfoBean.getData());
                        }
                    }
                });
    }

    /**
     * 获取圈子详情
     *
     * @param circleId
     * @param token
     * @param userId
     */
    @Override
    public void getCircleInfo(long circleId, String token, String userId) {
        PTApplication.getRequestService().getCircleInfo(circleId, token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EnterCircleInfoBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(EnterCircleInfoBean enterCircleInfoBean) {
                        Logger.e("" + enterCircleInfoBean.isSuccess());
                        if (enterCircleInfoBean.isSuccess()) {
                            mCircleView.showCircleInfo(enterCircleInfoBean.getData());
                        }
                    }
                });
    }

    /**
     * 加入圈子
     *
     * @param circleId
     * @param token
     * @param userId
     */
    @Override
    public void joinCircle(long circleId, String token, String userId) {
        PTApplication.getRequestService().joinCircle(circleId, token, userId)
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
                        Logger.e("" + noDataBean.isSuccess());
                        mCircleView.joinCircleSuccess(noDataBean.isSuccess(), noDataBean.getMsg());

                    }
                });
    }

    /**
     * 退出圈子
     *
     * @param circleId
     * @param token
     * @param userId
     */
    @Override
    public void signOutCircle(long circleId, String token, String userId) {
        PTApplication.getRequestService().signOutCircle(circleId, token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        if (noDataBean.isSuccess()) {
                            mCircleView.signOutCircleSuccess(noDataBean.getMsg());
                        }
                    }
                });
    }

    /**
     * 更改圈子公告
     *
     * @param circleId
     * @param notice
     * @param token
     * @param userId
     */
    @Override
    public void moditityNotice(long circleId, String notice, String token, String userId) {
        PTApplication.getRequestService().moditityNotice(circleId, notice, token, userId)
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
                        if (noDataBean.isSuccess()) {
                            mCircleView.modifitySuccess(noDataBean.getMsg());
                        }
                    }
                });
    }

    /**
     * 回复评论
     *
     * @param content     内容
     * @param declaration 该条喊话消息的ID
     * @param toUserId    回复谁的那个谁的ID
     */
    @Override
    public void commentWho(String content, final long declaration, long toUserId) {
        if (PTApplication.myInfomation != null) {
            PTApplication.getRequestService().commentCircleOfFriend(content, declaration, toUserId, PTApplication.userToken, PTApplication.myInfomation.getData().getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<NoDataBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            ToastUtils.getToast("评论失败！请检查网络");
                        }

                        @Override
                        public void onNext(NoDataBean noDataBean) {
                            if (noDataBean.isSuccess()) {
                                // 成功后刷新单条
                                PTApplication.getRequestService().getOneDeclaration(declaration)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<CommentItemBean>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                Logger.e(e.getMessage());
                                            }

                                            @Override
                                            public void onNext(CommentItemBean commentItemBean) {
                                                if (commentItemBean.isSuccess()) {
                                                    mCircleView.refreshOneDeclaration(commentItemBean.getData().get(0));
                                                }
                                            }
                                        });
                            } else {
                                ToastUtils.getToast(noDataBean.getMsg());
                            }
                        }
                    });
        } else {
            ToastUtils.getToast("请先登录！");
        }
    }

    @Override
    public void findmyDeclation(String token, String userId, int page, int size, final boolean isLoadMore) {
        PTApplication.getRequestService().findMyDeclaration(token,userId,page,size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentItemBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError： " + e.getMessage());
                        mCircleView.showDeclaration(false, null, false);
                    }

                    @Override
                    public void onNext(CommentItemBean commentItemBean) {
                        Logger.e(commentItemBean.getData().toString());
                        mCircleView.showDeclaration(commentItemBean.isSuccess(), commentItemBean.getData(), isLoadMore);
                    }
                });
    }

    /**
     * 获取喊话内容
     *
     * @param city
     * @param page
     * @param size
     */
    @Override
    public void getDeclaration(String city, Integer page, Integer size, final boolean isLoadMore) {
        PTApplication.getRequestService().getDeclaration(city, page, size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentItemBean>() {
                    @Override
                    public void onCompleted() {
                        //Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError： " + e.getMessage());
                        mCircleView.showDeclaration(false, null, false);
                    }

                    @Override
                    public void onNext(CommentItemBean commentItemBean) {
                        mCircleView.showDeclaration(commentItemBean.isSuccess(), commentItemBean.getData(), isLoadMore);
                    }
                });
    }

    /**
     * 创建喊话
     *
     * @param city
     * @param content
     * @param token
     * @param userId
     */
    @Override
    public void createDeclare(String city, String content, String token, String userId) {
        PTApplication.getRequestService().declare(city, content, token, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError");
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        Logger.e("onNext");
                        mCircleView.showDeclareSucccess(noDataBean.isSuccess(), noDataBean.getMsg());
                    }
                });
    }
}


