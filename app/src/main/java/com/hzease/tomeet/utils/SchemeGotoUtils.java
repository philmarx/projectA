package com.hzease.tomeet.utils;

import android.content.Context;
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

    public static void switchDestination(Uri uri, final Context context) {
        switch (uri.getQueryParameter("action")) {
            case "invited":
                // roomId
                final String roomId = uri.getQueryParameter("key1");
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
                                        context.startActivity(new Intent(context, GameChatRoomActivity.class).putExtra(AppConstants.TOMEET_ROOM_ID, roomId));
                                    } else {
                                        ToastUtils.getToast(noDataBean.getMsg());
                                    }
                                }
                            });
                } else {
                    // 如果用户没登录
                    ToastUtils.getToast("请先登陆后再加入房间");
                }
                break;
            case "friend":
                // userId
                String userId = uri.getQueryParameter("key1");
                String origin = uri.getQueryParameter("key2");
                Logger.e("key2: " + origin + "   手机：" + android.os.Build.BRAND + "  " + android.os.Build.MODEL);
                if (PTApplication.myInfomation != null && !TextUtils.isEmpty(origin)) {
                    PTApplication.getRequestService().becameFriend(PTApplication.userToken, PTApplication.userId, userId, origin)
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
                                        ToastUtils.getToast(noDataBean.getMsg());
                                }
                            });
                } else {
                    // 如果用户没登录
                    ToastUtils.getToast("请先登陆后再点此链接");
                }
                break;
        }
    }

}
