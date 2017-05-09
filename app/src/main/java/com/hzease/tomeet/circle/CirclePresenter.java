package com.hzease.tomeet.circle;

import android.view.View;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.CircleInfoBean;
import com.hzease.tomeet.data.CommentConfig;
import com.hzease.tomeet.data.CommentItemBean;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.data.UpdatePwdBean;
import com.hzease.tomeet.data.source.PTRepository;
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
     * 创建圈子
     *
     * @param avatarSignature
     * @param bgSignature
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
    public void createCircle(String avatarSignature, String bgSignature, String city, double latitude, double longitude, String name, String notice, String place, String token, String userId) {
        PTApplication.getRequestService().createCircle(avatarSignature,bgSignature,city,latitude,longitude,name,notice,place,token,userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdatePwdBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UpdatePwdBean updatePwdBean) {
                        if (updatePwdBean.isSuccess()){
                            mCircleView.createSuccess();
                        }
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
        PTApplication.getRequestService().getDeclaration(city,page,size)
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
                    }

                    @Override
                    public void onNext(CommentItemBean commentItemBean) {
                        Logger.e("onNext: " + commentItemBean.getMsg());
                        if (commentItemBean.isSuccess()){
                            mCircleView.showDeclaration(commentItemBean,isLoadMore);
                        }
                    }
                });
    }

    @Override
    public void showEditTextBody(CommentConfig config) {
        if(mCircleView != null){
            mCircleView.updateEditTextBodyVisible(View.VISIBLE, config);
        }
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
        PTApplication.getRequestService().declare(city,content,token,userId)
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
                        mCircleView.showDeclareSucccess(noDataBean.isSuccess(),noDataBean.getMsg());
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
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(CircleInfoBean circleInfoBean) {
                        Logger.e("onNext");
                        if (circleInfoBean.isSuccess()){
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
        PTApplication.getRequestService().findNearBy(latitude,longitude)
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
}
