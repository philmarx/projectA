package com.hzease.tomeet.utils;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.game.ui.GameChatRoomActivity;
import com.orhanobut.logger.Logger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2017/7/11 13:06
 * email: MrKey.K@gmail.com
 * description:
 */

public class SchemeGotoUtils {

    public void switchDestination(Uri uri) {
        switch (uri.getHost()) {
            case "invited":
                // roomId
                final String roomId = uri.getQueryParameter("roomId");
                if (PTApplication.myInfomation != null) {
                    PTApplication.getRequestService().joinRoom(PTApplication.userToken, PTApplication.userId, roomId, AppConstants.TOMEET_EVERY_ROOM_PASSWORD)
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
                                    Logger.e(noDataBean.toString());
                                    if (noDataBean.isSuccess()) {
                                        PTApplication.getInstance().startActivity(new Intent(PTApplication.getInstance(), GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, roomId));
                                    } else {
                                        ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                                    }
                                }
                            });
                } else {
                    // 如果用户没登录
                    ToastUtils.getToast(PTApplication.getInstance(), "请先登陆后再加入房间");
                }
                break;
            case "share":
                // userId
                final String userId = uri.getQueryParameter("userId");
                if (PTApplication.myInfomation != null) {
                    PTApplication.getRequestService().becameFriend(PTApplication.userToken, PTApplication.userId, userId)
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
                                    Logger.e(noDataBean.toString());
                                    if (!TextUtils.isEmpty(noDataBean.getMsg()))
                                        ToastUtils.getToast(PTApplication.getInstance(), noDataBean.getMsg());
                                }
                            });
                } else {
                    // 如果用户没登录
                    ToastUtils.getToast(PTApplication.getInstance(), "请先登陆后再点此链接");
                }
                break;
        }
    }

}
